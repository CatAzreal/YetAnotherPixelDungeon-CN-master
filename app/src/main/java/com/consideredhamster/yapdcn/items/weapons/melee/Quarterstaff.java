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
package com.consideredhamster.yapdcn.items.weapons.melee;

import com.consideredhamster.yapdcn.visuals.sprites.ItemSpriteSheet;

public class Quarterstaff extends MeleeWeaponHeavyOH {

	{
		name = "短杖";
		image = ItemSpriteSheet.QUARTERSTAFF;
	}
	
	public Quarterstaff() {
		super( 1 );
	}

	@Override
	public Type weaponType() {
		return Type.M_POLEARM;
	}
	
	@Override
	public String desc() {
		return "这是一根两端以金属包覆的硬木短杖。尤其受法师欢迎，独特的设计使得该武器能够增强魔力传导，强化所有法杖的施法效果。";
	}
}
