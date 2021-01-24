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

import com.consideredhamster.yapdcn.Element;
import com.consideredhamster.yapdcn.Dungeon;
import com.consideredhamster.yapdcn.actors.hero.HeroClass;
import com.consideredhamster.yapdcn.items.food.MeatRaw;
import com.consideredhamster.yapdcn.visuals.sprites.CrabSprite;

public class SewerCrab extends MobHealthy {

    public SewerCrab() {

        super( 4 );

        /*

            base maxHP  = 14
            armor class = 4

            damage roll = 4-7

            accuracy    = 5
            dexterity   = 4

            perception  = 95%
            stealth     = 95%

         */

        name = "水道巨蟹";
        spriteClass = CrabSprite.class;
		
		loot = new MeatRaw();
		lootChance = 0.25f;

        resistances.put( Element.Dispel.class, Element.Resist.IMMUNE );
        resistances.put( Element.Knockback.class, Element.Resist.PARTIAL );

	}

    @Override
    public float moveSpeed() {
        return state == HUNTING || state == FLEEING ? super.moveSpeed() * 2.0f: super.moveSpeed() ;
    }
	
//	@Override
//	public void die( Object cause, Element dmg ) {
//		Ghost.Quest.process( pos );
//		super.die( cause, dmg );
//	}
	
	@Override
	public String description() {
//		return
//			"These huge crabs are at the top of the food chain in the sewers. " +
//			"They are extremely fast and their thick exoskeleton can withstand " +
//			"heavy blows.";

        return "这些巨大的螃蟹位于下水道食物链的顶端。"

                + ( Dungeon.hero.heroClass == HeroClass.WARRIOR ?
                "尽管它们的甲壳很厚，但只要愿意，它们可以快速的移动。" : "" )

                + ( Dungeon.hero.heroClass == HeroClass.SCHOLAR ?
                "有前人假设，如果不加以照顾，它们可能会在一座城市下形成庞大的群体" : "" )

                + ( Dungeon.hero.heroClass == HeroClass.BRIGAND ?
                "如果你不想缺胳膊断腿的话，最好悠着点。" : "" )

                + ( Dungeon.hero.heroClass == HeroClass.ACOLYTE ?
                "它们的巨螯能击穿金属盔甲，甲壳似乎坚不可摧，机灵的眼珠正将你当做下一个猎物。" : "" )

                ;
	}
}
