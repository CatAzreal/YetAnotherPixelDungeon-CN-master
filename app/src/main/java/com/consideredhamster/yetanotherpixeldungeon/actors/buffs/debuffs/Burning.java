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
package com.consideredhamster.yetanotherpixeldungeon.actors.buffs.debuffs;

import com.consideredhamster.yetanotherpixeldungeon.Element;
import com.consideredhamster.yetanotherpixeldungeon.Dungeon;
import com.consideredhamster.yetanotherpixeldungeon.actors.Char;
import com.consideredhamster.yetanotherpixeldungeon.actors.blobs.Blob;
import com.consideredhamster.yetanotherpixeldungeon.actors.blobs.Fire;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.Buff;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.bonuses.Invisibility;
import com.consideredhamster.yetanotherpixeldungeon.actors.hero.Hero;
import com.consideredhamster.yetanotherpixeldungeon.items.Heap;
import com.consideredhamster.yetanotherpixeldungeon.items.Item;
import com.consideredhamster.yetanotherpixeldungeon.items.food.MeatBurned;
import com.consideredhamster.yetanotherpixeldungeon.items.food.MeatRaw;
import com.consideredhamster.yetanotherpixeldungeon.items.food.MeatStewed;
import com.consideredhamster.yetanotherpixeldungeon.items.herbs.Herb;
import com.consideredhamster.yetanotherpixeldungeon.items.scrolls.Scroll;
import com.consideredhamster.yetanotherpixeldungeon.levels.Level;
import com.consideredhamster.yetanotherpixeldungeon.scenes.GameScene;
import com.consideredhamster.yetanotherpixeldungeon.visuals.Assets;
import com.consideredhamster.yetanotherpixeldungeon.visuals.sprites.CharSprite;
import com.consideredhamster.yetanotherpixeldungeon.visuals.ui.BuffIndicator;
import com.consideredhamster.yetanotherpixeldungeon.misc.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;

public class Burning extends Debuff {

	private static final String TXT_BURNS_UP		= "%s被烧毁了！";

    @Override
    public Element buffType() {
        return Element.FLAME;
    }

    @Override
    public String toString() {
        return "燃烧";
    }

    @Override
    public String statusMessage() { return "燃烧"; }

    @Override
    public String playerMessage() { return "你身上烧起来了！快，去找水！"; }

    @Override
    public int icon() {
        return BuffIndicator.BURNING;
    }

    @Override
    public void applyVisual() {

        if (target.sprite.visible) {
            Sample.INSTANCE.play( Assets.SND_BURNING );
        }

        target.sprite.add( CharSprite.State.BURNING );
    }

    @Override
    public void removeVisual() {
        target.sprite.remove( CharSprite.State.BURNING );
    }

    @Override
    public String description() {
        return "烧起来了！燃烧状态下，你会受到持续伤害，且有可能导致可燃物品被烧毁。同时该状态下敌人会更容易注意到你的存在。";
    }
	
	@Override
	public boolean act() {

        target.damage(
                Random.Int( (int)Math.sqrt(
                        target.totalHealthValue() * 1.5f
                ) ) + 1, this, Element.FLAME_PERIODIC
        );

        Blob blob = Dungeon.level.blobs.get( Burning.class );
//            Blob blob2 = Dungeon.level.blobs.get( Miasma.class );

        if (Level.flammable[target.pos] && ( blob == null || blob.cur[ target.pos ] <= 0 )) {
//            if (Level.flammable[target.pos] || blob1 != null && blob1.cur[target.pos] > 0 || blob2 != null && blob2.cur[target.pos] > 0) {
            GameScene.add(Blob.seed(target.pos, 1, Fire.class));
        }

        if ( target instanceof Hero ) {

            Item item = ((Hero) target).belongings.randomUnequipped();

            if ( item instanceof Scroll || item instanceof Herb ) {

                item = item.detach(((Hero) target).belongings.backpack);
                GLog.w(TXT_BURNS_UP, item.toString());

                Heap.burnFX(target.pos);

            } else if ( item instanceof MeatRaw || item instanceof MeatStewed ) {

                item = item.detach(((Hero) target).belongings.backpack);
                MeatBurned steak = new MeatBurned();

                if (!steak.collect(((Hero) target).belongings.backpack)) {
                    Dungeon.level.drop(steak, target.pos).sprite.drop();
                }

                GLog.w(TXT_BURNS_UP, item.toString());

                Heap.burnFX(target.pos);

            }
        }

        if ( !target.isAlive() || Level.water[ target.pos ] && !target.flying ) {
            detach();
            return true;
        }

		return super.act();
	}

    @Override
    public boolean attachTo( Char target ) {

        if (super.attachTo( target )) {

            Buff.detach( target, Invisibility.class);
            Buff.detach( target, Ensnared.class );
            Buff.detach( target, Frozen.class );

            return true;

        } else {

            return false;

        }
    }

}
