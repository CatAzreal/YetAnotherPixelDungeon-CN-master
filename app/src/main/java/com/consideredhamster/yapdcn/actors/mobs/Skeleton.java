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

import com.consideredhamster.yapdcn.actors.buffs.BuffActive;
import com.consideredhamster.yapdcn.actors.mobs.npcs.Ghost;
import com.watabou.noosa.audio.Sample;
import com.consideredhamster.yapdcn.visuals.Assets;
import com.consideredhamster.yapdcn.Element;
import com.consideredhamster.yapdcn.Dungeon;
import com.consideredhamster.yapdcn.actors.Char;
import com.consideredhamster.yapdcn.actors.buffs.debuffs.Withered;
import com.consideredhamster.yapdcn.items.misc.Gold;
import com.consideredhamster.yapdcn.visuals.sprites.SkeletonSprite;
import com.watabou.utils.Random;

public class Skeleton extends MobPrecise {

    public Skeleton() {

        super( 6 );

        /*

            base maxHP  = 19
            armor class = 6

            damage roll = 3-9

            accuracy    = 15
            dexterity   = 12

            perception  = 110%
            stealth     = 110%

         */

        name = "骷髅";
        info = "法术造物, 虚弱攻击";
        spriteClass = SkeletonSprite.class;

        loot = Gold.class;
        lootChance = 0.25f;

        resistances.put( Element.Frost.class, Element.Resist.PARTIAL );
        resistances.put( Element.Unholy.class, Element.Resist.PARTIAL );
        resistances.put( Element.Doom.class, Element.Resist.PARTIAL );

        resistances.put( Element.Body.class, Element.Resist.IMMUNE );
        resistances.put( Element.Mind.class, Element.Resist.IMMUNE );

	}

    @Override
    public boolean isMagical() {
        return true;
    }

    @Override
    public int attackProc( Char enemy, int damage, boolean blocked ) {

        if( !blocked && Random.Int( 10 ) < tier ) {
            BuffActive.addFromDamage( enemy, Withered.class, damage * 2 );
        }

        return damage;
    }

    @Override
    public void die( Object cause, Element dmg ) {
        Ghost.Quest.process( pos );
        super.die( cause, dmg );
    }

    @Override
    public String description() {
        return
                "骷髅是由不幸的冒险家和地牢居民的尸骨组成的，它被深处散发的邪恶魔法所唤起，与这种邪恶存在接触会削弱受害者的生命力，因此，骷髅在外臭名远扬。";
    }

	@Override
	public void die( Object cause ) {

		super.die( cause );

		if (Dungeon.visible[pos]) {
			Sample.INSTANCE.play( Assets.SND_BONES );
		}
	}

}
