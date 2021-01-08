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
package com.consideredhamster.yetanotherpixeldungeon.items.weapons;

import com.consideredhamster.yetanotherpixeldungeon.items.rings.RingOfMysticism;
import com.consideredhamster.yetanotherpixeldungeon.items.weapons.melee.MeleeWeaponLightOH;
import com.consideredhamster.yetanotherpixeldungeon.scenes.GameScene;
import com.consideredhamster.yetanotherpixeldungeon.visuals.windows.WndOptions;
import com.watabou.utils.GameMath;
import com.consideredhamster.yetanotherpixeldungeon.Dungeon;
import com.consideredhamster.yetanotherpixeldungeon.actors.Char;
import com.consideredhamster.yetanotherpixeldungeon.actors.hero.Hero;
import com.consideredhamster.yetanotherpixeldungeon.items.EquipableItem;
import com.consideredhamster.yetanotherpixeldungeon.items.Item;
import com.consideredhamster.yetanotherpixeldungeon.items.rings.RingOfKnowledge;
import com.consideredhamster.yetanotherpixeldungeon.items.wands.Wand;
import com.consideredhamster.yetanotherpixeldungeon.items.weapons.enchantments.*;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.ItemSprite;
import com.consideredhamster.yetanotherpixeldungeon.visuals.ui.QuickSlot;
import com.consideredhamster.yetanotherpixeldungeon.misc.utils.GLog;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public abstract class Weapon extends EquipableItem {

	private static final int HITS_TO_KNOW	= 10;
	
	private static final String TXT_IDENTIFY		= 
		"You are now familiar enough with your %s to identify it. It is %s.";
//	private static final String TXT_INCOMPATIBLE	=
//		"Interaction of different types of magic has negated the enchantment on this weapon!";
	private static final String TXT_TO_STRING		= "%s :%d";

//    private static final String TXT_BROKEN	= "Your %s has broken!";
	
	public int	tier = 1;

//	public int		STR	= 10;
//	public float	ACU	= 1;
//	public float	DLY	= 1f;

    public enum Type {
        M_SWORD, M_BLUNT, M_POLEARM,
        R_MISSILE, R_FLINTLOCK, UNSPECIFIED
    }
	
	private int hitsToKnow = Random.IntRange(HITS_TO_KNOW, HITS_TO_KNOW * 2);
	
	public Enchantment enchantment;

//    public Weapon() {
//        defaultAction = AC_THROW;
//    }


//    private static final String TXT_EQUIP	= "you equip %s";
//    private static final String TXT_UNEQUIP	= "you unequip %s";

//    public int		MIN	= 0;
//    public int		MAX = 1;

//    @Override
//    public ArrayList<String> actions( Hero hero ) {
//        ArrayList<String> actions = super.actions( hero );
//        actions.add( isEquipped( hero ) ? AC_UNEQUIP : AC_EQUIP );
//        return actions;
//    }

    public void doEquipCarefully( Hero hero ) {


        if(!(this instanceof MeleeWeaponLightOH) && hero.belongings.weap2!=null && this.incompatibleWith(hero.belongings.weap2) ) {

            final Hero heroFinal = hero;

            GameScene.show(
                    new WndOptions( TXT_ITEM_IS_INCOMPATIBLE, TXT_R_U_SURE_INCOMPATIBLE, TXT_YES, TXT_NO ) {

                        @Override
                        protected void onSelect(int index) {
                            if (index == 0) {
                                Weapon.super.doEquipCarefully( heroFinal );
                            }
                        }
                    }
            );

        }else
            super.doEquipCarefully( hero );
    }

    @Override
    public boolean isEquipped( Hero hero ) {
        return hero.belongings.weap1 == this || hero.belongings.weap2 == this;
    }

    @Override
    public boolean doEquip( Hero hero ) {

        if( !this.isEquipped( hero ) ) {

            detachAll(hero.belongings.backpack);

            if( QuickSlot.quickslot1.value == this && ( hero.belongings.weap1 == null || hero.belongings.weap1.bonus >= 0 ) )
                QuickSlot.quickslot1.value = hero.belongings.weap1 != null && hero.belongings.weap1.stackable ? hero.belongings.weap1.getClass() : hero.belongings.weap1 ;

            if( QuickSlot.quickslot2.value == this && ( hero.belongings.weap1 == null || hero.belongings.weap1.bonus >= 0 ) )
                QuickSlot.quickslot2.value = hero.belongings.weap1 != null && hero.belongings.weap1.stackable ? hero.belongings.weap1.getClass() : hero.belongings.weap1 ;

            if( QuickSlot.quickslot3.value == this && ( hero.belongings.weap1 == null || hero.belongings.weap1.bonus >= 0 ) )
                QuickSlot.quickslot3.value = hero.belongings.weap1 != null && hero.belongings.weap1.stackable ? hero.belongings.weap1.getClass() : hero.belongings.weap1 ;

            if ( ( hero.belongings.weap1 == null || hero.belongings.weap1.doUnequip(hero, true, false) ) &&
                    ( bonus >= 0 || isCursedKnown() || !detectCursed( this, hero ) ) ) {

                hero.belongings.weap1 = this;
                activate(hero);

                onEquip( hero );

                QuickSlot.refresh();

                hero.spendAndNext(time2equip(hero));
                return true;

            } else {

                QuickSlot.refresh();
                hero.spendAndNext(time2equip(hero) * 0.5f);

                if ( !collect( hero.belongings.backpack ) ) {
                    Dungeon.level.drop( this, hero.pos ).sprite.drop();
                }

                return false;

            }
        } else {

            GLog.w(TXT_ISEQUIPPED, name());
            return false;

        }
    }

    @Override
    public boolean doUnequip( Hero hero, boolean collect, boolean single ) {
        if (super.doUnequip( hero, collect, single )) {

            hero.belongings.weap1 = ( hero.belongings.weap1 == this ? null : hero.belongings.weap1 );

            hero.belongings.weap2 = ( hero.belongings.weap2 == this ? null : hero.belongings.weap2 );

            QuickSlot.refresh();

            return true;

        } else {

            return false;

        }
    }

    public void proc( Char attacker, Char defender, int damage ) {

		if ( enchantment != null ) {
			if( enchantment.proc( this, attacker, defender, damage ) && !isEnchantKnown() ) {
                identify(ENCHANT_KNOWN);
            }
		}
		
		if (!isIdentified()) {

            float effect = attacker.ringBuffs(RingOfKnowledge.Knowledge.class);

            hitsToKnow -= (int)effect ;
            hitsToKnow -= Random.Float() < effect % 1 ? 1 : 0 ;

			if (hitsToKnow <= 0) {
				identify();
				GLog.i( TXT_IDENTIFY, name(), toString() );
//				Badges.validateItemLevelAcquired(this);
			}
		}
	}
	
	private static final String UNFAMILIRIARITY	= "unfamiliarity";
	private static final String ENCHANTMENT		= "enchantment";
	private static final String IMBUE			= "imbue";
	
	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle( bundle );
		bundle.put( UNFAMILIRIARITY, hitsToKnow );
		bundle.put( ENCHANTMENT, enchantment );
	}
	
	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle( bundle );
		if ((hitsToKnow = bundle.getInt( UNFAMILIRIARITY )) == 0) {
			hitsToKnow = HITS_TO_KNOW;
		}
		enchant( (Enchantment) bundle.get( ENCHANTMENT ) );
	}

	public int damageRoll( Hero hero ) {

		int dmg = Math.max( 0, Random.NormalIntRange( min(), max() ) );


        int exStr = hero.STR() - strShown( true );

        if (exStr > 0) {
            dmg += Random.IntRange( 0, exStr );
        }

        if( enchantment instanceof Heroic) {
            dmg += bonus >= 0
                    ? dmg * (hero.HT - hero.HP) * (bonus + 1) / hero.HT / 8
                    : dmg * (hero.HT - hero.HP) * (bonus) / hero.HT / 6;
        }

        return dmg;

	}

    public boolean canBackstab() {
        return false;
    }

    @Override
    public boolean disarmable() {
        return super.disarmable() && !(enchantment instanceof Heroic);
    }

    public int min( int bonus ) {
        return 0;
    }

    public int max( int bonus ) {
        return 0;
    }

    public int min() {
        return min(bonus);
    }

    public int max() {
        return max(bonus);
    }

    @Override
    public Item uncurse() {

        if(bonus == -1) {
            enchant(null);
        }

        return super.uncurse();
    }

    @Override
    public int lootChapter() {
        return tier;
    }

    @Override
    public int lootLevel() {
        return ( lootChapter() - 1 ) * 6 + state * 2 + bonus * 2 + ( isEnchanted() ? 3 + bonus : 0 );
    }

    public float breakingRateWhenShot() {
        return 0f;
    }

    @Override
    public int priceModifier() { return 3; }

	@Override
	public String name() {
        return enchantment != null && isEnchantKnown() ? enchantment.name( this ) : super.name();
    }

    public String simpleName() {
        return super.name();
    }

    public Type weaponType() { return Type.UNSPECIFIED; }
	
	public Weapon enchant( Enchantment ench ) {

        enchantment = ench;

		return this;
	}
	
	public Weapon enchant() {
		
		Class<? extends Enchantment> oldEnchantment = enchantment != null ? enchantment.getClass() : null;
		Enchantment ench = Enchantment.random();
		while (ench.getClass() == oldEnchantment) {
			ench = Enchantment.random();
		}
		
		return enchant( ench );
	}
	
	public boolean isEnchanted() {
		return enchantment != null;
	}
	
	@Override
	public ItemSprite.Glowing glowing() {
		return enchantment != null && isEnchantKnown() ? enchantment.glowing() : null;
	}

