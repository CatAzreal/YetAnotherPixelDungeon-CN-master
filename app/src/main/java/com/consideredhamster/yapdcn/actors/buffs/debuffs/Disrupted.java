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
import com.consideredhamster.yapdcn.visuals.ui.BuffIndicator;

public class Disrupted extends Debuff {

    @Override
    public Element buffType() {
        return Element.DISPEL;
    }

    @Override
    public String toString() {
        return "阻断";
    }

    @Override
    public String statusMessage() { return "阻断"; }

//    @Override
//    public String playerMessage() { return "You catch fire! Quickly, run to the water!"; }

    @Override
    public int icon() {
        return BuffIndicator.DISRUPT;
    }

//    @Override
//    public void applyVisual() {
//        target.sprite.addFromDamage( CharSprite.State.BURNING );
//    }
//
//    @Override
//    public void removeVisual() {
//        target.sprite.remove( CharSprite.State.BURNING );
//    }

    @Override
    public String description() {
        return "你不应该看到这条描述，但如果你看到了...这个debuff等同于混乱，但仅对秘法生物生效";
    }
}
