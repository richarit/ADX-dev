package vote;
import java.io.IOException;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.lang.Double;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

//import adt.Alternative;
import adt.Criterium;
import adt.PairwiseComparisonMatrix;

import shell.AdaptationOptionsModel;
import shell.InputXMLModel;
import shell.ResultsFrame;
import vote.Vote;
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
 *    along with ADX.  If not, see <http://www.gnu.org/licenses/>.
 **/

// this object is serializable so that it can be saved and loaded as objectstream
// replaces the arraylist
public class Poll implements Cloneable,Serializable {
	private Vector <Vote> votes ;
	private int nb_votes;
	private int nb_options;
	private int nb_ranks; // parameter for the poll (and for all votes) is the number of options that can be voted for
	// points awarded for each vote cast could be 2 points for 1st place ranking, 1 point for a second place
	// Long awards []= {2,1};
	// or it could follow the number of votes entered at the beginning of vote 1
	public Long [] awards;
	
	private AdaptationOptionsModel aoModel;
	
	public void addVote(Vote vote){
	    // if (!isNew(vote)) throw new IllegalArgumentException("An alternative with the same name is already in this hierarchy");
	    nb_votes++;
	    votes.add(vote);
	    // goal.addAlternative(alt,this);
	  }
	
	public void removeVote(Vote vote){
		votes.remove(vote);
		nb_votes--;
	}
	/// no arg constructor
	public Poll(){

	    votes =new Vector<Vote>();
	    
	    setNb_votes(0);
	    
	    //insert PairwiseComparisonMatrix in Criterium
	    //goal.setP(new PairwiseComparisonMatrix(alternatives.size()));

	  }
	
	/**
	   * Gets the value of votes
	   *
	   * @return the value of votes
	   */
	  public Vector<Vote> getVotes() {
	    return this.votes;
	  }
	  
	  public Vote getVoteByNumber(int id){
		  if (id < nb_votes) return votes.get(id);
		  else {return votes.get(nb_votes-1);} 
	  }

	  /**
	   * Sets the value of votes
	   *
	   * @param argVotes Value to assign to this.votes
	   */
	  public void setVotes(Vector<Vote> argVotes){
	    this.votes = argVotes;
	  }


	  /**
	   * Gets the value of nb_votes
	   *
	   * @return the value of nb_votes
	   */
	  public int getNb_votes() {
	    return this.nb_votes;
	  }
	  
	  /**
	   * Sets the value of nb_votes
	   *
	   * @param argNb_votes Value to assign to this.nb_votes
	   */
	  public void setNb_votes(int argNb_votes){
	    this.nb_votes = argNb_votes;
	  }
	  
	  
	  public void setNb_ranks(int argNb_ranks){
		  this.nb_ranks = argNb_ranks;
	  }
	  
	  public int getNb_ranks() {
		    return this.nb_ranks;
	  }
	  
	  /**
	   * <code>isNew</code> method here.
	   *
	   * @param Alternatives alt
	   * @return boolean
	   */
	  public boolean isNew(Vote alt){
	    for(int i=0;i<nb_votes;i++){
	      Vote current_alt=(Vote) votes.get(i);
	      if (current_alt.equals(alt)) return false;
	    }
	    return true;
	  }
	  
	 
	  public void setAdaptationOptionsModel(InputXMLModel ixm) {
		  aoModel = new AdaptationOptionsModel(ixm.getDocument());
		  
	  }
	
	  
	  public AdaptationOptionsModel getAdaptationOptionsModel() {
		  return aoModel;
	  }
	  
	  

	  // calculate result of poll ...
      // these are all done in special methods in the ResultsFrame object votingResults 
	  // but should possibly be moved here (?)
	  // this is called when all the votes are in and the award mechanism has been determined
	
