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

import com.consideredhamster.yapdcn.Difficulties;
import com.consideredhamster.yapdcn.Dungeon;
import com.consideredhamster.yapdcn.Element;
import com.consideredhamster.yapdcn.actors.Actor;
import com.consideredhamster.yapdcn.actors.Char;
import com.consideredhamster.yapdcn.actors.buffs.BuffActive;
import com.consideredhamster.yapdcn.actors.buffs.debuffs.Burning;
import com.consideredhamster.yapdcn.actors.buffs.debuffs.Frozen;
import com.consideredhamster.yapdcn.actors.special.Pushing;
import com.consideredhamster.yapdcn.levels.Level;
import com.consideredhamster.yapdcn.misc.utils.GLog;
import com.consideredhamster.yapdcn.scenes.GameScene;
import com.consideredhamster.yapdcn.visuals.sprites.CharSprite;
import com.consideredhamster.yapdcn.visuals.sprites.GooSprite;
import com.watabou.noosa.tweeners.AlphaTweener;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class GooSpawn extends MobEvasive {

    private static final float SPLIT_DELAY	= 1f;

    private static final int SPAWN_HEALTH = 15;

    public boolean phase = false;

    public Goo mother = null;

    public GooSpawn() {

        super( 2, 3, false );

        name = "粘咕分裂体";
        info = "法术造物, 自我分裂";

        spriteClass = GooSprite.SpawnSprite.class;

        HT = SPAWN_HEALTH;

        if( Dungeon.difficulty == Difficulties.NORMAL ) {
            HT = Random.NormalIntRange( HT, HT * 2);
        } else if( Dungeon.difficulty > Difficulties.NORMAL ) {
            HT = HT * 2;
        }

        minDamage /= 2;
        maxDamage /= 2;

        HP  = HT;
        EXP = 5;

        armorClass = 0;

        resistances.put( Element.Acid.class, Element.Resist.PARTIAL );
        resistances.put( Element.Flame.class, Element.Resist.PARTIAL );

        resistances.put( Element.Mind.class, Element.Resist.IMMUNE );
        resistances.put( Element.Body.class, Element.Resist.IMMUNE );
        resistances.put( Element.Ensnaring.class, Element.Resist.IMMUNE );

    }

    @Override
    public boolean isMagical() {
        return true;
    }

    @Override
    public int dexterity() {
        return !phase ? super.dexterity() : 0 ;
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

	@Override
	public void damage( int dmg, Object src, Element type ) {

        if (HP <= 0) {
            return;
        }

        if ( type == Element.PHYSICAL && dmg > 1 && dmg < HP && dmg > Random.Int( SPAWN_HEALTH ) ) {

            split( this, dmg );

        }

        super.damage( dmg, src, type );
	}

    @Override
    public boolean act() {

        if ( phase && mother != null && Level.adjacent( pos, mother.pos ) ){

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
                GLog.n( "黏咕吸收了分裂体，开始自愈！" );
            }

            return true;

        }

        if ( Level.water[pos] && HP < HT ) {
            HP++;
        }

        if( !phase && mother != null && HP == HT ) {
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

    public static GooSpawn split( Mob spawner, int dmg ) {

        ArrayList<Integer> candidates = new ArrayList<Integer>();
        boolean[] passable = Level.passable;

        for (int n : Level.NEIGHBOURS8) {
            if (passable[spawner.pos + n] && Actor.findChar(spawner.pos + n) == null) {
                candidates.add(spawner.pos + n);
            }
        }

        if (candidates.size() > 0) {

            final GooSpawn clone = new GooSpawn();

            clone.pos = spawner.pos;
            clone.HT = Math.min( dmg, spawner.HP );

            clone.HP = 1;
            clone.EXP = 0;

            clone.state = clone.HUNTING;

            GameScene.add( clone, SPLIT_DELAY );

            Pushing.move( clone, Random.element( candidates ), new Callback() {
                @Override
                public void call(){
                    Actor.occupyCell( clone );
                    Dungeon.level.press(clone.pos, clone);
                }
            } );

            Burning buff1 = spawner.buff( Burning.class );

            if ( buff1 != null ) {
                BuffActive.addFromDamage( clone, Burning.class, buff1.getDuration() );
            }

            Frozen buff2 = spawner.buff( Frozen.class );

            if ( buff2 != null ) {
                BuffActive.addFromDamage( clone, Frozen.class, buff2.getDuration() );
            }

            return clone;
        }

        return null;
    }

    @Override
    public String description() {
        return  "我们对粘咕所知甚少。它甚至很有可能不是一个生物，而是下水道表面聚集的邪恶物质得到基本智能而产生的实体。";
    }

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
}
