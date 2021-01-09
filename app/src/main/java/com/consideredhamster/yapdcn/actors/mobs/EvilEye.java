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

import com.consideredhamster.yapdcn.items.food.MeatRaw;
import com.watabou.noosa.audio.Sample;
import com.consideredhamster.yapdcn.visuals.Assets;
import com.consideredhamster.yapdcn.Element;
import com.consideredhamster.yapdcn.Dungeon;
import com.consideredhamster.yapdcn.actors.Actor;
import com.consideredhamster.yapdcn.actors.Char;
import com.consideredhamster.yapdcn.visuals.effects.CellEmitter;
import com.consideredhamster.yapdcn.visuals.effects.DeathRay;
import com.consideredhamster.yapdcn.visuals.effects.particles.PurpleParticle;
import com.consideredhamster.yapdcn.levels.Level;
import com.consideredhamster.yapdcn.levels.Terrain;
import com.consideredhamster.yapdcn.misc.mechanics.Ballistica;
import com.consideredhamster.yapdcn.scenes.GameScene;
import com.consideredhamster.yapdcn.visuals.sprites.EyeSprite;
import com.watabou.utils.Random;

public class EvilEye extends MobRanged {

    public EvilEye() {

        super( 11 );

        /*

            base maxHP  = 23
            armor class = 6

            damage roll = 5-11

            accuracy    = 27
            dexterity   = 15

            perception  = 130%
            stealth     = 100%

         */

		name = "邪眼";
		spriteClass = EyeSprite.class;
		
		flying = true;
        loot = new MeatRaw();
        lootChance = 0.35f;

        resistances.put(Element.Energy.class, Element.Resist.PARTIAL);

        resistances.put( Element.Dispel.class, Element.Resist.IMMUNE );
        resistances.put( Element.Knockback.class, Element.Resist.VULNERABLE );

	}

    @Override
	protected boolean getCloser( int target ) {
		if (state == HUNTING && HP >= HT && (enemySeen || enemy != null && detected( enemy ))) {
			return getFurther( target );
		} else {
			return super.getCloser( target );
		}
	}

    @Override
    protected boolean canAttack( Char enemy ) {
        return ( HP < HT || !Level.adjacent( pos, enemy.pos ) ) &&
                Ballistica.cast( pos, enemy.pos, false, false ) == enemy.pos;
    }

    @Override
    protected void onRangedAttack( int cell ) {

        Sample.INSTANCE.play(Assets.SND_RAY);

        sprite.parent.add( new DeathRay( pos, cell ) );

        onCastComplete();

        super.onRangedAttack( cell );

    }

    @Override
    public boolean cast( Char enemy ) {

        boolean terrainAffected = false;

        for (int i=1; i <= Ballistica.distance ; i++) {

            int pos = Ballistica.trace[i];

            int terr = Dungeon.level.map[pos];

            if (terr == Terrain.DOOR_CLOSED) {

                Level.set(pos, Terrain.EMBERS);
                GameScene.updateMap(pos);
                terrainAffected = true;

            } else if (terr == Terrain.HIGH_GRASS) {

                Level.set( pos, Terrain.GRASS );
                GameScene.updateMap( pos );
                terrainAffected = true;

            }

            Char ch = Actor.findChar( pos );

            if (ch != null) {

//                if (hit(this, ch, false, true)) {

                    ch.damage( absorb( damageRoll(), enemy.armorClass(), true ), this, Element.ENERGY );

                    if (Dungeon.visible[pos]) {
                        ch.sprite.flash();
                        CellEmitter.center(pos).burst(PurpleParticle.BURST, Random.IntRange(1, 2));
                    }

//                } else {
//                    enemy.missed();
//                }
            }
        }

        if (terrainAffected) {
            Dungeon.observe();
        }

        return true;
    }
	
	@Override
	public String description() {
		return
			"One of this creature's other names is \"orb of hatred\", because when it sees an enemy, " +
			"it uses its deathgaze recklessly, often ignoring its allies and wounding them.";
	}
}
