package shell;
// Swing is more robust, has more features, is more portable and is easier to use than AWT. 
// Swing is a extension package to 1.1 but now part of the core hierarchy.
import adt.AHPEngine;
import adt.Hierarchy;
import vote.Poll;
import vote.RankingEngine;
import vote.Vote;

import gui.About;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import javax.swing.BoxLayout;
import javax.swing.Box;
import javax.swing.BorderFactory;

import java.awt.event.*;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Container;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

//Typically connections are made from a user interface bean (the event source) to an application logic bean (the target).
import java.beans.EventHandler;

import java.util.ArrayList;
import java.util.Vector;
// contains classes for handling URL
import java.net.*;
//package for document class and components
import org.jdom.*;

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
 *    
 *    
 *    Contact the developer : richard.taylor@sei-international.org
 *    Contact the weADAPT group : info@weadapt.org
 **/

// frames are designed to be containers for components such as menu bar, panels and other GUI elements
// JFrame consists of root, layered, content and glass pane. Content pane is the only one of interest.
public class ADxFrame extends JFrame {
	
	ADx adx;
	// Three strings for text to be painted in the central panel
	String line1;
	String line2;
	String line3;
	String line4;
	
	Container adxContent;
	
	// panels that need to be declared as global variables of frame
	JPanel JPanel, JPanelopt, JPanelInput, JPanelEngines, JPanelStart;		// These are panels with ok button
	GPLDialog gplDialog;
	GPLPanel gplPanel;
	ADxPanel optionsText, inputPanel;
	JPanel tickPanel, tickPanelInput, chooserPanel, enginePanel;
	MsgPanel responsePanel, logPanel, startedPanel;
	JScrollPane scrollPane;
	
	// Frames that need to be global because they are created/accessed in more than one method
	ResultsFrame votingResults;
	ResultsFrame AHPResults;
	
	// Engines that need to be global
	AHPEngine ae;
	RankingEngine re;
	
	InputXMLModel inputModel;				// contains NAPA input data
	CombinedResultsModel crModel;			// contains data on preferred options selected by all engines
	
	ArrayList<String> selectedEngines;
	ArrayList<String> completedEngines;		// list of engines completed the analysis task
	StringBuffer started = new StringBuffer("Showing combined results: ");
	
	// the combined results table (created when all the engines have completed) must be global
	JTable crTable;
	
	// Chooser of the xml source that gets the name of the source and holds the DOM object
	ChooserButton chooserButton;
	Document doc;
	
	// chooser panel on the right
	JFileChooser fc = new JFileChooser();
	
	String fname;	// reference to the string which holds the name of the file
	//String path;	// path to the file
	String loc;		// absolute path (path + fname combined)
	
	// state of the two checkboxes for alternative ways of selecting engines 
	boolean stateEs, stateUs;
	
	// state of checkboxes for the engines
	boolean stateElim, stateVot, stateAHP, stateRob, stateCBA;
	
	// JAHP variables
	JMenu mFile,mHelp; // Menus
	JMenuItem miLoad,miSave,miQuit,miAbout; //MenuItems
	//File home; //home directory
	// File home = new File ("related/"); // home directory where logos, icons etc could be found
	// inorder to create the JAR use:
    File home = new File ("/related"); // home directory where logos, icons etc could be found
    //File home = new File ("/"); 
    File home_example; //directory where examples could be found 
	File file_icon; // weadapt icon
	
	File imageFile1; //GPL info
	
