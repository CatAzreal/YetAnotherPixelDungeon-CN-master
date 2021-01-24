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

import com.consideredhamster.yapdcn.actors.buffs.BuffActive;
import com.consideredhamster.yapdcn.items.scrolls.ScrollOfPhaseWarp;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;
import com.consideredhamster.yapdcn.visuals.Assets;
import com.consideredhamster.yapdcn.Element;
import com.consideredhamster.yapdcn.actors.Actor;
import com.consideredhamster.yapdcn.actors.Char;
import com.consideredhamster.yapdcn.actors.buffs.debuffs.Charmed;
import com.consideredhamster.yapdcn.visuals.effects.MagicMissile;
import com.consideredhamster.yapdcn.visuals.effects.Speck;
import com.consideredhamster.yapdcn.levels.Level;
import com.consideredhamster.yapdcn.misc.mechanics.Ballistica;
import com.consideredhamster.yapdcn.visuals.sprites.SuccubusSprite;

public class Succubus extends MobPrecise {
	
	private static final int BLINK_DELAY = 6;
	
	private int delay = 0;

    public Succubus() {

        super( 18 );

        /*

            base maxHP  = 43
            armor class = 5

            damage roll = 6-21

            accuracy    = 36
            dexterity   = 30

            perception  = 125%
            stealth     = 125%

         */

        name = "魅魔";
        spriteClass = SuccubusSprite.class;

        armorClass /= 3;

        resistances.put(Element.Mind.class, Element.Resist.PARTIAL);
        resistances.put(Element.Unholy.class, Element.Resist.PARTIAL);
        resistances.put( Element.Dispel.class, Element.Resist.PARTIAL );

	}

    @Override
    public boolean isMagical() {
        return true;
    }

    @Override
    protected boolean canAttack( Char enemy ) {
        return ( super.canAttack( enemy ) ||
            Ballistica.cast(pos, enemy.pos, false, true) == enemy.pos
            && enemy.buff( Charmed.class) != null );
    }

    @Override
    protected void onRangedAttack( int cell ) {

        MagicMissile.purpleLight(sprite.parent, pos, cell,
                new Callback() {
                    @Override
                    public void call() {
                        onCastComplete();
                    }
                });

        Sample.INSTANCE.play(Assets.SND_ZAP);

        onCastComplete();

        super.onRangedAttack( cell );
    }

    @Override
    public boolean cast( Char enemy ) {

        if ( hit( this, enemy, true, true ) ) {

            Charmed buff = BuffActive.addFromDamage( enemy, Charmed.class, damageRoll() );

            if( buff != null ) {
//                buff.object = this.id();
                enemy.sprite.centerEmitter().start( Speck.factory(Speck.HEART), 0.2f, 5 );
            }

        } else {

            enemy.missed();

        }

        return true;
    }

    @Override
    public int attackProc( Char enemy, int damage, boolean blocked ) {

        if ( !blocked && isAlive() ) {

            int healed = Element.Resist.modifyValue( damage / 2, enemy, Element.BODY );

            if ( healed > 0 ) {

                heal( healed );

                if( sprite.visible ) {
                    sprite.emitter().burst(Speck.factory(Speck.HEALING), 1);
                }
            }
        }

        return damage;
    }

//    public void onZapComplete() {
//        zap();
//        next();
//    }
	
	@Override
	protected boolean getCloser( int target ) {
		if (delay <= 0 && enemySeen && enemy != null && Level.fieldOfView[target]
            && Level.distance( pos, target ) > 1 && enemy.buff( Charmed.class ) != null
            && Ballistica.cast( pos, enemy.pos, false, true ) == enemy.pos ) {

			blink( target );
			spend( -2 / moveSpeed() );
			return true;

		} else {

			delay--;
			return super.getCloser( target );

		}
	}
	
	private void blink( int target ) {
		
		int cell = Ballistica.cast( pos, target, false, true );
		
		if (Actor.findChar( cell ) != null && Ballistica.distance > 1) {
			cell = Ballistica.trace[Ballistica.distance - 1];
		}

        ScrollOfPhaseWarp.appear( this, cell );
		
		delay = BLINK_DELAY;
	}
	
	@Override
	public String description() {
		return
			"魅魔是一种看上去就很吸引人（以一种略带哥特式的打扮）的恶魔生物，恶魔的魅力能让它们诱惑凡人，使他们没法直接伤害正折磨他们的人，并更容易受到魅魔的伤害。";
	}
}
