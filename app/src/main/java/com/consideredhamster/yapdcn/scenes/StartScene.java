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
package com.consideredhamster.yapdcn.scenes;

import java.util.ArrayList;
import java.util.HashMap;

import com.consideredhamster.yapdcn.Difficulties;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.watabou.noosa.RenderedText;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.particles.Emitter;
import com.watabou.noosa.ui.Button;
import com.consideredhamster.yapdcn.visuals.Assets;
import com.consideredhamster.yapdcn.Badges;
import com.consideredhamster.yapdcn.Dungeon;
import com.consideredhamster.yapdcn.GamesInProgress;
import com.consideredhamster.yapdcn.YetAnotherPixelDungeon;
import com.consideredhamster.yapdcn.actors.hero.HeroClass;
import com.consideredhamster.yapdcn.visuals.effects.BannerSprites;
import com.consideredhamster.yapdcn.visuals.effects.Speck;
import com.consideredhamster.yapdcn.visuals.effects.BannerSprites.Type;
import com.consideredhamster.yapdcn.visuals.ui.Archs;
import com.consideredhamster.yapdcn.visuals.ui.ExitButton;
import com.consideredhamster.yapdcn.visuals.ui.RedButton;
import com.consideredhamster.yapdcn.misc.utils.Utils;
import com.consideredhamster.yapdcn.visuals.windows.WndClass;
import com.consideredhamster.yapdcn.visuals.windows.WndOptions;

public class StartScene extends PixelScene {

	private static final float BUTTON_HEIGHT	= 24;
	private static final float GAP				= 2;
	
	private static final String TXT_LOAD	= "加载游戏";
	private static final String TXT_NEW		= "开始游戏";
	
	private static final String TXT_ERASE		= "将覆盖当前游戏";
	private static final String TXT_DPTH_LVL	= "第%d层, %d级";
	
	private static final String TXT_REALLY	= "Do you really want to start new game?";
	private static final String TXT_WARNING	= "Your current game progress will be erased.";
	private static final String TXT_YES		= "Yes, start new game";
	private static final String TXT_NO		= "No, I changed my mind";

    private static final String TXT_DIFF_TITLE	= "Select difficulty";
    private static final String TXT_DIFF_ABOUT	= "";

    private static final String TXT_DIFFICULTY	= "Start on %s difficulty?";
//            "\u007F You can't earn badges while playing on Easy mode gives no badges\n" +
//            "\u007F Hardcore";
	
//	private static final String TXT_UNLOCK_1	= "To unlock this character class, defeat the 1st boss on %s difficulty or higher";
//	private static final String TXT_UNLOCK_2	= "To unlock this character class, defeat the 2nd boss on %s difficulty or higher";
//	private static final String TXT_UNLOCK_3	= "To unlock this character class, defeat the 3rd boss on %s difficulty or higher";

	private static final String TXT_LOCKED = "Got it!";

	private static final float WIDTH_P	= 116;
	private static final float HEIGHT_P	= 220;
	
	private static final float WIDTH_L	= 224;
	private static final float HEIGHT_L	= 124;
	
	private static HashMap<HeroClass, ClassShield> shields = new HashMap<HeroClass, ClassShield>();
	
	private float buttonX;
	private float buttonY;
	
	private GameButton btnLoad;
	private GameButton btnNewGame;

    private boolean brigandUnlocked;
    private boolean scholarUnlocked;
	private boolean acolyteUnlocked;

//	private Group unlock1;
//	private Group unlock2;
//	private Group unlock3;

	public static HeroClass curClass;
	