	public ADxFrame(){
		// initialise empty arraylist of strings of default size
		selectedEngines = new ArrayList<String>();
		completedEngines = new ArrayList<String>();
		
		// centre on the user's screen, set size to half screen size
		// Toolkit methods interface with the native windowing system
		// Note that this will give very different dimensions for widescreen
		// It should be tested to work with 1024*768 aspect.
		Toolkit kit = Toolkit.getDefaultToolkit();
		Dimension screenSize = kit.getScreenSize();
		int screenWidth = screenSize.width;
		int screenHeight= screenSize.height;
		
		// TODO increase the size of the PAGE_START area in border layout
		
		// representation of images is also system dependent
		// therefore use the toolkit to load
		Image img = kit.getImage("adxicon.jpg");
		setIconImage(img);
		setTitle("Adaptation Options Explorer");
		
		setSize(screenWidth / 2, screenHeight / 2);
		setLocation(screenWidth /4, screenHeight / 4);
		adxContent = getContentPane();
		
		// ChooserButton class with listener specially written for file choosing.
		// Create the object in the constructor method because it allows access to information about the chosen file.
		chooserButton = new ChooserButton("choose"); 
		
		//adxContent.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		//setOpaque(true); //components must be opaque
		
		// Panels have two useful properties: they have a surface onto which you can draw
		// They themselves are containers that can hold UI components such as buttons, sliders etc.
	
		// Panels have two useful properties: they have a surface onto which you can draw
		// They themselves are containers that can hold UI components such as buttons, sliders etc.
		
		// Start with a panel with ok button and panel with some text
		JPanel = new JPanel();
		JButton ok = new JButton("Ok");
		JButton terms = new JButton("Terms");
		// use the EventHandler class to automatically create listeners and install the handler
		// last argument determines the method that will be called if an event is heard by the listener - here, OPTIONS
		// ok.addActionListener((ActionListener)EventHandler.create(ActionListener.class, this, "OPTIONS"));
		// ***new*** call input instead of options
		ok.addActionListener((ActionListener)EventHandler.create(ActionListener.class, this, "INPUT"));
		
		terms.addActionListener((ActionListener)EventHandler.create(ActionListener.class, this, "TERMS"));
		
		// This is convenient if an event listener calls a method without parameters.
		// There is another create method of EventHandler which takes a second argument
		// JPanel.add(terms);
		JPanel.add(ok);
		JPanel.add(terms);
		adxContent.add(JPanel, BorderLayout.PAGE_END);
		// The painted text  panel (which would usually go in the central position) is created
		// with the class ADxPanel
		// Pass it the strings and the URL
		try{
			
			URL url = new URL("http://www.weadapt.org/");
			line1 = "ADx Copyright 2012 Richard Taylor";
			// line2 = "weADAPT group (www.weADAPT.org)";
			line2 = "This program comes with ABSOLUTELY NO WARRANTY";
		    line3 = "This is free software, and you are welcome to";
		    line4 = "redistribute it under certain conditions";
			//line3 = "email: info@weadapt.org";
		} catch (MalformedURLException e){
			
		}
		// TODO print out the URL such that it could be a http link to a web page
		gplPanel = new GPLPanel(line1, line2, line3, line4);
		adxContent.add( gplPanel, BorderLayout.CENTER);
		// alternatively, to show a single swing component simply set the pane to that component
		// setContentPane(adxPanel);	
	}
	
	// Options screen regarding whether the selection of engines will be assisted by ADx
	public void OPTIONS(){
		
		adxContent.remove(gplPanel);
		adxContent.remove(JPanel);
		validate();
		// this time there are three panels
		// The first panel is for the ok button
		JPanelopt = new JPanel();
		JButton optionsok = new JButton("Ok");
		// JPanelopt.setBounds(new Rectangle(200,200));
		// the listener should send the event thread to the next stage:  INPUT
		optionsok.addActionListener((ActionListener)EventHandler.create(ActionListener.class, this, "INPUT"));
		
		JPanelopt.add(optionsok);
		adxContent.add(JPanelopt, BorderLayout.PAGE_END);
		
		// create the two tickboxes
		JCheckBox us = new JCheckBox ("User Select");
		JCheckBox es = new JCheckBox ("ADx select");
		// use item listener with CheckBox
		us.addItemListener((ItemListener)EventHandler.create(ItemListener.class, this, "toggleUs") );
		es.addItemListener((ItemListener)EventHandler.create(ItemListener.class, this, "toggleEs") );
		// These do not start as selected being the default
		stateEs = false; stateUs = false;
		
		// Put check box items in a column in a panel. positioned on the left
		// Create a panel with a GridLayout including just one column 
		tickPanel = new JPanel(new GridLayout (0,1));
		// add some spacing at top and bottom of panel
		tickPanel.setBorder(BorderFactory.createEmptyBorder(120,0,100,0));
		tickPanel.add(us);
		tickPanel.add(es);
		adxContent.add(tickPanel, BorderLayout.LINE_START);
		
		// Finally use another ADx panel, where the text will explain the checkboxes
		line1 = "How should the selection of methods be carried out?";
		line2 = "You, the user, will select, or allow ADx recommend?";
		line3 = "Or both alternatives? Check boxes that apply:";
		optionsText = new ADxPanel(line1, line2, line3);
		adxContent.add( optionsText, BorderLayout.CENTER);
		//adxContent.add( optionsText, BorderLayout.LINE_END);
		validate();
        // You can change a panel's transparency by invoking the setOpaque method. 
        // A transparent panel draws no background, so that any components underneath show through.
	}
	
