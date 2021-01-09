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

public class DarkGold extends Item {
	
	{
		name = "暗金矿";
		image = ItemSpriteSheet.ORE;
		
		stackable = true;
		unique = true;
	}
	
	@Override
	public String info() {
		return
			"这种金属名中的暗并非源于其色泽(它看起来和普通金子一样)，而是因为它会在阳光下熔化，令其在地表上毫无用处。";
	}
	
	@Override
	public int price() {
		return quantity;
	}
}
