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
import com.consideredhamster.yapdcn.visuals.sprites.ItemSpriteSheet;
import com.consideredhamster.yapdcn.actors.Actor;
import com.consideredhamster.yapdcn.actors.Char;
import com.consideredhamster.yapdcn.visuals.effects.Splash;
import com.consideredhamster.yapdcn.visuals.effects.particles.SparkParticle;
import com.watabou.utils.Random;

public class WandOfMagicMissile extends WandCombat {

	{
		name = "魔弹法杖";
		image = ItemSpriteSheet.WAND_MAGICMISSILE;
	}

    @Override
    public float effectiveness( int bonus ) {
        return super.effectiveness( bonus ) * 1.50f;
    }
	
	@Override
	protected void onZap( int cell ) {

        int power = damageRoll();

        Splash.at( cell, 0x33FFFFFF, (int) Math.sqrt(power) + 2 );

        Char ch = Actor.findChar( cell );

        if (ch != null) {

            if( Char.hit( curUser, ch, true, true ) ) {

                ch.damage( Char.absorb( damageRoll(), ch.armorClass() ), curUser, Element.ENERGY );
                ch.sprite.centerEmitter().burst( SparkParticle.FACTORY, Random.IntRange( 2 + power / 10 , 4 + power / 5 ) );

            } else {

                ch.missed();

            }
		}
	}

	@Override
	public String desc() {
		return
                "[临时字串]伤害可被护甲阻挡，攻击也有概率失误，但基础伤害高于其他法杖";
//			"This wand's effect is quite simple, as it just launches bolts of pure magical energy. " +
//            "These bolts may be affected by the target's armor or miss entirely, but they " +
//            "partially compensate for this by being quite powerful.";
	}

}
