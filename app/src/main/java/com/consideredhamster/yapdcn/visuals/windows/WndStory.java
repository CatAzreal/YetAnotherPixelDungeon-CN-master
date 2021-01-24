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
import com.watabou.input.Touchscreen.Touch;
import com.watabou.noosa.Game;
import com.watabou.noosa.TouchArea;
import com.consideredhamster.yapdcn.visuals.Chrome;
import com.consideredhamster.yapdcn.Dungeon;
import com.consideredhamster.yapdcn.scenes.PixelScene;
import com.consideredhamster.yapdcn.visuals.ui.Window;
import com.watabou.utils.SparseArray;

public class WndStory extends Window {

	private static final int WIDTH = 120;
	private static final int MARGIN = 6;
	
	private static final float bgR	= 0.77f;
	private static final float bgG	= 0.73f;
	private static final float bgB	= 0.62f;
	
	public static final int ID_SEWERS		= 0;
	public static final int ID_PRISON		= 1;
	public static final int ID_CAVES		= 2;
	public static final int ID_METROPOLIS	= 3;
	public static final int ID_HALLS		= 4;
	
	private static final SparseArray<String> CHAPTERS = new SparseArray<String>();
	
	static {
		CHAPTERS.put( ID_SEWERS, 
		"这片地牢位于都城的正下方，它的最上层其实是由城市的下水道系统组成的。" +
		"作为名义上归属于都城的一部分，这片区域没有那么危险。虽然没人会说这地方很安全，但你至少不必担心受到过多邪恶魔法的影响。" );
		
		CHAPTERS.put( ID_PRISON, 
		"多年以前一座地下监狱为了收容危险的犯罪者而建立于此。在那时这个主意看起来不算差，毕竟想从这里逃脱难如登天。" +
		"但不久之后下方充斥着黑暗的瘴气在这里弥漫开来，扭曲了罪犯和狱卒的心智。最终这所监狱被遗弃，只有少数逃犯仍被关押在此。" );
		
		CHAPTERS.put( ID_CAVES, 
		"这座与废弃监狱下方直接连结的洞窟可以说是荒无人烟。它所处的区域对于都城而言太深而对于矮人来讲矿产也不值得开发。" +
		"这里曾有一个为人类-矮人贸易路线建立的贸易站，不过因为矮人都城的废弃这个站点也早已荒废。如今只有无孔不入的豺狼人和其他地下住民在此栖息。" );
		
		CHAPTERS.put( ID_METROPOLIS, 
		"矮人都市曾经是矮人城邦中最宏伟的一个。在其鼎盛时期矮人的机械化军队成功阻挡了古神与其恶魔军团的进攻。但据说这些凯旋回归的战士们" +
		"将腐化的恶种带回了都城，而这场胜利也成为了这个地下王国终末的开端。" );
		
		CHAPTERS.put( ID_HALLS,
		"这里曾经是矮人都城的外城区。在那场与古神对阵的惨胜中，矮人无法再聚集起有效的武装力量将此处重夺。恶魔军团逐渐在此地站稳了脚跟，而如今这里被称为“恶魔厅堂”\n\n" +
		"很少有冒险者能一路走到这里..." );
	};
	
	private RenderedTextMultiline tf;
	
	private float delay;
	
	public WndStory( String text ) {
		super( 0, 0, Chrome.get( Chrome.Type.SCROLL ) );

		tf = PixelScene.renderMultiline( text, 5 );
		tf.maxWidth(WIDTH - MARGIN * 2);
		PixelScene.align(tf);
		tf.invert();
		tf.setPos(MARGIN, 0);
		add( tf );
		
		add( new TouchArea( chrome ) {
			@Override
			protected void onClick( Touch touch ) {
				hide();
			}
		} );
		
		resize( (int)(tf.width() + MARGIN * 2), (int)Math.min( tf.height(), 180 ) );
	}
	
	@Override
	public void update() {
		super.update();
		
		if (delay > 0 && (delay -= Game.elapsed) <= 0) {
			shadow.visible = chrome.visible = tf.visible = true;
		}
	}
	
	public static void showChapter( int id ) {
		
		if (Dungeon.chapters.contains( id )) {
			return;
		}
		
		String text = CHAPTERS.get( id );
		if (text != null) {
			WndStory wnd = new WndStory( text );
			if ((wnd.delay = 0.6f) > 0) {
				wnd.shadow.visible = wnd.chrome.visible = wnd.tf.visible = false;
			}
			
			Game.scene().add( wnd );
			
			Dungeon.chapters.add( id );
		}
	}
}
