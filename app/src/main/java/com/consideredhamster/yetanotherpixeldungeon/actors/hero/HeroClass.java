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
package com.consideredhamster.yetanotherpixeldungeon.actors.hero;

import com.consideredhamster.yetanotherpixeldungeon.items.food.RationMedium;
import com.consideredhamster.yetanotherpixeldungeon.items.misc.OilLantern;
import com.consideredhamster.yetanotherpixeldungeon.items.potions.EmptyBottle;
import com.consideredhamster.yetanotherpixeldungeon.items.rings.RingOfShadows;
import com.consideredhamster.yetanotherpixeldungeon.items.wands.Wand;
import com.consideredhamster.yetanotherpixeldungeon.items.wands.WandOfMagicMissile;
import com.consideredhamster.yetanotherpixeldungeon.visuals.Assets;
import com.consideredhamster.yetanotherpixeldungeon.Badges;
import com.consideredhamster.yetanotherpixeldungeon.items.misc.Battery;
import com.consideredhamster.yetanotherpixeldungeon.items.misc.CraftingKit;
import com.consideredhamster.yetanotherpixeldungeon.items.misc.Waterskin;
import com.consideredhamster.yetanotherpixeldungeon.items.misc.Whetstone;
import com.consideredhamster.yetanotherpixeldungeon.items.armours.body.HuntressArmor;
import com.consideredhamster.yetanotherpixeldungeon.items.armours.body.StuddedArmor;
import com.consideredhamster.yetanotherpixeldungeon.items.armours.body.MageArmor;
import com.consideredhamster.yetanotherpixeldungeon.items.armours.body.RogueArmor;
import com.consideredhamster.yetanotherpixeldungeon.items.armours.shields.RoundShield;
import com.consideredhamster.yetanotherpixeldungeon.items.bags.Keyring;
import com.consideredhamster.yetanotherpixeldungeon.items.scrolls.ScrollOfRaiseDead;
import com.consideredhamster.yetanotherpixeldungeon.items.weapons.ranged.Sling;
import com.consideredhamster.yetanotherpixeldungeon.items.weapons.throwing.Bullets;
import com.consideredhamster.yetanotherpixeldungeon.items.weapons.melee.Dagger;
import com.consideredhamster.yetanotherpixeldungeon.items.weapons.melee.Quarterstaff;
import com.consideredhamster.yetanotherpixeldungeon.items.weapons.melee.Shortsword;
import com.consideredhamster.yetanotherpixeldungeon.items.weapons.throwing.Knives;
import com.watabou.utils.Bundle;

public enum HeroClass {

	WARRIOR( "warrior","战士" ), BRIGAND( "brigand","盗贼" ), SCHOLAR( "scholar" ,"学者"), ACOLYTE( "acolyte","侍祭" );

	private String title;
	private String hname;
	
	private HeroClass( String title, String hname ) {
		this.title = title;
		this.hname = hname;
	}

    public static final String[] WAR_ABOUT = {
//            "Your ancestors were many a man. Mad warlords and ruthless mercenaries, some. Noble knights and pious crusaders, others. Cowards? None of them.",
//            "Now, your family is broken and ruined, but blood of your fathers still flows strong in your veins. There is a way to remind all the world about your clan's former glory!",
//            "A way as simple as retrieving a single lost treasure hidden down there, below this city. How hard can that be, after all?",

            "战士的优势在于其优秀的身体素质-他比其他角色的生命力都更加强大，并且能够随着等级提高获得力量。不过因为他庞大的体型和急躁的性格，战士在潜行和闪避方面毫无天赋可言，使其在面对远程法术和攻击时更易受到伤害。",
            "强大的生存能力和对重型武器的适应力使得该角色非常适合新人游玩。",
    };

    public static final String[] ROG_ABOUT = {
//            "King's decree was simple - swift death at the hands of executioner, or a chance to redeem yourself by stealing one valuable trinket - a familiar work for someone like you.",
//            "Should've been a simple choice, normally. But... this one is in the Dungeon! There is a lots of rumors about this place - evil magics and stuff. You know how it goes with all these spellcasting bastards and their experiments.",
//            "Argh, to hell with that! You have always been a gambler. Nobody would say that you have let the Reaper claim you the easy way. And, who knows... After all of this is done, maybe you'll keep this trinket for yourself?",

            "在潜行和敏捷行动领域里，没有人能与盗贼比肩，尽管他必须通过使用轻型护甲才能更有效的利用自己的天赋。但他在武器运用和身体素质方面也毫不逊色。若不是因为对魔能，尤其是法杖的唾弃，游戏最强的职业非他莫属。",
            "游玩本角色需要玩家对游戏机制的基本了解，因此他更适合经验丰富的玩家游玩。",
    };

