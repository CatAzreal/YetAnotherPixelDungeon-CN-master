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
import com.consideredhamster.yapdcn.actors.buffs.bonuses.resistances.AcidResistance;
import com.consideredhamster.yapdcn.actors.buffs.debuffs.Corrosion;
import com.consideredhamster.yapdcn.actors.buffs.debuffs.Debuff;
import com.consideredhamster.yapdcn.actors.hero.Hero;
import com.consideredhamster.yapdcn.items.food.MeatStewed;
import com.consideredhamster.yapdcn.items.potions.PotionOfCausticOoze;
import com.consideredhamster.yapdcn.items.potions.PotionOfToxicGas;
import com.consideredhamster.yapdcn.items.potions.PotionOfWebbing;
import com.consideredhamster.yapdcn.visuals.sprites.ItemSprite;
import com.consideredhamster.yapdcn.visuals.sprites.ItemSpriteSheet;

public class SorrowmossHerb extends Herb {

    private static final ItemSprite.Glowing GREEN = new ItemSprite.Glowing( 0x009900 );

    {
        name = "苦苔草";
        image = ItemSpriteSheet.HERB_SORROWMOSS;

        cooking = BitterMeat.class;
        message = "良药苦口利于病。";

        mainPotion = PotionOfCausticOoze.class;

        subPotions.add( PotionOfToxicGas.class );
        subPotions.add( PotionOfWebbing.class );
    }

    private static void onConsume( Hero hero, float duration ) {
        BuffActive.add( hero, AcidResistance.class, duration );
        Debuff.remove( hero, Corrosion.class );
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
        return "据说苦苔草常在悲剧发生过的地方生长。尽管苦苔草的来历与用法都有些骇人，实际上它是安全可食用的。\n\n"+
                "苦苔草可以炼制_毒气_、_蚀泥_、_结网_药剂。直接服用则会解除_腐蚀_状态并获得短时间的_酸蚀属性_抗性。";
    //    return "[临时字串]可精炼毒气/蚀泥/幻气药剂，直接食用可移除淤泥状态，并获得酸蚀抗性(短)";
//        return "It is said that Sorrowmoss usually grows in places where a great tragedy took " +
//                "place. Despite its reputation and applications, it is actually completely safe to eat." +
//                "\n\n" +
//                "These herbs are used to brew potions of _Toxic Gas_, _Caustic Ooze_ and _Confusion Gas_. " +
//                "Consuming them will remove _corrosion_ and grant a short buff to your _acid_ resistance.";
    }

    public static class BitterMeat extends MeatStewed {

        {
            name = "焦苦炖肉";
            spiceGlow = GREEN;
            message = "像磨难一样煎熬，像失败一样苦涩。";
        }

        @Override
        public void onConsume( Hero hero ) {
            super.onConsume( hero );
            SorrowmossHerb.onConsume( hero, DURATION_MEAT );
        }

        @Override
        public String desc() {
            return "一块与_苦苔草_一同煮熟的肉，闻起来有股浓重的苦味。食用后可解除_腐蚀_状态并获得较长的_酸蚀属性_抗性";
//            return "[临时字串]可移除淤泥状态，并获得酸蚀抗性(短)";
//            return "This meat was stewed in a pot with a _Sorrowmoss_ herb. It smells pretty bitter. " +
//                    "Consuming it will remove _corrosion_ and grant a long buff to your _acid_ resistance.";
        }

        @Override
        public int price() {
            return 30 * quantity;
        }
    }
}