	@Override
	public void create() {
		
		super.create();
		
		Badges.loadGlobal();
		
		uiCamera.visible = false;
		
		int w = Camera.main.width;
		int h = Camera.main.height;
		
		float width, height;
		if (YetAnotherPixelDungeon.landscape()) {
			width = WIDTH_L;
			height = HEIGHT_L;
		} else {
			width = WIDTH_P;
			height = HEIGHT_P;
		}

		float left = (w - width) / 2;
		float top = (h - height) / 2; 
		float bottom = h - top;
		
		Archs archs = new Archs();
		archs.setSize( w, h );
		add( archs ); 
		
		Image title = BannerSprites.get( Type.SELECT_YOUR_HERO );
		title.x = align( (w - title.width()) / 2 );
		title.y = align( top - ( YetAnotherPixelDungeon.landscape() ? 24 : 8 ) );
		add( title );
		
		buttonX = left;
		buttonY = bottom - BUTTON_HEIGHT;
		
		btnNewGame = new GameButton( TXT_NEW ) {
			@Override
			protected void onClick() {
				if (GamesInProgress.check( curClass ) != null) {
					StartScene.this.add( new WndOptions( TXT_REALLY, TXT_WARNING, TXT_YES, TXT_NO ) {
						@Override
						protected void onSelect( int index ) {
							if (index == 0) {
                                askDifficulty();
							}
						}
					} );
					
				} else {
                    askDifficulty();
				}
			}
		};
		add( btnNewGame );

		btnLoad = new GameButton( TXT_LOAD ) {	
			@Override
			protected void onClick() {
				InterlevelScene.mode = InterlevelScene.Mode.CONTINUE;
				Game.switchScene( InterlevelScene.class );
			}
		};
		add( btnLoad );	
		
		float centralHeight = buttonY - title.y - title.height();
		
		HeroClass[] classes = {
			HeroClass.WARRIOR, HeroClass.BRIGAND, HeroClass.SCHOLAR, HeroClass.ACOLYTE
		};

		for (HeroClass cl : classes) {
			ClassShield shield = new ClassShield( cl );
			shields.put( cl, shield );
			add( shield );
		}

		if (YetAnotherPixelDungeon.landscape()) {
			float shieldW = width / 4;
			float shieldH = Math.min( centralHeight, shieldW );
			top = title.y + title.height + (centralHeight - shieldH) / 2;
			for (int i=0; i < classes.length; i++) {
				ClassShield shield = shields.get( classes[i] );
				shield.setRect( left + i * shieldW, top, shieldW, shieldH );
			}
//
//			ChallengeButton challenge = new ChallengeButton();
//			challenge.setPos(
//				w / 2 - challenge.width() / 2,
//				top + shieldH - challenge.height() / 2 );
//			add( challenge );
			
		} else {
			float shieldW = width / 2;
			float shieldH = Math.min( centralHeight / 2, shieldW * 1.2f );
			top = title.y + title.height() + centralHeight / 2 - shieldH;
			for (int i=0; i < classes.length; i++) {
				ClassShield shield = shields.get( classes[i] );
				shield.setRect(
					left + (i % 2) * shieldW,
					top + (i / 2) * shieldH,
					shieldW, shieldH );
			}
			
//			ChallengeButton challenge = new ChallengeButton();
//			challenge.setPos(
//				w / 2 - challenge.width() / 2,
//				top + shieldH - challenge.height() / 2 );
//			add( challenge );
		}

//        unlock1 = new Group();
//        add(unlock1);

//        if (!(brigandUnlocked = Badges.isUnlocked( Badges.Badge.BOSS_SLAIN_1 ))) {
//
//            BitmapTextMultiline text = PixelScene.createMultiline(
//                    Utils.format(TXT_UNLOCK_1, Difficulties.NAMES[Difficulties.NORMAL]), 9 );
//            text.maxWidth = (int)width;
//            text.measure();
//
//            float pos = (bottom - BUTTON_HEIGHT) + (BUTTON_HEIGHT - text.height()) / 2;
//            for (BitmapText line : text.new LineSplitter().split()) {
//                line.measure();
//                line.hardlight( 0xFFFF00 );
//                line.x = PixelScene.align( w / 2 - line.width() / 2 );
//                line.y = PixelScene.align( pos );
//                unlock1.add(line);
//
//                pos += line.height();
//            }
//        }
//
//        unlock2 = new Group();
//        add(unlock2);
//
//        if (!(scholarUnlocked = Badges.isUnlocked( Badges.Badge.BOSS_SLAIN_2 ))) {
//
//            BitmapTextMultiline text = PixelScene.createMultiline(
//                    Utils.format(TXT_UNLOCK_2, Difficulties.NAMES[Difficulties.NORMAL]), 9 );
//            text.maxWidth = (int)width;
//            text.measure();
//
//            float pos = (bottom - BUTTON_HEIGHT) + (BUTTON_HEIGHT - text.height()) / 2;
//            for (BitmapText line : text.new LineSplitter().split()) {
//                line.measure();
//                line.hardlight( 0xFFFF00 );
//                line.x = PixelScene.align( w / 2 - line.width() / 2 );
//                line.y = PixelScene.align( pos );
//                unlock2.add(line);
//
//                pos += line.height();
//            }
//        }
//
//
//        unlock3 = new Group();
//		add(unlock3);
//
//		if (!(acolyteUnlocked = Badges.isUnlocked( Badges.Badge.BOSS_SLAIN_3 ))) {
//
//			BitmapTextMultiline text = PixelScene.createMultiline(
//                    Utils.format(TXT_UNLOCK_3, Difficulties.NAMES[Difficulties.NORMAL]), 9 );
//			text.maxWidth = (int)width;
//			text.measure();
//
//			float pos = (bottom - BUTTON_HEIGHT) + (BUTTON_HEIGHT - text.height()) / 2;
//			for (BitmapText line : text.new LineSplitter().split()) {
//				line.measure();
//				line.hardlight( 0xFFFF00 );
//				line.x = PixelScene.align( w / 2 - line.width() / 2 );
//				line.y = PixelScene.align( pos );
//				unlock3.add(line);
//
//				pos += line.height();
//			}
//		}
		
		ExitButton btnExit = new ExitButton();
		btnExit.setPos( Camera.main.width - btnExit.width(), 0 );
		add( btnExit );
		
		curClass = null;
		updateClass( HeroClass.values()[YetAnotherPixelDungeon.lastClass()] );
		
		fadeIn();
		
//		Badges.loadingListener = new Callback() {
//			@Override
//			public void call() {
//				if (Game.scene() == StartScene.this) {
//					YetAnotherPixelDungeon.switchNoFade(StartScene.class);
//				}
//			}
//		};
	}
	
//	@Override
//	public void destroy() {
//
//		Badges.saveGlobal();
//		Badges.loadingListener = null;
//
//		super.destroy();
//	}
	
