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
import com.consideredhamster.yapdcn.actors.Actor;
import com.consideredhamster.yapdcn.actors.Char;
import com.consideredhamster.yapdcn.levels.Level;
import com.consideredhamster.yapdcn.visuals.Assets;
import com.consideredhamster.yapdcn.visuals.effects.Lightning;
import com.consideredhamster.yapdcn.visuals.effects.Speck;
import com.consideredhamster.yapdcn.visuals.sprites.DM100Sprite;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;

public class DM100 extends MobHealthy {

    public DM100() {

        super( 4, 9, false );

		name = "DM-100";
		info = "高速移动, 修复伙伴";
		spriteClass = DM100Sprite.class;

		maxDamage /= 2;
		dexterity /= 2;
		EXP += EXP / 2;

		resistances.put( Element.Acid.class, Element.Resist.PARTIAL );
		resistances.put( Element.Flame.class, Element.Resist.PARTIAL );
		resistances.put( Element.Frost.class, Element.Resist.PARTIAL );
		resistances.put( Element.Unholy.class, Element.Resist.PARTIAL );
		resistances.put( Element.Physical.class, Element.Resist.PARTIAL );
	
		resistances.put( Element.Doom.class, Element.Resist.IMMUNE );
		resistances.put( Element.Mind.class, Element.Resist.IMMUNE );
		resistances.put( Element.Body.class, Element.Resist.IMMUNE );
		resistances.put( Element.Dispel.class, Element.Resist.IMMUNE );
		resistances.put( Element.Knockback.class, Element.Resist.IMMUNE );

	}

    @Override
    public float moveSpeed() {
        return 2.0f;
    }
    
    @Override
	public boolean act() {
	
		if ( state == SLEEPING ) {
		
			spend( TICK );
			return true;
		
		}

		Char ally = null;

		for ( int n : Level.NEIGHBOURS8 ) {

			Char ch = Actor.findChar(pos + n );

			if ( ch instanceof DM100 || ch instanceof DM300 ) {
				// checking who is more damaged
				if ( ally == null || ( ch.HT - ch.HP ) > ( ally.HT - ally.HP ) ) {
					ally = ch;
				}
			}
		}

		if ( ally != null && ally.HP < ally.HT ) {

			final int allyPos = ally.pos;

			sprite.cast( allyPos, new Callback() {
				@Override
				public void call() { repair( allyPos ); }
			}  );
			sprite.parent.add( new Lightning( pos, allyPos ) );

			spend( TICK );
			return false;

		} else {
			
			return super.act();
			
		}
	}

	public void repair( int pos ) {

		Char ch = Actor.findChar(pos );
		ch.heal( damageRoll() / 2 );

		Sample.INSTANCE.play(Assets.SND_LIGHTNING);
		sprite.emitter().burst( Speck.factory( Speck.HEALING ), 1 );

		next();
	}
	
	@Override
	public String description() {
		return
				"这些机器是矮人数世纪前的造物。此后，矮人文明逐渐用各类魔像，元素生物乃至恶魔造物取代了这些机器，并由此走向衰败。DM-100以及其更大的型号通常被用于建造或采矿用途，有时也会用于守卫都城。";
	}
}