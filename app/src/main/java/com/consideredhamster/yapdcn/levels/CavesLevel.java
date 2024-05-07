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

import com.watabou.noosa.Game;
import com.watabou.noosa.Group;
import com.watabou.noosa.Scene;
import com.watabou.noosa.particles.PixelParticle;
import com.consideredhamster.yapdcn.visuals.Assets;
import com.consideredhamster.yapdcn.Dungeon;
import com.consideredhamster.yapdcn.visuals.DungeonTilemap;
import com.consideredhamster.yapdcn.actors.mobs.npcs.Blacksmith;
import com.consideredhamster.yapdcn.levels.Room.Type;
import com.consideredhamster.yapdcn.levels.painters.Painter;
import com.watabou.utils.PathFinder;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;
import com.watabou.utils.Rect;

import java.util.Arrays;

public class CavesLevel extends RegularLevel {

	{
		color1 = 0x534f3e;
		color2 = 0xb9d661;
		
//		viewDistance = 6;
	}
	
	@Override
	public String tilesTex() {
		return Assets.TILES_CAVES;
	}
	
	@Override
	public String waterTex() {
		return Assets.WATER_CAVES;
	}

    protected boolean[] water() {
        return Patch.generate( feeling == Feeling.WATER ? 0.60f : 0.45f, 6 );
    }

    protected boolean[] grass() {
        return Patch.generate( feeling == Feeling.GRASS ? 0.55f : 0.35f, 3 );
    }
	
	@Override
	protected void assignRoomType() {
		super.assignRoomType();
		
		Blacksmith.Quest.spawn( rooms );
	}
	
	@Override
	protected void decorate() {

        boolean[] passable = new boolean[ LENGTH ];

        for( int i = 0 ; i < LENGTH ; i++ ){
//            passable[ i ] = map[ i ] != Terrain.WALL;
            passable[ i ] =
                    map[ i ] != Terrain.WALL && map[ i ] != Terrain.STATUE && map[ i ] != Terrain.GRATE &&
                    map[ i ] != Terrain.CHASM && map[ i ] != Terrain.WALL_SIGN;
        }

        for( int i = 0 ; i < LENGTH ; i++ ) {
            if( i != entrance && map[ i ] != Terrain.WALL ) {
                if( !PathFinder.buildDistanceMap( i, entrance, passable ) ) {
                    map[ i ] = Terrain.WALL;
                }
            }
        }

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
                        Terrain.WALL_DECO1, Terrain.WALL_DECO2, Terrain.WALL_DECO3
                );

            } else if(
                    map[i] == Terrain.WALL && Random.Int( 10 ) == 0
            ) {

                map[i] = Random.oneOf(
                        Terrain.WALL_DECO, Terrain.WALL_DECO4, Terrain.WALL_DECO5
                );
            }
        }

        for (Room room : rooms) {
            if (room.type != Room.Type.STANDARD) {
                continue;
            }

            if (room.width() <= 3 || room.height() <= 3) {
                continue;
            }
        }

//			for (Room n : room.connected.keySet()) {
//				if ((n.type == Room.Type.STANDARD || n.type == Room.Type.TUNNEL) && Random.Int( 3 ) == 0) {
//					Painter.set( this, room.connected.get( n ), Terrain.EMPTY_DECO );
//				}
//			}

		for (Room r : rooms) {
			if (r.type == Type.STANDARD) {
				for (Room n : r.neighbours) {
					if (n.type == Type.STANDARD && !r.connected.containsKey( n )) {
						Rect w = r.intersect( n );
						if (w.left == w.right && w.bottom - w.top >= 5) {
							
							w.top += 2;
							w.bottom -= 1;
							
							w.right++;
							
							Painter.fill( this, w.left, w.top, 1, w.height(), Terrain.CHASM );
							
						} else if (w.top == w.bottom && w.right - w.left >= 5) {
							
							w.left += 2;
							w.right -= 1;
							
							w.bottom++;
							
							Painter.fill( this, w.left, w.top, w.width(), 1, Terrain.CHASM );
						}
					}
				}
			}
		}

		for (Room room : rooms){
            if( room.type != Room.Type.STANDARD ){
                continue;
            }

            if( room.width() <= 3 || room.height() <= 3 ){
                continue;
            }

            int s = room.square();

            if( Random.Int( s ) > 5 ){
                int corner = ( room.left + 1 ) + ( room.top + 1 ) * WIDTH;
                if( map[ corner - 1 ] == Terrain.WALL && map[ corner - WIDTH ] == Terrain.WALL ){
                    map[ corner ] = map[ corner ] == Terrain.EMPTY_SP ? Terrain.STATUE_SP : Random.oneOf( Terrain.GRATE, Terrain.STATUE, Terrain.WALL );
                }
            }

            if( Random.Int( s ) > 5 ){
                int corner = ( room.right - 1 ) + ( room.top + 1 ) * WIDTH;
                if( map[ corner + 1 ] == Terrain.WALL && map[ corner - WIDTH ] == Terrain.WALL ){
                    map[ corner ] = map[ corner ] == Terrain.EMPTY_SP ? Terrain.STATUE_SP : Random.oneOf( Terrain.GRATE, Terrain.STATUE, Terrain.WALL );
                }
            }

            if( Random.Int( s ) > 5 ){
                int corner = ( room.left + 1 ) + ( room.bottom - 1 ) * WIDTH;
                if( map[ corner - 1 ] == Terrain.WALL && map[ corner + WIDTH ] == Terrain.WALL ){
                    map[ corner ] = map[ corner ] == Terrain.EMPTY_SP ? Terrain.STATUE_SP : Random.oneOf( Terrain.GRATE, Terrain.STATUE, Terrain.WALL );
                }
            }

            if( Random.Int( s ) > 5 ){
                int corner = ( room.right - 1 ) + ( room.bottom - 1 ) * WIDTH;
                if( map[ corner + 1 ] == Terrain.WALL && map[ corner + WIDTH ] == Terrain.WALL ){
                    map[ corner ] = map[ corner ] == Terrain.EMPTY_SP ? Terrain.STATUE_SP : Random.oneOf( Terrain.GRATE, Terrain.STATUE, Terrain.WALL );
                }
            }
        }
	}

    @Override
    public String tileName( int tile ) {
        return CavesLevel.tileNames(tile);
    }

    @Override
    public String tileDesc( int tile ) {
        return CavesLevel.tileDescs(tile);
    }
	
