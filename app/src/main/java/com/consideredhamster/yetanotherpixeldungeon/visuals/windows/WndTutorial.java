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
package com.consideredhamster.yetanotherpixeldungeon.visuals.windows;

import com.consideredhamster.yetanotherpixeldungeon.visuals.Assets;
import com.consideredhamster.yetanotherpixeldungeon.YetAnotherPixelDungeon;
import com.consideredhamster.yetanotherpixeldungeon.scenes.PixelScene;
import com.consideredhamster.yetanotherpixeldungeon.visuals.ui.RenderedTextMultiline;
import com.consideredhamster.yetanotherpixeldungeon.visuals.ui.ScrollPane;
import com.consideredhamster.yetanotherpixeldungeon.visuals.ui.Window;
import com.watabou.gltextures.SmartTexture;
import com.watabou.gltextures.TextureCache;
import com.watabou.noosa.Image;
import com.watabou.noosa.RenderedText;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.ui.Component;

public class WndTutorial extends WndTabbed {

	private static final int WIDTH_P	= 120;
	private static final int HEIGHT_P	= 160;

	private static final int WIDTH_L	= 150;
	private static final int HEIGHT_L	= 128;

    private static SmartTexture icons;
    private static TextureFilm film;

	private static final String TXT_TITLE	= "游戏教程";

	private static final String[] TXT_LABELS = {
        "壹", "貳", "叁", "肆", "伍",
    };

    private static final String[] TXT_TITLES = {
        "教程-基本界面",
		"教程-游戏机制",
		"教程-装备介绍",
		"教程-消耗物品",
		"教程-地牢住民",
    };

