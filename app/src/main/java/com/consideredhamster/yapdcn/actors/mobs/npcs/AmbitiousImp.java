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

import com.consideredhamster.yapdcn.Element;
import com.consideredhamster.yapdcn.Dungeon;
import com.consideredhamster.yapdcn.Journal;
import com.consideredhamster.yapdcn.actors.Actor;
import com.consideredhamster.yapdcn.actors.buffs.Buff;
import com.consideredhamster.yapdcn.actors.hero.HeroClass;
import com.consideredhamster.yapdcn.actors.mobs.Golem;
import com.consideredhamster.yapdcn.actors.mobs.Mob;
import com.consideredhamster.yapdcn.actors.mobs.DwarfMonk;
import com.consideredhamster.yapdcn.visuals.effects.particles.ElmoParticle;
import com.consideredhamster.yapdcn.items.quest.DwarfToken;
import com.consideredhamster.yapdcn.items.rings.Ring;
import com.consideredhamster.yapdcn.items.rings.RingOfAccuracy;
import com.consideredhamster.yapdcn.items.rings.RingOfDurability;
import com.consideredhamster.yapdcn.items.rings.RingOfMysticism;
import com.consideredhamster.yapdcn.items.rings.RingOfEvasion;
import com.consideredhamster.yapdcn.items.rings.RingOfFortune;
import com.consideredhamster.yapdcn.items.rings.RingOfAwareness;
import com.consideredhamster.yapdcn.items.rings.RingOfKnowledge;
import com.consideredhamster.yapdcn.items.rings.RingOfProtection;
import com.consideredhamster.yapdcn.items.rings.RingOfVitality;
import com.consideredhamster.yapdcn.items.rings.RingOfSatiety;
import com.consideredhamster.yapdcn.items.rings.RingOfShadows;
import com.consideredhamster.yapdcn.items.rings.RingOfWillpower;
import com.consideredhamster.yapdcn.levels.Level;
import com.consideredhamster.yapdcn.levels.Room;
import com.consideredhamster.yapdcn.scenes.GameScene;
import com.consideredhamster.yapdcn.visuals.sprites.AmbitiousImpSprite;
import com.consideredhamster.yapdcn.misc.utils.Utils;
import com.consideredhamster.yapdcn.visuals.windows.WndImp;
import com.consideredhamster.yapdcn.visuals.windows.WndQuest;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class AmbitiousImp extends NPC {

	{
		name = "雄心勃勃的小恶魔";
		spriteClass = AmbitiousImpSprite.class;
	}
	
	private static final String TXT_GOLEMS1	=
			"你是个冒险家吗？我爱冒险家！如果有什么东西需要被解决的话，他们永远都能把活干好。我说的对吗？当然是在有赏金的前提下；)\n" +
					"总之，我需要你杀一些_魔像_。你看，我要在这里开始搞点小本生意，但这些愚蠢的傀儡只会毁掉我的生意！跟这些发着光" +
					"的大个子花岗岩根本没法交流，真是该死！所以请，杀死……我想想，_6个魔像_，然后奖励就是你的了。";
	
	private static final String TXT_MONKS1	=
		"你是个冒险家吗？我爱冒险家！如果有什么东西需要被解决的话，他们永远都能把活干好。我说的对吗？当然是在有赏金的前提下  ；)\n" +
				"总之，我需要你杀一些_武僧_。你看，我要在这里开始搞点小本生意，但这些疯子不买任何东西还会吓跑顾客。所以请，杀死……我想想，_8个武僧_，然后奖励就是你的了。";
	
	private static final String TXT_GOLEMS2	=
		"你的魔像狩猎做的怎么样了？";
	
	private static final String TXT_MONKS2	=
		"喔，你还活着！我就知道你得功夫很好；) 只要别忘了拿来那些武僧的标记就好。";
	
	private static final String TXT_CYA	= "我们会再见的，%s!";
	private static final String TXT_HEY	= "喂喂喂，%s!";
	
	private boolean seenBefore = false;
	
	@Override
	protected boolean act() {
		
		if (!Quest.given && Dungeon.visible[pos]) {
			if (!seenBefore) {
				yell( Utils.format( TXT_HEY, Dungeon.hero.className() ) );
			}
			seenBefore = true;
		} else {
			seenBefore = false;
		}
		
		throwItem();
		
		return super.act();
	}
	
//	@Override
//	public int dexterity( Char enemy ) {
//		return 1000;
//	}
	
//	@Override
//	public String defenseVerb() {
//		return "evaded";
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
	public void interact() {
		
		sprite.turnTo( pos, Dungeon.hero.pos );
		if (Quest.given) {

			DwarfToken tokens = Dungeon.hero.belongings.getItem( DwarfToken.class );
			if (tokens != null && (tokens.quantity() >= 8 || (!Quest.alternative && tokens.quantity() >= 6))) {
				GameScene.show( new WndImp( this, tokens ) );
			} else {
				tell( Quest.alternative ? TXT_MONKS2 : TXT_GOLEMS2, Dungeon.hero.className() );
			}
			
		} else {
			tell( Quest.alternative ? TXT_MONKS1 : TXT_GOLEMS1 );
			Quest.given = true;
			Quest.completed = false;
			
			Journal.add( Journal.Feature.IMP );
		}
	}
	
	private void tell( String format, Object...args ) {
		GameScene.show( 
			new WndQuest( this, Utils.format( format, args ) ) );
	}
	
	public void flee() {
		
		yell(Utils.format(TXT_CYA, Dungeon.hero.className()));
		
		destroy();
//		sprite.die();

//        sprite.emitter().burst(Speck.factory(Speck.WOOL), 15);
        sprite.emitter().start(ElmoParticle.FACTORY, 0.03f, 60);
        sprite.killAndErase();
	}
	
	@Override
	public String description() {
		return 
			"小恶魔是一种低等恶魔。没有强大的力量也没有魔法天赋，这些生物仅以其残忍品性和贪婪无餍而恶名昭彰。" +
			"不过其中少数个体相当聪明且善于交际。比如这边这只，看起来就挺像回事的。";
	}
	
	public static class Quest {
		
		private static boolean alternative;
		
		private static boolean spawned;
		private static boolean given;
		private static boolean completed;
		
		public static Ring reward;
		
		public static void reset() {
			spawned = false;

			reward = null;
		}
		
		private static final String NODE		= "demon";
		
		private static final String ALTERNATIVE	= "alternative";
		private static final String SPAWNED		= "spawned";
		private static final String GIVEN		= "given";
		private static final String COMPLETED	= "completed";
		private static final String REWARD		= "reward";
		
		public static void storeInBundle( Bundle bundle ) {
			
			Bundle node = new Bundle();
			
			node.put( SPAWNED, spawned );
			
			if (spawned) {
				node.put( ALTERNATIVE, alternative );
				
				node.put( GIVEN, given );
				node.put( COMPLETED, completed );
				node.put( REWARD, reward );
			}
			
			bundle.put( NODE, node );
		}
		
		public static void restoreFromBundle( Bundle bundle ) {

			Bundle node = bundle.getBundle( NODE );
			
			if (!node.isNull() && (spawned = node.getBoolean( SPAWNED ))) {
				alternative	= node.getBoolean( ALTERNATIVE );
				
				given = node.getBoolean( GIVEN );
				completed = node.getBoolean( COMPLETED );
				reward = (Ring)node.get( REWARD );
			}
		}
		
		public static void spawn( Level level, Room room ) {

			if (!spawned && Dungeon.depth > 19 && Random.Int( 24 - Dungeon.depth ) == 0) {

				AmbitiousImp npc = new AmbitiousImp();

				do {
					npc.pos = level.randomRespawnCell();
				} while (npc.pos == -1 || level.heaps.get( npc.pos ) != null);

				level.mobs.add(npc);
				Actor.occupyCell( npc );
				
				spawned = true;	
				alternative = true;
				
				given = false;

                int random = Random.Int( 3 );

                if( Dungeon.hero.heroClass == HeroClass.WARRIOR ) {
                    reward = random == 2 ? new RingOfSatiety() : random == 1 ? new RingOfVitality() : new RingOfProtection();
                } else if( Dungeon.hero.heroClass == HeroClass.BRIGAND ) {
                    reward = random == 2 ? new RingOfFortune() : random == 1 ? new RingOfShadows() : new RingOfEvasion();
                } else if( Dungeon.hero.heroClass == HeroClass.SCHOLAR ) {
                    reward = random == 2 ? new RingOfKnowledge() : random == 1 ? new RingOfMysticism() : new RingOfWillpower();
                } else if( Dungeon.hero.heroClass == HeroClass.ACOLYTE ) {
                    reward = random == 2 ? new RingOfDurability() : random == 1 ? new RingOfAwareness() : new RingOfAccuracy();
                }

				reward.bonus = random + 1;
			}
		}
		
		public static void process( Mob mob ) {
			if (spawned && given && !completed) {
				if ((alternative && mob instanceof DwarfMonk) ||
					(!alternative && mob instanceof Golem)) {
					
					Dungeon.level.drop( new DwarfToken(), mob.pos ).sprite.drop();
				}
			}
		}
		
		public static void complete() {
			reward = null;
			completed = true;
			
			Journal.remove(Journal.Feature.IMP);
		}
		
		public static boolean isCompleted() {
			return completed;
		}
	}
}
