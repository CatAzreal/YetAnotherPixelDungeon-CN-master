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

import com.consideredhamster.yapdcn.actors.buffs.BuffActive;
import com.consideredhamster.yapdcn.actors.buffs.bonuses.Invisibility;
import com.consideredhamster.yapdcn.actors.hero.Hero;

public class PotionOfInvisibility extends Potion {

    public static final float DURATION	= 15f;

	{
		name = "隐形药剂";
        shortName = "In";
	}
	
	@Override
	protected void apply( Hero hero ) {
        BuffActive.add( hero, Invisibility.class, DURATION );
        setKnown();
    }
	
	@Override
	public String desc() {
	    return "饮用这瓶药剂会使你呈现暂时的隐身状态。敌人无法看到隐身的你，但它们并非束手无策。在敌人发起视野内攻击、施放法杖或是阅读卷轴会导致你立刻暴露。";
	    //        return "[临时字串]使用方式：饮用；效果：隐形。攻击，敌前使用法杖/卷轴破除效果";
//			"Drinking this potion will render you temporarily invisible. While invisible, " +
//			"enemies will be unable to see you, but they can try to find you nevertheless. " +
//            "Attacking an enemy, as well as using a wand or a scroll before enemy's eyes, " +
//            "will dispel the effect.";
	}

    @Override
    public int price() {
        return isTypeKnown() ? 70 * quantity : super.price();
    }

    @Override
    public float brewingChance() {
        return 0.65f;
    }

}
