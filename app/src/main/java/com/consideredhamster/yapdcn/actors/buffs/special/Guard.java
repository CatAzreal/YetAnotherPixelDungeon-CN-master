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
package com.consideredhamster.yapdcn.actors.buffs.special;

import com.consideredhamster.yapdcn.actors.Char;
import com.consideredhamster.yapdcn.actors.buffs.Buff;
import com.consideredhamster.yapdcn.actors.buffs.BuffReactive;
import com.consideredhamster.yapdcn.visuals.Assets;
import com.consideredhamster.yapdcn.Dungeon;
import com.consideredhamster.yapdcn.visuals.sprites.CharSprite;
import com.consideredhamster.yapdcn.visuals.ui.BuffIndicator;
import com.watabou.noosa.Camera;
import com.watabou.noosa.audio.Sample;

public class Guard extends BuffReactive {

//    private static String TXT_PARRIED = "parried";
    private static String TXT_BLOCKED = "格挡";

//    private static String TXT_PARRY_BROKEN = "parry failed!";
    private static String TXT_BLOCK_BROKEN = "格挡失败！";

    @Override
    public int icon() {
        return BuffIndicator.GUARD;
    }

    @Override
    public String toString() {
        return "格挡";
    }

    @Override
    public String statusMessage() { return "格挡"; }

    @Override
    public boolean attachTo( Char target ) {

        Buff.detach( target, Combo.class);
        Buff.detach( target, Focus.class );

        return super.attachTo( target );
    }

    @Override
    public String description() {
        return "你正保持着防御姿态，随时准备挡下敌人的攻势。每次成功格挡都有概率使敌人被弹反，给予你对敌人实施强力反击的机会。";
    }

    public void reset( boolean withShield ) {

        target.sprite.showStatus(CharSprite.WARNING, TXT_BLOCK_BROKEN );
//        target.sprite.showStatus(CharSprite.DEFAULT, withShield ? TXT_BLOCK_BROKEN : TXT_PARRY_BROKEN);

    }

    public void proc( boolean withShield ) {

        if( target.sprite.visible ) {
            Sample.INSTANCE.play( Assets.SND_HIT, 1, 1, 0.5f );
            target.sprite.showStatus(CharSprite.DEFAULT, TXT_BLOCKED );
//            target.sprite.showStatus(CharSprite.DEFAULT, withShield ? TXT_BLOCKED : TXT_PARRIED);

            if (target == Dungeon.hero) {
                Camera.main.shake(2, 0.1f);
            }
        }
    }

}
