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
package com.consideredhamster.yetanotherpixeldungeon.scenes;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;

import com.consideredhamster.yetanotherpixeldungeon.visuals.ui.RenderedTextMultiline;
import com.watabou.input.Touchscreen;
import com.watabou.noosa.BitmapText;
import com.watabou.noosa.BitmapTextMultiline;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Game;
import com.watabou.noosa.RenderedText;
import com.watabou.noosa.TouchArea;
import com.watabou.noosa.audio.Music;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;
import com.consideredhamster.yetanotherpixeldungeon.visuals.Assets;
import com.consideredhamster.yetanotherpixeldungeon.Badges;
import com.consideredhamster.yetanotherpixeldungeon.Dungeon;
import com.consideredhamster.yetanotherpixeldungeon.Statistics;
import com.consideredhamster.yetanotherpixeldungeon.YetAnotherPixelDungeon;
import com.consideredhamster.yetanotherpixeldungeon.actors.Actor;
import com.consideredhamster.yetanotherpixeldungeon.items.Generator;
import com.consideredhamster.yetanotherpixeldungeon.levels.Level;
import com.consideredhamster.yetanotherpixeldungeon.visuals.windows.WndError;
import com.consideredhamster.yetanotherpixeldungeon.visuals.windows.WndStory;

public class InterlevelScene extends PixelScene {

	private static final float TIME_TO_FADE = 0.5f;

	private static final String TXT_DESCENDING	= "下楼中...";
	private static final String TXT_ASCENDING	= "上楼中...";
	private static final String TXT_LOADING		= "载入中...";
	private static final String TXT_RESURRECTING= "正在复活...";
	private static final String TXT_RETURNING	= "返回坐标...";
	private static final String TXT_FALLING		= "坠落中...";
	private static final String TXT_CONTINUE	= "点击以继续!";

	private static final String ERR_FILE_NOT_FOUND	= "文件缺失";
	private static final String ERR_GENERIC			= "Something went wrong..."	;

	public static enum Mode {
		DESCEND, ASCEND, CONTINUE, RESURRECT, RETURN, FALL
	};
	public static Mode mode;

	public static int returnDepth;
	public static int returnPos;

	public static boolean noStory = false;

	public static boolean fallIntoPit;

