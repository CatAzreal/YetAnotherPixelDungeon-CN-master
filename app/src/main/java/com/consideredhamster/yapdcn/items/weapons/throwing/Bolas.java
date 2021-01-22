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
package com.consideredhamster.yapdcn.items.weapons.throwing;

import com.consideredhamster.yapdcn.actors.buffs.BuffActive;
import com.consideredhamster.yapdcn.actors.Char;
import com.consideredhamster.yapdcn.actors.buffs.debuffs.Ensnared;
import com.consideredhamster.yapdcn.actors.hero.Hero;
import com.consideredhamster.yapdcn.visuals.sprites.ItemSpriteSheet;

public class Bolas extends ThrowingWeaponSpecial {

	{
		name = "套绳球";
		image = ItemSpriteSheet.HUNTING_BOLAS;
	}

	public Bolas() {
		this( 1 );
	}

	public Bolas(int number) {
        super( 2 );
		quantity = number;
	}

    @Override
    public void proc( Char attacker, Char defender, int damage ) {
        super.proc(attacker, defender, damage);

        BuffActive.addFromDamage(defender, Ensnared.class, damageRoll( (Hero) attacker ) * 2 );
    }
	
	@Override
	public String desc() {
		return 
			"套绳球通常用于狩猎，其无法造成大量伤害，但能够捆绑住目标。这类物品通常都以蜘蛛丝制作，以确保其绳索的坚韧程度。";
	}
}
