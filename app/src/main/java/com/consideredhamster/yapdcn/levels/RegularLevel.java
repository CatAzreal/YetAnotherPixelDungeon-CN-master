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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import com.consideredhamster.yapdcn.Bones;
import com.consideredhamster.yapdcn.Dungeon;
import com.consideredhamster.yapdcn.actors.Actor;
import com.consideredhamster.yapdcn.actors.mobs.Bestiary;
import com.consideredhamster.yapdcn.actors.mobs.Mob;
import com.consideredhamster.yapdcn.actors.mobs.Piranha;
import com.consideredhamster.yapdcn.actors.mobs.Statue;
import com.consideredhamster.yapdcn.items.Generator;
import com.consideredhamster.yapdcn.items.misc.Gold;
import com.consideredhamster.yapdcn.items.Heap;
import com.consideredhamster.yapdcn.items.Item;
import com.consideredhamster.yapdcn.levels.Room.Type;
import com.consideredhamster.yapdcn.levels.painters.*;
import com.watabou.utils.Bundle;
import com.watabou.utils.Graph;
import com.watabou.utils.Random;
import com.watabou.utils.Rect;

public abstract class RegularLevel extends Level {

    protected HashSet<Room> rooms = new HashSet<Room>();
	
	protected Room roomEntrance;
	protected Room roomExit;
	
	protected ArrayList<Room.Type> specials;
	
	public int secretDoors;
	
	@Override
	protected boolean build() {
		
		if (!initRooms()) {
			return false;
		}

		int distance;
		int retry = 0;
		int minDistance = (int)Math.sqrt( rooms.size() );
		do {
			do {
				roomEntrance = Random.element( rooms );
			} while (roomEntrance.width() < 4 || roomEntrance.height() < 4);

			if( Dungeon.chapter() < 5 ){
                do{
                    roomExit = Random.element( rooms );
                } while( roomExit == roomEntrance || roomExit.width() < 4 || roomExit.height() < 4 );
            } else {
                do{
                    roomExit = Random.element( rooms );
                } while( roomExit == roomEntrance || roomExit.width() < 6 || roomExit.height() < 5 );
            }
	
			Graph.buildDistanceMap( rooms, roomExit );
			distance = roomEntrance.distance();
			
			if (retry++ > 10) {
				return false;
			}
			
		} while (distance < minDistance);
		
		roomEntrance.type = Type.ENTRANCE;
        roomExit.type = Type.EXIT;
		
		HashSet<Room> connected = new HashSet<Room>();
		connected.add( roomEntrance );
		
		Graph.buildDistanceMap( rooms, roomExit );
		List<Room> path = Graph.buildPath( rooms, roomEntrance, roomExit );
		
		Room room = roomEntrance;
		for (Room next : path) {
			room.connect( next );
			room = next;
			connected.add( room );
		}
		
		Graph.setPrice( path, roomEntrance.distance );
		
		Graph.buildDistanceMap( rooms, roomExit );
		path = Graph.buildPath( rooms, roomEntrance, roomExit );
		
		room = roomEntrance;
		for (Room next : path) {
			room.connect( next );
			room = next;
			connected.add( room );
		}
		
		int nConnected = (int)(rooms.size() * Random.Float( 0.5f, 0.7f ));
		while (connected.size() < nConnected) {
			Room cr = Random.element( connected );
			Room or = Random.element( cr.neighbours);
			if (!connected.contains( or )) {
				cr.connect( or );
				connected.add( or );
			}
		}
		
		if (Dungeon.shopOnLevel()) {
			Room shop = null;
			for (Room r : roomEntrance.connected.keySet()) {
				if (r.connected.size() == 1 && r.width() >= 6 && r.height() >= 5) {
					shop = r;
					break;
				}
			}
			
			if (shop == null) {
				return false;
			} else {
				shop.type = Room.Type.SHOP;
			}
		}
		
		specials = new ArrayList<Room.Type>( Room.SPECIALS );
		if (Dungeon.bossLevel( Dungeon.depth + 1 )) {
			specials.remove( Room.Type.WEAK_FLOOR );
		}

		assignRoomType();
		
		paint();

        placeSign( roomEntrance );

        if( feeling == Level.Feeling.BOOKS ){
            for( Room r : rooms ){
                if( r.type == Type.STANDARD ){
                    StandardPainter.paintBooks( this, r );
                }
            }
        }

        paintWater();
		paintGrass();
		
		placeTraps();
		
		return true;
	}
	