	// TODO Terms screen providing GPL info
	public void TERMS() throws IOException{
		// an about box with a scroll panel
		/*
		Copyright 2012 Richard Taylor
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
		 *    
		 *    
		 *    Contact the developer : richard.taylor@sei-international.org
		 *    Contact the weADAPT group : info@weadapt.org 
		 */ 
		//file_mail=new File(home,"ComposeMail24.gif");
	    //file_edit=new File(home,"Edit24.gif");
	    file_icon =new File(home,"weADAPT.jpg");
	    imageFile1 = new File(home,"GPL2-2.png");
	    //System.out.println("abs.path=" + file_icon.getAbsolutePath());
	    //System.out.println("path=" + file_icon.getPath());
	    //System.out.println(imageFile1.getAbsolutePath());
	    // show a dialog frame (About...). with terms information png
	    (new GPLDialog(this,file_icon,imageFile1)).setVisible(true);
		
	}
	
	public void INPUT(){
		adxContent.removeAll();
		
		validate();
		/* use the following code if OPTIONS () is activated
				
		// check state of two checkboxes. starting panel should contain a message relating to that.
		String msgUs = new String (" User selection is necessary in Version 2.0.");
		String msgEs = new String (" Engine selection not available in Version 2.0.");
		String response = new String();
		if(stateUs && !stateEs) response = "Thankyou";
		if(stateUs && stateEs) response = "Sorry, " + msgEs;
		if(!stateUs && !stateEs) response = "Sorry, " + msgUs;
		if(!stateUs && stateEs) response = "Sorry, " + msgUs + msgEs;
		responsePanel = new MsgPanel(response);
		responsePanel.setPreferredSize(new Dimension (0,40));
		responsePanel.setForeground(Color.RED);
		adxContent.add(responsePanel, BorderLayout.PAGE_START);
		
		// panel for the tickboxes should now be created with ticks set in the following way
		stateUs = true; stateEs = false;
		// should not be able to modify
		JCheckBox us = new JCheckBox ("User Select", stateUs);
		JCheckBox es = new JCheckBox ("ADx select", stateEs);
		us.setEnabled(false);
		es.setEnabled(false);
		// as the panel will be now frozen these buttons need no action listeners 
				
		// Create a panel with a GridLayout including just one column 
		GridLayout gl = new GridLayout (0,1);
		tickPanelInput = new JPanel(gl);
		tickPanelInput.add(us);
		tickPanelInput.add(es);
		// add some spacing at top and bottom of panel
		tickPanelInput.setBorder(BorderFactory.createEmptyBorder(120,0,100,0));
		adxContent.add(tickPanelInput, BorderLayout.LINE_START);
		*/		
		// input panel in the centre
		line1 = "A file chooser may be used to select your source xml file";
		line2 = "(If you have one) click on the button on the right";
		line3 = "Click Ok to go to the engines";
		inputPanel = new ADxPanel(line1, line2, line3);
		adxContent.add(inputPanel, BorderLayout.CENTER);
		
		// ok panel at the bottom
		JPanelInput = new JPanel();
		JButton ok = new JButton("Ok");
		
		// the listener should send the event thread to the next stage:  ENGINES
		ok.addActionListener((ActionListener)EventHandler.create(ActionListener.class, this, "ENGINES"));
		JPanelInput.add(ok);
		adxContent.add(JPanelInput, BorderLayout.PAGE_END);
		
		//fc.setCurrentDirectory(new File("C:/java programming/ADx"));
		chooserPanel = new JPanel();
		BoxLayout bl = new BoxLayout(chooserPanel, BoxLayout.PAGE_AXIS);
		chooserPanel.setLayout(bl);
		// TODO double height calculate height of available area for this panel
		//double buttonheight = 100;
		//double amount = height/2.0 - buttonheight;
		//int intamt = (int) Math.ceil(amount);
		//System.out.print (intamt);
		chooserPanel.add(Box.createRigidArea(new Dimension(0, 150)));		// space the button away from the top
		// ChooserButton class with listener specially written for file choosing
		// chooserButton = new ChooserButton("choose");
		//chooserButton.setPreferredSize(new Dimension(0,40));
		chooserButton.setFileChooser(fc);
		chooserButton.setParent(chooserPanel);
		//chooserPanel.setLayout(new GridLayout(0,1));
		chooserPanel.add(chooserButton);	
		adxContent.add(chooserPanel, BorderLayout.LINE_END);
		validate();
	}
	
