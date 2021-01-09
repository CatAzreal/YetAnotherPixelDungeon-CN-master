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
package com.consideredhamster.yapdcn.items.scrolls;

import com.consideredhamster.yapdcn.actors.buffs.BuffActive;
import com.consideredhamster.yapdcn.actors.buffs.debuffs.Tormented;
import com.watabou.noosa.Camera;
import com.watabou.noosa.audio.Sample;
import com.consideredhamster.yapdcn.visuals.Assets;
import com.consideredhamster.yapdcn.Element;
import com.consideredhamster.yapdcn.Dungeon;
import com.consideredhamster.yapdcn.actors.mobs.Mob;
import com.consideredhamster.yapdcn.visuals.effects.Flare;
import com.consideredhamster.yapdcn.visuals.effects.SpellSprite;
import com.consideredhamster.yapdcn.levels.Level;
import com.consideredhamster.yapdcn.scenes.GameScene;
import com.consideredhamster.yapdcn.misc.utils.GLog;

public class ScrollOfTorment extends Scroll {

	{
		name = "苦痛卷轴";
        shortName = "To";

        spellSprite = SpellSprite.SCROLL_MASSHARM;
        spellColour = SpellSprite.COLOUR_DARK;
	}
	
	@Override
	protected void doRead() {

		int count = 0;

		Mob affected = null;

		for (Mob mob : Dungeon.level.mobs.toArray( new Mob[0] )) {

			if (Level.fieldOfView[mob.pos] ) {

                new Flare( 6, 32 ).color( SpellSprite.COLOUR_DARK, true ).show(mob.sprite, 2f);

                int dmg = 10 + curUser.magicPower();

                mob.damage( mob.currentHealthValue() * dmg / 100, curUser, Element.MIND );
                BuffActive.addFromDamage( mob, Tormented.class, dmg );

				affected = mob;
                count++;

            }
		}

        int dmg = Math.min( curUser.HP - 1, curUser.HP * ( 190 - curUser.magicPower() ) / 400 );

        curUser.damage( dmg, curUser, Element.MIND );

        GameScene.flash(SpellSprite.COLOUR_DARK - 0x660000);
        Sample.INSTANCE.play(Assets.SND_FALLING);
        Camera.main.shake(4, 0.3f);

		switch (count) {
            case 0:
                GLog.i( "一瞬之间你的思维被痛苦所吞噬。" );
                break;
            case 1:
                GLog.i( "一瞬之间你和" + affected.name + "的思维被痛苦所吞噬。" );
                break;
            default:
                GLog.i( "一瞬之间你和周围所有生物的思维都被痛苦所吞噬！" );
		}



        super.doRead();
	}
	
	@Override
	public String desc() {
		return
                "对自己视野内敌人造成伤害和恐惧效果，自己承受部分卷轴效果，高魔能可弱化负面效果。伤害和恐惧效果取决于使用者魔能和目标当前生命值";
//			"Upon reading this parchment, mind-tearing flash of pain will affect all the " +
//            "present creatures in the field of view, harming them and making them flee. " +
//            "The user of this scroll is only partially affected by this effect, and higher " +
//            "magic skill allows to diminish this backlash even further." +
//            "\n\nPower of these effects depend on magic skill of the reader and current " +
//            "health of the target.";
	}
	
	@Override
	public int price() {
		return isTypeKnown() ? 95 * quantity : super.price();
	}
}
