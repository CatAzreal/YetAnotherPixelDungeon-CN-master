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
package com.consideredhamster.yapdcn.items.rings;

import java.util.HashSet;

import com.consideredhamster.yapdcn.Badges;
import com.consideredhamster.yapdcn.Dungeon;
import com.consideredhamster.yapdcn.YetAnotherPixelDungeon;
import com.consideredhamster.yapdcn.actors.Char;
import com.consideredhamster.yapdcn.actors.buffs.Buff;
import com.consideredhamster.yapdcn.actors.hero.Hero;
import com.consideredhamster.yapdcn.items.EquipableItem;
import com.consideredhamster.yapdcn.items.Item;
import com.consideredhamster.yapdcn.items.ItemStatusHandler;
import com.consideredhamster.yapdcn.visuals.sprites.ItemSpriteSheet;
import com.consideredhamster.yapdcn.visuals.ui.QuickSlot;
import com.consideredhamster.yapdcn.misc.utils.GLog;
import com.consideredhamster.yapdcn.misc.utils.Utils;
import com.consideredhamster.yapdcn.visuals.windows.WndOptions;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public abstract class Ring extends EquipableItem {

	private static final int TICKS_TO_KNOW	= 100;

	private static final float TIME_TO_EQUIP = 1f;

	private static final String TXT_IDENTIFY_NORMAL = "你对你的戒指已经足够熟悉并且可以因此将其完全鉴定。它是%s+";
	private static final String TXT_IDENTIFY_CURSED = "你对你的戒指已经足够熟悉并且可以因此将其完全鉴定。它是%s-";

	private static final String TXT_UNEQUIP_TITLE = "卸下一枚戒指";
	private static final String TXT_UNEQUIP_MESSAGE =
		"你只能同时装备两枚戒指，请卸下一枚已装备戒指再尝试。";

	protected Buff buff;

	private static final Class<?>[] rings = {
		RingOfVitality.class,
		RingOfAwareness.class,
		RingOfShadows.class,
		RingOfMysticism.class,
		RingOfKnowledge.class,
		RingOfAccuracy.class,
		RingOfEvasion.class,
		RingOfSatiety.class,
		RingOfDurability.class,
		RingOfFortune.class,
		RingOfProtection.class,
		RingOfWillpower.class
	};
	private static final String[] gems =
		{"钻石", "欧珀", "石榴石", "红宝石", "紫水晶", "黄玉", "黑曜石", "碧玺", "绿宝石", "蓝宝石", "石英", "玛瑙"};
	private static final Integer[] images = {
		ItemSpriteSheet.RING_DIAMOND,
		ItemSpriteSheet.RING_OPAL,
		ItemSpriteSheet.RING_GARNET,
		ItemSpriteSheet.RING_RUBY,
		ItemSpriteSheet.RING_AMETHYST,
		ItemSpriteSheet.RING_TOPAZ,
		ItemSpriteSheet.RING_ONYX,
		ItemSpriteSheet.RING_TOURMALINE,
		ItemSpriteSheet.RING_EMERALD,
		ItemSpriteSheet.RING_SAPPHIRE,
		ItemSpriteSheet.RING_QUARTZ,
		ItemSpriteSheet.RING_AGATE};

	private static ItemStatusHandler<Ring> handler;

	private String gem;

	private int ticksToKnow = Random.IntRange(TICKS_TO_KNOW, TICKS_TO_KNOW * 2);

	@SuppressWarnings("unchecked")
	public static void initGems() {
		handler = new ItemStatusHandler<Ring>( (Class<? extends Ring>[])rings, gems, images );
	}

	public static void save( Bundle bundle ) {
		handler.save( bundle );
	}

	@SuppressWarnings("unchecked")
	public static void restore( Bundle bundle ) {
		handler = new ItemStatusHandler<Ring>( (Class<? extends Ring>[])rings, gems, images, bundle );
	}

	public Ring() {
		super();
		syncGem();
        shortName = "??";
    }

	public void syncGem() {
		image	= handler.image( this );
		gem		= handler.label( this );
	}

    @Override
    public String quickAction() {
        return isEquipped( Dungeon.hero ) ? AC_UNEQUIP : AC_EQUIP;
    }

	@Override
	public boolean doEquip( final Hero hero ) {

		if (hero.belongings.ring1 != null && hero.belongings.ring2 != null) {

			final Ring r1 = hero.belongings.ring1;
			final Ring r2 = hero.belongings.ring2;

			YetAnotherPixelDungeon.scene().add(
				new WndOptions( TXT_UNEQUIP_TITLE, TXT_UNEQUIP_MESSAGE,
					Utils.capitalize( r1.toString() ),
					Utils.capitalize( r2.toString() ) ) {

					@Override
					protected void onSelect( int index ) {

						detach( hero.belongings.backpack );

						Ring equipped = (index == 0 ? r1 : r2);

                        if( QuickSlot.quickslot1.value == Ring.this && equipped.bonus >= 0 )
                            QuickSlot.quickslot1.value = equipped ;

                        if( QuickSlot.quickslot2.value == Ring.this && equipped.bonus >= 0 )
                            QuickSlot.quickslot2.value = equipped ;

                        if( QuickSlot.quickslot3.value == Ring.this && equipped.bonus >= 0 )
                            QuickSlot.quickslot3.value = equipped ;

						if (equipped.doUnequip( hero, true, false )) {

							if( !doEquip( hero ) ) {
                                equipped.doEquip( hero );
                            }

						} else {

							collect( hero.belongings.backpack );

						}
					}
				} );

			return false;

		} else {

            if( ( bonus >= 0 || isCursedKnown() || !detectCursed( this, hero ) ) ) {

                if (hero.belongings.ring1 == null) {
                    hero.belongings.ring1 = this;
                } else {
                    hero.belongings.ring2 = this;
                }

                if (QuickSlot.quickslot1.value == Ring.this)
                    QuickSlot.quickslot1.value = null;

                if (QuickSlot.quickslot2.value == Ring.this)
                    QuickSlot.quickslot2.value = null;

                if (QuickSlot.quickslot3.value == Ring.this)
                    QuickSlot.quickslot3.value = null;

                detach(hero.belongings.backpack);

                onEquip( hero );

                activate(hero);

                hero.spendAndNext( TIME_TO_EQUIP );

                return true;

            } else {

                if ( !collect( hero.belongings.backpack ) ) {
                    Dungeon.level.drop( this, hero.pos ).sprite.drop();
                }

                hero.spendAndNext( TIME_TO_EQUIP * 0.5f );

                return false;

            }
		}
	}

    @Override
	public void activate( Char ch ) {
		buff = buff();
		buff.attachTo( ch );
	}

    public void deactivate( Char ch ) {
        if( buff != null ) {
            buff.target.remove(buff);
            buff = null;
        }
    }

	@Override
	public boolean doUnequip( Hero hero, boolean collect, boolean single ) {
		if (super.doUnequip( hero, collect, single )) {

			if (hero.belongings.ring1 == this) {
				hero.belongings.ring1 = null;
			} else {
				hero.belongings.ring2 = null;
			}

			hero.remove( buff );
			buff = null;

            QuickSlot.refresh();

			return true;

		} else {

			return false;

		}
	}

	@Override
	public boolean isEquipped( Hero hero ) {
		return hero.belongings.ring1 == this || hero.belongings.ring2 == this;
	}

    @Override
    public boolean isUpgradeable() {
        return true;
    }

    @Override
    public boolean isIdentified() {
        return known >= UPGRADE_KNOWN;
    }

    @Override
    public boolean isCursedKnown() {
        return known >= CURSED_KNOWN;
    }

    @Override
	public boolean isTypeKnown() {
		return handler.isKnown( this );
	}

	protected void setKnown() {
		if (!isTypeKnown()) {
			handler.know( this );
		}

		Badges.validateAllRingsIdentified();
	}

	@Override
	public String name() {
		return isTypeKnown() ? super.name() : gem + "戒指";
	}

    @Override
    public String desc() {
        if( isIdentified() && bonus >= 0 ) {
            return "这枚戒指_已被鉴定_。同种类戒指效果可叠加。";
        } else if( isCursedKnown() && bonus < 0 ) {
            return "这枚戒指被诅咒了。其效果与非诅咒戒指正好相反，并且无法正常摘除。";
        } else {
            return "这枚戒指_尚未鉴定_。你不清楚它的具体功用。";
        }
    }

    @Override
    public String info() {

        final String p = "\n\n";

        StringBuilder info = new StringBuilder( isTypeKnown() ? desc() :
            "这枚金属环镶嵌着一颗在黑暗中闪烁光芒的大块"+gem+"。谁知道戴上后会有什么效果？"
        );

        info.append( p );

        if ( isEquipped( Dungeon.hero ) ) {
            info.append( "这枚戒指正戴在自己的手指上。" );
        } else if( Dungeon.hero.belongings.backpack.items.contains(this) ) {
            info.append( "这枚戒指正在你的背包里。" );
        } else {
            info.append( "这枚戒指在地面上。" );
        }

        if( bonus < 0 && isCursedKnown() ) {

            info.append( "" );

            if( isEquipped( Dungeon.hero ) ){
                info.append( "恶毒的法术正阻止着你将其除下。" );
            } else {
                info.append( "你能感觉到这件戒指里潜伏着一股充满恶意的魔力。" );
            }
        }

        return info.toString();
	}

	@Override
	public Item identify() {
		setKnown();
		return super.identify();
	}

	public static boolean allKnown() {
		return handler.known().size() == rings.length;
	}

    public static HashSet<Class<? extends Ring>> getKnown() {
        return handler.known();
    }

	@Override
	public int price() {

		int price = 75;

        if ( isIdentified() ) {
            price += bonus > 0 ? price * bonus / 3 : price * bonus / 6 ;
        } else if( isCursedKnown() && bonus >= 0 ) {
            price -= price / 4;
        } else {
            price /= 2;
        }

        return price;
	}

    @Override
    public float stealingDifficulty() { return 0.75f; }

	protected RingBuff buff() {
		return null;
	}

    public static float effect( int level ) {
        return level >= 0 ? 0.2f + 0.1f * level : 0.1f * level - 0.1f;
    }

	private static final String UNFAMILIRIARITY	= "unfamiliarity";

	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle( bundle );
		bundle.put( UNFAMILIRIARITY, ticksToKnow );
	}

	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle( bundle );
		if ((ticksToKnow = bundle.getInt( UNFAMILIRIARITY )) == 0) {
			ticksToKnow = TICKS_TO_KNOW;
		}
	}

    public class RingBuff extends Buff {

        protected String desc() {
            return "你没能感觉到这枚戒指有任何特殊的效果。";
        }

        public float effect() {
            return Ring.effect( Ring.this.bonus );
        }

		@Override
		public boolean attachTo( Char target ) {

			if (target instanceof Hero && !isTypeKnown()) {

				setKnown();

                if( bonus < 0 ){
                    GLog.n( desc() );
                } else {
                    GLog.i( desc() );
                }

			}

			return super.attachTo(target);
		}

		@Override
		public boolean act() {

            if (!isIdentified() && !((Hero)target).restoreHealth) {

                float effect = target.ringBuffs( RingOfKnowledge.Knowledge.class);

                ticksToKnow -= (int) effect;
                ticksToKnow -= Random.Float() < effect % 1 ? 1 : 0 ;

                if (ticksToKnow <= 0) {
                    String gemName = name();
                    identify();
					String normal = TXT_IDENTIFY_NORMAL + Math.abs(Ring.this.bonus) + "。\n";
					String cursed = TXT_IDENTIFY_CURSED + Math.abs(Ring.this.bonus) + "。\n";
                    GLog.i(
                        Ring.this.bonus >= 0 ? normal : cursed,
                        gemName, Ring.this.name()
                    );
                }
            }

			spend( TICK );

			return true;
		}
	}
}
