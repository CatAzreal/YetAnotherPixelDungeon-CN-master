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
package com.consideredhamster.yetanotherpixeldungeon.actors.buffs.debuffs;

import com.consideredhamster.yetanotherpixeldungeon.Element;
import com.consideredhamster.yetanotherpixeldungeon.visuals.ui.BuffIndicator;
import com.watabou.utils.Random;

public class Tormented extends Debuff {
	
	public final static String TXT_CANNOT_ATTACK = "你处于极端痛苦和恐慌之中！你现在没办法进攻！";

    @Override
    public Element buffType() {
        return Element.MIND;
    }

    @Override
    public String toString() {
        return "笞挞";
    }

    @Override
    public String statusMessage() { return "笞挞"; }

    @Override
    public String playerMessage() { return "你的精神正被恐惧和痛苦所灌洗！"; }

    @Override
    public int icon() {
        return BuffIndicator.TERROR;
    }

//    @Override
//    public void applyVisual() {
//        target.sprite.addFromDamage( CharSprite.State.BLEEDING );
//    }
//
//    @Override
//    public void removeVisual() {
//        target.sprite.remove( CharSprite.State.BLEEDING );
//    }

    @Override
    public String description() {
        return "令人恐惧的魔法被径直灌入你的精神之中，制造着痛苦与恐慌。" +
                "你的远程攻击几乎无法瞄准，克服恐惧去攻击敌人更不可能。快跑！";
    }

    @Override
    public boolean act() {

        target.damage( Random.Int( (int) Math.sqrt( target.currentHealthValue() * 1.5f ) ) + 1, this, Element.MIND );

        return super.act();

    }

//    private static final String OBJECT	= "object";
//
//    @Override
//    public void storeInBundle( Bundle bundle ) {
//        super.storeInBundle( bundle );
//        bundle.put( OBJECT, object );
//
//    }
//
//    @Override
//    public void restoreFromBundle( Bundle bundle ) {
//        super.restoreFromBundle(bundle);
//        object = bundle.getInt( OBJECT );
//    }
}
