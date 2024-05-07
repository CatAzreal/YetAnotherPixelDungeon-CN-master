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

public class WndTranslateLog extends Window {

	private static final int WIDTH_P	= 128;
	private static final int HEIGHT_P	= 170;

	private static final int WIDTH_L	= 170;
	private static final int HEIGHT_L	= 128;

	private static final String TXT_TITLE	= "YAPD v0.3.3 CN v1.34 翻译声明";

    private static final String TXT_DESCR =
            "大家好！我是冰杖。\n\n " +
            "不认识我？没关系，只需要知道我是汉化版本的作者即可。_汉化基于最新版本0.3.3，已获得作者ConsideredHamster许可。_\n" +
            "\n" +
            "是的，你没看错，仓鼠时隔近五年更新了，我也有理由把这个老项目重新掏出来翻新一下。\n" +
            "加了不少新内容，具体改动请参考翻译过的更新日志(点击主界面右下角版本号)\n" +
            "由于YAPD中文化的模式和破碎略有不同(它不自带本地化框架)，所以本次制作可能出现一些bug，届时请截图并联系作者qq377844252进行反馈\n" +
            "033初译，除了有虫可捉外应该都翻译齐了\n" +
            "131新增敌人面板翻译和序章描述翻译，132新增敌人免疫/抗性/弱点翻译\n" +
            "\n" +
            "鸣谢：\n" +
            "Alexstrasza和Lynn对032汉化后期剩余字串的及时翻译\n\n" +
            "Ømicrónrg9提供的YAPDCN图标\n\n" +
            "\n\n\n" +
            "_虽然MISPD0.3.0beta和YAPD的多语言化都没有出，但我相信MISPD0.3.0beta还是更有希望的！（_"
    ;

    private RenderedText txtTitle;
    private ScrollPane list;

    public WndTranslateLog() {

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

            height = normal.height() + offset;

            PixelScene.align(normal);
        }

        @Override
        protected void createChildren() {
            normal = PixelScene.renderMultiline( 5 );
            add( normal );
        }

    }
}
