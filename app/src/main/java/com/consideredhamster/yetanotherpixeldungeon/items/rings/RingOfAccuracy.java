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
package com.consideredhamster.yetanotherpixeldungeon.items.rings;

import java.util.Locale;

public class RingOfAccuracy extends Ring {

	{
		name = "精准之戒";
        shortName = "Ac";
	}
	
	@Override
	protected RingBuff buff( ) {
		return new Accuracy();
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
                "据传这枚戒指中寄宿着无数被历史遗忘的猎手与战士的灵魂，它们的加持能够让佩戴者更加熟练地运用各类远程和近战武器。"
//            "It is said that such rings were imbued with spirits of long-forgotten hunters and " +
//            "warriors, which allows them to grant the wearer greater skill with all manners of " +
//            "melee and ranged weapons."
        );

        desc.append( "\n\n" );

        desc.append( super.desc() );

        desc.append( " " );

        desc.append(
            "佩戴这枚戒指将提高你_" + mainEffect + "%" + "的命中能力_，并且_提高"+sideEffect+"%连击所造成的伤害_。"
        );

        return desc.toString();
    }
	
	public class Accuracy extends RingBuff {
        @Override
        public String desc() {
            return bonus >= 0 ?
                "你感觉自己的战斗技巧得到了升华。" :
                "你感觉自己的战斗技巧变得生疏起来。" ;
        }
	}
}
