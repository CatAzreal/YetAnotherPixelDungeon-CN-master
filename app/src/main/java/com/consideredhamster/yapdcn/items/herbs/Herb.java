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
package com.consideredhamster.yapdcn.items.herbs;

import com.consideredhamster.yapdcn.actors.buffs.special.Satiety;
import com.consideredhamster.yapdcn.items.food.Food;
import com.consideredhamster.yapdcn.items.food.MeatStewed;
import com.consideredhamster.yapdcn.items.potions.Potion;

import java.util.ArrayList;

public abstract class Herb extends Food {

    protected static final float DURATION_HERB = 10f;
    protected static final float DURATION_MEAT = 50f;

    public Class<? extends MeatStewed> cooking;
    public Class<? extends Potion> mainPotion;
    public ArrayList<Class<? extends Potion>> subPotions = new ArrayList<>();

    {
        energy = Satiety.MAXIMUM * 0.05f;
        time = 1.0f;
    }

    @Override
    public String info() {
        return desc() + "\n\n" +
            "食用该药草只需_" + (int)time + "_回合并且恢复_" + (int)( energy / 10 ) + "%_的饱食度。";
    }
}