//	@Override
	public static String tileNames( int tile ) {
		switch (tile) {
		case Terrain.GRASS:
			return "荧光地苔";
		case Terrain.HIGH_GRASS:
			return "荧光菌菇";
        case Terrain.WATER:
            return "冰澈水潭";
        case Terrain.STATUE:
            return "石笋";
        case Terrain.GRATE:
            return "乱石堆";
		default:
			return Level.tileNames(tile);
		}
	}
	
//	@Override
	public static String tileDescs( int tile ) {
		switch (tile) {
            case Terrain.ENTRANCE:
                return "通往上层的梯子。";
            case Terrain.EXIT:
                return "通往下层的梯子。";
            case Terrain.HIGH_GRASS:
                return "高耸的菌菇群阻碍了你的视线。";
            case Terrain.WALL_DECO:
                return "岩壁上能隐约看见金属矿脉的纹路，难道是金子？";
            case Terrain.WALL_DECO1:
                return "这面墙上嵌有数个矮人头骨装饰。";
            case Terrain.WALL_DECO2:
                return "这里挂着一面及其原始的部落旗帜。";
            case Terrain.WALL_DECO3:
                return "你能看到墙内隐约嵌着一个大型化石头骨。";
            case Terrain.WALL_DECO4:
            case Terrain.WALL_DECO5:
                return "这些菌菇竟然能长在墙上！";
            case Terrain.BOOKSHELF:
                return "到底会有谁需要在洞窟里摆上这么个书架？不论如何，最好去看看。";
            case Terrain.SHELF_EMPTY:
                return "到底会有谁需要在洞窟里摆上这么个书架？";
            case Terrain.STATUE:
                return "一只石笋从洞窟地表长出。";
            case Terrain.GRATE:
                return "一大堆石块挡住了你的去路。";
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
				scene.add( new Vein( i ) );
			}
		}
	}
	
	private static class Vein extends Group {
		
		private int pos;
		
		private float delay;
		
		public Vein( int pos ) {
			super();
			
			this.pos = pos;
			
			delay = Random.Float( 2 );
		}
		
		@Override
		public void update() {
			
			if (visible = Dungeon.visible[pos]) {
				
				super.update();
				
				if ((delay -= Game.elapsed) <= 0) {
					
					delay = Random.Float();
					
					PointF p = DungeonTilemap.tileToWorld( pos );
					((Sparkle)recycle( Sparkle.class )).reset( 
						p.x + Random.Float( DungeonTilemap.SIZE ), 
						p.y + Random.Float( DungeonTilemap.SIZE ) );
				}
			}
		}
	}
	
	public static final class Sparkle extends PixelParticle {
		
		public void reset( float x, float y ) {
			revive();
			
			this.x = x;
			this.y = y;
			
			left = lifespan = 0.5f;
		}
		
		@Override
		public void update() {
			super.update();
			
			float p = left / lifespan;
			size( (am = p < 0.5f ? p * 2 : (1 - p) * 2) * 2 );
		}
	}

    public static void pregenerate( Level level ) {

        // Just create some patches of empty tiles around the floor before painting any rooms
        boolean[] empty = new boolean[ Level.LENGTH ];

        switch( Dungeon.depth ) {
            case 13:
                chasm = Patch.generate( 0.400f, 2 );
                break;
            case 14:
                empty = Patch.generate( 0.425f, 3 );
                break;
            case 15:
                empty = Patch.generate( 0.450f, 3 );
                break;
            case 16:
                empty = Patch.generate( 0.475f, 4 );
                break;
            case 17:
                empty = Patch.generate( 0.500f, 4 );
                break;

        }

        for( int x = 2 ; x < Level.WIDTH - 2 ; x++ ){
            for( int y = 2 ; y < Level.HEIGHT - 2 ; y++ ){
                int pos = x + y * Level.WIDTH;

                if ( level.map[ pos ] == Terrain.WALL && empty[ pos ] ) {
                    level.map[ pos ] = Terrain.EMPTY;
                }
            }
        }

        for( int w = 0 ; w < 10; w++ ){
            for( int h = 0 ; h < 10 ; h++ ){

                int x = w * 3 + Random.Int( 3 ) + 1;
                int y = h * 3 + Random.Int( 3 ) + 1;

                int pos = x * Level.WIDTH + y ;

                if( level.map[pos] == Terrain.EMPTY ){
                    level.map[ pos ] = Random.oneOf( Terrain.STATUE, Terrain.CHASM, Terrain.GRATE );
                }

            }
        }

    }
}