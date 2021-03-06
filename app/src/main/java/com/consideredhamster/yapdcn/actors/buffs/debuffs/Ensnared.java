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
import com.consideredhamster.yapdcn.actors.Char;
import com.consideredhamster.yapdcn.visuals.sprites.CharSprite;
import com.consideredhamster.yapdcn.visuals.ui.BuffIndicator;

public class Ensnared extends Debuff {

    @Override
    public Element buffType() {
        return Element.ENSNARING;
    }

    @Override
    public String toString() {
        return "束缚";
    }

    @Override
    public String statusMessage() { return "束缚"; }

    @Override
    public String playerMessage() { return "你被束缚住了！尝试移动以挣扎逃脱！"; }

    @Override
    public int icon() {
        return BuffIndicator.ENSNARED;
    }

    @Override
    public void applyVisual() {
        target.sprite.add( CharSprite.State.ENSNARED );
    }

    @Override
    public void removeVisual() {
        target.sprite.remove( CharSprite.State.ENSNARED );
    }

    @Override
    public String description() {
        return "你被束缚住了。束缚状态下无法移动，闪避攻击会比平时更加困难，并且更容易被敌人察觉。你可以试着逃脱，但挣扎产生的响动必然会招来敌人注意。";
    }

    @Override
    public boolean attachOnLoad( Char target ) {
        if (super.attachOnLoad( target )) {
            target.rooted = true;
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean attachTo( Char target ) {
        if (super.attachTo( target )) {
            target.rooted = true;
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void detach() {
        target.rooted = false;
        super.detach();
    }

}