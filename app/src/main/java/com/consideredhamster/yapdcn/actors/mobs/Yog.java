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
package com.consideredhamster.yapdcn.actors.mobs;

import java.util.ArrayList;
import java.util.HashMap;

import com.consideredhamster.yapdcn.actors.blobs.Fire;
import com.consideredhamster.yapdcn.actors.buffs.BuffActive;
import com.consideredhamster.yapdcn.actors.hazards.CausticOoze;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.tweeners.AlphaTweener;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.consideredhamster.yapdcn.visuals.Assets;
import com.consideredhamster.yapdcn.Element;
import com.consideredhamster.yapdcn.Dungeon;
import com.consideredhamster.yapdcn.Statistics;
import com.consideredhamster.yapdcn.actors.Actor;
import com.consideredhamster.yapdcn.actors.Char;
import com.consideredhamster.yapdcn.actors.blobs.Blob;
import com.consideredhamster.yapdcn.actors.buffs.Buff;
import com.consideredhamster.yapdcn.actors.buffs.debuffs.Corrosion;
import com.consideredhamster.yapdcn.actors.buffs.BuffPassive;
import com.consideredhamster.yapdcn.visuals.effects.MagicMissile;
import com.consideredhamster.yapdcn.visuals.effects.Speck;
import com.consideredhamster.yapdcn.visuals.effects.particles.FlameParticle;
import com.consideredhamster.yapdcn.items.misc.Gold;
import com.consideredhamster.yapdcn.items.keys.SkeletonKey;
import com.consideredhamster.yapdcn.levels.Level;
import com.consideredhamster.yapdcn.misc.mechanics.Ballistica;
import com.consideredhamster.yapdcn.scenes.GameScene;
import com.consideredhamster.yapdcn.visuals.sprites.BurningFistSprite;
import com.consideredhamster.yapdcn.visuals.sprites.CharSprite;
import com.consideredhamster.yapdcn.visuals.sprites.RottingFistSprite;
import com.consideredhamster.yapdcn.visuals.sprites.YogSprite;
import com.watabou.utils.Random;
import com.consideredhamster.yapdcn.misc.utils.GLog;

public class Yog extends Mob {
	
	{
		name = Dungeon.depth == Statistics.deepestFloor ? "Yog-Dzewa" : "Yog-Dzewa之回响";
		info = "头目!";

		spriteClass = YogSprite.class;
		
		HP = HT = 500;
		
		EXP = 50;
        loot = Gold.class;
        lootChance = 4f;
		
		state = PASSIVE;

        resistances.put( Element.Body.class, Element.Resist.PARTIAL );
        resistances.put( Element.Dispel.class, Element.Resist.PARTIAL );

        resistances.put( Element.Mind.class, Element.Resist.IMMUNE );
        resistances.put( Element.Knockback.class, Element.Resist.IMMUNE );
        resistances.put( Element.Ensnaring.class, Element.Resist.IMMUNE );

	}

    private final static int FIST_RESPAWN_MIN = 25;
    private final static int FIST_RESPAWN_MAX = 30;

	private static final String TXT_DESC =
            "Yog-Dzewa是一位上古之神，来自混沌位面的强大存在。一个世纪前，古代矮人在同恶魔大军的战争中惨胜，却无法杀死这名神祇。他们只得将这名存在封印在都城下的厅堂之中，希望这位力量被削弱的神祇永无重见天日之时。";

    @Override
    public float awareness(){
        return 2.0f;
    }

    @Override
    protected float healthValueModifier() { return 0.25f; }
	
	public Yog() {
		super();
	}
	
	public void spawnFists() {
		RottingFist fist1 = new RottingFist();
		BurningFist fist2 = new BurningFist();
		
		do {
			fist1.pos = pos + Level.NEIGHBOURS8[Random.Int( 8 )];
			fist2.pos = pos + Level.NEIGHBOURS8[Random.Int( 8 )];
		} while (!Level.passable[fist1.pos] || !Level.passable[fist2.pos] || fist1.pos == fist2.pos);
		
		GameScene.add( fist1 );
		GameScene.add(fist2);
	}
	
	@Override
	public void damage( int dmg, Object src, Element type ) {

        int decreaseValue = 1;

		if (Dungeon.level.mobs.size() > 0) {
			for (Mob mob : Dungeon.level.mobs) {
				if (mob instanceof BurningFist || mob instanceof RottingFist) {

                    if( src instanceof Char ) {
                        mob.beckon( ((Char)src).pos );
                    }

                    decreaseValue += 2;
				}
			}
		}

        dmg /= decreaseValue;

		super.damage(dmg, src, type);
	}

