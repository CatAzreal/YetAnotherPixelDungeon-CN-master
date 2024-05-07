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

import com.consideredhamster.yapdcn.actors.buffs.BuffActive;
import com.consideredhamster.yapdcn.actors.buffs.bonuses.Invisibility;
import com.consideredhamster.yapdcn.actors.buffs.debuffs.Debuff;
import com.consideredhamster.yapdcn.actors.hazards.BombHazard;
import com.consideredhamster.yapdcn.visuals.effects.particles.ShadowParticle;
import com.consideredhamster.yapdcn.misc.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.consideredhamster.yapdcn.visuals.Assets;
import com.consideredhamster.yapdcn.Badges;
import com.consideredhamster.yapdcn.Element;
import com.consideredhamster.yapdcn.Statistics;
import com.consideredhamster.yapdcn.Dungeon;
import com.consideredhamster.yapdcn.actors.Char;
import com.consideredhamster.yapdcn.visuals.effects.CellEmitter;
import com.consideredhamster.yapdcn.visuals.effects.Speck;
import com.consideredhamster.yapdcn.items.misc.Gold;
import com.consideredhamster.yapdcn.items.keys.SkeletonKey;
import com.consideredhamster.yapdcn.items.weapons.throwing.Shurikens;
import com.consideredhamster.yapdcn.levels.Level;
import com.consideredhamster.yapdcn.misc.mechanics.Ballistica;
import com.consideredhamster.yapdcn.scenes.GameScene;
import com.consideredhamster.yapdcn.visuals.sprites.MissileSprite;
import com.consideredhamster.yapdcn.visuals.sprites.TenguSprite;
import com.watabou.utils.Random;

public class Tengu extends MobRanged {

	private static final int JUMP_DELAY = 18;

	private final static int LINE_GREETINGS = 0;
    private final static int LINE_JUMPCLOSER = 1;
    private final static int LINE_JUMPFURTHER = 2;
    private final static int LINE_HIDEAWAY = 3;
    private final static int LINE_REVEALED = 4;
    private final static int LINE_SHOWINGUP = 5;
    private final static int LINE_NEARDEATH = 6;
    private final static int LINE_LOOKINGFOR = 7;
    private final static int LINE_DISCOVERED = 8;

    private final static String[][] LINES = {

            {
                    "哈哈！欢迎来到敝舍！",
                    "欢迎欢迎！你是来找乐子的吗？",
                    "哦，你总算是来了！快让好戏开场吧！",
            },
            {
                    "来，我们靠近一点...",
                    "嗨！你好啊！",
                    "碰到了！你输啦！",
                    "哈哈！找到你了！",
                    "嗷呜!..吓到了吗？哈哈哈！",
            },
            {
                    "我们还是保持一点距离的好...",
                    "嘿，这是给你准备的小礼物！",
                    "别在那傻站着！快过来抓我啊！",
                    "哈哈，抓不到我！",
                    "来，接住这个！哈哈哈哈哈！",
            },
            {
                    "好吧，给我点时间歇歇。祝你玩的开心！",
                    "看来我得暂时把你交给这些家伙了。",
                    "哇哦，你还是个硬茬啊！来来来，陪我的弟兄们玩玩。",
                    "我给你找了些新朋友！不客气！",
                    "玩的真开心啊！该进入中场时间了。",
            },
            {
                    "哦！被你找到了！真厉害啊！",
                    "天哪！你这么急着要陪我继续玩吗？",
                    "嘿！我还没休息够呢。",
            },
            {
                    "嘿，猜猜是谁？我又回来了！",
                    "又见面了！希望你没闲着！",
                    "回来了！我们继续？",
                    "这边这边！你怎么动作这么慢？",
                    "天狗参上！哈哈哈哈！",
            },
            {
                    "嘿嘿...玩的...挺开心的...",
                    "看来...我们的小游戏...要结束了...",
                    "你玩的很棒...探求者...玩的很棒...",
                    "看起来我终于解脱了...谢谢你，探求者。",
                    "哦？看起来我输了...那就这样吧。",
            },
            {
                    "你跑哪里去了？",
                    "你在这里吗？还是...这里？",
                    "你为什么躲着我？",
                    "嘿，别躲了！躲猫猫可是我的专利！",
                    "快出来吧！不然这场游戏可玩不下去了！",
            },
            {
                    "嘿！找到你了！",
                    "啊哈！原来你在这！",
                    "躲得还不错嘛！",
                    "你可没法在我这一直躲下去的！",
                    "嘿，你这躲得可不怎么样啊。",
            }
    };

