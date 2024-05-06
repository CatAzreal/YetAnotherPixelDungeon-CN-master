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
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.bonuses.resistances.FireResistance;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.debuffs.Burning;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.debuffs.Debuff;
import com.consideredhamster.yetanotherpixeldungeon.actors.hero.Hero;
import com.consideredhamster.yetanotherpixeldungeon.items.food.MeatStewed;
import com.consideredhamster.yetanotherpixeldungeon.items.potions.PotionOfBlessing;
import com.consideredhamster.yetanotherpixeldungeon.items.potions.PotionOfLiquidFlame;
import com.consideredhamster.yetanotherpixeldungeon.items.potions.PotionOfRage;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.ItemSprite;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.ItemSpriteSheet;

public class FirebloomHerb extends Herb {

    private static final ItemSprite.Glowing ORANGE = new ItemSprite.Glowing( 0xFF5511 );

    {
        name = "焰花草";
        image = ItemSpriteSheet.HERB_FIREBLOOM;

        cooking = SpicyMeat.class;
        message = "好辣！";

        mainPotion = PotionOfLiquidFlame.class;

        subPotions.add( PotionOfBlessing.class );
        subPotions.add( PotionOfRage.class );
    }

    private static void onConsume( Hero hero, float duration ) {

        BuffActive.add( hero, FireResistance.class, duration );
        Debuff.remove( hero, Burning.class );

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
        return "曾有江湖骗子利用焰花草来表演“烈火丛中过，片焰不沾身”的技巧，用于博人眼球，但如今这种把戏已经人尽皆知。\n\n"+
                "焰花草能炼制_液火_、_神祝_、_狂怒_药剂。直接服用则解除_燃烧_效果并获得短暂的_火焰属性_抗性。";
//        return "[临时字串]可精炼液火/祝圣/狂怒药剂，直接食用可移除燃烧效果获得火焰抗性(短)";
//        return "Charlatans sometimes used Firebloom herbs to impress crowds by being allegedly " +
//                "unaffected by lighted candles or hot coals. Now it is a pretty known trick." +
//                "\n\n" +
//                "These herbs are used to brew potions of _Liquid Flame_, _Blessing_ and _Rage_. " +
//                "Consuming them will remove _burning_ and grant a short buff to your _fire_ resistance.";
    }

    public static class SpicyMeat extends MeatStewed {

        {
            name = "火辣炖肉";
            spiceGlow = ORANGE;
            message = "好辣！";
        }

        @Override
        public void onConsume( Hero hero ) {
            super.onConsume( hero );
            FirebloomHerb.onConsume( hero, DURATION_MEAT );
        }

        @Override
        public String desc() {
            return "一块与_焰花草_一同煮熟的肉，闻起来相当辣。食用后可解除_燃烧_状态并获得较长的_火焰属性_抗性。";
 //           return "[临时字串]直接食用可移除燃烧效果获得火焰抗性(长)";
//            return "This meat was stewed in a pot with a _Firebloom_ herb. It smells pretty spicy. " +
//                    "Consuming it will remove _burning_ and grant a long buff to your _fire_ resistance.";
        }

        @Override
        public int price() {
            return 30 * quantity;
        }
    }
}
