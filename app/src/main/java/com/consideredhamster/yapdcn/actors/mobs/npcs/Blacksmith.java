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
package com.consideredhamster.yapdcn.actors.mobs.npcs;

import java.util.Collection;

import com.consideredhamster.yapdcn.visuals.effects.Speck;
import com.watabou.noosa.audio.Sample;
import com.consideredhamster.yapdcn.visuals.Assets;
import com.consideredhamster.yapdcn.Element;
import com.consideredhamster.yapdcn.Dungeon;
import com.consideredhamster.yapdcn.Journal;
import com.consideredhamster.yapdcn.actors.buffs.Buff;
import com.consideredhamster.yapdcn.actors.hero.Hero;
import com.consideredhamster.yapdcn.items.EquipableItem;
import com.consideredhamster.yapdcn.items.Item;
import com.consideredhamster.yapdcn.items.quest.DarkGold;
import com.consideredhamster.yapdcn.items.quest.Pickaxe;
import com.consideredhamster.yapdcn.levels.Room;
import com.consideredhamster.yapdcn.levels.Room.Type;
import com.consideredhamster.yapdcn.scenes.GameScene;
import com.consideredhamster.yapdcn.visuals.sprites.BlacksmithSprite;
import com.consideredhamster.yapdcn.misc.utils.GLog;
import com.consideredhamster.yapdcn.visuals.windows.WndBlacksmith;
import com.consideredhamster.yapdcn.visuals.windows.WndQuest;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class Blacksmith extends NPC {

	private static final String TXT_GOLD_1 =
		"嘿，人类！不想当个没用的废物，对吗？拿着那个镐子然后给我挖点_暗金矿，5块_就够了。什么？要我如何报答你？真贪心...\n" +
		"好吧，好吧，我没钱付给你，但我可以给你打打铁。想想你运气有多好吧，我可是这附近唯一的铁匠。";
	private static final String TXT_BLOOD_1 =
		"嘿，人类！不想当个没用的废物，对吗？拿着那个镐子然后用它_杀只蝙蝠_，我需要镐头上沾着的血。什么？要我如何报答你？真贪心...\n" +
		"好吧，好吧，我没钱付给你，但我可以给你打打铁。想想你运气有多好吧，我可是这附近唯一的铁匠。";
	private static final String TXT2 =
		"你在逗我？我的镐子呢？！";
	private static final String TXT3 =
		"_暗金矿_5块。说真的，有那么难吗？";
	private static final String TXT4 =
		"我说了我需要镐子沾上蝙蝠血。赶紧！";
	private static final String TXT_COMPLETED =
		"噢，你终于回来了… 算了，总比回不来好。";
	private static final String TXT_GET_LOST =
		"我忙着呢。滚开！";
	
	private static final String TXT_LOOKS_BETTER	= "你的%s看起来状态好多了";
	
	{
		name = "巨魔铁匠";
		spriteClass = BlacksmithSprite.class;
	}

	@Override
	protected boolean act() {
		throwItem();		
		return super.act();
	}
	
	@Override
	public void interact() {
		
		sprite.turnTo( pos, Dungeon.hero.pos );
		
		if (!Quest.given) {
			
			GameScene.show( new WndQuest( this, 
				Quest.alternative ? TXT_BLOOD_1 : TXT_GOLD_1 ) {
				
				@Override
				public void onBackPressed() {
					super.onBackPressed();
					
					Quest.given = true;
					Quest.completed = false;
					
					Pickaxe pick = new Pickaxe();
					if (pick.doPickUp( Dungeon.hero )) {
						GLog.i( Hero.TXT_YOU_NOW_HAVE, pick.name() );
					} else {
						Dungeon.level.drop( pick, Dungeon.hero.pos ).sprite.drop();
					}
				};
			} );
			
			Journal.add( Journal.Feature.TROLL );
			
		} else if (!Quest.completed) {
			if (Quest.alternative) {
				
				Pickaxe pick = Dungeon.hero.belongings.getItem( Pickaxe.class );
				if (pick == null) {
					tell( TXT2 );
				} else if (!pick.bloodStained) {
					tell( TXT4 );
				} else {
//					if (pick.isEquipped( Dungeon.hero )) {
//						pick.doUnequip( Dungeon.hero, false );
//					}
					pick.detach( Dungeon.hero.belongings.backpack );
					tell( TXT_COMPLETED );
					
					Quest.completed = true;
					Quest.reforged = false;
				}
				
			} else {
				
				Pickaxe pick = Dungeon.hero.belongings.getItem( Pickaxe.class );
				DarkGold gold = Dungeon.hero.belongings.getItem( DarkGold.class );
				if (pick == null) {
					tell( TXT2 );
				} else if (gold == null || gold.quantity() < 5) {
					tell( TXT3 );
				} else {
//					if (pick.isEquipped( Dungeon.hero )) {
//						pick.doUnequip( Dungeon.hero, false );
//					}
					pick.detach( Dungeon.hero.belongings.backpack );
					gold.detachAll( Dungeon.hero.belongings.backpack );
					tell( TXT_COMPLETED );
					
					Quest.completed = true;
					Quest.reforged = false;
				}
				
			}
		} else if (!Quest.reforged) {
			
			GameScene.show( new WndBlacksmith( this, Dungeon.hero ) );
			
		} else {
			
			tell( TXT_GET_LOST );
			
		}
	}
	
	private void tell( String text ) {
		GameScene.show( new WndQuest( this, text ) );
	}
	
	public static String verify( Item item1, Item item2 ) {
		
		if (item1 == item2) {
			return "Select 2 different items, not the same item twice!";
		}
		
		if (item1.getClass() != item2.getClass()) {
			return "Select 2 items of the same type!";
		}
		
		if (!item1.isIdentified() || !item2.isIdentified()) {
			return "I need to know what I'm working with, identify them first!";
		}

        if (item1.bonus < 0 || item2.bonus < 0) {
			return "I don't work with cursed items!";
		}
		
		if (!item1.isUpgradeable() || !item2.isUpgradeable()) {
			return "I can't upgrade this items any further.";
		}
		
		return null;
	}
	
	public static void upgrade( Item item1, Item item2 ) {
		
		Item first, second;
		if (item2.bonus > item1.bonus) {
			first = item2;
			second = item1;
		} else {
			first = item1;
			second = item2;
		}

        Dungeon.hero.sprite.emitter().start(Speck.factory(Speck.UP), 0.2f, 3);
        Sample.INSTANCE.play( Assets.SND_EVOKE );
        Item.evoke( Dungeon.hero );

		if (first.isEquipped( Dungeon.hero )) {
			((EquipableItem)first).doUnequip( Dungeon.hero, true );
		}

		first.upgrade();
        first.state = Math.min( 3, Math.max( first.state, second.state ) + 1 );
        first.fix();

		GLog.p( TXT_LOOKS_BETTER, first.name() );

		Dungeon.hero.spendAndNext( 2f );
//		Badges.validateItemLevelAcquired(first);

		if (second.isEquipped( Dungeon.hero )) {
			((EquipableItem)second).doUnequip( Dungeon.hero, false );
		}
		second.detachAll( Dungeon.hero.belongings.backpack );

		Quest.reforged = true;
		
		Journal.remove( Journal.Feature.TROLL );
	}
	
//	@Override
//	public int dexterity( Char enemy ) {
//		return 1000;
//	}
	
	@Override
	public void damage( int dmg, Object src, Element type ) {
	}
	
	@Override
	public boolean add( Buff buff ) {
        return false;
	}
	
	@Override
	public boolean reset() {
		return true;
	}
	
	@Override
	public String description() {
		return 
			"This troll blacksmith looks like all trolls look: he is tall and lean, and his skin resembles stone " +
			"in both color and texture. The troll blacksmith is tinkering with unproportionally small tools.";
	}

	public static class Quest {
		
		private static boolean spawned;
		
		private static boolean alternative;
		private static boolean given;
		private static boolean completed;
		private static boolean reforged;
		
		public static void reset() {
			spawned		= false;
			given		= false;
			completed	= false;
			reforged	= false;
		}
		
		private static final String NODE	= "blacksmith";
		
		private static final String SPAWNED		= "spawned";
		private static final String ALTERNATIVE	= "alternative";
		private static final String GIVEN		= "given";
		private static final String COMPLETED	= "completed";
		private static final String REFORGED	= "reforged";
		
		public static void storeInBundle( Bundle bundle ) {
			
			Bundle node = new Bundle();
			
			node.put( SPAWNED, spawned );
			
			if (spawned) {
				node.put( ALTERNATIVE, alternative );
				node.put( GIVEN, given );
				node.put( COMPLETED, completed );
				node.put( REFORGED, reforged );
			}
			
			bundle.put( NODE, node );
		}
		
		public static void restoreFromBundle( Bundle bundle ) {

			Bundle node = bundle.getBundle( NODE );
			
			if (!node.isNull() && (spawned = node.getBoolean( SPAWNED ))) {
				alternative	=  node.getBoolean( ALTERNATIVE );
				given = node.getBoolean( GIVEN );
				completed = node.getBoolean( COMPLETED );
				reforged = node.getBoolean( REFORGED );
			} else {
				reset();
			}
		}
		
		public static void spawn( Collection<Room> rooms ) {
			if (!spawned && Dungeon.depth > 13 && Random.Int( 18 - Dungeon.depth ) == 0) {
				
				Room blacksmith = null;
				for (Room r : rooms) {
					if (r.type == Type.STANDARD && r.width() > 3 && r.height() > 3) {
						blacksmith = r;
						blacksmith.type = Type.BLACKSMITH;
						
						spawned = true;
						alternative = false;
						
						given = false;
						
						break;
					}
				}
			}
		}

        public static boolean isCompleted() {
            return completed;
        }
	}
}
