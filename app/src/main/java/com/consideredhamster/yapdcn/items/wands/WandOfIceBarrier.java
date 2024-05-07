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
import com.consideredhamster.yapdcn.actors.Char;
import com.consideredhamster.yapdcn.actors.buffs.BuffActive;
import com.consideredhamster.yapdcn.actors.buffs.debuffs.Frozen;
import com.consideredhamster.yapdcn.actors.mobs.npcs.NPC;
import com.consideredhamster.yapdcn.misc.utils.GLog;
import com.consideredhamster.yapdcn.scenes.GameScene;
import com.consideredhamster.yapdcn.visuals.effects.CellEmitter;
import com.consideredhamster.yapdcn.visuals.effects.Speck;
import com.consideredhamster.yapdcn.visuals.sprites.IceBlockSprite;
import com.consideredhamster.yapdcn.visuals.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;
import com.consideredhamster.yapdcn.visuals.Assets;
import com.consideredhamster.yapdcn.Dungeon;
import com.consideredhamster.yapdcn.actors.Actor;
import com.consideredhamster.yapdcn.visuals.effects.MagicMissile;
import com.consideredhamster.yapdcn.levels.Level;
import com.watabou.utils.Callback;

public class WandOfIceBarrier extends WandUtility {

	{
		name = "冰壁法杖";
        image = ItemSpriteSheet.WAND_ICEBARRIER;

        hitChars = false;
        goThrough = false;

	}

    @Override
    public float effectiveness( int bonus ) {
        return super.effectiveness( bonus ) * 0.80f;
    }

//    protected static ArrayList<Integer> cells = new ArrayList<Integer>();

	@Override
	protected void onZap( int cell ) {

	    int distance = Level.distance( curUser.pos, cell );

        for ( int n : Level.NEIGHBOURS5 ) {

            int c = cell + n;
            CellEmitter.get( c ).burst( Speck.factory( Speck.ICESHARD ), 3 );

            if( distance == Level.distance( curUser.pos, c ) && ( Actor.findChar( c ) != null || Level.passable[ c ]  ) ) {

                int power = ( Level.water[ c ] ? damageRoll() * 3 / 2 : damageRoll() );

                Char ch = Actor.findChar( c );

                if( ch != null ){

                    if( ch instanceof IceBlock ) {
                        ch.HT += power;
                        ch.heal( power );
                    } else {
                        power = Char.absorb( power, ch.armorClass(), false );
                        ch.damage( power, curUser, Element.PHYSICAL );

                        if( ch.isAlive() ){
                            BuffActive.add( ch, Frozen.class, power );
                        }
                    }

                } else if( power > 0 ) {

                    IceBlock.spawnAt( power, c );

                }
            }
        }
    }

    @Override
	protected void fx( final int cell, final Callback callback ) {

        MagicMissile.frost( curUser.sprite.parent, curUser.pos, cell, callback );
        Sample.INSTANCE.play( Assets.SND_ZAP );

//        cells = new ArrayList<>();
//        cells.add( cell );

//        MagicMissile.frost( curUser.sprite.parent, curUser.pos, cell, new Callback() {
//            @Override
//            public void call(){
//            curUser.ready();
//            GameScene.selectCell( new SecondaryListener( cell, callback ) );
//            }
//        } );

	}
	
