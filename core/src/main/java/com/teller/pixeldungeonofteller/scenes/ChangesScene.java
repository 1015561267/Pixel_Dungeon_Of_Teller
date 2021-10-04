/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015  Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2016 Evan Debenham
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */
package com.teller.pixeldungeonofteller.scenes;

import com.teller.pixeldungeonofteller.Chrome;
import com.teller.pixeldungeonofteller.PixelDungeonOfTeller;
import com.teller.pixeldungeonofteller.messages.Messages;
import com.teller.pixeldungeonofteller.ui.Archs;
import com.teller.pixeldungeonofteller.ui.ExitButton;
import com.teller.pixeldungeonofteller.ui.RenderedTextMultiline;
import com.teller.pixeldungeonofteller.ui.ScrollPane;
import com.teller.pixeldungeonofteller.ui.Window;
import com.watabou.noosa.Camera;
import com.watabou.noosa.NinePatch;
import com.watabou.noosa.RenderedText;
import com.watabou.noosa.ui.Component;

//TODO: update this class with relevant info as new versions come out.
public class ChangesScene extends PixelScene {

    private static final String TXT_Update =


            "_2020-5-17\n"
                    + "_-_ 饥饿系统，相关的数值，休息等等机制更新，以YAPD与NNYPD为模板\n"
                    + "_-_ 修复由锯齿飞盘，冰面，残页，击退等引起的一系列bug\n"
                    + "_-_ 回旋镖改动，现在是一个副手投掷武器\n"
                    +

            "_2020-5-10\n"
                    + "_-_ 将徽章显示系统更新到破碎0.9.0的样式，以及部分贴图调整\n"
                    +

            "_2020-4-18\n"
                    + "_-_ 加入新的副手火枪：手炮\n"
                    +


            "_2020-4-14\n"
                    + "_-_ 尝试加入新地形：冰面\n"
                    + "_-_ 尝试修复因改动击退引起的bug\n"
                    +

            "_2020-4-7\n"
                    + "_-_ 尝试改进击退效果，思路与参考源码来自YAPD\n"
                    +

            "_2020-3-31\n"
                    + "_-_ 修复由锯齿飞盘引起的bug\n"
                    +

            "_2020-3-28\n"
                    + "_-_ 加入新的副手盾牌：锯齿飞盘\n"
                    +

            "_2020-3-25\n"
                    + "_-_ 改进武器体系的底层逻辑\n"
                    + "_-_ 继续修复残页与武器引起的各种bug\n" +


            "_2020-3-21\n"
                    +"_-_ 修复魔法残页引发的各种bug\n"
                    +


            "_2020-3-19_\n"
                    + "_-_ 完善魔法书的机制，实现魔法残页\n"
                    +


            "_2020-3-18_\n"
                    + "_-_ 由Qi修复了天狗掉落多个转职书的bug\n"
                    +


            "_2020-3-17_\n"
                    + "_-_ 建立GitHub项目并上传，网址：https://github.com/1015561267/Pixel_Dungeon_Of_Teller\n"
                    +


            "_2020-11-13_\n"
                    + "_-_ 物品贴图读取机制优化，避免了由此引起的物品贴图“浮在空中”\n"
                    +

            "_2020-8-28_\n"
                    + "_-_ 将存档机制更新到破碎0.6.4的存档位机制\n"
                    + "_-_ 加入新的副手武器——冲锋枪（然后被它的谜之bug困扰大半年）\n"
                    +


            "_2020-7-16_\n"
                    + "_-_ 尝试修复之前一系列迭代更新导致的bug\n"
                    +

            "_2020-7-14_\n"
                    + "_-_ 尝试将日志系统更新到破碎0.6.2版本，并修复相应bug\n"
                    +

            "_2020-7-13_\n"
                    + "_-_ 尝试将地图生成机制更新到破碎0.6版本，以及0.6.1与0.6.2的新内容\n"
                    +

            "_2020-7-12_\n"
                    + "_-_ 将画面显示更新到破碎0.5版本的2.5d显示效果\n"
                    +


            "_2020-7-11_\n"
                    + "_-_ 由护甲机制修改引发的数值修正\n"
                    +


            "_2020-7-10_\n"
                    + "_-_ 护甲机制修改，现在不再类似于护盾与生命值而是一种随着伤害递减的减免效果\n"
                    +


            "_2020-7-9_\n"
                    + "_-_ 适配了主流的全面屏\n"
                    +


            "_2020-2-9_\n"
                    + "_-_ 副手法术书时将显示切换快捷槽\n"
                    + "_-_ 理论上修复了二阶段天狗被秒杀的问题\n"
                    +