    private int timeToJump = 0;
    protected int breaks = 0;

    public Tengu() {

        super( 3, 15, true );

        name = Dungeon.depth == Statistics.deepestFloor ? "天狗" : "天狗之残影";
        info = "头目!";

        spriteClass = TenguSprite.class;

        loot = Gold.class;
        lootChance = 4f;

        resistances.put(Element.Mind.class, Element.Resist.PARTIAL);
        resistances.put(Element.Body.class, Element.Resist.PARTIAL);

        resistances.put( Element.Dispel.class, Element.Resist.IMMUNE );
    }

    @Override
    public float attackSpeed() {
        return isRanged() ? super.attackSpeed() : super.attackSpeed() * 2;
    }

    @Override
    protected float healthValueModifier() { return 0.25f; }

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
		if (!rooted ) {
            if ( enemy != null ) {
                if ( canSeeTarget( enemy ) ) {
                    yell(LINE_JUMPCLOSER);
                    jumpCloser();
                } else {
                    if( enemy.stealth() <= 0 || Random.Float() < 0.1 / enemy.stealth() ) {

                        if( enemy.invisible == 0 ) {
                            yell( LINE_DISCOVERED );
                        }

                        jumpCloser();

                    } else {

                        if( Random.Int( 10 ) == 0 ) {
                            yell( LINE_LOOKINGFOR );
                        }

                        jumpAway();

                    }
                }
            } else {
                jumpAway();
            }
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

		if ( !rooted && timeToJump >= ( JUMP_DELAY - breaks * 2 ) ) {
            if( enemy != null && !Level.adjacent( pos, enemy.pos ) ) {
                jumpCloser();
                yell( LINE_JUMPCLOSER );
            } else {
                yell( LINE_JUMPFURTHER );
                jumpAway();
            }

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

        if( buff( Invisibility.class ) != null ) {
            yell( LINE_REVEALED );
        }

        timeToJump++;

        super.damage( dmg, src, type );
    }

    @Override
    public boolean act() {

        if( 3 - breaks > 4 * HP / HT ) {

            yell( LINE_HIDEAWAY );
            GLog.i( "Tengu disappears somewhere!" );

            breaks++;
            hideAway();

            return true;

        } else if( buff( Invisibility.class ) != null ) {

            if( shadowCount() > 0 ) {

                heal( 1 );
                sprite.idle();
                spend( TICK );
                next();

                return true;

            } else {

                Invisibility.dispel( this );

                enemy = Dungeon.hero;
                jumpCloser();

                yell( LINE_SHOWINGUP );

                return true;

            }

        } else {

            timeToJump++;

        }

        return super.act();
    }

    private void jumpCloser() {

        timeToJump = 0;
        int newPos = pos;

        ArrayList<Integer> cells = new ArrayList<>();

        for (Integer cell : Dungeon.level.filterTrappedCells(Dungeon.level.getPassableCellsList())) {
            if (pos != cell && Level.adjacent( enemy.pos, cell ) ) {
                cells.add(cell);
            }
        }

        if (!cells.isEmpty()) {
            newPos = Random.element(cells);

            if( shadowCount() < breaks && canSeeTarget( enemy ) ) {
                spawnShadow(pos);
            }
        }

        if( Dungeon.visible[ pos ] ){
            CellEmitter.get( pos ).burst( Speck.factory( Speck.WOOL ), 6 );
            Sample.INSTANCE.play( Assets.SND_PUFF );
        }

        if( enemy != null ) {
            beckon( enemy.pos );
        }

        sprite.move( pos, newPos );
        move( newPos );

        spend( 1 / moveSpeed() );
    }

    private void jumpAway() {

        timeToJump = 0;
        int newPos = pos;

        ArrayList<Integer> cells = new ArrayList<>();

        for (Integer cell : Dungeon.level.filterTrappedCells(Dungeon.level.getPassableCellsList())) {
            if ( pos != cell && !Level.adjacent( pos, cell ) && Level.distance( pos, cell ) <= 6 ) {
                cells.add(cell);
            }
        }

        if (!cells.isEmpty()) {
            newPos = Random.element(cells);

            if( Level.adjacent( pos, enemy.pos ) && canSeeTarget( enemy ) ) {
                spawnBomb(pos);
            }
        }

        if( Dungeon.visible[ pos ] ){
            CellEmitter.get( pos ).burst( Speck.factory( Speck.WOOL ), 6 );
            Sample.INSTANCE.play( Assets.SND_PUFF );
        }

        sprite.move( pos, newPos );
        move( newPos );

        spend( 1 / moveSpeed() );
    }

    private void spawnBomb( int cell ) {

        BombHazard hazard = new BombHazard();
        hazard.setValues( cell, BombHazard.BOMB_NINJA, Random.Int( 1, 3 + breaks ), 0 );
        GameScene.add( hazard );
        ( (BombHazard.BombSprite) hazard.sprite ).appear();

    }

    private void spawnShadow( int cell ) {

        final TenguShadow clone = new TenguShadow();

        clone.pos = cell;
//        clone.HT = clone.HP = 1;
        clone.EXP = 0;

        clone.state = clone.HUNTING;
        clone.target = target;

        clone.beckon( target );

        sprite.turnTo( pos, cell );

        GameScene.add( clone, TICK );
        clone.sprite.emitter().burst( ShadowParticle.CURSE, 6 );

    }

    private void hideAway() {

        Debuff.removeAll( this );
        BuffActive.add( this, Invisibility.class, HT - HP );

        jumpAway();
        clearShadows();

        ArrayList<Integer> cells = new ArrayList<>();
        for (Integer cell : Dungeon.level.filterTrappedCells(Dungeon.level.getPassableCellsList())) {
            if ( pos != cell && !Level.adjacent( pos, cell ) && Level.distance( pos, cell  ) <= 4 ) {
                cells.add(cell);
            }
        }

        for( int i = 0 ; i < breaks + 1 ; i++ ) {
            if (!cells.isEmpty()) {
                Integer cell = Random.element(cells);
                cells.remove(cell);
                spawnShadow(cell);
            }
        }
    }

    private int shadowCount() {
        int result = 0;

        for( Mob mob : Dungeon.level.mobs ) {
            if( mob instanceof TenguShadow ) {
                result++;
            }
        }

        return result;
    }

    private void clearShadows() {

        for( Mob mob : (Iterable<Mob>)Dungeon.level.mobs.clone() ) {
            if( mob instanceof TenguShadow ) {
                mob.die( null, null );
            }
        }
    }

    @Override
    public void notice() {
        super.notice();

        if ( enemySeen && HP == HT && breaks == 0 ) {
            yell( LINE_GREETINGS );
        }
    }

    private void yell( int line ) {
        yell( LINES[ line ][ Random.Int( LINES[ line ].length ) ] );
    }

    @Override
    public float awareness(){
        return 2.0f;
    }

    @Override
    public void die( Object cause, Element dmg ) {

        yell( LINE_NEARDEATH );
        clearShadows();

        super.die( cause, dmg );

        GameScene.bossSlain();
        Badges.validateBossSlain();
        Dungeon.level.drop( new SkeletonKey(), pos ).sprite.drop();

    }
	
	@Override
	public String description() {
		return
			"天狗是远古刺客组织的成员，组织的名字也叫天狗。这些刺客以多样的武术和暗影魔法而闻名。";
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
