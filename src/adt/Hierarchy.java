// Abstract Data Type
package adt ;

//imports

import javax.swing.*;          //This is the final package name.
//import com.sun.java.swing.*; //Used by JDK 1.2 Beta 4 and all
//Swing releases before Swing 1.1 Beta 3.
import javax.swing.border.*;

import org.jdom.Document;
import org.jdom.Element;

import shell.AdaptationOptionsModel;
import shell.InputXMLModel;

import java.util.ArrayList;
import java.util.List;
import java.awt.*;

import java.awt.event.*;
import java.util.*;
import java.io.*;
import java.net.*;

import Jama.*;



/**
 * Decision <code>Hierarchy</code> class
 * @author  Maxime MORGE <A HREF="mailto:morge@emse.fr">morge@emse.fr</A> 
 * @version Fev 13, 2003
 * @version March 24, 2003  final one
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
public class Hierarchy implements Cloneable,Serializable{


  // ATTRBUTS
  private Criterium goal;
  private Vector <Alternative> alternatives ;// these are ordered in the end
  private double a[];
  private int nb_alternatives ;
  
  private AdaptationOptionsModel aoModel;

  	HashMap hash = new HashMap();
  	/*
	mMap.put("PostgreSQL", "Free Open Source Enterprise Database");
	mMap.put("DB2", "Enterprise Database , It's expensive");
	mMap.put("Oracle", "Enterprise Database , It's expensive");
	mMap.put("MySQL", "Free Open SourceDatabase");

	Iterator iter = mMap.entrySet().iterator();

	while (iter.hasNext()) {
		Map.Entry mEntry = (Map.Entry) iter.next();
		System.out.println(mEntry.getKey() + " : " + mEntry.getValue());
	}

  /**
   * Gets the value of goal
   *
   * @return the value of goal
   */
  public Criterium getGoal() {
    return this.goal;
  }

  /**
   * Sets the value of goal
   *
   * @param argGoal Value to assign to this.goal
   */
  public void setGoal(Criterium argGoal){
    this.goal = argGoal;
  }

  /**
   * Gets the value of alternatives
   *
   * @return the value of alternatives
   */
  public Vector<Alternative> getAlternatives() {
    return this.alternatives;
  }

  /**
   * Sets the value of alternatives
   *
   * @param argAlternatives Value to assign to this.alternatives
   */
  public void setAlternatives(Vector<Alternative> argAlternatives){
    this.alternatives = argAlternatives;
  }


  /**
   * Gets the value of nb_alternatives
   *
   * @return the value of nb_alternatives
   */
  public int getNb_alternatives() {
    return this.nb_alternatives;
  }
  
  /**
   * Sets the value of nb_alternatives
   *
   * @param argNb_alternatives Value to assign to this.nb_alternatives
   */
  public void setNb_alternatives(int argNb_alternatives){
    this.nb_alternatives = argNb_alternatives;
  }
  
  // construct a heirarchy with a specified goal only (no C or A)
  public Hierarchy(Criterium goal){
	  setGoal(goal);
	  alternatives =new Vector<Alternative>();
	  setNb_alternatives(0);
	  goal.setLl(true);
	    goal.setGoal(true);
	    goal.setFather(null);
	    goal.setSons(alternatives);
	    goal.setNb_sons(nb_alternatives);
	    //insert PairwiseComparisonMatrix in Criterium
	    goal.setP(new PairwiseComparisonMatrix(alternatives.size()));
  }


  /**
   * Creates a new  <code>Hierarchy</code> instance.
   * a minimal hierarchy is composed by 1 goal : "My goal" and 2 alternatives : "Alternative 1" and "Alternative 2"
   */
  public Hierarchy(){

    //Specification of the alternatives
    Alternative alt1 =new Alternative();
    Alternative alt2 =new Alternative();
    alt1.setName("Alternative 1");
    alt2.setName("Alternative 2");
    try{// validation of URL
      alt1.setUrl(new URL("http://messel.emse.fr/~mmorge/JAHP/1alternative.html"));
      alt2.setUrl(new URL("http://messel.emse.fr/~mmorge/JAHP/2alternative.html"));}
    catch(MalformedURLException e){
      System.err.println("Error : MalformedURLException"+e);
      System.exit(-1);
    }
    alternatives =new Vector<Alternative>();
    alternatives.add(alt1);
    alternatives.add(alt2);
    setNb_alternatives(2);
    //Specification of the goal
    goal =new Criterium();
    goal.setName("My goal");
    try{
      goal.setUrl(new URL("http://messel.emse.fr/~mmorge/JAHP/goal.html"));}
    catch(MalformedURLException e){
      System.err.println("Error : MalformedURLException"+e);
      System.exit(-1);
    }

    goal.setLl(true);
    goal.setGoal(true);
    goal.setFather(null);
    goal.setSons(alternatives);
    goal.setNb_sons(nb_alternatives);
    //insert PairwiseComparisonMatrix in Criterium
    goal.setP(new PairwiseComparisonMatrix(alternatives.size()));

  }



  /**
   * <code>print</code> Returns a string representation of this Hierarchy, containing the String representation of each elements to debug.
   *
   * @return a <code>String</code> value
   */
  public String print(){
    String s=new String();
    s="Nb of alternatives          : "+nb_alternatives+"\n";
    s=s+"Alternatives of hierarchy : \n" ;
    Vector<Alternative> v=new Vector<Alternative>();
    v=getAlternatives();
    for(int i=0;i<getNb_alternatives();i++){
      s=s+"Alternative "+i+"\n";
      Alternative alt =(Alternative) v.get(i);
      alt.print();
    }
    s=s+"Goal of the hierarchy       : \n"+getGoal().print();
    return s;
  }


  /**
   * <code>toString</code> Returns a short string representation of this Hierarchy
   *
   * @return a <code>String</code> value
   */
  public String toString(){
    String s=new String();
    s="Goal of the hierarchy       : \n"+getGoal().toString();
    s=s+"Nb of alternatives          : "+nb_alternatives+"\n";
    return s;
  }


  /**
   * <code>addAlternative</code> method here.
   *
   * @param alt <code>Alternative</code> value
   * @exception IllegalArgumentException "Out of bounded..."
   * @return int index of the alternatives 
   */
  public int indexAlt(Alternative alt){
    int index=0;
    if (isNew(alt)) throw new IllegalArgumentException("out of bounded alternatives");
    for(int i=0;i<nb_alternatives;i++){
      Alternative current_alt=(Alternative) alternatives.get(i);
      if (current_alt.equals(alt)){
	index=i;
	return index;
      }
    }
    return -1;
  }



  /**
   * <code>addAlternative</code> method here.
   *
   * @param alt <code>Alternative</code> value
   * @exception IllegalArgumentException "Out of bounded"
   */
  public void addAlternative(Alternative alt){
    if (!isNew(alt)) throw new IllegalArgumentException("An alternative with the same name is already in this hierarchy");
    nb_alternatives++;
    alternatives.add(alt);
    goal.addAlternative(alt,this);
  }

  /**
   * <code>addSubcriterium</code> method here.
   *
   * @param Criterium c which should be the father
   * @param Criterium subc which should be added
   * @param Vector alternatives
   * @param int nb_alternatives
   * @exception IllegalArgumentException
   */
  public void addSubcriterium(Criterium c,Criterium subc,Vector alternatives,int nb_alternatives){

    if (!goal.isNew(subc)) throw new IllegalArgumentException("A criterium with the same name is always in this hierarchy");
    subc.setFather(c);
    subc.setGoal(false);
    subc.setLl(true);
    subc.setSons((Vector<Alternative>) alternatives.clone());
    subc.setNb_sons(nb_alternatives);
    subc.setP(new PairwiseComparisonMatrix(nb_alternatives));
    goal.addSubcriterium(c,subc);
  }


  /**
   * <code>isNew</code> method here.
   *
   * @param Alternatives alt
   * @return boolean
   */
  public boolean isNew(Alternative alt){
    for(int i=0;i<nb_alternatives;i++){
      Alternative current_alt=(Alternative) alternatives.get(i);
      if (current_alt.equals(alt)) return false;
    }
    return true;
  }

  //************************************
  //
  //Method to calculate solutions
  // 
  // 
  //************************************* 
  
  /**
   * <code>best_alternative</code> method here.
   *
   * @return an int : the index of the best alternative 
   */
  public int bestAlternative(){
    int index=0;
    double value=0.0;
    for(int i=0;i<nb_alternatives;i++){
      if (Pi(i)>value){
	index=i;
	value=Pi(i);
      }
  }
    return index;
  }
  
  public void SortAlternatives(){
	  	a = new double [nb_alternatives];
	  	int i, j;
	  	 
	  	double temp;
	  	Alternative talt = new Alternative();
	  	for(int b=0;b<nb_alternatives;b++)a[b]=Pi(b);
	  	// check start values
	  	for(i=0;i<nb_alternatives;i++) {
	    	System.out.println(a[i]);
	    	System.out.println(alternatives.get(i).getName());
	    }
	  	
	    for(i=0;i<nb_alternatives-1;i++)
	    {
	    	for(j=i+1;j<nb_alternatives;j++)
	    	{
	    		if(a[i]>a[j])
	    		{
	    			temp=a[i];
	    			a[i]=a[j];
	    			a[j]=temp;
	    			// reorganise vector
	    			talt=alternatives.get(i);
	    			alternatives.set(i, alternatives.get(j));
	    			alternatives.set(j, talt);
	    		}
	    	}
	    }
	 // check end values
	    for(i=0;i<nb_alternatives;i++) {
	    	System.out.println(a[i]);
	    	System.out.println(alternatives.get(i).getName());
	    }
	    
  }



  /**
   * <code>Pi</code> method here.
   * @param i :  the index of the alternative
   * @return double  : the value of the alternative according to the hierarchy
   */
  public double Pi(int i){
    return goal.Jstar(i);
  }

  /**
   * <code>V</code> method here.
   * @param Criterium c according to the hierarchy is evaluated
   * @return double  : the value of the criterium according to the hierarchy
   * @return IllegalArgumentException
   */
  public double V(Criterium c){
    if (goal.equals(c)) throw new IllegalArgumentException("the value of the criterium according to the hierarchy can not be calculated");
    return(goal.I(c));
  }


  //************************************
  //
  //Method to modify hierarchy
  // 
  // 
  //************************************* 

  /**
   * <code>delAlternative</code> method here.
   * @param Alternative :  the alternative which should be deleted
   * @exception IllegalArgumentException
   */
  public void delAlternative(Alternative alt){
    Vector<Alternative> old_alts=(Vector<Alternative>)alternatives.clone();
    int old_nb_alts=nb_alternatives;
    Vector<Alternative> new_alts=new Vector();
    int index=0;
    if (nb_alternatives==1) throw new IllegalArgumentException("alternative can not be deleted : a hierarchy must have even a alternative");
    for(int i=0;i<nb_alternatives;i++){
      Alternative current_alt=(Alternative) old_alts.get(i);
      if (current_alt.equals(alt)){
	index=i;
      }
      else new_alts.add(current_alt);
    }
    setAlternatives(new_alts);
    nb_alternatives--;
    goal.delAlternative(index);
  }

  /**
   * <code>delCriterium</code> method here.
   * @param Criterium :  the criterium which should be deleted
   * @exception IllegalArgumentException
   */
  public void delCriterium(Criterium c){
    goal.delCriterium(c,this);
  }
  
	// this is called when all the exercise has been completed
   // these are all done in special methods in the ResultsFrame object votingResults 
   // but should possibly be moved here (?)
	public void computeAHP(){
		// iterate through alternatives in the subset
		// extract the values for the final score (and the rank)
		
		aoModel.addColumn("score", 1.0);		
		aoModel.addColumn("rank", 1.0);
		
		List <Option> options = new ArrayList<Option> (0); // used in computing rank
		List <Element> all_options = aoModel.getAll_options();
		// if the name in h matches to those in the AO
		for (Element ao: all_options){
			int oID = all_options.indexOf(ao);
			String dtext = ao.getChildText("descriptor");
			
			for (Alternative a: alternatives){
				int aID = alternatives.indexOf(a);
				// whitespace can mess up string comparison
				// add html tags and trim whitespace before testing
				if (new String(a.getName()).equals(new String("<HTML>" + dtext.trim() + "</HTML>"))){	
				//if (a.getName().equals(new String ("<HTML>" + dtext + "</HTML>"))){
					// add the new data
					//System.out.println("Found match: " + dtext);
					// put the rank and the value in the correct columns
					// Pi causing error
					aoModel.setValueAt(Pi(aID), oID, aoModel.findColumn("score"));
					//System.out.println("set value: " + Pi(aID) + ", at row: " + oID);
				
					// build options list (used for ranking)
					Option option = new Option();
					option.setDescriptor(dtext);
					option.setScore((Pi(aID)));
					// now put into the correct position in the arraylist
					int this_pos = 0;
					for (Option other: options){
						int oth_pos = options.indexOf(other);
						if (option.compareWith(other)==-1)this_pos++;
						// -1 signifies that other has a higher score, therefore increase this position
					}
					options.add(this_pos, option);
				}
			}
		}
		
		
		
		// calculate the rank based on the score
		// compare options and determine the winners
		// iterate through list and attribute ranks, allowing for joint placings
		Option this_option;
		Option last_option= new Option();
		last_option.setScore(0);
		for (int i=0; i<aoModel.getNumOptions();i++){
			Element aoe = aoModel.getAll_options().get(i);
			// in the case it does not have a score, it does not have a rank
			if ("".equals(aoe.getChildText("score"))){
				aoModel.setValueAt("n.a.", i, aoModel.findColumn("rank"));
				
			}
			else
			{
				for (int j=0; j<options.size();j++){
					this_option = options.get(j);
					// if there is a value and ...
				
					int int_rank = j+1;
					String str_rank = new String("" + int_rank);
					if(j!=0){
					//if(j!=0 && last_option.getScore()!=0.0){
						//System.out.println("Last option, score = " + last_option.getScore());
						
						if(this_option.compareWith(last_option)==0) str_rank = last_option.getRank();
					}
					// TEST
					//System.out.println("This option, score = " + this_option.getScore());
					if (this_option.getScore()==0.0) str_rank = "n.a.";
					this_option.setRank(str_rank);
					last_option= this_option;
					
					
				}
			}
		}
		
		// finally set the text in the AO model
		for (int i=0; i<aoModel.getNumOptions();i++){
			//this_option = options.get(i);
			Element te = aoModel.getAll_options().get(i);
			// identify the correct option according to current row by its descriptor
			String desc = te.getChildText("descriptor");			
			for (Option option: options){
				if(option.getDescriptor()==desc) {
					this_option = option;
					aoModel.setValueAt(this_option.getRank(), i, aoModel.findColumn("rank"));
				}
		
			}
		}
		printRanks(options);
	}
  
	public void printRanks(List<Option> opts){
		for(Option opt:opts){
			System.out.println(opt.getDescriptor() + " " + opt.getScore() + " " +  opt.getRank());
		}
	}
	
	public AdaptationOptionsModel getAdaptationOptionsModel() {
		  return aoModel;
	}
	
	// this is not a setter or getter but more a creation method
	//public void setAdaptationOptionsModel(Document doc){
		// Model extends AbstractTableModel. It is passed the dom object and extracts the table data from that
			
	//	aoModel = new AdaptationOptionsModel(doc);
	//}
	public void setAdaptationOptionsModel(InputXMLModel ixm) {
	 aoModel = new AdaptationOptionsModel(ixm.getDocument());
	 aoModel.purgeVotingColumns();
	 
		  
    }

  /////////////////////
  //
  // Methods to load and save a hierarchy
  // NOT Yet Implemented
  /////////////////////


}


  

