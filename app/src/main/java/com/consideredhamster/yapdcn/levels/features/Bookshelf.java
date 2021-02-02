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
package com.consideredhamster.yapdcn.levels.features;

import com.consideredhamster.yapdcn.visuals.Assets;
import com.consideredhamster.yapdcn.Dungeon;
import com.consideredhamster.yapdcn.items.Generator;
import com.consideredhamster.yapdcn.levels.Level;
import com.consideredhamster.yapdcn.levels.Terrain;
import com.consideredhamster.yapdcn.scenes.GameScene;
import com.consideredhamster.yapdcn.misc.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;

public class Bookshelf {

    private static final String TXT_FOUND_SCROLL	= "你找到了一张卷轴！";
    private static final String TXT_FOUND_NOTHING	= "这里没什么值得注意的。";
    private static final String TXT_FOUND_READING	= "你找到了%s";

    private static final String[] BOOKS = {

			// LORE

			"一些宗教手稿，其上讲述了一位名为Watabou的神，这位强大却又谜团重重的神创建了这个世界，但随后又弃世离去。",

			"一则传说故事，描绘了Watabou使用了一个奇迹魔方创造了这个世界的韵律法则，但这个神之器物如今已无迹可寻。",

			"一些手稿，上面记述着如今的世界是曾经世界中破碎而成的无数世界之一，每一个世界都各有不同并以自己的方式存续至今。",

			"一些上古石刻板，板上刻画着一个残忍且恶毒的神蚕食着这个世界的故事，不知为何这个神似乎是被认作一只仓鼠。",

			"一本描述强大的Yendor巫师与一名黑暗之神英勇战斗并获胜的古旧书本，不过在故事的最后Yendor付出了生命的代价。",

			// DENIZENS

			"一本描述豺狼人一族，及其野蛮祭祀和习性的古书。从书中不难看出，豺狼人认为它们可以吸收所食猎物的精华。",

			"一本记录了各种与巨魔接触事件的书，不过书的一部分被撕扯下来了。这些生物平时幽静隐居，但在受到威胁时又异常凶猛。",

			"一本小册，其上记录着矮人铁匠与工程师们在科技上的光辉成就。书面上残留着一抹如干涸血液般的污渍。",

			"一本记录着世界深处各类恐怖恶魔生物的魔典，根据书中所述，其中最为强大的个体名为Yog Dzewa。",

			"一个破旧的典籍，似乎讲述了召唤各种秘法生物的仪式-死灵，魔像，元素等等...但它已经破旧不堪，无法读出有用的信息。",

			// CHARACTERS

			"一本历史书，讲述了个性暴躁但勇敢而光荣的极北图勒(Thule)大陆一族的事迹。这片大陆之上的勇武之士和海贸商才冠绝九州。",

			"一本地理书，讲述了南部沙漠之中生活的一支野蛮但狡诈恶毒的部落民。",

			"一本由远东帝国的一名魔法师撰写而成的哲学著作，这些法师们以重视知识高于一切而闻名。",

			"一本描述西部住民们简朴却充实生活方式的自传书，他们遵循着其德鲁伊祭司的意志并接受其恩惠。 ",

			// CREDITS

			"一本讲述烈焰女巫Inevielle的古书。她以天才的词法能力和对珍奇动物的驯兽技巧而出名。",

			"一本知名画家Logodum的传记，其出色的绘画作品点缀着整个世界。直到如今其作品仍然广为人知。",

			"一本描述着一名疲惫不堪的冒险者的故事片段。有着可靠的指虎和抛网在旁，他能冲破任何困境。",

			"一本B'gnu-Thun的旧笔记。作为一名优秀的猎人兼匠人，他凭借自己的才华创造了坚固盾牌，华美皮革和不计其数的其他物件。",

			"一则关于狡诈的豺狼战酋R'byj的故事，凭借自己的战术知识极大地改变了当今世界的构造。",

			"由远东诗人Jivvy编撰的乐谱，他演奏的出色乐曲理应传遍世界。",

			"一本由破碎之Evan写下的典籍，这位大贤者为了自己的世界孜孜不倦地努力。如此事迹启发了无数后人，并被推崇膜拜。",

			// EXTRA CREDITS FOR YAPDCN
			// Jinkeloid
			// In case anyone seeing this think we deserve a spot in your mod, I prepared translation for this, tbh I was just bored
			"一本记录各类法杖的魔典，书中提到冰壁法杖曾是大法师Yendor的强大造物，试图再现这根法杖能力的法师Jinkeloid在过程中不慎触发了杖内禁术，永久改变了这个世界所使用的文字。",
			// Translation: A grimoire that cataloged every wand in this world, mentioning Wand of Ice Barrier being the artifact made by almighty mage Yendor,
			// a mage named Jinkeloid trying to harness this wand triggered a curse within, forever changing the language of this world.
			// Lynn
			"一卷由星象家Lynn绘制的夜空星图，不仅记载了可用作占卜的星座分布，还有各类用于导航用途的高亮恒星，只可惜地牢之下无法仰望星空。",
			// a scroll of star chart created by Lynn the astrologer, not only constellations are in, other stars for navigation purposes are also present,
			// what a pity you can't gaze the stars right now.
			// Alexstrasza
			"一本出自位面旅行者Alexstrasza之手的日志，尽管没有做出创世者般的壮举，他在不同位面中留下的足迹依旧留下了不少影响。",
			// a diary written by Alexstrasza the planeswalker, though having no deeds as the world creators, he managed to leave his step and influenced many worlds after.

			// CONTEST WINNERS

			"一部关于传奇冒险家Connor Johnson的书，凭借着坚持不懈的意志和无与伦比的天赋，那些以常人标准毫无可能的挑战对他而言如同闲庭信步。",

			"一卷关于一名高阶祭司，惊世元祖Heimdazell的史诗。他拥有如猫头鹰般的睿智和坚果墙般的韧性。新的世界正因他的事迹而诞生发展。",

			"一则关于Nero的神话，对于这位手持巨斧，脾性暴躁的狂暴矮人战酋来说，面对任何挑战都不在话下。",

			"一则关于盗贼Krautwich的传说故事，凭借伴他已久的匕首和十字弩，成功闯过了一片无数人失败倒下的领域。",

			"一个讲述了嗜虐魔神所创危险地宫的古老传说。看着很有意思，可惜它位于世界的另一端。",

			"一则关于大千之Illion的寓言，其上讲述了他寻求全能锤杖而踏上的无止尽征途。",


			// MISC

			"一些由远古哲学家们留下的笔记，其上记述着他们的观念：这个世界仍处在不平衡的状态之中，因其仍未被完成。",

			"一些奇异且支离破碎的预言，其中提到了死者吹响号角，血红服饰的恶魔的来访，以及身着盛装的巨蟹等等。",

    };

	public static void examine( int cell ) {

        if (Random.Float() < ( 0.05f + 0.05f * Dungeon.chapter() ) ) {

            Dungeon.level.drop( Generator.random( Generator.Category.SCROLL ), Dungeon.hero.pos ).sprite.drop();
            GLog.i( TXT_FOUND_SCROLL );

            //slightly increase the chance of obtaining lore pieces so what I wrote may be seen, and they are more likely to occur in later chapters
        } else if (Random.Float() < 0.03f + 0.06f * Dungeon.chapter() ) {

            GLog.i( TXT_FOUND_READING, BOOKS[ Random.Int( BOOKS.length ) ] );

        } else {

            GLog.i( TXT_FOUND_NOTHING );

        }

        Level.set( cell, Terrain.SHELF_EMPTY );
        Dungeon.observe();
		
		if (Dungeon.visible[cell]) {
            GameScene.updateMap( cell );
			Sample.INSTANCE.play(Assets.SND_OPEN);
		}
	}
}
