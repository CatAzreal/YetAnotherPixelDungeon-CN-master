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
package com.consideredhamster.yetanotherpixeldungeon.visuals.effects;

import com.watabou.noosa.Game;
import com.watabou.noosa.Gizmo;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.CharSprite;

public class EnragedFX extends Gizmo {

	private float phase;

	private CharSprite target;

	public EnragedFX(CharSprite target) {
		super();

		this.target = target;
		phase = 0;
	}
	
	@Override
	public void update() {
		super.update();

		if ( ( phase += Game.elapsed ) < 1.0f ) {
			target.tint( 1.0f, 0.0f, 0.0f, phase * 0.2f );
		} else {
			target.tint( 1.0f, 0.0f, 0.0f, ( 2.0f - phase ) * 0.2f );
		}

        if( phase > 2.0f )
            phase -= 2.0f;
	}

	public void calm() {

		target.resetColorAlpha();
		killAndErase();

	}
	
	public static EnragedFX fury( CharSprite sprite ) {
		
		EnragedFX frenzy = new EnragedFX( sprite );
		sprite.parent.add( frenzy );
		
		return frenzy;
	}
}
