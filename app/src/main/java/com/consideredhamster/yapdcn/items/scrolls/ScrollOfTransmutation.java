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

import com.consideredhamster.yapdcn.Dungeon;
import com.consideredhamster.yapdcn.visuals.effects.Speck;
import com.consideredhamster.yapdcn.visuals.effects.SpellSprite;
import com.consideredhamster.yapdcn.visuals.effects.particles.ShadowParticle;
import com.consideredhamster.yapdcn.items.Generator;
import com.consideredhamster.yapdcn.items.Item;
import com.consideredhamster.yapdcn.items.armours.Armour;
import com.consideredhamster.yapdcn.items.armours.body.BodyArmor;
import com.consideredhamster.yapdcn.items.armours.body.BodyArmorCloth;
import com.consideredhamster.yapdcn.items.armours.body.BodyArmorHeavy;
import com.consideredhamster.yapdcn.items.armours.body.BodyArmorLight;
import com.consideredhamster.yapdcn.items.armours.shields.Shield;
import com.consideredhamster.yapdcn.items.rings.Ring;
import com.consideredhamster.yapdcn.items.wands.Wand;
import com.consideredhamster.yapdcn.items.wands.WandUtility;
import com.consideredhamster.yapdcn.items.weapons.Weapon;
import com.consideredhamster.yapdcn.items.weapons.melee.MeleeWeaponLightOH;
import com.consideredhamster.yapdcn.items.weapons.throwing.ThrowingWeapon;
import com.consideredhamster.yapdcn.items.weapons.throwing.ThrowingWeaponAmmo;
import com.consideredhamster.yapdcn.items.weapons.throwing.ThrowingWeaponHeavy;
import com.consideredhamster.yapdcn.items.weapons.throwing.ThrowingWeaponLight;
import com.consideredhamster.yapdcn.items.weapons.throwing.ThrowingWeaponSpecial;
import com.consideredhamster.yapdcn.visuals.sprites.HeroSprite;
import com.consideredhamster.yapdcn.misc.utils.GLog;
import com.consideredhamster.yapdcn.visuals.windows.WndBag;

public class ScrollOfTransmutation extends InventoryScroll {

	private static final String TXT_ITEM_TRANSMUTED	= "你的%s转化成了%s！";
    private static final String TXT_ITEM_RESISTS	= "你的%s之上的诅咒抗拒了嬗变之力！";
	private static final String TXT_ITEM_UNKNOWN	= "%s无法被转化！";

	{
		name = "嬗变卷轴";
        shortName = "Tr";

		inventoryTitle = "选择一个可嬗变物品";
		mode = WndBag.Mode.TRANSMUTABLE;

        spellSprite = SpellSprite.SCROLL_TRANSMUT;
        spellColour = SpellSprite.COLOUR_WILD;
	}
	
	@Override
	protected void onItemSelected( Item item ) {

        if( item.bonus < 0  ) {

            item.identify( CURSED_KNOWN );
            GLog.w(TXT_ITEM_RESISTS, item.name());
            curUser.sprite.emitter().burst( ShadowParticle.CURSE, 6 );

        } else if( !transmute( item ) ) {

            item.identify( CURSED_KNOWN );
            GLog.w( TXT_ITEM_UNKNOWN, item.name() );
//            curUser.sprite.emitter().start( Speck.factory(Speck.CHANGE), 0.1f, 3 );

		} else {

            item.fix();
            item.identify( CURSED_KNOWN );
//            curUser.sprite.emitter().start( Speck.factory(Speck.CHANGE), 0.1f, 5 );

        }
	}

