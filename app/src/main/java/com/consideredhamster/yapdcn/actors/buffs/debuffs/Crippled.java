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
import com.consideredhamster.yapdcn.visuals.sprites.CharSprite;
import com.consideredhamster.yapdcn.visuals.ui.BuffIndicator;
import com.watabou.utils.Random;

public class Crippled extends Debuff {

    @Override
    public Element buffType() {
        return Element.BODY;
    }

    @Override
    public String toString() {
        return "残废";
    }

    @Override
    public String statusMessage() { return "残废"; }

    @Override
    public String playerMessage() { return "你的腿残废了！最好不要乱动。"; }

    @Override
    public int icon() {
        return BuffIndicator.CRIPPLED;
    }

    @Override
    public void applyVisual() {
        target.sprite.add( CharSprite.State.BLEEDING );
    }

    @Override
    public void removeVisual() {
        target.sprite.remove( CharSprite.State.BLEEDING );
    }

    @Override
    public String description() {
        return "你的双腿受到了严重伤害，使你更难闪避攻击并大幅降低移动速度。继续尝试移动只会延长恢复所需的时间。";
    }

    @Override
    public boolean act() {

        target.damage( Random.Int( (int) Math.sqrt( target.totalHealthValue() * 0.5f ) ) + 1, this, Element.BODY );

        if( target.moving && Random.Int( 2 ) == 0 ) duration++;

        return super.act();
    }

}
