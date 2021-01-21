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
import com.consideredhamster.yapdcn.actors.mobs.npcs.AmbitiousImp;
import com.consideredhamster.yapdcn.items.Item;
import com.consideredhamster.yapdcn.items.quest.DwarfToken;
import com.consideredhamster.yapdcn.scenes.PixelScene;
import com.consideredhamster.yapdcn.visuals.sprites.ItemSprite;
import com.consideredhamster.yapdcn.visuals.ui.RedButton;
import com.consideredhamster.yapdcn.visuals.ui.Window;
import com.consideredhamster.yapdcn.misc.utils.GLog;
import com.consideredhamster.yapdcn.misc.utils.Utils;

public class WndImp extends Window {
	
	private static final String TXT_MESSAGE	= 
		"哦太棒了！你简直就是我的英雄！\n关于你的奖励，我现在没带钱，但我这有个更好的东西。这个戒指是我们的传家宝物，是我爷爷从一个死掉的%s手上摘下来的。";
	private static final String TXT_REWARD		= "接受戒指";
	
	private static final int WIDTH		= 120;
	private static final int BTN_HEIGHT	= 20;
	private static final int GAP		= 2;
	
	public WndImp( final AmbitiousImp imp, final DwarfToken tokens ) {
		
		super();
		
		IconTitle titlebar = new IconTitle();
		titlebar.icon( new ItemSprite( tokens.image(), null ) );
		titlebar.label( Utils.capitalize( tokens.name() ) );
		titlebar.setRect( 0, 0, WIDTH, 0 );
		add( titlebar );

        String story = TXT_MESSAGE;

        switch( Dungeon.hero.heroClass ) {
            case WARRIOR:
                story = Utils.format( story, "paladin" );
                break;
            case SCHOLAR:
                story = Utils.format( story, "sorcerer" );
                break;
            case BRIGAND:
                story = Utils.format( story, "assassin" );
                break;
            case ACOLYTE:
                story = Utils.format( story, "ranger" );
                break;
        }


		RenderedTextMultiline message = PixelScene.renderMultiline( story, 6 );
		message.maxWidth(WIDTH);
		PixelScene.align(message);
		float y = titlebar.bottom() + GAP;
		message.setPos(0,y);
		add( message );
		
		RedButton btnReward = new RedButton( TXT_REWARD ) {
			@Override
			protected void onClick() {
				takeReward( imp, tokens, AmbitiousImp.Quest.reward );
			}
		};
		btnReward.setRect( 0, y + message.height() + GAP, WIDTH, BTN_HEIGHT );
		add( btnReward );
		
		resize( WIDTH, (int)btnReward.bottom() );
	}
	
	private void takeReward( AmbitiousImp imp, DwarfToken tokens, Item reward ) {
		
		hide();
		
		tokens.detachAll( Dungeon.hero.belongings.backpack );

		reward.identify();
		if (reward.doPickUp( Dungeon.hero )) {
			GLog.i( Hero.TXT_YOU_NOW_HAVE, reward.name() );
		} else {
			Dungeon.level.drop( reward, imp.pos ).sprite.drop();
		}
		
		imp.flee();
		
		AmbitiousImp.Quest.complete();
	}
}