    public static boolean transmute( Item item ) {

        if(item instanceof ThrowingWeapon) {

            Item newItem = changeThrowing((ThrowingWeapon) item);

            GLog.i(TXT_ITEM_TRANSMUTED, item.name(), newItem.name());

            if( curUser.belongings.weap1 == item ) {

                curUser.belongings.weap1 = (Weapon)newItem;

            } else if( curUser.belongings.weap2 == item ) {

                curUser.belongings.weap2 = (Weapon) newItem;

            } else {

                item.detachAll(Dungeon.hero.belongings.backpack);

                if (!newItem.doPickUp(curUser)) {
                    Dungeon.level.drop(newItem, curUser.pos).sprite.drop();
                }
            }

        } else if(item instanceof Weapon) {

            Item newItem = changeWeapon((Weapon) item);

            GLog.i(TXT_ITEM_TRANSMUTED, item.name(), newItem.name());

            if( curUser.belongings.weap1 == item ) {

                curUser.belongings.weap1 = (Weapon)newItem;

            } else if( curUser.belongings.weap2 == item ) {

                if( newItem instanceof MeleeWeaponLightOH ) {

                    curUser.belongings.weap2 = (Weapon) newItem;

                } else {

                    curUser.belongings.weap2 = null;

                    if (!newItem.doPickUp(curUser)) {
                        Dungeon.level.drop(newItem, curUser.pos).sprite.drop();
                    }

                }

            } else {

                item.detach(Dungeon.hero.belongings.backpack);

                if (!newItem.doPickUp(curUser)) {
                    Dungeon.level.drop(newItem, curUser.pos).sprite.drop();
                }
            }

        } else if (item instanceof Armour) {

            Item newItem = changeArmour((Armour) item);

            GLog.i(TXT_ITEM_TRANSMUTED, item.name(), newItem.name());

            if( curUser.belongings.weap2 == item ) {

                curUser.belongings.weap2 = (Shield) newItem;

            } else if( curUser.belongings.armor == item ) {

                curUser.belongings.armor = (BodyArmor) newItem;
                ((HeroSprite)curUser.sprite).updateArmor();

            } else {

                item.detach(Dungeon.hero.belongings.backpack);

                if (!newItem.doPickUp(curUser)) {
                    Dungeon.level.drop(newItem, curUser.pos).sprite.drop();
                }
            }

        } else if (item instanceof Ring) {

            Item newItem = changeRing((Ring) item);

            GLog.i(TXT_ITEM_TRANSMUTED, item.name(), newItem.name());

            if( curUser.belongings.ring1 == item ) {

                newItem.identify( ENCHANT_KNOWN );
                GLog.i(TXT_ITEM_TRANSMUTED, item.name(), newItem.name());

                curUser.belongings.ring1.deactivate( curUser );
                curUser.belongings.ring1 = (Ring)newItem;
                curUser.belongings.ring1.activate( curUser );

            } else if( curUser.belongings.ring2 == item ) {

                newItem.identify( ENCHANT_KNOWN );
                GLog.i(TXT_ITEM_TRANSMUTED, item.name(), newItem.name());

                curUser.belongings.ring2.deactivate( curUser );
                curUser.belongings.ring2 = (Ring)newItem;
                curUser.belongings.ring2.activate( curUser );

            } else {

                item.detach(Dungeon.hero.belongings.backpack);

                if (!newItem.doPickUp(curUser)) {
                    Dungeon.level.drop(newItem, curUser.pos).sprite.drop();
                }
            }

        } else if (item instanceof Wand) {

            Item newItem = changeWand((Wand) item);

            GLog.i(TXT_ITEM_TRANSMUTED, item.name(), newItem.name());

            if (curUser.belongings.weap2 == item) {

                curUser.belongings.weap2 = (Wand) newItem;
                curUser.belongings.weap2.activate(curUser);

            } else {

                item.detach(Dungeon.hero.belongings.backpack);

                if (!newItem.doPickUp(curUser)) {
                    Dungeon.level.drop(newItem, curUser.pos).sprite.drop();
                }
            }
        } else {
            return false;
        }

        curUser.sprite.emitter().start( Speck.factory( Speck.CHANGE ), 0.2f, 5 );

        return true;
    }


    private static Weapon changeWeapon( Weapon w ) {

        Weapon n;
        do {
            n = (Weapon) Generator.random(Generator.Category.WEAPON, false);
        } while (n == null || n.getClass() == w.getClass() || n.weaponType() != w.weaponType() );

        n.known = w.known;
        n.state = w.state;
        n.bonus = w.bonus;
        n.enchantment = w.enchantment;
        n.durability = w.durability;

        return n;
    }

