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


public class ClothArmor extends BodyArmorCloth {

	{	
		name = "布衣";

        image = ItemSpriteSheet.ARMOR_HUNTRESS;

        appearance = 1;
	}
	
	public ClothArmor() {
		super( 1 );
	}
	
	@Override
	public String desc() {
		return "这件衣物比起抵挡敌人攻击更适合御寒。不过总比没有来的强。";
	}
}
