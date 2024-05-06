/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Yet Another Pixel Dungeon
 * Copyright (C) 2015-2016 Considered Hamster
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
package com.consideredhamster.yapdcn.scenes;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import com.consideredhamster.yapdcn.visuals.ui.RenderedTextMultiline;
import com.watabou.input.Touchscreen;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Game;
import com.watabou.noosa.RenderedText;
import com.watabou.noosa.TouchArea;
import com.watabou.noosa.audio.Music;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;
import com.consideredhamster.yapdcn.visuals.Assets;
import com.consideredhamster.yapdcn.Badges;
import com.consideredhamster.yapdcn.Dungeon;
import com.consideredhamster.yapdcn.Statistics;
import com.consideredhamster.yapdcn.YetAnotherPixelDungeon;
import com.consideredhamster.yapdcn.actors.Actor;
import com.consideredhamster.yapdcn.items.Generator;
import com.consideredhamster.yapdcn.levels.Level;
import com.consideredhamster.yapdcn.visuals.windows.WndError;
import com.consideredhamster.yapdcn.visuals.windows.WndStory;

public class InterlevelScene extends PixelScene {

    private static final float TIME_TO_FADE = 0.5f;

    private static final String TXT_DESCENDING	= "下楼中...";
    private static final String TXT_ASCENDING	= "上楼中...";
    private static final String TXT_LOADING		= "载入中...";
    private static final String TXT_RESURRECTING= "正在复活...";
    private static final String TXT_RETURNING	= "返回坐标...";
    private static final String TXT_FALLING		= "坠落中...";
    private static final String TXT_CONTINUE	= "点击以继续!";

    private static final String ERR_FILE_NOT_FOUND	= "文件缺失";
    private static final String ERR_GENERIC			= "Something went wrong..."	;

    public static enum Mode {
        DESCEND, ASCEND, CONTINUE, RESURRECT, RETURN, FALL
    };
    public static Mode mode;

    public static int returnDepth;
    public static int returnPos;

    public static boolean noStory = false;

    public static boolean fallIntoPit;