	protected boolean initRooms() {
		rooms = new HashSet<Room>();
		split( new Rect( 0, 0, WIDTH - 1, HEIGHT - 1 ) );
		
		if ( rooms.size() < 7 + Dungeon.chapter() ) {
			return false;
		}
		
		Room[] ra = rooms.toArray( new Room[0] );
		for (int i=0; i < ra.length-1; i++) {
			for (int j=i+1; j < ra.length; j++) {
				ra[i].addNeighbour(ra[j]);
			}
		}
		
		return true;
	}
	
	protected void assignRoomType() {
		
		int chapter = Dungeon.chapter();
		
		for (Room r : rooms) {
			if (r.type == Type.NULL && 
				r.connected.size() == 1) {

				if (specials.size() > 0 &&
					r.width() > 3 && r.height() > 3
                  && ( Random.Int( 6 - chapter ) == 0 ||
                    Dungeon.depth % 6 == 2 && specials.contains( Type.LABORATORY ) ||
                    Dungeon.depth % 6 == 4 && specials.contains( Type.MAGIC_WELL ) )
                ) {

                    if (Dungeon.depth % 6 == 2 && specials.contains( Type.LABORATORY )) {

						r.type = Type.LABORATORY;
						
					} else if (Dungeon.depth % 6 == 4 && specials.contains( Type.MAGIC_WELL )) {

						r.type = Type.MAGIC_WELL;

					} else {

						int n = specials.size();
						r.type = specials.get( Random.Int( n ) );

					}
					
					Room.useType( r.type );
					specials.remove( r.type );
					
				} else if (Random.Int( 2 ) == 0){

					HashSet<Room> neighbours = new HashSet<Room>();
					for (Room n : r.neighbours) {
						if (!r.connected.containsKey( n ) && 
							!Room.SPECIALS.contains( n.type ) &&
							n.type != Type.PIT) {
							
							neighbours.add(n);
						}
					}
					if (neighbours.size() > 1) {
						r.connect( Random.element( neighbours ) );
					}
				}
			}
		}
		
		int count = 0;
		for (Room r : rooms) {
			if (r.type == Type.NULL) {
				int connections = r.connected.size();
				if (connections == 0) {
					
				} else if (Random.Int( connections * connections ) == 0) {
					r.type = Type.STANDARD;
					count++;
				} else {
					r.type = Type.TUNNEL; 
				}
			}
		}
		
		while (count < 4) {
			Room r = randomRoom( Type.TUNNEL, 1 );
			if (r != null) {
				r.type = Type.STANDARD;
				count++;
			}
		}
	}
	
	protected void paintWater() {
		boolean[] lake = water();
		for (int i=0; i < LENGTH; i++) {
			if (map[i] == Terrain.EMPTY && lake[i]) {
				map[i] = Terrain.WATER;
			}
		}
	}
	
	protected void paintGrass() {
		boolean[] grass = grass();
		
		if (feeling == Feeling.GRASS) {
			
			for (Room room : rooms) {
				if (room.type != Type.NULL && room.type != Type.PASSAGE && room.type != Type.TUNNEL) {
					grass[(room.left + 1) + (room.top + 1) * WIDTH] = true;
					grass[(room.right - 1) + (room.top + 1) * WIDTH] = true;
					grass[(room.left + 1) + (room.bottom - 1) * WIDTH] = true;
					grass[(room.right - 1) + (room.bottom - 1) * WIDTH] = true;
				}
			}
		}

		for (int i=WIDTH+1; i < LENGTH-WIDTH-1; i++) {
			if (map[i] == Terrain.EMPTY && grass[i]) {

				if( feeling != Feeling.ASHES ) {

					int count = 1;

					for (int n : NEIGHBOURS8) {
						if (grass[i + n]) {
							count++;
						}
					}

					map[i] = (Random.Float() < count / 12f) ? Terrain.HIGH_GRASS : Terrain.GRASS;

				} else {
					map[i] = Terrain.EMBERS;
				}
			}
		}
	}
	
	protected abstract boolean[] water();
	protected abstract boolean[] grass();
	
