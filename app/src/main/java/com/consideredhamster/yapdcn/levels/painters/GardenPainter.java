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

import com.watabou.utils.Point;
import com.consideredhamster.yapdcn.Dungeon;
import com.consideredhamster.yapdcn.items.Generator;
import com.consideredhamster.yapdcn.items.Heap;
import com.consideredhamster.yapdcn.levels.Level;
import com.consideredhamster.yapdcn.levels.Room;
import com.consideredhamster.yapdcn.levels.Terrain;
import com.watabou.utils.Random;

public class GardenPainter extends Painter {

	public static void paint( Level level, Room room ) {
		
		fill( level, room, Terrain.WALL );
		fill( level, room, 1, Terrain.HIGH_GRASS );
		fill( level, room, 2, Terrain.WATER );

        for (Point door : room.connected.values()) {
            if (door.x == room.left) {
                set( level, door.x + 1, door.y, Terrain.EMPTY );
            } else if (door.x == room.right) {
                set( level, door.x - 1, door.y, Terrain.EMPTY );
            } else if (door.y == room.top) {
                set( level, door.x, door.y + 1, Terrain.EMPTY );
            } else if (door.y == room.bottom) {
                set( level, door.x , door.y - 1, Terrain.EMPTY );
            }
        }

        if( Dungeon.level.feeling != Level.Feeling.ASHES ){

            int chance = 9 - Dungeon.chapter();

            for( int i = room.left + 1 ; i < room.right ; i++ ){
                if( Random.Int( chance ) == 0 ){
                    level.drop( Generator.random( Generator.Category.HERB ), ( room.top + 1 ) * Level.WIDTH + i, true ).type = Heap.Type.HEAP;
                }

                if( Random.Int( chance ) == 0 ){
                    level.drop( Generator.random( Generator.Category.HERB ), ( room.bottom - 1 ) * Level.WIDTH + i, true ).type = Heap.Type.HEAP;
                }
            }

            for( int i = room.top + 2 ; i < room.bottom - 1 ; i++ ){
                if( Random.Int( chance ) == 0 ){
                    level.drop( Generator.random( Generator.Category.HERB ), i * Level.WIDTH + room.left + 1, true ).type = Heap.Type.HEAP;
                }

                if( Random.Int( chance ) == 0 ){
                    level.drop( Generator.random( Generator.Category.HERB ), i * Level.WIDTH + room.right - 1, true ).type = Heap.Type.HEAP;
                }
            }

            room.entrance().set( Dungeon.depth > 1 ? Room.Door.Type.HIDDEN : Room.Door.Type.REGULAR );

        } else {

            room.entrance().set( Room.Door.Type.EMPTY );

        }
    }
}