    //todo: The tips are supposed to switch every few seconds but the game seems refused to do so with renderedText
    private static final String[] TIPS = {

            // GENERAL

            "在地牢中每五层都会出现一个商店供你消费金币",
            "地牢中只有3个复活十字架，但有极低的概率找到更多",

            "陷阱与浸水宝库中更容易找到未受诅咒的道具",
            "修有陵墓或存在活化石像的房间中获取的道具不会受到诅咒",

            "感知属性影响侦察陷阱和周边隐藏门所需的时间",
            "感知属性影响你被动发现周边隐藏门的概率",
            "感知属性影响你探索或睡眠时听见周遭敌人声音的几率",
            "感知属性影响你在格挡时防反敌人的几率",

            "潜行属性影响在商店可行窃的次数和成功几率",
            "潜行属性影响你伏击敌人的几率",

            "调谐属性影响所有法杖的充能速率",
            "调谐属性影响装备物品前感知到诅咒的概率",

//            "Magic power affects effectiveness of enchanted equipment",
            "魔能属性影响法杖造成的伤害/效力",
            "力量属性影响你每回合从束缚状态中解脱的概率",

            "随着地牢的深入，掉落物和特殊房间的数量也会随之提高",
            "尽可能保证你的角色等级高于或等同于当前层数",

            "可升级物品的等级上限为3——不必担心，它们可堪大用",
            "高等级物品有着更高的耐久度，但在被诅咒时也会以更快的速度损坏",

            "别忘了，你可以在游戏设置中关闭这些提示！",
            "更多提示会随后加入，敬请期待！",

            // WEAPONS & ARMOURS

            "使用超重武器会降低你的攻击速度",
            "强力武器通常会进一步降低你的命中和潜行属性",

            "使用超重护甲或盾牌会降低你的移动速度",
            "强力护甲或盾牌通常会进一步降低你的敏捷和潜行属性",
            "升级布制护甲能够进一步提高其属性加值",

            "格挡攻击的概率取决于你持有盾牌的护甲等级或武器伤害",
            "成功格挡攻击有概率防反你的敌人，让你获得反击敌人的空当",

            "额外力量可以降低重型装备的惩罚",
            "你可以通过长时间使用武器，护甲，法杖和戒指来自然鉴定其性质",
            "睡眠时你不会承受来自装备的潜行减益",

            "燧发武器无视攻击距离造成的命中减益并能够穿透目标护甲造成伤害",

            "你能够拆解土制炸药和炸药包以回收部分材料",

            // WANDS & RINGS

            "施法失败的几率取决于法杖状态和等级，若被诅咒则概率进一步提高",
            "汲取充能的概率取决于法杖状态及等级",

            "未被鉴定和受诅咒的法杖会出现施法失败的情况，但无法被汲取充能",
            "若法杖已被鉴定，你能够从无充能法杖中汲取额外充能",

            "部分戒指仅在特定情况下才有效用，其他情况下没有一直佩戴的必要",
            "两个相同类型的戒指效果可以叠加",

            "每个区域通常只有一根法杖，不过有小概率额外获取",
            "每个区域通常只有一枚戒指，不过有小概率额外获取",

//            "Wands of Magic Missile have the highest up-front single-target damage",
            "特定情况下，解离光束可通过调整角度穿透单个目标两次",
            "神罚法杖在对抗秘法生物时非常有效",

            "雷霆法杖的攻击无法通过飞行目标身下的水域传导",
            "酸蚀法杖在近战范围可造成150%有效伤害，但在对抗远处敌人时会受到50%的伤害惩罚",
            "通过火纹法杖的法阵造成的伤害远高于直接对敌人施法",

            "通过原力法杖造成的崩塌伤害高于直接对敌人施法",
            "通过棘藤法杖在植被地格生成的棘藤更加强大",
            "通过冰壁法杖在浸水地格生成的冰墙更加坚固",

            "魅惑法杖可以伤害并扰乱具有秘法特性的敌人",
            "窃血法杖在对抗睡眠或漫游状态的敌人时效力更强",
            "咒罚法杖的效率基本取决于目标的当前生命值",

            // POTIONS

            "每个区域通常只有一瓶智慧药剂，不过有小概率额外获取",
            "智慧药剂还能够提高等级上限，允许你将等级进一步提高",

            "每个区域通常只有二瓶力量药剂，不过有小概率额外获取",
            "饮用力量药剂是游戏中最有效的生命回复方式",

            "每个商店至少会出售一瓶愈合药剂",
            "愈合药剂还能够治愈中毒，流血等绝大多数物理减益效果",

            "灵视药剂允许你在效力期间几乎无视低视野所带来的惩罚",
            "灵视药剂还会在效力期间提高你的感知属性",

            "浮空药剂在效力期间会提供额外的移动速度和闪避能力",
            "浮空药剂还能够用于安全降落在深渊下方",

            "隐形时你的潜行能力也会提高，能够进一步降低商店行窃的难度",
            "敌人在尝试移动到你的所在位置时会解除你的隐形效果",

            "饮用护盾药剂也能够在效力期间提高你的物理抗性",
            "护盾药剂的效果可以和装备相互叠加",

            "圣灵药剂能够对秘法生物造成极高伤害",
            "在身下扔出圣灵药剂能够弱化持有道具的诅咒程度",

            "液火药剂产生的燃烧效果不会蔓延至临近浸水地格",
            "液火药剂必定点燃周边的可燃地格",
            // Chronie starts here
            // Thanks Chronie!
            "冰雾药剂能够迅速消除房间里蔓延的火势",
            "冰雾药剂在对付水中的敌人时更为有效",

            "小心使用毒气药剂，因为其释放的气体极度易燃",
            "毒气药剂在对付一大群没有抗性的生物时特为有效",

            "使用雷暴药剂会吸引游荡的敌人",
            "雷暴药剂能引起一场降雨，会使地面变成水，也可以灭火",

            "如果你想和敌人保持距离，结网药剂会是非常好的选择",
            "幻气药剂搭配深渊将会非常致命",
            "狂怒药剂能使你获得所有挑战卷轴带来的增益，同时不含任何副作用",
            "淤泥药剂在投掷到无水的地格时会留下一滩持续一段时间的酸性污泥",

            // SCROLLS

            "每个区域通常只有一张附魔卷轴，不过有小概率额外获取",
            "对一件受诅咒的物品使用附魔卷轴会使其上的诅咒被弱化甚至被驱散",

            "每个区域通常只有二张升级卷轴，不过有小概率额外获取",
            "使用升级卷轴驱散一件已有附魔的物品的诅咒能使你保留这个附魔",

            "明智的规划探魔卷轴能节约你很多时间",
            "在每个商店中总会，也仅会提供一张探魔卷轴",

            "嬗变卷轴不会转化出与原道具相同的物品",
            "嬗变卷轴亦可对投掷物及弹药使用",

            "圣光卷轴可以抵消掉雷暴药剂的效果",
            "圣光卷轴也会对一些敌人产生治疗效果，千万当心",

            "探地卷轴只会探出房间和道具，不会标记出隐藏门和陷阱",
            "被探地卷轴点亮的区域不会因变相卷轴的效果被消除",

            "放逐卷轴可以对不死生物，元素生物和魔像造成伤害",
            "放逐卷轴能够弱化包里的所有道具的诅咒",

            "被黑暗卷轴致盲的敌人可能会自己掉下悬崖或踩进陷阱",
            "黑暗卷轴可以抵消圣光卷轴产生的效果",

            "变相卷轴可以轻松救你一命，同样也可能轻易要了你的命",
            "使用变相卷轴会使你混乱一小段时间",

            "死灵卷轴对付单个目标时效果非常致命，包括你在内",
            "由死灵卷轴召唤的恶灵的魅惑效果会在一段时间后消失",

            "挑战卷轴留到boss关使用或许是最佳选择",
            "使用挑战卷轴能诱使宝箱怪暴露原形",

            "如果视野内没有敌人，苦痛卷轴对你的伤害更高",
            "苦痛卷轴无法折磨没有心智的生物",

            // FOOD

            "每层总会有至少一个干粮，不过也要留意隐藏的房间",
            "部分怪物会掉落生肉，甚至小份干粮",

            "在饱腹状态下，你的自然恢复速度会较平常提升",
            "可以用火来点燃生肉或炖肉，但这么做会让它的效果降低，而且不那么好吃",

            "烧焦的肉的营养较低，而且不能再下锅煮了",
            "炖煮生肉不会受到debuff，但是吃的时候需要用更多时间",

            "有时能找到额外的食物，但是它们会是小份的",
            "你可以在商店里购买馅饼，这往往能物超所值",

            "极度饥饿并不会立即发生伤害，最开始时只会阻止自然生命回复",
            "如果你无视饥饿状态太久，就会变得更危险",

            "如果你的力量超过装备所需力量，饱食度的消耗会稍微变慢",
            "移动，攻击，施法和格挡会比静止不动消耗回合时消耗饱食度更快",

            // BOSSES

            "绝大多数boss会被激怒，但是每场boss战只会出现三次激怒",
            "Boss受到爆炸，药水和卷轴的伤害更高",

            "需要注意的是，粘咕释放的瘴气极度易燃",
            "粘咕有一些手段治疗自己，但是这些伎俩都可以被规避",

            "当遭受伤害时，天狗的传送会更频繁",
            "天狗在被束缚时不会进行传送",

            "DM-300既不是有机物也不是魔法造物",
            "DM-300在每次激怒后都会更频繁地使用自己的特殊攻击",

            "矮人国王在仪式进行时完全刀枪不入",
            "然而这个仪式似乎可以使用某种咒语打断...",

            // TERRAIN

            "如果你想悄悄接近某人，请尽量避免在水中移动",
            "如果你想悄悄接近某人，可以考虑待在高草丛里",

            "飞行生物视野可以越过高草，并且不受地面的效果影响",
            "水会增强电击和霜冻效果，同时也可以扑灭火焰，洗去酸性物质",

            "陷阱只会在通常的房间中出现，而不会出现在走廊或特殊房间中",
            "在地牢中居住的怪物对陷阱和隐藏门的位置非常了解",

            "如果你在旱地上睡着，你的生命回复速度会提升至三倍",
            "在水里睡觉的回复效率比在其他地方低得多",
            "当所有的临近地格都被占用或不可通行时，闪避几率会降低",

            // MISC

            "你可以使用你的水袋来倒水灭火，或洗掉腐蚀污泥",
            "你可以用你的水袋来浇灌附近的高草地来种植草药",

            "明亮的油灯能让你更容易发现陷阱和隐藏门",
            "如果你有多余的燃油瓶，你可以用油灯点燃邻近的地格",
    };

