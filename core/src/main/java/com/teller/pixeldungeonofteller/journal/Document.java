package com.teller.pixeldungeonofteller.journal;

import com.teller.pixeldungeonofteller.messages.Messages;
import com.watabou.utils.Bundle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;

public enum Document {
    ADVENTURERS_GUIDE;

    private LinkedHashMap<String, Boolean> pages = new LinkedHashMap<>();

    public Collection<String> pages(){
        return pages.keySet();
    }

    public boolean addPage( String page ) {
        if (pages.containsKey(page) && !pages.get(page)){
            pages.put(page, true);
            Journal.saveNeeded = true;
            return true;
        }
        return false;
    }

    public boolean hasPage( String page ){
        return pages.containsKey(page) && pages.get(page);
    }

    public String title(){
        return Messages.get( this.getClass(), name() + ".title");
    }

    public String pageTitle( String page ){
        return Messages.get( this, name() + "." + page + ".title");
    }

    public String pageBody( String page ){
        return Messages.get( this, name() + "." + page + ".body");
    }

    public static final String GUIDE_INTRO_PAGE = "Intro";
    public static final String GUIDE_SEARCH_PAGE = "Examining_and_Searching";

    static {
        ADVENTURERS_GUIDE.pages.put(GUIDE_INTRO_PAGE, 	false);
        ADVENTURERS_GUIDE.pages.put("Identifying", 		false);
        ADVENTURERS_GUIDE.pages.put(GUIDE_SEARCH_PAGE, 	false);
        ADVENTURERS_GUIDE.pages.put("Strength", 		false);
        ADVENTURERS_GUIDE.pages.put("Food", 			false);
        ADVENTURERS_GUIDE.pages.put("Levelling", 		false);
        ADVENTURERS_GUIDE.pages.put("Surprise_Attacks", false);
        ADVENTURERS_GUIDE.pages.put("Dieing", 			false);
        ADVENTURERS_GUIDE.pages.put("Looting", 		    false);
        ADVENTURERS_GUIDE.pages.put("Magic", 			false);
    }

    private static final String DOCUMENTS = "documents";

    public static void store( Bundle bundle ){

        Bundle docBundle = new Bundle();

        for ( Document doc : values()){
            ArrayList<String> pages = new ArrayList<>();
            for (String page : doc.pages()){
                if (doc.pages.get(page)){
                    pages.add(page);
                }
            }
            if (!pages.isEmpty()) {
                docBundle.put(doc.name(), pages.toArray(new String[0]));
            }
        }

        bundle.put( DOCUMENTS, docBundle );

    }

    public static void restore( Bundle bundle ){

        if (!bundle.contains( DOCUMENTS )){
            return;
        }

        Bundle docBundle = bundle.getBundle( DOCUMENTS );

        for ( Document doc : values()){
            if (docBundle.contains(doc.name())){
                List<String> pages = Arrays.asList(docBundle.getStringArray(doc.name()));
                for (String page : pages){
                    if (doc.pages.containsKey(page)) {
                        doc.pages.put(page, true);
                    }
                }
            }
        }
    }

}
