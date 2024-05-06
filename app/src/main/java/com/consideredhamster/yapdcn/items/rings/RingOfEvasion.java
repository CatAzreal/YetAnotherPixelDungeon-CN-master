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

public class RingOfEvasion extends Ring {

	{
		name = "闪避之戒";
        shortName = "Ev";
	}
	
	@Override
	protected RingBuff buff( ) {
		return new Evasion();
	}
	
	@Override
	public String desc() {

        String mainEffect = "??";
        String sideEffect = "??";

        if( isIdentified() ){
            mainEffect = String.format( Locale.getDefault(), "%.0f", 100 * Ring.effect( bonus ) / 2 );
            sideEffect = String.format( Locale.getDefault(), "%.0f", 100 * Ring.effect( bonus ) );
        }

        StringBuilder desc = new StringBuilder(
                "这种戒指能强化佩戴者的反应和行动速度，使其更难被命中——尤其是正在移动之时。"
//            "Rings of this kind serve to improve reflexes and speed of their wearer, making them " +
//            "harder to be hit - especially when they are on the move."
        );

        if( !dud ) {

            desc.append("\n\n");

            desc.append(super.desc());

            desc.append(" ");

            desc.append(
                    "佩戴这枚戒指在站定不动时提供_" + mainEffect + "%" + "的闪避能力_，而移动中时提高_" + sideEffect + "%的闪避能力_。"
            );
        }
        return desc.toString();
	}
	
	public class Evasion extends RingBuff {
        @Override
        public String desc() {
            return bonus >= 0 ?
                "你感觉自己的反应速度得到增强。" :
                "你感觉自己的反应速度变得迟缓。" ;
        }
	}
}