	protected void placeTraps() {
		
		int nTraps = nTraps();
		float[] trapChances = trapChances();
		
		for (int i=0; i < nTraps; i++) {
			
			int trapPos = randomTrapCell();

			if ( trapPos > -1 ) {
				switch (Random.chances( trapChances )) {
				case 0:
					map[trapPos] = Terrain.SECRET_TOXIC_TRAP;
					break;
				case 1:
					map[trapPos] = Terrain.SECRET_FIRE_TRAP;
					break;
				case 2:
					map[trapPos] = Terrain.SECRET_BOULDER_TRAP;
					break;
				case 3:
					map[trapPos] = Terrain.SECRET_POISON_TRAP;
					break;
				case 4:
					map[trapPos] = Terrain.SECRET_ALARM_TRAP;
					break;
				case 5:
					map[trapPos] = Terrain.SECRET_LIGHTNING_TRAP;
					break;
				case 6:
					map[trapPos] = Terrain.SECRET_BLADE_TRAP;
					break;
				case 7:
					map[trapPos] = Terrain.SECRET_SUMMONING_TRAP;
					break;
                case 8:
                    map[trapPos] = Terrain.INACTIVE_TRAP;
                    break;
				}
			}
		}
	}
	
	protected int nTraps() {

		if( Dungeon.depth == 1 )
			return 0;

		if( feeling == Feeling.TRAPS )
			return Dungeon.chapter() + Random.Int( 5 );

		if( feeling == Feeling.WATER )
			return Random.Int( Dungeon.chapter() + 1 );

		return Random.Int( Dungeon.chapter() + 1 ) + Random.Int( 5 );

	}
	
	protected float[] trapChances() {
		float[] chances = { 1, 1, 1, 1, 1, 1, 1, 1, 1 };
		return chances;
	}

	protected void placeSign( Room room ) {

        // gotta find an empty place for the sign

        ArrayList<Integer> candidates = new ArrayList<>(  );

        for( int i = room.left + 1 ; i < room.right ; i++ ) {
            int pos = i + room.top * Level.WIDTH;
            if( map[pos] == Terrain.WALL){
                candidates.add( pos );
            }
        }

        for (Room.Door door : room.connected.values()) {
            Integer pos = door.x + door.y * Level.WIDTH;

            if( candidates.contains( pos ) ) {
                candidates.remove( pos );
            }
        }

        if( !candidates.isEmpty() ){
//            set( Random.element( candidates ), Terrain.WALL_SIGN );
            map[ Random.element( candidates ) ] = Terrain.WALL_SIGN;
        }
    }
	
	protected int minRoomSize = 7;
	protected int maxRoomSize = 9;
	
	protected void split( Rect rect ) {
		
		int w = rect.width();
		int h = rect.height();
		
		if (w > maxRoomSize && h < minRoomSize) {

			int vw = Random.Int( rect.left + 3, rect.right - 3 );
			split( new Rect( rect.left, rect.top, vw, rect.bottom ) );
			split( new Rect( vw, rect.top, rect.right, rect.bottom ) );
			
		} else 
		if (h > maxRoomSize && w < minRoomSize) {

			int vh = Random.Int( rect.top + 3, rect.bottom - 3 );
			split( new Rect( rect.left, rect.top, rect.right, vh ) );
			split( new Rect( rect.left, vh, rect.right, rect.bottom ) );
			
		} else 	
		if ((Math.random() <= (minRoomSize * minRoomSize / rect.square()) && w <= maxRoomSize && h <= maxRoomSize) || w < minRoomSize || h < minRoomSize) {

			rooms.add( (Room)new Room().set( rect ) );
			
		} else {
			
			if (Random.Float() < (float)(w - 2) / (w + h - 4)) {
				int vw = Random.Int( rect.left + 3, rect.right - 3 );
				split( new Rect( rect.left, rect.top, vw, rect.bottom ) );
				split( new Rect( vw, rect.top, rect.right, rect.bottom ) );
			} else {
				int vh = Random.Int( rect.top + 3, rect.bottom - 3 );
				split( new Rect( rect.left, rect.top, rect.right, vh ) );
				split( new Rect( rect.left, vh, rect.right, rect.bottom ) );
			}
			
		}
	}
	
	protected void paint() {
		
		for (Room r : rooms) {
			if (r.type != Type.NULL) {
				placeDoors( r );
				r.type.paint( this, r );
//			} else {
//				if (feeling == Feeling.CHASM && Random.Int( 2 ) == 0) {
//					Painter.fill( this, r, Terrain.WALL );
//				}
			}
		}
		
		for (Room r : rooms) {
			paintDoors( r );
		}
	}
	