	// screen showing the engines
	public void ENGINES(){
		System.out.println("Starting Engines");
		// create the crModel after completing the INPUT stage successfully with the chooseButton
		// obtain the DOM doc and pass it to the crModel constructor
		doc = chooserButton.getDocument();
		inputModel = new InputXMLModel(doc);
		
		crModel = new CombinedResultsModel(this);
		// obtain the table data and the column names from the input model
		crModel.setInputData(inputModel);
		
		//adxContent.remove(JPanelInput);
		//adxContent.remove(inputPanel);
		//adxContent.remove(tickPanelInput);
		//adxContent.remove(chooserPanel);
		//adxContent.remove(responsePanel);
		adxContent.removeAll();
		// ok panel at the bottom
		JPanelEngines = new JPanel();
		JButton ok = new JButton("Ok");
		ok.setToolTipText("Click this button to start the application of selected methods.");
		// the listener should start various processes in separate threads, and bring the ADx to the "START" phase
		ok.addActionListener((ActionListener)EventHandler.create(ActionListener.class, this, "START"));
		JPanelEngines.add(ok);
		adxContent.add(JPanelEngines, BorderLayout.PAGE_END);
		
		// in the top panel there is a string referring to the chooser log
		logPanel = new MsgPanel(chooserButton.getLog().toString());
		logPanel.setForeground(Color.RED);
		logPanel.setPreferredSize(new Dimension (0,40));
		adxContent.add(logPanel, BorderLayout.PAGE_START);
		//  name of the file *relative to the current directory* can be obtained from the chooser button object 
		fname = chooserButton.getFname();
		
		File f = fc.getSelectedFile();
		loc = f.getAbsolutePath();
		
		
		// in the adx panel there are various checkboxes (most will be unavailable) representing the engines
		// panel for the tickboxes should now be created with ticks set in the following way
		stateElim = false; stateVot = false; stateAHP = false; stateRob = false; stateCBA = false;
		
		// first, the internal engines
		JCheckBox elim = new JCheckBox ("Elim.By.Aspects", stateElim);
		elim.setEnabled(false);
		
		// the voting engine and AHP engine will be enabled
		// if the file was chosen successfully they will be selected by default  
		if(chooserButton.getReturnVal() == JFileChooser.APPROVE_OPTION) 
		{
			stateVot=true;
			stateAHP = true;
		}
		else 
		{
			stateVot = false;
			stateAHP = false;
		}
		
		JCheckBox vot = new JCheckBox ("Voting", stateVot);
		vot.setEnabled(true);
		vot.setToolTipText("Voting can be carried out in a workshop or classroom setting. Each participant in turn may input his/her preferences for particular options.");
		JCheckBox ahp = new JCheckBox ("Analytic Hierarchy Process", stateAHP);
		ahp.setEnabled(true);
		ahp.setToolTipText("AHP is most often used by a single decision-maker, but can also be used in group settings; although participants must agree on a judgement for each comparison pair.");

		// now the external engines
		JCheckBox rob = new JCheckBox ("Robust Decision Making", stateRob);
		JCheckBox cba = new JCheckBox ("Cost Benefit Analysis", stateCBA);
		rob.setEnabled(false);
		cba.setEnabled(false);
		
		// TODO place an icon above each checkbox
		// use item listener with CheckBox
		elim.addItemListener((ItemListener)EventHandler.create(ItemListener.class, this, "toggleElim") );
		vot.addItemListener((ItemListener)EventHandler.create(ItemListener.class, this, "toggleVot") );
		ahp.addItemListener((ItemListener)EventHandler.create(ItemListener.class, this, "toggleAHP") );
		rob.addItemListener((ItemListener)EventHandler.create(ItemListener.class, this, "toggleRob") );
		cba.addItemListener((ItemListener)EventHandler.create(ItemListener.class, this, "toggleCBA") );
		
		enginePanel = new JPanel();
		enginePanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		BoxLayout bl = new BoxLayout(enginePanel, BoxLayout.PAGE_AXIS);
		JLabel jl = new JLabel("Select those methods you wish to apply from the list of engines");
		enginePanel.add(jl);
		enginePanel.setLayout(bl);
		enginePanel.add(elim);
		enginePanel.add(vot);
		enginePanel.add(ahp);
		enginePanel.add(rob);
		enginePanel.add(cba);
		adxContent.add(enginePanel, BorderLayout.CENTER);
		// for some reason the engine panel butts up to the lhs??
		validate();
	}
	
