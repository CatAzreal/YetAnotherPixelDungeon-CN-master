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
package com.consideredhamster.yapdcn.items.misc;

import com.consideredhamster.yapdcn.Dungeon;
import com.consideredhamster.yapdcn.actors.Actor;
import com.consideredhamster.yapdcn.actors.blobs.Blob;
import com.consideredhamster.yapdcn.actors.blobs.Fire;
import com.consideredhamster.yapdcn.actors.buffs.Buff;
import com.consideredhamster.yapdcn.actors.buffs.bonuses.Invisibility;
import com.consideredhamster.yapdcn.actors.buffs.debuffs.Frozen;
import com.consideredhamster.yapdcn.actors.buffs.special.Light;
import com.consideredhamster.yapdcn.actors.hero.Hero;
import com.consideredhamster.yapdcn.items.Item;
import com.consideredhamster.yapdcn.levels.Level;
import com.consideredhamster.yapdcn.misc.mechanics.Ballistica;
import com.consideredhamster.yapdcn.misc.utils.GLog;
import com.consideredhamster.yapdcn.misc.utils.Utils;
import com.consideredhamster.yapdcn.scenes.CellSelector;
import com.consideredhamster.yapdcn.scenes.GameScene;
import com.consideredhamster.yapdcn.visuals.Assets;
import com.consideredhamster.yapdcn.visuals.effects.CellEmitter;
import com.consideredhamster.yapdcn.visuals.effects.particles.FlameParticle;
import com.consideredhamster.yapdcn.visuals.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;

import java.util.ArrayList;

public class OilLantern extends Item {

	public static final String AC_LIGHT	= "点亮";
    public static final String AC_SNUFF    = "熄灭";
    public static final String AC_REFILL   = "灌油";
    public static final String AC_BURN 	= "点火";

	private static final float TIME_TO_USE = 1f;
	private static final int MAX_CHARGE = 100;

	private static final String TXT_STATUS	= "%d%%";

	private static final String TXT_CANT_BURN	= "你还需要一个燃油瓶才能这么做！";
	private static final String TXT_NO_FLASKS	= "你没有足够的燃料填充油灯！";

	private static final String TXT_DEACTIVATE = "你的油灯突然闪烁着昏暗的火光，随后熄灭了！";

    private static final String TXT_REFILL = "你将燃料灌入油灯中。";

    private static final String TXT_LIGHT = "你点燃了油灯。";

    private static final String TXT_SNUFF = "你熄灭了油灯。";

    private static final String TXT_BURN_SELF = "你将燃油瓶中的燃油倾倒在身上，然后点燃了自己。这...你...为了啥啊？";
    private static final String TXT_BURN_TILE = "你将燃油瓶中的燃油倾倒在一旁，随后点燃了燃油。";
    private static final String TXT_BURN_FAIL = "你试着点燃旁边的一处地格，但火势没能蔓延开来。";

	{
		name = "煤油灯";
		image = ItemSpriteSheet.LANTERN;

        active = false;
        charge = MAX_CHARGE;
        flasks = 0;

		visible = false;
		unique = true;

        updateSprite();
	}

    private boolean active;
    private int charge;
    private int flasks;

    private static final String ACTIVE = "active";
    private static final String FLASKS = "flasks";
    private static final String CHARGE = "charge";

    public void updateSprite() {
        image = isActivated() ? ItemSpriteSheet.LANTERN_LIT : ItemSpriteSheet.LANTERN ;
    }

    public int getCharge() {
        return charge;
    }

    public int getFlasks() {
        return flasks;
    }

    public void spendCharge() {
        charge--;
        updateQuickslot();
    }

    public boolean isActivated() {
        return active ;
    }

