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
package com.consideredhamster.yapdcn.items.misc;

import java.io.IOException;
import java.util.ArrayList;

import com.consideredhamster.yapdcn.items.Item;
import com.watabou.noosa.Game;
import com.consideredhamster.yapdcn.Badges;
import com.consideredhamster.yapdcn.Dungeon;
import com.consideredhamster.yapdcn.Statistics;
import com.consideredhamster.yapdcn.actors.hero.Hero;
import com.consideredhamster.yapdcn.scenes.AmuletScene;
import com.consideredhamster.yapdcn.visuals.sprites.ItemSpriteSheet;

public class Amulet extends Item {
	
	private static final String AC_END = "祈愿";
	
	{
		name = "Yendor护符";
		image = ItemSpriteSheet.AMULET;
		
		unique = true;
	}
	
	@Override
	public ArrayList<String> actions( Hero hero ) {
		ArrayList<String> actions = super.actions( hero );
		actions.add( AC_END );
		return actions;
	}
	
	@Override
	public void execute( Hero hero, String action ) {
		if (action == AC_END) {
			
			showAmuletScene( false );
			
		} else {
			
			super.execute( hero, action );
			
		}
	}
	
	@Override
	public boolean doPickUp( Hero hero ) {
		if (super.doPickUp( hero )) {
			
			if (!Statistics.amuletObtained) {
				Statistics.amuletObtained = true;
				Badges.validateVictory();

				showAmuletScene( true );
			}
			
			return true;
		} else {
			return false;
		}
	}
	
	private void showAmuletScene( boolean showText ) {
		try {
			Dungeon.saveAll();
			AmuletScene.noText = !showText;
			Game.switchScene( AmuletScene.class );
		} catch (IOException e) {
		}
	}
	
	@Override
	public String info() {
		return 
			"Yendor护符是世间公认的最强神器，曾经属于巫师Yendor。据传护符能够实现持有者的任何一个愿望，但仅能生效一次。";
	}
}
