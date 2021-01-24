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

	private static final String TXT_TITLE	= "另类地牢中文版Beta5.1";

    private static final String TXT_DESCR =

        "大家好！我是冰杖。\n " +
        "不认识我？没关系，只需要知道我是汉化版本的作者即可。_汉化基于版本0.3.2a，已获得作者ConsideredHamster许可。_\n" +
                "\n" +
                "当前汉化进度(若有遗漏字串欢迎截图，协助我查漏补缺)：\n" +
                "除了教程和书架额外彩蛋外全部汉化，由于后面速度加快，部分字串翻译可能会有缺漏，极少数可能存在错译，欢迎各位玩家指正\n" +
                "Q&A\n" +
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
