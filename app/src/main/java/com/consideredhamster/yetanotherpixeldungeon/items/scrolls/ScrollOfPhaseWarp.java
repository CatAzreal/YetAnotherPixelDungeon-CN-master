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

import com.consideredhamster.yetanotherpixeldungeon.actors.Char;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.BuffActive;
import com.consideredhamster.yetanotherpixeldungeon.visuals.Assets;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.Speck;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.tweeners.AlphaTweener;
import com.watabou.utils.Random;
import com.consideredhamster.yetanotherpixeldungeon.Dungeon;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.debuffs.Vertigo;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.SpellSprite;
import com.consideredhamster.yetanotherpixeldungeon.misc.utils.GLog;

public class ScrollOfPhaseWarp extends Scroll {

	public static final String TXT_TELEPORTED_VISITED =
		"一瞬之间你被传送到了其他地方，你总觉得自己来过这里。";

    public static final String TXT_TELEPORTED_UNKNOWN =
        "一瞬之间你被传送到了其他地方，你对这片区域毫无印象。";
	
	public static final String TXT_NO_TELEPORT = 
		"传送失败！";
	
	{
		name = "变相卷轴";
        shortName = "Ph";

        spellSprite = SpellSprite.SCROLL_TELEPORT;
        spellColour = SpellSprite.COLOUR_WILD;
	}
	
	@Override
	protected void doRead() {

        int pos;

        pos = Dungeon.level.randomRespawnCell( false, true );

        if (pos == -1) {

            GLog.w( TXT_NO_TELEPORT );

        } else {

//            float chance = 0.5f / curUser.attunement();
//
//            if( chance > Random.Float() ) {
//
//                GLog.i(Dungeon.level.visited[pos] ? TXT_TELEPORTED_VISITED : TXT_TELEPORTED_UNKNOWN);
//                Arrays.fill(Dungeon.level.visited, false);
//
//            }

            ScrollOfPhaseWarp.appear(curUser, pos);
            Dungeon.level.press(pos, curUser);

            BuffActive.add(curUser, Vertigo.class, Random.Float(5f, 10f) );
            Dungeon.observe();

        }

        super.doRead();
	}

    public static void appear( Char ch, int pos ) {

        ch.sprite.interruptMotion();

        ch.move( pos );
        ch.sprite.place( pos );

        if (ch.invisible == 0) {
            ch.sprite.alpha( 0 );
            ch.sprite.parent.add( new AlphaTweener( ch.sprite, 1, 0.4f ) );
        }

        ch.sprite.emitter().start( Speck.factory( Speck.LIGHT ), 0.2f, 3 );
        Sample.INSTANCE.play( Assets.SND_TELEPORT );
    }

    @Override
	public String desc() {
		return
			"羊皮纸上的咒语能立刻让阅读者传送到本层的另一处。危急时刻可用作逃脱手段，不过这种传送方式会对使用者的精神造成影响。";
	}

    @Override
    public int price() {
        return isTypeKnown() ? 75 * quantity : super.price();
    }
}
