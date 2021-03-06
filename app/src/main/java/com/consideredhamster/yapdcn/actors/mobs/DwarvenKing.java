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

import com.consideredhamster.yapdcn.actors.buffs.BuffActive;
import com.consideredhamster.yapdcn.actors.buffs.debuffs.Debuff;
import com.watabou.noosa.Camera;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.tweeners.AlphaTweener;
import com.watabou.utils.Random;
import com.consideredhamster.yapdcn.visuals.Assets;
import com.consideredhamster.yapdcn.Badges;
import com.consideredhamster.yapdcn.Element;
import com.consideredhamster.yapdcn.Dungeon;
import com.consideredhamster.yapdcn.Statistics;
import com.consideredhamster.yapdcn.actors.Actor;
import com.consideredhamster.yapdcn.actors.Char;
import com.consideredhamster.yapdcn.actors.blobs.Blob;
import com.consideredhamster.yapdcn.actors.buffs.Buff;
import com.consideredhamster.yapdcn.actors.buffs.bonuses.Enraged;
import com.consideredhamster.yapdcn.actors.buffs.special.UnholyArmor;
import com.consideredhamster.yapdcn.actors.buffs.debuffs.Withered;
import com.consideredhamster.yapdcn.visuals.effects.BlobEmitter;
import com.consideredhamster.yapdcn.visuals.effects.Flare;
import com.consideredhamster.yapdcn.visuals.effects.DrainLife;
import com.consideredhamster.yapdcn.actors.special.Pushing;
import com.consideredhamster.yapdcn.visuals.effects.Speck;
import com.consideredhamster.yapdcn.visuals.effects.SpellSprite;
import com.consideredhamster.yapdcn.items.misc.Gold;
import com.consideredhamster.yapdcn.items.keys.SkeletonKey;
import com.consideredhamster.yapdcn.levels.CityBossLevel;
import com.consideredhamster.yapdcn.levels.Level;
import com.consideredhamster.yapdcn.scenes.GameScene;
import com.consideredhamster.yapdcn.visuals.sprites.CharSprite;
import com.consideredhamster.yapdcn.visuals.sprites.KingSprite;
import com.consideredhamster.yapdcn.visuals.sprites.UndeadSprite;
import com.watabou.utils.Bundle;
import com.consideredhamster.yapdcn.misc.utils.GLog;

public class DwarvenKing extends MobPrecise {

    private final static int LINE_GREETINGS = 0;
    private final static int LINE_AWAKENING = 1;
    private final static int LINE_THREATENED = 2;
    private final static int LINE_CHANNELING = 3;
    private final static int LINE_ANNOYANCE = 4;
    private final static int LINE_EMPOWERED = 5;
    private final static int LINE_MOVEASIDE = 6;
    private final static int LINE_NEARDEATH = 7;

    private final static String TXT_ENRAGED = "矮人国王愤怒了！";
    private final static String TXT_STOPPED = "矮人国王停止了仪式。";
    private final static String TXT_CALMDWN = "矮人国王不再愤怒。";
    private final static String TXT_SUMMONS = "矮人国王咏唱了强大的咒术！";
    private final static String TXT_CHANNEL = "矮人国王开始了某种仪式...";

    private static String[][] LINES = {

            {
                    "大胆！",
                    "是谁竟敢扰我清梦？",
                    "人类？在 我 的 王座之间？",
            },
            {
                    "起来，我的奴隶们！",
                    "我命令你们战斗！",
                    "下仆们！开宴之时已至！",
            },
            {
                    "区区虫豸...你会付出代价的！",
                    "你的死法将会非 常 痛 苦...",
                    "是时候教训你了，凡人。",
                    "你，会变成一个非常强大的奴隶。",
            },
            {
                    "我的随从们，到我身边来！",
                    "见识一下我真正的力量！",
                    "向我而来，我的臣民们！",
                    "你的国王在命令你！",
                    "从你们的坟墓里起来！",
            },
            {
                    "这些死人就像它们活着的时候那样没用。",
                    "这些谄媚者都是些一文不值的垃圾。",
                    "到头来什么事都得我自己做。",
            },
            {
                    "是时候把你从棋盘上抹去了，凡人。",
                    "你的这场游戏已经超时了，凡人。",
                    "不见棺材不落泪？去死！",
                    "你的惨叫将会成为入我耳中的交响乐。",
                    "你要为你所做的一切付出代价，人类。",
            },
            {
                    "滚开，虫子！",
                    "别惹我生气，小不点。",
                    "你这样可没法阻止我。",
            },
            {
                    "你杀不死我，" + Dungeon.hero.heroClass.title() + "...我是...不朽的... ",
                    "我会回来的, " + Dungeon.hero.heroClass.title() + "...我会...回...",
                    "不，不！这怎么可能？！我竟被...凡人...干掉...",
            },
    };


