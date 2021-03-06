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

import com.consideredhamster.yapdcn.actors.Char;
import com.consideredhamster.yapdcn.actors.buffs.BuffActive;
import com.consideredhamster.yapdcn.actors.buffs.debuffs.Frozen;
import com.consideredhamster.yapdcn.items.wands.Wand;
import com.consideredhamster.yapdcn.items.wands.WandOfIceBarrier;
import com.consideredhamster.yapdcn.items.weapons.Weapon;
import com.consideredhamster.yapdcn.visuals.sprites.ItemSprite.Glowing;

public class Freezing extends Weapon.Enchantment {

    @Override
    public Glowing glowing() {
        return BLUE;
    }

    @Override
    public Class<? extends Wand> wandBonus() {
        return WandOfIceBarrier.class;
    }

    @Override
    protected String name_p() {
        return "冰霜之%s";
    }

    @Override
    protected String name_n() {
        return "冻凝之%s";
    }

    @Override
    protected String desc_p() {
        return "命中时冻伤敌人";
    }

    @Override
    protected String desc_n() {
        return "命中时冻伤自己";
    }

    @Override
    protected boolean proc_p( Char attacker, Char defender, int damage ) {
        BuffActive.addFromDamage( defender, Frozen.class, damage );
        return true;
    }

    @Override
    protected boolean proc_n( Char attacker, Char defender, int damage ) {
        BuffActive.addFromDamage( attacker, Frozen.class, damage );
        return true;
    }
}
