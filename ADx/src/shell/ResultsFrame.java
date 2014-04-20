package shell;


import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.*;
import java.awt.GridLayout;
import java.awt.Color;
import java.beans.EventHandler;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import java.util.Map.Entry;

//package for document class and components
import org.jdom.*;
// package for saxbuilder and other document builder
import org.jdom.input.*;
import org.jdom.output.XMLOutputter;

import adt.AHPEngine;
import adt.Alternative;
import adt.Hierarchy;

import vote.Option;
import vote.RankingEngine;
import vote.Vote;




// Classes for javascript Rhino engine
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import static java.lang.System.out;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;

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
// Generic frame for results of the analysis performed by individual engines
// contains 
// is displayed after the user input to the method and before the aggregation + html option
public class ResultsFrame extends JFrame {
	Container rContent;
	RankingEngine re;
	AHPEngine ae;
	String loc;			// location (path + fname) of the file selected by the chooser
	File choice;		// and the corresponding file object
	AdaptationOptionsModel aoModel;
	Engine e;
	CombinedResultsModel crModel;
	JTable jt;
	
	// top (e.g. highest ranked) options
	List<Element> preferredOptions;
	Long npdefault = new Long(3);
	Long numPreferred;
	//boolean npentered = false;
	
	String explanation;
	JFormattedTextField np_field = new JFormattedTextField(); 	// formatted field for entering number preferred options
	JLabel np_label;
	// a message detailing the purpose of the mock engine result
	// String explanation = "A ranking exercise was carried out as part of the Tanzania NAPA study";
	
	public ResultsFrame(RankingEngine r, String str, CombinedResultsModel crm, Engine e){
		// centre on the user's screen, set size to half screen size
		// Toolkit methods interface with the native windowing system
		// Note that this will give very different dimensions for widescreen
		// It should be tested to work with 1024*768 aspect.
		Toolkit kit = Toolkit.getDefaultToolkit();
		Dimension screenSize = kit.getScreenSize();
		int screenWidth = screenSize.width;
		int screenHeight= screenSize.height;
		this.e = e;
		// same location as the options browser	
		setSize(screenWidth / 2, screenHeight / 2);
		setLocation(screenWidth * 3/8, screenHeight * 3 / 8);

		rContent = getContentPane();
		re = r;
		setNumPreferred (npdefault);
		// for mock voting engine Use the file name associated with the file chooser which should be the tanzania case
		loc = str;
		crModel = crm;
		//mockVoting();
	}
	
	public ResultsFrame(AHPEngine r, String str, CombinedResultsModel crm, Engine e){
		// centre on the user's screen, set size to half screen size
		// Toolkit methods interface with the native windowing system
		// Note that this will give very different dimensions for widescreen
		// It should be tested to work with 1024*768 aspect.
		Toolkit kit = Toolkit.getDefaultToolkit();
		Dimension screenSize = kit.getScreenSize();
		int screenWidth = screenSize.width;
		int screenHeight= screenSize.height;
		this.e = e;
		// same location as the options browser	
		setSize(screenWidth / 2, screenHeight / 2);
		setLocation(screenWidth * 3/8, screenHeight * 3 / 8);

		rContent = getContentPane();
		ae = r;
		setNumPreferred (npdefault);
		// for mock voting engine Use the file name associated with the file chooser which should be the tanzania case
		loc = str;
		crModel = crm;
		//mockVoting();
	}

	public void setExplanation(String str){
		explanation = str;
	}
	
