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
package com.consideredhamster.yetanotherpixeldungeon.actors.buffs.bonuses;

import com.consideredhamster.yetanotherpixeldungeon.Dungeon;
import com.consideredhamster.yetanotherpixeldungeon.actors.Char;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.Buff;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.special.Light;
import com.consideredhamster.yetanotherpixeldungeon.actors.hero.Hero;
import com.consideredhamster.yetanotherpixeldungeon.actors.mobs.Bestiary;
import com.consideredhamster.yetanotherpixeldungeon.actors.mobs.Mob;
import com.consideredhamster.yetanotherpixeldungeon.items.misc.OilLantern;
import com.consideredhamster.yetanotherpixeldungeon.visuals.Assets;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.CharSprite;
import com.consideredhamster.yetanotherpixeldungeon.visuals.ui.BuffIndicator;
import com.consideredhamster.yetanotherpixeldungeon.misc.utils.GLog;
import com.consideredhamster.yetanotherpixeldungeon.visuals.ui.TagAttack;
import com.watabou.noosa.audio.Sample;

public class Invisibility extends Bonus {

    private static final String TXT_DISPEL		=
            "隐形被解除！";

    @Override
    public String toString() {
        return "隐形";
    }

    @Override
    public String statusMessage() { return "隐形"; }

    @Override
    public String playerMessage() { return "你看到自己的手正在失去形体！"; }

    @Override
    public int icon() {
        return BuffIndicator.INVISIBLE;
    }

    @Override
    public void applyVisual(){

        if( target.sprite.visible ){
            Sample.INSTANCE.play( Assets.SND_MELD );
        }

        target.sprite.add( CharSprite.State.INVISIBLE );
    }

    @Override
    public void removeVisual() {
        if( target.invisible <= 0 ){
            target.sprite.remove( CharSprite.State.INVISIBLE );
        }
    }

    @Override
    public String description() {
        return "你和周围的地形几乎融为一体，使你很难被敌人发现(但发现你的敌人仍会试图找到你)，并降低在商店偷窃的难度。 \n" +
                "\n" +
                "攻击，偷窃或在敌人尝试移入你的所在位置时会立即解除隐形效果。 ";
    }

    @Override
    public boolean attachOnLoad( Char target ) {
        if (super.attachOnLoad( target )) {

            target.invisible++;

            return true;
        } else {
            return false;
        }
    }

	@Override
	public boolean attachTo( Char target ) {
		if (super.attachTo( target )) {

			target.invisible++;

            if( target == Dungeon.hero ){

                OilLantern lantern = Dungeon.hero.belongings.getItem( OilLantern.class );

                if( target.isAlive() && lantern != null && lantern.isActivated() ){
                    lantern.deactivate( Dungeon.hero, true );
                }

            } else {

                Dungeon.hero.checkVisibleMobs();
                TagAttack.updateState();

            }

			return true;

		} else {
			return false;
		}
	}
	
	@Override
	public void detach() {

		if( target.invisible > 0 ) {
            target.invisible--;

            if( target != Dungeon.hero ) {
                Dungeon.hero.checkVisibleMobs();
                TagAttack.updateState();
            }
        }

		super.detach();
	}


    public static void dispel() {

        Invisibility.dispel( Dungeon.hero );

    }

	public static void dispel( Char ch ) {

        Invisibility buff = ch.buff( Invisibility.class );
        if ( buff != null ) {

            if (ch == Dungeon.hero) {
                GLog.w(TXT_DISPEL);
            }

            buff.detach();
		}
	}
}
