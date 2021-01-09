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
package com.consideredhamster.yapdcn.items.armours.body;

import com.consideredhamster.yapdcn.visuals.sprites.ItemSpriteSheet;


public class StuddedArmor extends BodyArmorLight {

	{	
		name = "镶钉皮甲";
		image = ItemSpriteSheet.ARMOR_STUDDED;
        appearance = 4;
	}
	
	public StuddedArmor() { super( 1 ); }
	
	@Override
	public String desc() {
		return "鞣制皮革制成的护甲，其上通过镶钉强化了防御。不像布甲一般轻便但有着更好的防护能力。";
	}
}
