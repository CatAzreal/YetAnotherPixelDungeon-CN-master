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

import com.watabou.utils.Bundle;
import com.consideredhamster.yapdcn.Element;
import com.consideredhamster.yapdcn.Dungeon;
import com.consideredhamster.yapdcn.actors.Char;
import com.consideredhamster.yapdcn.visuals.effects.Lightning;
import com.consideredhamster.yapdcn.visuals.effects.particles.EnergyParticle;
import com.consideredhamster.yapdcn.items.misc.Gold;
import com.consideredhamster.yapdcn.levels.Level;
import com.consideredhamster.yapdcn.misc.mechanics.Ballistica;
import com.consideredhamster.yapdcn.visuals.sprites.WarlockSprite;

public class DwarfWarlock extends MobRanged {

    private boolean charged = false;

    private static final String CHARGED = "charged";

    public DwarfWarlock() {

        super( 15 );

        /*

            base maxHP  = 30
            armor class = 8

            damage roll = 7-14

            accuracy    = 35
            dexterity   = 20

            perception  = 140%
            stealth     = 100%

         */

		name = "矮人术士";
		spriteClass = WarlockSprite.class;
		
		loot = Gold.class;
		lootChance = 0.25f;

        resistances.put( Element.Unholy.class, Element.Resist.PARTIAL );
        resistances.put( Element.Dispel.class, Element.Resist.IMMUNE );

	}

    @Override
    public boolean act() {

        if( !enemySeen )
            charged = false;

        return super.act();

    }

    @Override
    protected boolean doAttack( Char enemy ) {

        if( !Level.adjacent( pos, enemy.pos ) && !charged ) {

            charged = true;

            if( Dungeon.visible[ pos ] ) {
                sprite.centerEmitter().burst(EnergyParticle.FACTORY_BLUE, 20);
            }

            spend( attackDelay() );

            return true;

        } else {

            charged = false;

            return super.doAttack( enemy );
        }
    }

	@Override
	protected boolean canAttack( Char enemy ) {
        return super.canAttack( enemy ) || Ballistica.cast( pos, enemy.pos, false, true ) == enemy.pos;
	}

    @Override
    protected void onRangedAttack( int cell ) {

        sprite.parent.add( new Lightning( pos, cell ) );

        onCastComplete();

        super.onRangedAttack(cell);

    }

    @Override
	public boolean cast( Char enemy ) {

//        if ( hit( this, enemy, false, true ) ) {

            enemy.damage( damageRoll() + damageRoll(), this, Element.SHOCK);

            return true;

//        } else {
//            enemy.missed();
//        }
//
//        return false;
	}
	
//	public void onZapComplete() {
//		cast();
//		next();
//	}
	
//	@Override
//	public void call() {
//		next();
//	}
	
	@Override
	public String description() {
		return
			"当矮人的兴趣从工程建设转向奥秘学术时，术士们开始在城市中掌权。它们从元素魔法起步，但很快就开始研究恶魔学和死灵术。";
	}

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle(bundle);
        bundle.put( CHARGED, charged );
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle(bundle);
        charged = bundle.getBoolean( CHARGED );
    }
}
