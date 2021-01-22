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
package com.consideredhamster.yapdcn.actors.buffs.special;

import com.consideredhamster.yapdcn.actors.buffs.Buff;
import com.consideredhamster.yapdcn.items.rings.RingOfVitality;
import com.consideredhamster.yapdcn.levels.Level;
import com.consideredhamster.yapdcn.scenes.GameScene;
import com.consideredhamster.yapdcn.actors.hero.Hero;
import com.consideredhamster.yapdcn.items.rings.RingOfSatiety;
import com.consideredhamster.yapdcn.visuals.ui.BuffIndicator;
import com.consideredhamster.yapdcn.misc.utils.GLog;
import com.watabou.utils.Bundle;

public class Satiety extends Buff {

    public static final float POINT	= 1f;
    public static final float REGENERATION_RATE	= 0.002f;

    public static final float MAXIMUM	= 1000f;
    public static final float STARVING  = 0f;

    public static final float DEFAULT	= MAXIMUM * 0.75f;
    public static final float PARTIAL   = MAXIMUM * 0.25f;

    public static final float STARVING_HALF = MAXIMUM * -0.25f;
    public static final float STARVING_FULL = MAXIMUM * -0.50f;

	private static final String TXT_NOT_SATIATED	= "你不再感到饱腹。";
	private static final String TXT_NOT_HUNGRY		= "你不再感到饥饿。";
	private static final String TXT_NOT_STARVING	= "你不再感到饥肠辘辘。";

	private static final String TXT_SATIATED		= "你吃的很饱！";
	private static final String TXT_HUNGRY		    = "你开始饿了。";
	private static final String TXT_STARVING	    = "你感到饥肠辘辘！";
	private static final String TXT_STARVING_HALF   = "你饿的更严重了！";
	private static final String TXT_STARVING_FULL   = "你饿的快要死了！";

	private static final String TXT_AWAKE_HUNGRY	= "你被肚里传来的叫声惊醒。";
	private static final String TXT_AWAKE_STARVING	= "你被肚里传来的空腹疼痛惊醒。";

	private float remaining = MAXIMUM;
    private float surplus = 0.0f;

