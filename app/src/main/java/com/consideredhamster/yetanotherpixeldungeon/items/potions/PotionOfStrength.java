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
package com.consideredhamster.yetanotherpixeldungeon.items.potions;

import com.consideredhamster.yetanotherpixeldungeon.Badges;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.Buff;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.debuffs.Withered;
import com.consideredhamster.yetanotherpixeldungeon.actors.hero.Hero;
import com.consideredhamster.yetanotherpixeldungeon.visuals.effects.Speck;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.CharSprite;
import com.consideredhamster.yetanotherpixeldungeon.visuals.ui.QuickSlot;
import com.consideredhamster.yetanotherpixeldungeon.misc.utils.GLog;

public class PotionOfStrength extends Potion {

	{
		name = "力量药剂";
        shortName = "St";
	}
	
	@Override
	protected void apply( Hero hero ) {
		setKnown();

        hero.STR++;
        hero.strBonus++;
        hero.magicPower++;

		int hpBonus = 2 ;

        int restore = hero.HT - hero.HP;

        hero.HP = hero.HT += hpBonus;

        if( restore > 0 ) {
            hero.sprite.showStatus(CharSprite.POSITIVE, "%+dHP", restore);
        }

        hero.sprite.showStatus( CharSprite.POSITIVE, "+1力量,+1魔能,+%d生命上限", hpBonus );

        hero.sprite.emitter().burst(Speck.factory(Speck.MASTERY), 12);

        Buff.detach(hero, Withered.class);

        GLog.p("新生的力量从你的身体和精神中喷薄而出。" );

        QuickSlot.refresh();

		Badges.validateStrengthAttained();
	}
	
	@Override
	public String desc() {
	    return "这瓶强力的药剂会洗刷你的肌肉，永久强化你的力量与魔能，并恢复所有的生命值。";
//        return "[临时字串]使用方式：饮用；效果：增加力量和魔能，回复所有生命，增加生命上限";
//			"This powerful liquid will course through your muscles, permanently increasing your " +
//            "physical and magical powers, as well as fully restoring your health.";
	}
	
	@Override
	public int price() {
		return isTypeKnown() ? 150 * quantity : super.price();
	}

    @Override
    public float brewingChance() {
        return 0.30f;
    }
}
