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

import com.consideredhamster.yapdcn.visuals.effects.Speck;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.tweeners.AlphaTweener;
import com.watabou.utils.Callback;
import com.consideredhamster.yapdcn.visuals.Assets;
import com.consideredhamster.yapdcn.Element;
import com.consideredhamster.yapdcn.Dungeon;
import com.consideredhamster.yapdcn.actors.Actor;
import com.consideredhamster.yapdcn.actors.Char;
import com.consideredhamster.yapdcn.visuals.effects.CellEmitter;
import com.consideredhamster.yapdcn.visuals.effects.MagicMissile;
import com.consideredhamster.yapdcn.visuals.effects.particles.ShadowParticle;
import com.consideredhamster.yapdcn.levels.Level;
import com.consideredhamster.yapdcn.misc.mechanics.Ballistica;
import com.consideredhamster.yapdcn.scenes.GameScene;
import com.consideredhamster.yapdcn.visuals.sprites.WraithSprite;
import com.watabou.utils.Random;

public class Wraith extends MobRanged {

	private static final float SPAWN_DELAY	= 2.0f;

	private static final float BLINK_CHANCE	= 0.125f;

    public Wraith() {
        this( Dungeon.depth );
    }

    public Wraith( int depth ) {

        super( depth / 6 + 1, depth + 1, false );

        name = "恶灵";
        info = "法术造物, 飞行, 瞬移, 生命吸取, 邪能魔弹";
        spriteClass = WraithSprite.class;

        minDamage += tier;
        maxDamage += tier;

        minDamage /= 2;
        maxDamage /= 2;

        HP = HT /= 2;

        flying = true;

        resistances.put( Element.Doom.class, Element.Resist.PARTIAL );
        resistances.put( Element.Frost.class, Element.Resist.PARTIAL );
        resistances.put( Element.Unholy.class, Element.Resist.PARTIAL );
        resistances.put( Element.Physical.class, Element.Resist.PARTIAL );

        resistances.put( Element.Body.class, Element.Resist.IMMUNE );
        resistances.put( Element.Mind.class, Element.Resist.IMMUNE );

        resistances.put( Element.Dispel.class, Element.Resist.VULNERABLE );
        resistances.put( Element.Knockback.class, Element.Resist.VULNERABLE );
    }

    @Override
    public boolean isMagical() {
        return true;
    }

    @Override
    public boolean ignoresAC() {
        return true;
    }

    @Override
    public int armorClass() {
        return 0;
    }

    private void blink() {

        ArrayList<Integer> cells = new ArrayList<>();

        for( Integer cell : Dungeon.level.getPassableCellsList() ){
            if( pos != cell && Level.fieldOfView[ cell ] ) {
                cells.add( cell );
            }
        }

        int newPos = !cells.isEmpty() ? Random.element( cells ) : pos ;

        if (Dungeon.visible[pos]) {
            CellEmitter.get(pos).start( ShadowParticle.UP, 0.01f, Random.IntRange(5, 10) );
        }

        if (Dungeon.visible[newPos]) {
            CellEmitter.get(newPos).start(ShadowParticle.MISSILE, 0.01f, Random.IntRange(5, 10));
        }

        ((WraithSprite)sprite).blink(pos, newPos);

        move( newPos );

        spend( 1 / moveSpeed() );

    }

//	private static final String DELAY = "delay";
//
//	@Override
//	public void storeInBundle( Bundle bundle ) {
//		super.storeInBundle( bundle );
//		bundle.put(DELAY, timeToJump);
//	}
//
//	@Override
//	public void restoreFromBundle( Bundle bundle ) {
//		super.restoreFromBundle(bundle);
//        timeToJump = bundle.getInt( DELAY );
//	}

	@Override
	public String description() {
		return
			"怨灵是来自于墓穴中渴望复仇的罪人之魂。作为飘渺的非实体，攻击能穿透任何盔甲，而且基本对正常武器免疫。";
	}



    @Override
    protected boolean canAttack( Char enemy ) {
        return Ballistica.cast(pos, enemy.pos, false, true) == enemy.pos;
    }

    @Override
    protected void onRangedAttack( int cell ) {

        MagicMissile.shadow(sprite.parent, pos, cell,
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

            enemy.damage( damageRoll(), this, Element.UNHOLY );

        } else {

            enemy.missed();

        }

        return true;
    }

    @Override
    protected boolean doAttack( Char enemy ) {

        if ( !rooted && Random.Float() < BLINK_CHANCE ) {

            blink();
            return true;

        } else {

            return super.doAttack( enemy );

        }
    }

    @Override
    protected boolean act() {

        if( Dungeon.hero.isAlive() && state != SLEEPING && !enemySeen
                && Level.distance( pos, Dungeon.hero.pos ) <= 2
                && detected( Dungeon.hero ) && detected( Dungeon.hero )
                ) {

            beckon( Dungeon.hero.pos );

        }

        return super.act();
    }

    @Override
    public int attackProc( Char enemy, int damage, boolean blocked ) {

        if ( distance( enemy ) <= 1 && isAlive() ) {

            int healed = Element.Resist.modifyValue( damage / 2, enemy, Element.BODY );

            if (healed > 0) {

                heal( healed );

                if( sprite.visible ) {
                    sprite.emitter().burst(Speck.factory(Speck.HEALING), 1);
                }
            }
        }

        return damage;
    }

    @Override
    public boolean reset() {
        state = HUNTING;
        pos = Dungeon.level.randomRespawnCell();
        return true;
    }

    public static ArrayList<Wraith> spawnAround( int pos, int amount ) {
        return spawnAround( Dungeon.depth, pos, amount );
    }

    public static ArrayList<Wraith> spawnAround( int depth, int pos, int amount ) {

        ArrayList<Wraith> wraiths = new ArrayList<>();

        if( amount > 0 ) {
            ArrayList<Integer> candidates = new ArrayList<Integer>();

            for (int n : Level.NEIGHBOURS8) {
                int cell = pos + n;
                if (!Level.solid[cell] && Actor.findChar(cell) == null) {
                    candidates.add(cell);
                }
            }

            for (int i = 0; i < amount; i++) {
                if (candidates.size() > 0) {
                    int o = Random.Int( candidates.size() );
                    wraiths.add( spawnAt( depth, candidates.get(o) ) );
                    candidates.remove( o );
                } else {
                    break;
                }
            }
        }

        return wraiths;
    }

    public static Wraith spawnAt( int pos ) {
        return spawnAt( Dungeon.depth, pos );
    }

    public static Wraith spawnAt( int depth, int pos ) {

        if (!Level.solid[pos] && Actor.findChar( pos ) == null) {

            Wraith w = new Wraith( depth );

            w.pos = pos;
            w.special = true;
            w.enemySeen = true;
            w.state = w.HUNTING;

            GameScene.add( w, SPAWN_DELAY );

            w.sprite.alpha( 0 );
            w.sprite.parent.add( new AlphaTweener( w.sprite, 1, 0.5f ) );

            w.sprite.emitter().burst( ShadowParticle.CURSE, 5 );

            return w;
        } else {
            return null;
        }
    }
}
