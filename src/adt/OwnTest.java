// Abstract Data Type
package adt ;


import java.util.*;
import java.io.*;
import java.net.*;
//import org.exolab.castor.xml.*;
//import org.apache.xml.serialize.*;



/**
 * <code>OwnTest</code> : a useful class to build a complex hierarchy and test data type and algorithms
 * @author  Maxime MORGE <A HREF="mailto:morge@emse.fr">morge@emse.fr</A> 
 * @version March 8, 2003
 * @version March 26, 2003 final version
 * * modified by Richard Taylor 2012 richard.taylor@sei-international.org
 */
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
public class OwnTest
{

  /**
   *  Constructs an empty Hierarchy, only with a goal passed to the constructor
   */
	public Hierarchy nullHierarchy (String goalString){
		
		// define the goal and use it to construct an empty hierarchy
		Criterium goal = new Criterium();
		goal.setName(goalString);
		Hierarchy h = new Hierarchy(goal);
	
		return h;
	}
	
	
	/**
	 *  Constructs a complex hierarchy  used to test.
	 */
  public Hierarchy getHierarchyExample() {
    // build a tested hierarchy H={G,A1,A2}
    Hierarchy h = new Hierarchy();
    //System.out.println("\t \t Hierarchy h:\n"+h.toString());
    
    //build and add a third  alternative A3
    Alternative alt=new Alternative();
    alt.setName("Alternative 3");
    //alt.setComment("The third alternative");
    try{
      alt.setUrl(new URL("http://messel.emse.fr/~mmorge/3alternative.html"));}
    catch(MalformedURLException e){
      System.err.println("Error : MalformedURLException"+e);
      System.exit(-1);
    }
    //System.out.println("\t \t Alternative alt:\n"+alt.toString());
    h.addAlternative(alt);
    //System.out.println("Hierarchy h + Alternative alt:\n"+h.toString());
    //build and add subcriteria C1,C2,C3,C4,C5,C7 
    Criterium c1=new Criterium();
    c1.setName("Criterium 1");
    //c1.setComment("The first criterium");
    try{
      c1.setUrl(new URL("http://messel.emse.fr/~mmorge/1criterium.html"));}
    catch(MalformedURLException e){
      System.err.println("Error : MalformedURLException"+e);
      System.exit(-1);
    }	  
    //Every criterium should contain alternatives...
    //It's easier to addSubcriterium
    //and to calculate J(a_i|c_j); J* I I* \pi
    //I()
    h.addSubcriterium(h.getGoal(),c1,h.getAlternatives(),h.getNb_alternatives());
    //h.addCriterium(c) ;
    //System.out.println("\t \t Hierarchy h + alt +c1:\n"+h.toString());
    //System.out.println("************************************************************************************************\n");
    Criterium c2=new Criterium();
    c2.setName("Criterium 2");
    //c2.setComment("The second criterium");
    try{
      c2.setUrl(new URL("http://messel.emse.fr/~mmorge/2criterium.html"));}
    catch(MalformedURLException e){
      System.err.println("Error : MalformedURLException"+e);
      System.exit(-1);
    }	  
    //Every criterium should contain alternatives...
    //It's easier to addSubcriterium
    //and to calculate J(a_i|c_j); J* I I* \pi
    //I()
    h.addSubcriterium(h.getGoal(),c2,h.getAlternatives(),h.getNb_alternatives());
    //h.addCriterium(c) ;
    //System.out.println("\t \t Hierarchy h + alt +c1+c2:\n"+h.toString());
    //System.out.println("************************************************************************************************\n");
    Criterium c3=new Criterium();
    c3.setName("Criterium 3");
    //c3.setComment("The third criterium");
    try{
      c3.setUrl(new URL("http://messel.emse.fr/~mmorge/3criterium.html"));}
    catch(MalformedURLException e){
      System.err.println("Error : MalformedURLException"+e);
      System.exit(-1);
    }	  
    //Every criterium should contain alternatives...
    //It's easier to addSubcriterium
    //and to calculate J(a_i|c_j); J* I I* \pi
    //I()
    h.addSubcriterium(c1,c3,h.getAlternatives(),h.getNb_alternatives());
    //h.addCriterium(c) ;
    //System.out.println("\t \t Hierarchy h + alt +c1+c2+c3:\n"+h.toString());
    //System.out.println("************************************************************************************************\n");
    Criterium c4=new Criterium();
    //c4.setName("Criterium 4");
    //c4.setComment("The fourth criterium");
    try{
      c4.setUrl(new URL("http://messel.emse.fr/~mmorge/4criterium.html"));}
    catch(MalformedURLException e){
      System.err.println("Error : MalformedURLException"+e);
      System.exit(-1);
    }	  
    //Every criterium should contain alternatives...
    //It's easier to addSubcriterium
    //and to calculate J(a_i|c_j); J* I I* \pi
    //I()
    h.addSubcriterium(c1,c4,h.getAlternatives(),h.getNb_alternatives());
    //h.addCriterium(c) ;
    //System.out.println("\t \t Hierarchy h + alt +c1+c2+c3+c4:\n"+h.toString());
    //System.out.println("************************************************************************************************\n");
    Criterium c5=new Criterium();
    c5.setName("Criterium 5");
    //c5.setComment("The fifth criterium");
    try{
      c5.setUrl(new URL("http://messel.emse.fr/~mmorge/5criterium.html"));}
    catch(MalformedURLException e){
      System.err.println("Error : MalformedURLException"+e);
      System.exit(-1);
    }	  
    //Every criterium should contain alternatives...
    //It's easier to addSubcriterium
    //and to calculate J(a_i|c_j); J* I I* \pi
    //I()
    h.addSubcriterium(c1,c5,h.getAlternatives(),h.getNb_alternatives());
    //h.addCriterium(c) ;
    //System.out.println("\t \t Hierarchy h + alt +c1+c2+c3+c4+c5:\n"+h.toString());
    //System.out.println("************************************************************************************************\n");
    Criterium c6=new Criterium();
    c6.setName("Criterium 6");
    //c6.setComment("The sixth criterium");
    try{
      c6.setUrl(new URL("http://messel.emse.fr/~mmorge/6criterium.html"));}
    catch(MalformedURLException e){
      System.err.println("Error : MalformedURLException"+e);
      System.exit(-1);
    }	  
    //Every criterium should contain alternatives...
    //It's easier to addSubcriterium
    //and to calculate J(a_i|c_j); J* I I* \pi
    //I()
    h.addSubcriterium(c2,c6,h.getAlternatives(),h.getNb_alternatives());
    //h.addCriterium(c) ;
    //System.out.println("\t \t Hierarchy h + alt +c1+c2+c3+c4+c5+c6:\n"+h.toString());
    //System.out.println("************************************************************************************************\n");
    Criterium c7=new Criterium();
    c7.setName("Criterium 7");
    //c7.setComment("The seventh criterium");
    try{
      c7.setUrl(new URL("http://messel.emse.fr/~mmorge/7criterium.html"));}
    catch(MalformedURLException e){
      System.err.println("Error : MalformedURLException"+e);
      System.exit(-1);
    }	  
    //Every criterium should contain alternatives...
    //It's easier to addSubcriterium
    //and to calculate J(a_i|c_j); J* I I* \pi
    //I()
    h.addSubcriterium(c2,c7,h.getAlternatives(),h.getNb_alternatives());
    //h.addCriterium(c) ;
    //System.out.println("\t \t Hierarchy h + alt +c1+c2+c3+c4+c5+c6+c7:\n"+h.toString());
    return h;

  }

/*
  public static void main(String[] argv) {
        
    // build a tested hierarchy 
    Hierarchy h = new Hierarchy();
    OwnTest ot =new OwnTest();
    h=ot.getHierarchyExample();
    //Systemout.println("\t \t Hierarchy h:\n"+h.print());

  }*/
	


}

