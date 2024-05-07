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

import com.consideredhamster.yapdcn.actors.mobs.npcs.Wandmaker;
import com.consideredhamster.yapdcn.levels.painters.Painter;
import com.watabou.noosa.Game;
import com.watabou.noosa.Scene;
import com.watabou.noosa.particles.Emitter;
import com.watabou.noosa.particles.PixelParticle;
import com.consideredhamster.yapdcn.visuals.Assets;
import com.consideredhamster.yapdcn.Dungeon;
import com.consideredhamster.yapdcn.visuals.DungeonTilemap;
import com.consideredhamster.yapdcn.scenes.GameScene;
import com.watabou.utils.ColorMath;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;
import com.watabou.utils.Rect;

import java.util.Arrays;

public class SewerLevel extends RegularLevel {

	{
		color1 = 0x48763c;
		color2 = 0x59994a;
	}
	
	@Override
	public String tilesTex() {
		return Assets.TILES_SEWERS;
	}
	
	@Override
	public String waterTex() {
		return Assets.WATER_SEWERS;
	}

    @Override
    protected boolean[] water() {
        return Patch.generate( feeling == Feeling.WATER ? 0.60f : 0.45f, 5 );
    }

    @Override
    protected boolean[] grass() {
        return Patch.generate( feeling == Feeling.GRASS ? 0.60f : 0.40f, 4 );
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

                if( map[i + WIDTH] == Terrain.WATER && Random.Int( 2 ) == 0 ){
                    map[i] = Random.oneOf(
                            Terrain.WALL_DECO
                    );
                } else {
                    map[ i ] = Random.oneOf(
                            Terrain.WALL_DECO1, Terrain.WALL_DECO2
                    );
                }

            } else if(
                    map[i] == Terrain.WALL && Random.Int( 10 ) == 0
            ) {

                map[i] = Random.oneOf(
                    Terrain.WALL_DECO3, Terrain.WALL_DECO4, Terrain.WALL_DECO5
                );
            }
        }

        for (Room r : rooms) {
            if (r.type == Room.Type.STANDARD) {
                for (Room n : r.neighbours) {
                    if (n.type == Room.Type.STANDARD && !r.connected.containsKey( n )) {
                        Rect w = r.intersect( n );
                        if ( w.left == w.right && w.bottom - w.top <= 3) {

                            w.top += 1;
                            w.right++;

                            Painter.fill( this, w.left, w.top, 1, w.height(), Terrain.BARRICADE );

                        } else if (w.top == w.bottom && w.right - w.left <= 3) {

                            w.left += 1;
                            w.bottom++;

                            Painter.fill( this, w.left, w.top, w.width(), 1, Terrain.BARRICADE );
                        }
                    }
                }
            }
        }
	}
	
	@Override
	protected void createMobs() {
		super.createMobs();

        Wandmaker.Quest.spawn( this, roomEntrance );

	}
	
	@Override
	public void addVisuals( Scene scene ) {
		super.addVisuals(scene);
		addVisuals(this, scene);
	}
	
	public static void addVisuals( Level level, Scene scene ) {
		for (int i=0; i < LENGTH; i++) {
			if (level.map[i] == Terrain.WALL_DECO) {
				scene.add( new Sink( i ) );
			}
		}
	}

    @Override
    public String tileName( int tile ) {
        return SewerLevel.tileNames(tile);
    }

    @Override
    public String tileDesc( int tile ) {
        return SewerLevel.tileDescs(tile);
    }
	
//	@Override
	public static String tileNames( int tile ) {
		switch (tile) {
		case Terrain.WATER:
			return "Murky water";
		default:
			return Level.tileNames(tile);
		}
	}

//	@Override
	public static String tileDescs(int tile) {
		switch (tile) {
			case Terrain.EMPTY_DECO:
				return "潮湿且发黄的苔藓覆盖其上。";
            case Terrain.WALL_DECO:
            case Terrain.WALL_DECO1:
                return "这面墙上有个排水口，不过很明显它的大小并不允许你钻进去。";
            case Terrain.WALL_DECO2:
                return "这里有一个通风口，偶尔呼吸到地上的新鲜空气还是挺不错的。";
            case Terrain.WALL_DECO3:
            case Terrain.WALL_DECO4:
            case Terrain.WALL_DECO5:
                return "潮湿且发绿的苔藓覆盖在墙面上。";
			case Terrain.BOOKSHELF:
				return "这个书架上尽是些脏污的书籍。里面会不会藏着些用得上的东西？";
			case Terrain.SHELF_EMPTY:
				return "这个书架上尽是些脏污的书籍。";
		default:
			return Level.tileDescs(tile);
		}
	}
	
	private static class Sink extends Emitter {
		
		private int pos;
		private float rippleDelay = 0;
		
		private static final Emitter.Factory factory = new Factory() {
			
			@Override
			public void emit( Emitter emitter, int index, float x, float y ) {
				WaterParticle p = (WaterParticle)emitter.recycle( WaterParticle.class );
				p.reset( x, y );
			}
		};
		
		public Sink( int pos ) {
			super();
			
			this.pos = pos;
			
			PointF p = DungeonTilemap.tileCenterToWorld( pos );
			pos( p.x - 2, p.y + 1, 4, 0 );
			
			pour( factory, 0.05f );
		}
		
		@Override
		public void update() {
			if (visible = Dungeon.visible[pos]) {
				
				super.update();
				
				if ((rippleDelay -= Game.elapsed) <= 0) {
					GameScene.ripple( pos + WIDTH ).y -= DungeonTilemap.SIZE / 2;
					rippleDelay = Random.Float( 0.2f, 0.3f );
				}
			}
		}
	}

	public static final class WaterParticle extends PixelParticle {
		
		public WaterParticle() {
			super();
			
			acc.y = 50;
			am = 0.5f;
			
			color( ColorMath.random( 0xb6ccc2, 0x3b6653 ) );
			size( 2 );
		}
		
		public void reset( float x, float y ) {
			revive();
			
			this.x = x;
			this.y = y;
			
			speed.set( Random.Float( -2, +2 ), 0 );
			
			left = lifespan = 0.5f;
		}
	}
}
