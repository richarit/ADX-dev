package vote;
import java.util.ArrayList;
/**
 * Copyright 2012, 2014 Richard Taylor
 * 
 * This file is part of the Adaptation Options Explorer (ADX)
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
// vote consists of a string entry for each option
public class Vote<T> extends ArrayList<T>{
	// TODO default is of the form voter-i but allow the user to enter another name
	private String nameString = "";
	
	private int len;
	// create the constructor using super to defer to ArrayList constructor
	public Vote (int i, T arg){
		super(i);
		len = i;
		init(arg);
	}
	
	// initialise (with blank strings)	
	private void init(T arg){
		for (int i=0; i<len; i++) {
	    	add(arg);
	    }
	}
	
	/**
	* Copy constructor.
	*/
	public Vote(Vote aVote, T arg) {
	    this(aVote.len, arg); // no defensive copies are created here, since 
	    //there are no mutable object fields (String is immutable)
	    // add each rank to the copied Vote
	    for (int i=0; i<len ; i++){	
	    	this.set(i, (T) aVote.get(i));
	    }
	}

	public void print(){
		for (int i=0; i<len ; i++){	
			T rank = this.get(i);
			// attempt to parse rank
	    	if (rank!="") {
	    		//int ij = Integer.parseInt((String) rank);
	    		System.out.println("option " + i + " is voted rank " + rank);
	    	}
	    }
	}
	
	public void setNameString(String ns){
		nameString = ns;
	}
	public String getNameString(){
		return nameString;
	}

}