	private void placeDoors( Room r ) {
		for (Room n : r.connected.keySet()) {
			Room.Door door = r.connected.get( n );
			if (door == null) {
				
				Rect i = r.intersect( n );
				if (i.width() == 0) {
					door = new Room.Door( 
						i.left, 
						Random.IntRange( i.top + 1, i.bottom - 1 ) );
				} else {
					door = new Room.Door( 
						Random.IntRange( i.left + 1, i.right - 1 ),
						i.top);
				}

				r.connected.put( n, door );
				n.connected.put( r, door );
			}
		}
	}
	
	protected void paintDoors( Room r ) {

		for (Room n : r.connected.keySet()) {

			if (joinRooms( r, n )) {
				continue;
			}
			
			Room.Door d = r.connected.get( n );
			int door = d.x + d.y * WIDTH;
			
			switch (d.type) {
			case EMPTY:
				map[door] = Terrain.EMPTY;
				break;
			case TUNNEL:
				map[door] = tunnelTile();
				break;
			case REGULAR:
                map[door] = Terrain.DOOR_CLOSED;
				break;
			case UNLOCKED:
				map[door] = Terrain.DOOR_CLOSED;
				break;
			case HIDDEN:
				map[door] = Terrain.DOOR_ILLUSORY;
				break;
			case BARRICADE:
				map[door] = Terrain.BARRICADE;
				break;
			case LOCKED:
				map[door] = Terrain.LOCKED_DOOR;
				break;
			}
        }
	}
	
	protected boolean joinRooms( Room r, Room n ) {
		
		if (r.type != Room.Type.STANDARD || n.type != Room.Type.STANDARD) {
			return false;
		}
		
		Rect w = r.intersect(n);
		if (w.left == w.right) {
			
			if (w.bottom - w.top < 3) {
				return false;
			}
			
			if (w.height() == Math.max( r.height(), n.height() )) {
				return false;
			}
			
			if (r.width() + n.width() > maxRoomSize) {
				return false;
			}
			
			w.top += 1;
			w.bottom -= 0;
			
			w.right++;
			
			Painter.fill( this, w.left, w.top, 1, w.height(), Terrain.EMPTY );
			
		} else {
			
			if (w.right - w.left < 3) {
				return false;
			}
			
			if (w.width() == Math.max( r.width(), n.width() )) {
				return false;
			}
			
			if (r.height() + n.height() > maxRoomSize) {
				return false;
			}
			
			w.left += 1;
			w.right -= 0;
			
			w.bottom++;
			
			Painter.fill( this, w.left, w.top, w.width(), 1, Terrain.EMPTY );
		}
		
		return true;
	}
	
	@Override
	public int nMobs() {
		return 5 + Math.max( Dungeon.depth % 6, Dungeon.chapter() );
	}
	
	@Override
	protected void createMobs() {

		int nMobs = nMobs();

        if ( feeling == Feeling.SWARM ) {
            nMobs += Dungeon.chapter();
        }

		for (int i=0; i < nMobs; i++) {

            Mob mob;

            int pos = randomRespawnCell();

            if( pos > -1 ){

                if( feeling == Feeling.GRASS && map[ pos ] == Terrain.HIGH_GRASS && Random.Int( 4 ) == 0 ){

                    mob = new Statue();
                    mob.state = mob.PASSIVE;

                } else if( feeling == Feeling.WATER && map[ pos ] == Terrain.WATER && Random.Int( 3 ) == 0 ){

                    mob = new Piranha();
                    mob.state = mob.SLEEPING;

                } else {

                    mob = Bestiary.mob( Dungeon.depth );
                    mob.state = feeling == Feeling.SWARM && Random.Int( 3 ) == 0 ? mob.WANDERING : mob.SLEEPING;

                }

                mob.pos = pos;

                mobs.add( mob );

                Actor.occupyCell( mob );
            }
		}
	}

    @Override
    public ArrayList<Integer> getPassableCellsList() {

        ArrayList<Integer> result = new ArrayList<>();

        for (Room room : rooms) {
            if( room.type == Type.STANDARD ){
                for( Integer cell : room.cells() ){
                    if( !solid[ cell ] && passable[ cell ] && Actor.findChar( cell ) == null ){
                        result.add( cell );
                    }
                }
            }
        }

        return result;
    }
	
	@Override
	public int randomDestination() {

        int cell = -1;

        if (!rooms.isEmpty())
        {
            while (true) {

                Room room = Random.element(rooms);
                if (room == null || room.type == Type.SHOP || room.type == Type.BLACKSMITH ) {
                    continue;
                }

                cell = room.random();
                if (Level.mob_passable[cell]) {
                    return cell;
                }

            }
        }

        return cell;
	}
	
