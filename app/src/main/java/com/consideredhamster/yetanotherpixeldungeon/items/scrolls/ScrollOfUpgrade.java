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
package com.consideredhamster.yetanotherpixeldungeon.items.scrolls;

import com.consideredhamster.yetanotherpixeldungeon.Badges;
import com.consideredhamster.yetanotherpixeldungeon.Statistics;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.Speck;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.SpellSprite;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.particles.ShadowParticle;
import com.consideredhamster.yetanotherpixeldungeon.items.Item;
import com.consideredhamster.yetanotherpixeldungeon.visuals.ui.QuickSlot;
import com.consideredhamster.yetanotherpixeldungeon.misc.utils.GLog;
import com.consideredhamster.yetanotherpixeldungeon.visuals.windows.WndBag;

public class ScrollOfUpgrade extends InventoryScroll {

    private static final String TXT_KNOWN_UPGRADED	=
            "你的%s看起来状态好多了！";
    private static final String TXT_KNOWN_REPAIRED  =
            "你的%s不能再升级了，但它看起来状态好了不少。";

	private static final String TXT_UNKNW_REPAIRED  =
            "你的%s看起来比之前应该要好了不少，不过你是不是应该先鉴定一下它？";
	private static final String TXT_UNKNW_WHOKNOWS	=
            "你的%s看起来没什么变化，你是不是应该先鉴定一下它？";

    private static final String TXT_CURSE_WEAKENED =
            "你的%s上的诅咒似乎被弱化了。";
    private static final String TXT_CURSE_DISPELLED	=
            "你的%s上的诅咒被净化了。";

	{
		name = "升级卷轴";
        shortName = "Up";

		inventoryTitle = "选择一个要升级的物品";
		mode = WndBag.Mode.UPGRADEABLE;

        spellSprite = SpellSprite.SCROLL_UPGRADE;
        spellColour = SpellSprite.COLOUR_HOLY;
	}
	
	@Override
	protected void onItemSelected( Item item ) {

        item.identify( CURSED_KNOWN );

        if( item.bonus >= 0 ) {

            if( item.isIdentified() ) {
                GLog.p( item.bonus < 3 ? TXT_KNOWN_UPGRADED : TXT_KNOWN_REPAIRED, item.name() );
            } else {
                GLog.p( item.state < 3 ? TXT_UNKNW_REPAIRED : TXT_UNKNW_WHOKNOWS, item.name() );
            }

            item.upgrade();
            curUser.sprite.emitter().start(Speck.factory(Speck.UP), 0.2f, 3);

        } else {

            item.upgrade();

            if( item.bonus < 0 ) {

                GLog.w( TXT_CURSE_WEAKENED, item.name() );
                curUser.sprite.emitter().burst(ShadowParticle.CURSE, 4);

            } else {

                GLog.p( TXT_CURSE_DISPELLED, item.name() );
                curUser.sprite.emitter().burst(ShadowParticle.CURSE, 6);

            }
        }


        item.repair(1);

        QuickSlot.refresh();

        Statistics.itemsUpgraded++;
		
		Badges.validateItemsUpgraded();
	}

	
	@Override
	public String desc() {
		return
			"这张卷轴可以升级一件物品，提升其品质。法杖的魔力及充能数会获得提升，武器和护甲可以造成或吸收" +
                    "更多的伤害，戒指的效果将会增强。也会同时降低武器和护甲的力量需求。";
	}

    @Override
    public int price() {
        return isTypeKnown() ? 125 * quantity : super.price();
    }
}
