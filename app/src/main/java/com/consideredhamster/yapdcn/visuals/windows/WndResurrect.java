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
import com.watabou.noosa.Game;
import com.consideredhamster.yapdcn.Element;
import com.consideredhamster.yapdcn.Dungeon;
import com.consideredhamster.yapdcn.Rankings;
import com.consideredhamster.yapdcn.Statistics;
import com.consideredhamster.yapdcn.actors.hero.Hero;
import com.consideredhamster.yapdcn.items.misc.Ankh;
import com.consideredhamster.yapdcn.scenes.InterlevelScene;
import com.consideredhamster.yapdcn.scenes.PixelScene;
import com.consideredhamster.yapdcn.visuals.sprites.ItemSprite;
import com.consideredhamster.yapdcn.visuals.ui.RedButton;
import com.consideredhamster.yapdcn.visuals.ui.Window;

public class WndResurrect extends Window {
	
	private static final String TXT_MESSAGE = "You died, but the ankh you were carrying can give you yet another chance to live. Will you accept it?";
	private static final String TXT_YES		= "Yes, I will fight!";
	private static final String TXT_NO		= "No, I give up";
	
	private static final int WIDTH		= 120;
	private static final int BTN_HEIGHT	= 20;
	private static final float GAP		= 2;
	
	public static WndResurrect instance;
	public static Object killedBy;
	public static Element killedWith;

	public WndResurrect( final Ankh ankh, Object killedBy, Element killedWith ) {
		
		super();
		
		instance = this;
		WndResurrect.killedBy = killedBy;
		WndResurrect.killedWith = killedWith;

		IconTitle titlebar = new IconTitle();
		titlebar.icon( new ItemSprite( ankh.image(), null ) );
		titlebar.label( ankh.name() );
		titlebar.setRect( 0, 0, WIDTH, 0 );
		add( titlebar );
		
		RenderedTextMultiline message = PixelScene.renderMultiline( TXT_MESSAGE, 6 );
		message.maxWidth(WIDTH);
		PixelScene.align(message);
		float y = titlebar.bottom() + GAP;
		message.setPos(0,y);
		add( message );
		
		RedButton btnYes = new RedButton( TXT_YES ) {
			@Override
			protected void onClick() {
				hide();
				
				Statistics.ankhsUsed++;

				InterlevelScene.mode = InterlevelScene.Mode.RESURRECT ;

                ankh.detach( Dungeon.hero.belongings.backpack );

				Game.switchScene( InterlevelScene.class );
			}
		};
		btnYes.setRect( 0, y + message.height() + GAP, WIDTH, BTN_HEIGHT );
		add( btnYes );
		
		RedButton btnNo = new RedButton( TXT_NO ) {
			@Override
			protected void onClick() {
				hide();
				
				Rankings.INSTANCE.submit( false );
				Hero.reallyDie( WndResurrect.killedBy, WndResurrect.killedWith );
			}
		};
		btnNo.setRect( 0, btnYes.bottom() + GAP, WIDTH, BTN_HEIGHT );
		add( btnNo );
		
		resize( WIDTH, (int)btnNo.bottom() );
	}
	
	@Override
	public void destroy() {
		super.destroy();
		instance = null;
	}
	
	@Override
	public void onBackPressed() {
	}
}
