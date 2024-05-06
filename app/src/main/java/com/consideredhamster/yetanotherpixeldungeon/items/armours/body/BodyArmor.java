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
package com.consideredhamster.yetanotherpixeldungeon.items.armours.body;

import com.consideredhamster.yetanotherpixeldungeon.visuals.ui.QuickSlot;
import com.watabou.utils.GameMath;
import com.consideredhamster.yetanotherpixeldungeon.Dungeon;
import com.consideredhamster.yetanotherpixeldungeon.actors.hero.Hero;
import com.consideredhamster.yetanotherpixeldungeon.items.armours.Armour;
import com.consideredhamster.yetanotherpixeldungeon.items.armours.glyphs.*;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.HeroSprite;

public abstract class BodyArmor extends Armour {

	public int appearance;

    public BodyArmor( int tier ) {

        super(tier);

    }
	
	@Override
	public boolean doEquip( Hero hero ) {
		
		detach(hero.belongings.backpack);
		
		if ( ( hero.belongings.armor == null || hero.belongings.armor.doUnequip( hero, true, false ) ) &&
                ( bonus >= 0 || isCursedKnown() || !detectCursed( this, hero ) ) ) {

			hero.belongings.armor = this;

            ((HeroSprite)hero.sprite).updateArmor();

            onEquip( hero );

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
			
			hero.belongings.armor = null;

			((HeroSprite)hero.sprite).updateArmor();
			
			return true;
			
		} else {
			
			return false;
			
		}
	}

    @Override
    public boolean isEquipped( Hero hero ) {
        return hero.belongings.armor == this;
    }

    @Override
	public int maxDurability() {
		return 150 ;
	}

    @Override
    protected float time2equip( Hero hero ) {
        return super.speedFactor( hero ) * 3;
    }

    @Override
    public int dr( int bonus ) {
        return tier * ( 4 + state )
                + ( glyph instanceof Durability || bonus >= 0 ? tier * bonus : 0 )
                + ( glyph instanceof Durability && bonus >= 0 ? 2 + bonus : 0 ) ;
    }

    @Override
    public int str(int bonus) {
        return 9 + tier * 4 - bonus * ( glyph instanceof Featherfall ? 2 : 1 );
    }
	
	@Override
	public String info() {

        final String p = "\n\n";
        final String s = " ";

        int heroStr = Dungeon.hero.STR();
        int itemStr = strShown( isIdentified() );
        float penalty = GameMath.gate( 0, penaltyBase(Dungeon.hero, strShown(isIdentified())), 20 ) * 2.5f;
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
            info.append( "这个_" + tier + "阶" + ( !descType().isEmpty() ? descType() + "" : "" )  + "护甲_需要_" + itemStr + "点力量_才能发挥其原有效能" +
                    ( isRepairable() ? "，以其_" + stateToString( state ) + "的状态_，" : "" ) +
                    "能够为你提供_" + armor + "点护甲等级加成_。");

            info.append( p );

            if (itemStr > heroStr) {
                info.append(
                        "因为你的力量不足，穿戴该护甲将导致你的潜行和敏捷_降低" + penalty + "%_的同时减少_" + (int)(100 - 10000 / (100 + penalty)) + "%的移动速度。" );
            } else if (itemStr < heroStr) {
                info.append(
                        "因为你的强健体格，穿戴该护甲" + ( penalty > 0 ? "_将仅导致你的潜行和敏捷降低" + penalty + "%_" : "_不会受到惩罚_" ) +
                        "，并且获得额外的_" + ((float)(heroStr - itemStr) / 2) + "点护甲等级。_" );
            } else {
                info.append(
                        "穿戴该护甲" + ( penalty > 0 ? "将导致你的潜行和敏捷_降低" + penalty + "%_，" +
                                "不过超出需求的力量值可以降低该惩罚" : "_不会受到惩罚_" ) + "。" );
            }
        } else {
            info.append(  "通常这个_" + tier + "阶" + ( !descType().isEmpty() ? descType() + "" : "" )  + "护甲_需要_" + itemStr + "点力量_才能发挥其原有效能" +
                    ( isRepairable() ? "，以其_" + stateToString( state ) + "的状态_，" : "" ) +
                    "应该能够为你提供_" + armor + "点护甲等级加成_。" );

            info.append( p );

            if (itemStr > heroStr) {
                info.append(
                        "因为你的力量不足，穿戴该护甲大概将导致你的潜行和敏捷_降低" + penalty + "%_的同时减少_" + (int)(100 - 10000 / (100 + penalty)) + "%的移动速度。" );
            } else if (itemStr < heroStr) {
                info.append(
                        "因为你的强健体格，穿戴该护甲大概" + ( penalty > 0 ? "_会导致你的潜行和敏捷降低" + penalty + "%_" : "_不会受到惩罚_" ) +
                                "，并且获得额外的_" + ((float)(heroStr - itemStr) / 2) + "点护甲等级。" );
            } else {
                info.append(
                        "穿戴该护甲大概" + ( penalty > 0 ? "会导致你的潜行和敏捷_降低" + penalty + "%_，" +
                                "不过超出需求的力量值可以降低该惩罚" : "_不会受到惩罚_" ) + "。" );
            }
        }

        info.append( p );

        if (isEquipped( Dungeon.hero )) {

            info.append( "你正装备着" + name + "。" );

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

        if( isEnchantKnown() && glyph != null ) {
            info.append( "" + ( isIdentified() && bonus != 0 ? "同时" : "不过" ) + "，它携带着_" + glyph.desc(this) + "的附魔_." );
        }

        info.append( "这是一件稀有度为_" + lootChapterAsString() +"_的护甲。" );

        return info.toString();

	}
	
	@Override
	public int price() {
		int price = 20 + state * 10;

        price *= lootChapter() + 1;

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
