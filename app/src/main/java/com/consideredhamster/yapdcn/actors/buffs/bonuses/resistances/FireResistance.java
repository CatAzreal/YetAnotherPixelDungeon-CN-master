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

public class FireResistance extends ElementResistance {

	@Override
	public String toString() {
		return "火焰抗性";
	}

    @Override
    public String statusMessage() { return "火焰抗性"; }

    @Override
    public String playerMessage() {
        return "你感受到了体内燃起的温暖。";
    }

    @Override
    public int icon() {
        return BuffIndicator.RESIST_FIRE;
    }

    @Override
    public Class<? extends Element> resistance(){
        return Element.Flame.class;
    }

    @Override
    public String description() {
        return "一股暖意席卷全身，就像是体内有什么在燃烧一般。你的火焰抗性得到增强";
    }
}
