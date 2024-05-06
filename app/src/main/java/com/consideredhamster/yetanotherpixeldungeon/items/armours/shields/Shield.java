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
package com.consideredhamster.yetanotherpixeldungeon.items.armours.shields;

import com.consideredhamster.yetanotherpixeldungeon.actors.Actor;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.Buff;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.special.Guard;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.special.Satiety;
import com.consideredhamster.yetanotherpixeldungeon.scenes.GameScene;
import com.consideredhamster.yetanotherpixeldungeon.visuals.windows.WndOptions;
import com.watabou.utils.GameMath;
import com.consideredhamster.yetanotherpixeldungeon.Dungeon;
import com.consideredhamster.yetanotherpixeldungeon.actors.hero.Hero;
import com.consideredhamster.yetanotherpixeldungeon.items.EquipableItem;
import com.consideredhamster.yetanotherpixeldungeon.items.armours.Armour;
import com.consideredhamster.yetanotherpixeldungeon.items.armours.glyphs.Durability;
import com.consideredhamster.yetanotherpixeldungeon.items.armours.glyphs.Featherfall;
import com.consideredhamster.yetanotherpixeldungeon.items.weapons.melee.MeleeWeaponHeavyTH;
import com.consideredhamster.yetanotherpixeldungeon.visuals.ui.QuickSlot;
import com.consideredhamster.yetanotherpixeldungeon.misc.utils.GLog;

public abstract class Shield extends Armour {


    public Shield(int tier) {

        super(tier);

    }

    private static final String TXT_NOTEQUIPPED = "你需要先装备盾牌以举盾。";
    private static final String TXT_GUARD = "guard";

    private static final String AC_GUARD = "举盾";

    @Override
    public String equipAction() {
        return AC_GUARD;
    }

    @Override
    public String quickAction() {
        return isEquipped( Dungeon.hero ) ? AC_UNEQUIP : AC_EQUIP;
    }

//    @Override
//    public ArrayList<String> actions( Hero hero ) {
//        ArrayList<String> actions = super.actions( hero );
//        actions.add( AC_GUARD );
//        return actions;
//    }

    @Override
    public void execute( Hero hero, String action ) {
        if (action == AC_GUARD) {

            if (!isEquipped(hero)) {

                GLog.n(TXT_NOTEQUIPPED);

            }  else {

                hero.buff( Satiety.class ).decrease( Satiety.POINT * str() / hero.STR() );
                Buff.affect(hero, Guard.class ).reset( 1 );
                hero.spendAndNext( Actor.TICK );

            }

        } else {

            super.execute( hero, action );

        }
    }

    public void doEquipCarefully( Hero hero ) {


        if( hero.belongings.weap1!=null && this.incompatibleWith(hero.belongings.weap1) ) {

            final Hero heroFinal = hero;

            GameScene.show(
                    new WndOptions( TXT_ITEM_IS_INCOMPATIBLE, TXT_R_U_SURE_INCOMPATIBLE, TXT_YES, TXT_NO ) {

                        @Override
                        protected void onSelect(int index) {
                            if (index == 0) {
                                Shield.super.doEquipCarefully( heroFinal );
                            }
                        }
                    }
            );

        }else
            super.doEquipCarefully( hero );
    }
	