	private static final String LEVEL	= "remaining";
    private static final String SURPLUS	= "surplus";

	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle( bundle );
		bundle.put( LEVEL, remaining );
		bundle.put( SURPLUS, surplus );
	}
	
	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle( bundle );
		remaining = bundle.getFloat( LEVEL );
        surplus = bundle.getFloat( SURPLUS );
	}
	
	@Override
	public boolean act() {

		if (target.isAlive()) {
			
			Hero hero = (Hero)target;

            float modifier = REGENERATION_RATE * hero.HT
//                * ( 1.0f + ( hero.lvl - 1 ) * 0.1f + hero.strBonus * 0.1f )
                ;

			if ( remaining <= STARVING ) {

                if ( remaining <= STARVING_FULL ){
                    modifier *= 2.0f;
                } else if ( remaining <= STARVING_HALF ){
                    modifier *= 1.0f;
                } else {
                    modifier *= 0.0f;
                }

                surplus -= modifier;

            } else {

                modifier *= target.ringBuffsHalved(RingOfVitality.Vitality.class);

                if( hero.restoreHealth && !Level.water[target.pos] ) {
                    modifier *= 3.0f;
                }

                if ( remaining > DEFAULT ) {
                    modifier *= 1.5f;
                } else if ( remaining > PARTIAL ) {
                    modifier *= 1.0f;
                } else {
                    modifier *= 0.5f;
                }

                surplus += modifier;

			}

            if( surplus >= 1.0f ) {

                if( target.HP < target.HT ){

                    target.HP = Math.min( target.HT, target.HP + (int) surplus );

                    if( target.HP == target.HT && ( (Hero) target ).restoreHealth ){

                        ( (Hero) target ).interrupt(
                            Level.water[ target.pos ] ?
                            "你睡得很不舒服。下次最好别躺在水滩上睡着。" :
                            "你精神抖擞地醒来。", !Level.water[ target.pos ]
                        );

                    }

                }

                surplus = surplus % 1.0f;

            } else if( surplus <= -1.0f ) {

                if( hero.HP < hero.HT / 5 ){
                    GameScene.flash( 0x330000 );
                } else if( hero.HP < hero.HT / 4 ){
                    GameScene.flash( 0x220000 );
                } else if( hero.HP < hero.HT / 3 ){
                    GameScene.flash( 0x110000 );
                }

                // let's be nice here and give the player one last chance to eat something
                if( hero.HP > 1 && ( hero.HP + (int) surplus <= 1 ) ){
                    hero.HP = 1;
                    hero.interrupt();
                    GLog.n( "你即将因饥饿而死！" );
                } else {
                    hero.HP = Math.max( 0, hero.HP + (int) surplus );
                }

                surplus = surplus % 1.0f;

                if( !target.isAlive() ){

                    target.die( this, null );

                }

            }

            decrease( POINT );
			spend( TICK );
			
		} else {
			
			deactivate();
			
		}

		return true;
	}

    public void setValue( float value ) {
        remaining = value;
        surplus = 0.0f;
    }

    public void increase( float value ) {

        if( remaining < STARVING ){
            remaining = STARVING;
        }

        float newLevel = Math.min( MAXIMUM, remaining + value * target.ringBuffsThirded( RingOfSatiety.Satiety.class ) );

        if ( remaining <= DEFAULT && newLevel > DEFAULT ) {

            GLog.i( TXT_SATIATED );

        } else if ( remaining <= PARTIAL && newLevel > PARTIAL ) {

            GLog.i( TXT_NOT_HUNGRY );

        } else if ( remaining <= STARVING && newLevel > STARVING ) {

            GLog.i( TXT_NOT_STARVING );

        }

        remaining = newLevel;

    }

    public void decrease( float value ) {

        float newLevel = Math.max( STARVING_FULL, remaining - value / target.ringBuffs( RingOfSatiety.Satiety.class ) );

        if ( remaining > DEFAULT && newLevel <= DEFAULT ) {

            GLog.i( TXT_NOT_SATIATED );

        } else if ( remaining > PARTIAL && newLevel <= PARTIAL ) {

            ((Hero)target).interrupt( TXT_AWAKE_HUNGRY );
            GLog.w( TXT_HUNGRY );

        } else if ( remaining > STARVING && newLevel <= STARVING ) {

            ((Hero)target).interrupt( TXT_AWAKE_STARVING );
            GLog.n( TXT_STARVING );

        } else if ( remaining > STARVING_HALF && newLevel <= STARVING_HALF ) {

            ((Hero)target).interrupt();
            GLog.n( TXT_STARVING_HALF );

        } else if ( remaining > STARVING_FULL && newLevel <= STARVING_FULL ) {

            ((Hero)target).interrupt();
            GLog.n( TXT_STARVING_FULL );

        }

        remaining = newLevel;

    }

	public boolean isStarving() {
		return remaining <= STARVING;
	}

	@Override
	public int icon() {
        if ( remaining <= STARVING ) {
            return BuffIndicator.STARVATION;
        } else if ( remaining <= PARTIAL ) {
            return BuffIndicator.HUNGER;
        } else if ( remaining > DEFAULT ) {
			return BuffIndicator.OVERFED;
		} else {
			return BuffIndicator.NONE;
		}
	}

    @Override
    public String toString() {
        if ( remaining <= STARVING_FULL ) {
            return "极度饥饿(致死)";
        } else if ( remaining <= STARVING_HALF ) {
            return "极度饥饿(严重)";
        } else if ( remaining <= STARVING ) {
            return "极度饥饿";
        } else if ( remaining <= PARTIAL ) {
            return "饥饿";
        } else if ( remaining > DEFAULT ) {
            return "饱食";
        } else {
            return "";
        }
    }

    @Override
    public String description() {
        if ( remaining <= STARVING_FULL ) {
            return "你急切地想要任何能塞进嘴里地东西！你的生命值开始以极快的速度流失，你要再不吃点什么的话真的会死。就算是药草你也吃得下肚。";
        } else if ( remaining <= STARVING_HALF ) {
            return "你已经饿的无可忍受了！生命值开始缓慢下跌，如果你再不立刻找点吃的，饥饿状态将达到致死级别。" +
                    "food soon, starvation will become unbearable.";
        } else if ( remaining <= STARVING ) {
            return "你现在非常饿！生命自然回复彻底停止，而且你要不赶紧再找些吃的，饥饿带来的痛苦还会继续加重。";
        } else if ( remaining <= PARTIAL ) {
            return "你的肚子饿的咕咕叫，导致生命自然回复速率减半。你最好找点吃的填填肚子。";
        } else if ( remaining > DEFAULT ) {
            return "你吃的很饱！这样的状态不仅使你心情良好，还提高了你50%的生命自然回复速率。";
        } else {
            return "";
        }
    }

    public float energy() {
        return remaining > 0 ? remaining : 0;
    }

}
