package vote;
import org.jdom.Element;
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
// Information about adaptation option, most importantly the final score
public class Option {
	
	private String descriptor; // unique descriptor
	private double score;
	private String rank;
	
	public double getScore(){
		return score;
	}
	public int compareWith(Option other) {
		// TODO Auto-generated method stub
		if(other.getScore()>getScore()) return -1;
		else{
			if(other.getScore()==getScore()) return 0;
			else return 1;
		}	
	}
	
	public void setDescriptor(String sco){
		descriptor = sco;
	}
	public String getDescriptor(){
		return descriptor;
	}
	public void setScore(double sco){
		score = sco;
	}
	public void setRank(String sco){
		rank = sco;
	}
	public String getRank(){
		return rank;
	}
}
