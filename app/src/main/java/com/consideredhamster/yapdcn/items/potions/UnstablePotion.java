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
package com.consideredhamster.yapdcn.items.potions;

import com.consideredhamster.yapdcn.actors.hero.Hero;
import com.consideredhamster.yapdcn.items.Generator;
import com.consideredhamster.yapdcn.visuals.sprites.ItemSpriteSheet;


public class UnstablePotion extends Potion {

	{
		name = "混合药剂";
        shortName = "??";

        image = ItemSpriteSheet.POTION_UNSTABLE;
        color = "layered";

//        harmful = true;
    }

    @Override
    public boolean isTypeKnown() {
        return true;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public boolean isIdentified() {
        return true;
    }

	private Potion getRandomPotion(){
        Potion potion= (Potion)Generator.random( Generator.Category.POTION );
        potion.color = color;
        potion.dud = true;
        return potion;
    }

    @Override
    protected void apply( Hero hero ) {
        getRandomPotion().apply(hero);
    }

    @Override
    public void shatter( int cell ) {
        getRandomPotion().shatter(cell);
    }

    @Override
	public String desc() {
	    return "瓶中盛有分层的奇怪药液，看上去很不稳定。没人确保饮用或是投掷会产生什么效果。";
 //       return "[临时字串]使用方式：？；效果：？";
//            "This flask contains odd layered liquid and is obviously unstable. " +
//            "Its effects will be completely unpredictable whether drunk or thrown.";
	}

    @Override
    public String quickAction() {
        return AC_THROW;
    }

	@Override
	public int price() {
		return isTypeKnown() ? 25 * quantity : super.price();
	}

    @Override
    public float brewingChance() {
        return 1.00f;
    }
}
