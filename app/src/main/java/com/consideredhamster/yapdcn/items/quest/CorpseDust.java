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

public class CorpseDust extends Item {
	
	{
		name = "尸尘";
		image = ItemSpriteSheet.DUST;
		
//		cursed = true;
//		cursedKnown = true;
//
		unique = true;
	}
	
	@Override
	public String info() {
		return
			"在外观上这团尸尘和普通灰尘差不多。但直觉告诉你尽快脱手为好。";
	}
}