    private static final String[][] TXT_POINTS = {
        {
        "在游戏中，几乎所有操作都可以通过直接点击目标来完成。这包括但不限于移动，攻击，拾起物品以及与NPC和其他地牢元素的互动。",
        "通过点击左上角的角色头像来查看你当前拥有的属性和效果。如果其中一个属性正受到未鉴定物品的影响，它的值将显示为\"??\"。",
        "点击左下角的时钟按钮可以跳过一个回合。长按该按钮可以进行休息——这会让你以更快地速度跳过回合，但也会大幅提高生命回复速度。",
        "取决于你在游戏中的设置，这个按钮允许你点击两次或长按以进行一次周边搜索，让你发现周边的隐藏暗门或陷阱。当然，你也可以轻点按钮并选中任何地牢中的物品，以阅读它们的描述并获知其当前状态。",
        "这个按钮位于屏幕右下角。点击它即可查看你的背包(想必你已经知道了)。长按它可以查看你当前持有的各类钥匙。",
        "背包按钮左侧有三个快捷栏，你可以利用快捷栏省略打开背包的步骤直接使用物品。长按快捷栏以添加或切换物品。",
        "背包按钮上方有一个副手快捷栏。它的效用取决于你当前使用的副手和主手装备。例如，在装备法杖和远程武器的场合，点击它就会进行一次施法/射击。",
        "副手快捷栏上方还有数个目前可能尚未显示的按钮。它们分别是危险标记，快速攻击，拾取提示和继续按钮。危险标记上会用数字标识出当前视野内的敌人数量。",
        "点击危险标记可以选取一名敌人，随后点击快速攻击按钮就可以在不点击屏幕的情况下攻击该名敌人。同时，长按快速攻击按钮也会显示目标的描述和当前状态。",
        "站在可拾取物品的正上方才会出现拾取提示。点击拾取提示即可在不点击角色所在地格的情况下拾取这些物品。",
        "角色头像下方的这个按钮可以让你在不打开背包的情况下直接喝下水袋中的水。你也可以长按此按钮，将水从水袋中倾倒出来。",
        "水袋按钮下方还有一个煤油灯相关的按钮。它的运作方式相对智能——点击它即可完成点亮，熄灭和添加灯油等操作。长按该按钮允许你倾倒煤油并点然周边的地格。",
},
        {
        "游戏中绝大多数动作都只消耗一个回合，这意味着一次攻击和移动一格所花费的时间完全一致，绝大多数生物的移动速度适用此规则。记住，指令一旦下达动作就会执行，你需要在所有动作都结算完成后才能继续行动。",
        "敌人有一定几率注意到你的存在。该几率取决于它们的感知与你的潜行属性值。同时也取决于你和敌人的距离，不过处于索敌状态的敌人不论如何都会发现视野中的你。",
        "不过，敌人在失去你视野的时候，它们可能会无法继续追击，并给予你伏击它们的机会。这类情况发生的概率很大程度上取决于你的潜行值。你可以(并且应当)利用墙角，房门或茂密植被来实现这个战术。不过别忘记飞行生物可以看穿茂密植被内的隐蔽。",
        "执行攻击或受到攻击时，游戏决定的第一件事是该攻击是否命中。通常情况下，命中的概率取决于攻击者命中属性和防御者的敏捷属性。这些属性在不同的职业和怪物间都有着不同的成长速率。",
        "防御方身侧每拥有一个无法通行的地格，它的敏捷属性就会降低1/16。这意味着如果你想让自己的攻击更易命中，你应当将敌人引用到狭长的走廊上；若想让自己更容易闪躲敌人的攻击，你就应当坚守空旷的区域。",
        "所有远程攻击的实际命中率都会因每格攻击距离减少1/8。唯一的例外便是燧发枪。在法杖之中只有魔弹法杖的施法可被闪避，但其命中率由魔能属性决定。",
        "玩家角色(及部分后期怪物)在第二次连续命中后，每次连击都会少量提高造成伤害。该机制会以\"连击\"字样显示在角色上方，并且显著提高你在对抗大群怪物的战斗效能。",
        "你的感知属性会影响正常行动时发现周遭陷阱或暗门，及隔墙感知到敌人行动的几率。值得注意的是，随着你深入地牢，地牢中埋藏的秘密会变得更加难以发现。",
        "茂密植被是执行伏击的理想地形，因其既能阻挡视野，也能使你在其中隐蔽身型。而在水潭中行走正好相反，既会产生无用噪音，也会减少你的潜行值。",
        "睡眠是最佳的回复方式。在你睡眠时，你会以三倍自然回复速率恢复生命值。在水潭上睡眠则不会获得加成，不过你仍可在需要快速跳过回合时执行该操作。",
        "手动搜索或点亮油灯可确保你对周边地格的秘密(如陷阱和暗门)一览无遗。不过需要记住的是，在地牢中点亮灯光也会让敌人立刻注意到你的存在。",
        "调谐属性能够显著影响持有法杖的充能速度，并且影响在装备未识别装备时看穿诅咒并终止行动的几率。魔能属性决定了法杖的效果和伤害，同时也会影响部分进攻性卷轴的效果。",
        },
        {
        "近战武器被分为多种类别。其中最基本的类别就是重型单手武器，它们没有任何特殊效果或减益，并且可与任何装备搭配使用而不会触发额外的力量惩罚。",
        "轻型单手武器可作为副手武器使用，但需要额外的力量属性才能有效使用。双持武器能够增加50%的攻击速度的同时也能格挡攻击。",
        "轻型双手武器基本上由各类长柄武器组成。这些武器的设计并不适于和其他武器双持使用，并需要额外的力量属性才能有效使用。不过它们仍能和盾牌一起使用。",
        "重型双手武器不能与盾牌或其他近战武器一起使用。投掷武器和法杖仍能装备于副手。同时，如果副手没有装备物品，你的主武器也可用来格挡。",
        "投掷武器可以装备为副手武器。它们无法被升级强化，但允许你在装备近战武器的同时拥有远程手段。它们不会因为耐久度受损，却有概率在使用时损毁(远程武器弹药同样适用此条)。",
        "远程武器需要主副手同时使用——主手装备武器，副手装备弹药。没有弹药的情况下你的攻击与未装备武器无异。每种远程武器都需要特定种类的弹药。",
        "燧发火器在射击时需要副手装备弹药，并需要背包内拥有足够的火药用于再次装填。同时，巨大的噪音往往会引起不必要的注意。不过火器的命中率不会随着距离的增加而衰减，并且它们能轻松穿透目标的护甲。",
        "不论何时，务必身着一件护甲。合适的护甲能够大幅提高你的生存几率，减少来自多数来源的伤害。不过护甲无法抵挡非物理伤害，如燃烧，电击，解离射线等。",
        "布制护甲提供的保护非常有限，不过它能够增强你的一个次要属性——潜行，感知或意志。增强数值会随着护甲升级而进一步提高，并能够形成一些强大（但具有风险）的角色构建。",
        "盾牌会占用你的副手，但有50%的几率将自身护甲等级计入伤害处理，并在格挡敌人攻击时更加有效。成功格挡或招架敌人攻击时有概率使其被弹反，允许你执行一次必定命中的反击。",
        "法杖有着非常强大的效果，但你需要装备它们才能使用，并且魔杖内只能存储少量充能。诅咒和未鉴定法杖在使用时有几率施法失败。有些法杖在使用时会消耗所有充能，而它们的效果取决于消耗的充能数。",
        "戒指是一种稀有的饰品，能够为装备者提供很大的助益。戒指本身并不强大，但它们提供的增益可以相互叠加。受诅咒的戒指反而会削弱你的能力。",
        },
        {
        "多数装备都可升级，升级后的装备会比原先更加强大，武器提高伤害，防具提高保护，力量需求降低，充能上限和速度增加且使用时更不易损坏。不过同一件装备只能升至3级，务必记住这点。",
        "武器和护甲都可被附魔。附魔可向装备赋予一些独特效果，比如附带火焰伤害或提高酸蚀抗性，但其触发几率很大程度上取决于你的装备等级。同时，受到诅咒的装备会逆转其上的魔法效果，使它们对你造成负面影响",
        "有些物品可能是被诅咒的，这意味着装备它们时，直到诅咒(通过特定卷轴和药剂的帮助)消除前都无法卸下。诅咒物品与非诅咒物品有着相同的伤害和防护效果，但它们需要更高的力量才能有效使用并且更易受损。稀有度明显高于当前章节的物品有更高的概率受到诅咒",
        "大多数装备都具有状态等级。会随着道具的使用缓慢降低，但可以借助对应的维修工具或升级卷轴来修复。状态等级对道具的影响和道具等级一样，但不会影响装备的力量需求或法杖的充能上限",
        "你的角色需要食物才能在地牢生存。每层至少存在一个口粮；你也可以在商店里购买食物，部分怪物有时也会掉落食物。当饱食度超过75%时，自然回复速率会增加；当饱食度低于25%时，自然回复速率减半。当的饱食度达到0%时，就会因饥饿而受到周期性伤害。",
        "最易于使用且数量充足的治疗方式就是你的水袋了。饮用水袋时，你会基于已损失生命值立即恢复生命值，你也可以在地牢中偶尔出现的水井里补充水袋。每个章节都至少拥有一座水井。",
        "地牢的无尽黑暗之中，自然光源几乎无法见到，极大地限制了你的视野。你可以点亮油灯来驱散视野上的影响。但如此做基本等同于放弃任何潜行行为，建议慎重考虑。",
        "如果有一些多余的火药，你可以利用它们制成土质炸药。这些炸药可被拆除并回收包裹其中的一部分火药，你也可以将它们捆绑在一起，组合成更加强力的炸药包。",
        "在使用得当的场合下，卷轴可以发挥出强大的功用。但使用不当的情况下，少数卷轴甚至会将你引入死亡之中。你没有办法通过观察确认卷轴的种类，除非你试着去读或者在商店里见到一个相同卷轴。",
        "探索地牢时你会找到各色药剂。药水根据效果不同有益有害。有益药剂能够使你获得增益，而有害药剂通常更适合被扔向敌人。",
        "有时在茂盛植被中会出现草药。你可以直接吃下它们，净化或防止特定减益状态的效果，也可在炼金釜中精炼药剂或烹饪生肉。",
        "如果发现背包格太少，可以在商店里购买行囊。不同的药草、药剂、卷轴或魔杖分别可以被收纳在一个专属的行囊中。此外，它还可以保护这些物品免受环境伤害比如火)。",
        },
        {
        "探索地牢时，你会遇见诸多敌人。击败敌人是经验的主要来源，可用于提高你的等级，不过只有在敌人拥有足够威胁时你才能从战斗中得到进步。",
        "地牢里最不缺的就是怪物，即使你杀死了眼前的怪物，更多的怪物会出现并追击你。有些怪物会在死亡时掉落物品，但最好不要对掉落物抱有太高期待。每当地牢中出现一只新生物时，本楼层出现生物的间隔时长就会提高一点。",
        "地牢中的每只怪物都有一些特别的能力，但通常你可以将它们分类对待。最常见的敌人，比如老鼠和苍蝇，通常具有较高的敏捷和潜行能力，不过它们的攻击能力很弱，并且在承受沉重打击时只会...想必你猜到了，死的很惨。",
        "一些相对不那么常见的敌人，如窃贼，骷髅或豺狼暴徒通常没有明显的缺陷和优势。有些怪物甚至能从中短距离攻击你，不过这些攻击通常效果不佳，并且受到伤害时它们会立即切换回自己的近战武器。",
        "不过部分敌人有着更加可靠的远程攻击手段，并且它们不论何时都会尝试这些手段。更糟糕的是该类别下的敌人通常造成的都是无视护甲的非物理伤害。不过，有些敌人在攻击前也需要花费一个回合去充能。",
        "有些怪物则远比其他敌人具有危险性，它们强壮，结实，并且出手精准。它们唯一的弱点是较低的敏捷属性。同时它们也更容易被伏击，行动时产生的声响也更容易被你听见。",
        "通常来说，多数敌人都只归属于自己的章节，不会出现在其他章节之中，但有些敌人会出现在地牢的所有角落之中。它们的能力会一直匹配当前层数的标准。不过此类型中绝大多数的敌人也都有某种弱点，使得应对它们时会相对简单一些。",
        "在地牢之中，boss才是最大的威胁。它们非常强大，扛打，并且拥有独特的能力。最棘手的一点是你无法回避boss战，必须击败它们才能继续深入地牢。击败它们需要你做好充足准备并全神贯注，不过在对抗特定boss时也存在着相应技巧，可以略微降低战斗的难度。",
        "不过地牢之中的生物并非全部都想置你于死地。地牢中有一些友好住民，甚至有些会请求你完成一个小任务。显然按照它们的要求去做你就会得到一定的奖励，但你也完全可以忽略它们，这不会对你后续的游戏产生任何其他影响。",
        "有些NPC嘛...对你没有任何要求，除了钱。每隔五层都会出现一个小商店，你可以在那里出售自己获取的多余物品并购置一些补给。商店出售的商品种类和质量取决于当前所处章节，但有些商品必定出现在店里。",
        "最后请记住，地牢之中的某些敌人是秘法来源，并非自然的造物，因此可以免疫一些需要肉身和生灵才会产生的负面效果。不过这也使一些不对寻常生物起作用的效果使用在它们身上时会产生未知的影响。",
        "那么，这就是全部了。如果你从头到尾阅读完了这篇教程，那这就意味着你已经具备了游玩本作所需的全部知识。一些更加细节的内容会在加载游戏时出现(记得多看看它们)，你还可以通过阅读像素地牢wiki上YAPD部分的文章(或者莲恩锐意制作中的YAPD功略)(多催催她！)来了解更多游戏的内部机制。祝你好运，千万小心那些宝箱怪！",
		},
    };