    private static final float SPAWN_DELAY	= 1f;
    private static final float BASE_ENRAGE	= 5f;

    //FIXME

    private static final BoneExplosion EXPLOSION  = new BoneExplosion();

    public static class BoneExplosion {}
    public static class KnockBack {}

    public DwarvenKing() {

        super( 5, 25, true );

        // 500

//        minDamage = 0;
//        maxDamage = 0;

        name = Dungeon.depth == Statistics.deepestFloor ? "矮人国王" : "不灭的矮人国王";
        spriteClass = KingSprite.class;

        loot = Gold.class;
        lootChance = 4f;

        resistances.put(Element.Body.class, Element.Resist.PARTIAL);
        resistances.put(Element.Mind.class, Element.Resist.PARTIAL);
        resistances.put(Element.Doom.class, Element.Resist.PARTIAL);
        resistances.put(Element.Unholy.class, Element.Resist.PARTIAL);

        resistances.put( Element.Dispel.class, Element.Resist.IMMUNE );
        resistances.put( Element.Knockback.class, Element.Resist.PARTIAL );
    }

    public int breaks = 0;
    public boolean phase = false;

    private static final String PHASE	= "phase";
    private static final String BREAKS	= "breaks";

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle(bundle);
        bundle.put( PHASE, phase );
        bundle.put( BREAKS, breaks );
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle(bundle);
        phase = bundle.getBoolean( PHASE );
        breaks = bundle.getInt( BREAKS );
    }

    @Override
    protected float healthValueModifier() {
        return 0.25f;
    }

    @Override
    public float awareness(){
        return 2.0f;
    }

    @Override
    public int dexterity() {
        return buff( UnholyArmor.class ) == null ? super.dexterity() : 0 ;
    }

    @Override
    public int armourAC() {
        return buff( UnholyArmor.class ) == null ? super.armourAC() : 0;
    }

    @Override
    protected boolean getCloser( int target ) {
        return phase ?
                super.getCloser( CityBossLevel.pedestal() ) :
                super.getCloser( target );
    }

    @Override
    protected boolean canAttack( Char enemy ) {
        return super.canAttack( enemy ) && ( !phase || ( buff( Enraged.class ) != null ) );
    }

    @Override
    public boolean act() {

        int throne = Dungeon.level instanceof CityBossLevel ? CityBossLevel.pedestal() : 0;

//        if( Dungeon.level instanceof CityBossLevel ) {
//            GameScene.add(Blob.seed(CityBossLevel.throne( true ), 1, Fire.class));
//            GameScene.add(Blob.seed(CityBossLevel.throne( false ), 1, Fire.class));
//        }

        if( Dungeon.hero.isAlive() ) {

            if (buff(UnholyArmor.class) != null) {

                sprite.cast(throne, null);
                spend(TICK);
                return true;

            } else if (throne > 0 && phase) {

                if (Level.adjacent(pos, throne) && findChar(throne) != null) {

                    yell( LINE_MOVEASIDE );
                    GLog.w( TXT_ENRAGED );

                    BuffActive.add( this, Enraged.class, TICK );
                    enemy = findChar(throne);

//                    return super.act();

                } else if (pos == throne) {

                    sprite.cast(throne, null);
                    sprite.centerEmitter().start(Speck.factory(Speck.SCREAM), 0.4f, 2);
                    Sample.INSTANCE.play(Assets.SND_CHALLENGE);

                    sacrificeAllMinions();

                    awakenWells();

                    if (breaks > 0) {

                        Buff.affect(this, UnholyArmor.class, armorDuration( breaks ) );
                        yell( LINE_CHANNELING );
                        GLog.w( TXT_CHANNEL );

                        Debuff.removeAll( this );

                    } else {

                        yell( LINE_AWAKENING );
                        GLog.w( TXT_SUMMONS );
                        phase = false;

                    }

                    Buff.detach(this, Enraged.class);
                    spend(TICK);

                    return true;
                }

            } else if (!phase && 3 - breaks > 4 * HP / HT) {

                breaks++;
                phase = true;

                Buff.detach(this, Enraged.class);
                yell( LINE_THREATENED );

                spend(TICK);
                return true;

            } else {

                phase = false;
                return super.act();

            }
        }

        return super.act();
    }

    @Override
    public boolean cast( Char enemy ) {

        Blob blob = Dungeon.level.blobs.get( Spawner.class );

        if( blob != null ) {
            for (int well : CityBossLevel.wells()) {
                if (blob.cur[well] > 1) {
                    GameScene.add(Blob.seed(well, -1, Spawner.class));
                }
            }
        }

        for (Mob mob : Dungeon.level.mobs) {
            if (mob instanceof Undead) {
                mob.target = pos;
                BuffActive.add( mob, Enraged.class, TICK );
            }
        }

        return true;
    }

    @Override
    public void damage( int dmg, Object src, Element type ) {

        if( buff( Enraged.class ) != null ) {

            dmg = dmg / 2 + Random.Int( 1 + dmg % 2 );

        }

        super.damage(dmg, src, type);
    }

    @Override
    public HashMap<Class<? extends Element>, Float> resistances() {

        HashMap<Class<? extends Element>, Float> result=new HashMap<>();;
        result.putAll( super.resistances());

        if( buff( UnholyArmor.class ) != null ){
            for( Class<? extends Element> type : UnholyArmor.RESISTS ) {
                result.put( type, Element.Resist.IMMUNE );
            }
        }

        return result;
    }


    @Override
    public int attackProc( Char enemy, int damage, boolean blocked ) {

        if ( enemy != null && buff( Enraged.class ) != null ) {
            Pushing.knockback( enemy, pos, 1, damage );
        }

        return damage;
    }

    @Override
    public void remove( Buff buff ) {
        if( buff instanceof UnholyArmor ) {
            phase = false;

            int duration = ((UnholyArmor) buff).consumed();

            if( duration > 0 ) {
                BuffActive.add( this, Enraged.class, BASE_ENRAGE + TICK * duration );
                yell( LINE_EMPOWERED );
                GLog.w( TXT_ENRAGED );
                spend( TICK );
            } else {
                new Flare( 6, 16 ).color( SpellSprite.COLOUR_DARK, true).show( sprite, 2f );
                yell( LINE_ANNOYANCE );
                GLog.w( TXT_STOPPED );
                spend( TICK );
            }
        } else if( buff instanceof Enraged ) {
            GLog.w( TXT_CALMDWN );
        }

        super.remove( buff );
    }
	
	@Override
	public void die( Object cause, Element dmg ) {

        yell( LINE_NEARDEATH );
        Camera.main.shake(3, 0.5f);
        new Flare( 6, 48 ).color( SpellSprite.COLOUR_DARK, true).show( sprite, 3f );

        Dungeon.level.drop( new SkeletonKey(), pos ).sprite.drop();

        for (Mob mob : (Iterable<Mob>) Dungeon.level.mobs.clone()) {
            if (mob instanceof Undead) {
                mob.die( cause, null );
            }
        }

        Blob blob = Dungeon.level.blobs.get( Spawner.class );

        if (blob != null) {
            blob.remove();
        }

        super.die( cause, dmg );

        Badges.validateBossSlain();
        GameScene.bossSlain();


	}
	
	@Override
	public void notice() {
		super.notice();

        if( enemySeen && HP == HT && breaks == 0 ) {
            yell( LINE_GREETINGS );
        }
	}
	
	@Override
	public String description() {
		return
			"这名最后的矮人国王以对死灵术式的透彻理解而闻名。他成功说服王国议会的全体成员参加了一场本应让所有参与者获得永生的祭祀仪式。最终成功的只有国王自己——如果不算上他身边那支化为枯骨的死灵大军的话。";
	}

