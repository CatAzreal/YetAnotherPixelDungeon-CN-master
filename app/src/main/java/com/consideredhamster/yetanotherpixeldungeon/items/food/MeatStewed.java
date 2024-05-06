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
package com.consideredhamster.yetanotherpixeldungeon.items.food;

import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.special.Satiety;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.ItemSprite;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.ItemSpriteSheet;

public class MeatStewed extends Food {

	protected ItemSprite.Glowing spiceGlow = null;

	{
		name = "炖肉";
		image = ItemSpriteSheet.STEWED_MEAT;

		energy = Satiety.MAXIMUM * 0.25f;
        message = "这块肉尝起来...倒是不差";
	}

	
	@Override
	public String desc() {
        return
            "这块肉好好地在锅里煮了一番。比起那些平淡无奇的干粮，它闻起来要美味的多。尽管食用它并_不会带来任何特殊效果_，但肯定比生着吃要好很多。";

	}

	@Override
	public int price() {
		return 15 * quantity;
	}

	@Override
	public ItemSprite.Glowing glowing() {
		return spiceGlow;
	}

}
