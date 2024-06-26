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

import com.watabou.utils.Callback;
import com.consideredhamster.yapdcn.Element;
import com.consideredhamster.yapdcn.Dungeon;
import com.consideredhamster.yapdcn.actors.Char;
import com.consideredhamster.yapdcn.actors.mobs.npcs.Ghost;
import com.consideredhamster.yapdcn.items.weapons.throwing.Arrows;
import com.consideredhamster.yapdcn.items.weapons.throwing.Bullets;
import com.consideredhamster.yapdcn.items.weapons.throwing.Javelins;
import com.consideredhamster.yapdcn.items.weapons.throwing.Quarrels;
import com.consideredhamster.yapdcn.misc.mechanics.Ballistica;
import com.consideredhamster.yapdcn.visuals.sprites.GnollSprite;
import com.consideredhamster.yapdcn.visuals.sprites.MissileSprite;

public class GnollHunter extends MobRanged {

    public GnollHunter() {

        super( Dungeon.chapter() * 5 - 2 );

        /*

            base maxHP  = 9/12/15
            armor class = 2/4/6

            damage roll = 2-5/4-8/6-11

            accuracy    = 11/15/19
            dexterity   = 5/6/7

            perception  = 110%/120%/130%
            stealth     = 100%/100%/100%

         */

		name = "豺狼猎手";
		info = "标枪投掷";

		spriteClass = GnollSprite.class;

        resistances.put( Element.Dispel.class, Element.Resist.IMMUNE );

        switch( Dungeon.chapter() ) {
            case 1:
                loot = Bullets.class;
                lootChance = 0.25f;
                break;
            case 2:
                loot = Arrows.class;
                lootChance = 0.375f;
                break;
            case 3:
                loot = Quarrels.class;
                lootChance = 0.5f;
                break;
            default:
                loot = Bullets.class;
                lootChance = 0.5f;
                break;
        }
	}

    @Override
    protected boolean canAttack( Char enemy ) {
        return super.canAttack( enemy ) || Ballistica.cast( pos, enemy.pos, false, true ) == enemy.pos;
    }

    @Override
    protected void onRangedAttack( int cell ) {
        ((MissileSprite)sprite.parent.recycle( MissileSprite.class )).
            reset(pos, cell, new Javelins(), new Callback() {
                @Override
                public void call() {
                    onAttackComplete();
                }
            });

        super.onRangedAttack( cell );
    }

	@Override
	public void die( Object cause, Element dmg ) {
		Ghost.Quest.process( pos );
		super.die( cause, dmg );
	}
	
	@Override
	public String description() {

        return "豺狼人是有着土狼模样的人形生物。说来奇怪，它们很少被发现有如此接近人类居住点的行为，而更喜欢到荒郊野外栖身。";
	}

}