	// START is initially called from ENGINES when the ok button is pressed.
	// Control passes out of this method to selected engine. Engine must call back START when it completes its task 
	public void START(){
		
		boolean complete = true;
		selectedEngines = new ArrayList<String>();
		if(stateAHP) selectedEngines.add("AHP");
		if(stateElim) selectedEngines.add("ELIMINATION");
		if(stateVot) selectedEngines.add("VOTING");
		if(stateRob) selectedEngines.add("ROBUSTDM");
		if(stateCBA) selectedEngines.add("CBA");
		
		for (String engine: selectedEngines){
			boolean foundse = false;
			for(String ce:completedEngines) if(ce==engine)foundse=true;
			if(!foundse) complete = false;	
		}
		if (!complete){		// look through array to find an engine not completed
			//if(stateAHP && !completedEngines.contains("AHP")) startAHP();			
			//else if(stateElim && !completedEngines.contains("Elimination")) startELIMINATION();
			//else if(stateVot && !completedEngines.contains("Voting"))startVOTING();
			// ... etc
			if(stateVot && !completedEngines.contains("VOTING"))startVOTING();			
			else if(stateElim && !completedEngines.contains("ELIMINATION")) startELIMINATION();
			else if(stateAHP && !completedEngines.contains("AHP")) startAHP();
			// ... etc
		}
		else{			 //(complete= true)
			// When all of the selected engines have completed all tasks, including setting the preferred options
			// paint the combined results. Combined results contains a JTable in the central panel.
			// The model has been created earlier and table data updated by the engines
			crTable = new JTable(crModel);	
			
			adxContent.remove(enginePanel);
			adxContent.remove(logPanel);
			adxContent.remove(JPanelEngines);
		
			// Scrollpane is invoked with an argument that refers to the table object
			// table is automatically added to the container
			scrollPane = new JScrollPane(crTable);
			crTable.setFillsViewportHeight(true);		// table uses entire height of its container
			crTable.setRowHeight(80);
			adxContent.add(scrollPane, BorderLayout.CENTER);
			
			// add an ok button at the bottom
			JPanelStart = new JPanel();
			JButton ok = new JButton("Ok");
			JButton print = new JButton("Print");
			
			JPanelStart.add(ok);
			JPanelStart.add(print);
			ok.addActionListener((ActionListener)EventHandler.create(ActionListener.class, this, "FINAL"));
			print.addActionListener((ActionListener)EventHandler.create(ActionListener.class, this, "print"));
			adxContent.add(JPanelStart, BorderLayout.PAGE_END);
			
			// Top message panel
			if(stateElim)started.append(", Elimination by Aspects");
			if(stateVot)started.append(", Voting");
			if(stateAHP)started.append(", AHP");
			if(stateRob)started.append(", Robust Decision Making");
			if(stateCBA)started.append(", Cost Benefit Analysis");
			if(stateElim||stateVot||stateAHP||stateRob||stateCBA){
				startedPanel = new MsgPanel(started.toString());
				startedPanel.setForeground(Color.RED);
				startedPanel.setPreferredSize(new Dimension (0,40));
				adxContent.add(startedPanel, BorderLayout.PAGE_START);
			}
			validate();
			// finally control passes to FINAL to display the end screen when the button is clicked
		}
	}
	
	
	// final screen. program may continue until other processes complete
	public void FINAL(){
		adxContent.remove(scrollPane);
		adxContent.remove(JPanelStart);
		adxContent.remove(startedPanel);
		//validate();
		//repaint();
		// close or exit button at the bottom
		JPanel closePanel = new JPanel();
		JButton close = new JButton("Close");
		closePanel.add(close);
		close.addActionListener((ActionListener)EventHandler.create(ActionListener.class, this, "close"));
		adxContent.add(closePanel, BorderLayout.PAGE_END);
		
		
		// final ADX panel
		line1 = "You have completed all steps of the ADX prototype";
		line2 = "Thank you for trying the tool.";
		line3 = "User feedback is welcome - please visit www.weADAPT.org";
		ADxPanel exitPanel = new ADxPanel(line1, line2, line3);
		adxContent.add(exitPanel, BorderLayout.CENTER);
		validate();		
	}
	
