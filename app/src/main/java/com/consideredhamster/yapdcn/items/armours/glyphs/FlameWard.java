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

import com.watabou.utils.Random;
import com.consideredhamster.yapdcn.Element;
import com.consideredhamster.yapdcn.actors.Char;
import com.consideredhamster.yapdcn.items.armours.Armour;
import com.consideredhamster.yapdcn.levels.Level;
import com.consideredhamster.yapdcn.visuals.sprites.ItemSprite.Glowing;

public class FlameWard extends Armour.Glyph {

    @Override
    public Glowing glowing() {
        return ORANGE;
    }

    @Override
    public Class<? extends Element> resistance() {
        return Element.Flame.class;
    }

    @Override
    protected String name_p() {
        return "炎盾之%s ";
    }

    @Override
    protected String name_n() {
        return "爆燃之%s";
    }

    @Override
    protected String desc_p() {
        return "燃烧击中你的敌人，并减少所受火焰伤害";
    }

    @Override
    protected String desc_n() {
        return "被击中时燃烧自己";
    }

	@Override
	protected boolean proc_p( Char attacker, Char defender, int damage ) {

        if (Level.adjacent(attacker.pos, defender.pos)) {
            attacker.damage(Random.IntRange(damage / 4, damage / 3), this, Element.FLAME);
            return true;
        }

        return false;
	}

    @Override
    protected boolean proc_n( Char attacker, Char defender, int damage ) {

            defender.damage(Random.IntRange(damage / 4, damage / 3), this, Element.FLAME);
            return true;

    }
}
