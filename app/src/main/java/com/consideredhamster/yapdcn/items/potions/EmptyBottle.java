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
import com.consideredhamster.yapdcn.visuals.effects.Splash;
import com.consideredhamster.yapdcn.visuals.sprites.ItemSpriteSheet;

import java.util.ArrayList;

public class EmptyBottle extends Potion {

	{
		name = "空瓶";
        shortName = "";
        harmful = true;
		image = ItemSpriteSheet.POTION_EMPTY;
	}

    @Override
    public boolean isTypeKnown() {
        return true;
    }

    @Override
    public boolean isIdentified() {
        return true;
    }

    @Override
    protected void splash( int cell ) {
        Splash.at( cell, 0xFFFFFF, 10 );
    }

    @Override
    public ArrayList<String> actions( Hero hero ) {
        ArrayList<String> actions = super.actions(hero );
        actions.remove( AC_DRINK );
        return actions;
    }
	@Override
	public String info() {
	    return "每位炼金师都明白炼制任何药剂都需要手头有一个气密容器，因为绝大部分原料接触空气后会迅速变质，甚至会发生剧烈反应乃至引发危险。不过要注意，这些瓶子是一次性的。";
//	    return "[临时字串]精炼药剂必需材料";
//		return "Any alchemist knows that proper potion brewing requires having an airtight " +
//            "container at hand, as most resulting chemicals either quickly lose their potence " +
//            "when exposed to air or, even worse, react violently to it. These bottles can be " +
//            "used only once, however.";
	}

	@Override
	public int price() {
		return isTypeKnown() ? 20 * quantity : super.price();
	}

}
