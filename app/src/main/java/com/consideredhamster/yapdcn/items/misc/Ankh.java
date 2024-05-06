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
package com.consideredhamster.yapdcn.items.misc;

import com.consideredhamster.yapdcn.actors.buffs.debuffs.Debuff;
import com.consideredhamster.yapdcn.actors.hero.Hero;
import com.consideredhamster.yapdcn.visuals.effects.Flare;
import com.consideredhamster.yapdcn.visuals.effects.particles.ShadowParticle;
import com.consideredhamster.yapdcn.items.Item;
import com.consideredhamster.yapdcn.items.armours.Armour;
import com.consideredhamster.yapdcn.items.bags.Bag;
import com.consideredhamster.yapdcn.items.weapons.Weapon;
import com.consideredhamster.yapdcn.scenes.GameScene;
import com.consideredhamster.yapdcn.visuals.sprites.CharSprite;
import com.consideredhamster.yapdcn.visuals.sprites.ItemSpriteSheet;
import com.consideredhamster.yapdcn.misc.utils.GLog;

import java.util.ArrayList;

public class Ankh extends Item {

	{
        visible = false;
		stackable = true;
		name = "重生十字架";
		image = ItemSpriteSheet.ANKH;
	}

    private static final String TXT_RESURRECT	= "十字架的力量使你重获新生！";

    @Override
    public ArrayList<String> actions( Hero hero ) {
        ArrayList<String> actions = super.actions( hero );

        actions.remove( AC_THROW );
        actions.remove( AC_DROP );

        return actions;
    }
	
	@Override
	public String info() {
		return 
			"这枚象征不朽的古老饰品能够使人起死回生。 ";
	}


	public static void resurrect( Hero hero ) {
        new Flare( 8, 32 ).color(0xFFFF66, true).show(hero.sprite, 2f) ;
        GameScene.flash(0xFFFFAA);

        hero.HP = hero.HT;

        Debuff.removeAll(hero);

        uncurse( hero, hero.belongings.backpack.items.toArray( new Item[0] ) );

        uncurse( hero,
                hero.belongings.weap1,
                hero.belongings.weap2,
                hero.belongings.armor,
                hero.belongings.ring1,
                hero.belongings.ring2
        );

        hero.sprite.showStatus(CharSprite.POSITIVE, "复活！");
        GLog.w(TXT_RESURRECT);
	}

    public static boolean uncurse( Hero hero, Item... items ) {

        boolean procced = false;

        for(Item item : items) {

            if (item != null) {

                if( item instanceof Bag ) {

                    uncurse( hero, ((Bag)item).items.toArray( new Item[0] ) );

                } else {

                    item.identify(CURSED_KNOWN);

                    if (item.bonus < 0) {

                        item.bonus++;
//                        item.bonus = Random.IntRange(item.bonus + 1, 0);

                        if (item.bonus == 0) {
                            if (item instanceof Weapon && ((Weapon) item).enchantment != null) {
                                ((Weapon) item).enchant(null);
                            } else if (item instanceof Armour && ((Armour) item).glyph != null) {
                                ((Armour) item).inscribe(null);
                            }
                        }

                        procced = true;

                    }
                }
            }
        }

        if (procced) {
            hero.sprite.emitter().start( ShadowParticle.UP, 0.05f, 10 );
        }

        return procced;
    }

	@Override
    public String status() {
        return Integer.toString( quantity );
    }

	@Override
	public int price() {
		return 100 * quantity;
	}
}
