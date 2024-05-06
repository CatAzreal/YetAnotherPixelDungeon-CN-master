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
package com.consideredhamster.yetanotherpixeldungeon.items.misc;

import com.consideredhamster.yetanotherpixeldungeon.Dungeon;
import com.consideredhamster.yetanotherpixeldungeon.items.Item;
import com.consideredhamster.yetanotherpixeldungeon.items.rings.RingOfDurability;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;
import com.consideredhamster.yetanotherpixeldungeon.visuals.Assets;
import com.consideredhamster.yetanotherpixeldungeon.actors.hero.Hero;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.Speck;
import com.consideredhamster.yetanotherpixeldungeon.scenes.GameScene;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.ItemSpriteSheet;
import com.consideredhamster.yetanotherpixeldungeon.misc.utils.GLog;
import com.consideredhamster.yetanotherpixeldungeon.misc.utils.Utils;
import com.consideredhamster.yetanotherpixeldungeon.visuals.windows.WndBag;

import java.util.ArrayList;

public class CraftingKit extends Item {

    private static final String TXT_SELECT_RANGED = "选择需要维护的武器。";
    private static final String TXT_REPAIR_RANGED = "你的%s的状态看起来好多了！";
    private static final String TXT_CHARGE_KEEPED = "戒指之力协助你完成了这次维护！";
    private static final String TXT_CHARGE_WASTED = "戒指之力的介入妨碍了这次维护！";

    private static final float TIME_TO_APPLY = 3f;

    private static final String AC_APPLY = "维护";

    private static final String TXT_STATUS	= "%d/%d";

    {
        name = "便携工具箱";
        image = ItemSpriteSheet.CRAFTING_KIT;
    }

    private static final String VALUE = "value";

    private int value = 3;
    private final int limit = 3;

    @Override
    public String quickAction() {
        return AC_APPLY;
    }

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle( bundle );
        bundle.put(VALUE, value);
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle(bundle);
        value = bundle.getInt(VALUE);
    }

    @Override
    public Item random() {
        value = Random.IntRange(1, 3);
        return this;
    }
	
	@Override
	public ArrayList<String> actions( Hero hero ) {
		ArrayList<String> actions = super.actions( hero );
		actions.add( AC_APPLY );
		return actions;
	}
	
	@Override
	public void execute( Hero hero, String action ) {
		if (action == AC_APPLY) {

			curUser = hero;
			curItem = this;
			GameScene.selectItem( itemSelector, WndBag.Mode.CRAFTING_KIT, TXT_SELECT_RANGED);
			
		} else {
			
			super.execute( hero, action );
			
		}
	}

    @Override
    public int price() {
        return 20 + 10 * value;
    }
	
	private void repair(Item ranged) {

        float bonus = Dungeon.hero.ringBuffsBaseZero( RingOfDurability.Durability.class ) * 0.5f;

        if( bonus > 0.0f && Random.Float() < bonus ) {
            GLog.p(TXT_CHARGE_KEEPED);
        } else {
            if( --value <= 0 ) {
                detach(curUser.belongings.backpack);
            }
        }

        if( bonus < 0.0f && Random.Float() > -bonus ) {
            GLog.n(TXT_CHARGE_WASTED);
        } else {
            ranged.repair(1);
            GLog.p(TXT_REPAIR_RANGED, ranged.name());
        }

        curUser.sprite.operate(curUser.pos);
        Sample.INSTANCE.play(Assets.SND_EVOKE);

        curUser.sprite.centerEmitter().start(Speck.factory(Speck.KIT), 0.05f, 10);
        curUser.spend( TIME_TO_APPLY );
        curUser.busy();
	}
	
	@Override
	public String info() {
        return
                "工具箱里的工具与材料虽无法修复刀枪剑戟与厚重盔甲，但可以修理很多其它装备，比如弓弩、弹弓、燧发武器与布制护甲。" +
                        "\n工具箱的材料还能进行" + ( value > 2 ? "三次" : value < 2 ? "最后一次" : "两次" ) + "修复。";
 //       return "[临时字串]可对远程武器和布甲进行"+( value > 2 ? "三次" : value < 2 ? "一次" : "两次" )+"维护";
//            "The instruments and materials in this toolkit cannot be used to repair a battle axe or a mail armor. " +
//            "However, they can be used to fix some other things, like bows, slings, flintlock weapons or cloth armors. " +
//            "Currently, it has enough materials for only " + ( value > 2 ? "three usages" : value < 2 ? "only one usage" : "two usages" ) + ".";
	}

    @Override
    public String status() {
        return Utils.format(TXT_STATUS, value, limit);
    }

    @Override
    public String toString() {
        return super.toString() + " (" + status() +  ")" ;
    }
	
	private final WndBag.Listener itemSelector = new WndBag.Listener() {
		@Override
		public void onSelect( Item item ) {
			if (item != null) {
				CraftingKit.this.repair(item);
			}
		}
	};
}
