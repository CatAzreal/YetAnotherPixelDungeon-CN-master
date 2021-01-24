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
package com.consideredhamster.yapdcn.scenes;

import com.consideredhamster.yapdcn.visuals.ui.RenderedTextMultiline;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.consideredhamster.yapdcn.visuals.Assets;
import com.consideredhamster.yapdcn.Dungeon;
import com.consideredhamster.yapdcn.ResultDescriptions;
import com.consideredhamster.yapdcn.visuals.effects.Flare;
import com.consideredhamster.yapdcn.visuals.effects.Speck;
import com.consideredhamster.yapdcn.visuals.ui.RedButton;
import com.watabou.utils.Random;

public class AmuletScene extends PixelScene {

	private static final String TXT_EXIT	= "带我回去！";
	private static final String TXT_STAY	= "我还没打算用它";
	
	private static final int WIDTH			= 120;
	private static final int BTN_HEIGHT		= 18;
	private static final float SMALL_GAP	= 2;
	private static final float LARGE_GAP	= 8;
	
	private static final String TXT = 
		"传说中的Yendor护符。经过了重重磨难，你终于将它握在了自己的手中。\n\n" +
        "你能感受到源自护符的强大能量。利用它的力量，你的任何愿望都能成真...不过它只能使用一次。\n\n" +
        "那么，现在你可以立刻使用护符的力量，将自己带回地面，将这个噩梦般的地牢永远抛在身后。\n\n" +
        "或者你可以尝试以自己的力量离开地牢，将Yendor护符的奇迹之力留作他用。\n\n" +
        "你的决定是？";

    private static final String TXT_SHORT =
            "那么，你的决定是？";
	
	public static boolean noText = false;
	
	private Image amulet;
	
	@Override
	public void create() {
		super.create();
		
		RenderedTextMultiline text = null;

        text = renderMultiline( !noText ? TXT : TXT_SHORT , 6 );
        text.maxWidth (WIDTH);
        PixelScene.align(text);
        add( text );
		
		amulet = new Image( Assets.AMULET );
		add( amulet );
		
		RedButton btnExit = new RedButton( TXT_EXIT ) {
			@Override
			protected void onClick() {
				Dungeon.win( ResultDescriptions.WIN );
				Dungeon.deleteGame( Dungeon.hero.heroClass, true );
				Game.switchScene( noText ? TitleScene.class : RankingsScene.class );
			}
		};
		btnExit.setSize( WIDTH, BTN_HEIGHT );
		add( btnExit );
		
		RedButton btnStay = new RedButton( TXT_STAY ) {
			@Override
			protected void onClick() {
				onBackPressed();
			}
		};
		btnStay.setSize( WIDTH, BTN_HEIGHT );
		add( btnStay );
		
		float height;
//		if (noText) {
//			height = amulet.height + LARGE_GAP + btnExit.height() + SMALL_GAP + btnStay.height();
//
//			amulet.x = align( (Camera.main.width - amulet.width) / 2 );
//			amulet.y = align( (Camera.main.height - height) / 2 );
//
//			btnExit.setPos( (Camera.main.width - btnExit.width()) / 2, amulet.y + amulet.height + LARGE_GAP );
//			btnStay.setPos( btnExit.left(), btnExit.bottom() + SMALL_GAP );
			
//		} else {
			height = amulet.height + LARGE_GAP + text.height() + LARGE_GAP + btnExit.height() + SMALL_GAP + btnStay.height();

			amulet.x = align( (Camera.main.width - amulet.width) / 2 );
			amulet.y = align( (Camera.main.height - height) / 2 );

			float x =  align( (Camera.main.width - text.width()) / 2 );
			float y = amulet.y + amulet.height + LARGE_GAP;
			text.setPos(x,y);

			btnExit.setPos( (Camera.main.width - btnExit.width()) / 2, y + text.height() + LARGE_GAP );
			btnStay.setPos( btnExit.left(), btnExit.bottom() + SMALL_GAP );
//		}

		new Flare( 8, 48 ).color( 0xFFDDBB, true ).show( amulet, 0 ).angularSpeed = +30;
		
		fadeIn();
	}
	
	@Override
	protected void onBackPressed() {
		InterlevelScene.mode = InterlevelScene.Mode.CONTINUE;
		Game.switchScene( InterlevelScene.class );
	}
	
	private float timer = 0;
	
	@Override
	public void update() {
		super.update();
		
		if ((timer -= Game.elapsed) < 0) {
			timer = Random.Float( 0.5f, 5f );
			
			Speck star = (Speck)recycle( Speck.class );
			star.reset( 0, amulet.x + 10.5f, amulet.y + 5.5f, Speck.DISCOVER );
			add( star );
		}
	}
}
