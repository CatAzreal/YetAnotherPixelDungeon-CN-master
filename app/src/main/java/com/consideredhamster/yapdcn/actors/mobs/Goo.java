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

import com.consideredhamster.yapdcn.Difficulties;
import com.consideredhamster.yapdcn.actors.buffs.BuffActive;
import com.watabou.noosa.tweeners.AlphaTweener;
import com.watabou.utils.Bundle;
import com.consideredhamster.yapdcn.Badges;
import com.consideredhamster.yapdcn.Element;
import com.consideredhamster.yapdcn.Dungeon;
import com.consideredhamster.yapdcn.actors.Actor;
import com.consideredhamster.yapdcn.actors.Char;
import com.consideredhamster.yapdcn.actors.blobs.Miasma;
import com.consideredhamster.yapdcn.actors.blobs.Blob;
import com.consideredhamster.yapdcn.actors.buffs.debuffs.Burning;
import com.consideredhamster.yapdcn.actors.buffs.bonuses.Enraged;
import com.consideredhamster.yapdcn.actors.special.Pushing;
import com.consideredhamster.yapdcn.visuals.effects.Speck;
import com.consideredhamster.yapdcn.items.misc.Gold;
import com.consideredhamster.yapdcn.levels.Level;
import com.consideredhamster.yapdcn.levels.SewerBossLevel;
import com.consideredhamster.yapdcn.levels.Terrain;
import com.consideredhamster.yapdcn.levels.features.Door;
import com.consideredhamster.yapdcn.scenes.GameScene;
import com.consideredhamster.yapdcn.visuals.sprites.CharSprite;
import com.consideredhamster.yapdcn.visuals.sprites.GooSprite;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;
import com.consideredhamster.yapdcn.misc.utils.GLog;

public abstract class Goo extends MobEvasive {

	private static final float PUMP_UP_DELAY	= 2f;

    private static final float SPLIT_DELAY	= 1f;

    private static final int SPAWN_HEALTH = 8;

    public boolean phase = false;

    public Goo() {

        super(2, 10, true);

        armorClass = 0;

        resistances.put(Element.Acid.class, Element.Resist.PARTIAL);
        resistances.put(Element.Flame.class, Element.Resist.PARTIAL);

        resistances.put(Element.Mind.class, Element.Resist.IMMUNE);
        resistances.put(Element.Body.class, Element.Resist.IMMUNE);


    }

    @Override
    public boolean isMagical() {
        return true;
    }

    private static Goo mother() {
        for (Mob mob : (Iterable<Mob>) Dungeon.level.mobs.clone()) {
            if (mob instanceof Mother) {
                return (Goo)mob;
            }
        }

        return null;
    }

//    @Override
//    public HashMap<Class<? extends Element>, Float> resistances() {
//
//        HashMap<Class<? extends Element>, Float> result=new HashMap<>();;
//        result.putAll( super.resistances());
//
//        if( buff( Frozen.class ) != null ){
//            result.put( Element.Physical.class, Element.Resist.VULNERABLE );
//        }
//
//        return result;
//    }

    private static final String PHASE	= "phase";

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle(bundle);
        bundle.put( PHASE, phase );
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle(bundle);
        phase = bundle.getBoolean( PHASE );
    }

