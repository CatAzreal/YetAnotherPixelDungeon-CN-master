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

import com.consideredhamster.yapdcn.actors.buffs.Buff;
import com.consideredhamster.yapdcn.actors.buffs.BuffActive;
import com.consideredhamster.yapdcn.actors.buffs.bonuses.Enraged;
import com.consideredhamster.yapdcn.visuals.sprites.CharSprite;
import com.consideredhamster.yapdcn.misc.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.consideredhamster.yapdcn.visuals.Assets;
import com.consideredhamster.yapdcn.Badges;
import com.consideredhamster.yapdcn.Element;
import com.consideredhamster.yapdcn.Statistics;
import com.consideredhamster.yapdcn.Dungeon;
import com.consideredhamster.yapdcn.actors.Actor;
import com.consideredhamster.yapdcn.actors.Char;
import com.consideredhamster.yapdcn.visuals.effects.CellEmitter;
import com.consideredhamster.yapdcn.visuals.effects.Speck;
import com.consideredhamster.yapdcn.items.misc.Gold;
import com.consideredhamster.yapdcn.items.keys.SkeletonKey;
import com.consideredhamster.yapdcn.items.scrolls.ScrollOfClairvoyance;
import com.consideredhamster.yapdcn.items.weapons.throwing.Shurikens;
import com.consideredhamster.yapdcn.levels.Level;
import com.consideredhamster.yapdcn.levels.Terrain;
import com.consideredhamster.yapdcn.misc.mechanics.Ballistica;
import com.consideredhamster.yapdcn.scenes.GameScene;
import com.consideredhamster.yapdcn.visuals.sprites.MissileSprite;
import com.consideredhamster.yapdcn.visuals.sprites.TenguSprite;
import com.watabou.utils.Random;

public class Tengu extends MobRanged {

	private static final int JUMP_DELAY = 8;

    private int timeToJump = 0;
    protected int breaks = 0;

    public Tengu() {

        super( 3, 15, true );

        name = Dungeon.depth == Statistics.deepestFloor ? "天狗" : "天狗之残影";
        spriteClass = TenguSprite.class;

        loot = Gold.class;
        lootChance = 4f;

        resistances.put(Element.Mind.class, Element.Resist.PARTIAL);
        resistances.put(Element.Body.class, Element.Resist.PARTIAL);

        resistances.put( Element.Dispel.class, Element.Resist.IMMUNE );
    }

    @Override
    public float attackDelay() {
        return buff( Enraged.class ) == null ? 1.0f : 0.5f ;
    }

    @Override
    protected float healthValueModifier() { return 0.25f; }

    @Override
    public int damageRoll() {
        return buff( Enraged.class ) == null ? super.damageRoll() : super.damageRoll() / 2 ;
    }

    @Override
    protected void onRangedAttack( int cell ) {
        ((MissileSprite)sprite.parent.recycle( MissileSprite.class )).
                reset(pos, cell, new Shurikens(), new Callback() {
                    @Override
                    public void call() {
                        onAttackComplete();
                    }
                });

        super.onRangedAttack(cell);
    }
	
	@Override
	protected boolean getCloser( int target ) {
		if (!rooted && Level.fieldOfView[target]) {
			jump();
			return true;
		} else {
			return super.getCloser( target );
		}
	}
	
	@Override
	protected boolean canAttack( Char enemy ) {
		return Ballistica.cast( pos, enemy.pos, false, true ) == enemy.pos;
	}
	
	@Override
	protected boolean doAttack( Char enemy ) {
		timeToJump++;
		if ( !rooted && timeToJump >= JUMP_DELAY ) {
			jump();
			return true;
		} else {
			return super.doAttack(enemy);
		}
	}

    @Override
    public void damage( int dmg, Object src, Element type ) {

        if (HP <= 0) {
            return;
        }

        if( buff( Enraged.class ) != null ) {
            dmg /= 2;
        }

        timeToJump++;

        super.damage( dmg, src, type );
    }

    @Override
    public void remove( Buff buff ) {

        if( buff instanceof Enraged ) {
            sprite.showStatus( CharSprite.NEUTRAL, "..." );
            GLog.i( "天狗已不再愤怒。" );
        }

        super.remove(buff);
    }

    @Override
    public boolean act() {

        if( 3 - breaks > 4 * HP / HT ) {

            breaks++;

            BuffActive.add(this, Enraged.class, breaks * Random.Float(2.5f, 5.0f));

            if (Dungeon.visible[pos]) {
//                sprite.showStatus( CharSprite.NEGATIVE, "enraged!" );
                GLog.n( "天狗被激怒了！" );
            }

            sprite.idle();

            spend( TICK );
            return true;

        } else if( buff( Enraged.class ) != null ) {

            timeToJump++;

        }

        return super.act();
    }
	
	private void jump() {

        timeToJump = 0;

        for( int i = 0 ; i < 4 ; i++ ){
            int trapPos;
            do{
                trapPos = Random.Int( Level.LENGTH );
            }
            while( !Level.fieldOfView[ trapPos ] || !Level.passable[ trapPos ] || Actor.findChar( trapPos ) != null );

            if( Dungeon.level.map[ trapPos ] == Terrain.INACTIVE_TRAP ){
                Level.set( trapPos, Terrain.BLADE_TRAP );
                GameScene.updateMap( trapPos );
                ScrollOfClairvoyance.discover( trapPos );
            }
        }

        ArrayList<Integer> cells = new ArrayList<>();

        for( Integer cell : Dungeon.level.filterTrappedCells( Dungeon.level.getPassableCellsList() ) ){

            if( pos != cell && !Level.adjacent( pos, cell ) && Level.fieldOfView[ cell ] ) {
                cells.add( cell );
            }

        }

		int newPos = !cells.isEmpty() ? Random.element( cells ) : pos ;

        sprite.move( pos, newPos );
        move( newPos );

        if( Dungeon.visible[ newPos ] ){
            CellEmitter.get( newPos ).burst( Speck.factory( Speck.WOOL ), 6 );
            Sample.INSTANCE.play( Assets.SND_PUFF );
        }

		spend( 1 / moveSpeed() );
	}
	
	@Override
	public void notice() {
		super.notice();
        if( enemySeen && HP == HT && breaks == 0 ) {
            yell( "Gotcha, " + Dungeon.hero.heroClass.title() + "!" );
        }
	}



    @Override
    public float awareness(){
        return 2.0f;
    }

    @Override
    public void die( Object cause, Element dmg ) {

        yell( "终于…解脱了……" );

        super.die( cause, dmg );

        GameScene.bossSlain();
        Badges.validateBossSlain();
        Dungeon.level.drop( new SkeletonKey(), pos ).sprite.drop();

    }
	
	@Override
	public String description() {
		return
			"天狗是远古刺客组织的成员，组织的名字也叫天狗。这些刺客以大量使用手里剑和陷阱而闻名。";
	}

    private static final String TIME_TO_JUMP	= "timeToJump";
    private static final String BREAKS	= "breaks";

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle(bundle);
        bundle.put( BREAKS, breaks );
        bundle.put( TIME_TO_JUMP, timeToJump );
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle(bundle);
        breaks = bundle.getInt( BREAKS );
        timeToJump = bundle.getInt( TIME_TO_JUMP );
    }
}
