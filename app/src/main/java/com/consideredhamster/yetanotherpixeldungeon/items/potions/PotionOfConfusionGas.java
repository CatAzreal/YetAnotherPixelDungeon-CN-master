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
package com.consideredhamster.yetanotherpixeldungeon.items.potions;

import com.consideredhamster.yetanotherpixeldungeon.actors.blobs.Blob;
import com.consideredhamster.yetanotherpixeldungeon.actors.blobs.ConfusionGas;
import com.consideredhamster.yetanotherpixeldungeon.scenes.GameScene;

public class PotionOfConfusionGas extends Potion {

    public static final int BASE_VAL	= 600;

	{
		name = "Potion of Confusion Gas";
        shortName = "CG";
        harmful = true;
	}
	
	@Override
	public void shatter( int cell ) {

        GameScene.add( Blob.seed( cell, BASE_VAL, ConfusionGas.class ) );
        super.shatter( cell );

	}
	
	@Override
	public String desc() {
		return	"Upon exposure to open air, the liquid in this flask will vaporize " +
            "into a confusing chromatic haze. Anyone who inhales the cloud will be disoriented " +
            "instantly.";
	}
	
	@Override
	public int price() {
		return isTypeKnown() ? 55 * quantity : super.price();
	}

    @Override
    public float brewingChance() {
        return 0.80f;
    }
}
