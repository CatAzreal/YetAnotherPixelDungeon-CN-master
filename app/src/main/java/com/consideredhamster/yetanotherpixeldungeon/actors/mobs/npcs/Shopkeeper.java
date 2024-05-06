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
package com.consideredhamster.yetanotherpixeldungeon.actors.mobs.npcs;

import com.consideredhamster.yetanotherpixeldungeon.Journal;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.debuffs.Debuff;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;
import com.consideredhamster.yetanotherpixeldungeon.visuals.Assets;
import com.consideredhamster.yetanotherpixeldungeon.Element;
import com.consideredhamster.yetanotherpixeldungeon.Dungeon;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.Buff;
import com.consideredhamster.yetanotherpixeldungeon.actors.mobs.Mob;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.CellEmitter;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.Speck;
import com.consideredhamster.yetanotherpixeldungeon.items.Heap;
import com.consideredhamster.yetanotherpixeldungeon.items.Item;
import com.consideredhamster.yetanotherpixeldungeon.scenes.GameScene;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.ShopkeeperHumanSprite;
import com.consideredhamster.yetanotherpixeldungeon.misc.utils.Utils;
import com.consideredhamster.yetanotherpixeldungeon.visuals.windows.WndBag;
import com.consideredhamster.yetanotherpixeldungeon.visuals.windows.WndTradeItem;

public class Shopkeeper extends NPC {

    private static final String TXT_GREETINGS = "你好啊！要来看看我的商品吗？";

    private static String[][] LINES_THREATENED = {

            {
                    "嘿，停下。",
                    "别这么做。",
                    "我们店里禁止这种行为。",
                    "请停止你的行为。",
                    "嗄！快停下！",
            },
            {
                    "再让我看见我就叫人来了！",
                    "快停下！我要叫人了！",
                    "我建议你不要惹我。",
            },
            {
                    "守卫！守卫！",
                    "来人啊，抢劫啦！",
                    "杀人啦！救命啊！",
            },
            {
                    "算球，我要走了。",
                    "够了，我不干了。",
                    "你为什么就不能让我安生一会！",
            },
    };

    private static String[][] LINES_CAUGHT = {

            {
                    "不要乱碰！No touching!",
                    "不想买的话请不要碰它。",
                    "只许看，不准碰。",
                    "如果对商品有什么疑问请过来问我，不要碰它们。",
                    "你刚刚往背包里放了个什么东西？",
            },
            {
                    "嘿，把它放回去！",
                    "嗯...你真打算从我这偷东西？",
                    "我刚是不是看见你拿了个什么东西？",
                    "我看见了！你在行窃！",
            },
            {
                    "你老妈看见了会怎么想？?",
                    "有贼！有贼！我全都看见了！",
                    "小兔崽子，搁这碰运气呢？",
                    "给我站住你这个行窃败类！",
            },
            {
                    "算了，走了，我受够了。",
                    "你这种窃贼迟早会遭报应的，再见。",
                    "这一代冒险者...世风日下啊...",
            },
    };



    private static String[][] LINES_STOLEN = {

            {
                    "哈？",
                    "嗯...",
                    "奇怪...",
            },
            {
                    "有什么不太对劲...",
                    "嗯...刚才放这的东西哪去了？",
                    "怪了事了。",
            },
            {
                    "这事太邪门了。",
                    "我卖的道具绝对不止这点。",
                    "又在这鬼鬼祟祟，我盯着你呢。",
            },
            {
                    "不会再让你偷下去了。",
                    "把自己当小天才了？那就祝你玩得开心！",
                    "我不会再容忍你的偷窃行为了。",
            },
    };

    private int threatened = 0;
    private boolean seenBefore = false;