	private void updateClass( HeroClass cl ) {
		
		if (curClass == cl) {
			add( new WndClass( cl ) );
			return;
		}
		
		if (curClass != null) {
			shields.get( curClass ).highlight( false );
		}
		shields.get( curClass = cl ).highlight(true);

//        if (cl == HeroClass.BRIGAND && !brigandUnlocked) {

//            unlock1.visible = true;
//            unlock2.visible = false;
//            unlock3.visible = false;
//            btnLoad.visible = false;
//            btnNewGame.visible = false;

//        } else if (cl == HeroClass.SCHOLAR && !scholarUnlocked) {

//            unlock1.visible = false;
//            unlock2.visible = true;
//            unlock3.visible = false;
//            btnLoad.visible = false;
//            btnNewGame.visible = false;

//        } else if (cl == HeroClass.ACOLYTE && !acolyteUnlocked) {

//            unlock1.visible = false;
//            unlock2.visible = false;
//            unlock3.visible = true;
//            btnLoad.visible = false;
//            btnNewGame.visible = false;
//
//        } else {
		
//			unlock1.visible = false;
//			unlock2.visible = false;
//			unlock3.visible = false;

			GamesInProgress.Info info = GamesInProgress.check( curClass );
			if (info != null) {
				
				btnLoad.visible = true;
				btnLoad.secondary( Utils.format( TXT_DPTH_LVL, info.depth, info.level ), info.challenges );
				
				btnNewGame.visible = true;
				btnNewGame.secondary( TXT_ERASE, false );
				
				float w = (Camera.main.width - GAP) / 2 - buttonX;
				
				btnLoad.setRect(
					buttonX, buttonY, w, BUTTON_HEIGHT );
				btnNewGame.setRect(
					btnLoad.right() + GAP, buttonY, w, BUTTON_HEIGHT );
				
			} else {
				btnLoad.visible = false;
				
				btnNewGame.visible = true;
				btnNewGame.secondary( null, false );
				btnNewGame.setRect( buttonX, buttonY, Camera.main.width - buttonX * 2, BUTTON_HEIGHT );
			}
			
//		}
	}

