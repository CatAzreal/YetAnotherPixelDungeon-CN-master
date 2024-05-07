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
package com.consideredhamster.yapdcn.actors.mobs;

import com.consideredhamster.yapdcn.Element;
import com.consideredhamster.yapdcn.actors.Char;
import com.consideredhamster.yapdcn.actors.buffs.Buff;
import com.consideredhamster.yapdcn.actors.buffs.special.Combo;
import com.consideredhamster.yapdcn.actors.mobs.npcs.AmbitiousImp;
import com.consideredhamster.yapdcn.items.food.RationSmall;
import com.consideredhamster.yapdcn.visuals.sprites.MonkSprite;

public class DwarfMonk extends MobEvasive {

    public DwarfMonk() {

        super( 13 );

        /*

            base maxHP  = 22
            armor class = 4

            damage roll = 3-13

            accuracy    = 22
            dexterity   = 28

            perception  = 100%
            stealth     = 140%

         */

        name = "矮人武僧";
        info = "速攻, 连击";

        spriteClass = MonkSprite.class;

		loot = new RationSmall();
		lootChance = 0.1f;

//        resistances.put(Element.Mind.class, Element.Resist.PARTIAL);
        resistances.put( Element.Dispel.class, Element.Resist.IMMUNE );
	}

    @Override
    public float attackSpeed() {
        return super.attackSpeed() * 2.0f;
    }
	
	@Override
	public void die( Object cause, Element dmg ) {

		AmbitiousImp.Quest.process( this );
		
		super.die( cause, dmg );

	}
	
	@Override
	public int attackProc( Char enemy, int damage, boolean blocked ) {

        Buff.affect(this, Combo.class).hit();
		
		return damage;
	}

    @Override
    public int damageRoll() {

        int dmg = super.damageRoll();

        Combo buff = buff( Combo.class );

        if( buff != null ) {

            dmg += (int) (dmg * buff.modifier());

        }

        return dmg;
    }
	
	@Override
	public String description() {
		return
			"这些僧侣皆是狂热的信徒，倾其所能保护矮人都城的秘密不受入侵者的袭扰。他们既不披甲亦不执械，仅仅依靠自己的徒手格斗技巧退敌。";
	}
}
