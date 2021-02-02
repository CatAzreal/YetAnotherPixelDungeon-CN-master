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
import com.watabou.noosa.Image;
import com.watabou.noosa.ui.Component;
import com.consideredhamster.yapdcn.items.Item;
import com.consideredhamster.yapdcn.scenes.PixelScene;
import com.consideredhamster.yapdcn.visuals.sprites.ItemSprite;
import com.consideredhamster.yapdcn.visuals.ui.HealthBar;
import com.consideredhamster.yapdcn.visuals.ui.Window;
import com.consideredhamster.yapdcn.misc.utils.Utils;

public class IconTitle extends Component {

	private static final float FONT_SIZE = 8;

	private static final float GAP = 2;

	protected Image imIcon;
	protected RenderedTextMultiline tfLabel;
	protected HealthBar health;

	private float healthLvl = Float.NaN;

	public IconTitle() {
		super();
	}

	public IconTitle( Item item ) {
		this(
			new ItemSprite( item.image(), item.glowing() ),
			Utils.capitalize( item.toString() ) );
	}

	public IconTitle( Image icon, String label ) {
		super();

		icon( icon );
		label( label );
	}

	@Override
	protected void createChildren() {
		imIcon = new Image();
		add( imIcon );

		tfLabel = PixelScene.renderMultiline((int) FONT_SIZE);
		tfLabel.hardlight( Window.TITLE_COLOR );
		add( tfLabel );

		health = new HealthBar();
		add( health );
	}

	@Override
	protected void layout() {

		health.visible = !Float.isNaN( healthLvl );

		imIcon.x = x;
		imIcon.y = y;

		float x = PixelScene.align( PixelScene.uiCamera, imIcon.x + imIcon.width() + GAP );
		float y = PixelScene.align( PixelScene.uiCamera,
                imIcon.height > tfLabel.height() ?
                        imIcon.y + (imIcon.height() - tfLabel.bottom()) / 2 :
                        imIcon.y + 2);
		tfLabel.setPos(x,y);
		tfLabel.maxWidth((int) (width - x));
        PixelScene.align(tfLabel);

		if (health.visible) {
			health.setRect( x, Math.max( y + tfLabel.height(), imIcon.y + imIcon.height() - health.height() ), tfLabel.maxWidth(), 0 );
			height = health.bottom();
		} else {
			height = Math.max( imIcon.y + imIcon.height(), y + tfLabel.height() );
		}
	}

	public void icon( Image icon ) {
		remove( imIcon );
		add( imIcon = icon );
	}

	public void label( String label ) {
		tfLabel.text( label );
	}

	public void label( String label, int color ) {
		tfLabel.text( label );
		tfLabel.hardlight( color );
	}

	public void color( int color ) {
		tfLabel.hardlight( color );
	}

	public void health( float value ) {
		health.level( healthLvl = value );
		layout();
	}
}
