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

import com.consideredhamster.yapdcn.actors.buffs.BuffActive;
import com.consideredhamster.yapdcn.actors.buffs.bonuses.resistances.ColdResistance;
import com.consideredhamster.yapdcn.actors.buffs.debuffs.Debuff;
import com.consideredhamster.yapdcn.actors.buffs.debuffs.Frozen;
import com.consideredhamster.yapdcn.actors.hero.Hero;
import com.consideredhamster.yapdcn.items.food.MeatStewed;
import com.consideredhamster.yapdcn.items.potions.PotionOfConfusionGas;
import com.consideredhamster.yapdcn.items.potions.PotionOfFrigidVapours;
import com.consideredhamster.yapdcn.items.potions.PotionOfInvisibility;
import com.consideredhamster.yapdcn.visuals.sprites.ItemSprite;
import com.consideredhamster.yapdcn.visuals.sprites.ItemSpriteSheet;

public class IcecapHerb extends Herb {

    private static final ItemSprite.Glowing BLUE = new ItemSprite.Glowing( 0x2244FF );

    {
        name = "冰罩草";
        image = ItemSpriteSheet.HERB_ICECAP;

        cooking = MintyMeat.class;
        message = "That herb tasted fresh like mint.";

        mainPotion = PotionOfFrigidVapours.class;

        subPotions.add( PotionOfConfusionGas.class );
        subPotions.add( PotionOfInvisibility.class );
    }

    private static void onConsume( Hero hero, float duration ) {

        BuffActive.add( hero, ColdResistance.class, duration );
        Debuff.remove( hero, Frozen.class );

    }

    @Override
    public void onConsume( Hero hero ) {
        super.onConsume( hero );
        onConsume( hero, DURATION_HERB );
    }

    @Override
    public int price() {
        return 15 * quantity;
    }

    @Override
    public String desc() {
        return "[临时字串]可精炼冰雾/隐形/幻气药剂，直接食用可移除冻伤状态并获得寒冷抗性(短)";
//        return "Icecap herbs feel cold to touch and have some numbing capabilities. Northern " +
//                "tribes sometimes use Icecap herbs as a food to keep themselves from frigid " +
//                "climate of their lands." +
//                "\n\n" +
//                "These herbs are used to brew potions of _Frigid Vapours_, _Invisibility_ and _Confusion Gas_. " +
//                "Consuming them will remove _chilling_ and grant a short buff to your _cold_ resistance.";
    }

    public static class MintyMeat extends MeatStewed {

        {
            name = "清爽炖肉";
            spiceGlow = BLUE;
            message = "That meat tasted fresh like mint.";
        }

        @Override
        public void onConsume( Hero hero ) {
            super.onConsume( hero );
            IcecapHerb.onConsume( hero, DURATION_MEAT );
        }

        @Override
        public String desc() {
            return "[临时字串]直接食用可移除冻伤状态并获得寒冷抗性(长)";
//            return "This meat was stewed in a pot with an _Icecap_ herb. It smells somewhat minty. " +
//                    "Consuming it will remove _chilling_ and grant a long buff to your _cold_ resistance.";
        }

        @Override
        public int price() {
            return 30 * quantity;
        }
    }
}

