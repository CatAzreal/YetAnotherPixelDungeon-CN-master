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

public class RingOfKnowledge extends Ring {

	{
		name = "知识之戒";
        shortName = "Kn";
	}
	
	@Override
	protected RingBuff buff( ) {
		return new Knowledge();
	}
	
	@Override
	public String desc() {

        String mainEffect = "??";

        if( isIdentified() ){
            mainEffect = String.format( Locale.getDefault(), "%.0f", 100 * Ring.effect( bonus ) );
        }

        StringBuilder desc = new StringBuilder(
                "这种戒指尤其受学者与其生徒们的欢迎，因为它能够提高佩戴者的认知能力，在更短的时间内学习记忆更多知识。"
//            "Both scholars and their pupils appreciate such rings, because they increase cognitive " +
//            "capacity of their wearer, allowing to learn more things in a shorter amount of time."
        );

        if( !dud ) {

            desc.append("\n\n");

            desc.append(super.desc());

            desc.append(" ");

            desc.append(
                    "佩戴这枚戒指能够_提高" + mainEffect + "%的经验获取和道具鉴定速率。"
            );
        }
        return desc.toString();
	}
	
	public class Knowledge extends RingBuff {
        @Override
        public String desc() {
            return bonus >= 0 ?
                "你感觉自己的思绪变得更加敏锐，并且能够记下更多事情。" :
                "你感觉自己变得更加愚钝，就像什么东西在阻碍你的思考一样。" ;
        }
	}
}
