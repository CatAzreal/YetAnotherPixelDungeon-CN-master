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
import com.consideredhamster.yapdcn.actors.buffs.bonuses.resistances.MagicalResistance;
import com.consideredhamster.yapdcn.actors.hero.Hero;
import com.consideredhamster.yapdcn.items.food.MeatStewed;
import com.consideredhamster.yapdcn.items.potions.PotionOfInvisibility;
import com.consideredhamster.yapdcn.items.potions.PotionOfLevitation;
import com.consideredhamster.yapdcn.items.potions.PotionOfWisdom;
import com.consideredhamster.yapdcn.items.potions.UnstablePotion;
import com.consideredhamster.yapdcn.visuals.sprites.ItemSprite;
import com.consideredhamster.yapdcn.visuals.sprites.ItemSpriteSheet;

public class FeyleafHerb extends Herb {

    private static final ItemSprite.Glowing CYAN = new ItemSprite.Glowing( 0xa1ffff );

    {
        name = "绯叶草";
        image = ItemSpriteSheet.HERB_FEYLEAF;

        cooking = TenderMeat.class;
        message = "That herb had a very delicate taste.";

        //these herbs cannot be brewed with themselves
        mainPotion = UnstablePotion.class;

        subPotions.add( PotionOfInvisibility.class );
        subPotions.add( PotionOfLevitation.class );
        subPotions.add( PotionOfWisdom.class );
    }

    private static void onConsume( Hero hero, float duration ) {

        BuffActive.add( hero, MagicalResistance.class, duration );

    }

    @Override
    public void onConsume( Hero hero ) {
        super.onConsume( hero );
        onConsume( hero, DURATION_HERB );
    }

    @Override
    public int price() {
        return 20 * quantity;
    }

    @Override
    public String desc() {
        return "[临时字串]可精炼隐形/浮空/智慧药剂，直接食用可获得魔法抗性(短)";
//        return "Stories tell that the original Feyleaf was once a dryad, given a new form by the " +
//                "twin gods either as a reward or punishment. These herbs usually grow in secluded " +
//                "and secretive places and are essential for brewing some of the more curious potions." +
//                "\n\n" +
//                "These herbs are used to brew potions of _Invisibility_, _Levitation_ and _Wisdom_ " +
//                "when combined with other herbs, but cannot be brewed with another such herb. " +
//                "Consuming them will grant a short buff to your _magical_ resistance.";
    }

    public static class TenderMeat extends MeatStewed {

        {
            name = "水嫩炖肉";
            spiceGlow = CYAN;
            message = "That meat had a very delicate taste.";
        }

        @Override
        public void onConsume( Hero hero ) {
            super.onConsume( hero );
            FeyleafHerb.onConsume( hero, DURATION_MEAT );
        }

        @Override
        public String desc() {
            return "[临时字串]直接食用可获得魔法抗性(长)";
//            return "This meat was stewed in a pot with a _Feyleaf_ herb. It smells pretty good. " +
//                "Consuming it will grant a long buff to your _magical_ resistance.";
        }

        @Override
        public int price() {
            return 40 * quantity;
        }

    }
}

