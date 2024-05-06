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
package com.consideredhamster.yapdcn.actors.buffs.debuffs;

import com.consideredhamster.yapdcn.Element;
import com.consideredhamster.yapdcn.levels.Level;
import com.consideredhamster.yapdcn.visuals.effects.particles.SparkParticle;
import com.consideredhamster.yapdcn.visuals.ui.BuffIndicator;
import com.watabou.utils.Random;

public class Shocked extends Debuff {

    private static final String TXT_DISARMED = "突如其来的电击使你不由得将手中的%s松开，使其掉落在地！";

    @Override
    public Element buffType() {
        return Element.SHOCK;
    }

    @Override
    public String toString() {
        return "触电";
    }

    @Override
    public String statusMessage() { return "触电"; }

    @Override
    public String playerMessage() { return "你触电了！"; }

    @Override
    public int icon() {
        return BuffIndicator.SHOCKED;
    }

//    @Override
//    public void applyVisual() {
//        target.sprite.addFromDamage( CharSprite.State.POISONED );
//    }
//
//    @Override
//    public void removeVisual() {
//        target.sprite.remove( CharSprite.State.POISONED );
//    }

    @Override
    public String description() {
        return "电流在你的体内涌动，等待着被释放的时机-再次被击中或踏入水中将释放这些多余电流，" +
                "并使你持有的武器掉落在地。同时，在此期间你的法杖会因为电流变得极度不稳定。";
    }

    @Override
    public boolean act(){

        if( target.isAlive() && !target.flying && Level.water[ target.pos ] ){
            discharge();
        }

        return super.act();
    }

    public void discharge() {

        target.damage(
                Random.IntRange( duration, duration * (int)Math.sqrt( target.totalHealthValue() ) ),
                this, Element.SHOCK_PERIODIC
        );

//        target.sprite.showStatus( CharSprite.NEGATIVE, "ZAP!");

//        if( target instanceof Hero ) {
//
//            Camera.main.shake( 2, 0.3f );
//
//            Hero hero = (Hero)target;
//            EquipableItem weapon = Random.oneOf( hero.belongings.weap1, hero.belongings.weap2 );

//            if( weapon != null && weapon.disarmable() ) {
//
//                GLog.w(TXT_DISARMED, weapon.name());
//                weapon.doDrop(hero);
//
//            }

//        } else {
//
//            target.delay( Random.IntRange( 1, 2 ) );
//
//        }

        if (target.sprite.visible) {
            target.sprite.centerEmitter().burst( SparkParticle.FACTORY, (int)Math.ceil( duration ) + 1 );
        }

        detach();
    };

}
