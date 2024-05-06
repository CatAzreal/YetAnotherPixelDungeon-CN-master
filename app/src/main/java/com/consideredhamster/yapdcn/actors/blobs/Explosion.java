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
package com.consideredhamster.yapdcn.actors.blobs;

import com.consideredhamster.yapdcn.Element;
import com.consideredhamster.yapdcn.actors.buffs.BuffActive;
import com.consideredhamster.yapdcn.actors.buffs.debuffs.Vertigo;
import com.consideredhamster.yapdcn.Dungeon;
import com.consideredhamster.yapdcn.actors.Actor;
import com.consideredhamster.yapdcn.actors.Char;
import com.consideredhamster.yapdcn.visuals.effects.CellEmitter;
import com.consideredhamster.yapdcn.visuals.effects.particles.BlastParticle;
import com.consideredhamster.yapdcn.visuals.effects.particles.SmokeParticle;
import com.consideredhamster.yapdcn.items.Heap;
import com.consideredhamster.yapdcn.levels.Level;
import com.consideredhamster.yapdcn.levels.Terrain;
import com.consideredhamster.yapdcn.scenes.GameScene;

public class Explosion {

    // FIXME
    private static Explosion EXPLOSION = new Explosion();

	// Returns true, if this cell is visible
	public static boolean affect( int c, int r, int radius, int damage, Object source ) {

        boolean terrainAffected = false;

        if (Dungeon.visible[c]) {
            CellEmitter.get(c).burst( BlastParticle.FACTORY, 12 / ( r + 1 ) );
            CellEmitter.get(c).burst( SmokeParticle.FACTORY, 6 / ( r + 1 ) );
        }

        if (Level.flammable[c]) {
            Level.set(c, Terrain.EMBERS);
            GameScene.updateMap(c);
            terrainAffected = true;
        }

        Char ch = Actor.findChar(c);

        if (ch != null) {

//            int mod = ch.HT * damage /
//                ( Bestiary.isBoss(ch) ? 200 : ch instanceof Hero ? 100 : 50 );
//
//            int dmg = Random.IntRange( mod / 2, mod );
//            dmg += Random.IntRange( damage / 2, damage );
//            dmg *= ( radius - r + 2 );
//            dmg /= ( radius + 2 );

            if (damage > 0) {
                ch.damage(Char.absorb(damage, ch.armorClass(), true ), EXPLOSION, Element.EXPLOSION);
                if ( ch.isAlive() ) {
                    BuffActive.addFromDamage( ch, Vertigo.class, damage );
                }
            }
        }

        if( Dungeon.hero.isAlive() ) {
            Heap heap = Dungeon.level.heaps.get(c);
            if (heap != null) {
                heap.blast();
            }
        }

        Dungeon.level.press(c, null);

        return terrainAffected;

	}
}
