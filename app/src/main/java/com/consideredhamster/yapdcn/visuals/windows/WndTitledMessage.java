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
import com.watabou.noosa.BitmapTextMultiline;
import com.watabou.noosa.Image;
import com.watabou.noosa.ui.Component;
import com.consideredhamster.yapdcn.YetAnotherPixelDungeon;
import com.consideredhamster.yapdcn.scenes.PixelScene;
import com.consideredhamster.yapdcn.visuals.ui.Window;

public class WndTitledMessage extends Window {

	private static final int WIDTH_P	= 120;
	private static final int WIDTH_L	= 144;
	
	private static final int GAP	= 2;
	
	private RenderedTextMultiline normal;
	private BitmapTextMultiline highlighted;
	
	public WndTitledMessage( Image icon, String title, String message ) {
		
		this( new IconTitle( icon, title ), message );

	}
	
	public WndTitledMessage( Component titlebar, String message ) {
		
		super();
		
		int width = YetAnotherPixelDungeon.landscape() ? WIDTH_L : WIDTH_P;
		
		titlebar.setRect( 0, 0, width, 0 );
		add( titlebar );
		
		normal = PixelScene.renderMultiline( message, 6 );
		normal.maxWidth(width);
		PixelScene.align(normal);
		float x = titlebar.left();
		float y = titlebar.bottom() + GAP;
		normal.setPos(x,y);
		add( normal );

		
		resize( width, (int)(y + normal.height()) );
	}
}
