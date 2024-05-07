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

import com.watabou.utils.Callback;
import com.consideredhamster.yapdcn.Element;
import com.consideredhamster.yapdcn.actors.Char;
import com.consideredhamster.yapdcn.items.misc.Gold;
import com.consideredhamster.yapdcn.items.weapons.throwing.Knives;
import com.consideredhamster.yapdcn.levels.Level;
import com.consideredhamster.yapdcn.misc.mechanics.Ballistica;
import com.consideredhamster.yapdcn.visuals.sprites.MissileSprite;
import com.consideredhamster.yapdcn.visuals.sprites.ThiefSprite;

public class Thief extends MobPrecise {

    public Thief() {

        super( 2 );

        /*

            base maxHP  = 11
            armor class = 3

            damage roll = 2-5 (1-2)

            accuracy    = 8
            dexterity   = 6

            perception  = 105%
            stealth     = 105%

         */

        name = "窃贼";
        info = "飞刀投掷";
        spriteClass = ThiefSprite.class;

        loot = Gold.class;
        lootChance = 0.25f;

        resistances.put( Element.Dispel.class, Element.Resist.IMMUNE );
	}

    @Override
    public int damageRoll() {
        return isRanged() ? super.damageRoll() / 2 : super.damageRoll();
    }

    @Override
    protected boolean canAttack( Char enemy ) {
        return super.canAttack( enemy ) || HP >= HT && Level.distance( pos, enemy.pos ) <= 2 &&
                Ballistica.cast(pos, enemy.pos, false, true) == enemy.pos;
    }

    @Override
    protected void onRangedAttack( int cell ) {
        ((MissileSprite)sprite.parent.recycle( MissileSprite.class )).
                reset(pos, cell, new Knives(), new Callback() {
                    @Override
                    public void call() {
                        onAttackComplete();
                    }
                });

        super.onRangedAttack( cell );
    }

//    @Override
//    public void die( Object cause, Element dmg ) {
//        Ghost.Quest.process( pos );
//        super.die( cause, dmg );
//    }

    @Override
    public String description(){

        return "下水道一直是各种歹徒和不法分子的藏身之地。这些胆小鬼通常装备各种不同的匕首和小刀，它们依靠的是肮脏的伎俩，而不是技巧和力量。";
    }
}