	  // this is called when all the votes are in and the award mechanism has been determined
	  public void computeVoting(){
			
		  	aoModel.addColumn("score", 1.0);		
			aoModel.addColumn("rank", 1.0);
			
			aoModel.addColumn("voter", 2.0);
			aoModel.addColumn("points", 1.0);
			// initially computes this with all of the default settings
			// configurable elements could be:
			// -- weightings of votes according to number of people present
			// -- awards
			List <Element> all_options = aoModel.getAll_options();
			// Iterate through all votes, calculate points they contribute
			// Record total points and the names of voters in the AO model
			for (Vote<String> v: votes){
			
				for (int i=0;i<v.size();i++){
					String vote_elt = v.get(i);
					if(vote_elt!=""){
						int rank = Integer.parseInt(vote_elt) - 1;	// rank must take the values 0,1,2 as array arg.
					// obtain the existing value from the model
						Element ao = all_options.get(i);
						String oldtext = ao.getChildText("points");
					
						Long new_i = new Long(0);
						if(oldtext!="")
							try {
								new_i = Long.parseLong(oldtext) + awards[rank].longValue();
							} catch(NumberFormatException ex){}
						else new_i = awards[rank].longValue();
						//System.out.println("rank is " + rank + ", awards " + re.awards[rank]);
						String newtext = new_i.toString();
						aoModel.setValueAt(newtext, i, aoModel.findColumn("points"));
						
						// add to the AO model in the "voter" column the name of current voter
						String votertext = ao.getChildText("voter");
						String newvotertext = new String();			
						if(votertext!="")
							newvotertext = new String(votertext + ", " + v.getNameString());
						else newvotertext = v.getNameString();
						
						aoModel.setValueAt(newvotertext, i, aoModel.findColumn("voter"));
					}
				}
			}
			
			//System.out.println("recorded total points");
			// compute the score of each option according to scoring scheme
			// e.g. total points divided by total number of voters
			for (int i=0; i<aoModel.getNumOptions();i++){
				// obtain the value of points from the model
				Element ao = all_options.get(i);
				String pointstext = ao.getChildText("points");
				Double pod = new Double(0);
				if(pointstext!="")
					try {
						pod = Double.parseDouble(pointstext);
					} catch(NumberFormatException ex){}
					
				else pod = new Double(0.0);
				Double score = pod / getNb_votes();
				//Double score = pod / re.getPoll().size();
				// set the Double value
				aoModel.setValueAt(score, i, aoModel.findColumn("score"));
			}
			
			List <Option> options = new ArrayList<Option> (0);
			// calculate the rank based on the score
			// based on the above computation can compare options and determine the winners
			for (int i=0; i<aoModel.getNumOptions();i++){
				Option option = new Option();
				Element ao = all_options.get(i);
				String descriptortext = ao.getChildText("descriptor");
				String scoretext = ao.getChildText("score");
				option.setDescriptor(descriptortext);
				option.setScore(Double.parseDouble(scoretext));
				// now put into the correct position in the arraylist
				int this_pos = 0;
				for (Option other: options){
					int oth_pos = options.indexOf(other);
					if (option.compareWith(other)==-1)this_pos++;
					// -1 signifies that other has a higher score, therefore increase this position
				}
				options.add(this_pos, option);
			}
			
			// iterate through list and attribute ranks, allowing for joint placings
			Option this_option;
			Option last_option= new Option();
			last_option.setScore(0);
			for (int i=0; i<aoModel.getNumOptions();i++){
				this_option = options.get(i);
				int int_rank = i+1;
				String str_rank = new String("" + int_rank);
				if(i!=0){
					if(this_option.compareWith(last_option)==0) str_rank = last_option.getRank();
				}
				if (this_option.getScore()==0.0) str_rank = "n.a.";
				this_option.setRank(str_rank);
				last_option= this_option;
			}
			
			// finally set the text in the AO model
			for (int i=0; i<aoModel.getNumOptions();i++){
				//this_option = options.get(i);
				Element ao = all_options.get(i);
				// identify the correct option according to current row by its descriptor
				String desc = ao.getChildText("descriptor");			
				for (Option option: options)
					if(option.getDescriptor()==desc) {
						this_option = option;
						aoModel.setValueAt(this_option.getRank(), i, aoModel.findColumn("rank"));
					}
			}
			
			printRanks(options);
			
			// TODO set up the summary model based on information in the AO model
			// summaryModel = new SummaryModel();	
			// show only options that score > 0.5 in tabulate summary		
			// votingResults.setAdaptationOptionsModel(aoModel);
		}
	  // rpad(String.valueOf(opt.getScore()),20)
	  public void printRanks(List<Option> opts){
		    System.out.println(rpad("Calculation of results.", 30) + "  " + rpad("Av. Pts.",8) + "  " + "Rank");
			for(Option opt:opts){
				//System.out.println(rpad(String.valueOf(opt.getScore()),4));
				System.out.println(rpad(opt.getDescriptor(),30) + "  " + rpad(String.valueOf(opt.getScore()),8)  + "  " +  opt.getRank());
			}
		}
	  
	  public String rpad(String inStr, int finalLength){
		  return (inStr + "                                      " // typically a sufficient length eg > 30
				  ).substring(0,finalLength);
	  }
		// 1st placed option gets points equal to the number of votes entered in the combo box
		// Other ranked options get uniformly decreasing points

		public void setAwardsByCombo(){
			Long s_ranks = new Long(getNb_ranks());
			awards= new Long[s_ranks.intValue()]; 
			for (int i=0;i<s_ranks;i++){
				awards[i]= s_ranks-i;
			}
		}
		
		// 1st placed option gets points equal to the number of votes entered in the combo box
		// Other ranked options get uniformly decreasing points
		public void printResultOfVoter(Vote<String> v){
			for (int i=0;i<v.size();i++){
				
				String elt = v.get(i);
				if(elt!=""){
					int rank = Integer.parseInt(elt) - 1;   	// rank must take the values 0,1,2 as array arg.
					System.out.println ("" + v.getNameString() + " gives " + awards[rank] + " points to option " + i);
				}
				
			}
			
		}
		
		
	  
}
