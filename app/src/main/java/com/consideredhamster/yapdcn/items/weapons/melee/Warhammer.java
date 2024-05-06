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

public class Warhammer extends MeleeWeaponHeavyTH {

	{
		name = "战锤";
		image = ItemSpriteSheet.WARHAMMER;
	}
	
	public Warhammer() {
		super( 3 );
	}

    @Override
    public int max( int bonus ) {
        return super.max(bonus) - 3;
    }

    @Override
    public int lootChapter() {
        return super.lootChapter() - 1;
    }

	@Override
	public Type weaponType() {
		return Type.M_BLUNT;
	}
	
	@Override
	public String desc() {
		return 
			"很少有生物能抵挡这铅铁巨块的辗压，但也只有最强壮的冒险者才能有效使用它。";
	}
}