	// print out the table containing the results
	public void print(){
		try{
			if(!crTable.print()){
				System.err.println("User cancelled printing");
			}
		}catch(java.awt.print.PrinterException e){
			System.err.format("Cannot print %s%n", e.getMessage());
		}
	}
	
	// save as html
	
	// this is called when the action event occurs on the "Close" button on the final screen
	public void close(){
		this.setVisible(false);
	}
	
	// toggle the state of the way to select engines or methods. These methods are called by item listeners
	// In the options stage
	public void toggleUs(){
		if(stateUs) stateUs = false;
		else stateUs = true;
	}
	public void toggleEs(){
		if(stateEs) stateEs = false;
		else stateEs = true;
	}
	// toggle the engines themselves
	public void toggleElim(){
		if(stateElim) stateElim = false;
		else stateElim = true;
	}
	public void toggleVot(){
		if(stateVot) stateVot = false;
		else stateVot = true;
	}
	public void toggleAHP(){
		if(stateAHP) stateAHP = false;
		else stateAHP = true;
	}
	public void toggleRob(){
		if(stateRob) stateRob = false;
		else stateRob = true;
	}
	public void toggleCBA(){
		if(stateCBA) stateCBA = false;
		else stateCBA = true;
	}
	

	public static final int DEFAULT_WIDTH = 300;
	public static final int DEFAULT_HEIGHT = 225;
	
	
	// start external engine NAIADE for MCA 
	// TODO is there any good reason why it must be static?
	public static void startNAIADE(){
		
		/* Execute a process that is external to the JVM with the exec method
		 try{
			Runtime rt = Runtime.getRuntime();
			// Processes are often seen as synonymous with programs or applications
			Process proc = rt.exec("C:/Program Files/NAIADE/Naiade.exe");
			//Process proc = rt.exec("C:/Program Files/Mozilla Firefox/firefox.exe");
		}
		*/
		
		// Execute a process that is external to the JVM with the ProcessBuilder method
		// as of JAVA 5.0
		try{
			String command = new String();
			command = "C:/Program Files/NAIADE/Naiade.exe";
			String [] str_array = {command};	// an array of one element
			// Process builder class includes methods to easily modify the environment variables
			ProcessBuilder pb = new ProcessBuilder(str_array);
			
			// Process builder class includes methods to easily modify the environment variables
			// e.g. start the process in a different directory
			//File f = new File("C:/Program Files/NAIADE");
			//command = "Naiade.exe";
			//String [] str_array = {command};	// an array of one element
			//ProcessBuilder pb = new ProcessBuilder(str_array);
			//pb.directory(f);
			
			//e.g. get a Map of the variables through the environment method
			//Map<String,String> env = pb.environment();
			
			// after environment variables and directory are set, start the process builder	
			// Processes are often seen as synonymous with programs or applications
			// use the process class to manipulate the process's streams and get its exit status
			Process proc = pb.start();
			
			// log the possible usage statements, exhaust that stream before waiting to exit
			InputStream stderr = proc.getErrorStream();
			InputStreamReader isr = new InputStreamReader(stderr);
			BufferedReader br = new BufferedReader(isr);
			String line = null;
			System.out.println("<error>");
			while ((line = br.readLine())!=null)System.out.println();
			System.out.println("</error>");
			
			// process the output
			// note that the method used to obtain a process's output stream is 
			// called getInputStream (the API sees things from the perspective of the Java program)
			InputStream stdout = proc.getInputStream();
			InputStreamReader osr = new InputStreamReader(stdout);
			BufferedReader obr = new BufferedReader(osr);
			String oline = null;
			System.out.println("<output>");
			while ((oline = obr.readLine())!=null)System.out.println();
			System.out.println("</output>");
			
			// waitFor() method of Process returns  a '0' when the windows application is 
			// closed down, indicating normal termination.
			// TODO Need to resolve the question of how to incorporate results into ADx.
			// int exitVal = proc.waitFor();
			
			// Instead of using waitFor() method of Process I would prefer passing a
			// boolean parameter called waitFor into the exitValue() method to determine
			// whether or not the current thread should wait

			boolean waitFor = false;
			
			int exitVal;
			try {
				if (waitFor) exitVal = proc.waitFor();
				else exitVal = proc.exitValue();
				
				System.out.println("process exitValue: " + exitVal);
			} catch (IllegalThreadStateException itse)
			{
				itse.printStackTrace();
		
			}
			
			//System.out.println("process exitValue: " + exitVal);
			
			// Note: Runtime.exec() sometimes hangs because of limited buffer size
			// for input and output streams of the subprocess
			
			// Normally an exit value of 0 indicates success
			 
		}catch (Throwable t){
			t.printStackTrace();
		}	
	}
	
