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
package com.consideredhamster.yapdcn.visuals.windows;

import com.consideredhamster.yapdcn.YetAnotherPixelDungeon;
import com.consideredhamster.yapdcn.scenes.PixelScene;
import com.consideredhamster.yapdcn.visuals.ui.RenderedTextMultiline;
import com.consideredhamster.yapdcn.visuals.ui.ScrollPane;
import com.consideredhamster.yapdcn.visuals.ui.Window;
import com.watabou.noosa.BitmapText;
import com.watabou.noosa.BitmapTextMultiline;
import com.watabou.noosa.RenderedText;
import com.watabou.noosa.ui.Component;

public class WndChangelog extends Window {

	private static final int WIDTH_P	= 128;
	private static final int HEIGHT_P	= 200;

	private static final int WIDTH_L	= 200;
	private static final int HEIGHT_L	= 128;

	private static final String TXT_TITLE	= "YAPD v0.3.3 更新日志";

    private static final String TXT_DESYA =
            "\n\n" +
                    "_通用_\n" +
                    "\n" +
                    "- 所有难度默认解锁\n" +
                    "- 最后三个区域更加开放\n" +
                    "- 新增墙面和地格装饰\n" +
                    "- 针对区域微调，使区域之间各有特色\n" +
                    "- 空房间内新增饰物，令其观感更多样化\n" +
                    "\n" +
                    "- 新增\"烧灼\"类楼层，其中有大量骸骨(很可能受诅咒)并且寸草不生\n" +
                    "- 新增\"库存\"类楼层，绝大部分墙面都会被空书架区代\n" +
                    "- 新增\"警戒\"类楼层，楼层内生成额外敌人，其中部分默认为游荡状态\n" +
                    "- \"陷阱\"类楼层不再影响敌人数量和重生频率\n" +
                    "- \"水淹\"类楼层陷阱数量减少\n" +
                    "\n" +
                    "- 移除\"诡异\"类楼层生成\n" +
                    "- 现在所有危险地格都有对应的地格描述\n" +
                    "- 野心勃勃的小恶魔现在只生成在每层出口\n" +
                    "- 巨魔铁匠的任务现在只需要5块暗金矿\n" +
                    "\n" +
                    "_敌人与头目_\n" +
                    "\n" +
                    "- 怪物描述中显示其属性，抗性和能力\n" +
                    "- 完全重做天狗boss战\n" +
                    "- 完全重做DM-300boss战\n" +
                    "- Boss召唤物现在可作为普通敌人生成\n" +
                    "\n" +
                    "- 邪眼被接近时不再尝试逃跑\n" +
                    "- 邪眼射线现在可以被墙壁反射\n" +
                    "- 石化魔像攻击时可以造成击退\n" +
                    "- 暗蚀守卫现在会投掷鱼叉将你拉近\n" +
                    "\n" +
                    "- 豺狼猎手/萨满的数值会根据当前所在区域变动\n" +
                    "- 巨型食人鱼和小恶魔可以进入隐身状态\n" +
                    "- 大幅降低敌人掉落食物的概率\n" +
                    "- 敌人生成速度的衰减会更快\n" +
                    "\n" +
                    "- 火焰元素替换为新敌人: 小火龙\n" +
                    "- 恶魔化身替换为新敌人: 恶魔法师\n" +
                    "- 重做魅魔贴图，使其看起来与其他恶魔更加相近\n" +
                    "- 移除了下水道处不同职业在敌人描述中的不同吐槽\n" +
                    "\n" +
                    "_道具与消耗品_\n" +
                    "\n" +
                    "- 与书架互动时有概率识别出一种未知的卷轴类别\n" +
                    "- 鉴定卷轴重做为探魔卷轴\n" +
                    "- 饮用隐形药剂会熄灭油灯\n" +
                    "- 相对的，点亮油灯时，隐形效果会被驱散\n" +
                    "- 所有消除诅咒的效果现在只能从对应道具上移除一层负等级\n" +
                    "- 崭新的物品现在也可以进行修理\n" +
                    "\n" +
                    "_炸药与火器_\n" +
                    "\n" +
                    "- 所有燧发火器现在只需要一份火药装填\n" +
                    "- 炸药现在会在掷出的一回合后爆炸\n" +
                    "- 制作土质炸药现在需要消耗更多火药\n" +
                    "- 炸药爆炸不再造成击退效果\n" +
                    "- 爆炸造成的伤害由非元素伤害更为爆炸元素伤害(适用于抗性及弱点)\n" +
                    "\n" +
                    "_法杖_\n" +
                    "\n" +
                    "- 若目标没有站在水中，雷霆法杖现在可传导至其他相邻目标\n" +
                    "- 冰壁法杖现在只在目标处生成一面冰墙\n" +
                    "- 解离法杖不再会影响同一目标两次\n" +
                    "- 对火纹阵法进行第五次以上的叠加现在只会强化其持续时长\n" +
                    "\n" +
                    "_减益效果_\n" +
                    "\n" +
                    "- 所有来源的雷电伤害现在都会和雷霆法杖一样，通过水面传播\n" +
                    "- 中毒，虚弱，魅惑，受控状态下的伤害减益从50%下调至25%\n" +
                    "- 混乱状态下不符合既定目标的移动会中断后续的自动行为\n" +
                    "- 混乱状态下的敌人会被强制切换到\"游荡\"行为模式\n" +
                    "\n" +
                    "_修复_\n" +
                    "\n" +
                    "- 修复洞窟毒蝎提早一层出现的问题\n" +
                    "- 修复放逐卷轴造成笞挞而非放逐减益效果的问题\n" +
                    "- 修复受诅咒的耐久之戒妨碍维修过于频繁的问题\n" +
                    "- 修复鱼叉的视觉效果问题\n" +
                    "- 一箩筐的小规模修复和文本捉虫\n" +

                    "\n" +

                    "感谢你游玩本模组！和往常一样，如果出现任何问题请务必告知我，这将对我有很大帮助。";

    private RenderedText txtTitle;
    private ScrollPane list;

    public WndChangelog() {

        super();

        if (YetAnotherPixelDungeon.landscape()) {
            resize( WIDTH_L, HEIGHT_L );
        } else {
            resize( WIDTH_P, HEIGHT_P );
        }

        txtTitle = PixelScene.renderText( TXT_TITLE, 8 );
        txtTitle.hardlight( Window.TITLE_COLOR );
        PixelScene.align(txtTitle);
        txtTitle.x = PixelScene.align( PixelScene.uiCamera, (width - txtTitle.width() ) / 2 );
        add( txtTitle );

        list = new ScrollPane( new ChangelogItem( TXT_DESYA, width, txtTitle.height() ) );
        add( list );

        list.setRect( 0, txtTitle.height(), width, height - txtTitle.height() );
        list.scrollTo( 0, 0 );

	}

    private static class ChangelogItem extends Component {

        private final int GAP = 4;

        private RenderedTextMultiline normal;

        public ChangelogItem( String text, int width, float offset ) {
            super();

            normal.text(text);
            normal.maxWidth(width);

            height = normal.height() + offset;

            PixelScene.align(normal);
        }

        @Override
        protected void createChildren() {
            normal = PixelScene.renderMultiline( 5 );
            add( normal );
        }

    }
}
