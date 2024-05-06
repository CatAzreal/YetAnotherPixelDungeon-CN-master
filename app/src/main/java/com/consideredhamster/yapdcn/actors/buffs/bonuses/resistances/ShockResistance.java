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

public class ShockResistance extends ElementResistance {

	@Override
	public String toString() {
		return "电击抗性";
	}

    @Override
    public String statusMessage() { return "电击抗性"; }

    @Override
    public String playerMessage() {
        return "电流在你的体表游走，毛发因此纷纷直立起来。";
    }

    @Override
    public int icon() {
        return BuffIndicator.RESIST_ELEC;
    }

    @Override
    public Class<? extends Element> resistance(){
        return Element.Shock.class;
    }

    @Override
    public String description() {
        return "周围的空气充满了静电。对电击伤害的抗性得到增强。";
    }
}
