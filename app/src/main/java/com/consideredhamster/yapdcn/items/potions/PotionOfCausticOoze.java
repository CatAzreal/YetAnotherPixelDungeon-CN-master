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
package com.consideredhamster.yapdcn.items.potions;

import com.consideredhamster.yapdcn.actors.Actor;
import com.consideredhamster.yapdcn.actors.Char;
import com.consideredhamster.yapdcn.actors.buffs.BuffActive;
import com.consideredhamster.yapdcn.actors.buffs.debuffs.Corrosion;
import com.consideredhamster.yapdcn.actors.hazards.CausticOoze;
import com.consideredhamster.yapdcn.levels.Level;
import com.consideredhamster.yapdcn.visuals.effects.CellEmitter;
import com.consideredhamster.yapdcn.visuals.effects.Speck;
import com.watabou.utils.Random;

public class PotionOfCausticOoze extends Potion {

    public static final int BASE_VAL = 10;

	{
		name = "淤泥药剂";
        shortName = "CO";
        harmful = true;
	}
	
	@Override
	public void shatter( int cell ) {

        int duration = Random.IntRange( BASE_VAL, BASE_VAL * 2 );
        CausticOoze.spawn( cell, duration );

        for ( int n : Level.NEIGHBOURS9 ) {

            int pos = cell + n;

            Char ch;

            if ( ( ch = Actor.findChar( pos )) != null ) {
                BuffActive.add( ch, Corrosion.class, n == 0 ?
                    Random.IntRange( BASE_VAL, BASE_VAL * 3 / 2 ) :
                    Random.IntRange( BASE_VAL / 2, BASE_VAL )
                );
            }

            CellEmitter.get( pos ).burst( Speck.factory( Speck.CAUSTIC ), 3 );
        }

        super.shatter( cell );
    }
	
	@Override
	public String desc() {
        return "[临时字串]使用方式：投掷；效果：生成腐蚀淤泥";
//			"Uncorking or shattering this pressurized glass will cause its contents to explode " +
//            "into a deadly vapours, which will cover everything around in a highly caustic ooze.";
	}
	
	@Override
	public int price() {
		return isTypeKnown() ? 85 * quantity : super.price();
	}

    @Override
    public float brewingChance() {
        return 0.50f;
    }
}
