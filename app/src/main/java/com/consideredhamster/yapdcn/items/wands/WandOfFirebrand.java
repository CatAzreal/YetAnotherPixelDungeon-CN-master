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
package com.consideredhamster.yapdcn.items.wands;

import com.consideredhamster.yapdcn.Dungeon;
import com.consideredhamster.yapdcn.Element;
import com.consideredhamster.yapdcn.actors.Char;
import com.consideredhamster.yapdcn.actors.blobs.Blob;
import com.consideredhamster.yapdcn.actors.blobs.Fire;
import com.consideredhamster.yapdcn.actors.hazards.FieryRune;
import com.consideredhamster.yapdcn.actors.hazards.Hazard;
import com.consideredhamster.yapdcn.levels.Level;
import com.consideredhamster.yapdcn.visuals.effects.CellEmitter;
import com.consideredhamster.yapdcn.visuals.effects.Speck;
import com.consideredhamster.yapdcn.visuals.effects.particles.BlastParticle;
import com.consideredhamster.yapdcn.visuals.sprites.ItemSpriteSheet;
import com.watabou.noosa.Camera;
import com.watabou.noosa.audio.Sample;
import com.consideredhamster.yapdcn.visuals.Assets;
import com.consideredhamster.yapdcn.visuals.effects.MagicMissile;
import com.consideredhamster.yapdcn.scenes.GameScene;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

public class WandOfFirebrand extends WandCombat {

	{
		name = "火纹法杖";
        image = ItemSpriteSheet.WAND_FIREBRAND;

        goThrough = false;
	}

    @Override
    public float effectiveness( int bonus ) {
        return super.effectiveness( bonus ) * 1.00f;
    }

	@Override
	protected void onZap( int cell ) {

        Char ch = Char.findChar( cell );

        if( ch != null ) {

            ch.damage( damageRoll(), curUser, Element.FLAME );

            if (Level.flammable[ cell ]) {
                GameScene.add( Blob.seed( cell, 3, Fire.class ) );
            }

            if( Dungeon.visible[ cell ] ){
                CellEmitter.get( cell ).burst( BlastParticle.FACTORY, 3 );
                CellEmitter.get( cell ).start( Speck.factory( Speck.BLAST_FIRE, true ), 0.05f, 6 );
            }

            Sample.INSTANCE.play( Assets.SND_BLAST, 1.0f, 1.0f, Random.Float( 1.0f, 1.5f ) );
            Sample.INSTANCE.play( Assets.SND_BLAST, 1.0f, 1.0f, Random.Float( 0.5f, 1.0f ) );
            Camera.main.shake( 1, 0.2f );

        } else if( Level.solid[ cell ] ){

            if( Level.flammable[ cell ] ){
                GameScene.add( Blob.seed( cell, 3, Fire.class ) );
            }

            if( Dungeon.visible[ cell ] ){
                CellEmitter.get( cell ).burst( BlastParticle.FACTORY, 3 );
                CellEmitter.get( cell ).start( Speck.factory( Speck.BLAST_FIRE, true ), 0.05f, 6 );
            }

            Sample.INSTANCE.play( Assets.SND_BLAST, 1.0f, 1.0f, Random.Float( 1.0f, 1.5f ) );
            Sample.INSTANCE.play( Assets.SND_BLAST, 1.0f, 1.0f, Random.Float( 0.5f, 1.0f ) );

            Camera.main.shake( 1, 0.2f );

        } else {

            FieryRune rune = Hazard.findHazard( cell, FieryRune.class );

            int strength = damageRoll() * 3 / 2;

            if( rune == null ){

                rune = new FieryRune();
                rune.setValues( cell, strength, damageRoll() );

                GameScene.add( rune );
                ( (FieryRune.RuneSprite) rune.sprite ).appear();

            } else {

                rune.upgrade( strength / 2, damageRoll() );

            }
        }
	}
	
	protected void fx( int cell, Callback callback ) {
		MagicMissile.fire( curUser.sprite.parent, curUser.pos, cell, callback );
		Sample.INSTANCE.play( Assets.SND_ZAP );
	}
	
	@Override
	public String desc() {
		return
                "用寻常方式使用这根法杖只能喷射出一阵烈焰。但对准地表使用时这根法杖将在地面上蚀刻一个临时性的火" +
                        "焰阵法，在被其他物体触发时产生爆炸。对已有火纹施法能够进一步加强阵法的效力，持续时长和爆炸范围。";
//			"Using this wand will release a single blast of flame. Using it on the ground, however, " +
//            "creates a temporary fiery rune, which will explode when triggered by another object. " +
//            "Repeated castings will enhance this rune, increasing its power, duration and area of effect.";
	}
}
