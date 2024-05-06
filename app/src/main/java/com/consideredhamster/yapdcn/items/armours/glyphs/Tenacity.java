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

public class Tenacity extends Armour.Glyph {

	@Override
	public Glowing glowing() {
		return MUSTARD;
	}

    @Override
    public Class<? extends Element> resistance() {
        return Element.Mind.class;
    }

    @Override
    protected String name_p() {
        return "强韧之%s";
    }

    @Override
    protected String name_n() {
        return "脆弱之%s";
    }

    @Override
    protected String desc_p() {
        return "低生命值时获得额外防护能力，并提高精神属性抗性";
    }

    @Override
    protected String desc_n() {
        return "低生命值时受到更多伤害";
    }

    @Override
    public boolean proc_p( Char attacker, Char defender, int damage ) {
        return false;
    }

    @Override
    public boolean proc_n( Char attacker, Char defender, int damage ) {
        return false;
    }
}