    private void askDifficulty() {

        StartScene.this.add( new WndDifficulty() );
    }
	
	private void startNewGame() {

		Dungeon.hero = null;
		InterlevelScene.mode = InterlevelScene.Mode.DESCEND;
		
		if (YetAnotherPixelDungeon.intro()) {
			YetAnotherPixelDungeon.intro(false);
			Game.switchScene( IntroScene.class );
		} else {
			Game.switchScene( InterlevelScene.class );
		}	
	}
	
	@Override
	protected void onBackPressed() {
		YetAnotherPixelDungeon.switchNoFade(TitleScene.class);
	}
	
	private static class GameButton extends RedButton {
		
		private static final int SECONDARY_COLOR_N	= 0xCACFC2;
		private static final int SECONDARY_COLOR_H	= 0xFFFF88;
		
		private RenderedText secondary;
		
		public GameButton( String primary ) {
			super( primary );
			
			this.secondary.text(null );
		}
		
		@Override
		protected void createChildren() {
			super.createChildren();
			
			secondary = renderText( 6 );
			add( secondary );
		}
		
		@Override
		protected void layout() {
			super.layout();
			
			if (secondary.text().length() > 0) {
				text.y = align( y + (height - text.height() - secondary.baseLine()) / 2 );
				
				secondary.x = align( x + (width - secondary.width()) / 2 );
				secondary.y = align( text.y + text.height() ); 
			} else {
				text.y = align( y + (height - text.baseLine()) / 2 );
			}
		}
		
		public void secondary( String text, boolean highlighted ) {
			secondary.text( text );
			align(secondary);
			
			secondary.hardlight( highlighted ? SECONDARY_COLOR_H : SECONDARY_COLOR_N );
		}
	}
	
	private class ClassShield extends Button {
		
		private static final float MIN_BRIGHTNESS	= 0.6f;
		
		private static final int BASIC_NORMAL		= 0x444444;
		private static final int BASIC_HIGHLIGHTED	= 0xCACFC2;
		
		private static final int MASTERY_NORMAL		= 0x666644;
		private static final int MASTERY_HIGHLIGHTED= 0xFFFF88;
		
		private static final int WIDTH	= 24;
		private static final int HEIGHT	= 28;
		private static final int SCALE	= 2;
		
		private HeroClass cl;
		
		private Image avatar;
		private RenderedText name;
		private Emitter emitter;
		
		private float brightness;
		
		private int normal;
		private int highlighted;
		
		public ClassShield( HeroClass cl ) {
			super();
		
			this.cl = cl;
			
			avatar.frame( cl.ordinal() * WIDTH, 0, WIDTH, HEIGHT );
			avatar.scale.set( SCALE );
			
			if (Badges.isUnlocked( cl.victoryBadge() )) {
				normal = MASTERY_NORMAL;
				highlighted = MASTERY_HIGHLIGHTED;
			} else {
				normal = BASIC_NORMAL;
				highlighted = BASIC_HIGHLIGHTED;
			}
			
			name.text( cl.hname() );
			align(name);
			name.hardlight( normal );
			
			brightness = MIN_BRIGHTNESS;
			updateBrightness();
		}
		
		@Override
		protected void createChildren() {
			
			super.createChildren();
			
			avatar = new Image( Assets.AVATARS );
			add( avatar );
			
			name = PixelScene.renderText( 9 );
			add( name );
			
			emitter = new Emitter();
			add( emitter );
		}
		
		@Override
		protected void layout() {
			
			super.layout();
			
			avatar.x = align( x + (width - avatar.width()) / 2 );
			avatar.y = align( y + (height - avatar.height() - name.height()) / 2 );
			
			name.x = align( x + (width - name.width()) / 2 );
			name.y = avatar.y + avatar.height() + SCALE;
			
			emitter.pos( avatar.x, avatar.y, avatar.width(), avatar.height() );
		}
		
