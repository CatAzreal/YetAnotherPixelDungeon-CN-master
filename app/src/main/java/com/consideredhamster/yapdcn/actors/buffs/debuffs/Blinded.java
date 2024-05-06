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

import com.consideredhamster.yapdcn.visuals.ui.BuffIndicator;

public class Blinded extends Debuff {

    public final static String TXT_CANNOT_READ = "读东西？现在？你连自己手都看不清楚。";

    @Override
    public String toString() {
        return "目盲";
    }

    @Override
    public String statusMessage() { return "目盲"; }

    @Override
    public String playerMessage() { return "你什么都看不见了！"; }

    @Override
    public int icon() {
        return BuffIndicator.BLINDED;
    }

//    @Override
//    public void applyVisual() {
//        target.sprite.add( CharSprite.State.VERTIGO );
//    }
//
//    @Override
//    public void removeVisual() {
//        target.sprite.remove( CharSprite.State.VERTIGO );
//    }

    @Override
    public String description() {
        return "你无法分辨视野内数寸之外的任何事物。你的命中和感知能力降低，并且持续期间无法阅读任何卷轴。";
    }
}
