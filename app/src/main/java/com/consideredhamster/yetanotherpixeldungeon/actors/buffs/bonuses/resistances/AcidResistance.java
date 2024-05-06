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
package com.consideredhamster.yetanotherpixeldungeon.actors.buffs.bonuses.resistances;

import com.consideredhamster.yetanotherpixeldungeon.Element;
import com.consideredhamster.yetanotherpixeldungeon.visuals.ui.BuffIndicator;

public class AcidResistance extends ElementResistance {

	@Override
	public String toString() {
		return "酸蚀抗性";
	}

    @Override
    public String statusMessage() { return "酸蚀抗性"; }

    @Override
    public String playerMessage() {
        return "你感觉自己的皮肤变得异常光滑油腻。";
    }

    @Override
    public int icon() {
        return BuffIndicator.RESIST_ACID;
    }

    @Override
    public Class<? extends Element> resistance(){
        return Element.Acid.class;
    }

    @Override
    public String description() {
        return "你的表皮附着了一层类油脂物，这种物质应该能帮助你抵御各类酸蚀效果。你的酸蚀抗性提高了。";
    }
}
