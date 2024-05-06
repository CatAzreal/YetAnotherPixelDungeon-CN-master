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
package com.consideredhamster.yetanotherpixeldungeon.items.herbs;

import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.BuffActive;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.bonuses.resistances.MagicalResistance;
import com.consideredhamster.yetanotherpixeldungeon.actors.hero.Hero;
import com.consideredhamster.yetanotherpixeldungeon.items.food.MeatStewed;
import com.consideredhamster.yetanotherpixeldungeon.items.potions.PotionOfInvisibility;
import com.consideredhamster.yetanotherpixeldungeon.items.potions.PotionOfLevitation;
import com.consideredhamster.yetanotherpixeldungeon.items.potions.PotionOfWisdom;
import com.consideredhamster.yetanotherpixeldungeon.items.potions.UnstablePotion;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.ItemSprite;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.ItemSpriteSheet;

public class FeyleafHerb extends Herb {

    private static final ItemSprite.Glowing CYAN = new ItemSprite.Glowing( 0xa1ffff );

    {
        name = "绯叶草";
        image = ItemSpriteSheet.HERB_FEYLEAF;

        cooking = TenderMeat.class;
        message = "吃起来有一种淡淡的清香。";

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
        return "传说记载绯叶草曾是一种蝴蝶，在神的影响下化为了这种药草。这种药草通常生长在偏僻而隐蔽的地方，是多种稀有药水的基础原料。\n\n"+
                "绯叶草可与其它草药共炼_隐形_、_浮空_、_智慧_药剂，但无法炼制两株绯叶草。直接服用则会获得短暂的_魔法属性_抗性。";
  //      return "[临时字串]可精炼隐形/浮空/智慧药剂，直接食用可获得魔法抗性(短)";
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
            message = "吃起来有一种淡淡的清香。";
        }

        @Override
        public void onConsume( Hero hero ) {
            super.onConsume( hero );
            FeyleafHerb.onConsume( hero, DURATION_MEAT );
        }

        @Override
        public String desc() {
            return "一块与_绯叶草_一同煮熟的肉，闻起来相当甜美。食用后可获得较长的_魔法属性_抗性。";
 //           return "[临时字串]直接食用可获得魔法抗性(长)";
//            return "This meat was stewed in a pot with a _Feyleaf_ herb. It smells pretty good. " +
//                "Consuming it will grant a long buff to your _magical_ resistance.";
        }

        @Override
        public int price() {
            return 40 * quantity;
        }

    }
}

