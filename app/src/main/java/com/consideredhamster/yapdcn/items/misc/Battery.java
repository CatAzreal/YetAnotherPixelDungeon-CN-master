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
package com.consideredhamster.yapdcn.items.misc;

import com.consideredhamster.yapdcn.Dungeon;
import com.consideredhamster.yapdcn.items.Item;
import com.consideredhamster.yapdcn.items.rings.RingOfDurability;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;
import com.consideredhamster.yapdcn.visuals.Assets;
import com.consideredhamster.yapdcn.actors.hero.Hero;
import com.consideredhamster.yapdcn.visuals.effects.Speck;
import com.consideredhamster.yapdcn.items.wands.Wand;
import com.consideredhamster.yapdcn.scenes.GameScene;
import com.consideredhamster.yapdcn.visuals.sprites.ItemSpriteSheet;
import com.consideredhamster.yapdcn.misc.utils.GLog;
import com.consideredhamster.yapdcn.misc.utils.Utils;
import com.consideredhamster.yapdcn.visuals.windows.WndBag;

import java.util.ArrayList;

public class Battery extends Item {
	
    private static final String TXT_SELECT_WAND = "选择需要充能的法杖。";
    private static final String TXT_REPAIR_WAND = "你的%s的状态看起来好多了！";
    private static final String TXT_CHARGE_KEEPED = "戒指之力协助你完成了这次充能！";
    private static final String TXT_CHARGE_WASTED = "戒指之力的介入妨碍了这次充能！";

	private static final float TIME_TO_APPLY = 2f;
	
	private static final String AC_APPLY = "充能";

    private static final String TXT_STATUS	= "%d/%d";
	
	{
		name = "奥术晶石";
		image = ItemSpriteSheet.BATTERY;
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
			GameScene.selectItem( itemSelector, WndBag.Mode.ARCANE_BATTERY, TXT_SELECT_WAND);
			
		} else {
			
			super.execute( hero, action );
			
		}
	}

    @Override
    public Item random() {
        value = Random.IntRange(1, 3);
        return this;
    }
	
	private void apply( Wand wand ) {

        float bonus = Dungeon.hero.ringBuffsBaseZero( RingOfDurability.Durability.class ) * 0.5f;

        if( bonus > 0.0f && Random.Float() < bonus ) {
            GLog.p(TXT_CHARGE_KEEPED);
        } else {
            if( --value <= 0 ) {
                detach(curUser.belongings.backpack);
            }
        }

        if( bonus < 0.0f && Random.Float() < -bonus ) {
            GLog.n(TXT_CHARGE_WASTED);
        } else {
            wand.repair(1);
            GLog.p(TXT_REPAIR_WAND, wand.name());
        }
		
		curUser.sprite.operate(curUser.pos);
		Sample.INSTANCE.play(Assets.SND_LIGHTNING);

        curUser.sprite.centerEmitter().start(Speck.factory(Speck.KIT), 0.05f, 10);
		curUser.spend( TIME_TO_APPLY );
		curUser.busy();
	}

    @Override
    public int price() {
        return 20 + 10 * value;
    }
	
	@Override
	public String info() {
        return "一块材质不明的细长坚硬黑石，你可以使用它对任何法杖充能，并稍稍修复其磨损。\n这块奥术晶石还能使用"+( value > 2 ? "三次" : value < 2 ? "最后一次" : "两次" ) ;
//        return "[临时字串]可对法杖进行"+( value > 2 ? "三次" : value < 2 ? "一次" : "两次" )+"充能";
//			"This is a narrow piece of some dark, very hard stone. Using it, you can partially recharge core of any magic wand, " +
//            "restoring its condition to a better level." +
//            "\nThis battery can be used " + ( value > 2 ? "three times" : value < 2 ? "only one time" : "two times" ) + " more.";
	}

    @Override
    public String status() {
        return Utils.format( TXT_STATUS, value, limit );
    }

    @Override
    public String toString() {
        return super.toString() + " (" + status() +  ")" ;
    }
	
	private final WndBag.Listener itemSelector = new WndBag.Listener() {
		@Override
		public void onSelect( Item item ) {
			if (item != null) {
                Battery.this.apply( (Wand)item );
			}
		}
	};

}
