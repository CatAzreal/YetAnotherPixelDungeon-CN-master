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
import com.consideredhamster.yapdcn.actors.Char;
import com.consideredhamster.yapdcn.actors.buffs.special.Satiety;
import com.consideredhamster.yapdcn.actors.mobs.npcs.Ghost;
import com.consideredhamster.yapdcn.visuals.sprites.SwarmSprite;

public class CarrionSwarm extends MobEvasive {

    public CarrionSwarm() {

        super( 5 );

        /*

            base maxHP  = 11
            armor class = 2

            damage roll = 1-7

            accuracy    = 11
            dexterity   = 14

            perception  = 100%
            stealth     = 120%

         */

        name = "食腐蝇群";
        info = "飞行, 吸取饱食度, 食物感知";

        spriteClass = SwarmSprite.class;
        flying = true;

        resistances.put( Element.Knockback.class, Element.Resist.VULNERABLE );
        resistances.put( Element.Mind.class, Element.Resist.VULNERABLE );
        resistances.put( Element.Dispel.class, Element.Resist.IMMUNE );

	}

//    @Override
//    public float attackDelay() {
//        return 0.5f;
//    }

    @Override
    public String description() {
        return
                "致命的蝇群愤怒地嗡嗡作响，这些不洁的敌人的嗅觉对任何能吃的东西都异常敏感 ";
    }

    @Override
    public int attackProc( Char enemy, int damage, boolean blocked  ) {

        if( !blocked && damage > 0 ){

            Satiety hunger = enemy.buff( Satiety.class );

            if( hunger != null ){

                hunger.decrease( Satiety.POINT * 10 );

            }

        }

        return damage;
    }

    @Override
    public void die( Object cause, Element dmg ) {
        Ghost.Quest.process( pos );
        super.die( cause, dmg );
    }
}
