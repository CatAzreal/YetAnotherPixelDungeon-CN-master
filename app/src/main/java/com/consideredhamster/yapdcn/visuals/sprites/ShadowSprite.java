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
package com.consideredhamster.yapdcn.visuals.sprites;

import com.consideredhamster.yapdcn.visuals.Assets;
import com.watabou.noosa.TextureFilm;

public class ShadowSprite extends MobSprite {

	public ShadowSprite() {
		super();
		
		texture( Assets.TSHADOW );
		
		TextureFilm frames = new TextureFilm( texture, 14, 16 );
		
		idle = new Animation( 2, true );
		idle.frames( frames, 0, 0, 0, 1 );
		
		run = new Animation( 15, false );
		run.frames( frames, 0 );
		
		attack = new Animation( 15, false );
		attack.frames( frames, 2, 3, 3, 0 );
		
		die = new Animation( 10, false );
		die.frames( frames, 4, 5, 6, 7, 8 );
		
		play( run.clone() );
	}

	@Override
	public int blood() {
		return 0x88000000;
	}
}