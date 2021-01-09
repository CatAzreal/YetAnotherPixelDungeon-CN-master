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
package com.consideredhamster.yapdcn.items.food;

import com.consideredhamster.yapdcn.actors.buffs.special.Satiety;
import com.consideredhamster.yapdcn.visuals.sprites.ItemSpriteSheet;

public class MeatBurned extends Food {

	{
		name = "烧焦的肉";
		image = ItemSpriteSheet.BURNED_MEAT;

		energy = Satiety.MAXIMUM * 0.15f;
        message = "这块肉吃起来...糟透了";
	}
	
	@Override
	public String desc() {
		return "这份肉上沾满了炭灰和结块。它仍可食用，但必然是赶不上正常煮熟的肉的。";
	}
	
	@Override
	public int price() {
		return 5 * quantity;
	}

}
