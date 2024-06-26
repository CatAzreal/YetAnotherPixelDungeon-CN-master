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
package com.consideredhamster.yapdcn.items.armours.glyphs;

import com.consideredhamster.yapdcn.actors.blobs.Thunderstorm;
import com.watabou.utils.Random;
import com.consideredhamster.yapdcn.Element;
import com.consideredhamster.yapdcn.actors.Char;
import com.consideredhamster.yapdcn.visuals.effects.Lightning;
import com.consideredhamster.yapdcn.items.armours.Armour;
import com.consideredhamster.yapdcn.levels.Level;
import com.consideredhamster.yapdcn.visuals.sprites.ItemSprite;

import java.util.ArrayList;
import java.util.HashSet;

public class StormWard extends Armour.Glyph {

    private ArrayList<Char> affected = new ArrayList<Char>();

    private int[] points = new int[20];
    private int nPoints;

    @Override
    public ItemSprite.Glowing glowing() {
        return WHITE;
    }

    @Override
    public Class<? extends Element> resistance() {
        return Element.Shock.class;
    }

    @Override
    protected String name_p() {
        return "雷纹之%s";
    }

    @Override
    protected String name_n() {
        return "电势之%s";
    }

    @Override
    protected String desc_p() {
        return "电击命中你的敌人，并获得电击属性的抗性";
    }

    @Override
    protected String desc_n() {
        return "被敌人命中时电击自身";
    }

    @Override
    protected boolean proc_p( Char attacker, Char defender, int damage ) {

        if (Level.adjacent(attacker.pos, defender.pos)) {

            HashSet<Char> affected = Thunderstorm.spreadFrom( attacker.pos );

            if( affected != null && !affected.isEmpty() ) {
                for( Char ch : affected ) {
                    int power = Random.IntRange( damage / 4, damage / 3 ) ;
                    ch.damage( ch == defender ? power : power / 2, this, Element.SHOCK );
                }
            }

            attacker.sprite.parent.add( new Lightning( attacker.pos, attacker.pos ) );

            return true;
        }

        return false;
    }

    @Override
    protected boolean proc_n( Char attacker, Char defender, int damage ) {

        HashSet<Char> affected = Thunderstorm.spreadFrom( defender.pos );

        if( affected != null && !affected.isEmpty() ) {
            for( Char ch : affected ) {
                int power = Random.IntRange( damage / 4, damage / 3 ) ;
                ch.damage( ch == defender ? power : power / 2, this, Element.SHOCK );
            }
        }

        defender.sprite.parent.add( new Lightning( defender.pos, defender.pos ) );

        return true;
    }
}
