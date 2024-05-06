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
package com.consideredhamster.yapdcn.visuals.windows;

import com.consideredhamster.yapdcn.YetAnotherPixelDungeon;
import com.consideredhamster.yapdcn.scenes.PixelScene;
import com.consideredhamster.yapdcn.visuals.ui.RenderedTextMultiline;
import com.consideredhamster.yapdcn.visuals.ui.ScrollPane;
import com.consideredhamster.yapdcn.visuals.ui.Window;
import com.watabou.noosa.BitmapText;
import com.watabou.noosa.BitmapTextMultiline;
import com.watabou.noosa.RenderedText;
import com.watabou.noosa.ui.Component;

public class WndChangelog extends Window {

	private static final int WIDTH_P	= 128;
	private static final int HEIGHT_P	= 160;

	private static final int WIDTH_L	= 160;
	private static final int HEIGHT_L	= 128;

	private static final String TXT_TITLE	= "YAPD v0.3.3";

    private static final String TXT_DESCR =
            "大家好！我是冰杖。\n\n " +
                    "不认识我？没关系，只需要知道我是汉化版本的作者即可。_汉化基于最新版本0.3.3，已获得作者ConsideredHamster许可。_\n" +
                    "\n" +
                    "是的，你没看错，仓鼠时隔近五年更新了，我也有理由把这个老项目重新掏出来翻新一下。\n" +
                    "由于YAPD中文化的模式和破碎略有不同(它不自带本地化框架)，所以本次制作可能出现一些bug，届时请截图并联系作者qq377844252进行反馈\n" +
                    "大量敌人的描述重做，拜此所赐敌人名称暂时也不做翻译\n" +
                    "新加入的绝大部分字串都尚未翻译，但游戏的中文翻译量应当足够正常进行游戏\n" +
                    "\n" +
                    "鸣谢：\n" +
                    "Alexstrasza和Lynn对汉化后期剩余字串的及时翻译，不然我懒癌一发作这版本大概可以再拖几个月(\n\n" +
                    "Ømicrónrg9提供的YAPDCN图标(没错这个也是我懒得做)\n\n" +
                    "\n\n\n" +
                    "_虽然MISPD0.3.0beta和YAPD的多语言化都没有出，但我相信MISPD0.3.0beta还是更有希望的！（_\n"
    ;

