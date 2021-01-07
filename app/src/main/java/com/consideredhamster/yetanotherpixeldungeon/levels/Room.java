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
package com.consideredhamster.yetanotherpixeldungeon.levels;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

import com.consideredhamster.yetanotherpixeldungeon.YetAnotherPixelDungeon;
import com.consideredhamster.yetanotherpixeldungeon.levels.painters.*;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;
import com.watabou.utils.Graph;
import com.watabou.utils.Point;
import com.watabou.utils.Random;
import com.watabou.utils.Rect;

public class Room extends Rect implements Graph.Node, Bundlable {
	
	public HashSet<Room> neighbours = new HashSet<Room>();
	public HashMap<Room, Door> connected = new HashMap<Room, Door>();
	
	public int distance;
	public int price = 1;
	
	public static enum Type {
		NULL( null ),
		STANDARD	( StandardPainter.class ),
		ENTRANCE	( EntrancePainter.class ),
		EXIT		( ExitPainter.class ),
		BOSS_EXIT	( BossExitPainter.class ),
		TUNNEL		( TunnelPainter.class ),
		PASSAGE		( PassagePainter.class ),
		SHOP		( ShopPainter.class ),
		BLACKSMITH	( BlacksmithPainter.class ),
		TREASURY	( TreasuryPainter.class ),
		ARMORY		( ArmoryPainter.class ),
		LIBRARY		( LibraryPainter.class ),
		LABORATORY	( LaboratoryPainter.class ),
		VAULT		( VaultPainter.class ),
		TRAPS		( TrapsPainter.class ),
		STORAGE		( StoragePainter.class ),
		BARRICADED	( BarricadedPainter.class ),
		MAGIC_WELL	( WellPainter.class ),
		GARDEN		( GardenPainter.class ),
		CRYPT		( CryptPainter.class ),
		STATUE		( StatuePainter.class ),
		POOL		( PoolPainter.class ),
		RAT_KING	( RatKingPainter.class ),
		WEAK_FLOOR	( WeakFloorPainter.class ),
		PIT			( PitPainter.class );
		
		private Method paint;
		
		private Type( Class<? extends Painter> painter ) {
			try {
				paint = painter.getMethod( "paint", Level.class, Room.class );
			} catch (Exception e) {
				paint = null;
			}
		}
		
		public void paint( Level level, Room room ) {
			try {
				paint.invoke( null, level, room );
			} catch (Exception e) {
				YetAnotherPixelDungeon.reportException(e);
			}
		}
	};
	
	public static final ArrayList<Type> SPECIALS = new ArrayList<Type>( Arrays.asList(
        Type.ARMORY, Type.MAGIC_WELL, Type.CRYPT, Type.POOL, Type.GARDEN, Type.LIBRARY,
        Type.TREASURY, Type.TRAPS, Type.STORAGE, Type.STATUE, Type.LABORATORY, Type.VAULT,
        Type.BARRICADED
	) );
	
	public Type type = Type.NULL;
	
	public int random() {
		return random( 0 );
	}
	
	public int random( int m ) {
		int x = Random.IntRange( left + 1 + m, right - 1 - m );
		int y = Random.IntRange(top + 1 + m, bottom - 1 - m);
		return x + y * Level.WIDTH;
	}

    public int random_top() {
        int x = Random.IntRange( left + 1, right - 1);
        int y = top;
        return x + y * Level.WIDTH;
    }
	
	public void addNeighbour(Room other) {
		
		Rect i = intersect( other );
		if ((i.width() == 0 && i.height() >= 3) || 
			(i.height() == 0 && i.width() >= 3)) {
			neighbours.add(other);
			other.neighbours.add(this);
		}
		
	}
	
	public void connect( Room room ) {
		if (!connected.containsKey( room )) {	
			connected.put( room, null );
			room.connected.put( this, null );			
		}
	}
	
	public Door entrance() {
		return connected.values().iterator().next();
	}
	
	public boolean inside( int p ) {
		int x = p % Level.WIDTH;
		int y = p / Level.WIDTH;
		return x > left && y > top && x < right && y < bottom;
	}

    public Point center() {
        return new Point(
                (left + right) / 2 + (((right - left) & 1) == 1 ? Random.Int( 2 ) : 0),
                (top + bottom) / 2 + (((bottom - top) & 1) == 1 ? Random.Int( 2 ) : 0) );
    }

    public ArrayList<Integer> cells() {

        ArrayList<Integer> result = new ArrayList();

        for( int x = left ; x <= right ; x++ ) {
            for( int y = top ; y <= bottom ; y++ ){
                result.add( x + y * Level.WIDTH );
            }
        }

        return result;
    }
	
	// **** Graph.Node interface ****

	@Override
	public int distance() {
		return distance;
	}

	@Override
	public void distance( int value ) {
		distance = value;
	}
	
	@Override
	public int price() {
		return price;
	}

	@Override
	public void price( int value ) {
		price = value;
	}

	@Override
	public Collection<Room> edges() {
		return neighbours;
	} 
	
	// FIXME: use proper string constants
	
	@Override
	public void storeInBundle( Bundle bundle ) {	
		bundle.put( "left", left );
		bundle.put( "top", top );
		bundle.put( "right", right );
		bundle.put( "bottom", bottom );
		bundle.put( "type", type.toString() );
	}
	
	@Override
	public void restoreFromBundle( Bundle bundle ) {
		left = bundle.getInt( "left" );
		top = bundle.getInt( "top" );
		right = bundle.getInt( "right" );
		bottom = bundle.getInt( "bottom" );		
		type = Type.valueOf( bundle.getString( "type" ) );
	}
	
	public static void shuffleTypes() {
		int size = SPECIALS.size();
		for (int i=0; i < size - 1; i++) {
			int j = Random.Int( i, size );
			if (j != i) {
				Type t = SPECIALS.get( i );
				SPECIALS.set( i, SPECIALS.get( j ) );
				SPECIALS.set( j, t );
			}
		}
	}
	
	public static void useType( Type type ) {
		if (SPECIALS.remove( type )) {
			SPECIALS.add( type );
		}
	}
	
	private static final String ROOMS	= "rooms";
	
	public static void restoreRoomsFromBundle( Bundle bundle ) {
		if (bundle.contains( ROOMS )) {
			SPECIALS.clear();
			for (String type : bundle.getStringArray( ROOMS )) {
				SPECIALS.add( Type.valueOf( type ));
			}
		} else {
			shuffleTypes();
		}
	}
	
	public static void storeRoomsInBundle( Bundle bundle ) {
		String[] array = new String[SPECIALS.size()];
		for (int i=0; i < array.length; i++) {
			array[i] = SPECIALS.get( i ).toString();
		}
		bundle.put( ROOMS, array );
	}
	
	public static class Door extends Point {
		
		public static enum Type {
			EMPTY, TUNNEL, REGULAR, UNLOCKED, HIDDEN, BARRICADE, LOCKED
		}
		public Type type = Type.EMPTY;
		
		public Door( int x, int y ) {
			super( x, y );
		}
		
		public void set( Type type ) {
			if (type.compareTo( this.type ) > 0) {
				this.type = type;
			}
		}
	}
}
