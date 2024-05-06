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
import com.consideredhamster.yetanotherpixeldungeon.actors.Char;
import com.consideredhamster.yetanotherpixeldungeon.actors.mobs.Bestiary;
import com.consideredhamster.yetanotherpixeldungeon.actors.mobs.Mob;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.CharSprite;
import com.consideredhamster.yetanotherpixeldungeon.visuals.ui.BuffIndicator;

public class Vertigo extends Debuff {

    @Override
    public Element buffType() {
        return Element.MIND;
    }

    @Override
    public String toString() {
        return "混乱";
    }

    @Override
    public String statusMessage() { return "混乱"; }

    @Override
    public String playerMessage() { return "周围的一切都转起来了！"; }

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
        return "整个世界都绕着你转起来了！你很难移动到目标地点，并且无法击中注意，在此期间敏捷和感知属性都会受到惩罚。";
    }

    @Override
    public boolean attachTo( Char target ) {
        if (super.attachTo( target )) {

            if( target instanceof Mob ) {
                Mob mob =(Mob)target;

                if( mob.state == mob.HUNTING ) {
                    mob.state = mob.WANDERING;
                }
            }

            return true;
        } else {
            return false;
        }
    }
}
