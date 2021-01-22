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
package com.consideredhamster.yapdcn.items.wands;

import com.consideredhamster.yapdcn.Element;
import com.consideredhamster.yapdcn.actors.buffs.BuffActive;
import com.consideredhamster.yapdcn.actors.buffs.debuffs.Disrupted;
import com.consideredhamster.yapdcn.visuals.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;
import com.consideredhamster.yapdcn.visuals.Assets;
import com.consideredhamster.yapdcn.actors.Actor;
import com.consideredhamster.yapdcn.actors.Char;
import com.consideredhamster.yapdcn.actors.buffs.debuffs.Charmed;
import com.consideredhamster.yapdcn.visuals.effects.MagicMissile;
import com.consideredhamster.yapdcn.visuals.effects.Speck;
import com.consideredhamster.yapdcn.misc.utils.GLog;
import com.watabou.utils.Callback;

public class WandOfCharm extends WandUtility {

	{
		name = "魅惑法杖";
        image = ItemSpriteSheet.WAND_DOMINATION;
	}

    @Override
    public float effectiveness( int bonus ) {
        return super.effectiveness( bonus ) * 0.95f;
    }

	@Override
	protected void onZap( int cell ) {
		Char ch = Actor.findChar( cell );

		if (ch != null) {

            int damage = damageRoll();

            if( ch.isMagical() ){

                BuffActive.add( ch, Disrupted.class, damage );
                ch.damage( damage / 2, curUser, Element.ENERGY );

            } else {

                BuffActive.add( ch, Charmed.class, damage );

            }

            ch.sprite.centerEmitter().start(Speck.factory(Speck.HEART), 0.2f, 5);

		} else {
			
			GLog.i( "什么都没有发生" );
			
		}
	}
	
	protected void fx( int cell, Callback callback ) {
		MagicMissile.purpleLight( curUser.sprite.parent, curUser.pos, cell, callback );
		Sample.INSTANCE.play( Assets.SND_ZAP );
	}
	
	@Override
	public String desc() {
		return
				"这根法杖上散发的紫色光芒能够催眠目标，迫使其狂暴地攻击敌人以保护施法者。法杖无法魅惑秘法生物，" +
				"但会伤害并阻碍其感知，使其错乱并被暴露在攻击之下。";
//			"The purple light from this wand will hypnotize the target, forcing it to violently " +
//            "protect you against other enemies for a while. It cannot charm magical enemies, but " +
//            "will instead harm and disrupt their senses, causing them to be confused and vulnerable.";
	}
}