    private static final String TXT_DESYA =
            "\n\n" +
                    "_GENERAL_\n" +
                    "\n" +
                    "- all difficulty levels are now unlocked by default\n" +
                    "- last three chapters became much more open-ended\n" +
                    "- added more decorations for wall and floor tiles\n" +
                    "- made a lot of tweaks to make chapters differ from each other\n" +
                    "- added random clutter in empty rooms to make them look a bit different\n" +
                    "\n" +
                    "- added \"burnt\" floors which have a lot of (likely cursed) bones and barely any grass\n" +
                    "- added \"storage\" floors which change most of the floor's walls to (mostly empty) shelves\n" +
                    "- added \"guarded\" floors with additional mobs spawned, some of them wandering by default\n" +
                    "- \"trapped\" floors no longer affect mob amount and respawn rate\n" +
                    "- decreased amount of traps on \"flooded\" floors\n" +
                    "\n" +
                    "- removed the \"haunted\" floor feeling\n" +
                    "- all hazards now have descriptions when inspected\n" +
                    "- the ambitious imp now only spawns in the floor's exit room\n" +
                    "- the troll blacksmith's quest now requires only 5 pieces of dark gold ore\n" +
                    "\n" +
                    "_ENEMIES & BOSSES_\n" +
                    "\n" +
                    "- the mob description window now also shows their stats, resistances and abilities\n" +
                    "- completely reworked the Tengu boss fight\n" +
                    "- completely reworked the DM-300 boss fight\n" +
                    "- boss summons now can be encountered as regular mobs\n" +
                    "\n" +
                    "- evil eyes no longer run away when approached\n" +
                    "- evil eye beams are now reflected from walls\n" +
                    "- golems can now inflict knockback upon attacking\n" +
                    "- blackguards can now pull you closer with harpoons\n" +
                    "\n" +
                    "- stats of gnoll hunters/shamans now scale with the current chapter\n" +
                    "- piranhas and imps can turn invisible now\n" +
                    "- significantly decreased the chance of mobs dropping food\n" +
                    "- delay between mob respawns now increases much faster\n" +
                    "\n" +
                    "- replaced fire elementals with a new enemy: fire drakes\n" +
                    "- replaced fiends with a new enemy: demonic magi\n" +
                    "- reworked the succubus sprite so it looks more demonic\n" +
                    "- removed class-specific mob descriptions in the sewers\n" +
                    "\n" +
                    "_ITEMS & CONSUMABLES_\n" +
                    "\n" +
                    "- interacting with bookshelves now has a chance of identifying one unknown scroll\n" +
                    "- changed the Scroll of Identify into the Scroll of Detect Magic\n" +
                    "- Potions of Invisibility now snuff out the lantern when used\n" +
                    "- Invisibility is also dispelled when lighting the lantern\n" +
                    "- all uncursing effects now remove only one negative level from cursed items\n" +
                    "- items in perfect condition can now be repaired anyway\n" +
                    "\n" +
                    "_BOMBS & FIREARMS_\n" +
                    "\n" +
                    "- all flintlock weapons now require only 1 portion of gunpowder to reload\n" +
                    "- bombs now explode on the next turn after being thrown\n" +
                    "- bomb sticks now require more gunpowder to craft\n" +
                    "- bomb explosions no longer inflict knockback\n" +
                    "- changed damage from explosions from non-elemental to their own element\n" +
                    "\n" +
                    "_WANDS_\n" +
                    "\n" +
                    "- the Wand of Lightning zaps now arc to nearby targets unless the target is standing in the water\n" +
                    "- the Wand of Ice Barrier now simply creates a short wall on the tiles adjacent to the target\n" +
                    "- the Wand of Disintegration shouldn't affect the same target twice anymore\n" +
                    "- stacking runes of the Wand of Firebrand beyond fifth stack only affects their duration now\n" +
                    "\n" +
                    "_DEBUFFS_\n" +
                    "\n" +
                    "- lightning damage from all sources now spreads over the water, just like the Wand of Lightning zaps\n" +
                    "- damage debuff from being Poisoned, Withered, Charmed and Controlled was decreased from 50% to 25%\n" +
                    "- the Vertigo debuff now interrupts your movement when causing you to misstep\n" +
                    "- mobs affected by the Vertigo debuff will now switch their behavior to \"wandering\"\n" +
                    "\n" +
                    "_FIXES_\n" +
                    "\n" +
                    "- fixed cave scorpions appearing one floor earlier than intended\n" +
                    "- fixed the issue that caused Scrolls of Banishment to apply the Tormented debuff instead of Banished\n" +
                    "- fixed the issue that caused cursed Rings of Durability to prevent repair too often\n" +
                    "- fixed visual issues with harpoons\n" +
                    "- fixed a lot of minor issues and typos\n" +

                    "\n" +

                    "Thank you for playing this mod! And, as usual, please report any issues you may find, that'll help me immensely.";

    private RenderedText txtTitle;
    private ScrollPane list;

    public WndChangelog() {

        super();

        if (YetAnotherPixelDungeon.landscape()) {
            resize( WIDTH_L, HEIGHT_L );
        } else {
            resize( WIDTH_P, HEIGHT_P );
        }

        txtTitle = PixelScene.renderText( TXT_TITLE, 8 );
        txtTitle.hardlight( Window.TITLE_COLOR );
        PixelScene.align(txtTitle);
        txtTitle.x = PixelScene.align( PixelScene.uiCamera, (width - txtTitle.width() ) / 2 );
        add( txtTitle );

        list = new ScrollPane( new ChangelogItem( TXT_DESCR, width, txtTitle.height() ) );
        add( list );

        list.setRect( 0, txtTitle.height(), width, height - txtTitle.height() );
        list.scrollTo( 0, 0 );

	}

    private static class ChangelogItem extends Component {

        private final int GAP = 4;

        private RenderedTextMultiline normal;

        public ChangelogItem( String text, int width, float offset ) {
            super();

            normal.text(text);
            normal.maxWidth(width);
            PixelScene.align(normal);

        }

        @Override
        protected void createChildren() {
            normal = PixelScene.renderMultiline( 5 );
            add( normal );
        }

    }
}
