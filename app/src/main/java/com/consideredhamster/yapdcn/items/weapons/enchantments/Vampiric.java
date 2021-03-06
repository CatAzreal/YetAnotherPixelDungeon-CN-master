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

import com.consideredhamster.yapdcn.Element;
import com.consideredhamster.yapdcn.actors.Char;
import com.consideredhamster.yapdcn.items.wands.Wand;
import com.consideredhamster.yapdcn.items.wands.WandOfAcidSpray;
import com.consideredhamster.yapdcn.items.weapons.Weapon;
import com.consideredhamster.yapdcn.visuals.effects.Speck;
import com.consideredhamster.yapdcn.visuals.sprites.ItemSprite.Glowing;
import com.watabou.utils.Random;

public class Vampiric extends Weapon.Enchantment {
	
	@Override
	public Glowing glowing() {
		return RED;
	}

    @Override
    public Class<? extends Wand> wandBonus() {
        return WandOfAcidSpray.class;
    }

    @Override
    protected String name_p() {
        return "嗜血之%s";
    }

    @Override
    protected String name_n() {
        return "恶毒之%s";
    }

    @Override
    protected String desc_p() {
        return "从非秘法敌人身上吸取生命";
    }

    @Override
    protected String desc_n() {
        return "命中敌人时伤害自身";
    }

    @Override
    protected boolean proc_p( Char attacker, Char defender, int damage ) {

        if ( attacker.isAlive() ) {

            int effValue = Element.Resist.modifyValue( damage / 2, defender, Element.BODY );

            if( effValue > defender.HP ) {
                effValue = defender.HP;
            }

            if ( effValue > 0 ) {

                attacker.heal( effValue );

                if( attacker.sprite.visible ) {
                    attacker.sprite.emitter().burst( Speck.factory(Speck.HEALING), 1);
                }
                return true;

            } else {
                return false;
            }

        } else {
            return false;
        }
    }

    @Override
    protected boolean proc_n( Char attacker, Char defender, int damage ) {
        attacker.damage(Random.IntRange(damage / 3, damage / 2), this, Element.BODY);
        attacker.sprite.burst(0x660022, (int) Math.sqrt(damage / 2) + 1);
        return true;
    }
}
