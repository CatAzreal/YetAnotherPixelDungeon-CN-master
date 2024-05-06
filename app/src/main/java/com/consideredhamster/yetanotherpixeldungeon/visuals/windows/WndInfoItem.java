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
package com.consideredhamster.yetanotherpixeldungeon.visuals.windows;

import com.consideredhamster.yetanotherpixeldungeon.YetAnotherPixelDungeon;
import com.consideredhamster.yetanotherpixeldungeon.visuals.ui.RenderedTextMultiline;
import com.watabou.noosa.BitmapTextMultiline;
import com.consideredhamster.yetanotherpixeldungeon.items.Heap;
import com.consideredhamster.yetanotherpixeldungeon.items.Heap.Type;
import com.consideredhamster.yetanotherpixeldungeon.items.Item;
import com.consideredhamster.yetanotherpixeldungeon.scenes.PixelScene;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.ItemSprite;
import com.consideredhamster.yetanotherpixeldungeon.visuals.ui.ItemSlot;
import com.consideredhamster.yetanotherpixeldungeon.visuals.ui.Window;
import com.consideredhamster.yetanotherpixeldungeon.misc.utils.Utils;

public class WndInfoItem extends Window {
	
	private static final String TXT_CHEST			= "宝箱";
	private static final String TXT_LOCKED_CHEST	= "上锁宝箱";
	private static final String TXT_CRYSTAL_CHEST	= "水晶宝箱";
	private static final String TXT_TOMB			= "坟墓";
	private static final String TXT_SKELETON		= "英雄遗骸";
	private static final String TXT_WONT_KNOW		= "直到开启前你都没法弄清楚里面究竟放着什么东西！";
	private static final String TXT_NEED_KEY		= TXT_WONT_KNOW + "你需要一把金钥匙来打开它。";
	private static final String TXT_INSIDE			= "你能看见箱子里的%s，但你需要一把金钥匙来打开它。";
	private static final String TXT_OWNER	= 
		"这处古旧坟墓里可能埋藏着一些有用的东西，但其下埋葬的人必然不希望你如此做。";
	private static final String TXT_REMAINS	= 
		"在你之前的先驱者们所能留下的所有东西都在里面了，也许你该看看里面有些什么。";
	
	private static final float GAP	= 2;

    private static final int WIDTH_P = 120;
    private static final int WIDTH_L = 240;

    private RenderedTextMultiline normal;
    private BitmapTextMultiline highlighted;
	
	public WndInfoItem( Heap heap ) {
		
		super();
		
		if (heap.type == Heap.Type.HEAP || heap.type == Heap.Type.FOR_SALE) {
			
			Item item = heap.peek();
			
			int color = TITLE_COLOR;
			if (item.isIdentified() && item.bonus > 0) {
				color = ItemSlot.UPGRADED;				
			} else if (item.isIdentified() && item.bonus < 0) {
				color = ItemSlot.DEGRADED;				
			}
			fillFields( item.image(), item.glowing(), color, item.toString(), item.info() );
			
		} else {
			
			String title;
			String info;
			
			if (heap.type == Type.CHEST || heap.type == Type.CHEST_MIMIC) {
				title = TXT_CHEST;
				info = TXT_WONT_KNOW;
			} else if (heap.type == Type.TOMB) {
				title = TXT_TOMB;
				info = TXT_OWNER;
			} else if (heap.type == Type.BONES || heap.type == Type.BONES_CURSED) {
				title = TXT_SKELETON;
				info = TXT_REMAINS;
			} else if (heap.type == Type.CRYSTAL_CHEST) {
				title = TXT_CRYSTAL_CHEST;
				info = Utils.format( TXT_INSIDE, Utils.indefinite( heap.peek().name() ) );
			} else {
				title = TXT_LOCKED_CHEST;
				info = TXT_NEED_KEY;
			}
			
			fillFields( heap.image(), heap.glowing(), TITLE_COLOR, title, info );
			
		}
	}
	
	public WndInfoItem( Item item ) {
		
		super();
		
		int color = TITLE_COLOR;
		if (item.isIdentified() && item.bonus > 0) {
			color = ItemSlot.UPGRADED;				
		} else if (item.isIdentified() && item.bonus < 0) {
			color = ItemSlot.DEGRADED;				
		}
		
		fillFields( item.image(), item.glowing(), color, item.toString(), item.info() );
	}
	
	private void fillFields( int image, ItemSprite.Glowing glowing, int titleColor, String title, String info ) {

        int width = YetAnotherPixelDungeon.landscape() ? WIDTH_L : WIDTH_P ;

		IconTitle titlebar = new IconTitle();
		titlebar.icon(new ItemSprite(image, glowing));
		titlebar.label(Utils.capitalize(title), titleColor);
		titlebar.setRect( 0, 0, width, 0 );
		add( titlebar );


        normal = PixelScene.renderMultiline( info, 6 );
        normal.maxWidth(width);
        PixelScene.align(normal);
        float x = titlebar.left();
        float y = titlebar.bottom() + GAP;
        normal.setPos(x,y);
        add( normal );

		
//		BitmapTextMultiline txtInfo = PixelScene.createMultiline( info, 6 );
//		txtInfo.maxWidth = WIDTH;
//		txtInfo.measure();
//		txtInfo.x = titlebar.left();
//		txtInfo.y = titlebar.bottom() + GAP;
//		add( txtInfo );
		
//		resize( WIDTH, (int)(txtInfo.y + txtInfo.height()) );
        resize( width, (int)(y + normal.height()) );
	}
}
