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
import com.consideredhamster.yapdcn.actors.blobs.Darkness;
import com.consideredhamster.yapdcn.visuals.effects.Speck;
import com.consideredhamster.yapdcn.visuals.effects.SpellSprite;
import com.consideredhamster.yapdcn.scenes.GameScene;
import com.consideredhamster.yapdcn.misc.utils.GLog;

public class ScrollOfDarkness extends Scroll {

    private static final String TXT_MESSAGE	= "你被一团暗无止境的阴影吞噬了！";

	{
		name = "黑暗卷轴";
        shortName = "Da";

        spellSprite = SpellSprite.SCROLL_DARKNESS;
        spellColour = SpellSprite.COLOUR_WILD;
	}
	
	@Override
	protected void doRead() {

        curUser.sprite.centerEmitter().start( Speck.factory( Speck.DARKNESS ), 0.3f, 5 );
        Sample.INSTANCE.play( Assets.SND_GHOST );

        GameScene.add( Blob.seed( curUser.pos, 1000 * ( 110 + curUser.magicPower() ) / 100, Darkness.class ) );

        GLog.i( TXT_MESSAGE );

        super.doRead();
	}
	
	@Override
	public String desc() {
		return
                "这张平淡无奇的纸张，一旦念出其上的文字就会唤出无底深渊之中的诡秘黑暗。这股黑暗深不可测，非利用法术不可看穿。\n\n效果持续时长取决于阅读者的魔能。";
//			"Unnatural darkness from the deepest abysses is summoned by the call of this otherwise " +
//            "unremarkable sheet of paper. This darkness is so thick that it nothing can see through it " +
//            "without magical means." +
//            "\n\nDuration of this effect depends on magic skill of the reader.";
	}

    @Override
    public int price() {
        return isTypeKnown() ? 65 * quantity : super.price();
    }
}
