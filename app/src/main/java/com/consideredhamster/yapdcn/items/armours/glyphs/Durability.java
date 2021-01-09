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

import com.consideredhamster.yapdcn.actors.Char;
import com.consideredhamster.yapdcn.items.armours.Armour;
import com.consideredhamster.yapdcn.visuals.sprites.ItemSprite.Glowing;

public class Durability extends Armour.Glyph {

    @Override
    public Glowing glowing() {
        return GRAY;
    }

    @Override
    protected String name_p() {
        return "坚固之%s";
    }

    @Override
    protected String name_n() {
        return "易碎之%s";
    }

    @Override
    protected String desc_p() {
        return "降低受损速度并提供额外的防护能力";
    }

    @Override
    protected String desc_n() {
        return "加速防具受损并减少护甲的防护能力";
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