	// a generic method that might be used by each engine - summary information
	public void tabulateSummary(){

		// Put the explanation in the top panel
		JPanel exp = new MsgPanel(explanation);
		exp.setPreferredSize(new Dimension (0,40));
		exp.setForeground(Color.BLACK);
		
		rContent.add(exp, BorderLayout.PAGE_START);
	
		jt = new JTable(aoModel);
		
		// only include the following columns: name of option, score and rank
		ArrayList <String> colNames = new ArrayList<String> ();
		colNames.add("descriptor");
		colNames.add("score");
		colNames.add("rank");
		int width = colNames.size();
	
		
		ArrayList <Double> columnLengths = new ArrayList<Double>();
		columnLengths.add(new Double(2.0));
		columnLengths.add(new Double(1.0));
		columnLengths.add(new Double(1.0));
		

		// Scrollpane is invoked with an argument that refers to the table object
		// table is automatically added to the container
		
		JScrollPane scrollPane = new JScrollPane(jt, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS );
		jt.setAutoCreateRowSorter(true);		// enable sorting and filtering of rows, e.g. by clicking on column heading
		jt.setFillsViewportHeight(true);		// table uses entire height of its container
		jt.setRowHeight(80);
		
		//scrollPane.
		
		//divide dimension of the frame by the sum of the factors
		double total_factors = 0.0;
		for(int i=0; i< width; i++) 
		total_factors += columnLengths.get(i).doubleValue();
		//double unit_length = scrollPane.getWidth()/total_factors;
		// Customise initial column widths. 
		// Each column is represented by a TableColumn object which supplies getter and setter methods. 
		TableColumn tc = null;
		for(int i=0; i<width; i++){
			tc = jt.getColumnModel().getColumn(i);
			//int int_width = (int) Math.floor(unit_length * aoModel.columnLengths[i]);
			//tc.setPreferredWidth(int_width);
			
			double db = columnLengths.get(i).doubleValue();
			//System.out.println(db);
			tc.setPreferredWidth((int)Math.round(db)*200);
			//tc.setPreferredWidth(200);
			
		}
		
		
		// TODO sort the table rows in the model class
		// Construct a TableRowSorter and set it as the sorter for the table
		TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(aoModel);
		jt.setRowSorter(sorter);
		
		// add scrollPane rather than panel. This is the main content of the central panel
		rContent.add(scrollPane, BorderLayout.CENTER);
		
		// Bottom panel contains an "Ok" button with an action listener
		JPanel okPanel = new JPanel();
		JButton ok = new JButton("Ok");
		ok.setToolTipText("Exit to the next engine or combined results screen");
		JButton print = new JButton("Print");
		print.setToolTipText("Generate pdf of this engine's results");
		// use the EventHandler class to automatically create listeners and install the handler
		// last argument determines the method that will be called if an event is heard by the listener
		// "setPreferredOptions" which records the favoured options to be made available to aggregator
		ok.addActionListener((ActionListener)EventHandler.create(ActionListener.class, this, "setPreferredOptions"));
		print.addActionListener((ActionListener)EventHandler.create(ActionListener.class, this, "print"));
		
		// add summary and details buttons with their listeners
		//JButton summary = new JButton("Summary Info");
		//JButton details = new JButton("Detailed Info");
		
		//okPanel.add(summary);
		//okPanel.add(details);
		okPanel.add(print);
		okPanel.add(ok);
		
		//summary.addActionListener((ActionListener)EventHandler.create(ActionListener.class, this, "tabulateSummary"));
		//details.addActionListener((ActionListener)EventHandler.create(ActionListener.class, this, "tabulateDetails"));
		
		rContent.add(okPanel, BorderLayout.PAGE_END);
		validate();
	
	}
	
