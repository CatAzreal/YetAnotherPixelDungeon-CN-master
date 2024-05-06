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
package com.consideredhamster.yapdcn.visuals.effects.particles;

import com.watabou.noosa.particles.Emitter;
import com.watabou.noosa.particles.PixelParticle;
import com.watabou.noosa.particles.Emitter.Factory;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;

public class EnergyParticle extends PixelParticle {
	
	public static final Emitter.Factory FACTORY = new Factory() {	
		@Override
		public void emit( Emitter emitter, int index, float x, float y ) {
			((EnergyParticle)emitter.recycle( EnergyParticle.class )).reset( x, y );
		}
		@Override
		public boolean lightMode() {
			return true;
		};
	};

    public static final Emitter.Factory FACTORY_BLUE = new Factory() {
        @Override
        public void emit( Emitter emitter, int index, float x, float y ) {
            ((BlueEnergyParticle)emitter.recycle( BlueEnergyParticle.class )).reset( x, y );
        }
        @Override
        public boolean lightMode() {
            return true;
        };
    };

    public static final Emitter.Factory FACTORY_WHITE = new Factory() {
        @Override
        public void emit( Emitter emitter, int index, float x, float y ) {
            ((WhiteEnergyParticle)emitter.recycle( WhiteEnergyParticle.class )).reset( x, y );
        }
        @Override
        public boolean lightMode() {
            return true;
        };
    };
	
	public static final Emitter.Factory FACTORY_PURPLE = new Factory() {
		@Override
		public void emit( Emitter emitter, int index, float x, float y ) {
			((PurpleEnergyParticle)emitter.recycle( PurpleEnergyParticle.class )).reset( x, y );
		}
		@Override
		public boolean lightMode() {
			return true;
		};
	};
	
	public static final Emitter.Factory FACTORY_ORANGE = new Factory() {
		@Override
		public void emit( Emitter emitter, int index, float x, float y ) {
			((OrangeEnergyParticle)emitter.recycle( OrangeEnergyParticle.class )).reset( x, y );
		}
		@Override
		public boolean lightMode() {
			return true;
		};
	};
	
	public static final Emitter.Factory FACTORY_GREEN = new Factory() {
		@Override
		public void emit( Emitter emitter, int index, float x, float y ) {
			((GreenEnergyParticle)emitter.recycle( GreenEnergyParticle.class )).reset( x, y );
		}
		@Override
		public boolean lightMode() {
			return true;
		};
	};
	
	public static final Emitter.Factory FACTORY_BLIGHT = new Factory() {
		@Override
		public void emit( Emitter emitter, int index, float x, float y ) {
			((BlightEnergyParticle)emitter.recycle( BlightEnergyParticle.class )).reset( x, y );
		}
		@Override
		public boolean lightMode() {
			return false;
		};
	};

    public static final Emitter.Factory FACTORY_BLACK = new Factory() {
        @Override
        public void emit( Emitter emitter, int index, float x, float y ) {
            ((BlackEnergyParticle)emitter.recycle( BlackEnergyParticle.class )).reset( x, y );
        }
//        @Override
//        public boolean lightMode() {
//            return true;
//        };
    };
	
	public EnergyParticle() {
		super();
		
		lifespan = 0.5f;

		color( 0xFFFFAA );

        speed.polar(Random.Float(PointF.PI2),Random.Float(32,48));
	}
	
	public void reset( float x, float y ) {
		revive();
		
		left = lifespan;
		
		this.x = x - speed.x * lifespan;
		this.y = y - speed.y * lifespan;
	}
	
	@Override
	public void update() {
		super.update();
		
		float p = left / lifespan;
		am = p < 0.5f ? p * p * 4 : (1 - p) * 2; 
		size( Random.Float( 5 * left / lifespan ) );
	}

    public static class BlueEnergyParticle extends EnergyParticle {

        public BlueEnergyParticle() {

            super();

            color(0x88CCFF);

        }
    }

    public static class WhiteEnergyParticle extends EnergyParticle {

        public WhiteEnergyParticle() {

            super();

            color(0xFFFFFF);
        }
    }
	
	public static class PurpleEnergyParticle extends EnergyParticle {
		
		public PurpleEnergyParticle() {
			
			super();
			
			color(0xFFAAFF);
		}
	}
	
	public static class OrangeEnergyParticle extends EnergyParticle {
		
		public OrangeEnergyParticle() {
			
			super();
			
			color(0xEE7722);
		}
	}
	
	public static class BlightEnergyParticle extends EnergyParticle {
		
		public BlightEnergyParticle() {
			
			super();
			
			color(0x00c500);
		}
	}
	
	public static class GreenEnergyParticle extends EnergyParticle {
		
		public GreenEnergyParticle() {
			
			super();
			
			color(0x22EE66);
		}
	}

    public static class BlackEnergyParticle extends EnergyParticle {

        public BlackEnergyParticle() {

            super();

            color(0x000000);
        }
    }
}