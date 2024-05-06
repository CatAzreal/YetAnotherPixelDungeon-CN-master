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
package com.consideredhamster.yetanotherpixeldungeon.items.potions;

import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.BuffActive;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.bonuses.Levitation;
import com.consideredhamster.yetanotherpixeldungeon.actors.hero.Hero;

public class PotionOfLevitation extends Potion {

    public static final float DURATION	= 25f;

	{
		name = "浮空药剂";
        shortName = "Le";
	}
	
	@Override
	protected void apply( Hero hero ) {
        BuffActive.add( hero, Levitation.class, DURATION );
        setKnown();
    }
	
	@Override
	public String desc() {
	    return "饮用这瓶神奇的药剂后你会漂浮在低空中，可以更快更隐蔽地移动。漂浮状态下你会直接飞越陷阱与裂缝，且视野不再被高草阻挡"+
                "然而，漂浮无法躲过火焰或气体这类充满整个空间的东西。";
//        return "[临时字串]使用方式：饮用；效果：免疫陷阱/地面效果(不免疫气体和火焰)，提高移动速度和潜行能力";
//			"Drinking this curious liquid will cause you to hover in the air, moving faster and stealthier. " +
//			"This state also allows drifting over traps or chasms and seeing over the high grass. Flames and gases " +
//			"fill the air, however, and cannot be bypassed while airborne.";
	}

    @Override
    public int price() {
        return isTypeKnown() ? 80 * quantity : super.price();
    }

    @Override
    public float brewingChance() {
        return 0.55f;
    }
}
