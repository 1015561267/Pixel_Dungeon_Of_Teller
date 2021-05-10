package com.teller.pixeldungeonofteller.ui;

import com.teller.pixeldungeonofteller.scenes.PixelScene;
import com.watabou.noosa.Game;
import com.watabou.noosa.RenderedText;
import com.watabou.noosa.ui.Component;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class RenderedTextBlock extends Component {

    private int maxWidth = Integer.MAX_VALUE;
    public int nLines;

    private static final RenderedText SPACE = new RenderedText();
    private static final RenderedText NEWLINE = new RenderedText();

    protected String text;
    protected String[] tokens = null;
    protected ArrayList<RenderedText> words = new ArrayList<>();
    protected boolean multiline = false;

    private int size;
    private float zoom;
    private int color = -1;

    private int hightlightColor = Window.TITLE_COLOR;
    private boolean highlightingEnabled = true;

    public static final int LEFT_ALIGN = 1;
    public static final int CENTER_ALIGN = 2;
    public static final int RIGHT_ALIGN = 3;
    private int alignment = LEFT_ALIGN;

    public RenderedTextBlock(int size) {
        this.size = size;
    }

    public RenderedTextBlock(String text, int size) {
        this.size = size;
        text(text);
    }

    //I have no ambition for multi-platfrom yet,so just copy some code from evan to make his new cool badges usable
    private Pattern regularsplitter = Pattern.compile(
            "(?<=\n)|(?=\n)|(?<=_)|(?=_)|" +
                    "(?<=\\p{InHiragana})|(?=\\p{InHiragana})|" +
                    "(?<=\\p{InKatakana})|(?=\\p{InKatakana})|" +
                    "(?<=\\p{InCJK_Unified_Ideographs})|(?=\\p{InCJK_Unified_Ideographs})|" +
                    "(?<=\\p{InCJK_Symbols_and_Punctuation})|(?=\\p{InCJK_Symbols_and_Punctuation})|" +
                    "(?<=\\p{InHalfwidth_and_Fullwidth_Forms})|(?=\\p{InHalfwidth_and_Fullwidth_Forms})");

    //additionally splits on words, so that each word can be arranged individually
    private Pattern regularsplitterMultiline = Pattern.compile(
            "(?<= )|(?= )|(?<=\n)|(?=\n)|(?<=_)|(?=_)|" +
                    "(?<=\\p{InHiragana})|(?=\\p{InHiragana})|" +
                    "(?<=\\p{InKatakana})|(?=\\p{InKatakana})|" +
                    "(?<=\\p{InCJK_Unified_Ideographs})|(?=\\p{InCJK_Unified_Ideographs})|" +
                    "(?<=\\p{InCJK_Symbols_and_Punctuation})|(?=\\p{InCJK_Symbols_and_Punctuation})|" +
                    "(?<=\\p{InHalfwidth_and_Fullwidth_Forms})|(?=\\p{InHalfwidth_and_Fullwidth_Forms})");

    public String[] splitforTextBlock(String text, boolean multiline) {
        if (multiline) {
            return regularsplitterMultiline.split(text);
        } else {
            return regularsplitter.split(text);
        }
    }

    public void text(String text) {
        this.text = text;

        if (text != null && !text.equals("")) {

            tokens = splitforTextBlock(text, multiline);

            build();
        }
    }

    public void text(String text, int maxWidth) {
        this.maxWidth = maxWidth;
        multiline = true;
        text(text);
    }

    public String text() {
        return text;
    }

    public void maxWidth(int maxWidth) {
        if (this.maxWidth != maxWidth) {
            this.maxWidth = maxWidth;
            multiline = true;
            text(text);
        }
    }

    public int maxWidth() {
        return maxWidth;
    }

    private synchronized void build() {
        if (tokens == null) return;

        clear();
        words = new ArrayList<>();
        boolean highlighting = false;
        for (String str : tokens) {

            if (str.equals("_") && highlightingEnabled) {
                highlighting = !highlighting;
            } else if (str.equals("\n")) {
                words.add(NEWLINE);
            } else if (str.equals(" ")) {
                words.add(SPACE);
            } else {
                RenderedText word = new RenderedText(str, size);

                if (highlighting) word.hardlight(hightlightColor);
                else if (color != -1) word.hardlight(color);
                word.scale.set(zoom);

                words.add(word);
                add(word);

                if (height < word.height()) height = word.height();
            }
        }
        layout();
    }

    public synchronized void zoom(float zoom) {
        this.zoom = zoom;
        for (RenderedText word : words) {
            if (word != null) word.scale.set(zoom);
        }
        layout();
    }

    public synchronized void hardlight(int color) {
        this.color = color;
        for (RenderedText word : words) {
            if (word != null) word.hardlight(color);
        }
    }

    public synchronized void resetColor() {
        this.color = -1;
        for (RenderedText word : words) {
            if (word != null) word.resetColor();
        }
    }

    public synchronized void alpha(float value) {
        for (RenderedText word : words) {
            if (word != null) word.alpha(value);
        }
    }

    public synchronized void setHightlighting(boolean enabled) {
        setHightlighting(enabled, Window.TITLE_COLOR);
    }

    public synchronized void setHightlighting(boolean enabled, int color) {
        if (enabled != highlightingEnabled || color != hightlightColor) {
            hightlightColor = color;
            highlightingEnabled = enabled;
            build();
        }
    }

    public synchronized void invert() {
        if (words != null) {
            for (RenderedText word : words) {
                if (word != null) {
                    word.ra = 0.77f;
                    word.ga = 0.73f;
                    word.ba = 0.62f;
                    word.rm = -0.77f;
                    word.gm = -0.73f;
                    word.bm = -0.62f;
                }
            }
        }
    }

    public synchronized void align(int align) {
        alignment = align;
        layout();
    }

    @Override
    protected synchronized void layout() {
        super.layout();
        float x = this.x;
        float y = this.y;
        float height = 0;
        nLines = 1;

        ArrayList<ArrayList<RenderedText>> lines = new ArrayList<>();
        ArrayList<RenderedText> curLine = new ArrayList<>();
        lines.add(curLine);

        width = 0;
        for (RenderedText word : words) {
            if (word == SPACE) {
                x += 1.5f;
            } else if (word == NEWLINE) {
                //newline
                y += height + 2f;
                x = this.x;
                nLines++;
                curLine = new ArrayList<>();
                lines.add(curLine);
            } else {
                if (word.height() > height) height = word.height();

                if ((x - this.x) + word.width() > maxWidth && !curLine.isEmpty()) {
                    y += height + 2f;
                    x = this.x;
                    nLines++;
                    curLine = new ArrayList<>();
                    lines.add(curLine);
                }

                word.x = x;
                word.y = y;
                PixelScene.align(word);
                x += word.width();
                curLine.add(word);

                if ((x - this.x) > width) width = (x - this.x);

                //TODO spacing currently doesn't factor in halfwidth and fullwidth characters
                //(e.g. Ideographic full stop)
                x -= 0.5f;

            }
        }
        this.height = (y - this.y) + height;

        if (alignment != LEFT_ALIGN) {
            for (ArrayList<RenderedText> line : lines) {
                if (line.size() == 0) continue;
                float lineWidth = line.get(line.size() - 1).width() + line.get(line.size() - 1).x - this.x;
                if (alignment == CENTER_ALIGN) {
                    for (RenderedText text : line) {
                        text.x += (width() - lineWidth) / 2f;
                        PixelScene.align(text);
                    }
                } else if (alignment == RIGHT_ALIGN) {
                    for (RenderedText text : line) {
                        text.x += width() - lineWidth;
                        PixelScene.align(text);
                    }
                }
            }
        }
    }
}