	@Override
	public boolean doEquip( Hero hero ) {
		
		detach(hero.belongings.backpack);

        if( QuickSlot.quickslot1.value == this && ( hero.belongings.weap2 == null || hero.belongings.weap2.bonus >= 0 ) )
            QuickSlot.quickslot1.value = hero.belongings.weap2 != null && hero.belongings.weap2.stackable ? hero.belongings.weap2.getClass() : hero.belongings.weap2 ;

        if( QuickSlot.quickslot2.value == this && ( hero.belongings.weap2 == null || hero.belongings.weap2.bonus >= 0 ) )
            QuickSlot.quickslot2.value = hero.belongings.weap2 != null && hero.belongings.weap2.stackable ? hero.belongings.weap2.getClass() : hero.belongings.weap2 ;

        if( QuickSlot.quickslot3.value == this && ( hero.belongings.weap2 == null || hero.belongings.weap2.bonus >= 0 ) )
            QuickSlot.quickslot3.value = hero.belongings.weap2 != null && hero.belongings.weap2.stackable ? hero.belongings.weap2.getClass() : hero.belongings.weap2 ;

        if ( ( hero.belongings.weap2 == null || hero.belongings.weap2.doUnequip( hero, true, false ) ) &&
                ( bonus >= 0 || isCursedKnown() || !detectCursed( this, hero ) ) ) {

			hero.belongings.weap2 = this;

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
	}
	
	@Override
	public boolean doUnequip( Hero hero, boolean collect, boolean single ) {
		if (super.doUnequip( hero, collect, single )) {
			
			hero.belongings.weap2 = null;
            QuickSlot.refresh();

			return true;
			
		} else {
			
			return false;
			
		}
	}

    @Override
    public boolean isEquipped( Hero hero ) {
        return hero.belongings.weap2 == this;
    }

    @Override
	public int maxDurability() {
		return 100 ;
	}

    @Override
    public int dr( int bonus ) {
        return 5 - tier + tier * state
                + ( glyph instanceof Durability || bonus >= 0 ? tier * bonus : 0 )
                + ( glyph instanceof Durability && bonus >= 0 ? 2 + bonus : 0 ) ;
    }

    @Override
    public int penaltyBase(Hero hero, int str) {
        return super.penaltyBase(hero, str) + tier * 8 - 8 ;
    }

    @Override
    public int str( int bonus ) {
        return 6 + tier * 4 - bonus * ( glyph instanceof Featherfall ? 2 : 1 );
    }

    @Override
    public int strShown( boolean identified ) {
        return super.strShown( identified ) + (
                this == Dungeon.hero.belongings.weap2 && incompatibleWith( Dungeon.hero.belongings.weap1 ) ?
                        Dungeon.hero.belongings.weap1.str(
                                Dungeon.hero.belongings.weap1.isIdentified() ?
                                        Dungeon.hero.belongings.weap1.bonus : 0
                        ) : 0 );
    }

    @Override
    public boolean incompatibleWith( EquipableItem item ) { return item instanceof MeleeWeaponHeavyTH; }
	
	@Override
	public String info() {

        final String p = "\n\n";
        final String s = " ";

        int heroStr = Dungeon.hero.STR();
        int itemStr = strShown( isIdentified() );
        float penalty = GameMath.gate(0, penaltyBase(Dungeon.hero, strShown(isIdentified())), 20) * 2.5f;
        float armor = Math.max(0, isIdentified() ? dr() : dr(0) );

        StringBuilder info = new StringBuilder( desc() );

//        if( !descType().isEmpty() ) {
//
//            info.append( p );
//
//            info.append( descType() );
//        }

        info.append( p );

        if (isIdentified()) {
            info.append( "这个_" + tier + "阶盾牌_需要_" + itemStr + "点力量_才能发挥其原有效能" + ( isRepairable() ? "，以其_" + stateToString( state ) + "的状态_，" : "" ) +
                    "有概率为你提供_" + armor + "点护甲等级加成_。");

            info.append( p );


            if (itemStr > heroStr) {
                info.append(
                        "因为你的力量不足，装备该盾牌将导致你的潜行和敏捷_降低" + penalty + "%_的同时减少_" + (int)(100 - 10000 / (100 + penalty)) + "%的移动速度_。" );
            } else if (itemStr < heroStr) {
                info.append(
                        "因为你的强健体格，装备该盾牌" + ( penalty > 0 ? "_将仅导致你的潜行和敏捷降低" + penalty + "%_" : "_不会受到惩罚_" ) +
                                "，并且获得额外的_" + ((float)(heroStr - itemStr) / 2) + "点_护甲等级。" );
            } else {
                info.append(
                        "装备该盾牌" + ( penalty > 0 ? "将导致你的潜行和敏捷_降低" + penalty + "%_，" +
                                "不过超出需求的力量值可以降低该惩罚" : "_不会受到惩罚_" ) + "。" );
            }
        } else {
            info.append(  "通常这个_" + tier + "阶盾牌_需要_" + itemStr + "点力量_才能发挥其原有效能" +
                    ( isRepairable() ? "，以其_" + stateToString( state ) + "的状态_，" : "" ) +
                    "应该有概率为你提供_" + armor + "点护甲等级加成_。" );

            info.append( p );

            if (itemStr > heroStr) {
                info.append(
                        "因为你的力量不足，装备该盾牌大概将导致你的潜行和敏捷_降低" + penalty + "%_的同时减少_" + (int)(100 - 10000 / (100 + penalty)) + "%的移动速度_。" );
            } else if (itemStr < heroStr) {
                info.append(
                        "因为你的强健体格，装备该盾牌大概" + ( penalty > 0 ? "_会导致你的潜行和敏捷降低" + penalty + "%_" : "_不会受到惩罚_" ) +
                                "，并且获得额外的_" + ((float)(heroStr - itemStr) / 2) + "点_护甲等级。" );
            } else {
                info.append(
                        "装备该盾牌大概" + ( penalty > 0 ? "会导致你的潜行和敏捷_降低" + penalty + "%_，" +
                                "不过超出需求的力量值可以降低该惩罚" : "_不会受到惩罚_" ) + "。" );
            }
        }

        info.append( p );

        if (isEquipped( Dungeon.hero )) {

            info.append( "你正手持着" + name + "。" );

        } else if( Dungeon.hero.belongings.backpack.items.contains(this) ) {

            info.append( "这件" + name + "正放在你的背包里。" );

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

        if( isEnchantKnown() && glyph != null ) {
            info.append( "" + ( isIdentified() && bonus != 0 ? "同时" : "不过" ) + "，它携带着_" + glyph.desc(this) + "的附魔_。" );
        }

        info.append( "这是一件稀有度为_" + lootChapterAsString() +"_的盾牌。" );

        return info.toString();

	}

    @Override
    public int lootChapter() {
        return super.lootChapter() + 1;
    }
	
	@Override
	public int price() {
		int price = 15 + state * 5;

        price *= lootChapter();

        if ( isIdentified() ) {
            price += bonus > 0 ? price * bonus / 3 : price * bonus / 6 ;
        } else if( isCursedKnown() && bonus >= 0 ) {
            price -= price / 4;
        } else {
            price /= 2;
        }

        if( glyph != null && isEnchantKnown() ) {
            price += price / 4;
        }

		return price;
	}
}
