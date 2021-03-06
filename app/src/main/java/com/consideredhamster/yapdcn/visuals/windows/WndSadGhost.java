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
package com.consideredhamster.yapdcn.visuals.windows;

import com.consideredhamster.yapdcn.visuals.ui.RenderedTextMultiline;
import com.consideredhamster.yapdcn.Dungeon;
import com.consideredhamster.yapdcn.actors.hero.Hero;
import com.consideredhamster.yapdcn.actors.mobs.npcs.Ghost;
import com.consideredhamster.yapdcn.items.Item;
import com.consideredhamster.yapdcn.items.quest.DriedRose;
import com.consideredhamster.yapdcn.scenes.PixelScene;
import com.consideredhamster.yapdcn.visuals.sprites.ItemSprite;
import com.consideredhamster.yapdcn.visuals.ui.RedButton;
import com.consideredhamster.yapdcn.visuals.ui.Window;
import com.consideredhamster.yapdcn.misc.utils.GLog;
import com.consideredhamster.yapdcn.misc.utils.Utils;

public class WndSadGhost extends Window {
	
	private static final String TXT_ROSE	=
		"太好了！就是它！请立刻把它给我！" +
		"拿走其中一件装备吧，愿它能成为你征途中的助力...";
	private static final String TXT_RAT	= 
		"太好了！那只可恶的生物终于伏诛，我也可以安息了..." +
		"请拿走其中一件装备吧，愿它能成为你征途中的助力...";
	private static final String TXT_WEAPON	= "Ghost's weapon";
	private static final String TXT_ARMOR	= "Ghost's armor";
	
	private static final int WIDTH		= 120;
	private static final int BTN_HEIGHT	= 20;
	private static final float GAP		= 2;
	
	public WndSadGhost( final Ghost ghost, final Item item ) {
		
		super();
		
		IconTitle titlebar = new IconTitle();
		titlebar.icon( new ItemSprite( item.image(), null ) );
		titlebar.label( Utils.capitalize( item.name() ) );
		titlebar.setRect( 0, 0, WIDTH, 0 );
		add( titlebar );
		
		RenderedTextMultiline message = PixelScene.renderMultiline( item instanceof DriedRose ? TXT_ROSE : TXT_RAT, 6 );
		message.maxWidth(WIDTH);
		PixelScene.align(message);
		float y = titlebar.bottom() + GAP;
		message.setPos(0,y);
		add( message );
		
		RedButton btnWeapon = new RedButton( Ghost.Quest.weapon.toString() ) {
			@Override
			protected void onClick() {
				selectReward( ghost, item, Ghost.Quest.weapon );
			}
		};
		btnWeapon.setRect( 0, y + message.height() + GAP, WIDTH, BTN_HEIGHT );
		add( btnWeapon );
		
		RedButton btnArmor = new RedButton( Ghost.Quest.armor.toString() ) {
			@Override
			protected void onClick() {
				selectReward( ghost, item, Ghost.Quest.armor );
			}
		};
		btnArmor.setRect( 0, btnWeapon.bottom() + GAP, WIDTH, BTN_HEIGHT );
		add( btnArmor );
		
		resize( WIDTH, (int)btnArmor.bottom() );
	}
	
	private void selectReward( Ghost ghost, Item item, Item reward ) {
		
		hide();
		
		item.detach( Dungeon.hero.belongings.backpack );
		
		if (reward.doPickUp( Dungeon.hero )) {
			GLog.i( Hero.TXT_YOU_NOW_HAVE, reward.name() );
		} else {
			Dungeon.level.drop( reward, ghost.pos ).sprite.drop();
		}
		
		ghost.yell( "再见，冒险者！" );
		ghost.die( null );
		
		Ghost.Quest.complete();
	}
}
