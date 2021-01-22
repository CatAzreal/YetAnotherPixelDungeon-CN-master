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
import com.consideredhamster.yapdcn.actors.buffs.bonuses.resistances.MindResistance;
import com.consideredhamster.yapdcn.actors.buffs.debuffs.Charmed;
import com.consideredhamster.yapdcn.actors.buffs.debuffs.Debuff;
import com.consideredhamster.yapdcn.actors.buffs.debuffs.Tormented;
import com.consideredhamster.yapdcn.actors.buffs.debuffs.Vertigo;
import com.consideredhamster.yapdcn.actors.hero.Hero;
import com.consideredhamster.yapdcn.items.food.MeatStewed;
import com.consideredhamster.yapdcn.items.potions.PotionOfConfusionGas;
import com.consideredhamster.yapdcn.items.potions.PotionOfMindVision;
import com.consideredhamster.yapdcn.items.potions.PotionOfRage;
import com.consideredhamster.yapdcn.visuals.sprites.ItemSprite;
import com.consideredhamster.yapdcn.visuals.sprites.ItemSpriteSheet;

public class DreamfoilHerb extends Herb {

    private static final ItemSprite.Glowing PURPLE = new ItemSprite.Glowing( 0xAA00AA );

    {
        name = "梦叶草";
        image = ItemSpriteSheet.HERB_DREAMFOIL;

        cooking = SweetMeat.class;
        message = "甜美如梦，神清气爽。";

        mainPotion = PotionOfMindVision.class;

        subPotions.add( PotionOfConfusionGas.class );
        subPotions.add( PotionOfRage.class );
    }

    public static void onConsume( Hero hero, float duration ) {

        BuffActive.add( hero, MindResistance.class, duration );

        Debuff.remove( hero, Vertigo.class );
        Debuff.remove( hero, Charmed.class );
        Debuff.remove( hero, Tormented.class );

    }

    @Override
    public void onConsume( Hero hero ) {
        super.onConsume( hero );
        onConsume( hero, DURATION_HERB );
    }

    @Override
    public int price() {
        return 10 * quantity;
    }

    @Override
    public String desc() {
        return "梦叶草因其特殊的功效而时常有人种植，哪怕不惜违反法令。依据使用方法的不同，梦叶草会以不同的方式干扰使用者的精神。\n\n"+
                "梦叶草可炼制_灵视_、_幻气_、_狂怒_药剂。直接服用则能解除_精神属性负面状态_并获得短暂的_精神属性_抗性。";
  //      return "[临时字串]可精炼灵识/幻气/盛怒药剂，食用移除精神负面状态，精神状态抗性(短)";
//        return "Dreamfoil herbs are often intentionally cultivated because of their curious " +
//                "effects... Sometimes even illegally. Still, depending on how they are used, they " +
//                "can affect one's mind in a variety of ways." +
//                "\n\n" +
//                "These herbs can be used to brew potions of _Mind Vision_, _Confusion Gas_ and " +
//                "_Rage_. Consuming them will remove _mind debuffs_ and grant a short " +
//                "buff to your _mind_ resistance.";
    }

    public static class SweetMeat extends MeatStewed {

        {
            name = "香甜炖肉";
            spiceGlow = PURPLE;
            message = "梦境般的甜美味道。";
        }

        @Override
        public void onConsume( Hero hero ) {
            super.onConsume( hero );
            DreamfoilHerb.onConsume( hero, DURATION_MEAT );
        }

        @Override
        public String desc() {
            return "一块与_梦叶草_一同煮熟的肉，闻起来相当甜美。食用后可解除_精神属性负面状态_并获得较长的_精神属性_抗性。";
//            return "[临时字串]移除精神负面状态，精神状态抗性(长)";
//            return "This meat was stewed in a pot with a _Dreamfoil_ herb. It smells pretty sweet. " +
//                "Consuming it will remove _mind debuffs_ and grant a long buff to your _mind_ resistance.";
        }

        @Override
        public int price() {
            return 20 * quantity;
        }
    }
}


