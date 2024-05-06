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

import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.ShopkeeperTrollSprite;
import com.consideredhamster.yetanotherpixeldungeon.misc.utils.Utils;

public class ShopkeeperTroll extends Shopkeeper {

	private static final String TXT_GREETINGS = "人类？这里？\n有意思。";
	
	{
		name = "巨魔店主";
		spriteClass = ShopkeeperTrollSprite.class;
	}

    @Override
    protected void greetings() {
        yell( Utils.format(TXT_GREETINGS) );
    }
	
	@Override
	public String description() {
		return 
			"这个巨魔看起来和任何其他巨魔一样：又高又瘦，皮肤的色泽和纹理都像是石头。不过你可没见过几只会开商店的巨魔。";
	}
}