	@Override
	protected void createItems() {

        int chapter = Dungeon.chapter();

        int itemsMin = 1;

        int itemsMax = chapter + 1;

		for ( int i = Random.IntRange( itemsMin, itemsMax ) ; i > 0 ; i-- ) {

            drop( Generator.randomEquipment(), randomDropCell(), true ).randomizeType();

        }

        for ( int i = Random.IntRange( itemsMin, itemsMax ) - 1 ; i > 0 ; i-- ) {

            drop( Generator.randomComestible(), randomDropCell(), true ).randomizeType();

        }

        for ( int i = Random.IntRange( itemsMin, itemsMax ) + 1 ; i > 0 ; i-- ) {

            drop( Generator.random( Generator.Category.GOLD ), randomDropCell(), true ).randomizeType();

        }

        if( feeling == Feeling.ASHES ) {

            for ( int i = Random.IntRange( itemsMin, itemsMax ); i > 0 ; i-- ) {

                drop(Generator.randomEquipment(), randomDropCell( false ), true).type = Random.Int( 6 - chapter ) == 0 ? Heap.Type.BONES_CURSED : Heap.Type.BONES ;

            }
        }

        if( feeling == Feeling.TRAPS ) {

            for ( int i = Random.IntRange( itemsMin, itemsMax ); i > 0 ; i-- ) {

                drop(new Gold().random(), randomDropCell( false ), true).type = Random.Int( 6 - chapter ) == 0 ? Heap.Type.CHEST_MIMIC : Heap.Type.CHEST ;

            }
        }

		for (Item item : itemsToSpawn) {
//			int cell = randomDropCell();
//			if (item instanceof ScrollOfUpgrade) {
//				while (map[cell] == Terrain.FIRE_TRAP || map[cell] == Terrain.SECRET_FIRE_TRAP) {
//					cell = randomDropCell();
//				}
//			}
			drop( item, randomDropCell(), true ).randomizeType();
		}
		
		Item item = Bones.get();
		if (item != null) {
			drop( item, randomDropCell( false ), true ).type = Heap.Type.BONES_CURSED;
		}

        for (int i=0; i < LENGTH; i++) {

            // chances are 5/7.5/10/12.5/15% to spawn a herb per high grass tile
            if (map[i] == Terrain.HIGH_GRASS && heaps.get( i ) == null && Random.Int( 40 ) <= chapter ) {
                drop( Generator.random( Generator.Category.HERB ), i, true ).type = Heap.Type.HEAP;
            }
        }

    }

	protected Room randomRoom( Room.Type type, int tries ) {
		for (int i=0; i < tries; i++) {
			Room room = Random.element( rooms );
			if (room.type == type) {
				return room;
			}
		}
		return null;
	}
	
	public Room room( int pos ) {
		for (Room room : rooms) {
			if (room.type != Type.NULL && room.inside( pos )) {
				return room;
			}
		}
		
		return null;
	}

    protected int randomTrapCell() {
        for (int i=0; i < 10; i++) {
            Room room = randomRoom( Room.Type.STANDARD, 1 );
            if (room != null) {
                int pos = room.random();
                if (map[pos] == Terrain.EMPTY || map[pos] == Terrain.EMPTY_DECO || map[pos] == Terrain.EMBERS || map[pos] == Terrain.GRASS) {
                    return pos;
                }
            }
        }
        return -1;
    }

    protected int randomDropCell() {
        return randomDropCell( true );
    }

	protected int randomDropCell( boolean ignoreHeaps ) {
		while ( true ) {
			Room room = randomRoom( Room.Type.STANDARD, 1 );
			if ( room != null ) {
				int pos = room.random();
				if ( mob_passable[pos] && ( ignoreHeaps || heaps.get( pos ) == null ) ) {
					return pos;
				}
			}
		}
	}
	
	@Override
	public int pitCell() {
		for (Room room : rooms) {
			if (room.type == Type.PIT) {
				return room.random();
			}
		}
		
		return super.pitCell();
	}
	
	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle( bundle );
		bundle.put( "rooms", rooms );
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle( bundle );
		
		rooms = new HashSet<Room>( (Collection<? extends Room>) (Object) bundle.getCollection( "rooms" ) );
		for (Room r : rooms) {
			if (r.type == Type.WEAK_FLOOR) {
				weakFloorCreated = true;
				break;
			}
		}
	}
	
}
