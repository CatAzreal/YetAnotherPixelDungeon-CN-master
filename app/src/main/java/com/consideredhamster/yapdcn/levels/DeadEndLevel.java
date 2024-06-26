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
package com.consideredhamster.yapdcn.levels;

import java.util.Arrays;

import com.watabou.noosa.Scene;
import com.consideredhamster.yapdcn.visuals.Assets;
import com.watabou.utils.Random;

public class DeadEndLevel extends Level {

	private static final int SIZE = 5;
	
	{
		color1 = 0x534f3e;
		color2 = 0xb9d661;
	}
	
	@Override
	public String tilesTex() {
		return Assets.TILES_CAVES;
	}
	
	@Override
	public String waterTex() {
		return Assets.WATER_CAVES;
	}
	
	@Override
	protected boolean build() {

		Arrays.fill( map, Terrain.WALL );
		
		for (int i=2; i < SIZE; i++) {
			for (int j=2; j < SIZE; j++) {
				map[i * WIDTH + j] = Terrain.EMPTY;
			}
		}
		
		for (int i=1; i <= SIZE; i++) {
			map[WIDTH + i] = 
			map[WIDTH * SIZE + i] =
			map[WIDTH * i + 1] =
			map[WIDTH * i + SIZE] =
				Terrain.WATER;
		}
		
		entrance = SIZE * WIDTH + SIZE / 2 + 1;
		map[entrance] = Terrain.ENTRANCE;
		
//		exit = -1;
//        exit = entrance - WIDTH * SIZE + WIDTH;
//        map[exit] = Terrain.LOCKED_EXIT;
		
		map[(SIZE / 2 + 1) * (WIDTH + 1)] = Terrain.SIGN;
		
		return true;
	}

    @Override
	protected void decorate() {
		for (int i=0; i < LENGTH; i++) {
			if (map[i] == Terrain.EMPTY && Random.Int( 10 ) == 0) { 
				map[i] = Terrain.EMPTY_DECO;
			} else if (map[i] == Terrain.WALL && Random.Int( 8 ) == 0) { 
				map[i] = Random.oneOf(
                    Terrain.WALL_DECO, Terrain.WALL_DECO1, Terrain.WALL_DECO2,
                    Terrain.WALL_DECO3, Terrain.WALL_DECO4, Terrain.WALL_DECO5
                );
			}
		}
	}

	@Override
	protected void createMobs() {
	}

	@Override
	protected void createItems() {
	}
	
//	@Override
//	public int randomRespawnCell() {
//		return -1;
//	}

    @Override
    public String tileName( int tile ) {
        return CavesLevel.tileNames(tile);
    }

    @Override
    public String tileDesc( int tile ) {
        return CavesLevel.tileDescs(tile);
    }

    @Override
    public void addVisuals( Scene scene ) {
        CavesLevel.addVisuals( this, scene );
    }
}
