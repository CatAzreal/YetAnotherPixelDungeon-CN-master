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
package com.consideredhamster.yapdcn.levels.features;

import com.consideredhamster.yapdcn.Dungeon;
import com.consideredhamster.yapdcn.levels.DeadEndLevel;
import com.consideredhamster.yapdcn.scenes.GameScene;
import com.consideredhamster.yapdcn.visuals.windows.WndMessage;

public class Sign {

	private static final String TXT_DEAD_END =
			"你来这里是要做些什么？";
	
	private static final String[] TIPS = {

            "国王敕令，本城下水道因近期状况报告而向公众暂时关闭。进入者后果自负！",
            "下水道维护人员应时刻照亮自己的油灯。许多线路很难在缺乏光照的状态下找到。",
            "由于日益增长的犯罪与豺狼人侵入报告，相关人员应时刻携带武装保护自身。",
            "应近期要求，市政部门在下水道各处安装了净水水源。切记时刻保持身体水分！",
            "市政部门温馨提示：任何关于下水道处目击巨型螃蟹生物的报告都是虚假且不切实际的",
            "警告！国王已敕令封锁后续通路。配备的安全系统将阻止任何未授权的访问。",

            "嗨。你好！欢迎随时下来找我哦。",
			"你不觉得蜘蛛看起来很可爱吗？巨型蜘蛛更可爱哦！",
			"这里豺狼人有点多，不好意思哈，它们当初求了我好久我才放进来的",
            "你可以在这见见我的朋友们，别害怕，他们只是来玩的！",
            "你就快到了！再往下走一点点就到...哈哈哈哈！",
            "欢迎来到我的游乐场！准备好找点乐子了吗？",

            "虾米滚，不滚吃虾米",
            "俺瞅见你了",
            "这有蝎汁",
            "豺囊壮，豺囊强",
            "矮子滚，这豺囊说了算",
            "欢迎*来到矮人都城！\n*不欢迎任何外来者。",

            "上城区",
            "市集",
            "工坊区",
            "军备区",
            "宫殿广场",
            "王座",

            "皇室宝库",

            "汝外来者当放弃一切希望",
            "终有一日吾辈将脱离桎梏",
            "吾之治下将再现圣世",
            "吾之威势必无人可挡",

            "",

            "",

//            "greetings, mortal" +
//                "\n\nare you ready to die?",
//            "my servants can smell your blood, human",
//            "worship me, and i may yet be merciful" +
//                "\n\nthen again, maybe not",
//            "you have played this game for too long, mortal" +
//                "\n\ni think i shall remove you from the board"
	};
	
	private static final String TXT_NOMESSAGE =
		"不管这里曾写过什么，上面留下的印记已完全不可解读。";
	
//	public static void read( int pos ) {
	public static void read() {

		if (Dungeon.level instanceof DeadEndLevel) {
			
			GameScene.show( new WndMessage( TXT_DEAD_END ) );
			
		} else {
			
			int index = Dungeon.depth - 1;
			
			if (index < TIPS.length && TIPS[index] != "" ) {
				GameScene.show( new WndMessage( TIPS[index] ) );
			} else {
                GameScene.show( new WndMessage( TXT_NOMESSAGE ) );
//				Level.set( pos, Terrain.EMBERS );
//				GameScene.updateMap( pos );
//				GameScene.discoverTile( pos, Terrain.SIGN );
				
//				CellEmitter.get( pos ).burst( ElmoParticle.FACTORY, 6 );
//				Sample.INSTANCE.play( Assets.SND_BURNING );
				
//				GLog.w( TXT_BURN );
				
			}
		}
	}
}