	// methods should cause two things would happen:
	// (1)generate an output screen of this engine and (2) forward the preferred options to the results aggregator
	
	public static void startELIMINATION(){
		// start JESS
	}
	
	public void startAHP(){
		System.out.println("Entered StartAHP method");
		
		// implementation of voting engine 
		ae = new AHPEngine(inputModel, this);
		//loc = path + fname;
		ae.setTitle("AHP engine  (FILE: " + loc + " )");
		ae.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// show method is @deprecated
		ae.setVisible(true);
	}
	public void startVOTING(){
		/* mock version of this method will show the rankings given in the tanzania NAPA
		// if input file is chosen and if voting method is selected (which it is by default)
		String explanation = new String("Ranking of options in the Tanzania NAPA");
		// generate a results frame for voting engine. Uses the mockVoting method.
		// Pass the combined results object and preferred objects will be added
		votingResults = new ResultsFrame(fname, crModel); */

		System.out.println("Entered StartVoting method");
		
		// implementation of voting engine 
		// Carry out voting exercise using the adaptation options browser - RankingEngine object created here
	
		re = new RankingEngine(doc, this);
		//loc = path + fname;
		re.setTitle("Ranking engine  (FILE: " + loc + " )");
		re.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// show method is @deprecated
		re.setVisible(true);
	}
	
	public void AHPResult(Hierarchy hierarchy) {
				
		// Results of the ahp will be accessible to the results frame and to the combined results model. 
		// generate a results frame for AHP engine.
		AHPResults = new ResultsFrame(ae, loc, crModel, Engine.AHP);
		AHPResults.setTitle("Results of AHP exercise");
		AHPResults.setExplanation("Preference data");
		
		// TODO calculation part should be separate from the display part
		AHPResults.setUpAHPModel();
		AHPResults.computeAHP();
		
		// this call asks the results frame to display the table of results
		// when the OK button is clicked, the table will be set invisible again
		// and the
		AHPResults.tabulateDetails();
		
		
		
		AHPResults.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		AHPResults.setVisible(true);
		
	}
	
	//public void votingResult(ArrayList<Vote<String>> res){
	// does not read the argument at this point 
	public void votingResult(Poll pi){
		
		// Results of the voting will be accessible to the results frame and to the combined results model. 
		// generate a results frame for voting engine.
		votingResults = new ResultsFrame(re, loc, crModel, Engine.VOTING);
		votingResults.setTitle("Results of ranking exercise");
		votingResults.setExplanation("Data collected during participatory session");
		//votingResults.setUpVotingModel();
		pi.computeVoting();
		
		votingResults.setAdaptationOptionsModel(pi.getAdaptationOptionsModel());
		//votingResults.computeVoting();
		votingResults.tabulateDetails();
		
		votingResults.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		votingResults.setVisible(true);
		
		// input /combo boxes in the top panel
		// build a list as in the mock voting exercise
		// update and finish buttons at the bottom - set listeners on each of these buttons
		/*
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
		
		// votingResults has access to ranking engine and can obtain data from it
		
		votingResults.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		votingResults.setVisible(true);
		
		// update button - redraw the panel
		*/
		
		// finish button
		// cr model has its options set after the engine has accomplished the task and control passes to START();
		// end by sending the 
		
	
	}
	public void recordCompletedEngine(String str){
		completedEngines.add(str);		// list of engines completed the analysis task
	}


	

}

