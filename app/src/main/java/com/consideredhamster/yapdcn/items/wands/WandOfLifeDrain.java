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
import com.consideredhamster.yapdcn.misc.utils.GLog;
import com.consideredhamster.yapdcn.visuals.effects.DrainWill;
import com.consideredhamster.yapdcn.visuals.effects.Flare;
import com.consideredhamster.yapdcn.visuals.effects.DrainLife;
import com.consideredhamster.yapdcn.visuals.effects.SpellSprite;
import com.consideredhamster.yapdcn.visuals.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;
import com.consideredhamster.yapdcn.visuals.Assets;
import com.consideredhamster.yapdcn.actors.Actor;
import com.consideredhamster.yapdcn.actors.Char;
import com.consideredhamster.yapdcn.visuals.effects.Speck;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

public class WandOfLifeDrain extends WandUtility {

	{
		name = "窃血法杖";
        image = ItemSpriteSheet.WAND_LIFEDRAIN;
	}

    @Override
    public float effectiveness( int bonus ) {
        return super.effectiveness( bonus ) * 0.85f;
    }

    @Override
    protected void onZap( int cell ){

        Char ch = Actor.findChar( cell );

        if (ch != null ) {

            int damage = damageRoll();

            // zero dexterity usually means that target is asleep or wandering
            if( ch.dexterity() == 0 ) {
                damage += damage / 2 + Random.Int( damage % 2 + 1 );
//                ch.sprite.showStatus( CharSprite.DEFAULT, "sneak zap!" );
            }

            int healing = ch.HP;

            ch.damage( damage, curUser, Element.ENERGY );

            healing -= ch.HP;

            healing = Element.Resist.modifyValue( healing, ch, Element.BODY );

            if( healing > 0 && ch != curUser ){
                curUser.heal( healing );
            }

        } else {

            GLog.i( "什么都没发生" );

        }
    }

    @Override
    protected void fx( int cell, Callback callback ) {

        Char target = Actor.findChar( cell );

        if (target != null && target != curUser ) {

            if( !target.isMagical() ) {

                curUser.sprite.emitter().burst( Speck.factory( Speck.HEALING ), 5 );
                curUser.sprite.parent.add( new DrainLife( curUser.pos, cell, null ) );

                if( target.sprite.visible ){
                    new Flare( 6, 20 ).color( SpellSprite.COLOUR_DARK, true ).show( target.sprite, 0.5f );
                }

            } else {

//                target.sprite.emitter().start( Speck.factory( Speck.CONTROL ), 0.5f, 5 );
                curUser.sprite.parent.add( new DrainWill( curUser.pos, cell, null ) );

                if( target.sprite.visible ){
                    new Flare( 6, 20 ).color( SpellSprite.COLOUR_WILD, true ).show( target.sprite, 0.5f );
                }
            }

        } else {

            curUser.sprite.parent.add( new DrainLife( curUser.pos, cell, null ) );

        }

        Sample.INSTANCE.play(Assets.SND_ZAP);
        callback.call();

    }

	@Override
	public String desc() {
		return
                "这根法杖允许你从生物身上窃取生命力并恢复自身。对非生物敌人施法只会造成伤害，不过它在对睡眠中或未察觉危险的敌人来说尤为有效。";
//			"This wand will allow you to steal life energy from living creatures to restore your " +
//            "own health. Using it against non-living creatures will just harm them, but it is " +
//            "especially effective against targets which are sleeping or otherwise unaware of danger.";
	}
}
