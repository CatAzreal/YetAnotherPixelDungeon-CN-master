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
import com.consideredhamster.yapdcn.actors.buffs.bonuses.resistances.BodyResistance;
import com.consideredhamster.yapdcn.actors.buffs.debuffs.Crippled;
import com.consideredhamster.yapdcn.actors.buffs.debuffs.Debuff;
import com.consideredhamster.yapdcn.actors.buffs.debuffs.Poisoned;
import com.consideredhamster.yapdcn.actors.buffs.debuffs.Withered;
import com.consideredhamster.yapdcn.actors.hero.Hero;
import com.consideredhamster.yapdcn.items.food.MeatStewed;
import com.consideredhamster.yapdcn.items.potions.PotionOfBlessing;
import com.consideredhamster.yapdcn.items.potions.PotionOfMending;
import com.consideredhamster.yapdcn.items.potions.PotionOfShield;
import com.consideredhamster.yapdcn.visuals.sprites.ItemSprite;
import com.consideredhamster.yapdcn.visuals.sprites.ItemSpriteSheet;


public class SungrassHerb extends Herb {

    private static final ItemSprite.Glowing YELLOW = new ItemSprite.Glowing( 0xFFFF44 );

    {
        name = "阳春草";
        image = ItemSpriteSheet.HERB_SUNGRASS;

        cooking = SavoryMeat.class;
        message = "吃起来不错！";

        mainPotion = PotionOfMending.class;

        subPotions.add( PotionOfBlessing.class );
        subPotions.add( PotionOfShield.class );
    }

    private static void onConsume( Hero hero, float duration ) {

        BuffActive.add( hero, BodyResistance.class, duration );

        Debuff.remove( hero, Poisoned.class );
        Debuff.remove( hero, Crippled.class );
        Debuff.remove( hero, Withered.class );

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
        return "阳春草具有强大的恢复功效，动物经常吞食阳春草解毒。即使在阳光极少照到的阴暗角落，阳春草仍能破土而出。\n\n"+
                "阳春草可以炼制_愈合_、_神祝_、_护盾_药剂。直接服用则会解除_体质属性负面效果_并获得短时间的_体质属性_抗性。";
//        return "[临时字串]可精炼愈合/祝圣/护盾药剂，直接食用移除物理减益效果，并获得物理减益抗性(短)";
//        return "Wild animals often eat Sungrass herbs to purge their body of toxins. Sprouts of " +
//                "this herb are pretty common in places where sunlight is scarse, but still present." +
//                "\n\n" +
//                "These herbs are used to brew potions of _Mending_, _Blessing_ and _Shield_. " +
//                "Consuming them will remove _body debuffs_ and grant a short buff to your _body_ resistance.";
    }

    public static class SavoryMeat extends MeatStewed {

        {
            name = "风味炖肉";
            spiceGlow = YELLOW;
            message = "五味俱全的美食！";
        }

        @Override
        public void onConsume( Hero hero ) {
            super.onConsume( hero );
            SungrassHerb.onConsume( hero, DURATION_MEAT );
        }

        @Override
        public String desc() {
            return "一块与_阳春草_一同煮熟的肉，闻起来相当美味。食用后可解除_体质属性负面效果_并获得较长时间的_体质属性_抗性。";
 //           return "[临时字串]直接食用移除物理减益效果，并获得物理减益抗性(长)";
//            return "This meat was stewed in a pot with a _Sungrass_ herb. It smells pretty tasty. " +
//                    "Consuming it will remove _body debuffs_ and grant a long buff to your _body_ resistance.";
        }

        @Override
        public int price() {
            return 20 * quantity;
        }
    }
}