//    @Override
//    public HashSet<Class<? extends EffectType>> resistances() {
//        return RESISTANCES;
//    }
//
//    @Override
//    public HashSet<Class<? extends EffectType>> immunities() {
//        return IMMUNITIES;
//    }
	
	public static class Undead extends MobPrecise {

		public DwarvenKing king = null;
		public int well = 0;

        public Undead() {

            super( 5, 0, false );

			name = "死灵矮人";
			spriteClass = UndeadSprite.class;

            minDamage /= 2;
            maxDamage /= 2;

            armorClass /= 2;

            resistances.put(Element.Unholy.class, Element.Resist.PARTIAL);
            resistances.put(Element.Acid.class, Element.Resist.PARTIAL);

            resistances.put(Element.Mind.class, Element.Resist.IMMUNE);
            resistances.put(Element.Body.class, Element.Resist.IMMUNE);

		}

        public boolean isMagical() {
            return true;
        }

        private static final String WELL	= "well";

        @Override
        public void storeInBundle( Bundle bundle ) {
            super.storeInBundle(bundle);
            bundle.put( WELL, well );
        }

        @Override
        public void restoreFromBundle( Bundle bundle ) {
            super.restoreFromBundle(bundle);
            well = bundle.getInt( WELL );
        }

        @Override
        public int dexterity() {
            return buff( Enraged.class ) == null ? super.dexterity() : 0 ;
        }

        @Override
        public int armourAC() {
            return buff( Enraged.class ) == null ? super.armourAC() : 0;
        }

        @Override
        public boolean act() {

            if( king == null ) {
                king = king();
//            } else if (!enemySeen) {
//                beckon(king.pos);
            }

            if( buff( Enraged.class ) != null && king != null && king.isAlive() && Level.adjacent( pos, king.pos ) ) {

                int healthRestored = Math.min( Random.IntRange( HP / 3, HP / 2 ), king.HT - king.HP );

                king.HP += healthRestored;

                if (king.sprite.visible) {

                    sprite.parent.add( new DrainLife( pos, king.pos, null ) );

                    king.sprite.emitter().burst(Speck.factory(Speck.HEALING), 1);

                    king.sprite.showStatus(CharSprite.POSITIVE, Integer.toString(healthRestored));

                }

                UnholyArmor buff = king.buff( UnholyArmor.class );

                if( buff != null ) {

                    buff.consumed( king.breaks );
                    sprite.showStatus(CharSprite.NEGATIVE, "sacrificed");
                    new Flare( 6, 16 ).color( SpellSprite.COLOUR_DARK, true).show( sprite, 2f );

                }

                die( this );

                return true;
            }
            return super.act();
        }


        @Override
        protected boolean getCloser( int target ) {
            return buff( Enraged.class ) != null && king != null && king.isAlive() ?
                    super.getCloser( king.pos ) :
                    super.getCloser( target );
        }

        @Override
        protected boolean canAttack( Char enemy ) {
            return super.canAttack( enemy ) && buff( Enraged.class ) == null;
        }

		@Override
		public int attackProc( Char enemy, int damage, boolean blocked ) {

            if( Random.Int( 10 ) < 5 ) {
                BuffActive.addFromDamage( enemy, Withered.class, damage );
            }

			return damage;
		}

		
		@Override
		public void
        die( Object cause, Element dmg ) {

//            if( buff( Enraged.class ) != null && king != null && king.isAlive() && cause != king ) {
//                for (int n : Level.NEIGHBOURS8) {
//
//                    int p = pos + n;
//
//                    Char ch = findChar(p);
//
//                    if (ch != null && ch.isAlive()) {
//                        ch.damage(Char.absorb(damageRoll(), ch.armorClass() / 2), EXPLOSION, Element.PHYSICAL);
//                    }
//                }
//
//                sprite.emitter().burst( FlameParticle.FACTORY, 10 );
//            }

            if( well > 0 && king != null) {
                GameScene.add( Blob.seed( well, spawnDelay( king.breaks ), Spawner.class ) );
            }

			if (Dungeon.visible[pos]) {
				Sample.INSTANCE.play( Assets.SND_BONES );
                sprite.emitter().burst( Speck.factory( Speck.BONE ), 6 );
			}

            super.die( cause, dmg );
		}
		
		@Override
		public String description() {
			return
				"由矮人国王的意志唤醒的矮人亡灵，曾经的王国议会成员。除了长着厚实的胡须以外，和正常的骷髅外观上并没有区别。";
		}
	}

    public static class Spawner extends Blob {

        public Spawner() {
            super();

            name = "骸骨坑";
        }

        @Override
        protected void evolve() {

            int from = WIDTH + 1;
            int to = Level.LENGTH - WIDTH - 1;

            for (int pos=from; pos < to; pos++) {

                if (cur[pos] > 0) {

                    cur[pos]--;

                    if( cur[ pos ] <= 0 ) {

                        if( !spawnSkeleton( pos ) ) {
                            cur[ pos ] = 1;
                        };
                    }
				}

                volume += ( off[pos] = cur[pos] );

            }
        }

        public void seed( int cell, int amount ) {
            if (cur[cell] == 0) {
                volume += amount;
                cur[cell] = amount;
            }
        }

        @Override
        public void use( BlobEmitter emitter ) {
            super.use(emitter);
            emitter.start( Speck.factory(Speck.RATTLE), 0.1f, 0 );
        }

        @Override
        public String tileDesc() {
            return "坑底的骸骨仍在活动，令人不寒而栗。";
        }
    }

    private void sacrificeAllMinions() {
        int healthRestored = 0;

        for (Mob mob : (Iterable<Mob>) Dungeon.level.mobs.clone()) {
            if (mob instanceof Undead ) {

                healthRestored += Random.IntRange( mob.HP / 3, mob.HP / 2 );

                if (sprite.visible || mob.sprite.visible) {
                    sprite.parent.add( new DrainLife( mob.pos, pos, null ) );
                    new Flare( 6, 16 ).color( SpellSprite.COLOUR_DARK, true).show( mob.sprite, 2f );
                }

                mob.die( this );
            }
        }

        healthRestored = Math.min( healthRestored, HT - HP );

        if( healthRestored > 0 ) {

            HP += healthRestored;

            if (sprite.visible) {
                sprite.emitter().burst(Speck.factory(Speck.HEALING), (int) Math.sqrt(healthRestored));
                sprite.showStatus(CharSprite.POSITIVE, Integer.toString(healthRestored));
            }
        }
    }

    private void awakenWells() {
        int dormant = 3 - breaks;
        ArrayList<Integer> wells = new ArrayList<>();
        Blob blob = Dungeon.level.blobs.get(Spawner.class);

        for (int well : CityBossLevel.wells()) {
            if (blob == null || blob.cur[well] <= 0) {
                wells.add(well);
            } else if (blob.cur[well] > 0) {
                GameScene.add(Blob.seed(well, spawnDelay(breaks), Spawner.class));
            }
        }

        while (wells.size() > dormant) {

            Integer w = Random.element(wells);

            GameScene.add(Blob.seed(w, spawnDelay(breaks), Spawner.class));
            wells.remove(w);

        }
    }

    private static boolean spawnSkeleton( int pos ) {

        if( Actor.findChar(pos) != null ) {
            ArrayList<Integer> candidates = new ArrayList<Integer>();

            for (int n : Level.NEIGHBOURS8) {
                int cell = pos + n;
                if (!Level.solid[cell] && Actor.findChar(cell) == null) {
                    candidates.add(cell);
                }
            }

            if (candidates.size() > 0) {
                pos = candidates.get( Random.Int( candidates.size() ) );
            } else {
                return false;
            }
        }

        Undead clone = new Undead();

        clone.pos = pos;
        clone.well = pos;

        clone.king = king();
        clone.state = clone.HUNTING;

        if( clone.king != null )
            clone.HT = clone.HP = 15 + clone.king.breaks * 5;

        Dungeon.level.press(clone.pos, clone);
        Sample.INSTANCE.play(Assets.SND_CURSED);

        GameScene.add(clone, SPAWN_DELAY);

        if (Dungeon.visible[clone.pos]) {

            clone.sprite.alpha(0);
            clone.sprite.parent.add(new AlphaTweener(clone.sprite, 1, 0.5f));
            clone.sprite.emitter().start(Speck.factory(Speck.RAISE_DEAD), 0.01f, 15);

        }

        clone.sprite.idle();

        return true;
    }

    private static DwarvenKing king() {
        for (Mob mob : Dungeon.level.mobs) {
            if (mob instanceof DwarvenKing) {
                return (DwarvenKing)mob;
            }
        }

        return null;
    }

    private static int spawnDelay( int breaks ) {
        return Random.IntRange( 10, 15 ) - Random.IntRange( breaks * 1, breaks * 2 ); // 10-15 8-12 6-9 4-6
    }

    private static float armorDuration( int breaks ) {
        return 5 + breaks * 5 + Random.Int( 6 );
    }

    private void yell( int line ) {
        yell( LINES[ line ][ Random.Int( LINES[ line ].length ) ] );
    }
}
