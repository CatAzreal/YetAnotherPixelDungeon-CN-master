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
import com.consideredhamster.yapdcn.actors.buffs.BuffActive;
import com.consideredhamster.yapdcn.actors.buffs.bonuses.Enraged;
import com.consideredhamster.yapdcn.misc.utils.GLog;
import com.watabou.utils.Callback;
import com.consideredhamster.yapdcn.Element;
import com.consideredhamster.yapdcn.actors.Char;
import com.consideredhamster.yapdcn.items.misc.Gold;
import com.consideredhamster.yapdcn.items.weapons.throwing.Tomahawks;
import com.consideredhamster.yapdcn.levels.Level;
import com.consideredhamster.yapdcn.misc.mechanics.Ballistica;
import com.consideredhamster.yapdcn.visuals.sprites.BruteSprite;
import com.consideredhamster.yapdcn.visuals.sprites.MissileSprite;
import com.watabou.utils.Random;

public class GnollBrute extends MobPrecise {

	private static final String TXT_ENRAGED = "%s被激怒了！";

    public GnollBrute() {

        super( 10 );

        /*

            base maxHP  = 27
            armor class = 9 + 9

            damage roll = 4-13 (2-6)

            accuracy    = 22
            dexterity   = 18

            perception  = 105%
            stealth     = 105%

         */

		name = "豺狼暴徒";
		info = "暴怒, 飞斧投掷";

		spriteClass = BruteSprite.class;
		
		loot = Gold.class;
		lootChance = 0.25f;

        resistances.put( Element.Body.class, Element.Resist.PARTIAL );
        resistances.put( Element.Mind.class, Element.Resist.PARTIAL );
        resistances.put( Element.Dispel.class, Element.Resist.IMMUNE );
	}

    @Override
    public int shieldAC() {

        return armorClass;

    }

    @Override
    public float awareness(){
        return buff( Enraged.class ) != null ? super.awareness() * 0.5f : super.awareness() ;
    }

    @Override
    public boolean blocksRanged() {
        return true;
    }

    @Override
    public int damageRoll() {
        return isRanged() ? super.damageRoll() / 2 : super.damageRoll();
    }

    @Override
    protected boolean canAttack( Char enemy ) {
        return super.canAttack( enemy ) || HP >= HT && Level.distance(pos, enemy.pos) <= 2
                && Ballistica.cast(pos, enemy.pos, false, true) == enemy.pos;
    }

    @Override
    protected void onRangedAttack( int cell ) {
        ((MissileSprite)sprite.parent.recycle( MissileSprite.class )).
                reset(pos, cell, new Tomahawks(), new Callback() {
                    @Override
                    public void call() {
                        onAttackComplete();
                    }
                });

        super.onRangedAttack( cell );
    }
	
	@Override
	public void damage( int dmg, Object src, Element type ) {

        super.damage( dmg, src, type );

		if ( isAlive() && buff( Enraged.class ) == null && HP < HT / 2 ) {

            BuffActive.add(this, Enraged.class, Random.Float( 5.0f, 10.0f ) );
            spend( TICK );

            if (Dungeon.visible[pos]) {
                GLog.w( TXT_ENRAGED, name );
            }
        }
	}

	@Override
	public String description() {
		return
			"豺狼暴徒是豺狼人中体型最庞大，力量最强壮且生命力最顽强的精英。尽管智力不高，但却是非常凶猛的斗士，当豺狼暴徒承受了足够的伤害时，它会狂暴一段时间。";
	}
}
