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
package com.consideredhamster.yetanotherpixeldungeon;

public class Difficulties {

	public static final int EASY			= 0;
	public static final int NORMAL			= 1;
	public static final int HARDCORE		= 2;
	public static final int IMPOSSIBLE		= 3;
	
	public static final String[] NAMES = {
		"简单",
		"普通",
		"硬核",
		"另类"
	};

    public static final String[] ABOUT = {

            "-玩家承受更少伤害\n" +
            "-Boss血量减少20%\n" +
            "-普通怪物生命上限设为最低\n" +
            "-本难度下无法获得徽章！\n",

			"-玩家承受正常伤害\n" +
			"-Boss血量保持正常\n" +
			"-普通怪物生命上限在随机范围内生成\n",

			"-玩家承受正常伤害\n" +
			"-Boss血量增加20%\n" +
			"-普通怪物生命上限设为最高\n",

			"-玩家承受额外伤害\n" +
			"-Boss血量增加50%\n" +
			"-普通怪物生命上限设为最高\n",
    };
}
