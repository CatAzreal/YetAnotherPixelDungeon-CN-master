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
import com.watabou.noosa.RenderedText;
import com.watabou.noosa.ui.Component;

public class WndChangelog extends Window {

	private static final int WIDTH_P	= 128;
	private static final int HEIGHT_P	= 160;

	private static final int WIDTH_L	= 160;
	private static final int HEIGHT_L	= 128;

	private static final String TXT_TITLE	= "另类地牢中文版";

    private static final String TXT_DESCR =

        "大家好！我是冰杖。\n\n " +
        "不认识我？没关系，只需要知道我是汉化版本的作者即可。_汉化基于版本0.3.2a，已获得作者ConsideredHamster许可。_\n" +
                "\n" +
                "目前游戏所有部分都应被汉化，若出现并非人名或游戏名词的英文请截图并联系作者qq377844252进行反馈\n" +
                "若有明显的误译或意义不明之处，或者闪退情况也欢迎联系\n" +
                "之后如果有空的话，也许会对这个版本做些QoL改动，不过这些都是后话\n" +
                "\n" +
                "鸣谢：\n" +
                "Alexstrasza和Lynn对汉化后期剩余字串的及时翻译，不然我懒癌一发作这版本大概可以再拖几个月(\n\n" +
                "Ømicrónrg9提供的YAPDCN图标(没错这个也是我懒得做)\n\n" +
                "六花和老梁在版本最终版前提供的两段字串翻译\n" +
                "\n\n\n" +
                "_如我先前所说，仓鼠做完YAPD正式多语言本地化前MISPD0.3beta一定会出的，相信我！（_\n"
    ;

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

    public void onMenuPressed() {
        hide();
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
