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

import com.consideredhamster.yapdcn.Element;
import com.consideredhamster.yapdcn.visuals.sprites.CharSprite;
import com.consideredhamster.yapdcn.visuals.ui.BuffIndicator;

import java.util.HashSet;

public class Shielding extends Bonus {

    public static final HashSet<Class<? extends Element>> RESISTS;
    public static final float RESISTANCE = 0.25f;

    static {
        RESISTS = new HashSet<>();

        RESISTS.add(Element.Physical.class);

        RESISTS.add(Element.Flame.class);
        RESISTS.add(Element.Flame.Periodic.class);
        RESISTS.add(Element.Shock.class);
        RESISTS.add(Element.Shock.Periodic.class);
        RESISTS.add(Element.Acid.class);
        RESISTS.add(Element.Acid.Periodic.class);
        RESISTS.add(Element.Unholy.class);
        RESISTS.add(Element.Frost.class);
    }


    @Override
    public String toString() {
        return "护障";
    }

    @Override
    public String statusMessage() { return "护障"; }

    @Override
    public String playerMessage() { return "你被一道魔法壁障保护了起来！"; }

    @Override
    public int icon() {
        return BuffIndicator.SHIELDED;
    }

    @Override
    public void applyVisual() {
        target.sprite.add( CharSprite.State.PROTECTION );
    }

    @Override
    public void removeVisual() {
        target.sprite.remove( CharSprite.State.PROTECTION );
    }

	@Override
    public String description() {
        return "神圣的光壁笼罩着你的全身，提升了你的护甲等级与元素抗性。";
    }
}
