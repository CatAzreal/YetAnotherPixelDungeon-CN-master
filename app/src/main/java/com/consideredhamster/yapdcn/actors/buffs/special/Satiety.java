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
            return "Starving (unbearable)";
        } else if ( remaining <= STARVING_HALF ) {
            return "Starving (harsh)";
        } else if ( remaining <= STARVING ) {
            return "Starving (mild)";
        } else if ( remaining <= PARTIAL ) {
            return "Hungry";
        } else if ( remaining > DEFAULT ) {
            return "Satiated";
        } else {
            return "";
        }
    }

    @Override
    public String description() {
        if ( remaining <= STARVING_FULL ) {
            return "You desperately crave for food! Your health is drained even faster, and it will " +
                    "eventually kill you if you don't eat something. Even herbs will do.";
        } else if ( remaining <= STARVING_HALF ) {
            return "Starvation got worse! You slowly lose your health and if you will not find any " +
                    "food soon, starvation will become unbearable.";
        } else if ( remaining <= STARVING ) {
            return "You are starving! Your health regeneration is stopped completely and soon it " +
                    "will start to become painful if you will not find something to eat.";
        } else if ( remaining <= PARTIAL ) {
            return "You are hungry, which decreases your health regeneration rate by half. Better " +
                    "stop to have some quick snack if you have any.";
        } else if ( remaining > DEFAULT ) {
            return "Your stomach is full! This makes you feel good, and increases your health " +
                    "regeneration rate by half.";
        } else {
            return "";
        }
    }

    public float energy() {
        return remaining > 0 ? remaining : 0;
    }

}
