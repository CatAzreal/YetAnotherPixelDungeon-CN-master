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

import com.consideredhamster.yapdcn.items.weapons.throwing.Quarrels;
import com.consideredhamster.yapdcn.items.weapons.throwing.ThrowingWeaponAmmo;
import com.consideredhamster.yapdcn.visuals.sprites.ItemSpriteSheet;

public class Arbalest extends RangedWeaponMissile {

	{
		name = "十字弩";
		image = ItemSpriteSheet.ARBALEST;
	}

	public Arbalest() {
		super( 3 );
	}

    @Override
    public Class<? extends ThrowingWeaponAmmo> ammunition() {
        return Quarrels.class;
    }

	@Override
	public Type weaponType() {
		return Type.R_MISSILE;
	}
	
	@Override
	public String desc() {
		return "这件诡秘的器械允许使用者以鬼神之力射出弩箭，但只有训练有素的强壮身躯才能将弦再度拉起。";
	}
}
