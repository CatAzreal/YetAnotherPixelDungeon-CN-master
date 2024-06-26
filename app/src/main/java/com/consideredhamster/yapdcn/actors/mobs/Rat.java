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
package com.consideredhamster.yapdcn.actors.mobs;

import com.consideredhamster.yapdcn.Element;
import com.consideredhamster.yapdcn.visuals.sprites.RatSprite;

public class Rat extends MobEvasive {

    public Rat() {

        super( 1 );

        /*

            base maxHP  = 5
            armor class = 1

            damage roll = 0-4

            accuracy    = 5
            dexterity   = 7

            perception  = 100%
            stealth     = 110%

         */

        name = "噬齿小鼠";
        info = "无";

        spriteClass = RatSprite.class;

        minDamage += 1;

        resistances.put( Element.Dispel.class, Element.Resist.IMMUNE );
        resistances.put( Element.Knockback.class, Element.Resist.VULNERABLE );

    }

//	@Override
//	public void die( Object cause, Element dmg ) {
//		Ghost.Quest.process( pos );
//
//		super.die( cause, dmg );
//	}
	
	@Override
	public String description() {
		return "这种老鼠在这座城市居住的时间几乎和下水道存在的时间相当，不久前有传言称这些老鼠会攻击宠物，幼儿，有时甚至是成年人。";
	}
}