    public static final String[] MAG_ABOUT = {
//            "Your weary eyes stare down into the abyss. Fingers nervously clutching quarterstaff, searching for calmness in the familiar touch of its wood. This is it.",
//            "All threads lead here. All these years you've spent on seeking the Amulet weren't in vain. The key to all power imaginable, to all knowledge obtainable is hidden in this darkness.",
//            "You only need to brace yourself and make your first step. Your search has ended here. And here, it has only began.",

            "学者精通于法杖的运用之道。绝佳的魔能调谐能力允许他以更高的速率填充法杖充能，并且在法术领域无人能出其右。常年封闭于研究室的生活钝化了他的五感，降低他的命中和感知能力，迫使他必须依赖法杖深入地牢。",
            "不过以同僚的标准来看，他仍然算得上强壮且行动敏捷。武器的低适应性和对法杖的极度依赖使得这名角色更需要技巧游玩，因此该职业仅推荐熟练者游玩。",
    };

    public static final String[] HUN_ABOUT = {
//            "Holy mothers say that fey blood in your veins is a curse for your body but a blessing for your spirit. Frail in build, you always relied on your senses, intuition and faith in the Goddesses.",
//            "Beautiful Artemis, proud Athena and wise Gaia - they have always guided you, sending you insights and prophetic dreams. But, as time went, predictions started to became dark and foreboding.",
//            "They are crystal-clear now - something grows down there, under this City. Something wicked. And it must be nipped in the bud as soon as possible, or else... No gods would save us.",

            "侍祭拥有与生俱来得强大直觉。侍祭对周遭环境的感知与精准的战斗技巧无与伦比，使她更有效地反击敌人和执行远程攻击。不过，精灵血统使她的身体脆弱且多病。她的起始力量低于其他角色，并且升级时得到的额外生命力成长略低于他人。" +
            "脆弱的角色起始条件使侍祭成为一个更具挑战性的职业，该职业更适合精通本游戏的专业玩家，而非仅了解游戏表层机制的玩家游玩。",
    };

    public static final String[] WAR_DETAILS = {
            "· 短剑",
            "· 圆盾",
            "· 镶钉皮甲",
            "· 维护套件",
            "",
            "+ 最大生命",
            "+ 力量",
            "",
            "- 敏捷",
            "- 潜行",
    };

    public static final String[] ROG_DETAILS = {
            "· 匕首",
            "· 飞刀x10",
            "· 盗贼风衣",
            "· 磨刀石",
            "· 暗影之戒",
            "",
            "+ 敏捷",
            "+ 潜行",
            "",
            "- 魔能",
            "- 调谐",
    };

    public static final String[] MAG_DETAILS = {
            "· 短杖",
            "· 魔弹法杖",
            "· 秘法长袍",
            "· 奥术晶石",
            "· 死灵卷轴",
            "",
            "+ 魔能",
            "+ 调谐",
            "",
            "- 命中",
            "- 感知",
    };

    public static final String[] HUN_DETAILS = {
            "· 投索",
            "· 弹药x30",
            "· 精灵斗篷",
            "· 便携工具包",
            "· 空瓶x3",
            "",
            "+ 命中",
            "+ 感知",
            "",
            "- 最大生命",
            "- 力量",
    };
	
	public void initHero( Hero hero ) {
		
		hero.heroClass = this;
		
		initCommon(hero);
		
		switch (this) {
            case WARRIOR:
                initWarrior( hero );
                break;

            case BRIGAND:
                initRogue( hero );
                break;

            case SCHOLAR:
                initMage( hero );
                break;

            case ACOLYTE:
                initHuntress( hero );
                break;
		}
	}
	
	private static void initCommon( Hero hero ) {

		new Keyring().collect();
        new RationMedium().collect();

        new Waterskin().setLimit( 5 ).fill().collect();
        new OilLantern().collect();

    }
	
	public Badges.Badge masteryBadge() {
		switch (this) {
		case WARRIOR:
			return Badges.Badge.MASTERY_WARRIOR;
        case BRIGAND:
            return Badges.Badge.MASTERY_BRIGAND;
		case SCHOLAR:
			return Badges.Badge.MASTERY_SCHOLAR;
		case ACOLYTE:
			return Badges.Badge.MASTERY_ACOLYTE;
		}
		return null;
	}



