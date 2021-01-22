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

import com.consideredhamster.yapdcn.Dungeon;
import com.consideredhamster.yapdcn.Element;
import com.consideredhamster.yapdcn.actors.Actor;
import com.consideredhamster.yapdcn.actors.Char;
import com.consideredhamster.yapdcn.actors.special.Pushing;
import com.consideredhamster.yapdcn.levels.Level;
import com.consideredhamster.yapdcn.misc.mechanics.Ballistica;
import com.consideredhamster.yapdcn.visuals.sprites.ItemSpriteSheet;
import com.watabou.utils.Callback;

public class Harpoons extends ThrowingWeaponHeavy {

	{
		name = "鱼叉";
		image = ItemSpriteSheet.HARPOON;
	}

	public Harpoons() {
		this( 1 );
	}

	public Harpoons(int number) {
        super( 3 );
		quantity = number;
	}

    @Override
    public boolean returnsWhenThrown() {
        return true;
    }

    @Override
    public int image() {
        return ItemSpriteSheet.HARPOON;
    }

    @Override
    public int imageAlt() {
        return ItemSpriteSheet.HARPOON_THROWN;
    }

    @Override
    public void proc(Char attacker, final Char defender, int damage ) {

        super.proc(attacker, defender, damage);

        if( !Level.adjacent( attacker.pos, defender.pos ) ) {

            int distance = damage * 6 / defender.totalHealthValue() + 1;

            distance = Element.Resist.modifyValue( distance, defender, Element.KNOCKBACK );

            if( distance > 0 ) {

                final int newPos = Ballistica.trace[Math.max( 1, Ballistica.distance - distance )];

                Pushing.move( defender, newPos, new Callback() {
                    @Override
                    public void call(){
                        Actor.moveToCell( defender, newPos );
                        Dungeon.level.press( newPos, defender );
                        defender.delay( 1f );
                    }
                } );

            } else {

                super.onThrow( defender.pos );

            }
        }
    }
	
	@Override
	public String desc() {
		return 
			"鱼叉的重量主要来源于其尾部衔接的链条。这种罕见的投掷武器可以将目标拉扯到自己身旁。若目标无法被拉扯，则命中的鱼叉会掉落在地。";
	}
}
