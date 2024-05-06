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
package com.consideredhamster.yapdcn.levels.painters;

import com.consideredhamster.yapdcn.Dungeon;
import com.consideredhamster.yapdcn.items.keys.SkeletonKey;
import com.consideredhamster.yapdcn.levels.Level;
import com.consideredhamster.yapdcn.levels.Room;
import com.consideredhamster.yapdcn.levels.Terrain;

public class ExitPainter extends Painter {

	public static void paint( Level level, Room room ) {

		fill( level, room, Terrain.WALL );
		fill( level, room, 1, Terrain.EMPTY );

        for (Room.Door door : room.connected.values()) {
            door.set( Room.Door.Type.REGULAR );
        }

        if( Dungeon.chapter() < 5 ){

            level.exit = room.random( 1 );
            set( level, level.exit, Terrain.EXIT );

        } else {

            level.exit = room.random( 2 );

            fill( level, level.exit % Level.WIDTH - 1, level.exit / Level.WIDTH - 1, 3, 1, Terrain.WALL );

            set( level, level.exit - 1, Terrain.WALL_DECO );
            set( level, level.exit + 1, Terrain.WALL_DECO );

            if( Dungeon.depth > 25 ){
                set( level, level.exit, Terrain.LOCKED_EXIT );
                level.addItemToSpawn( new SkeletonKey() );
            } else {
                set( level, level.exit, Terrain.UNLOCKED_EXIT );
            }

        }
	}
	
}
