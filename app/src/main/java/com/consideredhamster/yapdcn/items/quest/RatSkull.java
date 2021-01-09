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
package com.consideredhamster.yapdcn.items.quest;

import com.consideredhamster.yapdcn.items.Item;
import com.consideredhamster.yapdcn.visuals.sprites.ItemSpriteSheet;

public class RatSkull extends Item {
	
	{
		name = "巨鼠头骨";
		image = ItemSpriteSheet.SKULL;
		
		unique = true;
	}
	
	@Override
	public String info() {
		return
			"看起来是件不错的狩猎纪念品，但它的气味过于刺鼻完全没法挂在墙上。";
	}
	
	@Override
	public int price() {
		return 100;
	}
}
