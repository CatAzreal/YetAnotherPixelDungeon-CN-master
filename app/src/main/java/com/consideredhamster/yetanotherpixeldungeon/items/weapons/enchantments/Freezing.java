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
package com.consideredhamster.yetanotherpixeldungeon.items.weapons.enchantments;

import com.consideredhamster.yetanotherpixeldungeon.actors.Char;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.BuffActive;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.debuffs.Frozen;
import com.consideredhamster.yetanotherpixeldungeon.items.wands.Wand;
import com.consideredhamster.yetanotherpixeldungeon.items.wands.WandOfIceBarrier;
import com.consideredhamster.yetanotherpixeldungeon.items.weapons.Weapon;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.ItemSprite.Glowing;

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
        return "Freezing %s";
    }

    @Override
    protected String name_n() {
        return "Chilling %s";
    }

    @Override
    protected String desc_p() {
        return "freeze your enemies on hit";
    }

    @Override
    protected String desc_n() {
        return "freeze you on hit";
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
