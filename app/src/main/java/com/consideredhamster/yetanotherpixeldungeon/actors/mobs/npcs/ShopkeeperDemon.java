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
package com.consideredhamster.yetanotherpixeldungeon.actors.mobs.npcs;

import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.AmbitiousImpSprite;
import com.consideredhamster.yetanotherpixeldungeon.misc.utils.Utils;

public class ShopkeeperDemon extends Shopkeeper {

    private static final String TXT_GREETINGS = "你好啊朋友！";
	
	{
		name = "雄心勃勃的小恶魔";
		spriteClass = AmbitiousImpSprite.class;
	}

    @Override
    protected void greetings() {
        yell( Utils.format(TXT_GREETINGS) );
    }
	
	@Override
	public String description() {
		return 
			"小恶魔是一种低等恶魔。没有强大的力量也没有魔法天赋，这些生物仅以其残忍品性和贪婪无餍而恶名昭彰。" +
			"不过其中少数个体相当聪明且善于交际。比如这边这只，看起来就挺像回事的。";
	}
}
