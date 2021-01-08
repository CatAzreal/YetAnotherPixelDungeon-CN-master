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
package com.consideredhamster.yetanotherpixeldungeon.items.bags;

import com.consideredhamster.yetanotherpixeldungeon.actors.hero.Hero;
import com.consideredhamster.yetanotherpixeldungeon.items.Item;
import com.consideredhamster.yetanotherpixeldungeon.items.scrolls.Scroll;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.ItemSpriteSheet;
import com.consideredhamster.yetanotherpixeldungeon.visuals.ui.Icons;

public class ScrollHolder extends Bag {

	{
		name = "卷轴筒";
		image = ItemSpriteSheet.HOLDER;
		
		size = 19;
        visible = false;
        unique = true;
	}
	
	@Override
	public boolean grab( Item item ) {
		return item instanceof Scroll;
	}

    @Override
    public Icons icon() {
        return Icons.SCROLL_HOLDER;
    }
	
	@Override
	public int price() {
		return 50;
	}
	
	@Override
	public String info() {
		return
			"你可以向这个管状容器内放入任意数量的卷轴。这个容器不仅能节省你的背包空间还能保护卷轴不被点燃。";
	}

    @Override
    public boolean doPickUp( Hero hero ) {

        return hero.belongings.getItem( ScrollHolder.class ) == null && super.doPickUp( hero ) ;

    }
}