		@Override
		protected void onTouchDown() {
			
			emitter.revive();
			emitter.start( Speck.factory( Speck.LIGHT ), 0.05f, 7 );
			
			Sample.INSTANCE.play( Assets.SND_CLICK, 1, 1, 1.2f );
			updateClass( cl );
		}
		
		@Override
		public void update() {
			super.update();
			
			if (brightness < 1.0f && brightness > MIN_BRIGHTNESS) {
				if ((brightness -= Game.elapsed) <= MIN_BRIGHTNESS) {
					brightness = MIN_BRIGHTNESS;
				}
				updateBrightness();
			}
		}
		
		public void highlight( boolean value ) {
			if (value) {
				brightness = 1.0f;
				name.hardlight( highlighted );
			} else {
				brightness = 0.999f;
				name.hardlight( normal );
			}

			updateBrightness();
		}
		
		private void updateBrightness() {
			avatar.gm = avatar.bm = avatar.rm = avatar.am = brightness;
		}
	}

//    private class ChallengeButton extends Button {
//
//        private Image image;
//
//        public ChallengeButton() {
//            super();
//
//            width = image.width;
//            height = image.height;
//
//            image.am = Badges.isUnlocked( Badges.Badge.VICTORY ) ? 1.0f : 0.5f;
//        }
//
//        @Override
//        protected void createChildren() {
//
//            super.createChildren();
//
//            image = Icons.get( YetAnotherPixelDungeon.challenges() > 0 ? Icons.CHALLENGE_ON :Icons.CHALLENGE_OFF );
//            add(image );
//        }
//
//        @Override
//        protected void layout() {
//
//            super.layout();
//
//            image.x = align( x );
//            image.y = align( y  );
//        }
//
//        @Override
//        protected void onClick() {
//            if (Badges.isUnlocked( Badges.Badge.VICTORY )) {
//                StartScene.this.add( new WndChallenges( YetAnotherPixelDungeon.challenges(), true ) {
//                    public void onBackPressed() {
//                        super.onBackPressed();
//                        image.copy( Icons.get( YetAnotherPixelDungeon.challenges() > 0 ?
//                                Icons.CHALLENGE_ON :Icons.CHALLENGE_OFF ) );
//                    };
//                } );
//            } else {
//                StartScene.this.add( new WndMessage( TXT_WIN_THE_GAME ) );
//            }
//        }
//
//        @Override
//        protected void onTouchDown() {
//            Sample.INSTANCE.play(Assets.SND_CLICK );
//        }
//    }


    private class WndDifficulty extends WndOptions {

        public WndDifficulty() {
            super( TXT_DIFF_TITLE, TXT_DIFF_ABOUT, Difficulties.NAMES );
        }


        @Override
        protected ArrayList<Integer> disabled() {

            ArrayList<Integer> disabled = new ArrayList<>();

//            if( !Badges.isUnlocked( Badges.Badge.VICTORY_1 ) ) {
//
//                disabled.add( Difficulties.HARDCORE );
//
//            }
//
//            if( !Badges.isUnlocked( Badges.Badge.VICTORY_2 ) ) {
//
//                disabled.add(Difficulties.IMPOSSIBLE);
//
//            }

            return disabled;
        }


        @Override
        protected void onSelect( int index ) {

            if( disabled.contains( index ) && index > 0 ) {

                StartScene.this.add( new WndOptions( Utils.format( TXT_DIFFICULTY, Difficulties.NAMES[ index ] ),
                        Difficulties.ABOUT[ index ], TXT_LOCKED
                ) {
                    @Override
                    protected void onSelect( int index ) {
                        StartScene.this.add( new WndDifficulty() );
                    }
                } );

            } else {

                Dungeon.difficulty = index;

                StartScene.this.add( new WndOptions( Utils.format( TXT_DIFFICULTY, Difficulties.NAMES[ index ] ),
                        Difficulties.ABOUT[ index ], TXT_YES, TXT_NO
                ) {
                    @Override
                    protected void onSelect( int index ) {
                        if (index == 0) {

                            startNewGame();

                        } else {

                            StartScene.this.add( new WndDifficulty() );

                        }
                    }
                } );
            }
        }
    }
}
