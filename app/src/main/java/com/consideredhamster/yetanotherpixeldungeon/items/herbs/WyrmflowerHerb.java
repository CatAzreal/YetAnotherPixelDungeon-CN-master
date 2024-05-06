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

import com.consideredhamster.yetanotherpixeldungeon.actors.hero.Hero;
import com.consideredhamster.yetanotherpixeldungeon.items.food.MeatStewed;
import com.consideredhamster.yetanotherpixeldungeon.items.potions.PotionOfStrength;
import com.consideredhamster.yetanotherpixeldungeon.items.potions.PotionOfWisdom;
import com.consideredhamster.yetanotherpixeldungeon.items.potions.UnstablePotion;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.CharSprite;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.ItemSprite;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.ItemSpriteSheet;

public class WyrmflowerHerb extends Herb {

    private static final ItemSprite.Glowing RED = new ItemSprite.Glowing( 0xCC0000 );

    private static final int HPBONUS_HERB = 2;
    private static final int HPBONUS_MEAT = 4;

    {
        name = "龙藤花";
        image = ItemSpriteSheet.HERB_WYRMFLOWER;

        cooking = PotentMeat.class;
        message = "很浓的味道。";

        //these herbs cannot be brewed with themselves
        mainPotion = UnstablePotion.class;

        subPotions.add( PotionOfWisdom.class );
        subPotions.add( PotionOfStrength.class );
    }

    private static void onConsume( Hero hero, int hpBonus ) {
        hero.HP = hero.HT += hpBonus;
        hero.sprite.showStatus( CharSprite.POSITIVE, "+%d 最大生命", hpBonus );
    }

    @Override
    public void onConsume( Hero hero ) {
        super.onConsume( hero );
        onConsume( hero, HPBONUS_HERB );
    }

    @Override
    public int price() {
        return 25 * quantity;
    }

    @Override
    public String desc() {
        return "一种稀有的药草，其强大的药性使无数炼金术士为止着迷。龙藤花是几种最强大的药剂的关键原料，其价值不可小视。\n\n"+
                "龙藤花可与其它草药共炼_智慧_、_力量_药剂，但无法炼制两株龙藤花。直接服用则会使你的最大生命增加_2_点。";
 //       return "[临时字串]可精炼智慧/力量药剂，该药草无法同时精炼两株，食用后最大生命+2";
//        return "A very rare herb, it is often sought by alchemists for its powerful alchemical " +
//                "properties. Being a key ingredient for the most potent of potions, such valuable " +
//                "find cannot be underappreciated." +
//                "\n\n" +
//                "These herbs are used to brew potions of _Wisdom_ and _Strength_ " +
//                "when combined with other herbs, but cannot be brewed with another such herb. " +
//                "Consuming them will increase your maximum health by _2_ points.";
    }

    public static class PotentMeat extends MeatStewed {

        {
            name = "盈能炖肉";
            spiceGlow = RED;
            message = "很浓的药草味。";
        }

        @Override
        public void onConsume( Hero hero ) {
            super.onConsume( hero );
            WyrmflowerHerb.onConsume( hero, HPBONUS_MEAT );
        }

        @Override
        public String desc() {
            return "一块与_龙藤花_一同煮熟的肉，闻起来有股浓重的药草味。食用后可使你的最大生命增加_4_点。";
 //           return "食用后+4最大生命";
//            return "This meat was stewed in a pot with a _Wyrmflower_ herb. It smells pretty potent. " +
//                    "Consuming it will increase your maximum health by _4_ points.";
        }

        @Override
        public int price() {
            return 50 * quantity;
        }
    }
}

