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
package com.consideredhamster.yapdcn.items.potions;

import com.consideredhamster.yapdcn.Dungeon;
import com.consideredhamster.yapdcn.actors.buffs.BuffActive;
import com.consideredhamster.yapdcn.actors.buffs.bonuses.MindVision;
import com.consideredhamster.yapdcn.actors.hero.Hero;

public class PotionOfMindVision extends Potion {

    public static final float DURATION	= 35f;

	{
		name = "灵视药剂";
        shortName = "Mi";
	}
	
	@Override
	protected void apply( Hero hero ) {
        BuffActive.add(hero, MindVision.class, DURATION );
        Dungeon.observe();

        setKnown();
    }
	
	@Override
	public String desc() {
	    return "饮下这瓶药剂后，你的意识与其他生物的心灵调谐一致，使得你能透过墙壁感知到全层的生物。这瓶药剂也能抵消失明带来的大部分麻烦。";
 //       return "[临时字串]使用方式：饮用；效果：获知全局生物位置移除诅咒，提高感知";
//			"After drinking this, your mind will become attuned to the psychic signature " +
//			"of distant creatures, enabling you to sense everyone on current floor through walls. " +
//			"Also this potion will negate most of the disadvantages of blindness.";
	}

    @Override
    public int price() {
        return isTypeKnown() ? 45 * quantity : super.price();
    }

    @Override
    public float brewingChance() {
        return 0.90f;
    }
}