//    @Override
//    protected void onThrow( int cell ) {
//        Char enemy = Actor.findChar(cell);
//
////        Invisibility.dispel();
//
//        if (!throwEquipped || enemy == curUser || enemy == null) {
//            super.onThrow( cell );
//        } else {
//
//            if( this instanceof ThrowingWeapon ) {
//
//                if (!curUser.shoot(enemy, this) || Random.Int( 60 ) > ((ThrowingWeapon)this).baseAmount() ) {
//
//                    if (this instanceof Chakrams || this instanceof Boomerangs || this instanceof Harpoons) {
//
//                        ((MissileSprite)curUser.sprite.parent.recycle( MissileSprite.class )).
//                                reset(cell, curUser.pos, curItem, null);
//
//                        curUser.belongings.weap2 = this;
////                    curUser.spend( -TIME_TO_EQUIP );
//
////                        QuickSlot.refresh();
//
//                    } else {
//                        super.onThrow(cell);
//                    }
//
//                } else {
//
//                    if (quantity == 1) {
//
//                        doUnequip( curUser, false, false );
//
//                    } else {
//
//                        detach( null );
//
//                    }
//                }
//
//            } else {
//
//                curUser.shoot(enemy, this);
//
//                super.onThrow(cell);
//            }
//        }
//
//        QuickSlot.refresh();
//    }

	public static abstract class Enchantment implements Bundlable {

        private static final Class<?>[] enchants = new Class<?>[]{
            Blazing.class, Shocking.class, Freezing.class, Caustic.class,  Heroic.class,
            Arcane.class, Tempered.class, Vampiric.class, Ethereal.class, Vorpal.class
        };

		private static final float[] chances = new float[]{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 };

        protected static ItemSprite.Glowing ORANGE = new ItemSprite.Glowing( 0xFF5511 );
        protected static ItemSprite.Glowing WHITE = new ItemSprite.Glowing( 0xFFFFFF );
        protected static ItemSprite.Glowing GREEN = new ItemSprite.Glowing( 0x009900 );
        protected static ItemSprite.Glowing MUSTARD = new ItemSprite.Glowing( 0xBBBB33 );
        protected static ItemSprite.Glowing CYAN = new ItemSprite.Glowing( 0x00AAFF );
        protected static ItemSprite.Glowing GRAY = new ItemSprite.Glowing( 0x888888 );
        protected static ItemSprite.Glowing RED = new ItemSprite.Glowing( 0xCC0000 );
        protected static ItemSprite.Glowing BLUE = new ItemSprite.Glowing( 0x2244FF );
        protected static ItemSprite.Glowing BLACK = new ItemSprite.Glowing( 0x000000 );
        protected static ItemSprite.Glowing PURPLE = new ItemSprite.Glowing( 0xAA00AA );
        protected static ItemSprite.Glowing YELLOW = new ItemSprite.Glowing( 0xFFFF44 );

        protected abstract String name_p();
        protected abstract String name_n();
        protected abstract String desc_p();
        protected abstract String desc_n();

        protected abstract boolean proc_p( Char attacker, Char defender, int damage );
        protected abstract boolean proc_n( Char attacker, Char defender, int damage );

        public Class<? extends Wand> wandBonus() {
            return null;
        }

        public static boolean procced( int bonus ) {

            return Random.Float() < ( 0.1f + 0.1f * ( 1 + Math.abs( bonus ) )* ( bonus >= 0 ?
                Dungeon.hero.ringBuffs( RingOfMysticism.Mysticism.class ) : 1.0f ) );

        }

        public boolean proc( Weapon weapon, Char attacker, Char defender, int damage ) {

            boolean result = procced( weapon.bonus )
                && ( weapon.bonus >= 0
                    ? proc_p(attacker, defender, damage)
                    : proc_n(attacker, defender, damage)
                );

            if( result ) {
                weapon.identify( ENCHANT_KNOWN );
            }

            return result;
        }

        public String name( Weapon weapon ) {
            return String.format( weapon.bonus >= 0 ? name_p() : name_n(), weapon.name );
        }

        public String desc( Weapon weapon ) {
            return weapon.bonus >= 0 ? desc_p() : desc_n();
        }

		@Override
		public void restoreFromBundle( Bundle bundle ) {
		}

		@Override
		public void storeInBundle( Bundle bundle ) {
		}

		public ItemSprite.Glowing glowing() {
			return ItemSprite.Glowing.WHITE;
		}

		@SuppressWarnings("unchecked")
		public static Enchantment random() {
			try {
				return ((Class<Enchantment>)enchants[ Random.chances( chances ) ]).newInstance();
			} catch (Exception e) {
				return null;
			}
		}
	}

    public String descType() {
        return "";
    }

    @Override
    public String info() {

        final String p = "\n\n";
        final String s = " ";

        int heroStr = Dungeon.hero.STR();
        int itemStr = strShown( isIdentified() );
        float penalty = GameMath.gate( 0, penaltyBase(Dungeon.hero, strShown(isIdentified())) -
                ( isEnchantKnown() && enchantment instanceof Ethereal ? bonus : 0 ), 20 ) * 2.5f;
//        float power = Math.max(0, isIdentified() ? (float)(min() + max()) / 2 : ((float)(min(0) + max(0)) / 2) );

        StringBuilder info = new StringBuilder( desc() );

//        if( !descType().isEmpty() ) {
//
//            info.append( p );
//
//            info.append( descType() );
//        }

        info.append( p );

        if (isIdentified()) {
            info.append( "这个_" + tier + "阶" + ( !descType().isEmpty() ? descType() + "" : "" )  + "武器_需要_" + itemStr + "点力量_才能发挥其原有效能" +
                    ( isRepairable() ? "，以其_" + stateToString( state ) + "的状态_，" : "" ) +
                    "每次攻击能够造成_" + min() + "-" + max() + "点伤害_。");

            info.append( p );

            if (itemStr > heroStr) {
                info.append(
                        "因为你的力量不足，装备该武器将导致你的潜行和命中_降低" + penalty + "%_的同时减少_" + (int)(100 - 10000 / (100 + penalty)) + "%的攻击速度。" );
            } else if (itemStr < heroStr) {
                info.append(
                        "因为你的强健体格，装备该武器" + ( penalty > 0 ? "_将仅导致你的潜行和命中降低" + penalty + "%_" : "_不会受到惩罚_" ) +
                                "，并且攻击时额外造成_0-" + (heroStr - itemStr) + "点伤害_。" );
            } else {
                info.append(
                        "装备该武器" + ( penalty > 0 ? "将导致你的潜行和命中_降低" + penalty + "%_，" +
                                "不过超出需求的力量值可以降低该惩罚" : "_不会受到惩罚_" ) + "。" );
            }
        } else {
            info.append( "通常这个_" + tier + "阶" + ( !descType().isEmpty() ? descType() + "" : "" )  + "武器_需要_" + itemStr + "点力量_才能发挥其原有效能" +
                    ( isRepairable() ? "，以其_" + stateToString( state ) + "的状态_，" : "" ) +
                    "每次攻击应该能够造成_" + min() + "-" + max() + "点伤害_。");

            info.append( p );

            if (itemStr > heroStr) {
                info.append(
                        "因为你的力量不足，装备该武器应该会导致你的潜行和命中_降低" + penalty + "%_的同时减少_" + (int)(100 - 10000 / (100 + penalty)) + "%的攻击速度。" );
            } else if (itemStr < heroStr) {
                info.append(
                        "因为你的强健体格，装备该武器应该" + ( penalty > 0 ? "_会仅导致你的潜行和命中降低" + penalty + "%_" : "_不会受到惩罚_" ) +
                                "，并且攻击时额外造成_0-" + (heroStr - itemStr) + "点伤害_。" );
            } else {
                info.append(
                        "装备该武器应该" + ( penalty > 0 ? "会导致你的潜行和命中_降低" + penalty + "%_，" +
                                "不过超出需求的力量值可以降低该惩罚" : "_不会受到惩罚_" ) + "。" );
            }
        }

        info.append( p );

        if (isEquipped( Dungeon.hero )) {

            info.append("你正手持着" + name + "。");

        } else if( Dungeon.hero.belongings.backpack.items.contains(this) ) {

            info.append( "这件" + name + "正装在你的背包里。" );

        } else {

            info.append( "这件" + name + "在地面上。" );

        }

        info.append( s );
        if( isIdentified() && bonus > 0 ) {
            info.append( "它看起来被_强化_过。" );
        } else if( isCursedKnown() ) {
            info.append( bonus >= 0 ? "它看起来_没有受到诅咒_的影响。" :
                    "恶毒的_诅咒_似乎隐藏在这件" + name +"之下。" );
        } else {
            info.append( "这件" + name + "尚_未被鉴定_。" );
        }

        info.append( s );

        if( isEnchantKnown() && enchantment != null ) {
            info.append( " " + ( isIdentified() && bonus != 0 ? "同时" : "不过" ) +
                    "，它携带着_" + enchantment.desc(this) + "附魔_。" );
        }

        info.append( "这是一件稀有度为_" + lootChapterAsString() +"_的武器。" );

        return info.toString();
    }
}
