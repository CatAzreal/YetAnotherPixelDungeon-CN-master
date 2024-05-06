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

public class RingOfDurability extends Ring {

	{
		name = "耐久之戒";
        shortName = "Du";
	}
	
	@Override
	protected RingBuff buff( ) {
		return new Durability();
	}
	
	@Override
	public String desc() {

        String mainEffect = "??";
        String sideEffect = "??";

        if( isIdentified() ){
            mainEffect = String.format( Locale.getDefault(), "%.0f", 100 * Ring.effect( bonus ) );
            sideEffect = String.format( Locale.getDefault(), "%.0f", 100 * Ring.effect( bonus ) / 2 );
        }

        StringBuilder desc = new StringBuilder(
                "不论是三军阵前还是匠人桌旁，耐久之戒都是珍奇之物，它可以使手上的器具长久使用且更易维护。"
//            "Rings of Durability are valued by men of crafts and warfare alike, due to their ability " +
//            "to make tools of their trade to serve longer and be repaired with greater ease."
        );

        desc.append( "\n\n" );

        desc.append( super.desc() );

        desc.append( " " );

        desc.append(
                "佩戴这枚戒指将提高你所用_物品" + mainEffect + "%" + "的耐久度_，并且修理物品时_"+sideEffect+"%不消耗修理工具_。"
        );

        return desc.toString();
	}
	
	public class Durability extends RingBuff {
        @Override
        public String desc() {
            return bonus >= 0 ?
                "你感受到自己身上的物品被笼罩在了一层保护光环之下。" :
                "你感受到自己身上的物品被笼罩在了一层滞阻光环之下。" ;
        }
	}
}
