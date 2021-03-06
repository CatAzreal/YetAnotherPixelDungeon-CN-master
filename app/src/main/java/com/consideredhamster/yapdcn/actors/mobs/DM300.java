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

import com.consideredhamster.yapdcn.Badges;
import com.consideredhamster.yapdcn.Element;
import com.consideredhamster.yapdcn.Dungeon;
import com.consideredhamster.yapdcn.Statistics;
import com.consideredhamster.yapdcn.actors.Actor;
import com.consideredhamster.yapdcn.actors.Char;
import com.consideredhamster.yapdcn.actors.buffs.Buff;
import com.consideredhamster.yapdcn.actors.buffs.bonuses.Enraged;
import com.consideredhamster.yapdcn.actors.buffs.debuffs.Vertigo;
import com.consideredhamster.yapdcn.actors.buffs.BuffActive;
import com.consideredhamster.yapdcn.items.Heap;
import com.consideredhamster.yapdcn.visuals.effects.CellEmitter;
import com.consideredhamster.yapdcn.visuals.effects.Speck;
import com.consideredhamster.yapdcn.visuals.effects.particles.ElmoParticle;
import com.consideredhamster.yapdcn.items.misc.Gold;
import com.consideredhamster.yapdcn.items.keys.SkeletonKey;
import com.consideredhamster.yapdcn.levels.Level;
import com.consideredhamster.yapdcn.levels.Terrain;
import com.consideredhamster.yapdcn.scenes.GameScene;
import com.consideredhamster.yapdcn.visuals.sprites.CharSprite;
import com.consideredhamster.yapdcn.visuals.sprites.DM300Sprite;
import com.consideredhamster.yapdcn.misc.utils.GLog;
import com.watabou.noosa.Camera;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class DM300 extends MobHealthy {

    protected int breaks = 0;

    public DM300() {

        super( 4, 20, true );

        // 450

        dexterity /= 2; // yes, we divide it again
        armorClass *= 2; // and yes, we multiply it back

        name = Dungeon.depth == Statistics.deepestFloor ? "DM-300" : "DM-400";
        spriteClass = DM300Sprite.class;

        loot = Gold.class;
        lootChance = 4f;

        resistances.put(Element.Flame.class, Element.Resist.PARTIAL );
        resistances.put(Element.Frost.class, Element.Resist.PARTIAL );
        resistances.put(Element.Unholy.class, Element.Resist.PARTIAL );

        resistances.put(Element.Mind.class, Element.Resist.IMMUNE );
        resistances.put(Element.Body.class, Element.Resist.IMMUNE );
        resistances.put(Element.Dispel.class, Element.Resist.IMMUNE );

        resistances.put( Element.Knockback.class, Element.Resist.PARTIAL );
        resistances.put( Element.Doom.class, Element.Resist.PARTIAL );
    }

    @Override
    public boolean isMagical() {
        return false;
    }

    @Override
    public float awareness(){
        return 1.0f;
    }

    @Override
    public float moveSpeed() {
        return buff( Enraged.class ) != null ? 1.0f : 0.75f + breaks * 0.05f;
    }

    @Override
    protected float healthValueModifier() {
        return 0.25f;
    }

    @Override
    public void damage( int dmg, Object src, Element type ) {

        if( buff( Enraged.class ) != null ) {
            dmg /= 2;
        }

        super.damage( dmg, src, type );
    }
	
	@Override
	public void move( int step ) {
		super.move( step );

        if( buff( Enraged.class ) != null ) {

            dropBoulders( step + Level.NEIGHBOURS8[ Random.Int( Level.NEIGHBOURS8.length ) ], damageRoll() / 2 );

            dropBoulders( step + Level.NEIGHBOURS12[ Random.Int( Level.NEIGHBOURS12.length ) ], damageRoll() / 3 );

            Camera.main.shake( 2, 0.1f );

        } else if (Dungeon.level.map[step] == Terrain.INACTIVE_TRAP && HP < HT) {
			
			HP += ( HT - HP ) / 5;
			sprite.emitter().burst( ElmoParticle.FACTORY, 5 );
			
			if (Dungeon.visible[step] && Dungeon.hero.isAlive()) {
				GLog.n( "DM-300修复了自己！" );
			}
		}



	}

    @Override
    public void remove( Buff buff ) {

        if( buff instanceof Enraged ) {
            sprite.showStatus( CharSprite.NEUTRAL, "..." );
            GLog.i("DM-300已不再愤怒。");
        }

        super.remove(buff);
    }

    @Override
    public boolean act() {

        if( 3 - breaks > 4 * HP / HT ) {

            breaks++;

            BuffActive.add(this, Enraged.class, breaks * Random.Float(8.0f, 12.0f));

            if (Dungeon.visible[pos]) {
//                sprite.showStatus( CharSprite.NEGATIVE, "enraged!" );
                GLog.n( "DM-300被激怒了！" );
            }

            sprite.idle();

            spend( TICK );
            return true;

        }

        return super.act();
    }
	
	@Override
	public void die( Object cause, Element dmg ) {

        yell( "任务失败，系统关闭。" );

        super.die( cause, dmg );

        GameScene.bossSlain();
        Dungeon.level.drop( new SkeletonKey(), pos ).sprite.drop();

        Badges.validateBossSlain();

	}

	@Override
	public void notice() {
		super.notice();
        if( enemySeen && HP == HT && breaks == 0 ) {
            yell("检测到未经授权的人员。");
        }
	}
	
	@Override
	public String description() {
		return
			"数个世纪前矮人们制造了这个机器。但此后，矮人们开始使用魔像、元素生物甚至是恶魔来替换机器，最终导致其文明的衰败。DM-300及类似的机器通常用于建设和挖掘，某些情况下，也可以用于城防。";
	}

    public void dropBoulders( int pos, int power ) {

        if( pos < 0 || pos > Level.LENGTH )
            return;

        if( Level.solid[pos] )
            return;

        Char ch = Actor.findChar(pos);
        if (ch != null) {

            int dmg = Char.absorb( Random.IntRange( power / 2 , power ), ch.armorClass() );
//                    int dmg = Math.max(0, Random.IntRange(Dungeon.depth, Dungeon.depth + 10) - Random.NormalIntRange(0, ch.armorClass()));

            ch.damage(dmg, this, Element.PHYSICAL);

            if ( ch.isAlive() ) {
                BuffActive.addFromDamage(ch, Vertigo.class, dmg);
            }
        }

        Heap heap = Dungeon.level.heaps.get(pos);
        if (heap != null) {
            heap.shatter();
        }

        if (Dungeon.visible[pos]) {

            CellEmitter.get(pos).start( Speck.factory(Speck.ROCK), 0.1f, 4 );

        }
    }

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
}
