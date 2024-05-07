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

import com.watabou.noosa.Game;
import com.consideredhamster.yapdcn.visuals.windows.WndStory;

public class IntroScene extends PixelScene {

	private static final String TEXT = 	
		"在你之前，也曾经有很多来自地面的英雄向这个地牢进发，然而多数人却是一去不返。更是从未有人成功取得传说中埋藏在地牢深处的Yendor护符。\n\n" +
		"" +
		"你认为自己准备好了，更重要的是，你认为命运女神在向你微笑。" +
		"是时候开始你的冒险了！";
	
	@Override
	public void create() {
		super.create();
		
		add( new WndStory( TEXT ) {
			@Override
			public void hide() {
				super.hide();
				Game.switchScene( InterlevelScene.class );
			}
		} );
		
		fadeIn();
	}
}
