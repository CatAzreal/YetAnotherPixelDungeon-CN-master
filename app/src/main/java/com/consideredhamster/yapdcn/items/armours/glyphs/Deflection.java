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
package com.consideredhamster.yapdcn.items.armours.glyphs;

import com.consideredhamster.yapdcn.Element;
import com.consideredhamster.yapdcn.actors.Char;
import com.consideredhamster.yapdcn.items.armours.Armour;
import com.consideredhamster.yapdcn.visuals.sprites.ItemSprite.Glowing;

public class Deflection extends Armour.Glyph {

	@Override
	public Glowing glowing() {
		return PURPLE                  ;
	}

    @Override
    public Class<? extends Element> resistance() {
        return Element.Energy.class;
    }

    @Override
    protected String name_p() {
        return "反射之%s";
    }

    @Override
    protected String name_n() {
        return "泄能之%s";
    }

    @Override
    protected String desc_p() {
        return "将非物理伤害反射回攻击者，并提高魔法伤害抗性";
    }

    @Override
    protected String desc_n() {
        return "将造成的非物理伤害返还给自身";
    }

    @Override
    protected boolean proc_p( Char attacker, Char defender, int damage ) {
        return false;
    }

    @Override
    protected boolean proc_n( Char attacker, Char defender, int damage ) {
        return false;
    }
}
