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

import com.consideredhamster.yapdcn.Element;

import java.util.HashSet;
import java.util.Locale;

public class RingOfWillpower extends Ring {

	{
		name = "意志之戒";
        shortName = "Wi";
	}

    public static final HashSet<Class<? extends Element>> RESISTS = new HashSet<>();
    static {
        RESISTS.add(Element.Mind.class);
    }
	
	@Override
	protected RingBuff buff( ) {
		return new Willpower();
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
                "[介绍文本暂无]"
//            "By increasing wearer's willpower, this ring indirectly increases their magical " +
//            "abilities. Also, it offers additional benefit of making it easier to " +
//            "shrug off all kinds of mental debuffs."
        );

        desc.append( "\n\n" );

        desc.append( super.desc() );

        desc.append( " " );

        desc.append(
                "佩戴这枚戒指将提高你_" + mainEffect + "%_" + "的魔能_，并且_提高"+sideEffect+"%的负面精神状态抗性_。"
        );

        return desc.toString();
    }

    public class Willpower extends RingBuff {
        @Override
        public String desc() {
            return bonus >= 0 ?
                "你的奥术亲和得到了强化。" :
                "你的奥术知识和此地的魔能连结弱化了。" ;
        }
    }
}
