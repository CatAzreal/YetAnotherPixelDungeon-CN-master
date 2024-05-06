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
package com.consideredhamster.yetanotherpixeldungeon.actors.mobs.npcs;

import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.ShopkeeperGhostSprite;
import com.consideredhamster.yetanotherpixeldungeon.misc.utils.Utils;

public class ShopkeeperGhost extends Shopkeeper {

	private static final String TXT_GREETINGS = "你好，迷途者，\n要来店里看看吗？";
	
	{
		name = "奇异的店主";
		spriteClass = ShopkeeperGhostSprite.class;
	}

    @Override
    protected void greetings() {
        yell( Utils.format(TXT_GREETINGS) );
    }

    @Override
    public boolean isMagical() {
        return true;
    }
	
	@Override
	public String description() {
		return 
			"这家伙看起来没有任何威胁，可它仅仅是存在于此就让你紧张不安。你不禁思考它这究竟会卖些什么东西？";
	}
}
