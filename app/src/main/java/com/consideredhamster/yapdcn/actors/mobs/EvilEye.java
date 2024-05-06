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
import com.consideredhamster.yapdcn.items.wands.WandOfDisintegration;
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

		name = "evil eye";
		info = "Flying, Disintegration ray";
		spriteClass = EyeSprite.class;
		
		flying = true;
        loot = new MeatRaw();
        lootChance = 0.175f;

        resistances.put(Element.Energy.class, Element.Resist.PARTIAL);

        resistances.put( Element.Dispel.class, Element.Resist.IMMUNE );
        resistances.put( Element.Knockback.class, Element.Resist.VULNERABLE );

	}

//    @Override
//	protected boolean getCloser( int target ) {
//		if (state == HUNTING && HP >= HT && (enemySeen || enemy != null && detected( enemy ))) {
//			return getFurther( target );
//		} else {
//			return super.getCloser( target );
//		}
//	}

    @Override
    protected boolean canAttack( Char enemy ) {
        return /*Level.adjacent( pos, enemy.pos ) &&*/ Ballistica.cast( pos, enemy.pos, false, false ) == enemy.pos;
    }

    @Override
    protected void onRangedAttack( int cell ) {
        onCastComplete();
        super.onRangedAttack( cell );
    }

    @Override
    public boolean attack( Char enemy ){
        shootRay( enemy.pos );
        return true;
    }

    private void shootRay( int target ) {

        boolean terrainAffected = false;

        int reflectFrom = Ballistica.cast( pos, target, true, false );

        sprite.parent.add( new DeathRay( pos, reflectFrom ) );
        Sample.INSTANCE.play( Assets.SND_RAY );

        for ( int i = 1 ; i <= Ballistica.distance ; i++ ) {
            terrainAffected = terrainAffected || burnTile( Ballistica.trace[i] );
        }

        if( Level.solid[ reflectFrom ] ){

            int reflectTo = WandOfDisintegration.getReflectTo( pos, reflectFrom );

            if( reflectFrom != reflectTo ){

                Ballistica.cast( reflectFrom, reflectTo, true, false );

                reflectTo = Ballistica.trace[ Ballistica.distance ] ;

                sprite.parent.add( new DeathRay( reflectFrom, reflectTo ) );

                for ( int i = 1 ; i <= Ballistica.distance ; i++ ) {
                    terrainAffected = terrainAffected || burnTile( Ballistica.trace[i] );
                }

            }
        }

        if (terrainAffected) {
            Dungeon.observe();
        }
    }

    private boolean burnTile( int cell ) {

        boolean terrainAffected = false;

        Char ch = Actor.findChar( cell );

        if ( ch != null ) {
            ch.damage( Char.absorb( damageRoll(), ch.armorClass(), true ), this, Element.ENERGY );
            CellEmitter.center( cell ).burst( PurpleParticle.BURST, 3 );
        }

        if ( Dungeon.level.map[cell] == Terrain.DOOR_CLOSED ) {

            Level.set( cell, Terrain.EMBERS );
            GameScene.updateMap( cell );
            terrainAffected = true;

            if( Dungeon.visible[ cell ] ){
                CellEmitter.center( cell ).burst( PurpleParticle.BURST, 16 );
            }

        } else if ( Dungeon.level.map[cell] == Terrain.HIGH_GRASS ) {

            Level.set( cell, Terrain.GRASS );
            GameScene.updateMap( cell );
            terrainAffected = true;

            if( Dungeon.visible[ cell ] ){
                CellEmitter.center( cell ).burst( PurpleParticle.BURST, 4 );
            }

        } else {

            if( Dungeon.visible[ cell ] ){
                CellEmitter.center( cell ).burst( PurpleParticle.BURST, 3 );
            }

        }

        return terrainAffected;
    }
	
	@Override
	public String description() {
		return
			"这种生物有着另一个名字：\"憎恨之珠\",因为当它看到敌人时，它会不顾一切地使用死亡凝视，经常忽视并击中它的友军。";
	}
}
