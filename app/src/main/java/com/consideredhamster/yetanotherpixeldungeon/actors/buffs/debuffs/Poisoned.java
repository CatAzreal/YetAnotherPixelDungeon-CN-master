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

import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.CharSprite;
import com.watabou.utils.Random;
import com.consideredhamster.yetanotherpixeldungeon.Element;
import com.consideredhamster.yetanotherpixeldungeon.visuals.ui.BuffIndicator;

public class Poisoned extends Debuff {

    public final static String TXT_CANNOT_EAT = "你全身难受恶心，现在吃不下任何东西。";

    @Override
    public Element buffType() {
        return Element.BODY;
    }

    @Override
    public String toString() {
        return "中毒";
    }

    @Override
    public String statusMessage() { return "中毒"; }

    @Override
    public String playerMessage() { return "你中毒了！"; }

    @Override
    public int icon() {
        return BuffIndicator.POISONED;
    }

    @Override
    public void applyVisual() {
        target.sprite.add( CharSprite.State.POISONED );
    }

    @Override
    public void removeVisual() {
        target.sprite.remove( CharSprite.State.POISONED );
    }

    @Override
    public String description() {
        return "你感觉非常不适...毒素似乎正在你的体内游走。该状态会对你造成持续伤害并使你更加孱弱，" +
                "降低攻击速度和伤害。";
    }

    @Override
    public boolean act() {

        target.damage( Random.Int( (int) Math.sqrt( target.totalHealthValue() ) ) + 1, this, Element.BODY );

        return super.act();

    }

}
