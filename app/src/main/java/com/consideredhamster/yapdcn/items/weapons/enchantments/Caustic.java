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
package com.consideredhamster.yapdcn.items.weapons.enchantments;

import com.consideredhamster.yapdcn.items.wands.WandOfAcidSpray;
import com.watabou.utils.Random;
import com.consideredhamster.yapdcn.Element;
import com.consideredhamster.yapdcn.actors.Char;
import com.consideredhamster.yapdcn.items.wands.Wand;
import com.consideredhamster.yapdcn.items.weapons.Weapon;
import com.consideredhamster.yapdcn.visuals.sprites.ItemSprite.Glowing;

public class Caustic extends Weapon.Enchantment {

	@Override
	public Glowing glowing() {
		return GREEN;
	}

    @Override
    public Class<? extends Wand> wandBonus() {
        return WandOfAcidSpray.class;
    }

    @Override
    protected String name_p() {
        return "浸酸之%s";
    }

    @Override
    protected String name_n() {
        return "蚀化之%s";
    }

    @Override
    protected String desc_p() {
        return "使命中的敌人被腐蚀淤泥覆盖";
    }

    @Override
    protected String desc_n() {
        return "命中敌人时使自己被腐蚀淤泥覆盖";
    }

    @Override
    protected boolean proc_p( Char attacker, Char defender, int damage ) {

        defender.damage(Random.IntRange(damage / 3, damage / 2), this, Element.ACID);
        return true;

    }

    @Override
    protected boolean proc_n( Char attacker, Char defender, int damage ) {

        attacker.damage( Random.IntRange(damage / 3, damage / 2), this, Element.ACID );
        return true;

    }
}
