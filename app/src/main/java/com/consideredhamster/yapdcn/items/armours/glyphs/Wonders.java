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

public class Wonders extends Armour.Glyph {

    @Override
    protected String name_p() {
        return "奇迹之%s";
    }

    @Override
    protected String name_n() {
        return "混沌之%s";
    }

    @Override
    protected String desc_p() {
        return "被命中时对攻击者造成随机减益效果";
    }

    @Override
    protected String desc_n() {
        return "被命中时受到随机减益效果的影响";
    }

    @Override
    public boolean proc( Armour armor, Char attacker, Char defender, int damage ) {
        return random().proc( armor, attacker, defender, damage );
    }

    @Override
    protected boolean proc_p( Char attacker, Char defender, int damage ) {

        return true;

    }

    @Override
    protected boolean proc_n( Char attacker, Char defender, int damage ) {

        return true;

    }

}
