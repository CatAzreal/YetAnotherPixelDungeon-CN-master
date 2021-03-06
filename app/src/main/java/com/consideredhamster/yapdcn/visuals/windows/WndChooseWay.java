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

import com.consideredhamster.yapdcn.visuals.ui.RenderedTextMultiline;
import com.consideredhamster.yapdcn.actors.hero.HeroSubClass;
import com.consideredhamster.yapdcn.items.misc.TomeOfMastery;
import com.consideredhamster.yapdcn.scenes.PixelScene;
import com.consideredhamster.yapdcn.visuals.sprites.ItemSprite;
import com.consideredhamster.yapdcn.visuals.ui.RedButton;
import com.consideredhamster.yapdcn.visuals.ui.Window;
import com.consideredhamster.yapdcn.misc.utils.Utils;

public class WndChooseWay extends Window {
	
	private static final String TXT_MESSAGE	= "你要选择哪一条典范之道？";
	private static final String TXT_CANCEL	= "之后再做决定";
	
	private static final int WIDTH		= 120;
	private static final int BTN_HEIGHT	= 18;
	private static final float GAP		= 2;
	
	public WndChooseWay( final TomeOfMastery tome, final HeroSubClass way1, final HeroSubClass way2 ) {
		
		super();
		
		IconTitle titlebar = new IconTitle();
		titlebar.icon( new ItemSprite( tome.image(), null ) );
		titlebar.label( tome.name() );
		titlebar.setRect( 0, 0, WIDTH, 0 );
		add( titlebar );

		Highlighter hl = new Highlighter( way1.desc() + "\n\n" + way2.desc() + "\n\n" + TXT_MESSAGE );
		
		RenderedTextMultiline normal = PixelScene.renderMultiline( hl.text, 6 );
		normal.maxWidth(WIDTH);
		PixelScene.align(normal);
		float x = titlebar.left();
		float y = titlebar.bottom() + GAP;
		normal.setPos(x,y);
		add( normal );

//		if (hl.isHighlighted()) {
//			normal.mask = hl.inverted();
//
//			BitmapTextMultiline highlighted = PixelScene.createMultiline( hl.text, 6 );
//			highlighted.maxWidth = normal.maxWidth;
//			highlighted.measure();
//			highlighted.x = normal.x;
//			highlighted.y = normal.y;
//			add( highlighted );
//
//			highlighted.mask = hl.mask;
//			highlighted.hardlight( TITLE_COLOR );
//		}
		
		RedButton btnWay1 = new RedButton( way1.title() ) {
			@Override
			protected void onClick() {
				hide();
				tome.choose( way1 );
			}
		};
		btnWay1.setRect( 0, y + normal.height() + GAP, (WIDTH - GAP) / 2, BTN_HEIGHT );
		add( btnWay1 );
		
		RedButton btnWay2 = new RedButton( Utils.capitalize( way2.title() ) ) {
			@Override
			protected void onClick() {
				hide();
				tome.choose( way2 );
			}
		};
		btnWay2.setRect( btnWay1.right() + GAP, btnWay1.top(), btnWay1.width(), BTN_HEIGHT );
		add( btnWay2 );
		
		RedButton btnCancel = new RedButton( TXT_CANCEL ) {
			@Override
			protected void onClick() {
				hide();
			}
		};
		btnCancel.setRect( 0, btnWay2.bottom() + GAP, WIDTH, BTN_HEIGHT );
		add( btnCancel );
		
		resize( WIDTH, (int)btnCancel.bottom() );
	}
}