    private enum Phase {
        FADE_IN, STATIC, FADE_OUT
    }

    private Phase phase;
    private float timeLeft;

//    private BitmapText            message;
//    private ArrayList<BitmapText> tipBox;

    private RenderedText message;
    private ArrayList<RenderedText> tipBox;

    private Thread thread;
    private String error = null;
    private boolean pause = false;

    @Override
    public void create() {
        super.create();

        String text = "";
//        int depth = Dungeon.depth;

        switch (mode) {
            case DESCEND:
                text = TXT_DESCENDING;
//                depth++;
                break;
            case ASCEND:
                text = TXT_ASCENDING;
//                depth--;
                break;
            case CONTINUE:
                text = TXT_LOADING;

//                GamesInProgress.Info info = GamesInProgress.check( StartScene.curClass );

//                if (info != null) {
//
//                    depth = info.depth;
//
//                }

//                depth = depth > 0 ? depth : 0 ;

                break;
            case RESURRECT:
                text = TXT_RESURRECTING;
                break;
            case RETURN:
                text = TXT_RETURNING;
//                depth = returnDepth;
                break;
            case FALL:
                text = TXT_FALLING;
//                depth++;
                break;
        }

        message = PixelScene.renderText( text, 10 );
        message.x = (Camera.main.width - message.width()) / 2;
        message.y = (Camera.main.height - message.height()) / 2;
        add(message);
        align(message);

        tipBox = new ArrayList<>();

        if( YetAnotherPixelDungeon.loadingTips() > 0 ) {

            RenderedTextMultiline tip = PixelScene.renderMultiline(TIPS[Random.Int(TIPS.length)], 6);
            tip.maxWidth(Camera.main.width * 8 / 10);
            tip.setPos(Camera.main.width / 2 - tip.width() / 2,Camera.main.height * 3 / 4 - tip.height() * 3 / 4 + tipBox.size() * tip.height());
            align(tip);
            add(tip);

//            for (RenderedText line : tip.new LineSplitter().split()) {
//                line.measure();
//                line.x = PixelScene.align(Camera.main.width / 2 - line.width() / 2);
//                line.y = PixelScene.align(Camera.main.height * 3 / 4 - tip.height() * 3 / 4 + tipBox.size() * line.height());
//                tipBox.add(line);
//                add(line);
//            }
        }


        phase = Phase.FADE_IN;
        timeLeft = TIME_TO_FADE;

        thread = new Thread() {
            @Override
            public void run() {

                try {

                    Generator.reset();

                    switch (mode) {
                        case DESCEND:
                            descend();
                            break;
                        case ASCEND:
                            ascend();
                            break;
                        case CONTINUE:
                            restore();
                            break;
//					case RESURRECT:
//                        resurrect();
//                        break;
                        case RETURN:
                            returnTo();
                            break;
                        case FALL:
                            fall();
                            break;
                    }

                    if ((Dungeon.depth % 6) == 0 && Dungeon.depth == Statistics.deepestFloor ) {
                        Sample.INSTANCE.load( Assets.SND_BOSS );
                    }

                    if( mode != Mode.CONTINUE ) {
                        Dungeon.saveAll();
                        Badges.saveGlobal();
                    }

                } catch (FileNotFoundException e) {

                    error = ERR_FILE_NOT_FOUND;

                } catch (Exception e) {

                    error = e.toString();
                    YetAnotherPixelDungeon.reportException(e);

                }

//                error = ERR_FILE_NOT_FOUND;

                if (phase == Phase.STATIC && error == null) {
                    phase = Phase.FADE_OUT;
                    timeLeft = TIME_TO_FADE * 2;
                }
            }
        };
        thread.start();
    }

