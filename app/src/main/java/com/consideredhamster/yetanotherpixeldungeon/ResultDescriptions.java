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
package com.consideredhamster.yetanotherpixeldungeon;

import com.consideredhamster.yetanotherpixeldungeon.actors.blobs.Blob;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.debuffs.Crippled;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.Buff;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.debuffs.Burning;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.special.Satiety;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.debuffs.Corrosion;
import com.consideredhamster.yetanotherpixeldungeon.actors.buffs.debuffs.Poisoned;
import com.consideredhamster.yetanotherpixeldungeon.actors.mobs.Bestiary;
import com.consideredhamster.yetanotherpixeldungeon.actors.mobs.DwarfMonk;
import com.consideredhamster.yetanotherpixeldungeon.actors.mobs.DwarvenKing;
import com.consideredhamster.yetanotherpixeldungeon.actors.mobs.GnollBrute;
import com.consideredhamster.yetanotherpixeldungeon.actors.mobs.Golem;
import com.consideredhamster.yetanotherpixeldungeon.actors.mobs.Mimic;
import com.consideredhamster.yetanotherpixeldungeon.actors.mobs.Mob;
import com.consideredhamster.yetanotherpixeldungeon.actors.mobs.Piranha;
import com.consideredhamster.yetanotherpixeldungeon.actors.mobs.Rat;
import com.consideredhamster.yetanotherpixeldungeon.items.armours.Armour;
import com.consideredhamster.yetanotherpixeldungeon.items.weapons.Weapon;
import com.consideredhamster.yetanotherpixeldungeon.levels.features.Chasm;
import com.consideredhamster.yetanotherpixeldungeon.levels.traps.BoulderTrap;
import com.consideredhamster.yetanotherpixeldungeon.levels.traps.Trap;
import com.consideredhamster.yetanotherpixeldungeon.misc.utils.Utils;

public abstract class ResultDescriptions {

//    public static final String FAIL	= "%s";
    public static final String WIN	= "获得Yendor护符";

    public static String generateResult( Object killedBy, Element killedWith ) {

        return Utils.capitalize( killedBy == Dungeon.hero ? "" + killedWith( killedBy, killedWith ) + "了自己" :
                "被" + killedBy( killedBy ) + killedWith( killedBy, killedWith ) + "");
    }

    public static String generateMessage( Object killedBy, Element killedWith ) {

        return ( killedBy == Dungeon.hero ? "你" + killedWith( killedBy, killedWith ) + "了自己" :
                "你被" + killedBy( killedBy ) + killedWith( killedBy, killedWith ) + "了") + "...";
    }

    private static String killedWith( Object killedBy, Element killedWith ) {

        String result = "击杀";

        if( killedWith == null ) {

            if( killedBy instanceof Mob) {

                Mob mob = (Mob)killedBy;

                if( Bestiary.isBoss( mob ) || mob instanceof Rat ) {

                    result = "击败";

                } else if ( mob instanceof GnollBrute ) {

                    result = "屠杀";

                } else if ( mob instanceof DwarfMonk ) {

                    result = "击垮";

                } else if ( mob instanceof Golem ) {

                    result = "砸扁";

                } else if ( mob instanceof Piranha ) {

                    result = "活活吃掉";

                } else if ( mob instanceof Mimic ) {

                    result = "偷袭致死";

                }

            } else if( killedBy instanceof DwarvenKing.BoneExplosion ) {

                result = "击杀";

            } if( killedBy instanceof DwarvenKing.KnockBack ) {

                result = "击垮";

            } if( killedBy instanceof BoulderTrap ) {

                result = "击垮";

            }


//        } else if( killedWith instanceof DamageType.Flame) {
//            result = "burned to crisp";
//        } else if( killedWith instanceof DamageType.Frost) {
//            result = "chilled to death";
        } else if( killedWith instanceof Element.Shock) {
            result = "电击致死";
        } else if( killedWith instanceof Element.Acid) {
            result = "融化致死";
//        } else if( killedWith instanceof DamageType.Mind) {
//            result = "";
//        } else if( killedWith instanceof DamageType.Body) {
//            result = "drained";
//        } else if( killedWith instanceof DamageType.Unholy) {
//            result = "withered";
//        } else if( killedWith instanceof DamageType.Energy) {
//            result = "disintegrated";
        }

        return result;
    }

    private static String killedBy( Object killedBy ) {

        String result = "不明原因";

        if( killedBy instanceof Mob ) {
            Mob mob = ((Mob)killedBy);
            result = mob.name ;
        } else if( killedBy instanceof Blob ) {
            Blob blob = ((Blob)killedBy);
            result = Utils.indefinite( blob.name );
        } else if( killedBy instanceof Weapon.Enchantment ) {
            result = "诅咒武器";
        } else if( killedBy instanceof Armour.Glyph ) {
            result = "诅咒护甲";
        } else if( killedBy instanceof Buff ) {
            if( killedBy instanceof Crippled ) {
                result = "大量出血";
            } else if( killedBy instanceof Poisoned ) {
                result = "毒素";
            } else if( killedBy instanceof Satiety ) {
                result = "饥饿";
            } else if( killedBy instanceof Burning ) {
                result = "燃烧";
            } else if( killedBy instanceof Corrosion ) {
                result = "腐蚀淤泥";
            }
        } else if( killedBy instanceof Trap ) {
            result = "陷阱";
        } else if( killedBy instanceof Chasm ) {
            result = "重力";
        } else if( killedBy instanceof DwarvenKing.BoneExplosion ) {
            result = "碎骨爆炸";
        } else if( killedBy instanceof DwarvenKing.KnockBack ) {
            result = "矮人国王";
        }

        return result;
    }

}