	{
		name = "店主";
		spriteClass = ShopkeeperHumanSprite.class;
	}


	
	@Override
	protected boolean act() {

        if( noticed ) {

            noticed = false;

        }

        if (!seenBefore && Dungeon.visible[pos]) {
            Journal.add( Journal.Feature.SHOP );
            seenBefore = true;
            greetings();
        }

		throwItem();
		
		sprite.turnTo( pos, Dungeon.hero.pos );
		spend( TICK );
		return true;
	}
	
	@Override
    public void damage( int dmg, Object src, Element type ) {
        onAssault();
	}

	@Override
    public boolean add( Buff buff ) {
        if( buff instanceof Debuff ) {
            onAssault();
        }

        return false;
    }

    protected void greetings() {
        yell(Utils.format(TXT_GREETINGS));
    }

    protected void onAssault() {

        if( threatened < LINES_THREATENED.length ) {
            yell( LINES_THREATENED[threatened][Random.Int( LINES_THREATENED[threatened].length)]);
        }

        if( threatened >= 3 ) {
            runAway();
        } else if( threatened >= 2 ) {
            callForHelp();
        }

        threatened++;
    }

    public void onStealing() {

        if( threatened < LINES_STOLEN.length ) {
            yell( LINES_STOLEN[threatened][Random.Int( LINES_STOLEN[threatened].length)]);
        }

        if( threatened >= 3 ) {
            runAway();
//        } else if( threatened >= 2 ) {
//            callForHelp();
        }

        threatened++;
    }

    public void onCaught() {

        if( threatened < LINES_CAUGHT.length ) {
            yell( LINES_CAUGHT[threatened][Random.Int( LINES_CAUGHT[threatened].length)]);
        }

        if( threatened >= 3 ) {
            runAway();
        } else if( threatened >= 2 ) {
            callForHelp();
        }

        threatened++;
    }

    protected void callForHelp() {

        for (Mob mob : Dungeon.level.mobs) {
            if (mob.pos != pos) {
                mob.beckon( pos );
            }
        }

        if (Dungeon.visible[pos]) {
            CellEmitter.center( pos ).start( Speck.factory(Speck.SCREAM), 0.3f, 3 );
        }

        Sample.INSTANCE.play( Assets.SND_CHALLENGE );
    }

    protected void runAway() {

        for (Heap heap : Dungeon.level.heaps.values()) {
            if (heap.type == Heap.Type.FOR_SALE) {

                CellEmitter.get(heap.pos).burst( Speck.factory( Speck.WOOL ), 5 );
                heap.destroy();

            }
        }

		destroy();
		sprite.killAndErase();
		
		Journal.remove( Journal.Feature.SHOP );
		CellEmitter.get( pos ).burst(Speck.factory(Speck.WOOL), 10);
	}
	
	@Override
	public boolean reset() {
		return true;
	}
	
	@Override
	public String description() {
		return 
			"这个矮胖的家伙看起来更适合在某些大城市里做买卖而不是这种地牢黑市。不过对你来说算是个好消息。";
	}
	
	public float stealingChance( Item item  ) {

        float baseChance = item.stealingDifficulty() * ( threatened + 1 );

        return Math.max( 0.0f, 1.0f - baseChance / Dungeon.hero.stealth() );

    }

	public static WndBag sell() {
		return GameScene.selectItem( itemSelector, WndBag.Mode.FOR_SALE, "选择一件要出售的物品" );
	}
	
	private static WndBag.Listener itemSelector = new WndBag.Listener() {
		@Override
		public void onSelect( Item item ) {
			if (item != null) {
				WndBag parentWnd = sell();
				GameScene.show( new WndTradeItem( item, parentWnd ) );
			}
		}
	};

	@Override
	public void interact() {
		sell();
	}

    private static final String SEENBEFORE		= "seenbefore";
    private static final String THREATENED		= "threatened";

    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle(bundle);
        bundle.put( SEENBEFORE, seenBefore );
        bundle.put( THREATENED, threatened );
    }

    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle(bundle);
        seenBefore = bundle.getBoolean( SEENBEFORE );
        threatened = bundle.getInt( THREATENED );
    }
}