	//todo: The tips are supposed to switch every few seconds but the game seems refused to do so with renderedText
	private static final String[] TIPS = {

			// GENERAL

			"在地牢中每五层都会出现一个商店供你消费金币",
			"地牢中只有3个复活十字架，但有极低的概率找到更多",

			"陷阱与浸水宝库中更容易找到未受诅咒的道具",
			"修有陵墓或存在活化石像的房间中获取的道具不会受到诅咒",

			"感知属性影响侦察陷阱和周边隐藏门所需的时间",
			"感知属性影响你被动发现周边隐藏门的概率",
			"感知属性影响你探索或睡眠时听见周遭敌人声音的几率",
			"感知属性影响你在格挡时防反敌人的几率",

			"潜行属性影响在商店可行窃的次数和成功几率",
			"潜行属性影响你伏击敌人的几率",

			"调谐属性影响所有法杖的充能速率",
			"调谐属性影响装备物品前感知到诅咒的概率",

//            "Magic power affects effectiveness of enchanted equipment",
			"魔能属性影响法杖造成的伤害/效力",
			"力量属性影响你每回合从束缚状态中解脱的概率",

			"随着地牢的深入，掉落物和特殊房间的数量也会随之提高",
			"尽可能保证你的角色等级高于或等同于当前层数",

			"可升级物品的等级上限为3——不必担心，它们可堪大用",
			"高等级物品有着更高的耐久度，但在被诅咒时也会以更快的速度损坏",

			"别忘了，你可以在游戏设置中关闭这些提示！",
			"更多提示会随后加入，敬请期待！",

			// WEAPONS & ARMOURS

			"使用超重武器会降低你的攻击速度",
			"强力武器通常会进一步降低你的命中和潜行属性",
			"触电可以使你手中的武器强制掉落",

			"使用超重护甲或盾牌会降低你的移动速度",
			"强力护甲或盾牌通常会进一步降低你的敏捷和潜行属性",
			"升级布制护甲能够进一步提高其属性加值",

			"格挡攻击的概率取决于你持有盾牌的护甲等级或武器伤害",
			"成功格挡攻击有概率防反你的敌人，让你获得反击敌人的空当",

			"额外力量可以降低重型装备的惩罚",
			"你可以通过长时间使用武器，护甲，法杖和戒指来自然鉴定其性质",
			"睡眠时你不会承受来自装备的潜行减益",

			"强力燧发武器需要耗费更多的火药进行装填",
			"燧发武器无视攻击距离造成的命中减益并能够穿透目标护甲造成伤害",

			"你可以耗费火药来制作土制炸药",
			"你能够将多枚土质炸药打包成捆，进一步增加其破坏力",
			"你能够拆解土制炸药和炸药包以回收部分材料",

			// WANDS & RINGS

			"施法失败的几率取决于法杖状态和等级，若被诅咒则概率进一步提高",
			"汲取充能的概率取决于法杖状态及等级",

			"未被鉴定和受诅咒的法杖会出现施法失败的情况，但无法被汲取充能",
			"若法杖已被鉴定，你能够从无充能法杖中汲取额外充能",

			"部分戒指仅在特定情况下才有效用，其他情况下没有一直佩戴的必要",
			"两个相同类型的戒指效果可以叠加",

			"每个区域通常只有一根法杖，不过有小概率额外获取",
			"每个区域通常只有一枚戒指，不过有小概率额外获取",

//            "Wands of Magic Missile have the highest up-front single-target damage",
			"特定情况下，解离光束可通过调整角度穿透单个目标两次",
			"神罚法杖在对抗秘法生物时非常有效",

			"雷霆法杖的攻击无法通过飞行目标身下的水域传导",
			"酸蚀法杖在近战范围可造成150%有效伤害，但在对抗远处敌人时会受到50%的伤害惩罚",
			"通过火纹法杖的法阵造成的伤害远高于直接对敌人施法",

			"通过原力法杖造成的崩塌伤害高于直接对敌人施法",
			"通过棘藤法杖在植被地格生成的棘藤更加强大",
			"通过冰壁法杖在浸水地格生成的冰墙更加坚固",

			"魅惑法杖可以伤害并扰乱具有秘法特性的敌人",
			"窃血法杖在对抗睡眠或漫游状态的敌人时效力更强",
			"定命法杖的效率基本取决于目标的当前生命值",

			// POTIONS

			"每个区域通常只有一瓶智慧药剂，不过有小概率额外获取",
			"智慧药剂还能够提高等级上限，允许你将等级进一步提高",

			"每个区域通常只有二瓶力量药剂，不过有小概率额外获取",
			"饮用力量药剂是游戏中最有效的生命回复方式",

			"每个商店至少会出售一瓶愈合药剂",
			"愈合药剂还能够治愈中毒，流血等绝大多数物理减益效果",

			"灵视药剂允许你在效力期间几乎无视低视野所带来的惩罚",
			"灵视药剂还会在效力期间提高你的感知属性",

			"浮空药剂在效力期间会提供额外的移动速度和闪避能力",
			"浮空药剂还能够用于安全降落在深渊下方",

			"隐形时你的潜行能力也会提高，能够进一步降低商店行窃的难度",
			"敌人在尝试移动到你的所在位置时会解除你的隐形效果",

			"饮用护盾药剂也能够在效力期间提高你的物理抗性",
			"护盾药剂的效果可以和装备相互叠加",

			"圣灵药剂能够对秘法生物造成极高伤害",
			"在身下扔出圣灵药剂能够弱化持有道具的诅咒程度",

			"液火药剂产生的燃烧效果不会蔓延至临近浸水地格",
			"液火药剂必定点燃周边的可燃地格",

			"You can quickly put out fire in a room with a help of a potion of Frigid Vapours",
			"Potion of Frigid Vapours are more useful against targets standing in the water",

			"Some gases are highly flammable - be careful when using potions of Toxic Gas",
			"Potions of Toxic Gas are very effective against crowds of non-magical enemies",

			"Using a potion of Thunderstorm can attract wandering monsters",
			"Potions of Thunderstorm can be used to flood the dungeon floor or to extinguish fires",

			"Potions of Webbing can be very helpful if you prefer to keep your enemies away from you",
			"Potions of Confusion Gas are especially dangerous in combination with chasms",
			"Potions of Rage have all of the benefits of scrolls of Challenge but without any drawbacks",
			"Potions of Caustic Ooze leave temporary acid puddles when thrown on dry land tiles",

			// SCROLLS

			"There is only 1 scroll of Enchantment per chapter but there is a low chance to find more",
			"Using a scroll of Enchantment on a cursed item will significantly weaken its curse",

			"There are only 2 scrolls of Upgrade per chapter but there is a low chance to find more",
			"Uncursing an enchanted item with scroll of Upgrade allows you to keep the enchantment",

			"Using your scrolls of Identify wisely can save you a lot of time",
			"There is always at least one scroll of Identify in every shop",

			"Scrolls of Transmutation will never change an item into the same item",
			"Scrolls of Transmutation can be used to transmute ammunition and throwing weapons",

			"Scrolls of Sunlight can be used to counteract effect of a potion of Thunderstorm",
			"Never forget that scroll of Sunlight can heal some of your enemies, too",

			"Scrolls of Clairvoyance will not reveal traps or secret doors, only rooms and items",
			"Area revealed by a scroll of Clairvoyance cannot be erased by a scroll of Phase Warp",

			"Scrolls of Banishment can be used to harm undead, elementals and golems",
			"Scrolls of Banishment partially dispel curses from all of the items in your inventory",

			"Enemies blinded by a scroll of Darkness can fall into a chasm or step into a trap",
			"Scrolls of Darkness can be used to counteract effects of scrolls of Sunlight",

			"Scrolls of Phase Warp can save your life as easily as they can end it",
			"Using a scrolls of Phase Warp will confuse you for a short period",

			"Scrolls of Raise Dead can be very deadly against a single creature - including you",
			"Wraiths summoned by using a scroll of Raise Dead will stop being charmed after a while",

			"Using scrolls of Challenge in boss fights is probably the best way to use them",
			"Scroll of Challenge can be used to lure mimics out of their cover",

			"Scroll of Torment is more harmful to you if there are no more enemies in sight",
			"Scroll of Torment is useless against creatures which have no mind to torture",

			// FOOD

			"There is always at least 1 ration of food per depth, but look out for hidden rooms",
			"Some kinds of monsters can drop a raw meat or even a small ration",

			"A full stomach allows you to recover from wounds faster than normal",
			"Fire can burn raw or cooked meat, making it way less useful and tasty",

			"Burned meat is less nutritious and cannot be properly cooked anymore",
			"Stewed meat removes debuffs just like herbs do, but takes longer to eat",

			"Sometimes you can find additional rations, but they will be smaller",
			"You can buy pastry in shops; more often than not it is well worth its cost",

			"Starvation is not immediately harmful; initially it just prevents regeneration",
			"If you ignore starvation for too long, it will become more dangerous",

			"Satiety will be drained slightly slower if you have more strength than required by your equipment",
			"Moving, attacking, casting and blocking decrease your satiety faster than standing still",

			// BOSSES

			"Most bosses can become enraged, but only three times per fight",
			"Bosses are quite vulnerable to explosives, potions and scrolls.",

			"Mind that miasma released by Goo is highly flammable",
			"Goo have several ways to heal itself, but all of them can be denied",

			"Tengu teleports more often when threatened",
			"Tengu cannot teleport when ensnared or blinded",

			"DM-300 is neither organic nor magical creature.",
			"DM-300 starts moving slightly faster after every enrage.",

			"Dwarven King is completetly invulnerable during the ritual",
			"Dwarven King's ritual can be disrupted by a certain spell...",

			// TERRAIN

			"Try to avoid moving in water if you are trying to sneak up on someone",
			"Consider sticking to high grass if you are trying to sneak up on someone",

			"Flying creatures can see over the high grass and are unaffected by terrain effects",
			"Water amplifies shock and frost effects, but extinguishes fire and washes off acid",

			"Traps only appear in standard rooms and never appear in corridors or special rooms",
			"Monsters inhabiting this dungeon are aware of all of its traps and secret doors",

			"Your health regeneration is tripled if you are sleeping on dry land",
			"Sleeping in the water is much less efficient than sleeping anywhere else",
			"Evasion chance is decreased for every adjacent tile which is occupied or impassable",

			// MISC

			"You can pour water from your waterskin to put out fires and wash away caustic ooze",
			"You can grow herbs by watering adjacent high grass tiles with your waterskins",

			"Your lantern makes it much easier to check for traps and secret doors",
			"Oil lantern can be used to start fires on adjacent tiles if you have spare oil flasks",
	};