    private static Weapon changeThrowing( ThrowingWeapon w ) {

        ThrowingWeapon n = (ThrowingWeapon) Generator.random(Generator.Category.THROWING);

        if( w instanceof ThrowingWeaponAmmo ) {

            while (n == null || n.getClass() == w.getClass() || !( n instanceof ThrowingWeaponAmmo ) ) {
                n = (ThrowingWeapon) Generator.random(Generator.Category.THROWING, false);
            }

        } else if( w instanceof ThrowingWeaponSpecial ) {

            while (n == null || n.getClass() == w.getClass() || !( n instanceof ThrowingWeaponSpecial ) ) {
                n = (ThrowingWeapon) Generator.random(Generator.Category.THROWING, false);
            }

        } else if( w instanceof ThrowingWeaponLight ) {

            while (n == null || n.getClass() == w.getClass() || !( n instanceof ThrowingWeaponLight ) ) {
                n = (ThrowingWeapon) Generator.random(Generator.Category.THROWING, false);
            };

        } else if( w instanceof ThrowingWeaponHeavy ) {

            while (n == null || n.getClass() == w.getClass() || !( n instanceof ThrowingWeaponHeavy ) ) {
                n = (ThrowingWeapon) Generator.random(Generator.Category.THROWING, false);
            }

        }

        n.known = w.known;
        n.state = w.state;
        n.bonus = w.bonus;
        n.enchantment = w.enchantment;
        n.durability = w.durability;

        if( n.tier > w.tier ) {
            n.quantity = Math.max( 1, w.quantity * n.baseAmount() / w.baseAmount() );
        } else {
            n.quantity = w.quantity;
        }

        return n;
    }

    private static Armour changeArmour( Armour a ) {

        Armour n = (Armour) Generator.random(Generator.Category.ARMOR, false);

        if (a instanceof Shield) {
            while (n == null || n.getClass() == a.getClass() || !( n instanceof Shield ) ) {
                n = (Armour) Generator.random(Generator.Category.ARMOR, false);
            }
        } else if (a instanceof BodyArmorCloth) {
            while (n == null || n.getClass() == a.getClass() || !( n instanceof BodyArmorCloth ) ) {
                n = (Armour) Generator.random(Generator.Category.ARMOR, false);
            }
        } else if (a instanceof BodyArmorLight) {
            while (n == null || n.getClass() == a.getClass() || !( n instanceof BodyArmorLight ) ) {
                n = (Armour) Generator.random(Generator.Category.ARMOR, false);
            }
        } else if (a instanceof BodyArmorHeavy) {
            while (n == null || n.getClass() == a.getClass() || !( n instanceof BodyArmorHeavy ) ) {
                n = (Armour) Generator.random(Generator.Category.ARMOR, false);
            }
        }

        n.known = a.known;
        n.state = a.state;
        n.bonus = a.bonus;
        n.glyph = a.glyph;
        n.durability = a.durability;

        return n;
    }

    private static Ring changeRing( Ring r ) {
        Ring n;
        do {
            n = (Ring) Generator.random(Generator.Category.RING);
        } while (n == null || n.getClass() == r.getClass());

        n.bonus = r.bonus;
        n.known = r.known;



        return n;
    }

    private static Wand changeWand( Wand w ) {

        Wand n;
        do {
            n = (Wand)Generator.random( Generator.Category.WAND );
        } while (n == null || n.getClass() == w.getClass() || n instanceof WandUtility != w instanceof WandUtility);

        n.bonus = w.bonus;
        n.state = w.state;
        n.known = w.known;
        n.durability = w.durability;
        n.setCharges( w.getCharges() );

        return n;
    }
	
	@Override
	public String desc() {
		return
			"这张卷轴能够将一个道具转换成其他价值近似的道具，只能用在武器，护甲，法杖和戒指上。";
	}

    @Override
    public int price() {
        return isTypeKnown() ? 60 * quantity : super.price();
    }
}
