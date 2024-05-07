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

import com.consideredhamster.yapdcn.levels.painters.Painter;
import com.watabou.noosa.Scene;
import com.watabou.noosa.particles.Emitter;
import com.watabou.noosa.particles.PixelParticle;
import com.consideredhamster.yapdcn.visuals.Assets;
import com.consideredhamster.yapdcn.Dungeon;
import com.consideredhamster.yapdcn.visuals.DungeonTilemap;
import com.consideredhamster.yapdcn.actors.mobs.npcs.AmbitiousImp;
import com.consideredhamster.yapdcn.levels.Room.Type;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;

import java.util.Arrays;

public class CityLevel extends RegularLevel {

	{
		color1 = 0x4b6636;
		color2 = 0xf2f2f2;

//        viewDistance = 5;
	}
	
	@Override
	public String tilesTex() {
		return Assets.TILES_CITY;
	}
	
	@Override
	public String waterTex() {
		return Assets.WATER_CITY;
	}

    @Override
    protected boolean[] water() {
        return Patch.generate( feeling == Feeling.WATER ? 0.65f : 0.45f, 4 );
    }

    @Override
    protected boolean[] grass() {
        return Patch.generate( feeling == Feeling.GRASS ? 0.60f : 0.40f, 3 );
    }
	
	@Override
	protected void assignRoomType() {
		super.assignRoomType();
		
		for (Room r : rooms) {
			if (r.type == Type.TUNNEL) {
				r.type = Type.PASSAGE;
			}
		}
	}
	
	@Override
	protected void decorate() {

	    for (int i=WIDTH + 1; i < LENGTH - WIDTH - 1; i++) {
            if (map[i] == Terrain.EMPTY) {
                if (Random.Int( 10 ) == 0 ) {
                    map[i] = Terrain.EMPTY_DECO;
                }
            }
        }

        for (int i=0; i < LENGTH ; i++) {
            if (
                    i + WIDTH < LENGTH && map[i] == Terrain.WALL &&
                    !Arrays.asList( Terrain.WALLS ).contains( map[i + WIDTH] ) &&
                    Random.Int( 15 ) == 0
            ) {

                map[i] = Random.oneOf(
                        Terrain.WALL_DECO, Terrain.WALL_DECO1, Terrain.WALL_DECO2
                );

            } else if(
                    map[i] == Terrain.WALL && Random.Int( 10 ) == 0
            ) {

                map[i] = Random.oneOf(
                        Terrain.WALL_DECO3, Terrain.WALL_DECO4, Terrain.WALL_DECO5
                );
            }
        }

        for( Room room : rooms ){
            if( room.type == Room.Type.STANDARD ){
                for (Room.Door door : room.connected.values()) {
                    if( door.type == Room.Door.Type.REGULAR ){

                        int pos = door.y * Level.WIDTH + door.x;

                        boolean clear = true;

                        for( int i : Level.NEIGHBOURSX ) {
                            if( map[ pos + i ] == Terrain.WALL ) {
                                clear = false;
                            }
                        }

                        if( clear && Random.Int( 5 ) == 0 ) {
                            for( int i : Level.NEIGHBOURS4 ) {
                                if( map[ pos + i ] == Terrain.WALL ) {
                                    map[ pos + i ] = Terrain.GRATE;
                                }
                            }
                        }
                    }
                }
            }
        }

//        for (int i=WIDTH; i < LENGTH - WIDTH; i++) {
//            if (map[i] == Terrain.WALL && map[i + WIDTH] != Terrain.WALL && Random.Int( 20 ) == 0 ) {
//                map[i] = Terrain.WALL_DECO2;
//            }
//        }
//
//        for (int i=0; i < LENGTH; i++) {
//            if (map[i] == Terrain.EMPTY && Random.Int( 10 ) == 0){
//                map[ i ] = Terrain.EMPTY_DECO;
//            } else if (map[i] == Terrain.WALL && Random.Int( 8 ) == 0) {
//                map[i] = Terrain.WALL_DECO;
//            }
//        }
	}
	
	@Override
	protected void createItems() {
		super.createItems();
		
		AmbitiousImp.Quest.spawn(this, roomExit);
	}

    @Override
    public String tileName( int tile ) {
        return CityLevel.tileNames(tile);
    }

    @Override
    public String tileDesc( int tile ) {
        return CityLevel.tileDescs(tile);
    }

//	@Override
	public static String tileNames( int tile ) {
		switch (tile) {
		case Terrain.WATER:
			return "异色水潭";
		case Terrain.HIGH_GRASS:
			return "茂盛花朵";
		default:
			return Level.tileNames(tile);
		}
	}
	
//	@Override
	public static String tileDescs(int tile) {
		switch (tile) {
            case Terrain.ENTRANCE:
                return "通向下层的斜坡。";
            case Terrain.EXIT:
                return "通向上层的斜坡。";
            case Terrain.EMPTY_DECO:
            case Terrain.WALL_DECO3:
            case Terrain.WALL_DECO4:
                return "这里缺了一些地砖。";
            case Terrain.WALL_DECO:
                return "这里有个仍在燃烧的锻炉。";
            case Terrain.WALL_DECO1:
            case Terrain.WALL_DECO2:
                return "这里挂着许多武器，当然都是观赏装饰用的。";
            case Terrain.EMPTY_SP:
                return "厚实的地毯覆盖其上。";
            case Terrain.STATUE:
            case Terrain.STATUE_SP:
                return "这座雕像刻画出了一位摆出英勇姿态的矮人。";
            case Terrain.BOOKSHELF:
                return "不同科目的书籍填满了整个书架。里面会不会藏着些用得上的东西？";
            case Terrain.SHELF_EMPTY:
                return "不同科目的书籍填满了整个书架。";
            default:
                return Level.tileDescs(tile);
		}
	}
	