	private enum Phase {
		FADE_IN, STATIC, FADE_OUT
	}

	private Phase phase;
	private float timeLeft;

//    private BitmapText            message;
//    private ArrayList<BitmapText> tipBox;

	private RenderedText message;
	private ArrayList<RenderedText> tipBox;

	private Thread thread;
	private String error = null;
	private boolean pause = false;

	@Override
	public void create() {
		super.create();

		String text = "";
//        int depth = Dungeon.depth;

		switch (mode) {
			case DESCEND:
				text = TXT_DESCENDING;
//                depth++;
				break;
			case ASCEND:
				text = TXT_ASCENDING;
//                depth--;
				break;
			case CONTINUE:
				text = TXT_LOADING;

//                GamesInProgress.Info info = GamesInProgress.check( StartScene.curClass );

//                if (info != null) {
//
//                    depth = info.depth;
//
//                }

//                depth = depth > 0 ? depth : 0 ;

				break;
			case RESURRECT:
				text = TXT_RESURRECTING;
				break;
			case RETURN:
				text = TXT_RETURNING;
//                depth = returnDepth;
				break;
			case FALL:
				text = TXT_FALLING;
//                depth++;
				break;
		}

		message = PixelScene.renderText( text, 10 );
		message.x = (Camera.main.width - message.width()) / 2;
		message.y = (Camera.main.height - message.height()) / 2;
		add(message);
		align(message);

		tipBox = new ArrayList<>();

		if( YetAnotherPixelDungeon.loadingTips() > 0 ) {

			RenderedTextMultiline tip = PixelScene.renderMultiline(TIPS[Random.Int(TIPS.length)], 6);
			tip.maxWidth(Camera.main.width * 8 / 10);
			tip.setPos(Camera.main.width / 2 - tip.width() / 2,Camera.main.height * 3 / 4 - tip.height() * 3 / 4 + tipBox.size() * tip.height());
			align(tip);
			add(tip);

//            for (RenderedText line : tip.new LineSplitter().split()) {
//                line.measure();
//                line.x = PixelScene.align(Camera.main.width / 2 - line.width() / 2);
//                line.y = PixelScene.align(Camera.main.height * 3 / 4 - tip.height() * 3 / 4 + tipBox.size() * line.height());
//                tipBox.add(line);
//                add(line);
//            }
		}


		phase = Phase.FADE_IN;
		timeLeft = TIME_TO_FADE;

		thread = new Thread() {
			@Override
			public void run() {

				try {

					Generator.reset();

					switch (mode) {
						case DESCEND:
							descend();
							break;
						case ASCEND:
							ascend();
							break;
						case CONTINUE:
							restore();
							break;
//					case RESURRECT:
//                        resurrect();
//                        break;
						case RETURN:
							returnTo();
							break;
						case FALL:
							fall();
							break;
					}

					if ((Dungeon.depth % 6) == 0 && Dungeon.depth == Statistics.deepestFloor ) {
						Sample.INSTANCE.load( Assets.SND_BOSS );
					}

					if( mode != Mode.CONTINUE ) {
						Dungeon.saveAll();
						Badges.saveGlobal();
					}

				} catch (FileNotFoundException e) {

					error = ERR_FILE_NOT_FOUND;

				} catch (Exception e) {

					error = e.toString();
					YetAnotherPixelDungeon.reportException(e);

				}

//                error = ERR_FILE_NOT_FOUND;

				if (phase == Phase.STATIC && error == null) {
					phase = Phase.FADE_OUT;
					timeLeft = TIME_TO_FADE * 2;
				}
			}
		};
		thread.start();
	}

