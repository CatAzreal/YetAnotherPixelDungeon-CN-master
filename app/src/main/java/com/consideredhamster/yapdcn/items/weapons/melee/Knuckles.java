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

import com.consideredhamster.yapdcn.actors.hero.Hero;
import com.consideredhamster.yapdcn.visuals.sprites.ItemSpriteSheet;

public class Knuckles extends MeleeWeaponLightOH {

	{
		name = "指虎";
		image = ItemSpriteSheet.KNUCKLEDUSTER;
	}
	
	public Knuckles() {
		super( 1 );
	}

    @Override
    public int maxDurability() {

        return 150 ;

    }

    @Override
    public float speedFactor( Hero hero ) {

        return super.speedFactor( hero ) * 1.333f;

    }

//    @Override
//    public boolean disarmable() {
//        return false;
//    }

    @Override
    public int str(int bonus) {
        return super.str(bonus) + 1;
    }

    @Override
    public int max( int bonus ) {
        return super.max(bonus) + 3;
    }

    @Override
    public int lootChapter() {
        return super.lootChapter() + 1;
    }

    @Override
    public int penaltyBase(Hero hero, int str) {
        return super.penaltyBase(hero, str) + 4;
    }

    @Override
    public Type weaponType() {
        return Type.M_BLUNT;
    }
	
	@Override
	public String desc() {
		return "一块专门塑成契合指节形状的铁片。简约的设计允许使用者如同正常挥舞拳头一样做出快速攻击，而且几乎不可能脱手。";
	}
}