	private RenderedText txtTitle;
	private ScrollPane list;

    private enum Tabs {

        INTERFACE,
        MECHANICS,
        CONSUMABLES,
        EQUIPMENT,
        DENIZENS,

    };

//	private ArrayList<Component> items = new ArrayList<>();

	private static Tabs currentTab;

	public WndTutorial() {
		
		super();

        icons = TextureCache.get( Assets.HELP );
        film = new TextureFilm( icons, 24, 24 );
		
		if (YetAnotherPixelDungeon.landscape()) {
			resize( WIDTH_L, HEIGHT_L );
		} else {
			resize( WIDTH_P, HEIGHT_P );
		}
		
		txtTitle = PixelScene.renderText( TXT_TITLE, 8 );
		txtTitle.hardlight( Window.TITLE_COLOR );
		PixelScene.align(txtTitle);
		add( txtTitle );
		
		list = new ScrollPane( new Component() );
		add( list );
		list.setRect( 0, txtTitle.height(), width, height - txtTitle.height() );

		Tab[] tabs = {
            new LabeledTab( TXT_LABELS[0] ) {
                @Override
                protected void select( boolean value ) {
                    super.select( value );

                    if( value ) {
                        currentTab = Tabs.INTERFACE;
                        updateList( TXT_TITLES[0] );
                    }
                };
            },
            new LabeledTab( TXT_LABELS[1] ) {
                @Override
				protected void select( boolean value ) {
					super.select( value );

                    if( value ) {
                        currentTab = Tabs.MECHANICS;
                        updateList( TXT_TITLES[1] );
                    }
				};
			},
            new LabeledTab( TXT_LABELS[2] ) {
                @Override
                protected void select( boolean value ) {
                    super.select( value );

                    if( value ) {
                        currentTab = Tabs.EQUIPMENT;
                        updateList( TXT_TITLES[2] );
                    }
                };
            },
            new LabeledTab( TXT_LABELS[3] ) {
                @Override
                protected void select( boolean value ) {
                    super.select( value );

                    if( value ) {
                        currentTab = Tabs.CONSUMABLES;
                        updateList( TXT_TITLES[3] );
                    }
                };
            },
            new LabeledTab( TXT_LABELS[4] ) {
                @Override
                protected void select( boolean value ) {
                    super.select( value );

                    if( value ) {
                        currentTab = Tabs.DENIZENS;
                        updateList( TXT_TITLES[4] );
                    }
                };
            },
		};

        int tabWidth = ( width + 12 ) / tabs.length ;

		for (Tab tab : tabs) {
			tab.setSize( tabWidth, tabHeight() );
			add( tab );
		}
		
		select( 0 );
	}
	
