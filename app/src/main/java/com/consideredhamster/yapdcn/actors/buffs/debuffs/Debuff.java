package com.consideredhamster.yapdcn.actors.buffs.debuffs;

import com.consideredhamster.yapdcn.actors.Char;
import com.consideredhamster.yapdcn.actors.buffs.Buff;
import com.consideredhamster.yapdcn.actors.buffs.BuffActive;
import com.consideredhamster.yapdcn.misc.utils.GLog;
import com.consideredhamster.yapdcn.visuals.sprites.CharSprite;

import java.util.HashSet;

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
public abstract class Debuff extends BuffActive {

    @Override
    public String messagePrefix() { return GLog.WARNING; }

    @Override
    public int statusColor() { return CharSprite.WARNING; }

    public static void removeAll( Char target ) {
        remove( target, Debuff.class );
    };

    public static<T extends BuffActive> void remove( Char target, Class<T> debuffClass ) {
        for( Buff buff : (HashSet<Buff>)target.buffs().clone() ){
            if( debuffClass.isInstance( buff ) ){
                buff.detach();
            }
        }
    };

}
