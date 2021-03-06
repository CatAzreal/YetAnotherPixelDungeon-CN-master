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
import com.consideredhamster.yapdcn.actors.buffs.bonuses.resistances.PhysicalResistance;
import com.consideredhamster.yapdcn.actors.hero.Hero;
import com.consideredhamster.yapdcn.items.food.MeatStewed;
import com.consideredhamster.yapdcn.items.potions.PotionOfShield;
import com.consideredhamster.yapdcn.items.potions.PotionOfStrength;
import com.consideredhamster.yapdcn.items.potions.PotionOfWebbing;
import com.consideredhamster.yapdcn.items.potions.UnstablePotion;
import com.consideredhamster.yapdcn.visuals.sprites.ItemSprite;
import com.consideredhamster.yapdcn.visuals.sprites.ItemSpriteSheet;

public class EarthrootHerb extends Herb {

    private static final ItemSprite.Glowing BROWN = new ItemSprite.Glowing( 0x663300 );

    {
        name = "地缚根";
        image = ItemSpriteSheet.HERB_EARTHROOT;

        cooking = ChewyMeat.class;
        message = "韧性十足，难以咀嚼。";

        //these herbs cannot be brewed with themselves
        mainPotion = UnstablePotion.class;

        subPotions.add( PotionOfStrength.class );
        subPotions.add( PotionOfWebbing.class );
        subPotions.add( PotionOfShield.class );
    }

    private static void onConsume( Hero hero, float duration ) {

        BuffActive.add( hero, PhysicalResistance.class, duration );
//        Debuff.remove( hero, Ensnared.class );

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
        return "一些部落将地缚根用作仪祭中的食物，据说吃下它会变得更为坚强。然而采集一些地缚根绝非易事，因为它们多生长在幽深的洞窟中。\n\n"+
                "地缚根可与其它草药共炼_护盾_、_结网_、_力量_药剂，但无法炼制两株地缚根。直接服用则会获取短时间的_物理_抗性。";
        //return "[临时字串]可精炼护盾/织网/力量药剂，直接食用可获得物理伤害抗性(短)";
//        return "Certain tribes use these roots as food in their rituals, as it is believed that " +
//                "consuming them improves fortitude. Actually finding one of these roots is " +
//                "usually a feat in itself, given that they usually grow in deep caverns." +
//                "\n\n" +
//                "These herbs are used to brew potions of _Shield_, _Webbing_ and _Strength_ " +
//                "when combined with other herbs, but cannot be brewed with another such herb. " +
//                "Consuming them will grant a short buff to your _physical_ resistance.";
    }

    public static class ChewyMeat extends MeatStewed {

        {
            name = "筋道炖肉";
            spiceGlow = BROWN;
            message = "一块嚼劲十足的炖肉。";
        }

        @Override
        public void onConsume( Hero hero ) {
            super.onConsume( hero );
            EarthrootHerb.onConsume( hero, DURATION_MEAT );
        }

        @Override
        public String desc() {
              return "一块与_地缚根_一同煮熟的肉，看起来硬邦邦的。食用后可获得较长时间的_物理_抗性。";
    //        return "[临时字串]可获得物理伤害抗性(长)";
//            return "This meat was stewed in a pot with an _Earthroot_ herb. It feels to be rough. " +
//                "Consuming it will grant a long buff to your _physical_ resistance.";
        }

        @Override
        public int price() {
            return 40 * quantity;
        }

    }
}

