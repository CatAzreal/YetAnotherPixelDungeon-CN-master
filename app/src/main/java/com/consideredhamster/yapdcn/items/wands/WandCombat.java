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

import com.consideredhamster.yapdcn.Dungeon;
import com.consideredhamster.yapdcn.actors.buffs.debuffs.Charmed;
import com.consideredhamster.yapdcn.actors.buffs.debuffs.Withered;
import com.consideredhamster.yapdcn.actors.hero.Hero;
import com.consideredhamster.yapdcn.items.armours.body.MageArmor;
import com.consideredhamster.yapdcn.items.rings.RingOfMysticism;
import com.consideredhamster.yapdcn.items.rings.RingOfWillpower;
import com.consideredhamster.yapdcn.items.weapons.melee.Quarterstaff;
import com.watabou.utils.Random;

import java.util.Locale;

public abstract class WandCombat extends Wand {

    @Override
    public int maxCharges( int bonus ) {
        return 2 + ( bonus > 0 ? bonus : 0 ) ;
    }

    @Override
    public float effectiveness( int bonus ) {
        return ( 0.30f + 0.05f * state + ( bonus > 0 ? 0.10f * bonus : 0 ) );
    }

    @Override
    public float rechargeRate( int bonus ) {
        return 0.04f;
    }

    @Override
    public int damageRoll() {

        if( curUser != null ) {

            float eff = effectiveness();

            if( curUser.buff( Withered.class ) != null )
                eff *= 0.5f;

            if( curUser.buff( Charmed.class ) != null )
                eff *= 0.5f;

            int max = (int)( curUser.magicPower() * eff );
            int min = max * 3 / 5;

            return Random.IntRange( min, max );

        } else {

            return 0;

        }
    }

    @Override
    protected float miscastChance( int bonus ) {

        if( !isIdentified() || bonus < 0 ){
            return 0.35f - 0.05f * state - 0.05f * bonus ;
        } else {
            return 0f;
        }
    }

    @Override
    protected float squeezeChance( int bonus ) {

        if( isIdentified() && bonus >= 0 ){
            return 0.2f + 0.05f * state + 0.05f * bonus;
        } else {
            return 0f;
        }

    }

    @Override
    protected void spendCharges() {

        if( curCharges > 0 ) curCharges--;

    }

    @Override
    protected String wandInfo() {

        Hero hero = Dungeon.hero;

        final String p = "\n\n";
        StringBuilder info = new StringBuilder();

        // if we are not sure what stats we currently have due to some of the relevant equipment
        // being unidentified, then use base values of these stats. not the best way to do it,
        // but it should work for now. maybe when i'll get to reworking weapons, i'll expand this
        float magicPower =
                hero.belongings.ring1 instanceof RingOfWillpower && !hero.belongings.ring1.isIdentified() ||
                hero.belongings.ring2 instanceof RingOfWillpower && !hero.belongings.ring2.isIdentified() ||
                hero.belongings.weap1 instanceof Quarterstaff && !hero.belongings.weap1.isIdentified() ?
                hero.magicPower : hero.magicPower();

        float attunement =
                hero.belongings.ring1 instanceof RingOfMysticism && !hero.belongings.ring1.isIdentified() ||
                hero.belongings.ring2 instanceof RingOfMysticism && !hero.belongings.ring2.isIdentified() ||
                hero.belongings.armor instanceof MageArmor && !hero.belongings.armor.isIdentified() ?
                hero.baseAttunement() : hero.attunement();

        // again, if the wand is not identified yet, then values are displayed as if it was unupgraded

        int max = (int)( magicPower * effectiveness( isIdentified() ? bonus : 0 ) );
        int min = max * 3 / 5;

        String recharge = String.format( Locale.getDefault(), "%.1f",
                1.0f / attunement / rechargeRate( isIdentified() ? bonus : 0 )
        );

        String chance = String.format( Locale.getDefault(), "%.0f",
            100 * ( !isIdentified() || bonus < 0
            ? miscastChance( isIdentified() ? bonus : 0 )
            : squeezeChance( isIdentified() ? bonus : 0 ) )
        );

        if ( !isIdentified() ){

            info.append(
                "这根法杖_" + ( isCursedKnown() && bonus < 0 ? "受到了诅咒" : "未被鉴定" ) +
                "_, 不过它现在_状态为" + stateToString( state ) + "_。多数情况下这根法杖应该只有_" + maxCharges( 0 ) + " charges_ and will probably " +
                "点充能，并且使用时有_" + chance + "%的几率施法失败。"
            );

            info.append( p );

            info.append(
                "基于你的当前魔能和调谐属性，这根法杖大概每次释放充能时可以造成_" + min + "-" + max + "点伤害" +
                        "_并且_每" + recharge + "回合_恢复1充能。"
            );

        } else {

            info.append(
                "这根法杖_" + ( bonus < 0 ? "受到了诅咒" : "未被诅咒" ) + "_，并且它目前状态为_" + stateToString( state ) +
                "_。法杖目前充能量为_" +  getCharges() + "/" + maxCharges() + "_，并且使用时有_" + chance +"%_的概率"
                + ( bonus < 0 ? "施法失败" : "无充能施法" )
            );

            info.append( p );

            info.append(
                    "基于你的当前魔能和调谐属性，这根法杖每次释放充能时可以造成_" + min + "-" + max + "点伤害" +
                    "_并且_每" + recharge + "回合_恢复1充能。"
            );

        }

        return info.toString();
    }
}
