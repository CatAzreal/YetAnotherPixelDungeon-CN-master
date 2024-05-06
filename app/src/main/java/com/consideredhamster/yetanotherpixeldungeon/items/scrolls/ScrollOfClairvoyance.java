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
package com.consideredhamster.yetanotherpixeldungeon.items.scrolls;

import com.consideredhamster.yetanotherpixeldungeon.Dungeon;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.CellEmitter;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.Speck;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.SpellSprite;
import com.consideredhamster.yetanotherpixeldungeon.levels.Level;
import com.consideredhamster.yetanotherpixeldungeon.misc.utils.GLog;

public class ScrollOfClairvoyance extends Scroll {

	private static final String TXT_LAYOUT = "你获知了本层的地形布局！";
	
	{
		name = "探地卷轴";
        shortName = "Cl";

        spellSprite = SpellSprite.SCROLL_FARSIGHT;
        spellColour = SpellSprite.COLOUR_RUNE;
	}
	
	@Override
	protected void doRead() {
		
		int length = Level.LENGTH;
//		int[] map = Dungeon.level.map;
		boolean[] mapped = Dungeon.level.mapped;
		boolean[] discoverable = Level.discoverable;

//		boolean noticed = false;
		
		for (int i=0; i < length; i++) {
			
//			int terr = map[i];
			
			if (discoverable[i]) {
				
				mapped[i] = true;
//				if ((Terrain.flags[terr] & Terrain.TRAPPED) != 0) {
//
//					Level.set( i, Terrain.discover( terr ) );
//					GameScene.updateMap( i );
//
//					if (Dungeon.visible[i]) {
//						GameScene.discoverTile( i, terr );
//						discover( i );
//
//						noticed = true;
//					}
//				}
			}
		}
		Dungeon.observe();
		
		GLog.i( TXT_LAYOUT );
//		if (noticed) {
//			Sample.INSTANCE.play( Assets.SND_SECRET );
//		}

//        curUser.sprite.emitter().start( Speck.factory( Speck.LIGHT ), 0.2f, Random.IntRange(3, 5) );
//        new Flare( 6, 32 ).color(0x3399FF, true).show(curUser.sprite, 2f);

        super.doRead();
	}
	
	@Override
	public String desc() {
		return
			"阅读这张卷轴时，一副明晰的景象会刻入你的记忆中，告知你整个楼层的精确布局并揭开所有隐藏的秘密。不过道具位置和生物分布依旧是未知状态。";
	}
	
	@Override
	public int price() {
		return isTypeKnown() ? 80 * quantity : super.price();
	}
	
	public static void discover( int cell ) {
		CellEmitter.get( cell ).start( Speck.factory( Speck.DISCOVER ), 0.1f, 4 );
	}
}
