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

public class RingOfAwareness extends Ring {

	{
		name = "感知之戒";
        shortName = "Aw";
	}
	
	@Override
	protected RingBuff buff( ) {
		return new Awareness();
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
            "这枚戒指可以增强佩戴者的感知力，使其对周遭威胁更加敏锐，并可在反击时造成更致命的伤害。"
        );

        if( !dud ) {

            desc.append("\n\n");

            desc.append(super.desc());

            desc.append(" ");

            desc.append(
                    "佩戴这枚戒指将提高你_\" + mainEffect + \"%\" + \"的感知能力_，并且_提高\"+sideEffect+\"%反击所造成的伤害_。"
            );
        }

        return desc.toString();

	}
	
	public class Awareness extends RingBuff {
        @Override
        public String desc() {
            return bonus >= 0 ?
                "你感觉自己变得更加警觉。" :
                "你感觉自己的意识不再敏锐。" ;
        }
	}
}