	// a generic method that might be used by each engine
	public void tabulateDetails(){

		// Put the explanation in the top panel
		JPanel exp = new MsgPanel(explanation);
		exp.setPreferredSize(new Dimension (0,40));
		exp.setForeground(Color.BLACK);
		
		
		rContent.add(exp, BorderLayout.PAGE_START);
		
		
		jt = new JTable(aoModel);
		
		int width = jt.getColumnCount();

		// Scrollpane is invoked with an argument that refers to the table object
		// table is automatically added to the container
		
		JScrollPane scrollPane = new JScrollPane(jt, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS );
		jt.setAutoCreateRowSorter(true);		// enable sorting and filtering of rows, e.g. by clicking on column heading
		jt.setFillsViewportHeight(true);		// table uses entire height of its container
		jt.setRowHeight(80);
		
		//scrollPane.
		// TODO setting column widths not working
		//divide dimension of the frame by the sum of the factors
		double total_factors = 0.0;
		for(int i=0; i< aoModel.getColumnCount(); i++) 
		total_factors += aoModel.columnLengths.get(i).doubleValue();
		//double unit_length = scrollPane.getWidth()/total_factors;
		// Customise initial column widths. 
		// Each column is represented by a TableColumn object which supplies getter and setter methods. 
		TableColumn tc = null;
		for(int i=0; i<width; i++){
			tc = jt.getColumnModel().getColumn(i);
			//int int_width = (int) Math.floor(unit_length * aoModel.columnLengths[i]);
			//tc.setPreferredWidth(int_width);
			
			double db = aoModel.columnLengths.get(i).doubleValue();
			//System.out.println(db);
			tc.setPreferredWidth((int)Math.round(db)*200);
			//tc.setPreferredWidth(200);
			
		}
		
		
		// TODO sort the table rows in the model class
		// Construct a TableRowSorter and set it as the sorter for the table
		TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(aoModel);
		jt.setRowSorter(sorter);
		
		// add scrollPane rather than panel. This is the main content of the central panel
		rContent.add(scrollPane, BorderLayout.CENTER);
		
		// Bottom panel contains an "Ok" button with an action listener
		JPanel okPanel = new JPanel();
		
		JButton ok = new JButton("Ok");
		ok.setToolTipText("Exit to the next engine or combined results screen");
		JButton print = new JButton("Print");
		print.setToolTipText("Generate pdf of this engine's results");
		
		// use the EventHandler class to automatically create listeners and install the handler
		// last argument determines the method that will be called if an event is heard by the listener
		// "setPreferredOptions" which records the favoured options to be made available to aggregator
		ok.addActionListener((ActionListener)EventHandler.create(ActionListener.class, this, "setPreferredOptions"));
		print.addActionListener((ActionListener)EventHandler.create(ActionListener.class, this, "print"));
		
		// add summary and details buttons with their listeners
		//JButton summary = new JButton("Summary Info");
		//JButton details = new JButton("Detailed Info");
		
		//okPanel.add(summary);
		//okPanel.add(details);
		
		//summary.addActionListener((ActionListener)EventHandler.create(ActionListener.class, this, "tabulateSummary"));
		//details.addActionListener((ActionListener)EventHandler.create(ActionListener.class, this, "tabulateDetails"));
	
		np_field.setValue(numPreferred);
		np_field.setSize(20, 20);
		np_label = new JLabel();
		np_label.setToolTipText("The number of options to be carried forward into a combined results screen can be modified for each engine.");

		np_label.setText("No. preferred options");
		okPanel.add(np_label);
		okPanel.add(np_field);
		okPanel.add(ok);
		
		rContent.add(okPanel, BorderLayout.PAGE_END);
		validate();
	}
	
	public void setUpAHPModel(){
		//AHPm = new AdaptationOptionsModel(doc);
		
		//AHPl = new AOListModel();
		
		try{
			// This builds a document of whatever's in the given resource
			SAXBuilder builder = new SAXBuilder();
			// the argument to the builder is any url. Here it is passed the file instance
			// (same file as opened in the browser)
			Document doc = builder.build(loc);			
			// tell the parser whether to validate against a Document Type Definition (DTD) during the build
			// doc.setDocType(DocType dt = new DocType());
			// setValidation(boolean validate)
			// Model extends AbstractTableModel. It is passed the dom object and extracts the table data from that
		
			aoModel = new AdaptationOptionsModel(doc);
			
			// add two columns score (0< x <1) 
			//and rank order gotten from Hierarchy object
			aoModel.addColumn("score", 1.0);		
			aoModel.addColumn("rank", 1.0);
			
		}
		catch (IOException ioe) {
			     System.err.println(ioe);
		}
		catch (JDOMException jde) {
			     System.err.println(jde);
		}
	}
	
	// initialise the model with all of the required columns
	// "addColumn" also initialises entries with empty string
	public void setUpVotingModel(){
		
		try{
			// This builds a document of whatever's in the given resource
			SAXBuilder builder = new SAXBuilder();
			// the argument to the builder is any url. Here it is passed the file instance
			// (same file as opened in the browser)
			Document doc = builder.build(loc);			
			// tell the parser whether to validate against a Document Type Definition (DTD) during the build
			// doc.setDocType(DocType dt = new DocType());
			// setValidation(boolean validate)
			// Model extends AbstractTableModel. It is passed the dom object and extracts the table data from that
		
			aoModel = new AdaptationOptionsModel(doc);
		
			aoModel.addColumn("score", 1.0);		
			aoModel.addColumn("rank", 1.0);
			aoModel.addColumn("voter", 2.0);
			aoModel.addColumn("points", 1.0);
		}
		catch (IOException ioe) {
			     System.err.println(ioe);
		}
		catch (JDOMException jde) {
			     System.err.println(jde);
		}
	}
	
