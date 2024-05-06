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
package com.consideredhamster.yapdcn.visuals.effects;

import com.consideredhamster.yapdcn.levels.Level;
import com.consideredhamster.yapdcn.visuals.DungeonTilemap;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;

public class RippleShock extends Image {

	private static final float TIME_TO_FADE = 0.5f;

	private float time;

	public RippleShock() {
		super( Effects.get( Effects.Type.SHOCK ) );
	}
	
	public void reset( int p ) {
		revive();
		
		x = (p % Level.WIDTH) * DungeonTilemap.SIZE;
		y = (p / Level.WIDTH) * DungeonTilemap.SIZE;
		
		origin.set( width / 2, height / 2 );
		
		time = TIME_TO_FADE;
	}
	
	@Override
	public void update() {
		super.update();
		
		if ((time -= Game.elapsed) <= 0) {
			kill();
		} else {
			float p = time / TIME_TO_FADE;

//            if( Random.Int( 3 ) == 0 ){
//                scale.y *= -1;
//            }
//
//            if( Random.Int( 3 ) == 0 ){
//                scale.x *= -1;
//            }

			alpha( p );
		}
	}
}