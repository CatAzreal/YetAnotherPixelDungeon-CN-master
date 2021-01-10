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

import com.consideredhamster.yapdcn.actors.blobs.Blob;
import com.consideredhamster.yapdcn.scenes.GameScene;
import com.consideredhamster.yapdcn.actors.blobs.FrigidVapours;

public class PotionOfFrigidVapours extends Potion {

    public static final int BASE_VAL	= 200;
	
	{
		name = "冰雾药剂";
        shortName = "Fr";
        harmful = true;
	}
	
	@Override
	public void shatter( int cell ) {

        GameScene.add( Blob.seed( cell, BASE_VAL, FrigidVapours.class ) );
        super.shatter( cell );

	}
	
	@Override
	public String desc() {
        return "[临时字串]使用方式：投掷；效果：冰冻生物";
//			"Upon exposure to open air, this chemical will evaporate into a freezing cloud, causing " +
//			"any creature that touches it to be frozen in place, unable to act or move.";
	}

    @Override
    public int price() {
        return isTypeKnown() ? 65 * quantity : super.price();
    }

    @Override
    public float brewingChance() {
        return 0.70f;
    }
}