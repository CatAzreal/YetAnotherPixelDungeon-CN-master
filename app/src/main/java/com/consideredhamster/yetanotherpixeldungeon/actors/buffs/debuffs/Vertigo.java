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

public class Vertigo extends Debuff {

    @Override
    public Element buffType() {
        return Element.MIND;
    }

    @Override
    public String toString() {
        return "Vertigo";
    }

    @Override
    public String statusMessage() { return "vertigo"; }

    @Override
    public String playerMessage() { return "Everything is spinning around you!"; }

    @Override
    public int icon() {
        return BuffIndicator.VERTIGO;
    }

    @Override
    public void applyVisual() {
        target.sprite.add( CharSprite.State.VERTIGO );
    }

    @Override
    public void removeVisual() {
        target.sprite.remove( CharSprite.State.VERTIGO );
    }

    @Override
    public String description() {
        return "Everything is spinning around you! Your movement can be erratic at times, you " +
                "cannot concentrate, and your dexterity and perception are decreased.";
    }
}
