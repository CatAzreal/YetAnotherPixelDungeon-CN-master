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
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.CharSprite;
import com.consideredhamster.yetanotherpixeldungeon.visuals.ui.BuffIndicator;

public class Withered extends Debuff {

    @Override
    public Element buffType() {
        return Element.BODY;
    }

    @Override
    public String toString() {
        return "虚弱";
    }

    @Override
    public String statusMessage() { return "虚弱"; }

    @Override
    public String playerMessage() { return "你感到力量正在被抽走！"; }

    @Override
    public int icon() {
        return BuffIndicator.WITHER;
    }

    @Override
    public void applyVisual() {
        target.sprite.add( CharSprite.State.WITHERED );
    }

    @Override
    public void removeVisual() {
        target.sprite.remove( CharSprite.State.WITHERED );
    }

    @Override
    public String description() {
        return "不洁之力渗入了你的身体之中，弱化着你手中的武具。诸如治疗，充能等回复手段也因此受到影响。";
    }

}
