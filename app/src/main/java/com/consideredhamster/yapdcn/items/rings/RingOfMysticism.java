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
package com.consideredhamster.yapdcn.items.rings;

import java.util.Locale;

public class RingOfMysticism extends Ring {
	
	{
		name = "秘法之戒";
        shortName = "My";
	}
	
	@Override
	protected RingBuff buff( ) {
		return new Mysticism();
	}
	
	@Override
	public String desc() {

        String mainEffect = "??";
        String sideEffect = "??";

        if( isIdentified() ){
            mainEffect = String.format( Locale.getDefault(), "%.0f", 100 * Ring.effect( bonus ) / 3 );
            sideEffect = String.format( Locale.getDefault(), "%.0f", 100 * Ring.effect( bonus ) );
        }

        StringBuilder desc = new StringBuilder(
                "这枚戒指上带有强化佩戴者魔能感知的附魔。因其提供的调谐能力，这类戒指通常被各类法术业者使用。"
//            "This ring was enchanted to increase magical sensitivity of it's wearer. Rings of " +
//            "this kind are often used by spellcasters of all kinds, since having greater " +
//            "attunement with magical currents really helps in their line of work."
        );

        if( !dud ) {

            desc.append("\n\n");

            desc.append(super.desc());

            desc.append(" ");

            desc.append(
                    "佩戴这枚戒指将提高你_" + mainEffect + "%" + "的调谐能力_，并且_护甲和武器的附魔触发几率提高" + sideEffect + "%_。"
            );
        }

        return desc.toString();
	}
	
	public class Mysticism extends RingBuff {
        @Override
        public String desc() {
            return bonus >= 0 ?
                "一瞬之间，你对魔能流向的感知得到了强化。" :
                "一瞬之间，你对魔能流向的感知变得更加迟钝。" ;
        }
	}
}