	@Override
	public void update() {
		super.update();

		float p = timeLeft / TIME_TO_FADE;

		switch (phase) {

			case FADE_IN:

				message.alpha( 1 - p );

//            for (BitmapText line : tipBox) {
//                line.alpha( 1 - p );
//            }

				if ((timeLeft -= Game.elapsed) <= 0) {
					if (thread.isAlive() || error != null || YetAnotherPixelDungeon.loadingTips() > 2 ) {
						phase = Phase.STATIC;

						if( !thread.isAlive() && error == null) {
							message.text(TXT_CONTINUE);
							message.x = (Camera.main.width - message.width()) / 2;
							message.y = (Camera.main.height - message.height()) / 2;
							align(message);

							TouchArea hotArea = new TouchArea(0, 0, Camera.main.width, Camera.main.height) {
								@Override
								protected void onClick(Touchscreen.Touch touch) {
									phase = Phase.FADE_OUT;
									timeLeft = TIME_TO_FADE;
									this.destroy();
								}
							};
							add(hotArea);
						}

					} else {
						phase = Phase.FADE_OUT;
						timeLeft = ( YetAnotherPixelDungeon.loadingTips() > 0 ?
								TIME_TO_FADE * YetAnotherPixelDungeon.loadingTips() * 3 : TIME_TO_FADE );
					}
				}
				break;

			case FADE_OUT:

				message.alpha( p );

//            for (BitmapText line : tipBox) {
//                line.alpha( p );
//            }

				if (mode == Mode.CONTINUE || (mode == Mode.DESCEND && Dungeon.depth == 1)) {
					Music.INSTANCE.volume( p );
				}
				if ((timeLeft -= Game.elapsed) <= 0) {
					Game.switchScene( GameScene.class );
				}
				break;

			case STATIC:

				if (error != null) {

					add(new WndError(error) {
						public void onBackPressed() {
							super.onBackPressed();
							Game.switchScene(StartScene.class);
						}
					});

					error = null;

				}
				break;
		}
	}