            "_2020-1-29_\n"
                    + "_-_ 完成第二本法术书——圣光之书\n"
                    + "_-_ 一些遗留的bug修复\n"
            +
            "_2020-1-26_\n"
                    + "_-_ 部分bug修复\n"
            +
            "_2020-1-24_\n"
                    + "_-_ 原有法术书系统移除并重做，法术书现在是一个副手武器，完成第一个重做法术书——老旧的魔法书\n"
                    + "_-_ 法术书副手槽实现动态贴图\n"
            +
            "_2020-1-19_\n"
                    + "_-_ 燧发枪实现特殊功能，详情请见物品介绍\n"
                    + "_-_ 燧发枪相关bug修复\n"
        +            "_2020-1-18_\n"
                    + "_-_ 新武器-燧发枪，一阶双持火枪，非移动的情况下装填\n"
                    + "_-_ 左右武器快捷栏位\n"
                    + "_-_ bug修复\n"
                    +"_2020-1-16_\n"
                     + "_-_ 伤害系统升级，加入伤害继承，原伤害豁免机制失效\n"
                     + "_-_ 武器与怪物数据平衡，大量伤害下调以适应新伤害系统\n"
                    + "_-_ 大量bug修复\n"
                    + "_2019-8-11_\n"
                    + "_-_ 新武器-双截棍，三阶双持，高攻速\n"
                    + "_-_ 显示优化与bug修复\n"
                    + "_2019-8-10_\n"
                    + "_-_ 新武器-臂铠，二阶附加，额外伤害减免\n"
                    + "_-_ 新武器-标枪筒，四阶副手，需要装填的投掷武器，替换了标枪\n"
                    + "_-_ 重做-圆盾，现在格挡效果会试图将到来的切割与穿刺物理伤害削弱并转化为冲击\n"
                    + "_-_ 新彩蛋食物-山楂片，替换了商店的一个小口粮生成，提供开胃buff，此状态下再进食可能恢复额外的饱食度或者噎着(什么也不发生)\n"
                    + "_-_ 修复了包括卡下楼在内的一些bug\n\n"
                    + "_2019-8-8_\n"
                    + "_-_ 新武器-忍者护手，三阶附加，近程散射手里剑与远程投掷苦无\n"
                    + "_-_ 新武器-带刺链球，二阶副手，主冲击，慢速\n"
                    + "_-_ 新武器-胁差，三阶副手，主切割\n"
                    + "_-_ 新武器-刺剑，三阶副手，主穿刺\n"
                    + "_-_ 新物品-劣质修补包，商店有售，最多回复一半护甲\n"
                    + "_-_ 削弱-袖剑，反击概率进一步下调\n"
                    + "_-_ 增强-圆盾，格挡概率提高\n"
                    + "_-_ 平衡-部分双持武器数值微调\n"
                    + "_-_ 移除-符文之刃与暗杀之剑被移除了掉落\n"
                    + "_-_ 副手攻击逻辑改进，自然冷却回合大大加长，主手攻击可根据攻速缩减冷却时间\n"
                    + "_-_ 怪物机制改动-骷髅，（新生）火元素，魔像，dm300现在没有血量，在护盾与护甲耗尽后死亡\n"
                    + "_-_ 增加了商店生成，武器防具购买价格减半，出售不受影响\n"
                    + "_-_ 众多bug修复\n\n"
                    + "_2019-7-27_\n"
                    + "_-_ 武器系统修改，人物拥有主副手武器槽，武器被分为五大类，有穿脱共存逻辑检定\n"
                    + "_-_ 新的副手攻击逻辑，主手攻击时副手若可攻击且冷却完毕，会独立发起一次攻击，分开计算命中与伤害\n"
                    + "_-_ 新武器-袖剑，二阶附加，敌人攻击前有概率反击\n"
                    + "_-_ 武器重做-圆盾，三阶副手，可花费一回合格挡，获得buff，期间有概率使物理伤害减半\n"
                    + "_-_ 武器重做-双钗，三阶双持，需求和伤害相应调整\n"
                    + "_-_ 武器重做-飞斧，五阶双持，装备以后投掷可以获得原投掷效果，并且不会消失\n\n"
                    + "_2019-7-17_\n"
                    + "_-_ 生物机制重做，角色和怪物现在都具有生命值(hp)，护盾值(shiele)，护甲值(armor)，角色的护甲和护盾值由防具提供，怪物自带。护盾在单位护盾值不满时开始自动回复，速度逐渐加快。角色护甲值目前仅能通过针线包修补，怪物不会恢复。血量无大幅改动\n"
                    + "_-_ 伤害机制重做，现在伤害分为物理，法术，绝对伤害，详询指南部分\n"
                    + "_-_ 由上述改动引起的武器，防具，怪物的多方面相应改动\n"
                    + "_-_ 人物属性拓展为力量，敏捷，智力，敏捷作用不大，智力完全没用，力量药水的相应改动\n"
                    + "_-_ 暂时移除了武力，根骨，元素戒指的生成掉落\n"
                    + "_-_ yog获得了大幅数值加强和机制上的微小改进\n";
            /*
            "_v0.4.3c:_\n" +
                    "_-_ Additional bugfixes\n" +
                    "\n" +
                    "_v0.4.3b:_\n" +
                    "_-_ Thrown potions now trigger traps and plants\n" +
                    "_-_ Various bugfixes\n" +
                    "\n" +
                    "_v0.4.3a:_\n" +
                    "_-_ Reworked glyph of brimstone\n" +
                    "\t \tnow grants shielding instead of healing\n" +
                    "_-_ Reworked glyph of stone\n" +
                    "\t \tnow reduces speed in doorways\n" +
                    "_-_ Power saver looks less blurry on some devices\n" +
                    "\n" +
                    "_v0.4.3:_\n" +
                    "General Improvements:\n" +
                    "_-_ Added rankings and hall of heroes sync\n" +
                    "_-_ Added Power Saver mode in settings\n" +
                    "_-_ Game now supports small screen devices\n" +
                    "_-_ Improved variety of level visuals\n" +
                    "\n" +
                    "Balance Changes:\n" +
                    "_-_ Flail max damage increased by ~15%\n" +
                    "_-_ Wand of Frost damage reduction increased\n" +
                    "\t \tfrom 5% per turn of chill to 7.5%\n" +
                    "_-_ Ring of Furor speed bonus reduced by\n" +
                    "\t \t~15% for slow weapons, ~0% for fast weapons\n" +
                    "_-_ Reduced sacrificial curse bleed by ~33%\n" +
                    "\n" +
                    "_v0.4.2:_\n" +
                    "_-_ Improved performance on many devices\n" +
                    "_-_ Various balance changes\n" +
                    "\n" +
                    "_v0.4.1:_\n" +
                    "_-_ Armor effectiveness increased\n" +
                    "_-_ Evil Eyes reworked\n" +
                    "\n" +
                    "_v0.4.0:_ Reworked equips, enchants & curses\n" +
                    "\n" +
                    "_v0.3.5:_ Reworked Warrior & subclasses\n" +
                    "\n"+
                    "_v0.3.4:_ Multiple language support\n" +
                    "\n" +
                    "_v0.3.3:_ Support for Google Play Games\n" +
                    "\n" +
                    "_v0.3.2:_ Prison Rework & Balance Changes\n" +
                    "\n" +
                    "_v0.3.1:_ Traps reworked & UI upgrades\n" +
                    "\n" +
                    "_v0.3.0:_ Wands & Mage completely reworked\n" +
                    "\n" +
                    "_v0.2.4:_ Small improvements and tweaks\n" +
                    "\n" +
                    "_v0.2.3:_ Artifact additions & improvements\n" +
                    "\n" +
                    "_v0.2.2:_ Small improvements and tweaks\n" +
                    "\n" +
                    "_v0.2.1:_ Sewer improvements\n" +
                    "\n" +
                    "_v0.2.0:_ Added artifacts, reworked rings\n" +
                    "\n" +
                    "_v0.1.1:_ Added blandfruit, reworked dew vial\n" +
                    "\n" +
                    "_v0.1.0:_ Improvements to potions/scrolls";*/

