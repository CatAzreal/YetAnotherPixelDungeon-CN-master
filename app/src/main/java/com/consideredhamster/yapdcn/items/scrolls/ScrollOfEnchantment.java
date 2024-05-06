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

import com.consideredhamster.yapdcn.visuals.effects.SpellSprite;
import com.consideredhamster.yapdcn.visuals.effects.particles.EnergyParticle;
import com.consideredhamster.yapdcn.visuals.effects.particles.ShadowParticle;
import com.consideredhamster.yapdcn.items.Item;
import com.consideredhamster.yapdcn.items.armours.Armour;
import com.consideredhamster.yapdcn.items.rings.Ring;
import com.consideredhamster.yapdcn.items.wands.Wand;
import com.consideredhamster.yapdcn.items.weapons.Weapon;
import com.consideredhamster.yapdcn.misc.utils.GLog;
import com.consideredhamster.yapdcn.visuals.windows.WndBag;

public class ScrollOfEnchantment extends InventoryScroll {

    private static final String TXT_ITEM_ENCHANT	= "卷轴将你的%s转换为%s！";
    private static final String TXT_ITEM_UPGRADE	= "因%s已经被附魔，卷轴对其进行了升级！";
    private static final String TXT_ITEM_RESISTS	= "卷轴弱化了%s上的诅咒！";
    private static final String TXT_ITEM_UNCURSE	= "卷轴移除了%s上的诅咒！";
	private static final String TXT_ITEM_UNKNOWN	= "卷轴在%s没能起到任何作用！";

	{
		name = "附魔卷轴";
        shortName = "En";

		inventoryTitle = "选择一个可被附魔的物品";
		mode = WndBag.Mode.ENCHANTABLE;

        spellSprite = SpellSprite.SCROLL_ENCHANT;
        spellColour = SpellSprite.COLOUR_RUNE;
	}
	
	@Override
	protected void onItemSelected( Item item ) {

        if (item instanceof Weapon) {

            Weapon weapon = (Weapon)item;
            
            if( weapon.bonus >= 0 ) {

                weapon.identify(ENCHANT_KNOWN);

                if (weapon.isEnchanted() && weapon.bonus < 3 ) {

                    weapon.upgrade();
                    GLog.p(TXT_ITEM_UPGRADE, weapon.name());

                } else {

                    weapon.enchant();
                    GLog.i( TXT_ITEM_ENCHANT, weapon.simpleName(), weapon.name() );

                }

                curUser.sprite.centerEmitter().burst(EnergyParticle.FACTORY, 10);

            } else {

//                weapon.bonus = Random.IntRange( item.bonus + 1, 0 );

                weapon.bonus++;
                weapon.identify( CURSED_KNOWN );
                curUser.sprite.emitter().start( ShadowParticle.UP, 0.05f, 10 );

                if( item.bonus < 0 ) {

                    GLog.n( TXT_ITEM_RESISTS, item.name() );
                    curUser.sprite.emitter().burst(ShadowParticle.CURSE, 4);

                } else {

                    GLog.w( TXT_ITEM_UNCURSE, item.name() );
                    curUser.sprite.emitter().burst(ShadowParticle.CURSE, 6);

                }
            }

        } else if (item instanceof Armour) {

            Armour armour = (Armour)item;

            if( armour.bonus >= 0 ) {

                armour.identify(ENCHANT_KNOWN);

                if (armour.isInscribed() && armour.bonus < 3 ) {

                    armour.upgrade();
                    GLog.p(TXT_ITEM_UPGRADE, armour.name());

                } else {

                    armour.inscribe();
                    GLog.i( TXT_ITEM_ENCHANT, armour.simpleName(), armour.name() );

                }

                curUser.sprite.centerEmitter().burst(EnergyParticle.FACTORY, 10);

            } else {

//                armour.bonus = Random.IntRange( item.bonus + 1, 0 );

                armour.bonus++;
                armour.identify( CURSED_KNOWN );
                curUser.sprite.emitter().start( ShadowParticle.UP, 0.05f, 10 );

                if( item.bonus < 0 ) {

                    GLog.n( TXT_ITEM_RESISTS, item.name() );
                    curUser.sprite.emitter().burst(ShadowParticle.CURSE, 4);

                } else {

                    GLog.w( TXT_ITEM_UNCURSE, item.name() );
                    curUser.sprite.emitter().burst(ShadowParticle.CURSE, 6);

                }
            }


        } else if ( item instanceof Wand || item instanceof Ring) {

            item.identify( CURSED_KNOWN );

            if( item.bonus >= 0 ) {

                if (item.bonus < 3 ) {

                    item.upgrade();

                    GLog.p(TXT_ITEM_UPGRADE, item.name());

                } else {

                    ScrollOfTransmutation.transmute( item );

                }

                curUser.sprite.centerEmitter().burst(EnergyParticle.FACTORY, 10);

            } else {

//                item.bonus = Random.IntRange( item.bonus + 1, 0 );

                item.bonus++;
                item.identify( CURSED_KNOWN );
                curUser.sprite.emitter().start( ShadowParticle.UP, 0.05f, 10 );

                if( item.bonus < 0 ) {

                    GLog.n( TXT_ITEM_RESISTS, item.name() );
                    curUser.sprite.emitter().burst(ShadowParticle.CURSE, 4);

                } else {

                    GLog.w( TXT_ITEM_UNCURSE, item.name() );
                    curUser.sprite.emitter().burst(ShadowParticle.CURSE, 6);

                }
            }

        } else {

            GLog.w( TXT_ITEM_UNKNOWN, item.name() );
		
		}
	}
	
	@Override
	public String desc() {
		return
                "这张卷轴可以为未附魔武器护甲灌注一个随机附魔，或升级已经被附魔的物品。若使用在被诅咒的物品上，卷" +
                "轴之力则会试图扭转其上的诅咒，成功则将其转为正面效果。将其使用在无法被进一步强化的物品上可能会出现...不可预知的后果。";
//			"This scroll is able to imbue unenchanted weapon or armor with random enchantment, or " +
//            "even upgrade already enchanted item. Wands and rings count as enchanted items by default. " +
//            "If used on a cursed item, it will try to dispel the curse and will even turn its " +
//            "enchantment into benevolent one in case of success. Using it on something which can't " +
//            "be improved any further may have... unpredictable results.";
	}

    @Override
    public int price() {
        return isTypeKnown() ? 150 * quantity : super.price();
    }
}
