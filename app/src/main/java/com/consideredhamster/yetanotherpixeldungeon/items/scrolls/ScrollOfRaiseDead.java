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

import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.BuffActive;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.debuffs.Controlled;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;
import com.consideredhamster.yetanotherpixeldungeon.visuals.Assets;
import com.consideredhamster.yetanotherpixeldungeon.actors.mobs.Wraith;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.SpellSprite;

import java.util.ArrayList;

public class ScrollOfRaiseDead extends Scroll {
	
	{
		name = "死灵卷轴";
        shortName = "Ra";

        spellSprite = SpellSprite.SCROLL_RAISEDEAD;
        spellColour = SpellSprite.COLOUR_DARK;
	}

	@Override
	protected void doRead() {

        ArrayList<Wraith> summoned = Wraith.spawnAround( curUser.magicPower() / 3, curUser.pos, 1 );

        for( Wraith w : summoned ){

            float duration = Random.Int( 16, 20 ) * ( 110 + curUser.magicPower() ) / 100;

            BuffActive.add( w, Controlled.class, duration );

        }

        Sample.INSTANCE.play(Assets.SND_DEATH);

		super.doRead();
	}

	@Override
	public String desc() {
		return "卷轴下埋藏的恶毒法术允许阅读者与不可述之物交流，赋予其召唤冥界恶灵的能力。不过小心，它迟早会将獠牙对向自己的主人！\n\n控制恶灵的时长取决于阅读者的魔能。";
//                "Malicious magics hidden within this scroll allow its reader to commune with unspeakable, " +
//                "giving him or her an ability to summon a lost soul from the underworld. Be careful, " +
//                "as it will eventually turn on its master!" +
//                "\n\nDuration of controlling effect depends on magic skill of the reader.";
	}

    @Override
    public int price() {
        return isTypeKnown() ? 100 * quantity : super.price();
    }
}
