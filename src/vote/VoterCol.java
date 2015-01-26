package vote;
import java.awt.Color;

/**
 * Copyright 2012 Richard Taylor
 * 
 * This file is part of the Adaptation Decision Explorer (ADx)
 *
 *    ADx is free software: you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation, either version 3 of the License, or
 *    (at your option) any later version.
 *
 *   ADx is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 *
 *    You should have received a copy of the GNU General Public License
 *    along with ADx.  If not, see <http://www.gnu.org/licenses/>.
 **/
// enum type for colours
//A public enumeration must be in a file of its own with filename matching the class name
public enum VoterCol { 
		
		RED (Color.RED),
		GREY (Color.LIGHT_GRAY),
		BLUE (Color.CYAN);
		
		private Color col;
		
		VoterCol(Color c){
			this.col= c;
		}
		public Color getCol()   { return col; }
		
		public Color picker(int i){
			int len = values().length;
			int number = (int) Math.floor (i/ len);
			return Color.cyan;
			//if(number == VoterCol.valueOf("RED").hashCode()) return Color.RED;
			//else return Color.cyan;
		}

	}
