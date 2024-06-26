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
package com.consideredhamster.yapdcn.items.scrolls;

import com.consideredhamster.yapdcn.items.armours.Armour;
import com.consideredhamster.yapdcn.items.rings.Ring;
import com.consideredhamster.yapdcn.items.wands.Wand;
import com.consideredhamster.yapdcn.items.weapons.Weapon;
import com.consideredhamster.yapdcn.visuals.effects.Flare;
import com.watabou.utils.Random;
import com.consideredhamster.yapdcn.visuals.effects.Speck;
import com.consideredhamster.yapdcn.visuals.effects.SpellSprite;
import com.consideredhamster.yapdcn.items.Item;
import com.consideredhamster.yapdcn.misc.utils.GLog;

public class ScrollOfDetectMagic extends Scroll {

//    private static final String TXT_REVEALED	= "You notice something peculiar!";
    private static final String TXT_IDENTIFIED	= "你的物品得到了鉴定！";
    private static final String TXT_NOTHING 	= "什么都没发生。";

	{
		name = "探魔卷轴";
        shortName = "DM";

//		inventoryTitle = "Select an item to identify";
//		mode = WndBag.Mode.UNIDENTIFED;

        spellSprite = SpellSprite.SCROLL_IDENTIFY;
        spellColour = SpellSprite.COLOUR_RUNE;
	}

    @Override
    protected void doRead() {

        boolean identified = identify( curUser.belongings.backpack.items.toArray( new Item[0] ) );

        identified = identified || identify(
                curUser.belongings.weap1,
                curUser.belongings.weap2,
                curUser.belongings.armor,
                curUser.belongings.ring1,
                curUser.belongings.ring2
        );

        if( identified ){
            GLog.i( TXT_IDENTIFIED );
        } else {
            GLog.i( TXT_NOTHING );
        }

        new Flare( 5, 32 ).color( 0x3399FF, true ).show(curUser.sprite, 2f);
        curUser.sprite.emitter().start(Speck.factory(Speck.QUESTION), 0.1f, Random.IntRange( 7, 9));

        super.doRead();
    }

//	@Override
//	protected void onItemSelected( Item item ) {
//
//        curUser.sprite.emitter().start(Speck.factory(Speck.QUESTION), 0.1f, Random.IntRange(6, 9));
//
//        item.identify();
//        GLog.i("It is " + item);
//
//    }
	
	@Override
	public String desc() {
		return
                "蚀刻在这张卷轴上的符文能够赋予阅读者感应术法波段的能力，揭示携带物品上带有的超自然属性。这能使阅读者区分出所有附魔和诅咒装备，但不会提供这之外的更多信息。";
	}

    public static boolean identify( Item... items ) {

        boolean procced = false;

        for (int i=0; i < items.length; i++) {
            Item item = items[i];
            if (
                item instanceof Weapon || item instanceof Armour ||
                item instanceof Ring || item instanceof Wand
            ) {
                if( !item.isIdentified() ){
                    if( item.isMagical() ){
                        item.identify( CURSED_KNOWN );
                    } else {
                        item.identify();
                    }
                    procced = true;
                }
            }
        }

        return procced;
    }

	@Override
	public int price() {
		return isTypeKnown() ? 55 * quantity : super.price();
	}
}