	private void updateList( String title ) {

		txtTitle.text( title );
		PixelScene.align(txtTitle);
		txtTitle.x = PixelScene.align( PixelScene.uiCamera, (width - txtTitle.width()) / 2 );
		
//		items.clear();
		
		Component content = list.content();
		content.clear();
		list.scrollTo( 0, 0 );
		
		int index = 0;
		float pos = 0;

        switch( currentTab ) {

            case INTERFACE:
                for (String text : TXT_POINTS[0]) {
                    TutorialItem item = new TutorialItem(text, index++, width);
                    item.setRect(0, pos, width, item.height());
                    content.add(item);
//                    items.add(item);

                    pos += item.height();
                }
                break;

            case MECHANICS:

                index += 12;

                for (String text : TXT_POINTS[1]) {
                    TutorialItem item = new TutorialItem(text, index++, width);
                    item.setRect(0, pos, width, item.height());
                    content.add(item);
//                    items.add(item);

                    pos += item.height();
                }
                break;

            case EQUIPMENT:

                index += 24;

                for (String text : TXT_POINTS[2]) {
                    TutorialItem item = new TutorialItem(text, index++, width);
                    item.setRect(0, pos, width, item.height());
                    content.add(item);
//                    items.add(item);

                    pos += item.height();
                }
                break;

            case CONSUMABLES:

                index += 36;

                for (String text : TXT_POINTS[3]) {
                    TutorialItem item = new TutorialItem(text, index++, width);
                    item.setRect(0, pos, width, item.height());
                    content.add(item);
//                    items.add(item);

                    pos += item.height();
                }
                break;

            case DENIZENS:

                index += 48;

                for (String text : TXT_POINTS[4]) {
                    TutorialItem item = new TutorialItem(text, index++, width);
                    item.setRect(0, pos, width, item.height());
                    content.add(item);
//                    items.add(item);

                    pos += item.height();
                }
                break;

        }
		
		content.setSize( width, pos );
	}
	
	private static class TutorialItem extends Component {

        private final int GAP = 4;
        private Image icon;
		private RenderedTextMultiline label;
		
		public TutorialItem( String text, int index, int width ) {
			super();

            icon.frame( film.get( index ) );

            label.text( text );
            label.maxWidth(width - (int)icon.width() - GAP);
            PixelScene.align(label);

            height = Math.max( icon.height(), label.height() ) + GAP;
		}
		
		@Override
		protected void createChildren() {

            icon = new Image( icons );
            add( icon );
			
			label = PixelScene.renderMultiline( 5 );
			add( label );
		}
		
		@Override
		protected void layout() {
			icon.y = PixelScene.align( y );


			float x1 = icon.x + icon.width;
			float y1 = PixelScene.align( y );
            label.setPos(x1,y1);
		}
	}
}
