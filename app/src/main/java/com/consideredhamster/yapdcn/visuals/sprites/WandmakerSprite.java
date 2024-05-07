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

import com.consideredhamster.yapdcn.visuals.effects.Speck;
import com.consideredhamster.yapdcn.visuals.effects.particles.ElmoParticle;
import com.watabou.noosa.TextureFilm;
import com.consideredhamster.yapdcn.visuals.Assets;
import com.consideredhamster.yapdcn.actors.Char;
import com.consideredhamster.yapdcn.visuals.effects.Shield;

public class WandmakerSprite extends MobSprite {
	
	private Shield shield;
	
	public WandmakerSprite() {
		super();
		
		texture( Assets.MAKER );
		
		TextureFilm frames = new TextureFilm( texture, 12, 14 );
		
		idle = new Animation( 10, true );
		idle.frames( frames, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 2, 3, 3, 3, 3, 3, 3, 2, 1 );
		
		run = new Animation( 20, true );
		run.frames( frames, 0 );
		
		die = new Animation( 20, false );
		die.frames( frames, 0 );
		
		play( idle );
	}
	
	@Override
	public void link( Char ch ) {
		super.link( ch );
		
//		if (shield == null) {
//			parent.add( shield = new Shield( this ) );
//		}
	}
	
	@Override
	public void die() {
		super.die();
		
//		if (shield != null) {
//			shield.putOut();
//		}

		emitter().start( ElmoParticle.FACTORY, 0.03f, 60 );
	}
	

}