    @Override
    public void create() {
        super.create();

        int w = Camera.main.width;
        int h = Camera.main.height;

        RenderedText title = PixelScene.renderText(Messages.get(this, "title"), 9);
        title.hardlight(Window.TITLE_COLOR);
        title.x = (w - title.width()) / 2;
        title.y = 4;
        align(title);
        add(title);

        ExitButton btnExit = new ExitButton();
        btnExit.setPos(Camera.main.width - btnExit.width(), 0);
        add(btnExit);

        RenderedTextMultiline text = renderMultiline(TXT_Update, 6);

        NinePatch panel = Chrome.get(Chrome.Type.TOAST);

        int pw = 135 + panel.marginLeft() + panel.marginRight() - 2;
        int ph = h - 16;

        panel.size(pw, ph);
        panel.x = (w - pw) / 2f;
        panel.y = title.y + title.height();
        align(panel);
        add(panel);

        ScrollPane list = new ScrollPane(new Component());
        add(list);

        Component content = list.content();
        content.clear();

        text.maxWidth((int) panel.innerWidth());

        content.add(text);

        content.setSize(panel.innerWidth(), (int) Math.ceil(text.height()));

        list.setRect(
                panel.x + panel.marginLeft(),
                panel.y + panel.marginTop() - 1,
                panel.innerWidth(),
                panel.innerHeight() + 2);
        list.scrollTo(0, 0);

        Archs archs = new Archs();
        archs.setSize(Camera.main.width, Camera.main.height);
        addToBack(archs);

        fadeIn();
    }

    @Override
    protected void onBackPressed() {
        PixelDungeonOfTeller.switchNoFade(TitleScene.class);
    }
}