	@Override
	public void addVisuals( Scene scene ) {
		super.addVisuals( scene );
		addVisuals( this, scene );
	}
	
	public static void addVisuals( Level level, Scene scene ) {
		for (int i=0; i < LENGTH; i++) {
			if (level.map[i] == Terrain.WALL_DECO) {
				scene.add( new Smoke( i ) );
			}
		}
	}
	
	private static class Smoke extends Emitter {
		
		private int pos;
		
		private static final Emitter.Factory factory = new Factory() {
			
			@Override
			public void emit( Emitter emitter, int index, float x, float y ) {
				SmokeParticle p = (SmokeParticle)emitter.recycle( SmokeParticle.class );
				p.reset( x, y );
			}
		};
		
		public Smoke( int pos ) {
			super();
			
			this.pos = pos;
			
			PointF p = DungeonTilemap.tileCenterToWorld( pos );
			pos( p.x - 4, p.y - 2, 4, 0 );
			
			pour( factory, 0.2f );
		}
		
		@Override
		public void update() {
			if (visible = Dungeon.visible[pos]) {
				super.update();
			}
		}
	}
	
	public static final class SmokeParticle extends PixelParticle {
		
		public SmokeParticle() {
			super();
			
			color( 0x000000 );
			speed.set( Random.Float( 8 ), -Random.Float( 8 ) );
		}
		
		public void reset( float x, float y ) {
			revive();
			
			this.x = x;
			this.y = y;
			
			left = lifespan = 2f;
		}
		
		@Override
		public void update() {
			super.update();
			float p = left / lifespan;
			am = p > 0.8f ? 1 - p : p * 0.25f;
			size( 8 - p * 4 );
		}
	}

    public static void pregenerate( Level level ) {

        // First, we fill everything with empty tiles
        Painter.fill( level, 1, 1, WIDTH - 2, HEIGHT - 2, Terrain.EMPTY );

        // Then, we generate chasms, which grow in size and number as you descend deeper
        boolean[] chasm = new boolean[ Level.LENGTH ];

        switch( Dungeon.depth ) {
//            case 19:
//                chasm = Patch.generate( 0.20f, 2 );
//                break;
            case 20:
                chasm = Patch.generate( 0.35f, 3 );
                break;
            case 21:
                chasm = Patch.generate( 0.45f, 4 );
                break;
            case 22:
                chasm = Patch.generate( 0.55f, 5 );
                break;
            case 23:
                chasm = Patch.generate( 0.65f, 6 );
                break;

        }

        for ( int i = 0 ; i < LENGTH ; i++ ){
            if ( level.map[ i ] == Terrain.EMPTY && chasm[ i ] ) {
                level.map[ i ] = Terrain.CHASM;
            }
        }

        // Then we seed everything with random stuff to add some visual variety to the empty floors
        for( int w = 0 ; w < 3; w++ ) {
            for( int h = 0 ; h < 3 ; h++ ) {

                int x = w * 8 + Random.Int( 6 ) + 2 ;
                int y = h * 8 + Random.Int( 6 ) + 2 ;

                int pos = x * Level.WIDTH + y ;

                switch( Random.Int( 10 ) ) {

                    case 0:

                        level.map[ pos ] = Terrain.STATUE;
                        break;

                    case 1:

                        level.map[ pos ] = Terrain.PEDESTAL;
                        break;

                    case 2:

                        level.map[ pos ] = Terrain.EMPTY_WELL;
                        break;

                    case 3:

                        level.map[ pos ] = Terrain.BARRICADE;
                        break;

                    case 4:

                        level.map[ pos ] = Terrain.STATUE_SP;
                        break;

                    case 5:

                        level.map[ pos ] = Terrain.WALL;
                        break;

                    case 6:

                        for( int c : Level.NEIGHBOURS9 ) {
                            level.map[ pos + c ] = Terrain.WALL;
                        }

                        break;

                    case 7:

                        level.map[ pos ] = Random.oneOf(
                            Terrain.PEDESTAL, Terrain.EMPTY_WELL, Terrain.EMPTY
                        );

                        for( int c : Level.NEIGHBOURS4 ) {
                            level.map[ pos + c ] = Terrain.STATUE;
                        }

                        break;

                    case 8:

                        for( int c : Level.NEIGHBOURSX ) {
                            level.map[ pos + c ] = Terrain.STATUE;
                        }

                        for( int c : Level.NEIGHBOURS4 ) {
                            level.map[ pos + c ] = Terrain.EMPTY;
                        }

                        level.map[ pos ] = Random.oneOf(
                                Terrain.PEDESTAL, Terrain.EMPTY_WELL, Terrain.EMPTY
                        );

                        break;

                    case 9:

                        for( int c : Level.NEIGHBOURS8 ) {
                            level.map[ pos + c ] = Terrain.EMPTY_SP;
                        }

                        level.map[ pos ] = Random.oneOf(
                                Terrain.PEDESTAL, Terrain.STATUE_SP, Terrain.EMPTY_SP
                        );

                        break;
                }
            }
        }
    }
}