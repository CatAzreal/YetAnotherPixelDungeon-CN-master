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
import com.consideredhamster.yapdcn.actors.buffs.bonuses.Shielding;
import com.consideredhamster.yapdcn.actors.hero.Hero;


public class PotionOfShield extends Potion {

    public static final float DURATION	= 30f;

	{
		name = "护盾药剂";
        shortName = "Sh";
	}

    @Override
    protected void apply( Hero hero ) {
        BuffActive.add(hero, Shielding.class, DURATION );
        setKnown();
    }

    @Override
	public String desc() {
	    return "这瓶药剂浓缩了防护魔法，饮用这瓶药剂会在身旁凝结一层力场护盾，增强你对物理伤害与大部分来源的属性伤害的抵抗力。";
//        return "[临时字串]使用方式：饮用；效果：提高物理及魔法伤害抗性";
//			"This potion is imbued with a powerful protective magic. Consuming it will temporarily " +
//            "create a kind of force field around you, giving you both improved physical protection " +
//            "and resistance to most sources of magical damage.";
	}

	
	@Override
	public int price() {
		return isTypeKnown() ? 90 * quantity : super.price();
	}

    @Override
    public float brewingChance() {
        return 0.45f;
    }
}
