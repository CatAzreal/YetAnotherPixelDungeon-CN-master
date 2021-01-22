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
package com.consideredhamster.yapdcn.items.weapons.ranged;

import com.consideredhamster.yapdcn.items.weapons.throwing.Arrows;
import com.consideredhamster.yapdcn.items.weapons.throwing.ThrowingWeaponAmmo;
import com.consideredhamster.yapdcn.visuals.sprites.ItemSpriteSheet;

public class Bow extends RangedWeaponMissile {

	{
		name = "长弓";
		image = ItemSpriteSheet.BOW;
	}

	public Bow() {
		super( 2 );
	}

    @Override
    public Class<? extends ThrowingWeaponAmmo> ammunition() {
        return Arrows.class;
    }

	@Override
	public Type weaponType() {
		return Type.R_MISSILE;
	}

	@Override
	public String desc() {
		return "在熟练的弓手面前，这件武器能发挥出难以想象的效力。";
	}
}
