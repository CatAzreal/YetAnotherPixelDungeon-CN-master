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
import com.watabou.noosa.Image;
import com.consideredhamster.yapdcn.Badges;
import com.consideredhamster.yapdcn.visuals.effects.BadgeBanner;
import com.consideredhamster.yapdcn.scenes.PixelScene;
import com.consideredhamster.yapdcn.visuals.ui.Window;

public class WndBadge extends Window {
	
	private static final int WIDTH = 120;
	private static final int MARGIN = 4;
	
	public WndBadge( Badges.Badge badge ) {
		
		super();
		
		Image icon = BadgeBanner.image( badge.image );
		icon.scale.set( 2 );
		add( icon );
		
		RenderedTextMultiline info = PixelScene.renderMultiline( badge.description, 6 );
		info.maxWidth(WIDTH - MARGIN * 2);

		
		float w = Math.max( icon.width(), info.width() ) + MARGIN * 2;
		
		icon.x = (w - icon.width()) / 2;
		icon.y = MARGIN;
		
		float pos = icon.y + icon.height() + MARGIN;
		info.setPos((w - info.width()) / 2 ,pos);
		add(info);
		pos += info.height();
		PixelScene.align(info);


//		for (BitmapText line : info.new LineSplitter().split()) {
//			line.measure();
//			line.x = PixelScene.align( (w - line.width()) / 2 );
//			line.y = PixelScene.align( pos );
//			add( line );
//
//			pos += line.height();
//		}

		resize( (int)w, (int)(pos + MARGIN) );
		
		BadgeBanner.highlight( icon, badge.image );
	}
}
