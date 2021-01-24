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

public class RingOfSatiety extends Ring {

	{
		name = "饱食之戒";
        shortName = "Sa";
	}
	
	@Override
	protected RingBuff buff( ) {
		return new Satiety();
	}
	
	@Override
	public String desc() {

        String mainEffect = "??";
        String sideEffect = "??";

        if( isIdentified() ){
            mainEffect = String.format( Locale.getDefault(), "%.0f", 100 * Ring.effect( bonus ) / 2 );
            sideEffect = String.format( Locale.getDefault(), "%.0f", 100 * Ring.effect( bonus ) / 3 );
        }

        StringBuilder desc = new StringBuilder(
                "饱食之戒能够优化佩戴者体内的代谢机能，使他们能够在不进食的情况下活动更久，并提高食物本身能被利用的营养价值，既可以用于免受饥饿折磨，亦可用于节约用餐。[介绍文本暂无]"
//            "Rings of satiety optimize digestive mechanisms of wearer's body, making it possible " +
//            "to go without food longer and increasing nutriety of consumed meals, both helping in " +
//            "the times of hunger and helping to prolong times of excess."
        );

        desc.append( "\n\n" );

        desc.append( super.desc() );

        desc.append( " " );

        desc.append(
                "佩戴这枚戒指将降低_" + mainEffect + "%" + "的饱食度流失速率_，并且_提高"+sideEffect+"%食物效果_。"
        );

        return desc.toString();
	}
	
	public class Satiety extends RingBuff {
        @Override
        public String desc() {
            return bonus >= 0 ?
                "你感受到一股舒适的暖流在自己的肠胃中游走。" ://胃暖暖的，舒服！（差点用这句了）
                "你感觉自己饿的更快了。" ;
        }
	}
}