    @Override
    public String quickAction() {
        return charge > 0 ? ( isActivated() ? AC_SNUFF : AC_LIGHT ) : AC_REFILL ;
    }
	
	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle( bundle );
        bundle.put( ACTIVE, active );
        bundle.put( CHARGE, charge );
        bundle.put( FLASKS, flasks );
	}
	
	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle( bundle );
        active = bundle.getBoolean( ACTIVE );
        charge = bundle.getInt( CHARGE );
        flasks = bundle.getInt( FLASKS );

        updateSprite();
	}
	
	@Override
	public ArrayList<String> actions( Hero hero ) {
		ArrayList<String> actions = super.actions( hero );

        actions.add( isActivated() ? AC_SNUFF : AC_LIGHT );
        actions.add( AC_REFILL );
        actions.add( AC_BURN );

        actions.remove( AC_THROW );
        actions.remove( AC_DROP );

		return actions;
	}

	@Override
	public void execute( final Hero hero, String action ) {

        if (action.equals( AC_LIGHT )) {

            if( charge > 0 ){

                if( hero.buff( Frozen.class ) == null ){

                    activate( hero, true );

                } else {

                    GLog.n( Frozen.TXT_CANNOT_LIGHT );

                }

            }

        } else if (action.equals( AC_SNUFF ) ) {

            if( isActivated() ){

                deactivate( hero, true );

            }

        } else if (action.equals( AC_REFILL ) ) {

            if ( flasks > 0 ) {

                refill( hero );

            } else {
                GLog.w( TXT_NO_FLASKS );
            }

        } else if (action.equals( AC_BURN ) ) {

            if ( flasks > 0 ) {

                curUser = hero;
                curItem = this;

                GameScene.selectCell( burner );

            } else {
                GLog.w( TXT_CANT_BURN );
            }

        } else {

			super.execute( hero, action );
			
		}
	}

    public void refill( Hero hero ) {

        flasks--;
        charge = MAX_CHARGE;

        hero.spend( TIME_TO_USE );
        hero.busy();

        Sample.INSTANCE.play( Assets.SND_DRINK, 1.0f, 1.0f, 1.2f );
        hero.sprite.operate( hero.pos );

        GLog.i( TXT_REFILL );
        updateQuickslot();

    }

    public void activate( Hero hero, boolean voluntary ) {

        active = true;
        updateSprite();

        Buff.affect( hero, Light.class );
//        hero.updateSpriteState();

        hero.search( false );

        if( voluntary ){

            hero.spend( TIME_TO_USE );
            hero.busy();

            GLog.i( TXT_LIGHT );
            hero.sprite.operate( hero.pos );

        }

        Sample.INSTANCE.play( Assets.SND_CLICK );
        updateQuickslot();

        Invisibility.dispel();
        Dungeon.observe();

    }

    public void deactivate( Hero hero, boolean voluntary ) {

        active = false;
        updateSprite();

        Buff.detach( hero, Light.class );
//        hero.updateSpriteState();

        if( voluntary ){

            hero.spend( TIME_TO_USE );
            hero.busy();

            hero.sprite.operate( hero.pos );
            GLog.i( TXT_SNUFF );

        } else {

            GLog.w( TXT_DEACTIVATE );

        }

        Sample.INSTANCE.play( Assets.SND_PUFF );
        updateQuickslot();

        Dungeon.observe();

    }
	
	public OilLantern collectFlask( OilFlask oil ) {

		flasks += oil.quantity;

		updateQuickslot();

        return this;

	}

    @Override
    public int price() {
        return 0;
    }

	@Override
	public String status() {
		return Utils.format( TXT_STATUS, charge );
	}
	
	@Override
	public String info() {
		return 
			"在因光源极度缺乏而知名的地牢之下，这款由结实玻璃打造而成的油灯是不可或缺的道具之一。只要燃料充足，就算是最黑暗的地牢之中，这个简便的设备也能照亮你的前方。\n\n" +
                ( isActivated() ?
                    "这盏小小油灯正充满活力地绽放出火光，照亮你的身心。" :
                    "这盏小小油灯的灯芯被掐灭了，静待着它再度燃烧的时刻。"
                ) +
            "灯中还剩下" + ( charge / 10.0 ) + "盎司的煤油，而你还有" + flasks + "瓶煤油可供其使用。";
	}

	public static class OilFlask extends Item {

        {
            name = "煤油瓶";
            image = ItemSpriteSheet.OIL_FLASK;

            visible = false;
        }

        @Override
        public boolean doPickUp( Hero hero ) {

            OilLantern lamp = hero.belongings.getItem( OilLantern.class );

            if (lamp != null) {

                lamp.collectFlask( this );
                GameScene.pickUp(this);

                Sample.INSTANCE.play(Assets.SND_ITEM);

                return true;

            }

            return super.doPickUp(hero);
        }


        @Override
        public int price() {
            return quantity * 30;
        }

        @Override
        public String info() {
            return
                "这个小瓶中装着10盎司的煤油。可以用于油灯燃料，也可以用于点燃周围的地格。";
        }
    }



    protected static CellSelector.Listener burner = new CellSelector.Listener() {
        @Override
        public void onSelect( Integer target ) {

            if (target != null) {

                Ballistica.cast( curUser.pos, target, false, true );

                int cell = Ballistica.trace[ 0 ];

                if( Ballistica.distance > 0 ){
                    cell = Ballistica.trace[ 1 ];
                }

                if( Level.flammable[ cell ] || !Level.solid[ cell ] && !Level.chasm[ cell ] ){
                    GameScene.add( Blob.seed( cell, 5, Fire.class ) );
                }

                ((OilLantern)curItem).flasks--;
                Invisibility.dispel();

                if( curUser.pos == cell ) {
                    GLog.i( TXT_BURN_SELF );
                } else if( Level.flammable[ cell ] || !Level.solid[ cell ] && !Level.chasm[ cell ] ){
                    GLog.i( TXT_BURN_TILE );
                } else {
                    GLog.i( TXT_BURN_FAIL );
                }

                Sample.INSTANCE.play(Assets.SND_BURNING, 0.6f, 0.6f, 1.5f);
                CellEmitter.get( cell ).burst( FlameParticle.FACTORY, 5 );

                curUser.sprite.operate(cell);
                curUser.busy();
                curUser.spend( Actor.TICK );

            }
        }
        @Override
        public String prompt() {
            return "选择一处要点火的地格";
        }
    };
}
