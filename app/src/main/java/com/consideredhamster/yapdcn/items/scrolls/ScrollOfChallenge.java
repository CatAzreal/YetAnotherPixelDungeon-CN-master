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

import com.consideredhamster.yapdcn.actors.Actor;
import com.consideredhamster.yapdcn.actors.buffs.BuffActive;
import com.consideredhamster.yapdcn.actors.buffs.bonuses.Enraged;
import com.watabou.noosa.audio.Sample;
import com.consideredhamster.yapdcn.visuals.Assets;
import com.consideredhamster.yapdcn.Dungeon;
import com.consideredhamster.yapdcn.actors.mobs.Bestiary;
import com.consideredhamster.yapdcn.actors.mobs.Mimic;
import com.consideredhamster.yapdcn.actors.mobs.Mob;
import com.consideredhamster.yapdcn.visuals.effects.Speck;
import com.consideredhamster.yapdcn.visuals.effects.SpellSprite;
import com.consideredhamster.yapdcn.items.Heap;
import com.consideredhamster.yapdcn.scenes.GameScene;
import com.consideredhamster.yapdcn.misc.utils.GLog;

public class ScrollOfChallenge extends Scroll {

    private static final String TXT_MESSAGE	= "这张卷轴之中释放出了响彻地牢的战斗怒吼！";

	{
		name = "挑战卷轴";
        shortName = "Ch";

        spellSprite = SpellSprite.SCROLL_CHALLENGE;
        spellColour = SpellSprite.COLOUR_DARK;
	}
	
	@Override
	protected void doRead() {

        for (Heap heap : Dungeon.level.heaps.values()) {
            if (heap.type == Heap.Type.CHEST_MIMIC) {
                Mimic m = Mimic.spawnAt( heap.hp, heap.pos, heap.items );
                if (m != null) {
//                    m.beckon( curUser.pos );
                    heap.destroy();
                }
            }
        }

        if (!Dungeon.bossLevel() && Dungeon.level.nMobs() > 0) {

            int amount = Dungeon.level.nMobs() + Dungeon.chapter() - Dungeon.level.mobs.size();

            for( int i = 0 ; i < amount ; i++ ) {

                Mob mob = Bestiary.mob(Dungeon.depth);
                mob.state = mob.HUNTING;
                mob.pos = Dungeon.level.randomRespawnCell();
                if (mob.pos != -1) {
                    GameScene.add( mob );
                    Actor.occupyCell( mob );
                }

            }
        }

        for (Mob mob : Dungeon.level.mobs) {
            if( mob.hostile && mob.state != mob.PASSIVE ) {
                mob.beckon(curUser.pos);
            }
        }
		
		GLog.w(TXT_MESSAGE);

        BuffActive.add( curUser, Enraged.class, 20.0f );

		curUser.sprite.centerEmitter().start(Speck.factory(Speck.SCREAM), 0.3f, 3);
		Sample.INSTANCE.play(Assets.SND_CHALLENGE);

        super.doRead();
	}
	
	@Override
	public String desc() {
		return
                "大声阅读其上的符文，这张卷轴将赐予你一股充斥着黑暗的怒火，短时间内大幅提高你的伤害。不过如此做也会激怒所有本层生物，并使你的所在位置暴露无遗。";
//			"When read aloud, this scroll will bless you with an unholy wrath, significantly " +
//            "increasing strength of your blows for a limited time. However, it will also aggravate " +
//            "all creatures on the level and reveal your position to them.";
	}

    @Override
    public int price() {
        return isTypeKnown() ? 90 * quantity : super.price();
    }
}
