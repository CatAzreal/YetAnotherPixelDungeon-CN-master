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
package com.consideredhamster.yapdcn.actors.blobs;

import com.consideredhamster.yapdcn.Dungeon;
import com.consideredhamster.yapdcn.Journal;
import com.consideredhamster.yapdcn.Journal.Feature;
import com.consideredhamster.yapdcn.items.misc.Waterskin;
import com.consideredhamster.yapdcn.levels.Level;
import com.consideredhamster.yapdcn.levels.Terrain;
import com.consideredhamster.yapdcn.misc.utils.GLog;
import com.consideredhamster.yapdcn.scenes.GameScene;
import com.consideredhamster.yapdcn.visuals.Assets;
import com.consideredhamster.yapdcn.visuals.effects.BlobEmitter;
import com.consideredhamster.yapdcn.visuals.effects.Speck;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;

public class WellWater extends Blob {

    private static final String TXT_PROCCED =
            "你从井里打了些水。";

    private static final String TXT_VIAL_IS_FULL =
            "你的水袋已经装满。";

    private static final String TXT_NO_MORE_WATER =
            "水井已经无水可打。";

    protected int pos;

    @Override
    public void restoreFromBundle( Bundle bundle ) {
        super.restoreFromBundle( bundle );

        for (int i=0; i < LENGTH; i++) {
            if (cur[i] > 0) {
                pos = i;
                break;
            }
        }
    }

    @Override
    protected void evolve() {
        volume = off[pos] = cur[pos];

        if (Dungeon.visible[pos]) {
            Journal.add( Feature.WELL );
        }
    }

    @Override
    public void seed( int cell, int amount ) {
        pos = cell;
        volume = cur[pos] = amount;
    }

    public static void affectCell( int cell ) {

            WellWater water = (WellWater)Dungeon.level.blobs.get( WellWater.class );
            if (water != null &&
                    water.volume > 0 &&
                    water.pos == cell &&
                    water.affect()) {

                Level.set( cell, Terrain.EMPTY_WELL );
                GameScene.updateMap( cell );

                return;
            }
    }

    protected boolean affect() {

        if (pos == Dungeon.hero.pos ) {

            Dungeon.hero.interrupt();

            Waterskin vial = Dungeon.hero.belongings.getItem(Waterskin.class);

            int space = vial.space();

            if ( space > 0 ) {

                int fill = Math.min( space, cur[ pos ] );

                vial.fill( fill );
                volume = off[pos] = cur[pos] -= fill;

                GLog.i(TXT_PROCCED);

                Sample.INSTANCE.play( Assets.SND_DRINK);

                if( cur[ pos ] <= 0 ) {

                    GLog.i(TXT_NO_MORE_WATER);
                    Journal.remove(Feature.WELL);
                    return true;

                }

            } else {

                GLog.i(TXT_VIAL_IS_FULL);

            }
        }

        return false;
    }

    @Override
    public void use( BlobEmitter emitter ) {
        super.use(emitter);
        emitter.start( Speck.factory( Speck.DISCOVER ), 0.5f, 0 );
    }

    @Override
    public String tileDesc() {
        return
                "井中之水看起来清冽爽口。你或许可以在此灌满自己的水袋。";
    }
}