	private void descend() throws Exception {

		Actor.fixTime();

		if (Dungeon.hero == null) {
			Dungeon.init();
			if (noStory) {
				Dungeon.chapters.add( WndStory.ID_SEWERS );
				noStory = false;
			}
		} else {
			Dungeon.saveAll();
		}

		Level level;
		if (Dungeon.depth >= Statistics.deepestFloor) {
			level = Dungeon.newLevel();
		} else {
			Dungeon.depth++;
			level = Dungeon.loadLevel( Dungeon.hero.heroClass );
		}
		Dungeon.switchLevel( level, level.entrance );
	}

	private void fall() throws Exception {

		Actor.fixTime();
		Dungeon.saveAll();

		Level level;
		if (Dungeon.depth >= Statistics.deepestFloor) {
			level = Dungeon.newLevel();
		} else {
			Dungeon.depth++;
			level = Dungeon.loadLevel( Dungeon.hero.heroClass );
		}

		Dungeon.switchLevel( level, fallIntoPit ? level.pitCell() : level.randomRespawnCell( true, true ) );
	}

	private void ascend() throws Exception {
		Actor.fixTime();

		Dungeon.saveAll();
		Dungeon.depth--;
		Level level = Dungeon.
				loadLevel( Dungeon.hero.heroClass );
		Dungeon.switchLevel( level, level.exit );
	}

	private void returnTo() throws Exception {

		Actor.fixTime();

		Dungeon.saveAll();
		Dungeon.depth = returnDepth;
		Level level = Dungeon.loadLevel( Dungeon.hero.heroClass );
		Dungeon.switchLevel(level, Level.resizingNeeded ? level.adjustPos(returnPos) : returnPos);
	}

	private void restore() throws Exception {

		Actor.fixTime();

		Dungeon.loadGame(StartScene.curClass);
		if (Dungeon.depth == -1) {
			Dungeon.depth = Statistics.deepestFloor;
			Dungeon.switchLevel( Dungeon.loadLevel( StartScene.curClass ), -1 );
		} else {
			Level level = Dungeon.loadLevel( StartScene.curClass );
			Dungeon.switchLevel( level, Level.resizingNeeded ? level.adjustPos( Dungeon.hero.pos ) : Dungeon.hero.pos );
		}
	}

//	private void resurrect() throws Exception {
//
//        Actor.fixTime();
//
//        if (Dungeon.bossLevel()) {
//
//            Dungeon.hero.resurrect( Dungeon.depth );
//            Dungeon.depth--;
//            Level level = Dungeon.newLevel();
//            Dungeon.switchLevel( level, level.entrance );
//
//        } else {
//
//            Dungeon.hero.resurrect(-1);
//            Actor.clear();
//            Arrays.fill(Dungeon.visible, false);
//            Dungeon.level.reset();
//            Dungeon.switchLevel(Dungeon.level, Dungeon.hero.pos);
//
//        }
//    }

	@Override
	protected void onBackPressed() {
		// Do nothing
	}
}