    @Override
    public void update() {
        super.update();

        float p = timeLeft / TIME_TO_FADE;

        switch (phase) {

            case FADE_IN:

                message.alpha( 1 - p );

//            for (BitmapText line : tipBox) {
//                line.alpha( 1 - p );
//            }

                if ((timeLeft -= Game.elapsed) <= 0) {
                    if (thread.isAlive() || error != null || YetAnotherPixelDungeon.loadingTips() > 2 ) {
                        phase = Phase.STATIC;

                        if( !thread.isAlive() && error == null) {
                            message.text(TXT_CONTINUE);
                            message.x = (Camera.main.width - message.width()) / 2;
                            message.y = (Camera.main.height - message.height()) / 2;
                            align(message);

                            TouchArea hotArea = new TouchArea(0, 0, Camera.main.width, Camera.main.height) {
                                @Override
                                protected void onClick(Touchscreen.Touch touch) {
                                    phase = Phase.FADE_OUT;
                                    timeLeft = TIME_TO_FADE;
                                    this.destroy();
                                }
                            };
                            add(hotArea);
                        }

                    } else {
                        phase = Phase.FADE_OUT;
                        timeLeft = ( YetAnotherPixelDungeon.loadingTips() > 0 ?
                                TIME_TO_FADE * YetAnotherPixelDungeon.loadingTips() * 3 : TIME_TO_FADE );
                    }
                }
                break;

            case FADE_OUT:

                message.alpha( p );

//            for (BitmapText line : tipBox) {
//                line.alpha( p );
//            }

                if (mode == Mode.CONTINUE || (mode == Mode.DESCEND && Dungeon.depth == 1)) {
                    Music.INSTANCE.volume( p );
                }
                if ((timeLeft -= Game.elapsed) <= 0) {
                    Game.switchScene( GameScene.class );
                }
                break;

            case STATIC:

                if (error != null) {

                    add(new WndError(error) {
                        public void onBackPressed() {
                            super.onBackPressed();
                            Game.switchScene(StartScene.class);
                        }
                    });

                    error = null;

                }
                break;
        }
    }

