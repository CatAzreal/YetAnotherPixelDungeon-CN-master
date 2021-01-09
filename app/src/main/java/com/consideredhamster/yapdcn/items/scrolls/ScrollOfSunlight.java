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
package com.consideredhamster.yapdcn.items.scrolls;

import com.watabou.noosa.audio.Sample;
import com.consideredhamster.yapdcn.visuals.Assets;
import com.consideredhamster.yapdcn.actors.blobs.Blob;
import com.consideredhamster.yapdcn.actors.blobs.Sunlight;
import com.consideredhamster.yapdcn.visuals.effects.Speck;
import com.consideredhamster.yapdcn.visuals.effects.SpellSprite;
import com.consideredhamster.yapdcn.scenes.GameScene;
import com.consideredhamster.yapdcn.misc.utils.GLog;

public class ScrollOfSunlight extends Scroll {

    private static final String TXT_MESSAGE	= "The whole area is suddenly illuminated with rays of warm light.";

	{
		name = "圣光卷轴";
        shortName = "Su";

        spellSprite = SpellSprite.SCROLL_SUNLIGHT;
        spellColour = SpellSprite.COLOUR_HOLY;
	}
	
	@Override
	protected void doRead() {

        curUser.sprite.centerEmitter().start( Speck.factory( Speck.NOTE ), 0.3f, 5 );
        Sample.INSTANCE.play( Assets.SND_LULLABY );

        GameScene.add( Blob.seed( curUser.pos, 250 * ( 110 + curUser.magicPower() ) / 100, Sunlight.class ) );

        GLog.i( TXT_MESSAGE );

        super.doRead();
	}
	
	@Override
	public String desc() {
		return
				"照亮区域，正常生物回复生命值，秘法生物受到阻滞";
//			"Reading this scroll will light the area with a bright sunlight. Here deep in the " +
//            "dungeon, sunlight can mean much more than simple illumination. Everything that lives " +
//            "which is touched by this light, will be rejuvenated in mere moments. Everything " +
//            "unnatural, on the contrary, will become disoriented and more susceptible to attacks." +
//            "\n\nDuration of this effect of depends on magic skill of the reader.";
	}
	
	@Override
	public int price() {
		return isTypeKnown() ? 70 * quantity : super.price();
	}
}