	// this is called when all the exercise has been completed
	public void computeAHP(){
		// iterate through alternatives in the subset
		// extract the values for the final score (and the rank)
		Hierarchy h = ae.getHierarchy();
		Vector<Alternative> v = h.getAlternatives();
		
		List <Option> options = new ArrayList<Option> (0); // used in computing rank
		List <Element> all_options = aoModel.getAll_options();
		// if the name in h matches to those in the AO
		for (Element ao: all_options){
			int oID = all_options.indexOf(ao);
			String dtext = ao.getChildText("descriptor");
			
			for (Alternative a: v){
				int aID = v.indexOf(a);
				// whitespace can mess up string comparison
				// add html tags and trim whitespace before testing
				if (new String(a.getName()).equals(new String("<HTML>" + dtext.trim() + "</HTML>"))){	
				//if (a.getName().equals(new String ("<HTML>" + dtext + "</HTML>"))){
					// add the new data
					System.out.println("Found match: " + dtext);
					// put the rank and the value in the correct columns
					// Pi causing error
					aoModel.setValueAt(h.Pi(aID), oID, aoModel.findColumn("score"));
					System.out.println("set value: " + h.Pi(aID) + ", at row: " + oID);
				
					// build options list (used for ranking)
					Option option = new Option();
					option.setDescriptor(dtext);
					option.setScore((h.Pi(aID)));
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
						System.out.println("Last option, score = " + last_option.getScore());
						
						if(this_option.compareWith(last_option)==0) str_rank = last_option.getRank();
					}
					// TEST
					System.out.println("This option, score = " + this_option.getScore());
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
	
	// this is called when all the votes are in and the award mechanism has been determined
	public void computeVoting(){
		
		// initially computes this with all of the default settings
		// configurable elements could be:
		// -- weightings of votes according to number of people present
		// -- awards
		List <Element> all_options = aoModel.getAll_options();
		// Iterate through all votes, calculate points they contribute
		// Record total points and the names of voters in the AO model
		for (Vote<String> v: re.getPoll().getVotes()){
		
			for (int i=0;i<v.size();i++){
				String vote_elt = v.get(i);
				if(vote_elt!=""){
					int rank = Integer.parseInt(vote_elt) - 1;	// rank must take the values 0,1,2 as array arg.
					// obtain the existing value from the model
					Element ao = all_options.get(i);
					String oldtext = ao.getChildText("points");
				
					Long new_i;
					if(oldtext!="")
						new_i = Long.parseLong(oldtext) + re.awards[rank].longValue();
					else new_i = re.awards[rank].longValue();
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
		
		// compute the score of each option according to scoring scheme
		// e.g. total points divided by total number of voters
		for (int i=0; i<aoModel.getNumOptions();i++){
			// obtain the value of points from the model
			Element ao = all_options.get(i);
			String pointstext = ao.getChildText("points");
			Double pod;
			if(pointstext!="")
				pod = Double.parseDouble(pointstext);
			else pod = new Double(0.0);
			Double score = pod / re.getPoll().getNb_votes();
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
			//printRanks(options);
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
	}
			
		
	public void printRanks(List<Option> opts){
		for(Option opt:opts){
			System.out.println(opt.getDescriptor() + " " + opt.getScore() + " " +  opt.getRank());
		}
	}
	
		
		
	
	
	/*
	// This method is specific to the mock engine. Generate a document from xml file containing the rankings.
	// add the preferred options to the combined results object
	public void mockVoting(){
		// Use the file name returned from the chooser, prefix the word "ranking". 
		// It should be found in the working directory and in the jar distribution
		String ranking = "ranking" + fname;
		choice = new File(ranking);
		
		try{
			// This builds a document of whatever's in the given resource
			SAXBuilder builder = new SAXBuilder();
			// the argument to the builder is any url. Here it is passed the file instance
			Document doc = builder.build(choice);
			// tell the parser whether to validate against a Document Type Definition (DTD) during the build
			// doc.setDocType(DocType dt = new DocType());
			// setValidation(boolean validate)
			// Model extends AbstractTableModel. It is passed the dom object and extracts the table data from that
			aoModel = new AdaptationOptionsModel(doc);
			
			// create a central panel (with a scroll bar?)
			//JPanel rPanel = new JPanel();
			// Text area specifies number of rows and columns for chars
			//JTextArea textArea = new JTextArea(12, 30);
			//JScrollPane scrollPane = new JScrollPane(textArea);
			// set the preferred size of container to be smaller than required to display the rows and cols requested. 
			//setPreferredSize(new Dimension(450, 110));

			
			// create JTable as main content of central panel.
			// JTable is used to display and edit regular two-dimensional tables of cells.
			// JTable simply takes a tabular range of cells and uses getValueAt(int, int) to 
			// retrieve the values from the model during painting.
			// Every table object uses a table model object to manage the actual table data.
			jt = new JTable(aoModel);
			
			int width = jt.getColumnCount();
	
			// Scrollpane is invoked with an argument that refers to the table object
			// table is automatically added to the container
			JScrollPane scrollPane = new JScrollPane(jt);
			jt.setAutoCreateRowSorter(true);		// enable sorting and filtering of rows, e.g. by clicking on column heading
			jt.setFillsViewportHeight(true);		// table uses entire height of its container
			jt.setRowHeight(80);
			
			// TODO setting column widths not working
			//divide dimension of the frame by the sum of the factors
			double total_factors = 0.0;
			for(double cl: aoModel.columnLengths) total_factors += cl;
			//double unit_length = scrollPane.getWidth()/total_factors;
			// Customise initial column widths. 
			// Each column is represented by a TableColumn object which supplies getter and setter methods. 
			TableColumn tc = null;
			for(int i=0; i<width; i++){
				tc = jt.getColumnModel().getColumn(i);
				//int int_width = (int) Math.floor(unit_length * aoModel.columnLengths[i]);
				//tc.setPreferredWidth(int_width);
				//System.out.println(int_width);
				tc.setPreferredWidth((int)Math.round(aoModel.columnLengths[i]));
				tc.setPreferredWidth(200);
			}
			
			
			// TODO sort the table rows in the model class
			// Construct a TableRowSorter and set it as the sorter for the table
			TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(aoModel);
			jt.setRowSorter(sorter);
			
			
			// TODO add any kind of objects to the model as well as Strings
			
		
			// for some reason the sort method does not accept defined element list (generics)
			//Collections.sort(all_options);
			//int rank = length;
			//aoTableObject.
			//ArrayList<Element> ordered_options = new ArrayList<Element>(rank);
			//Element[] ordered_opt = new Element[rank];
			
			
			// assume that integer ranks are unique, and assuming that ranks start at 1
			for (Element option: all_options){
				Element elt = option.getChild("rank");
				String text = elt.getTextTrim();
				System.out.println(text);
				if(text.contains("unranked"))rank = length;
				else rank = (int) Integer.parseInt(text);
				ordered_opt[rank-1]=option;
				//ordered_options.add(rank-1, elt);
			}
			// show information on all options in the panel, starting with the highest ranked
			for (Element option: ordered_opt){
				if(option!=null){
				// show the child elements of the adaptation option, with name of the element on lhs and its value on rhs
				List <Element> all_elements = option.getChildren();
				for (Element elt:all_elements){
					String name = elt.getName();
					JLabel elt_name = new JLabel (name);
					scrollPane.add(elt_name);
					String text = elt.getTextTrim();
					JLabel elt_value = new JLabel(text);
					elt_value.setOpaque(true);
					elt_value.setBackground(Color.WHITE);
					scrollPane.add(elt_value);
				}
				}
			}
			// add scrollPane rather than panel. This is the main content of the central panel
			rContent.add(scrollPane, BorderLayout.CENTER);
			
			// Put the explanation in the top panel
			JPanel exp = new MsgPanel(explanation);
			exp.setPreferredSize(new Dimension (0,40));
			exp.setForeground(Color.RED);
			rContent.add(exp, BorderLayout.PAGE_START);
			
			// Bottom panel contains an "Ok" button with an action listener
			JPanel okPanel = new JPanel();
			JButton ok = new JButton("Ok");
			JButton print = new JButton("Print");
			// use the EventHandler class to automatically create listeners and install the handler
			// last argument determines the method that will be called if an event is heard by the listener
			// "setPreferredOptions" which records the favoured options to be made available to aggregator
			ok.addActionListener((ActionListener)EventHandler.create(ActionListener.class, this, "setPreferredOptions"));
			print.addActionListener((ActionListener)EventHandler.create(ActionListener.class, this, "print"));
			
			// This is convenient if an event listener calls a method without parameters.
			// There is another create method of EventHandler which takes a second argument
			okPanel.add(ok);
			okPanel.add(print);
			rContent.add(okPanel, BorderLayout.PAGE_END);
			validate();
	
		}
		catch (IOException ioe) {
			     System.err.println(ioe);
		}
		catch (JDOMException jde) {
			     System.err.println(jde);
		}
		
		// try to execute the javascript
		//runExternalJSCode();
	}
	*/
	
	// Add a new column to the table in combined results model
	public void setPreferredOptions(){
		setNumPreferred ((Long) (np_field.getValue()));
		
		preferredOptions = new ArrayList <Element>();
		// assume that integer ranks are unique, and assuming that ranks start at 1
		for (Element option: aoModel.getAll_options()){
			String text = option.getChildText("rank");
			if(!text.contains("n.a.")){
				int rank = Integer.parseInt(text);
				if (rank <= numPreferred) preferredOptions.add(option);
			}
			
		}
		/*int rowid=0;
		for (int preferred=0; preferred<num_preferred; preferred++){
			preferredOptions[preferred]=new Vector ();
			do{
				Element op = ordered_opt[rowid];
				if(op!=null)preferredOptions[preferred].add(op);
				rowid++;
			} while (preferredOptions[preferred]==null&& rowid<aoModel.getRowCount());
				
		}*/
	
		/*
		// show information on all options in the panel, starting with the highest ranked
		for (Element option: ordered_opt){
			if(option!=null){
			// show the child elements of the adaptation option, with name of the element on lhs and its value on rhs
			List <Element> all_elements = option.getChildren();
			for (Element elt:all_elements){
				String name = elt.getName();
				JLabel elt_name = new JLabel (name);
				scrollPane.add(elt_name);
				String text = elt.getTextTrim();
				JLabel elt_value = new JLabel(text);
				elt_value.setOpaque(true);
				elt_value.setBackground(Color.WHITE);
				scrollPane.add(elt_value);
			}
			}
		}
		// then access it from the aggregate object
		for (int pv=0; pv<num_preferred; pv++){
			preferredOptions[pv] = new Vector();
			//preferredOptions[pv] = aoModel.rowdata[pv];
			preferredOptions[pv].add(aoModel.getValueAt(pv, 0));
		}*/
		crModel.addPreferredOptions(preferredOptions, e.name());
		// close the frame and return thread control to ADxFrame object
		this.setVisible(false);
	}
	
	public List<Element> getPreferredOptions(){
		return preferredOptions;
	}
	
	public void setNumPreferred(Long num){
		numPreferred = num;
	}
	
	// print out the table containing the results
	public void print(){
		try{
			if(!jt.print()){
				System.err.println("User cancelled printing");
			}
		}catch(java.awt.print.PrinterException e){
			System.err.format("Cannot print %s%n", e.getMessage());
		}
	}
	
	void runExternalJSCode() {
	      ScriptEngineManager scriptMgr = new ScriptEngineManager();
	      ScriptEngine jsEngine = scriptMgr.getEngineByName("JavaScript");
	      /*
	      InputStream is = this.getClass().getResourceAsStream("greetings.js");
	      try {
	        Reader reader = new InputStreamReader(is);
	        jsEngine.put("name", "Richard");
	        out.println(jsEngine.eval(reader));
	        out.println(jsEngine.get("name"));
	      } */
	      InputStream is = this.getClass().getResourceAsStream("maumtoolb.htm");
	      try {
		        Reader reader = new InputStreamReader(is);
		        //jsEngine.put("numcat", 2);
		        //File file = new File("newpage");
		        // Try the DOM
		        // create a html document?
		        //PrintWriter wcmPrintWriter = new PrintWriter(new FileOutputStream("newpage.html"), true);
		        // This builds a document of whatever's in the given resource
				//SAXBuilder builder = new SAXBuilder();
				// the argument to the builder is any url. Here it is passed the file instance
				//Document doc = builder.build(file);
		        //jsEngine.put("document", wcmPrintWriter);
		        // discard until reach the script tag
		        BufferedReader input = new BufferedReader(new FileReader("maumtoolb.htm"));
		        String line;
		        do{
		        	line = input.readLine();
		        }while (!line.contains("<script"));
		        line = input.readLine();
		        
		        out.println(jsEngine.eval(input));
		        out.println(jsEngine.get("document"));
		    } 
	    
	      catch (ScriptException ex) {
	        ex.printStackTrace();
	      }
		 catch (Exception e) {
			 System.out.println("Exception reading BAS");
			 System.out.println(e.getMessage());	
		 }
	   }
	
	public void setAdaptationOptionsModel(AdaptationOptionsModel aoModel){
		this.aoModel = aoModel;
	}
	public AdaptationOptionsModel getAdaptationOptionsModel (){
		return aoModel;
	}
	
}

