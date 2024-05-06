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

import com.consideredhamster.yetanotherpixeldungeon.visuals.ui.RenderedTextMultiline;
import com.watabou.noosa.BitmapTextMultiline;
import com.watabou.noosa.Group;
import com.consideredhamster.yetanotherpixeldungeon.Badges;
import com.consideredhamster.yetanotherpixeldungeon.actors.hero.HeroClass;
import com.consideredhamster.yetanotherpixeldungeon.actors.hero.HeroSubClass;
import com.consideredhamster.yetanotherpixeldungeon.scenes.PixelScene;
import com.consideredhamster.yetanotherpixeldungeon.misc.utils.Utils;
import com.watabou.noosa.RenderedText;

public class WndClass extends WndTabbed {
	
	private static final String TXT_MASTERY = "典范之道";
	private static final String TXT_DETAILS = "信息";

	private static final int WIDTH			= 120;
	
	private static final int TAB_WIDTH	= 44;
	
	private HeroClass cl;

	private HistoryTab tabHistory;
	private DetailsTab tabDetails;
	private MasteryTab tabMastery;

	public WndClass( HeroClass cl ) {
		
		super();

        this.cl = cl;
        Tab tab = null;

        tabHistory = new HistoryTab();
        add(tabHistory);

        tab = new RankingTab(Utils.capitalize(cl.hname()), tabHistory);
        tab.setSize(TAB_WIDTH, tabHeight());
        add( tab );

        tabDetails = new DetailsTab();
        add(tabDetails);

        tab = new RankingTab( TXT_DETAILS, tabDetails);
        tab.setSize(TAB_WIDTH, tabHeight());
        add( tab );
//		Badges.isUnlocked( cl.masteryBadge() )
		if (Badges.isUnlocked( cl.masteryBadge() )) {
			tabMastery = new MasteryTab();
			add( tabMastery );

            tab = new RankingTab( TXT_MASTERY, tabMastery);
            tab.setSize(TAB_WIDTH, tabHeight() );
            add( tab );

			resize(
				(int)Math.max( tabHistory.width, (int)Math.max( tabDetails.width, tabMastery.width ) ),
                    (int) Math.max(tabHistory.height, (int) Math.max(tabDetails.height, tabMastery.height)));
        } else {
            resize(
                    (int)Math.max( tabHistory.width, tabDetails.width ),
                (int)Math.max( tabHistory.height, tabDetails.height ) );
		}
		
		select( 0 );
	}

	private class RankingTab extends LabeledTab {
		
		private Group page;
		
		public RankingTab( String label, Group page ) {
			super( label );
			this.page = page;
		}
		
		@Override
		protected void select( boolean value ) {
			super.select( value );
			if (page != null) {
				page.visible = page.active = selected;
			}
		}
	}

	private class HistoryTab extends Group {
		
		private static final int MARGIN	= 4;
		private static final int GAP	= 4;
		
		public float height;
		public float width;
		
		public HistoryTab() {
			super();

			String[] items = cl.history();
			float pos = MARGIN;
			
			for (int i=0; i < items.length; i++) {
				
				if (i > 0) {
					pos += GAP;
				}
				
				RenderedTextMultiline item = PixelScene.renderMultiline( items[i], 6 );
				float x = MARGIN;
				float y = pos;
				item.maxWidth(WIDTH - MARGIN * 2);
				PixelScene.align(item);
				item.setPos(x,y);
				add( item );
				
				pos += item.height();
				float w = item.width();
				if (w > width) {
					width = w;
				}
			}
			
			width += MARGIN;
			height = pos + MARGIN;
		}
	}

    private class DetailsTab extends Group {

        private static final int MARGIN	= 4;
        private static final int GAP	= 4;

        private static final String DOT	= "";
//        private static final String DOT	= "\u007F ";

        public float height;
        public float width;

        public DetailsTab() {
            super();

            float dotWidth = 0;

            String[] items = cl.details();
            float pos = MARGIN;

            for (int i=0; i < items.length; i++) {

                if (i > 0) {
                    pos += GAP;
                }

                RenderedText dot = PixelScene.renderText( DOT, 6 );
                dot.x = MARGIN;
                dot.y = pos;
                if (dotWidth == 0) {
                    PixelScene.align(dot);
                    dotWidth = dot.width();
                }
                add( dot );

                RenderedTextMultiline item = PixelScene.renderMultiline( items[i], 6 );
               	float x = dot.x + dotWidth;
               	float y = pos;
                item.maxWidth((int)(WIDTH - MARGIN * 2 - dotWidth));
				PixelScene.align(item);
				item.setPos(x,y);
                add( item );

                pos += item.height();
                float w = item.width();
                if (w > width) {
                    width = w;
                }
            }

            width += MARGIN;
            height = pos + MARGIN;
        }
    }
	
	private class MasteryTab extends Group {
		
		private static final int MARGIN	= 4;
		
		private RenderedTextMultiline normal;
		private BitmapTextMultiline highlighted;
		
		public float height;
		public float width;
		
		public MasteryTab() {
			super();
			
			String text = null;
			switch (cl) {
			case WARRIOR:
				text = HeroSubClass.GLADIATOR.desc() + "\n\n" + HeroSubClass.BERSERKER.desc();
				break;
			case SCHOLAR:
				text = HeroSubClass.BATTLEMAGE.desc() + "\n\n" + HeroSubClass.WARLOCK.desc();
				break;
			case BRIGAND:
				text = HeroSubClass.FREERUNNER.desc() + "\n\n" + HeroSubClass.ASSASSIN.desc();
				break;
			case ACOLYTE:
				text = HeroSubClass.SNIPER.desc() + "\n\n" + HeroSubClass.WARDEN.desc();
				break;
			}

			RenderedTextMultiline normal = PixelScene.renderMultiline( text, 6 );
			normal.maxWidth(WIDTH);
			PixelScene.align(normal);
			float x = MARGIN;
			float y = MARGIN;
			normal.setPos(x,y);
			add( normal );
			
			height = y + normal.height() + MARGIN;
			width = x + normal.width() + MARGIN;
		}
	}
}
