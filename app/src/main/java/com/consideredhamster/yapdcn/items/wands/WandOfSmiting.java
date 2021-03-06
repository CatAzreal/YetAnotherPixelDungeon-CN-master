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

import com.consideredhamster.yapdcn.Element;
import com.consideredhamster.yapdcn.actors.buffs.BuffActive;
import com.consideredhamster.yapdcn.actors.buffs.debuffs.Blinded;
import com.consideredhamster.yapdcn.misc.mechanics.Ballistica;
import com.consideredhamster.yapdcn.visuals.effects.CellEmitter;
import com.consideredhamster.yapdcn.visuals.effects.Flare;
import com.consideredhamster.yapdcn.visuals.effects.HolyLight;
import com.consideredhamster.yapdcn.visuals.effects.Speck;
import com.consideredhamster.yapdcn.visuals.effects.SpellSprite;
import com.consideredhamster.yapdcn.visuals.effects.particles.ShaftParticle;
import com.consideredhamster.yapdcn.visuals.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;
import com.consideredhamster.yapdcn.visuals.Assets;
import com.consideredhamster.yapdcn.Dungeon;
import com.consideredhamster.yapdcn.actors.Actor;
import com.consideredhamster.yapdcn.actors.Char;
import com.consideredhamster.yapdcn.levels.Level;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

public class WandOfSmiting extends WandCombat {

	{
		name = "神罚法杖";
        image = ItemSpriteSheet.WAND_SMITING;

        goThrough = false;
        hitChars = false;
	}

    @Override
    public float effectiveness( int bonus ) {
        return super.effectiveness( bonus ) * 1.05f;
    }

	@Override
	protected void onZap( int cell ) {

        if( Level.solid[ cell ] ) {
            cell = Ballistica.trace[ Ballistica.distance - 1 ];
        }

		for ( int n : Level.NEIGHBOURS9 ){

            Char ch = Actor.findChar( cell + n );

            if( ch != null && ch != curUser ){

                int power = n == 0 ? damageRoll() : damageRoll() / 2 ;

                ch.damage( power, curUser, Element.ENERGY );

                if( ch.isMagical() ) {
                    power += ( power / 2 + Random.Int( power % 2 + 1 ) );
                }

                if( Random.Int( 4 ) == 0 ){
                    BuffActive.add( ch, Blinded.class, power );
                }

                if( ch.sprite.visible ){
                    ch.sprite.emitter().start( Speck.factory( Speck.HOLY ), 0.05f, n == 0 ? 6 : 3 );
                }
            }
        }

	}

    @Override
	protected void fx( int cell, Callback callback ) {

        if( Level.solid[ cell ] ) {
            cell = Ballistica.trace[ Ballistica.distance - 1 ];
        }

        if( Dungeon.visible[ cell ] ){

            Sample.INSTANCE.play( Assets.SND_LEVELUP, 1.0f, 1.0f, 0.5f );

            new Flare( 6, 24 ).color( SpellSprite.COLOUR_HOLY, true ).show( cell, 1f );
            CellEmitter.top( cell ).burst( ShaftParticle.FACTORY, 4 );
//            GameScene.flash( SpellSprite.COLOUR_HOLY - 0x555555 );
            HolyLight.createAtPos( cell );

        }

        Sample.INSTANCE.play( Assets.SND_ZAP );

        callback.call();
    }
	
	@Override
	public String desc() {
		return
                "这根闪耀着光辉的法杖可以让使用者传导并迸射出光明之力，对范围内的敌人造成伤害，甚至可能导致其目盲。其效果在对抗不死和秘法生物时尤其有效。";
//			"This gilded piece of wood allows its user to channel and release bursts of hallowed " +
//            "energy, harming and sometimes even blinding any wrongdoer caught in its area of " +
//            "effect. Its effects are even stronger against undead or magical foes.";
	}
}
