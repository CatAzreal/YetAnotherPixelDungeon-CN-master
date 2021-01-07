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
import com.consideredhamster.yetanotherpixeldungeon.visuals.ui.BuffIndicator;

public class MindVision extends Bonus {

	public static final float DURATION = 15f;
	
	@Override
	public String toString() {
		return "灵视";
	}

    @Override
    public String statusMessage() { return "灵视"; }

    @Override
    public String playerMessage() {
        return Dungeon.level.mobs.size() > 0 ?
            "你获取到了其他生物的感知！" :
            "你认为自己能确定本层目前没有其他生命的活动迹象";
    }

    @Override
    public int icon() {
        return BuffIndicator.MIND_VISION;
    }

//    @Override
//    public void applyVisual(){}
//
//    @Override
//    public void removeVisual() {}

    @Override
    public String description() {
        return "哇哦...你的知觉扩张并投射到了本层的其他生物上。你现在的感知能力获得了增强，并且能清楚了解到其他生物的所在位置。";
    }

	@Override
	public void detach() {
		super.detach();
		Dungeon.observe();
	}
}
