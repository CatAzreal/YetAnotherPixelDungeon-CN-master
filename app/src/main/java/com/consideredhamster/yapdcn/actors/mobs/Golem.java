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
import com.consideredhamster.yapdcn.actors.mobs.npcs.AmbitiousImp;
import com.consideredhamster.yapdcn.actors.special.Pushing;
import com.consideredhamster.yapdcn.visuals.sprites.GolemSprite;
import com.watabou.utils.Random;

public class Golem extends MobHealthy {

    public Golem() {

        super( 16 );

        /*

            base maxHP  = 41
            armor class = 16

            damage roll = 7-22

            accuracy    = 20
            dexterity   = 8

            perception  = 80%
            stealth     = 80%

         */

		name = "石造魔像";
		info = "魔法造物, 行动缓慢, 击退攻击";

		spriteClass = GolemSprite.class;
        dexterity /= 2;

        resistances.put( Element.Flame.class, Element.Resist.PARTIAL );
        resistances.put( Element.Frost.class, Element.Resist.PARTIAL );
        resistances.put( Element.Shock.class, Element.Resist.PARTIAL );
        resistances.put( Element.Energy.class, Element.Resist.PARTIAL );
        resistances.put( Element.Unholy.class, Element.Resist.PARTIAL );

        resistances.put( Element.Mind.class, Element.Resist.IMMUNE );
        resistances.put( Element.Body.class, Element.Resist.IMMUNE );

        resistances.put( Element.Knockback.class, Element.Resist.PARTIAL );
        resistances.put( Element.Doom.class, Element.Resist.PARTIAL );

	}

    @Override
    public boolean isMagical() {
        return true;
    }
	
	@Override
	public float attackSpeed() {
		return 0.75f;
	}

    @Override
    public float moveSpeed() {
        return 0.75f;
    }

    @Override
    public int attackProc( final Char enemy, int damage, boolean blocked ) {

        if( Random.Int( 10 ) < tier ) {
            Pushing.knockback( enemy, pos, 1, damage / 2 );
        }

        return damage;
    }
	
	@Override
	public void die( Object cause, Element dmg ) {
		AmbitiousImp.Quest.process( this );
		
		super.die( cause, dmg );
	}
	
	@Override
	public String description() {
		return
                "矮人们尝试将他们的机械知识与新发现的元素力量结合起来，他们用大地之灵作为\"灵魂\"，以机械作为身躯，造出了这个魔像。人们认为它是最可控的，尽管这么说，在仪式中发生哪怕最微小的错误都可能发生爆炸。不过一切都是值得的，因为石造魔像的挥拳足以击退任何来犯之敌。";
	}
}