//    @Override
//    public DamageType damageType() {
//        return DamageType.ACID;
//    }

	@Override
	public void damage( int dmg, Object src, Element type ) {

        if (HP <= 0) {
            return;
        }

        if( buff( Enraged.class ) != null ) {

            dmg /= 2;

        } else if ( type == Element.PHYSICAL && dmg > 1 && dmg < HP && dmg > Random.Int( SPAWN_HEALTH * 3 ) ) {

            ArrayList<Integer> candidates = new ArrayList<Integer>();
            boolean[] passable = Level.passable;

            for (int n : Level.NEIGHBOURS8) {
                if (passable[pos + n] && Actor.findChar(pos + n) == null) {
                    candidates.add(pos + n);
                }
            }

            if (candidates.size() > 0) {

                final Spawn clone = new Spawn();

                clone.pos = pos;
                clone.HT = dmg;

                clone.state = clone.HUNTING;

                if( Dungeon.difficulty == Difficulties.NORMAL ) {
                    clone.HP = Random.NormalIntRange( 1, clone.HT / 2 );
                } else if( Dungeon.difficulty > Difficulties.NORMAL ) {
                    clone.HP = clone.HT / 2;
                } else {
                    clone.HP = 1;
                }

                GameScene.add( clone, SPLIT_DELAY );

                Pushing.move( clone, Random.element( candidates ), new Callback() {
                    @Override
                    public void call(){
                        Actor.occupyCell( clone );
                        Dungeon.level.press(clone.pos, clone);
                    }
                } );

                Burning buff1 = buff( Burning.class );

                if ( buff1 != null ) {
                    BuffActive.addFromDamage( clone, Burning.class, buff1.getDuration() );
                }
            }
        }

        super.damage( dmg, src, type );
	}
	
	@Override
	public String description() {
		return
			"我们对粘咕所知甚少。它甚至很有可能不是一个生物，而是下水道表面聚集的邪恶物质得到基本智能而产生的实体。";
	}

    public static class Mother extends Goo {

        public Mother() {

            super();

            name = "黏咕";
            spriteClass = GooSprite.class;

            loot = Gold.class;
            lootChance = 4f;

            dexterity /= 2;

            resistances.put( Element.Knockback.class, Element.Resist.PARTIAL );

        }

        protected int breaks = 0;

        private static final String BREAKS	= "breaks";

        @Override
        public void storeInBundle( Bundle bundle ) {
            super.storeInBundle(bundle);
            bundle.put( BREAKS, breaks );
        }

        @Override
        public void restoreFromBundle( Bundle bundle ) {
            super.restoreFromBundle(bundle);
            breaks = bundle.getInt( BREAKS );
        }

        @Override
        public float awareness(){
            return state != SLEEPING ? super.awareness() : 0.0f ;
        }

        @Override
        protected float healthValueModifier() { return 0.25f; }

        @Override
        public boolean act() {

            if (( state == SLEEPING || Level.water[pos] ) && HP < HT && !phase ) {

                sprite.emitter().burst( Speck.factory( Speck.HEALING ), 1 );
                HP++;

                if( HP >= HT ) {
                    beckon( Dungeon.hero.pos );
                    Dungeon.hero.interrupt( "你被一种不好的感觉惊醒。" );
                    GLog.i("黏咕苏醒了!");
                }
            }

            if( phase ) {

                GameScene.add( Blob.seed(pos, 150 + breaks * 50, Miasma.class) );

                if( buff( Enraged.class ) == null ) {

                    phase = false;

                    state = SLEEPING;

                    Blob blob = Dungeon.level.blobs.get( Miasma.class );

                    if (blob != null) {
                        blob.remove();
                    }

                    for (int i = Random.Int(2) ; i < breaks + 1 ; i++) {

                        int pos = ((SewerBossLevel) Dungeon.level).getRandomSpawnPoint();

                        if( pos > 0 ) {

                            Spawn clone = new Spawn();

                            clone.HT = SPAWN_HEALTH;

                            if( Dungeon.difficulty == Difficulties.NORMAL ) {
                                clone.HT = Random.NormalIntRange( clone.HT, clone.HT * 2);
                            } else if( Dungeon.difficulty > Difficulties.NORMAL ) {
                                clone.HT = clone.HT * 2;
                            }

                            clone.HP = clone.HT;
                            clone.pos = pos;
                            clone.state = clone.HUNTING;
                            clone.phase = true;

                            clone.beckon(pos);

                            if (Dungeon.level.map[clone.pos] == Terrain.DOOR_CLOSED) {
                                Door.enter(clone.pos);
                            }

                            Dungeon.level.press(clone.pos, clone);

                            GameScene.add(clone, SPLIT_DELAY);

                            if (Dungeon.visible[clone.pos]) {
                                clone.sprite.alpha(0);
                                clone.sprite.parent.add(new AlphaTweener(clone.sprite, 1, 0.5f));
                            }

                            clone.sprite.idle();
                        }
                    }

                    if (Dungeon.visible[pos]) {
                        sprite.showStatus(CharSprite.DEFAULT, "ZZZzzz...");
                        GLog.i("黏咕已筋疲力尽！");
                    }

                    sprite.idle();
                    spend( PUMP_UP_DELAY );

                } else {

                    spend( TICK );

                }

                return true;

            } else if( state != SLEEPING && 3 - breaks > 4 * HP / HT ) {

                breaks++;

                phase = true;

                GameScene.add(Blob.seed(pos, 100, Miasma.class));

                BuffActive.add(this, Enraged.class, breaks * Random.Float( 5.0f, 6.0f ) );

                for (Mob mob : (Iterable<Mob>)Dungeon.level.mobs.clone()) {
                    if (mob instanceof Spawn) {
                        ((Spawn)mob).phase = true;
                        mob.sprite.idle();
                    }
                }

                if (Dungeon.visible[pos]) {
//                    sprite.showStatus( CharSprite.NEGATIVE, "enraged!" );
                    GLog.n("黏咕开始释放致命瘴气！");
                }

                sprite.idle();

                spend( TICK );
                return true;
            }

            return super.act();
        }

        @Override
        public void die( Object cause, Element dmg ) {

            yell( "咕……咕……" );

            super.die(cause, dmg);

	    	((SewerBossLevel)Dungeon.level).unseal();

            GameScene.bossSlain();

            Badges.validateBossSlain();

            for (Mob mob : (Iterable<Mob>)Dungeon.level.mobs.clone()) {
                if (mob instanceof Spawn) {
                    mob.die( cause, null );
                }
            }

            Blob blob = Dungeon.level.blobs.get( Miasma.class );

            if (blob != null) {
                blob.remove();
            }
        }

        @Override
        public void notice() {
            super.notice();
            if( enemySeen ) {
                yell("咕————！");
            }
        }

    }

    public static class Spawn extends Goo {

        public Spawn() {

            super();

            name = "黏咕分裂体";
            spriteClass = GooSprite.SpawnSprite.class;

            minDamage /= 2;
            maxDamage /= 2;

            EXP = 0;

        }

        private Goo mother;

        @Override
        public int dexterity() {
            return !phase ? super.dexterity() : 0 ;
        }

        @Override
        public boolean act() {

            if( mother == null ) {
                mother = mother();
            }

            if ( phase && mother != null && mother != this && Level.adjacent( pos, mother.pos ) ){

                Burning buff1 = buff( Burning.class );

                if( buff1 != null ){
                    BuffActive.add( mother, Burning.class, (float)buff1.getDuration() );
                }

                mother.heal( HP );
                die( this );


                Pushing.move( this, mother.pos, null );
                sprite.parent.add( new AlphaTweener( sprite, 0.0f, 0.1f ) );

                if( Dungeon.visible[ pos ] ) {
                    mother.sprite.showStatus( CharSprite.NEGATIVE, "吸收" );
                    GLog.n( "黏咕吸收了分裂体，发生了自愈！" );
                }

                return true;

            }

            if ( Level.water[pos] && HP < HT ) {
                HP++;
            }

            if( !phase && HP == HT ) {
                phase = true;
                sprite.idle();

                if( Dungeon.visible[ pos ] ){
                    sprite.showStatus( CharSprite.WARNING, "吸引" );
                    GLog.n( "一个黏咕分裂体正在变得活跃——不要让它待在水里！" );
                }

                spend( TICK );
                return true;
            }

            return super.act();
        }

        @Override
        protected boolean getCloser( int target ) {
            return phase && mother != null ?
                    super.getCloser( mother.pos ) :
                    super.getCloser( target );
        }

        @Override
        protected boolean canAttack( Char enemy ) {
            return !phase && super.canAttack( enemy );
        }
    }
}