	@Override
	public String desc() {
		return 
			"这根被寒霜覆盖着的法杖能使施法者在指定地块周围凭空创造一面冰墙。如果目标地块有其他单位，则对其造成冰冻伤害。该效果在水面上效果更强。";
	}

//    private static class SecondaryListener implements CellSelector.Listener {
//
//        public Callback callback;
//        public Integer source;
//
//        public SecondaryListener( Integer source, Callback callback ) {
//            this.source = source;
//            this.callback = callback;
//        }
//
//        @Override
//        public void onSelect( Integer target ){
//
//            if( target != null && !source.equals( target ) ){
//
//                target = Ballistica.cast( source, target, false, false );
//                MagicMissile.shards( curUser.sprite.parent, source, target, null );
//
//                if( Ballistica.distance > 0 ){
//
//                    for( int i = 1 ; i <= Ballistica.distance ; i++ ){
//                        cells.add( Ballistica.trace[ i ] );
//                    }
//                }
//
////                target = Ballistica.cast( source, source + source - target, false, false );
////                MagicMissile.shards( curUser.sprite.parent, source, target, null );
////
////                if( Ballistica.distance > 0 ){
////
////                    for( int i = 1 ; i <= Ballistica.distance ; i++ ){
////                        cells.add( Ballistica.trace[ i ] );
////                    }
////                }
//            }
//
//            Sample.INSTANCE.play( Assets.SND_ZAP );
//            callback.call();
//
//        }
//
//        @Override
//        public String prompt(){
//            return "Choose another tile";
//        }
//    };

    public static class IceBlock extends NPC {

        public IceBlock(){

            name = "冰墙";
            spriteClass = IceBlockSprite.class;

            resistances.put( Element.Flame.class, Element.Resist.VULNERABLE );
            resistances.put( Element.Explosion.class, Element.Resist.VULNERABLE );

            resistances.put( Element.Shock.class, Element.Resist.PARTIAL);
            resistances.put( Element.Acid.class, Element.Resist.PARTIAL);

            resistances.put( Element.Frost.class, Element.Resist.IMMUNE );
            resistances.put( Element.Body.class, Element.Resist.IMMUNE );
            resistances.put( Element.Mind.class, Element.Resist.IMMUNE );
            resistances.put( Element.Dispel.class, Element.Resist.IMMUNE );

            resistances.put( Element.Ensnaring.class, Element.Resist.IMMUNE );
            resistances.put( Element.Knockback.class, Element.Resist.IMMUNE );

            hostile = false;
            friendly = true;
        }

        @Override
        public boolean isMagical() {
            return false;
        }

        @Override
        protected boolean getCloser(int target) {
            return true;
        }

        @Override
        protected boolean getFurther(int target) {
            return true;
        }

        @Override
        public int viewDistance() {
            return 0;
        };

        @Override
        protected boolean act() {

            if( --HP <= 0 ){
                die( null );
                return true;
            }

            state = PASSIVE;

            return super.act();
        }

        @Override
        public void interact(){

            GLog.i( "你驱散了这块墙壁" );
            Sample.INSTANCE.play( Assets.SND_MELD );

            Dungeon.hero.sprite.pickup( pos );
            Dungeon.hero.spend( TICK );
            Dungeon.hero.busy();

            die( null );

        }

        @Override
        public Char chooseEnemy() {
            return null;
        }

        private void adjustStats( int level ) {
            HT = level * 2;
            armorClass = 0;

            minDamage = 0;
            maxDamage = 0;

            accuracy = 0;
            dexterity = 0;
        }

        public static IceBlock spawnAt( int level, int pos ){

            // cannot spawn on walls, chasms or already ocupied tiles
            if ( !Level.solid[pos] && !Level.chasm[pos] && Actor.findChar( pos ) == null ){

                IceBlock block = new IceBlock();

                block.adjustStats( level );
                block.HP = block.HT;

                block.pos = pos;
                block.enemySeen = true;
                block.state = block.PASSIVE;

                GameScene.add( block, TICK );
                Dungeon.level.press( block.pos, block );
                block.sprite.spawn();

                return block;

            } else {

                return null;

            }
        }

        @Override
        public void die( Object cause, Element dmg ) {
            super.die( cause, dmg );

            if ( cause != null && Dungeon.visible[pos] ) {
                CellEmitter.get( pos ).burst( Speck.factory( Speck.ICESHARD ), 8 );
            }
        }

        @Override
        public String description() {
            return "这块冰墙是由你的冰壁法杖生成的。它正在逐渐融化，也会被攻击破坏。在装备法杖的状态下与其交互可以直接驱散冰墙。";
        }
    }
}
