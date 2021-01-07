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

import com.consideredhamster.yetanotherpixeldungeon.YetAnotherPixelDungeon;
import com.consideredhamster.yetanotherpixeldungeon.scenes.PixelScene;
import com.consideredhamster.yetanotherpixeldungeon.visuals.ui.RenderedTextMultiline;
import com.consideredhamster.yetanotherpixeldungeon.visuals.ui.ScrollPane;
import com.consideredhamster.yetanotherpixeldungeon.visuals.ui.Window;
import com.watabou.noosa.BitmapText;
import com.watabou.noosa.BitmapTextMultiline;
import com.watabou.noosa.RenderedText;
import com.watabou.noosa.ui.Component;

public class WndChangelog extends Window {

	private static final int WIDTH_P	= 128;
	private static final int HEIGHT_P	= 210;

	private static final int WIDTH_L	= 210;
	private static final int HEIGHT_L	= 128;

	private static final String TXT_TITLE	= "另类地牢中文版Beta1";

    private static final String TXT_DESCR =

        "大家好！我是冰杖。\n " +
        "不认识我？没关系，只需要知道我是汉化版本的作者即可。_汉化基于版本0.3.2a，已获得作者ConsideredHamster许可。_\n" +
                "\n" +
                "当前汉化进度：\n" +
                "Actors: 增/减益效果已完成50%汉化，其余部分待处理\n" +
                "Items: 暂无\n" +
                "Levels: 暂无\n" +
                "Scenes: 主要场景已经进行了汉化\n" +
                "Windows: 少数窗口进行了汉化\n" +
                "\n" +
                "Q&A\n" +
                "\n" +
                "Q：为什么还没汉化完就发出来？\n" +
                "A：YAPD采用的是5年前Watabou开发的原版代码，而本次汉化采用的是Evan已经修补更迭数年的SPD基础引擎，" +
                "两者间有许多不兼容处，因此需要交由各位玩家协助debug，目前阶段源码修改和字符串翻译全部由我一人进行，" +
                "专门自测汉化只会拖长开发时间。\n" +
                "\n" +
                "Q：为什么不能像SPD一样将源文本发到平台，将翻译分包给其他志愿者？\n" +
                "A：实际上是可以实现的，但工作量非常庞大，因为YAPD的字符串全部内置在代码层，若要外置则需要提取所有字符串" +
                "并为其挨个命名。\n" +
                "\n" +
                "Q：只有你一个人，翻译质量如何保障？\n" +
                "A：我是在Transifex平台的破碎地牢志愿翻译简体中文小组的审核员，同时在其他游戏本地化上也有基本的经验，" +
                "这样的经验不足以让我涉足翻译行业，但在电子游戏领域的志愿翻译中我的能力完全足够。\n" +
                "\n" +
                "Q：MISPD？\n" +
                "A：肯定会在仓鼠做完YAPD正式多语言本地化前做好0.3beta的，相信我（\n"
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
