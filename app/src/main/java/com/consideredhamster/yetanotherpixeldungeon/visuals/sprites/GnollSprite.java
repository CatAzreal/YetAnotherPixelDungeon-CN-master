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
package com.consideredhamster.yetanotherpixeldungeon.visuals.sprites;

import com.watabou.noosa.TextureFilm;
import com.consideredhamster.yetanotherpixeldungeon.visuals.Assets;

public class GnollSprite extends MobSprite {

    public GnollSprite() {
		super();
		
		texture( Assets.GNOLL );
		
		TextureFilm frames = new TextureFilm( texture, 12, 15 );
		
		idle = new Animation( 2, true );
		idle.frames( frames, 0, 0, 0, 1, 0, 0, 1, 1 );
		
		run = new Animation( 12, true );
		run.frames( frames, 4, 5, 6, 7 );
		
		attack = new Animation( 12, false );
		attack.frames( frames, 2, 3, 0 );
		
		die = new Animation( 12, false );
		die.frames( frames, 8, 9, 10 );
		
		play( idle );
	}

//    @Override
//    public void attack( int cell ) {
//        if (!Level.adjacent(cell, ch.pos)) {
//
//            cellToAttack = cell;
//            turnTo( ch.pos , ((Mob)ch).target.pos );
//            play(cast);
//
//        } else {
//
//            super.attack( cell );
//
//        }
//    }
//
//    @Override
//    public void onComplete( Animation anim ) {
//        if (anim == cast) {
//
//
//            idle();
//        } else {
//            super.onComplete( anim );
//        }
//    }
}
