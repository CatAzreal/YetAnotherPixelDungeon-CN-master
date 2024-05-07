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

import com.consideredhamster.yapdcn.Dungeon;
import com.consideredhamster.yapdcn.actors.blobs.Thunderstorm;
import com.consideredhamster.yapdcn.actors.buffs.BuffActive;
import com.consideredhamster.yapdcn.actors.buffs.debuffs.Frozen;
import com.consideredhamster.yapdcn.visuals.effects.Lightning;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.consideredhamster.yapdcn.visuals.Assets;
import com.consideredhamster.yapdcn.Element;
import com.consideredhamster.yapdcn.actors.Char;
import com.consideredhamster.yapdcn.visuals.effects.MagicMissile;
import com.consideredhamster.yapdcn.visuals.effects.particles.EnergyParticle;
import com.consideredhamster.yapdcn.misc.mechanics.Ballistica;
import com.consideredhamster.yapdcn.visuals.sprites.MagusSprite;
import com.watabou.utils.Random;

import java.util.HashSet;

public class Magus extends MobRanged {

    private int spellPrepared = 0;

    private static final int NONE = 0;
    private static final int FLAME = 1;
    private static final int FROST = 2;
    private static final int SHOCK = 3;
    private static final int ACID = 4;
    private static final int UNHOLY = 5;
    private static final int ENERGY = 6;
    
    
    private static final String PREPARED = "prepared";

    public Magus() {

        super( 19 );

        /*

            base maxHP  = 37
            armor class = 10

            damage roll = 9-17

            accuracy    = 43
            dexterity   = 25

            perception  = 150%
            stealth     = 100%

         */

		name = "魔族法师";
		info = "法术造物, 元素射击";
		spriteClass = MagusSprite.class;

        resistances.put( Element.Flame.class, Element.Resist.PARTIAL );
        resistances.put( Element.Frost.class, Element.Resist.PARTIAL );
        resistances.put( Element.Acid.class, Element.Resist.PARTIAL );
        resistances.put( Element.Shock.class, Element.Resist.PARTIAL );
        resistances.put( Element.Energy.class, Element.Resist.PARTIAL );
        resistances.put( Element.Unholy.class, Element.Resist.PARTIAL );
//        resistances.put( Element.Mind.class, Element.Resist.PARTIAL );
//        resistances.put( Element.Body.class, Element.Resist.PARTIAL );
        resistances.put( Element.Doom.class, Element.Resist.PARTIAL );
//        resistances.put( Element.Dispel.class, Element.Resist.PARTIAL );

	}

    @Override
    public boolean isMagical() {
        return true;
    }

    @Override
    public boolean act() {

        if( !enemySeen )
            spellPrepared = 0;

        return super.act();

    }

    @Override
    protected boolean canAttack( Char enemy ) {
        return super.canAttack( enemy ) || Ballistica.cast( pos, enemy.pos, false, true ) == enemy.pos;
    }
    
    @Override
    public boolean doAttack( Char enemy ) {
        
        if( spellPrepared == NONE ) {
            
//            spellPrepared = SHOCK;
            spellPrepared = Random.oneOf( FLAME, FROST, SHOCK, ACID, UNHOLY, ENERGY );
    
            if( Dungeon.visible[ pos ] ) {
                switch ( spellPrepared ) {
                    case FLAME:
                        sprite.centerEmitter().burst( EnergyParticle.FACTORY_ORANGE, 10 );
                        break;
                    case FROST:
                        sprite.centerEmitter().burst( EnergyParticle.FACTORY_WHITE, 10 );
                        break;
                    case SHOCK:
                        sprite.centerEmitter().burst( EnergyParticle.FACTORY_BLUE, 10 );
                        break;
                    case ACID:
                        sprite.centerEmitter().burst( EnergyParticle.FACTORY_BLIGHT, 10 );
                        break;
                    case UNHOLY:
                        sprite.centerEmitter().burst( EnergyParticle.FACTORY_BLACK, 10 );
                        break;
                    case ENERGY:
                        sprite.centerEmitter().burst( EnergyParticle.FACTORY_GREEN, 10 );
                        break;
                }
            }
            
            spend(attackDelay());
            
            return true;
            
        } else {
            
            return super.doAttack( enemy );
            
        }
    }
    
