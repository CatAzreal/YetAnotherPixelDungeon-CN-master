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
package com.consideredhamster.yapdcn.actors.buffs.bonuses.resistances;

import com.consideredhamster.yapdcn.Element;
import com.consideredhamster.yapdcn.visuals.ui.BuffIndicator;

public class PhysicalResistance extends ElementResistance {
	
	@Override
	public String toString() {
		return "物理抗性";
	}

    @Override
    public String statusMessage() { return "物理抗性"; }

    @Override
    public String playerMessage() {
        return "你感觉自己的皮肤变得更加坚硬。";
    }

    @Override
    public int icon() {
        return BuffIndicator.RESIST_PHYS;
    }

    @Override
    public Class<? extends Element> resistance(){
        return Element.Physical.class;
    }

    @Override
    public float resistanceValue() {
        return 0.25f;
    }

    @Override
    public String description() {
        return "不知为何你全身上下的表皮都变得坚硬无比，而且表皮的变化完全没有影响到自己的行动能力。对物理伤害的抗性得到增强。";
    }
}