    private void descend() throws Exception {

        Actor.fixTime();

        if (Dungeon.hero == null) {
            Dungeon.init();
            if (noStory) {
                Dungeon.chapters.add( WndStory.ID_SEWERS );
                noStory = false;
            }
        } else {
            Dungeon.saveAll();
        }

        Level level;
        if( Dungeon.depth <= 25 ){

            if( Dungeon.depth >= Statistics.deepestFloor ){
                level = Dungeon.newLevel();
            } else {
                Dungeon.depth++;
                level = Dungeon.loadLevel( Dungeon.hero.heroClass );
            }
        } else {
            // You hear distant a sound of  malicious laughter.
            level = Dungeon.loadLevel( Dungeon.hero.heroClass );
        }
        Dungeon.switchLevel( level, level.entrance );
    }

    private void fall() throws Exception {

        Actor.fixTime();
        Dungeon.saveAll();

        Level level;
        if (Dungeon.depth >= Statistics.deepestFloor) {
            level = Dungeon.newLevel();
        } else {
            Dungeon.depth++;
            level = Dungeon.loadLevel( Dungeon.hero.heroClass );
        }

        Dungeon.switchLevel( level, fallIntoPit ? level.pitCell() : level.randomRespawnCell( true, true ) );
    }

    private void ascend() throws Exception {
        Actor.fixTime();

        Dungeon.saveAll();
        Dungeon.depth--;
        Level level = Dungeon.
                loadLevel( Dungeon.hero.heroClass );
        Dungeon.switchLevel( level, level.exit );
    }

    private void returnTo() throws Exception {

        Actor.fixTime();

        Dungeon.saveAll();
        Dungeon.depth = returnDepth;
        Level level = Dungeon.loadLevel( Dungeon.hero.heroClass );
        Dungeon.switchLevel(level, Level.resizingNeeded ? level.adjustPos(returnPos) : returnPos);
    }

    private void restore() throws Exception {

        Actor.fixTime();

        Dungeon.loadGame(StartScene.curClass);
        if (Dungeon.depth == -1) {
            Dungeon.depth = Statistics.deepestFloor;
            Dungeon.switchLevel( Dungeon.loadLevel( StartScene.curClass ), -1 );
        } else {
            Level level = Dungeon.loadLevel( StartScene.curClass );
            Dungeon.switchLevel( level, Level.resizingNeeded ? level.adjustPos( Dungeon.hero.pos ) : Dungeon.hero.pos );
        }
    }

//	private void resurrect() throws Exception {
//
//        Actor.fixTime();
//
//        if (Dungeon.bossLevel()) {
//
//            Dungeon.hero.resurrect( Dungeon.depth );
//            Dungeon.depth--;
//            Level level = Dungeon.newLevel();
//            Dungeon.switchLevel( level, level.entrance );
//
//        } else {
//
//            Dungeon.hero.resurrect(-1);
//            Actor.clear();
//            Arrays.fill(Dungeon.visible, false);
//            Dungeon.level.reset();
//            Dungeon.switchLevel(Dungeon.level, Dungeon.hero.pos);
//
//        }
//    }

    @Override
    protected void onBackPressed() {
        // Do nothing
    }
}
