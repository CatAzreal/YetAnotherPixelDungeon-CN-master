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
package com.consideredhamster.yetanotherpixeldungeon.items.armours.glyphs;

import com.consideredhamster.yetanotherpixeldungeon.Element;
import com.consideredhamster.yetanotherpixeldungeon.actors.Char;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.debuffs.Debuff;
import com.consideredhamster.yetanotherpixeldungeon.actors.hero.Hero;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.Flare;
import com.consideredhamster.yetanotherpixeldungeon.items.armours.Armour;
import com.consideredhamster.yetanotherpixeldungeon.scenes.GameScene;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.CharSprite;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.ItemSprite.Glowing;
import com.consideredhamster.yetanotherpixeldungeon.misc.utils.GLog;

public class Revival extends Armour.Glyph {

    private static final String TXT_RESURRECT	= "你因寄宿在附魔之中的力量获得重生！";
	
	@Override
	public Glowing glowing() {
		return YELLOW;
	}

    @Override
    public Class<? extends Element> resistance() {
        return Element.Unholy.class;
    }

    @Override
    protected String name_p() {
        return "重生之%s";
    }

    @Override
    protected String name_n() {
        return "灭亡之%s";
    }

    @Override
    protected String desc_p() {
        return "一定几率使你起死回生，并提高对邪恶属性伤害的抗性";
    }

    @Override
    protected String desc_n() {
        return "一定几率中断重生十字架的复活进程";
    }

    @Override
    public boolean proc_p( Char attacker, Char defender, int damage ) {
        return false;
    }

    @Override
    public boolean proc_n( Char attacker, Char defender, int damage ) {
        return false;
    }

    public static void resurrect( Hero hero ) {
        new Flare( 8, 32 ).color(0xFFFF66, true).show(hero.sprite, 2f) ;
        GameScene.flash(0xFFFFAA);

        hero.HP = hero.HT;

        Debuff.removeAll( hero );

        hero.sprite.showStatus(CharSprite.POSITIVE, "复活！");
        GLog.w(TXT_RESURRECT);
    }
}
