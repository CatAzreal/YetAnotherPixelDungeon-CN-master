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
import com.consideredhamster.yapdcn.actors.buffs.debuffs.Crippled;
import com.consideredhamster.yapdcn.actors.buffs.BuffActive;
import com.watabou.utils.Random;
import com.consideredhamster.yapdcn.Dungeon;
import com.consideredhamster.yapdcn.actors.Char;
import com.consideredhamster.yapdcn.items.food.MeatRaw;
import com.consideredhamster.yapdcn.levels.Level;
import com.consideredhamster.yapdcn.visuals.sprites.PiranhaSprite;

public class Piranha extends MobEvasive {
	
	public Piranha() {

		super( Dungeon.depth + 1 );

        name = "巨型食人鱼";
        spriteClass = PiranhaSprite.class;

        baseSpeed = 2f;

        minDamage += tier * 2;
        maxDamage += tier * 2;

        HP = HT += Random.IntRange(2, 4);

        loot = new MeatRaw();
        lootChance = 0.5f;

        resistances.put( Element.Dispel.class, Element.Resist.IMMUNE );
        resistances.put( Element.Knockback.class, Element.Resist.VULNERABLE );

	}
	
	@Override
	protected boolean act() {
		if (!Level.water[pos]) {
			die( null );
			return true;
		} else {
			return super.act();
		}
	}
	
	@Override
	public boolean reset() {
        state = SLEEPING;
        return true;
	}

	@Override
	protected boolean getCloser( int target ) {
		
		if (rooted) {
			return false;
		}
		
		int step = Dungeon.findPath(this, pos, target,
                Level.water,
                Level.fieldOfView);
		if (step != -1) {
			move( step );
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	protected boolean getFurther( int target ) {
		int step = Dungeon.flee(this, pos, target,
                Level.water,
                Level.fieldOfView);
		if (step != -1) {
			move( step );
			return true;
		} else {
			return false;
		}
	}

    @Override
    protected int nextStepTo( Char enemy ) {
        return Dungeon.findPath( this, pos, enemy.pos,
                Level.water,
                Level.fieldOfView );
    }

    @Override
    public int attackProc( Char enemy, int damage, boolean blocked ) {

        if( !blocked && Random.Int( 10 ) < tier ) {
            BuffActive.addFromDamage( enemy, Crippled.class, damage * 2 );
        }

        return damage;
    }

	@Override
	public String description() {
		return
			"这些肉食性鱼类不是地下水池中的天然生物。它们被专门培育用来保护被水淹没的储藏室。不管出身如何，它们都同样凶残和嗜血。";
	}

}
