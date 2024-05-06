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
package com.consideredhamster.yetanotherpixeldungeon.actors.mobs;

import com.consideredhamster.yetanotherpixeldungeon.Element;
import com.consideredhamster.yetanotherpixeldungeon.Dungeon;
import com.consideredhamster.yetanotherpixeldungeon.actors.hero.HeroClass;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.RatSprite;

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
		return "这种老鼠在这座城市居住的时间几乎和下水道存在的时间相当，不久前有传言称这些老鼠会攻击宠物，幼儿，有时甚至是成年人。"

                    + ( Dungeon.hero.heroClass == HeroClass.WARRIOR ?
                    "它们并不配做你的对手，然而在数量众多时仍然非常危险。" : "" )

                    + ( Dungeon.hero.heroClass == HeroClass.SCHOLAR ?
                    "毫无疑问，这些生物并不是这里的主要威胁，但它们不自然的攻击性可能令人不安。" : "" )

                    + ( Dungeon.hero.heroClass == HeroClass.BRIGAND ?
                    "为什么，为什么就一定要是老鼠呢……？" : "" )

                    + ( Dungeon.hero.heroClass == HeroClass.ACOLYTE ?
                    "某种邪恶的存在扭曲了这些小动物的思想，预示着一些事情即将到来。" : "" )

                ;
	}
}
