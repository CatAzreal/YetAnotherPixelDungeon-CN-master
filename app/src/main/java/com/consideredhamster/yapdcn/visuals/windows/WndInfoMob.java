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
package com.consideredhamster.yapdcn.visuals.windows;

import com.consideredhamster.yapdcn.Dungeon;
import com.consideredhamster.yapdcn.Element;
import com.consideredhamster.yapdcn.misc.utils.GLog;
import com.watabou.noosa.BitmapText;
import com.watabou.noosa.RenderedText;
import com.watabou.noosa.ui.Component;
import com.consideredhamster.yapdcn.actors.mobs.Bestiary;
import com.consideredhamster.yapdcn.actors.mobs.Mob;
import com.consideredhamster.yapdcn.scenes.PixelScene;
import com.consideredhamster.yapdcn.visuals.sprites.CharSprite;
import com.consideredhamster.yapdcn.visuals.ui.BuffIndicator;
import com.consideredhamster.yapdcn.visuals.ui.HealthBar;
import com.consideredhamster.yapdcn.misc.utils.Utils;

import java.util.ArrayList;
import java.util.Map;

public class WndInfoMob extends WndTitledMessage {

	public WndInfoMob( Mob mob ) {
		
		super( new MobTitle( mob ), desc( mob ) );
		
	}
	
	private static String desc( Mob mob ) {
		
		StringBuilder builder = new StringBuilder( ( mob.friendly || mob.hostile ? "\n\n" + stats( mob ) : "" ) );
		
		builder.append( "\n" + mob.description() );
		builder.append( "\n\n" + ( "这个" ) + mob.name + "正在" + mob.state.status() + "。" );

		return builder.toString();
	}

	private static String stats( Mob mob ) {

		StringBuilder stats = new StringBuilder();

		int mobAccuracy = mob.accuracy() * 2;
		int hitChance = ( mobAccuracy * 100 / ( mobAccuracy + Dungeon.hero.dexterity() ) );

		int heroAccuracy = Dungeon.hero.accuracy() * 2;
		int dodgeChance = 100 - ( heroAccuracy * 100 / ( heroAccuracy + mob.dexterity() ) );

		stats.append( "生命值: _" + mob.HP + "/" + mob.HT + " HP (护甲等级：" + mob.armorClass() + ")_\n" );
		stats.append( "基础伤害: _" + mob.minDamage() + "-" + mob.maxDamage() + " (平均值" +
				( ( mob.minDamage() + mob.maxDamage() ) / 2 ) + ")_\n" );

//        stats.append( "\n" );

		stats.append( "命中: _" + mob.accuracy() + " (命中率"+ hitChance +"%)_\n" );
		stats.append( "敏捷: _" + mob.dexterity() + " (闪避率"+ dodgeChance +"%)_\n" );

//        stats.append( "\n" );

//        stats.append( "Att speed: _" + (int)(attackSpeed() * 100) + "%_\n" );
//        stats.append( "Mov speed: _" + (int)(attackSpeed() * 100) + "%_\n" );
//
//        stats.append( "\n" );

		ArrayList<String> immunity = new ArrayList<>();
		ArrayList<String> resistant = new ArrayList<>();
		ArrayList<String> vulnerable = new ArrayList<>();

		for( Map.Entry<Class<? extends Element>, Float> entry : mob.resistances().entrySet() ) {
			if( entry.getValue() == Element.Resist.IMMUNE ) {
				immunity.add( entry.getKey().getSimpleName() );
			} else if( entry.getValue() == Element.Resist.PARTIAL ) {
				resistant.add( entry.getKey().getSimpleName() );
			} else if( entry.getValue() == Element.Resist.VULNERABLE ) {
				vulnerable.add( entry.getKey().getSimpleName() );
			}
		}

		if( !immunity.isEmpty() ){

			StringBuilder imm = new StringBuilder(  );
			imm.append( buffTranslation(immunity.remove(0)));

			for( String s : immunity ) {
				imm.append( ", ");
				imm.append( buffTranslation(s) );
			}

			stats.append( "免疫: _" + imm + "_\n" );
		}
		if( !resistant.isEmpty() ){

			StringBuilder res = new StringBuilder(  );
			res.append( buffTranslation(resistant.remove(0)));

			for( String s : resistant ) {
				res.append( ", ");
				res.append( buffTranslation(s) );
			}

			stats.append( "抗性: _" + res + "_\n" );
		}

		if( !vulnerable.isEmpty() ){

			StringBuilder vul = new StringBuilder(  );
			vul.append( buffTranslation(vulnerable.remove(0)));

			for( String s : vulnerable ) {
				vul.append( ", ");
				vul.append( buffTranslation(s) );
			}

			stats.append( "弱点: _" + vul.toString() + "_\n" );
		}

		stats.append( "特殊信息: _" + mob.info + "_\n" );

		return stats.toString();
	}

	private static String buffTranslation(String className){
		String insensitiveString = className.toLowerCase();
		insensitiveString = insensitiveString.replaceAll("\\s+","");
		switch (insensitiveString){
			case "physical":
				return "物理";
			case "explosion":
				return "爆炸";
			case "knockback":
				return "击退";
			case "ensnaring":
				return "束缚";
			case "flame":
				return "火焰";
			case "acid":
				return "腐蚀";
			case "shock":
				return "电击";
			case "mind":
				return "精神";
			case "body":
				return "体格";
			case "dispel":
				return "驱散";
			case "frost":
				return "冰霜";
			case "energy":
				return "能量";
			case "unholy":
				return "邪恶";
			case "doom":
				return "毁灭";
		}
		return className;
	}

	private static class MobTitle extends Component {
		
		private static final int GAP	= 2;
		
		private CharSprite image;
		private RenderedText name;
		private HealthBar health;
		private BuffIndicator buffs;

		public MobTitle( Mob mob ) {

			name = PixelScene.renderText( Utils.capitalize( mob.name ), 9 );
			name.hardlight( TITLE_COLOR );
			PixelScene.align(name);
			add( name );
			
			image = mob.sprite();
			add( image );
			
			health = new HealthBar();
			health.level( (float)mob.HP / mob.HT );
			add( health );
			
			buffs = new BuffIndicator( mob );
			add( buffs );
		}
		
		@Override
		protected void layout() {
			
			image.x = 0;
			image.y = Math.max( 0, name.height() + GAP + health.height() - image.height );
			
			name.x = image.width + GAP;
			name.y = image.height - health.height() - GAP - name.baseLine();
			
			float w = width - image.width - GAP;
			
			health.setRect( image.width + GAP, image.height - health.height(), w, health.height() ); 
			
			buffs.setPos( 
				name.x + name.width() + GAP, 
				name.y + name.baseLine() - BuffIndicator.SIZE );

			height = health.bottom();
		}
	}
}