    public Badges.Badge victoryBadge() {
        switch (this) {
            case WARRIOR:
                return Badges.Badge.VICTORY_WARRIOR;
            case BRIGAND:
                return Badges.Badge.VICTORY_BRIGAND;
            case SCHOLAR:
                return Badges.Badge.VICTORY_SCHOLAR;
            case ACOLYTE:
                return Badges.Badge.VICTORY_ACOLYTE;

        }
        return null;
    }
	
	private static void initWarrior( Hero hero ) {

		hero.STR++;

        hero.HP = (hero.HT += 5);
        hero.defenseSkill -= 5;

		(hero.belongings.weap1 = new Shortsword()).identify().repair().fix();
        (hero.belongings.weap2 = new RoundShield()).identify().repair().fix();
        (hero.belongings.armor = new StuddedArmor()).identify().repair().fix();

//        new TomeOfMastery().collect();
//        new Halberd().repair().fix().collect();
//        new Pistole().identify().repair().fix().collect();
//        new Arquebuse().identify().repair().fix().collect();
//        new Bullets().quantity( 300 ).collect();
//        new Explosives.Gunpowder().quantity( 300 ).collect();
//        new WandOfDamnation().repair().fix().collect();
//        new RingOfAccuracy().repair().fix().collect();
//        new WandOfFirebrand().repair().fix().identify().collect();
//        new WandOfIceBarrier().repair().fix().identify().collect();
//        new SplintArmor().inscribe().repair().fix().identify().collect();
//        new PoisonDarts().quantity(100).collect();
//        new ArmorerKit().collect();

    }

    private static void initRogue( Hero hero ) {

        hero.defenseSkill += 5;
        hero.magicPower -= 5;

        (hero.belongings.weap1 = new Dagger()).identify().repair().fix();
        (hero.belongings.weap2 = new Knives()).quantity(10);
        (hero.belongings.armor = new RogueArmor()).identify().repair().fix();
        (hero.belongings.ring1 = new RingOfShadows()).identify();

        hero.belongings.ring1.activate( hero );

        new Whetstone().collect();
    }
	
	private static void initMage( Hero hero ) {

        hero.magicPower += 5;
        hero.attackSkill -= 5;

		(hero.belongings.weap1 = new Quarterstaff()).identify().repair().fix();
		(hero.belongings.weap2 = new WandOfMagicMissile()).identify().repair().fix();
        (hero.belongings.armor = new MageArmor()).identify().repair().fix();

        ((Wand)hero.belongings.weap2).recharge();

        new ScrollOfRaiseDead().identify().collect();
        new Battery().collect();

	}
	
	private static void initHuntress( Hero hero ) {

        hero.STR--;

        hero.HP = (hero.HT -= 5);
        hero.attackSkill += 5;

        (hero.belongings.weap1 = new Sling()).repair().identify().fix();
        (hero.belongings.weap2 = new Bullets()).quantity( 30 );
        (hero.belongings.armor = new HuntressArmor()).identify().repair().fix();

        new EmptyBottle().quantity(3).collect();
        new CraftingKit().collect();
    }
	
	public String title() {
		return title;
	}

	public String hname(){
	    return hname;
    }

	public String spritesheet() {
		
		switch (this) {
            case WARRIOR:
                return Assets.WARRIOR;
            case BRIGAND:
                return Assets.BRIGAND;
            case SCHOLAR:
                return Assets.SCHOLAR;
            case ACOLYTE:
                return Assets.ACOLYTE;
		}
		
		return null;
	}

    public String[] history() {

        switch (this) {
            case WARRIOR:
                return WAR_ABOUT;
            case BRIGAND:
                return ROG_ABOUT;
            case SCHOLAR:
                return MAG_ABOUT;
            case ACOLYTE:
                return HUN_ABOUT;
        }

        return null;
    }

	public String[] details() {
		
		switch (this) {
            case WARRIOR:
                return WAR_DETAILS;
            case BRIGAND:
                return ROG_DETAILS;
            case SCHOLAR:
                return MAG_DETAILS;
            case ACOLYTE:
                return HUN_DETAILS;
        }
		
		return null;
	}

	private static final String CLASS	= "class";
	
	public void storeInBundle( Bundle bundle ) {
		bundle.put( CLASS, toString() );
	}
	
	public static HeroClass restoreInBundle( Bundle bundle ) {
		String value = bundle.getString( CLASS );
		return value.length() > 0 ? valueOf( value ) : WARRIOR;
	}
}
