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

public class RingOfShadows extends Ring {

	{
		name = "暗影之戒";
        shortName = "Sh";
	}
	
	@Override
	protected RingBuff buff( ) {
		return new Shadows();
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
                "这枚戒指上奇特的附魔控制着环绕在佩戴者周围的暗影，帮助他们更有效的隐藏在各类环境之中。对于那些涉及到暗杀和潜行工作的人这属于必备之物。"
//            "The curious enchantment on this ring controls the shadows around its wearer, helping " +
//            "him or her to blend with their surroundings. Such rings are indispensable for those " +
//            "whose job revolves around espionage or assassinations."
        );

        desc.append( "\n\n" );

        desc.append( super.desc() );

        desc.append( " " );

        desc.append(
                "佩戴这枚戒指将提高你_" + mainEffect + "%" + "的潜行能力_，并且_提高"+sideEffect+"%伏击所造成的伤害_。"
        );

        return desc.toString();
	}
	public class Shadows extends RingBuff {

        @Override
        public String desc() {
            return bonus >= 0 ?
                "一瞬之间，暗影如有形般缠绕着你，使你的身形更难被察觉。" :
                "一瞬之间，你周边的阴影纷纷逃开，使你的身形暴露在光线之中。" ;
        }
	}
}