    @Override
    protected boolean act() {

        state = PASSIVE;

        return super.act();
    }
	
	@Override
	public void beckon( int cell ) {
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void die( Object cause, Element dmg ) {

		for (Mob mob : (Iterable<Mob>)Dungeon.level.mobs.clone()) {
			if (mob instanceof BurningFist || mob instanceof RottingFist) {
				mob.die( cause, null );
			}
		}
		
        yell( "..." );
        super.die( cause, dmg );
        GameScene.bossSlain();
        Dungeon.level.drop( new SkeletonKey(), pos ).sprite.drop();

	}
	
	@Override
	public String description() {
		return TXT_DESC;
			
	}

    @Override
    public HashMap<Class<? extends Element>, Float> resistances() {

        HashMap<Class<? extends Element>, Float> result=new HashMap<>();;
        result.putAll( super.resistances());

        if( buff( RespawnBurning.class ) == null ){
            result.put( Element.Flame.class, Element.Resist.IMMUNE );
        }

        if( buff( RespawnRotting.class ) == null ){
            result.put( Element.Acid.class, Element.Resist.IMMUNE );
        }

        return result;
    }
	
	public static class RottingFist extends MobHealthy {

        public RottingFist() {

            super( 5, 25, true );
			name = "腐烂之拳";
			info = "腐蚀攻击, 酸性血液, 酸液治疗, 可重生";
			spriteClass = RottingFistSprite.class;

			EXP = 0;
			
			state = WANDERING;

            resistances.put( Element.Knockback.class, Element.Resist.PARTIAL );
            resistances.put( Element.Dispel.class, Element.Resist.PARTIAL );

            resistances.put( Element.Body.class, Element.Resist.PARTIAL );
            resistances.put( Element.Doom.class, Element.Resist.PARTIAL );

            resistances.put( Element.Acid.class, Element.Resist.IMMUNE );
            resistances.put( Element.Mind.class, Element.Resist.IMMUNE );

            resistances.put( Element.Shock.class, Element.Resist.VULNERABLE );
		}

        @Override
        protected float healthValueModifier() { return 0.25f; }

        @Override
        public void die( Object cause, Element dmg ) {
            super.die( cause, dmg );

            for (Mob mob : (Iterable<Mob>)Dungeon.level.mobs.clone()) {
                if ( mob instanceof Yog && mob.isAlive() ) {
                    Buff.affect( mob, RespawnRotting.class, Random.IntRange( FIST_RESPAWN_MIN, FIST_RESPAWN_MAX ) );
                }
            }
        }

		@Override
		public int attackProc( Char enemy, int damage, boolean blocked ) {

            BuffActive.addFromDamage( enemy, Corrosion.class, damage );

			return damage;
		}

        @Override
        public void damage( int dmg, Object src, Element type ) {

            if ( type == Element.ACID ) {

                if (HP < HT) {
                    int reg = Math.min( dmg / 2, HT - HP );

                    if (reg > 0) {
                        HP += reg;
                        sprite.showStatus(CharSprite.POSITIVE, Integer.toString(reg));
                        sprite.emitter().burst(Speck.factory(Speck.HEALING), (int) Math.sqrt(reg));
                    }
                }

            } else {

                super.damage(dmg, src, type);

            }
        }

        @Override
        public boolean add( Buff buff ) {

            return !(buff instanceof Corrosion ) && super.add( buff );

        }

        @Override
        public int defenseProc( Char enemy, int damage, boolean blocked ) {
            super.defenseProc( enemy, damage, blocked );

            CausticOoze.spawn( pos, damage );

            return damage;
        }
		
		@Override
		public String description() {
			return TXT_DESC;
				
		}
	}
	
	public static class BurningFist extends MobRanged {

        public BurningFist() {

            super( 5, 25, true );
			name = "燃烧之拳";
			info = "火焰魔弹, 持续燃烧, 可重生";
			spriteClass = BurningFistSprite.class;
			
			EXP = 0;
			
			state = WANDERING;

			minDamage /= 2;
			maxDamage /= 2;

            resistances.put( Element.Knockback.class, Element.Resist.PARTIAL );
            resistances.put( Element.Dispel.class, Element.Resist.PARTIAL );

            resistances.put( Element.Body.class, Element.Resist.PARTIAL );
            resistances.put( Element.Doom.class, Element.Resist.PARTIAL );

            resistances.put( Element.Mind.class, Element.Resist.IMMUNE );
            resistances.put( Element.Flame.class, Element.Resist.IMMUNE );

            resistances.put( Element.Frost.class, Element.Resist.VULNERABLE );
        }

        @Override
        protected float healthValueModifier() { return 0.25f; }
		
		@Override
		public void die( Object cause, Element dmg ) {
			super.die( cause, dmg );

            for (Mob mob : (Iterable<Mob>)Dungeon.level.mobs.clone()) {
                if ( mob instanceof Yog && mob.isAlive() ) {
                    Buff.affect( mob, RespawnBurning.class, Random.IntRange( FIST_RESPAWN_MIN, FIST_RESPAWN_MAX ) );
                }
            }
		}

        @Override
        public boolean act() {

            GameScene.add( Blob.seed( pos, 2, Fire.class ) );

            return super.act();
        }
		
		@Override
		protected boolean canAttack( Char enemy ) {
			return Ballistica.cast( pos, enemy.pos, false, true ) == enemy.pos;
		}

        @Override
        protected void onRangedAttack( int cell ) {

            MagicMissile.fire(sprite.parent, pos, cell,
                    new Callback() {
                        @Override
                        public void call() {
                            onCastComplete();
                        }
                    });

            Sample.INSTANCE.play(Assets.SND_ZAP);

            super.onRangedAttack( cell );
        }

        @Override
        public boolean cast( Char enemy ) {

            if (hit( this, enemy, true, true )) {

                enemy.damage( damageRoll(), this, Element.FLAME );

            } else {

                enemy.missed();

            }

            return true;
        }
		
		@Override
		public String description() {
			return TXT_DESC;
				
		}

	}

    public static class RespawnBurning extends BuffPassive {

        private boolean warned = false;

        private static final String WARNED	= "warned";

        @Override
        public void storeInBundle( Bundle bundle ) {
            super.storeInBundle( bundle );
            bundle.put( WARNED, warned );
        }

        @Override
        public void restoreFromBundle( Bundle bundle ) {
            super.restoreFromBundle(bundle);
            warned = bundle.getBoolean( WARNED );
        }

        @Override
        public void detach() {

            if( target.isAlive() ) {

                if( warned ) {

                    BurningFist fist = new BurningFist();

                    ArrayList<Integer> candidates = new ArrayList<Integer>();

                    for (int n : Level.NEIGHBOURS8) {
                        int cell = target.pos + n;
                        if (!Level.solid[cell] && Actor.findChar(cell) == null) {
                            candidates.add(cell);
                        }
                    }

                    if (candidates.size() > 0) {
                        fist.pos = candidates.get(Random.Int(candidates.size()));
                    } else {
                        fist.pos = Dungeon.level.randomRespawnCell();
                    }

                    GameScene.add(fist);

                    fist.sprite.alpha(0);
                    fist.sprite.parent.add(new AlphaTweener(fist.sprite, 1, 0.5f));
                    fist.sprite.emitter().burst(FlameParticle.FACTORY, 15);

                    GLog.w("燃烧之拳复活了！");
                    super.detach();
                } else {
                    warned=true;
                    GLog.w( "燃烧之拳很快就会复活！" );
                    spend( Random.IntRange( FIST_RESPAWN_MIN, FIST_RESPAWN_MAX ) );

                }
            }else
                super.detach();
        }
    }

    public static class RespawnRotting extends BuffPassive {

        private boolean warned = false;

        private static final String WARNED	= "warned";

        @Override
        public void storeInBundle( Bundle bundle ) {
            super.storeInBundle( bundle );
            bundle.put( WARNED, warned );
        }

        @Override
        public void restoreFromBundle( Bundle bundle ) {
            super.restoreFromBundle(bundle);
            warned = bundle.getBoolean( WARNED );
        }

        @Override
        public void detach() {

            if( target.isAlive() ) {

                if (warned) {

                    RottingFist fist = new RottingFist();

                    ArrayList<Integer> candidates = new ArrayList<Integer>();

                    for (int n : Level.NEIGHBOURS8) {
                        int cell = target.pos + n;
                        if (!Level.solid[cell] && Actor.findChar(cell) == null) {
                            candidates.add(cell);
                        }
                    }

                    if (candidates.size() > 0) {
                        fist.pos = candidates.get(Random.Int(candidates.size()));
                    } else {
                        fist.pos = Dungeon.level.randomRespawnCell();
                    }

                    GameScene.add(fist);

                    fist.sprite.alpha(0);
                    fist.sprite.parent.add(new AlphaTweener(fist.sprite, 1, 0.5f));
                    fist.sprite.emitter().burst(Speck.factory(Speck.TOXIC), 15);


                    GLog.w("腐烂之拳复活了！");
                    super.detach();
                } else {
                    warned=true;
                    GLog.w("腐烂之拳很快就会复活！");
                    spend(Random.IntRange(FIST_RESPAWN_MIN, FIST_RESPAWN_MAX));

                }
            }else
                super.detach();
        }
    }
}