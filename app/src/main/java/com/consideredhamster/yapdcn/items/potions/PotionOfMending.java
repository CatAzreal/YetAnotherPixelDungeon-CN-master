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

import com.consideredhamster.yapdcn.actors.buffs.bonuses.Mending;
import com.consideredhamster.yapdcn.actors.buffs.BuffActive;
import com.consideredhamster.yapdcn.actors.hero.Hero;
import com.consideredhamster.yapdcn.visuals.effects.Speck;
import com.consideredhamster.yapdcn.items.rings.RingOfVitality;
import com.watabou.utils.Random;

public class PotionOfMending extends Potion {

    public static final float DURATION	= 20f;

	{
		name = "愈合药剂";
        shortName = "Me";
	}
	
	@Override
	protected void apply( Hero hero ) {

        int totalHP = (int)( hero.HT * hero.ringBuffsHalved(RingOfVitality.Vitality.class ) );

        hero.heal( totalHP / 4 + ( totalHP % 4 > Random.Int(4) ? 1 : 0 ) );

        hero.sprite.emitter().burst(Speck.factory(Speck.HEALING), 5);

        BuffActive.add( hero, Mending.class, DURATION );

        setKnown();
    }

	@Override
	public String desc() {
		return "[临时字串]使用方式：饮用；效果：提高治疗速度，解除负面物理状态";
//			"When imbibed, this elixir will vastly improve imbiber's natural regeneration and cure " +
//            "any physical ailments as well.";
	}
	
	@Override
	public int price() {
		return isTypeKnown() ? 30 * quantity : super.price();
	}

    @Override
    public float brewingChance() {
        return 1.00f;
    }
}