    @Override
    public void onAttackComplete() {
        throwSpell( enemy.pos );
        spend(attackDelay());
        next();
    }
    
    @Override
    public void onRangedAttack( int cell ) {
        throwSpell( enemy.pos );
        super.onRangedAttack( cell );
    }

    @Override
    public boolean attack( Char enemy ) {

        if ( spellPrepared == SHOCK || hit( this, enemy, true, true )) {
    
            int power = damageRoll();
            
            switch ( spellPrepared ) {
                
                case FLAME:
                    
                    enemy.damage( power, this, Element.FLAME );
                    break;
                    
                case FROST:
                    
                    enemy.damage( power, this, Element.FROST );
    
                    if( enemy.isAlive() ){
                        BuffActive.add( enemy, Frozen.class, power );
                    }
                    
                    break;
                    
                case SHOCK:
                    
                    HashSet<Char> affected = Thunderstorm.spreadFrom( enemy.pos );
    
                    if( affected != null && !affected.isEmpty() ) {
                        for( Char ch : affected ) {
                            ch.damage( ch == enemy ? power : power / 2, this, Element.SHOCK );
                        }
                    }
                    break;
                    
                case ACID:
                    
                    enemy.damage( power, this, Element.ACID );
                    break;
                    
                case UNHOLY:
                    
                    enemy.damage( power, this, Element.UNHOLY );
                    break;
                    
                case ENERGY:
                    
                    enemy.damage( power, this, Element.ENERGY );
                    break;
            }
            

        } else {

            enemy.missed();

        }
        
        spellPrepared = NONE;

        return true;
    }
    
    protected void throwSpell( int cell ) {
        
        switch ( spellPrepared ) {
            case FLAME:
                MagicMissile.fire(sprite.parent, pos, cell,
                        new Callback() {
                            @Override
                            public void call() {
                                onCastComplete();
                            }
                        });
                break;
            case FROST:
                MagicMissile.frost(sprite.parent, pos, cell,
                        new Callback() {
                            @Override
                            public void call() {
                                onCastComplete();
                            }
                        });
                break;
            case SHOCK:
                sprite.parent.add( new Lightning( pos, cell ) );
                onCastComplete();
                break;
            case ACID:
                MagicMissile.acid(sprite.parent, pos, cell,
                        new Callback() {
                            @Override
                            public void call() {
                                onCastComplete();
                            }
                        });
                break;
            case UNHOLY:
                MagicMissile.shadow(sprite.parent, pos, cell,
                        new Callback() {
                            @Override
                            public void call() {
                                onCastComplete();
                            }
                        });
                break;
            case ENERGY:
                MagicMissile.hellfire(sprite.parent, pos, cell,
                        new Callback() {
                            @Override
                            public void call() {
                                onCastComplete();
                            }
                        });
                break;
            case NONE:
                onCastComplete();
                break;
        }
    
        Sample.INSTANCE.play(Assets.SND_ZAP);
        
    }

    @Override
    public String description() {
        return
                "它的双目充斥着恶意和鄙夷，魔族法师理论上讲归属于恶魔社会中的最高阶层。由于身形脆弱，它们通常更倾向于将自己掌握的各类元素法术由远处释放，将剩下接敌交战抛尸之类的脏活交给自己的低阶同族们做。";
    }

    @Override
    public void storeInBundle( Bundle bundle ) {
        super.storeInBundle(bundle);
        bundle.put( PREPARED, spellPrepared );
    }

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle(bundle);
        spellPrepared = bundle.getInt( PREPARED );
    }
}
