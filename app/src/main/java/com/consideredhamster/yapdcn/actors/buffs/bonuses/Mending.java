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

package com.consideredhamster.yapdcn.actors.buffs.bonuses;

import com.consideredhamster.yapdcn.actors.Char;
import com.consideredhamster.yapdcn.actors.buffs.Buff;
import com.consideredhamster.yapdcn.actors.buffs.debuffs.Crippled;
import com.consideredhamster.yapdcn.actors.buffs.debuffs.Poisoned;
import com.consideredhamster.yapdcn.actors.buffs.debuffs.Withered;
import com.watabou.utils.Random;
import com.consideredhamster.yapdcn.actors.hero.Hero;
import com.consideredhamster.yapdcn.visuals.sprites.CharSprite;
import com.consideredhamster.yapdcn.visuals.ui.BuffIndicator;

public class Mending extends Bonus {

    @Override
    public String toString() {
        return "愈合";
    }

    @Override
    public String statusMessage() { return "愈合"; }

    @Override
    public int icon() {
        return BuffIndicator.MENDING;
    }

    @Override
    public void applyVisual(){
        target.sprite.add( CharSprite.State.MENDING );
    }

    @Override
    public void removeVisual() {
        target.sprite.remove( CharSprite.State.MENDING );
    }

    @Override
    public String description() {
        return "温暖舒适的感觉遍布着整个身体，你感受到自己身上的伤口正在高速愈合";
    }

    @Override
    public boolean attachTo( Char target ) {
        if (super.attachTo( target )) {
            Buff.detach( target, Poisoned.class );
            Buff.detach( target, Crippled.class );
            Buff.detach( target, Withered.class );
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean act() {

        int effect = 10;

        if( target instanceof Hero ) {

            Hero hero = ((Hero) target);

            effect += (hero.lvl - 1) + hero.strBonus;

        }

        int healthRestored = ( effect / 10 + ( effect % 10 > Random.Int(10) ? 1 : 0 ) );

        target.heal( healthRestored > 0 ? healthRestored : 1 );

        return super.act();
    }
}
