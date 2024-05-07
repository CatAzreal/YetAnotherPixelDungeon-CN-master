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
import com.consideredhamster.yapdcn.items.weapons.throwing.Shurikens;
import com.consideredhamster.yapdcn.misc.mechanics.Ballistica;
import com.consideredhamster.yapdcn.visuals.sprites.MissileSprite;
import com.consideredhamster.yapdcn.visuals.sprites.ShadowSprite;
import com.watabou.utils.Callback;

public class TenguShadow extends MobRanged {

    public TenguShadow() {

        super( 3, 6, false );

        name = "影分身";
        info = "手里剑投掷, 无法移动";

        HT = HP /= 2;
        armorClass = 0;

        spriteClass = ShadowSprite.class;

        resistances.put(Element.Mind.class, Element.Resist.IMMUNE);
        resistances.put(Element.Body.class, Element.Resist.IMMUNE);

    }

    @Override
    public float attackSpeed() {
        return isRanged() ? super.attackSpeed() : super.attackSpeed() * 2;
    }

    @Override
    protected void onRangedAttack( int cell ) {
        ((MissileSprite)sprite.parent.recycle( MissileSprite.class )).
                reset(pos, cell, new Shurikens(), new Callback() {
                    @Override
                    public void call() {
                        onAttackComplete();
                    }
                });

        super.onRangedAttack(cell);
    }

    @Override
    protected boolean getCloser( int target ) {
        return false;
    }

    @Override
    protected boolean getFurther( int target ) { return false; }

	@Override
	protected boolean canAttack( Char enemy ) {
		return Ballistica.cast( pos, enemy.pos, false, true ) == enemy.pos;
	}

	@Override
	public String description() {
		return
			"尽管是由影子构成，这些影分身看起来拥有实体并能被寻常手段击散。不过，看起来这玩意除了站定在那扔手里剑外也没什么别的能耐了。";
	}
}
