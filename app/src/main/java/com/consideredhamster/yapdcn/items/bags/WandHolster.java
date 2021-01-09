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
package com.consideredhamster.yapdcn.items.bags;

import com.consideredhamster.yapdcn.actors.hero.Hero;
import com.consideredhamster.yapdcn.items.Item;
import com.consideredhamster.yapdcn.items.wands.Wand;
import com.consideredhamster.yapdcn.visuals.sprites.ItemSpriteSheet;
import com.consideredhamster.yapdcn.visuals.ui.Icons;

public class WandHolster extends Bag {

	{
		name = "法杖筒袋";
		image = ItemSpriteSheet.HOLSTER;
		
		size = 19;
        visible = false;
        unique = true;
	}
	
	@Override
	public boolean grab( Item item ) {
		return item instanceof Wand;
	}

    @Override
    public Icons icon() {
        return Icons.WAND_HOLSTER;
    }
	
//	@Override
//	public boolean collect( Bag container ) {
//		if (super.collect( container )) {
//			if (owner != null) {
//				for (Item item : items) {
//					((Wand)item).charge( owner );
//				}
//			}
//			return true;
//		} else {
//			return false;
//		}
//	}
//
//	@Override
//	public void onDetach( ) {
//		for (Item item : items) {
//			((Wand)item).stopCharging();
//		}
//	}
	
	@Override
	public int price() {
		return 50;
	}
	
	@Override
	public String info() {
		return
			"这款细长的皮制筒袋由某种异域动物的毛皮制成。这个容器最多可以放入" + size + "根法杖。";
	}

    @Override
    public boolean doPickUp( Hero hero ) {

        return hero.belongings.getItem( WandHolster.class ) == null && super.doPickUp( hero ) ;

    }
}
