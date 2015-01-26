package adt;

import gui.About;
import gui.AlternativePanel;
import gui.AlternativesPanel;
import gui.CriteriaPanel;
import gui.CriteriumPanel;
import gui.Leftpanel;
import gui.Rightpanel;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.*;
import java.awt.GridLayout;
import java.awt.Color;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.Set;
import java.util.Vector;
import java.lang.Number;


// package for document class and components
import org.jdom.*;
// package for saxbuilder and other document builder
import org.jdom.input.*;
import org.jdom.output.XMLOutputter;

import shell.ADxFrame;
import shell.InputXMLModel;
import vote.Poll;
import vote.Vote;
import shell.AOListModel;




//Typically connections are made from a user interface bean (the event source) to an application logic bean (the target).
import java.beans.EventHandler;

import javax.swing.JFileChooser;
import javax.swing.JComponent;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
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
// does not use a table model and JTable but creates a frame browser
public class AHPEngine extends JFrame implements ActionListener{
	
	  // JAHP variables
	  JMenu mFile,mHelp; // Menus
	  JMenuItem miLoad,miSave,miQuit,miAbout; //MenuItems
	  File home; //home directory
	  File home_icons; // directory where icons could be found
	  File home_example; //directory where examples could be found 
	  File default_file; //default file loaded
	  File file_mail; //mail icon
	  File file_edit; //mail icon
	  File imageFile1; //photo author

	  // data
	  // hierarchy is build in the buttonStart listener
	  private Hierarchy h;
	  private Criterium current_criterium;
	  private Alternative current_alternative;

	  // Panels
	  JSplitPane jsp;

	  private Rightpanel rp;
	  private AlternativesPanel asp;
	  private AlternativePanel ap;

	  private Leftpanel lp;
	  private CriteriaPanel csp;
	  private CriteriumPanel cp;
	
	int[] listIndices;  		// indices of those options chosen for the tool
	private int length;			// number of adaptation options
	private int uniqueID;		// criterion unique ID
	private String status;
	
	// define goal (to be input by user)
	String goalString = new String("Please enter the overall decision goal");
	
	// define criteria (to be input by user)
	//ArrayList<String> criteriaStrings = new  ArrayList <String>();
	
	Container aoContent;
	// Document doc;
	Element root;
	// define options (same as those contained in xml file ?)
	List <Element> all_options; 
	
	// Used for creating the JList
	AOListModel aol;
	InputXMLModel ixm;
	
	// This provides access to the class that creates the engine and processes the poll upon finalization 
	ADxFrame adxFrame;	
	
	JTextField goal = new JTextField();								// Text field for Goal which will later be transferred to the Hierarchy 
	
	// arrays of textfields for criteria and respective delete buttons
	ArrayList <JTextField> criteria = new ArrayList <JTextField> ();
	//ArrayList <JButton> buttons = new ArrayList <JButton> ();
	
	
	// GridBag the next most powerful and flexible layout manager after GroupLayout
	GridBagLayout gridbag = new GridBagLayout();
	GridBagConstraints c = new GridBagConstraints();
	//For each component to be added to this container:
	//...Create the component...
	//...Set instance variables in the GridBagConstraints instance...
	// pane.add(theComponent, c);

	//Long ranks;					//desired/ default votes - number of votes allowed each voter
	
	List <Element> optionsList;
	//ArrayList <Vote<String>> pollList;		// arraylist to which the voters responses are added
	//Poll poll;
	//Long defaultvotes; 
	//Vote<String> list;					// For each adaptation option store a string (ranking number)
	//boolean finalize = false;			// voter 1 is allowed to set the poll parameters
	
	//int numCriteria = 1;						// maintain a count of the number of voters for titling the window
	//int numSubCriteria = 1;
	int numOptions;
	
	
	// colNames is initially set from the DTD but can be expanded in the computational stage.
	// For a dynamic field variable better therefore to use an arraylist rather than static array
	public ArrayList <String> colNames = new ArrayList <String> ();					// initialise colNames
	public ArrayList <Double> columnLengths = new ArrayList <Double> ();
	
	// define variables to hold the comparison data that will be input by users
	// use the first screen to get the input from the user
	
	
	
	// obtain the column names from the DTD
	public void setColNames(){
		// Access one of the DTD elements (the first element) and obtain the field names from it
		Element first_ao = all_options.get(0);
		// iterate through the child elements of the first option, obtain name of the element (not its value)
		List <Element> all_elements = first_ao.getChildren();
		
		for (int i=0; i< all_elements.size(); i++){
		
			Element elt = all_elements.get(i);
			String name = elt.getName();
			colNames.add(name);
		}
	}
	
	public void setColLengths(){
		// Access one of the DTD elements (the first element) and obtain the field names from it
		Element first_ao = all_options.get(0);
		// iterate through child elements of the first option, obtain value of element and length of string
		for(int i=0;i<colNames.size(); i++){
			String str = colNames.get(i);
			if (str.length()<5)
				columnLengths.add(1.0);	// smallest possible value
			else{
				if (str.length()>10)
					columnLengths.add(2.0);	// highest possible value
				else columnLengths.add(1.5);
			}
		}
		
	}
	
	
	public AHPEngine(){
		super("Java Analytic Hierarchy Process");
	
		Toolkit kit = Toolkit.getDefaultToolkit();
		Dimension screenSize = kit.getScreenSize();
		int screenWidth = screenSize.width;
		int screenHeight= screenSize.height;
		
		// TODO increase the size of the PAGE_START area in border layout
		
		// representation of images is also system dependent
		// therefore use the toolkit to load
		//Image img = kit.getImage("adxicon.jpg");
		//setIconImage(img);
			
		setSize(screenWidth * 3 / 4, screenHeight / 2);
		setLocation(screenWidth * 1/8, screenHeight * 3 / 8);
		
		//textfield = new JTextField("Please enter the criterion c"+ numCriteria);
		//textfield.setMaximumSize(new Dimension(getSize().width/4, 20));
		
		aoContent = getContentPane();
		aoContent.removeAll();
		
		// finally, call the show method with each pair of options / criteria to compare
		// arguments are the level and the pair?
		firstInput();
	}
	// add doc argument
	public AHPEngine(InputXMLModel ixm, ADxFrame adx){
		super("Java Analytic Hierarchy Process");
	    
	    // window 
	    addWindowListener(new WindowAdapter() {
		public void windowClosing(WindowEvent e) {
		  System.exit(0);
		}
	      });
		
	    this.ixm = ixm;
	   
		this.adxFrame = adx;
		
		// set the length variable (overall number of options)
		// length = ixm.getRowCount();
		all_options = ixm.getAll_options(); 
		length = all_options.size();
		
		System.out.println("number of options in full set: " + length);
		//System.out.println("number of columns: " + ixm.getColumnCount());
		
		// JAHP constructor code
	

	    //File DATA
	    home= new File("..");// export JAHP_PATH
	    home_icons=new File(home,"icons");
	    home_example=new File(home,"examples");
	    default_file= new File(home_example,"essai.ahp");
	    file_mail=new File(home_icons,"ComposeMail24.gif");
	    file_edit=new File(home_icons,"Edit24.gif");
	    imageFile1=new File(home_icons,"morge.png");

		
		// create the AOL - this is for filtering options for AHP
		aol = new AOListModel(ixm);
		// make Jlist corresponding to the descriptors column
		aol.setColumnOfModel(0);
		
		uniqueID=0;
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
		//Image img = kit.getImage("adxicon.jpg");
		//setIconImage(img);
			
		setSize(screenWidth / 2, screenHeight / 2);
		setLocation(screenWidth * 3/8, screenHeight * 3 / 8);
		
		//textfield = new JTextField("Please enter the criterion c"+ numCriteria);
		//textfield.setMaximumSize(new Dimension(getSize().width/4, 20));
		
		aoContent = getContentPane();
		aoContent.removeAll();
		
		// there is quite a lot of code in RankingEngine not in AHPEngine constructor
		// create vote object to hold rank numbers for the current vote and initialise with blank strings
	    h = new Hierarchy();	  
	    h.setAdaptationOptionsModel(ixm);
		
		// finally, call the show method with each pair of options / criteria to compare
		// arguments are the level and the pair?
		firstInput();
		
	}
	
	
	// no arg const
	// this code bulids the JAHP gui
	public void startJAHP(){
		// JAHP constructor code
		current_alternative=(Alternative) (h.getAlternatives()).get(0);
	    current_criterium=h.getGoal();
	    System.out.println("goal is: " + h.getGoal());
	    
	    // Make Menu
	    JMenuBar menuBar = new JMenuBar();
	    // File Menum
	    mFile = new JMenu("File");
	    miLoad = new JMenuItem("Load...  ");
	    miLoad.addActionListener(this);
	    mFile.add(miLoad);
	    miSave = new JMenuItem("Save...  ");
	    miSave.addActionListener(this);
	    mFile.add(miSave);
	    miQuit = new JMenuItem("Quit     ");
	    miQuit.addActionListener(this);
	    mFile.add(miQuit);
	    menuBar.add(mFile);
	    // Help Menu
	    mHelp = new JMenu("Help");
	    miAbout = new JMenuItem("About...  ");
	    miAbout.addActionListener(this);
	    mHelp.add(miAbout);
	    menuBar.add(mHelp);
	    this.setJMenuBar(menuBar);



	    this.ap=new AlternativePanel(h,(Alternative) (h.getAlternatives()).get(0),this);
	    this.cp=new CriteriumPanel(h.getGoal(),h,this);
	    this.rp=new Rightpanel(h,cp,ap,this);
	    this.asp=new AlternativesPanel(h,this);
	    this.csp=new CriteriaPanel(h,this);
	    this.lp=new Leftpanel(h,csp,asp,this);    

	    // construct the SpliPane = ContentPan        
	    jsp = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,lp,rp);

	    jsp.setOneTouchExpandable(true);
	    jsp.setDividerLocation(150);
	    //aoContent.removeAll();
	  
	    setContentPane(jsp);
	    // Causes this Window to be sized to fit the preferred size 
	    // and layouts of its subcomponents
	    this.pack();
	    validate();	
	}
	
	// JAHP methods
	  /**
	   * <code>actionPerformed</code> method to handle an action event.
	   *
	   * @param event an <code>ActionEvent</code> value
	   */
	  public void actionPerformed(ActionEvent event){
	    Object source = event.getSource();
	    if (source == miLoad) { //LOAD
	      load();
	    } else if (source == miSave) {//SAVE
	      save();
	    } else if (source == miQuit) {//QUIT
	      System.exit(0);
	    } else if (source == miAbout) {//ABOUT
	      ShowAbout();
	    }
	  }
	
	  /*
	   *Method to show a new Criterium
	   */
	  public void updateSHOWCRITERIUM(Criterium c){
	    current_criterium=c;
	    jsp.remove(rp);
	    rp.updateSHOWCRITERIUM(c);
	    jsp.setRightComponent(rp);
	  }

	  /*
	   *Method to show a new Alternative
	   */
	  public void updateSHOWALTERNATIVE(Alternative alt){
	    current_alternative=alt;
	    jsp.remove(rp);
	    rp.updateSHOWALTERNATIVE(alt);
	    jsp.setRightComponent(rp);
	  }

	  /*
	   *Method to show when a new Alternative is added
	   */
	  public void updateafteraddALTERNATIVE(Alternative alt){
	    jsp.remove(rp);
	    rp.updateafteraddALTERNATIVE(alt);
	    jsp.setRightComponent(rp);
	    if (current_criterium.isLl()) updateSHOWCRITERIUM(current_criterium);
	  }

	  /*
	   *Method to show when a new Alternative is deleted
	   */  
	  public void updateafterdelALTERNATIVE(){
	    jsp.remove(rp);
	    rp.updateafterdelALTERNATIVE();
	    jsp.setRightComponent(rp);
	    if (current_criterium.isLl()) updateSHOWCRITERIUM(current_criterium);
	  }

	  /*
	   *Method to show when a new Criterium is added
	   */
	  public void updateafteraddCRITERIUM(Criterium c){
	    current_criterium=c;
	    jsp.remove(rp);
	    rp.updateafteraddCRITERIUM(c);
	    jsp.setRightComponent(rp);
	  }

	  /*
	   *Method to show when a new Criterium is deleted
	   */
	  public void updateafterdelCRITERIUM(){
	    jsp.remove(rp);
	    rp.updateafterdelCRITERIUM();
	    jsp.setRightComponent(rp);
	  }

	  /*
	   *Method to show a new Alternative
	   */
	  public void updateALTERNATIVE(){
	    //jsp.remove(lp);
	    lp.updateALTERNATIVE();
	    //jsp.setLeftComponent(lp);
	    //Systemout.println("JAHP update alt");
	   
	  }


	  /*
	   *Method to show when a  Criterium is modified
	   */
	  public void updateaftermodifyCRITERIUM(){
	    updateSHOWCRITERIUM(current_criterium);
	  }

	  /*
	   *Method to show when an Alternative is modified
	   */
	  public void updateaftermodifyALTERNATIVE(){
	    if (current_criterium.isLl()) updateSHOWCRITERIUM(current_criterium);
	  }
	  
	  /**
	   * Describe <code>getPreferredSize</code> method here.
	   *
	   * @return a <code>Dimension</code> value
	   * @see  <code>Container</code>
	   */
	  public Dimension getPreferredSize(){
	    return new Dimension(500,650);   
	  }

	  /**
	   * Describe <code>getMinimumSize</code> method here.
	   *
	   * @return a <code>Dimension</code> value
	   * @see  <code>Container</code>
	   */
	  public Dimension getMinimumSize(){
	    return new Dimension(400,550);  
	  }


	


	  /**
	   * <code>ShowAbout</code> method to show a dialog frame (About...).
	   *
	   */
	  void ShowAbout() {
		 try {
			 (new About(this,file_mail,imageFile1)).setVisible(true);
		 }catch(IOException e){
		 //do something with e... log, perhaps rethrow etc.
		 }
	  }


	  /**
	   * <code>save</code> method to show a save dialog frame.
	   *
	   */
	  void save() {
	      JFileChooser JFC = new JFileChooser(home);
	      // try to add a filter
	      FileFilter filter = new FileNameExtensionFilter("JAHP hierarchy","ahp");
	      //FileFilter filter = new FileFilter();
	      //filter.addExtension("ahp");
	      //filter.setDescription("JAHP hierarchy");
	      JFC.setFileFilter(filter);
	      int returnVal = JFC.showSaveDialog(this);
	      if(returnVal == JFileChooser.APPROVE_OPTION) {
		this.save(JFC.getSelectedFile());
	      }
	      
	    }

	  /**
	   * <code>load</code> method to show a load dialog frame.
	   *
	   */
	  void load() {
	      JFileChooser JFC = new JFileChooser(home);
	      FileFilter filter = new FileNameExtensionFilter("JAHP hierarchy","ahp");
	      //FileFilter filter = new FileFilter();
	      //filter.addExtension("ahp");
	      //filter.setDescription("JAHP hierarchy");
	      JFC.setFileFilter(filter);
	      int returnVal = JFC.showOpenDialog(this);
		if(returnVal == JFileChooser.APPROVE_OPTION) {
		    this.load(JFC.getSelectedFile());
		}
		
	    }


	  /**
	   * Describe <code>load</code> method to load a new node
	   *
	   * @param f a <code>java.io.File</code> value
	   */
	  public void load(java.io.File f){
	    
	    try{
	      FileInputStream fis =new FileInputStream(f);
	      ObjectInputStream o = new ObjectInputStream(fis);	
	      this.h = (Hierarchy)o.readObject();
	      o.close();
	      fis.close();
	    } catch (EOFException eofe) {
	    } catch (IOException ioe) {
	      System.err.println(ioe);
	    } catch (ClassNotFoundException cnfe) {
	      System.err.println(cnfe);
	    }


	    jsp.remove(lp);
	    jsp.remove(rp);

	    current_alternative=(Alternative)h.getAlternatives().get(0);
	    current_criterium=h.getGoal();
	    this.ap=new AlternativePanel(h,(Alternative) (Alternative) (h.getAlternatives()).get(0),this);
	    this.cp=new CriteriumPanel(h.getGoal(),h,this);
	    this.rp=new Rightpanel(h,cp,ap,this);
	    this.asp=new AlternativesPanel(h,this);
	    this.csp=new CriteriaPanel(h,this);
	    this.lp=new Leftpanel(h,csp,asp,this);    

	    // construct the SpliPane = ContentPan        
	    jsp.setLeftComponent(lp);
	    jsp.setRightComponent(rp);
	    
	    
	  }


	  /**
	   *  <code>save</code> method to save a hierarchy
	   *
	   * @param f a <code>java.io.File</code> value
	   */
	  public void save(java.io.File f){
	    try{
	      FileOutputStream fos = new FileOutputStream(f); 
	      ObjectOutputStream o = new ObjectOutputStream(fos);
	      o.writeObject(this.h);
	      o.close();
	      fos.close();
	    } catch (IOException ioe) {
	      System.err.println(ioe);
	    }
	  }

	  
	public int getColumnCount() {
		return colNames.size();
	}

	public String getColumnName(int arg0) {
		return colNames.get(arg0);
	}

	public int getRowCount() {
		return all_options.size();
	}
	

	// this frame will prompt the user for info on goals, criteria and selected options
	public void firstInput(){
		// 
		aoContent.removeAll();
		
		ActionListener al = EventHandler.create(ActionListener.class, this, "UPDATE");
		// create a top panel where the user can input goal information
		JPanel top = new JPanel();
		top.setLayout(new BoxLayout(top, BoxLayout.PAGE_AXIS));
		top.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		JLabel instr = new JLabel("Set AHP goal; then press ENTER");
		top.add(instr);
		instr.setToolTipText("Following hierarchical ordering (goals->criteria->alternatives) decision goal is entered first. It can be edited later.");

		// enter the goal and click a button
		// write this in a text field
		goal.setText(goalString);
		goal.setBackground(Color.LIGHT_GRAY);
		goal.setOpaque(true);
		goal.setMinimumSize(new Dimension(getSize().width/3, 20));
    	goal.setMaximumSize(new Dimension(getSize().width/2, 40));
    
    	//final JTextField jtf = new JTextField("Please enter the criterion c"+ uniqueID);
    	
		//jtf.setMinimumSize(new Dimension(getSize().width/3, 20));
    	//jtf.setMaximumSize(new Dimension(getSize().width/2, 60));
    	
    	goal.addActionListener(
			    new ActionListener() {
			        public void actionPerformed(ActionEvent e) {
			            // THIS CODE IS EXECUTED WHEN RETURN IS TYPED
			        	goalString = goal.getText();	
			        	System.out.println("Added goal: " + goal.getText());
			        	
			        }
			    }
			);	
    	
    	//goal.addActionListener(al);
    	goal.setAlignmentX(Component.CENTER_ALIGNMENT);
    	
		top.add(goal);
		// TODO
		
		top.setAlignmentX(Component.CENTER_ALIGNMENT);
		top.setPreferredSize(new Dimension(this.getSize().width/2, 50));
		
		aoContent.add(top, BorderLayout.PAGE_START);
		
	
		
		
    	// create a centre panel for the criteria
		JPanel centre = new JPanel();
		centre.setLayout(new BoxLayout(centre, BoxLayout.PAGE_AXIS));
		centre.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		centre.setPreferredSize(new Dimension(this.getSize().width/2, 300));
		
		// east panel will have remove buttons, corresponding to each textfield
		// plus possible confirm button
    	JPanel east = new JPanel();
    	// possible confirm button placed below delete buttons in east panel
		 
    	JButton addCur = new JButton("Confirm");
    
    	
		JLabel instrC = new JLabel("Criteria: enter text and then press ENTER");
		centre.add(instrC);
		instrC.setToolTipText("Criteria can be edited in the textbox, or can be deleted using the buttons on the right.");

		// add an instruction to east panel
		JLabel instrE = new JLabel("Delete");
		east.add(instrE);
		/*
		// display each criteria in its text field
		for (int i=0; i< numCriteria-1; i++ ){		
			centre.add(criteria.get(i));
			centre.add(Box.createRigidArea(new Dimension(0,10)));
			
		}*/
		
		// combined action listener so that when any button is pressed
		// everything is updated
		
		
		// Listen for action events, which are fired when the user hits RETURN
		// on the textfield
		/*
		jtf.addActionListener(
			    new ActionListener() {
			        public void actionPerformed(ActionEvent e) {
			            // THIS CODE IS EXECUTED WHEN RETURN IS TYPED
			         	JTextField myJtf = new JTextField (jtf.getText());
			        	myJtf.setMinimumSize(new Dimension(getSize().width/3, 20));
			        	myJtf.setMaximumSize(new Dimension(getSize().width/2, 60));
			        	
			        	criteria.add(myJtf);
			        	System.out.println("Added criterion: " + jtf.getText());
			        	
			        	// incase of change in the goalstring
			        	goalString = goal.getText();
			        	status = "added";
			        	firstInput();
			        }
			    }
			);	
		// TODO the same code is executed on mouseclick
		//jtf.addActionListener(al);
		
		// add this field
		centre.add(jtf);
	*//*
			// put an add criterion button below the latest text field
			// this button does not increase the criteria but allows a new jtf
			JButton addCr = new JButton("Add Criterion");
			centre.add(addCr);
			addCr.addActionListener(
    			    new ActionListener() {
    			        public void actionPerformed(ActionEvent e) {
    			            // THIS CODE IS EXECUTED WHEN RETURN IS TYPED
    			        	// or when mouse button clicked
    			        	
    			        	// In case the user didn't hit ENTER in ANY text field
    				    	// set it and add it to arraylist
    			        	for (JTextField j2: criteria){
    			        		int curindex = criteria.indexOf(j2); 
    				    		JTextField myJtf = new JTextField (j2.getText());
    				    		criteria.set(curindex, myJtf);
    				        	System.out.println("Replaced criterion: " + myJtf.getText());
    				    	}
    				        criteria.remove(delindex);
    			        	
    				    	
    			        	//int index = buttons.indexOf(delCr);
    			        	//buttons.remove(index);
    			        	//criteria.remove(index);
    			        	
    			        	// incase of change in the goalstring
    			        	goalString = goal.getText();
    			        	System.out.println("Deleted criterion");
    			        	status = "deleted";
    			        	firstInput();	
    			        }
    			    }
    		);*/
		
			if (criteria.size()==0) {
				JTextField first = new JTextField("Please enter the criterion c"+ uniqueID);
				first.setMinimumSize(new Dimension(getSize().width/3, 20));
		    	first.setMaximumSize(new Dimension(getSize().width/2, 60));
				criteria.add(first);
			}
			
			// redux - add all of textfields to centre panel
			// TODO no need to redo listeners (they still have them) ?
			for (JTextField crtf: criteria){	
				centre.add(crtf);
				centre.add(Box.createRigidArea(new Dimension(0,10)));	
			}
			// in this case, create a new text box and listener on the box
			if (status =="added"){
				uniqueID++;
				final JTextField jtf = new JTextField("Please enter the criterion c"+ uniqueID);
		    	
				jtf.setMinimumSize(new Dimension(getSize().width/3, 20));
		    	jtf.setMaximumSize(new Dimension(getSize().width/2, 60));
				centre.add(jtf);
				jtf.setToolTipText("Type new criterion here; please be as specific as possible. The text can be edited later.");

				// add a listener to this jtf
				
				// Listen for action events, which are fired when the user hits RETURN
				// on the textfield
				jtf.addActionListener(
					    new ActionListener() {
					        public void actionPerformed(ActionEvent e) {
					            // THIS CODE IS EXECUTED WHEN RETURN IS TYPED
					         	JTextField myJtf = new JTextField (jtf.getText());
					        	myJtf.setMinimumSize(new Dimension(getSize().width/3, 20));
					        	myJtf.setMaximumSize(new Dimension(getSize().width/2, 60));
					        	
					        	criteria.add(myJtf);
					        	System.out.println("Added criterion: " + jtf.getText());
					        	
					        	// incase of change in the goalstring
					        	goalString = goal.getText();
					        	status = "confirmed"; // return also confirms
					        	firstInput();
					        }
					   }
				);	
				
				// if we just requested (added) a new criterion, there should be a button
				// to go with the current field, to confirm it
		    	// (which is equivalent to pressing ENTER in the textfield)
		    	// Put this button below delete buttons in east panel
			 
	    		//addCur = new JButton("Confirm");
	    		addCur.setMinimumSize(new Dimension(getSize().width/3, 20));
	    		addCur.setMaximumSize(new Dimension(getSize().width/2, 60));
	    		// add action listener
	    		addCur.addActionListener(
	    			   new ActionListener() {
	    			      public void actionPerformed(ActionEvent e) {
	    			            // THIS CODE IS EXECUTED WHEN RETURN IS TYPED
	    			        	// or when mouse button clicked
	    			        	JTextField myJtf = new JTextField (jtf.getText());
					        	myJtf.setMinimumSize(new Dimension(getSize().width/3, 20));
					        	myJtf.setMaximumSize(new Dimension(getSize().width/2, 60));
					        	
					        	criteria.add(myJtf);
					        	System.out.println("Added criterion: " + jtf.getText());
					        	
					        	// incase of change in the goalstring
					        	goalString = goal.getText();
					        	status = "confirmed"; // return also confirms
					        	firstInput();
	    			        	
	    			        }
	    			    }
	    		);
	        	// place this button also on the east panel
	    		// east.add(addCur);
				
				
				
			}
			
			// see below - will also have a confirm button in this case
			int spacerHeight=0;
			// finally in the central panel there may be an add criterion button
			if (status != "added"){
				
			
				JButton addCr = new JButton("Add Criterion");
				centre.add(addCr);
				addCr.setToolTipText("As many additional criteria as desired can be added, one by one, using this button.");

				// listen for button click
				addCr.addActionListener(
    			    new ActionListener() {
    			        public void actionPerformed(ActionEvent e) {
    			            // THIS CODE IS EXECUTED WHEN RETURN IS TYPED
    			        	// or when mouse button clicked
    			        	
    			        	// In case the user didn't hit ENTER in ANY text field
    				    	// set it and add it to arraylist
    			        	for (JTextField j2: criteria){
    			        		int curindex = criteria.indexOf(j2); 
    				    		JTextField myJtf = new JTextField (j2.getText());
    				    		myJtf.setMinimumSize(new Dimension(getSize().width/3, 20));
					        	myJtf.setMaximumSize(new Dimension(getSize().width/2, 60));
					        	
    				    		criteria.set(curindex, myJtf);
    				        	System.out.println("checked criterion: " + myJtf.getText());
    				    	}
    				        //criteria.remove(delindex);
    			        	
    				    	
    			        	//int index = buttons.indexOf(delCr);
    			        	//buttons.remove(index);
    			        	//criteria.remove(index);
    			        	
    			        	// incase of change in the goalstring
    			        	goalString = goal.getText();
    			        	System.out.println("New criterion requested");
    			        	status = "added";
    			        	firstInput();	
    			        }
    			    }
				);
				// calculate height of required spacer
				spacerHeight = addCr.getHeight();
				
			}
			
		// east panel will have remove buttons, corresponding to each textfield
		// plus possible confirm button
    	//JPanel east = new JPanel();  // created above
		east.setLayout(new BoxLayout(east, BoxLayout.PAGE_AXIS));
		east.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		// Reset all existing delete buttons. Need to be able to respond to changes in text
		// not sure why the final modifier has to be used
    	for (JTextField jlab: criteria){
    		final int delindex = criteria.indexOf(jlab) + 1; 
    		final JButton delCr = new JButton("Del c"+ delindex);
    		//jtf.setMaximumSize(new Dimension(getSize().width/4, 20));
    		delCr.setMinimumSize(new Dimension(getSize().width/3, 20));
        	delCr.setMaximumSize(new Dimension(getSize().width/2, 60));
    		
        	delCr.addActionListener(
    			    new ActionListener() {
    			        public void actionPerformed(ActionEvent e) {
    			            // THIS CODE IS EXECUTED WHEN RETURN IS TYPED
    			        	// or when mouse button clicked
    			        	
    			        	// In case the user didn't hit ENTER in ANY text field
    				    	// set it and add it to arraylist
    			        	for (JTextField j2: criteria){
    			        		int curindex = criteria.indexOf(j2); 
    				    		JTextField myJtf = new JTextField (j2.getText());
    				    		myJtf.setMinimumSize(new Dimension(getSize().width/3, 20));
					        	myJtf.setMaximumSize(new Dimension(getSize().width/2, 60));
					        	
    				    		criteria.set(curindex, myJtf);
    				        	System.out.println("Replaced criterion: " + myJtf.getText());
    				    	}
    				        criteria.remove(delindex-1);
    			        	
    				    	
    			        	
    			        	
    			        	// incase of change in the goalstring
    			        	goalString = goal.getText();
    			        	System.out.println("Deleted criterion");
    			        	status = "deleted";
    			        	firstInput();	
    			        }
    			    }
    		);
        	// finally add the delete button and some spacing
        	east.add(delCr);
			east.add(Box.createRigidArea(new Dimension(0,10)));
    	}
    	
    	// lastly add the confirm button
    	if (status =="added") east.add(addCur);
    	// else stick a spacer in east
    	else east.add(Box.createVerticalStrut(spacerHeight));
    	
		// if we just added a criterion, there should be a button to confirm it
    	// (which is equivalent to pressing ENTER)
    	// Put this button below delete buttons in east panel
    	
		/*
		
		
		
		
    	
		final JButton delCr = new JButton("Del c"+ numCriteria);
		//jtf.setMaximumSize(new Dimension(getSize().width/4, 20));
		delCr.setMinimumSize(new Dimension(getSize().width/3, 20));
    	delCr.setMaximumSize(new Dimension(getSize().width/2, 60));
		
    	delCr.addActionListener(
			    new ActionListener() {
			        public void actionPerformed(ActionEvent e) {
			            // THIS CODE IS EXECUTED WHEN RETURN IS TYPED
			        	// or when mouse button clicked
			        	
			        	// in case the user didn't hit ENTER in the new text field
				    	// set it and add it to arraylist
				    	if (criteria.size()<numCriteria){
				    		uniqueID++;
				    		JTextField myJtf = new JTextField (jtf.getText());
				    		myJtf.setMinimumSize(new Dimension(getSize().width/3, 20));
				    		myJtf.setMaximumSize(new Dimension(getSize().width/2, 60));
				    		criteria.add(myJtf);
				        	System.out.println("Added criterion: " + jtf.getText());
				    	}
				        else // check and update all current criteria fields
				        {
				        	//criteria.set(numCriteria-1,myJtf);        
				        	//System.out.println("Set criteria: " + jtf.getText());
				        }
			        	
				    	
			        	int index = buttons.indexOf(delCr);
			        	buttons.remove(index);
			        	criteria.remove(index);
			        	
			        	// incase of change in the goalstring
			        	goalString = goal.getText();
			        	System.out.println("Deleted criterion");
			        	removeCriterion();	
			        }
			    }
		);
    	//jb.addActionListener(al);
    	
		// create an east panel for the delete buttons
		JPanel east = new JPanel();
		east.setLayout(new BoxLayout(east, BoxLayout.PAGE_AXIS));
		east.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		//east.setPreferredSize(new Dimension(this.getSize().width/2, 300));
		
		
		// place the delete buttons in the east panel
		for (int i=0; i< numCriteria-1; i++ ){
			buttons.get(i).setText("Del c" + (i + 1));
			east.add(buttons.get(i));
			east.add(Box.createRigidArea(new Dimension(0,10)));
			//System.out.println("added button" + i + " to panel");
		}
		
		// add the button for the current criteria
		buttons.add(delCr);
		//System.out.println("added new button to panel");
		// place this button also on the east panel
		east.add(delCr);
		
	
		//  add mouse listeners. commands are in the associated methods
		// addCr.addActionListener((ActionListener)EventHandler.create(ActionListener.class, this, "addCriterion"));
		addCr.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// carried out when button clicked
				numCriteria++;
				uniqueID++;
				// in case of change in the goalstring
		    	goalString = goal.getText();
		    	
		    	// in case the user didn't hit ENTER in the new text field
		    	// set it and add it to arraylist
		    	if (criteria.size()<numCriteria-1){
		    		JTextField myJtf = new JTextField (jtf.getText());
		    		myJtf.setMinimumSize(new Dimension(getSize().width/3, 20));
		    		myJtf.setMaximumSize(new Dimension(getSize().width/2, 60));
		    		criteria.add(myJtf);
		        	System.out.println("Added criterion: " + jtf.getText());
		    	}
		        else // check and update all current criteria fields
		        {
		        	//criteria.set(numCriteria-1,myJtf);        
		        	//System.out.println("Set criteria: " + jtf.getText());
		        }
		        
		    	
		    	// back to the input screen
				firstInput();
				
			 }// end actionPerformed
		});
		*/
		aoContent.add(centre, BorderLayout.CENTER);
		aoContent.add(east, BorderLayout.EAST);
		
		
		// create bottom panel where options can be chosen
		final JPanel bottom = new JPanel();
		
		bottom.setLayout(new BoxLayout(bottom, BoxLayout.PAGE_AXIS));
		bottom.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		JLabel instrO = new JLabel("Alternatives: select multiple options with CTRL + click");
		instrO.setToolTipText("Select all, or a subset of, predefined options from the scrolling list below by holding CTRL button while clicking.");

		//JLabel instrG = new JLabel("Goal, (sub-)criteria, alternatives can be added/edited later");
		
		bottom.add(instrO);
		// make Jlist corresponding to the AO model, descriptors column
		//aol = new AOListModel(ixm);
		//aol.setColumnOfModel(0);
		
		//create JList from the model
		final JList list = new JList(aol);
		// create scrollpane for the list
		JScrollPane pane = new JScrollPane(list);
		
		bottom.setPreferredSize(new Dimension(getSize().width/2, 140));
	    
		// create a button panel at the bottom with select all, get selected and start buttons in a row
		JPanel buttonP = new JPanel(); 				// create panel with horizontal flow layout by default
		// create buttons with associated action listener
		final JButton btnAll = new JButton("Select All");
		btnAll.setToolTipText("Click here to select all the options in the scrolling list.");
		btnAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// add all the indices to the list
				int [] indices = new int [length];
				for (int ii=0; ii< length; ii++) {indices[ii]= ii;}
				list.setSelectedIndices(indices);
				// incase of change in the goalstring
			    goalString = goal.getText();
			 }// end actionPerformed
		});
		
		final JButton btnGet = new JButton("Get Selected");
		btnGet.setToolTipText("Click here to check the selection of adaptation options.");
		
		btnGet.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// carried out when button clicked
			           String selectedElem = "";
			           int selectedIndices[] = list.getSelectedIndices();
			           for (int j = 0; j < selectedIndices.length; j++) {
			               String elem =
			                       (String) list.getModel().getElementAt(selectedIndices[j]);
			               selectedElem += "\n" + elem;
			 
			           }
			           // set the list of indices
			           listIndices = selectedIndices;
			           System.out.println("number of options selected: " + listIndices.length);
			           JOptionPane.showMessageDialog(bottom,  "You've selected:" + selectedElem);
			           // incase of change in the goalstring
			        	goalString = goal.getText();
			 }// end actionPerformed
		});
		
		final JButton btnStart = new JButton("Start AHP");
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// activated when startAHP button is pressed
		   	  	// incase of change in the goalstring
		   	  	goalString = goal.getText();
		   	  	
		   	  	// incase of change in options
		   	  	//String selectedElem = "";
		   	  	int selectedIndices[] = list.getSelectedIndices();
		   	  	
		   	  	// pop-up box
		   	  	if (selectedIndices.length == 0){
		   	  		JOptionPane.showMessageDialog(bottom, "You didn't select any options!");
		   	  		firstInput();
		   	  	}
		   	  	else 
		   	  	{
		   	  		/*for (int j = 0; j < selectedIndices.length; j++) {
		   	  			String elem =
		   	  				(String) list.getModel().getElementAt(selectedIndices[j]);
		   	  			selectedElem += "\n" + elem;
	 
		   	  		}*/
		   	  		// set the list of indices
		   	  		listIndices = selectedIndices;

		   	  		// construct a Hierarchy using null hierarchy
		   	  		OwnTest ot = new OwnTest();
		   	  		h = ot.nullHierarchy(goalString);
		   	  	    
		   	  		// create AO model from ixm
		   	  		h.setAdaptationOptionsModel(ixm);
		   	  
		   	  		for (int i : listIndices){
		   	  			String elem =
		   	  				(String) list.getModel().getElementAt(i);
		   	  			System.out.println("Adding " + elem);
		   	  			//Object elt = aol.getElementAt(i);
		   	  			Alternative alt = new Alternative ();
		   	  			//alt.setName(elt.toString());
		   	  			alt.setName(elem);
		   	  			h.addAlternative(alt);
		   	  		}
		   	  
		   	  		//build and add subcriteria 
		   	  		for (JTextField j: criteria){
		   	  			Criterium c2=new Criterium();
		   	  			c2.setName(j.getText());
		   	  			h.addSubcriterium(h.getGoal(),c2,h.getAlternatives(),h.getNb_alternatives());
		   	  		}
		   	    
		   	  		// start JAHP engine (if one or more option was selected)
		   	  		startJAHP();
		   	  		
		   	  	}
		   	  
			 }// end actionPerformed
		});
		//btnStart.addActionListener((ActionListener)EventHandler.create(ActionListener.class, this, "start"));
		
		bottom.add(pane);
		buttonP.add(btnAll);
		buttonP.add(btnGet);
		buttonP.add(btnStart);
		bottom.add(buttonP);
		
		//bottom.add(btnAll);
		//bottom.add(btnGet);
		//bottom.add(btnStart);
		//bottom.add(instrG);
		aoContent.add(bottom, BorderLayout.PAGE_END);

		
		validate();	
	}
	
	/*
	public void removeCriterion(){
		// 
		aoContent.removeAll();
		
		ActionListener al = EventHandler.create(ActionListener.class, this, "UPDATE");
		// create a top panel where the user can input goal information
		JPanel top = new JPanel();
		top.setLayout(new BoxLayout(top, BoxLayout.PAGE_AXIS));
		top.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		JLabel instr = new JLabel("AHP goal: enter text and then press ENTER");
		top.add(instr);
		// enter the goal and click a button
		// write this in a text field
		goal.setText(goalString);
		goal.setBackground(Color.GRAY);
		goal.setOpaque(true);
		goal.setMinimumSize(new Dimension(getSize().width/3, 20));
    	goal.setMaximumSize(new Dimension(getSize().width/2, 40));
    	//top.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
    	
    	goal.addActionListener(
			    new ActionListener() {
			        public void actionPerformed(ActionEvent e) {
			            // THIS CODE IS EXECUTED WHEN RETURN IS TYPED
			        	goalString = goal.getText();	
			        	
			        }
			    }
			);	
    	
    	//goal.addActionListener(al);
    	
		top.add(goal);
		top.setAlignmentX(Component.CENTER_ALIGNMENT);
		top.setPreferredSize(new Dimension(this.getSize().width/2, 50));
		
		aoContent.add(top, BorderLayout.PAGE_START);
		
    	// create a centre panel for the criteria
		JPanel centre = new JPanel();
		centre.setLayout(new BoxLayout(centre, BoxLayout.PAGE_AXIS));
		centre.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		JLabel instrC = new JLabel("Criteria: enter text and then press ENTER");
		centre.add(instrC);
		// update the numCriteria and add textfields according to current numCriteria
		numCriteria = criteria.size();
		// display each criteria in its text field
		for (int i=0; i< numCriteria; i++ ){		
			centre.add(criteria.get(i));
			centre.add(Box.createRigidArea(new Dimension(0,10)));
			
		}
		
		// combined action listener so that when any button is pressed
		// everything is updated
		
		
		// put an add criterion button below the latest text field
		JButton addCr = new JButton("Add Criterion");
		centre.add(addCr);
		
		
    	
		// create an east panel for the delete buttons
		JPanel east = new JPanel();
		east.setLayout(new BoxLayout(east, BoxLayout.PAGE_AXIS));
		east.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		
		// place the delete buttons in the east panel
		// (they still have their listeners attached)
		for (int i=0; i< numCriteria; i++ ){
			buttons.get(i).setText("Del c" + (i + 1));
			east.add(buttons.get(i));
			east.add(Box.createRigidArea(new Dimension(0,10)));
			//System.out.println("added button" + i + " to panel");
		}
		
		// no new button (and no textfield) is added after removal
		// buttons.add(new JButton (jb.getText()));
		// add listener to the current button
		//jb.addActionListener((ActionListener)EventHandler.create(ActionListener.class, this, "REMOVE"));
		
		
		
		//  add mouse listeners. commands are in the associated methods
		//addCr.addActionListener((ActionListener)EventHandler.create(ActionListener.class, this, "addCriterion"));
		//delCr.addActionListener((ActionListener)EventHandler.create(ActionListener.class, this, "REMOVE"));
		addCr.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// carried out when button clicked
				numCriteria++;
				uniqueID++;
				// in case of change in the goalstring
		    	goalString = goal.getText();
		    	
		    	// because just removed a criterion, this will not occur
		    	// in case the user didn't hit ENTER in the new text field
		    	// set it and add it to arraylist
		    	if (criteria.size()<numCriteria-1){
		    		JTextField myJtf = new JTextField (jtf.getText());
		    		myJtf.setMinimumSize(new Dimension(getSize().width/3, 20));
		    		myJtf.setMaximumSize(new Dimension(getSize().width/2, 60));
		    		criteria.add(myJtf);
		        	System.out.println("Added criterion: " + jtf.getText());
		    	}
		        else // check and update all current criteria fields
		        {
		        	//criteria.set(numCriteria-1,myJtf);        
		        	//System.out.println("Set criteria: " + jtf.getText());
		        }
		        
		    	
		    	// back to the input screen
				firstInput();
				
			 }// end actionPerformed
		});
		
		aoContent.add(centre, BorderLayout.CENTER);
		aoContent.add(east, BorderLayout.EAST);
		
		// create bottom panel where options can be chosen
		final JPanel bottom = new JPanel();
		bottom.setLayout(new BoxLayout(bottom, BoxLayout.PAGE_AXIS));
		bottom.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		JLabel instrO = new JLabel("Alternatives: select multiple options with CTRL + click");
		//JLabel instrG = new JLabel("Goal, (sub-)criteria, alternatives can be added/edited later");
		
		bottom.add(instrO);
		// make Jlist corresponding to the AO model, descriptors column
		//aol = new AOListModel(ixm);
		//aol.setColumnOfModel(0);
		
		//create JList from the model
		final JList list = new JList(aol);
		JScrollPane pane = new JScrollPane(list);
		//goal.setMinimumSize(new Dimension(getSize().width/3, 60));
    	//goal.setMaximumSize(new Dimension(getSize().width/2, 100));
    	
		bottom.setPreferredSize(new Dimension(getSize().width/2, 140));
		      
		// create a button and add action listener
		final JButton btnGet = new JButton("Get Selected");
		final JButton btnStart = new JButton("Start AHP");
		
		btnGet.addActionListener(new ActionListener() {
		     public void actionPerformed(ActionEvent e) {
			           String selectedElem = "";
			           int selectedIndices[] = list.getSelectedIndices();
			           for (int j = 0; j < selectedIndices.length; j++) {
			               String elem =
			                       (String) list.getModel().getElementAt(selectedIndices[j]);
			               selectedElem += "\n" + elem;
			 
			           }
			           // set the list of indices
			           listIndices = selectedIndices;
			           System.out.println("number of options selected: " + listIndices.length);
			           JOptionPane.showMessageDialog(bottom,
			                        "You've selected:" + selectedElem);
			           // incase of change in the goalstring
			        	goalString = goal.getText();
			 }// end actionPerformed
		});
	
		//btnStart.addActionListener((ActionListener)EventHandler.create(ActionListener.class, this, "start"));
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// activated when startAHP button is pressed
		   	  	// incase of change in the goalstring
		   	  	goalString = goal.getText();
		   	  	
		   	  	// incase of change in options
		   	  	String selectedElem = "";
		   	  	int selectedIndices[] = list.getSelectedIndices();
		   	  	for (int j = 0; j < selectedIndices.length; j++) {
	               String elem =
	                       (String) list.getModel().getElementAt(selectedIndices[j]);
	               selectedElem += "\n" + elem;
	 
		   	  	}
		   	  	// set the list of indices
		   	  	listIndices = selectedIndices;

		   	  	// construct a Hierarchy using null hierarchy
		   	  	OwnTest ot = new OwnTest();
		   	  	h = ot.nullHierarchy(goalString);
		   	  
		   	  	for (int i : listIndices){
		   	  		Object elt = aol.getElementAt(i);
		   	  		Alternative alt = new Alternative ();
		   	  		alt.setName(elt.toString());
		   	  		h.addAlternative(alt);
		   	  	}
		   	  
		   	  	//build and add subcriteria 
		   	  	for (JTextField j: criteria){
		   	  		Criterium c2=new Criterium();
		   	  		c2.setName(j.getText());
		   	  		h.addSubcriterium(h.getGoal(),c2,h.getAlternatives(),h.getNb_alternatives());
		   	  	}
		   	  
		   	  	// start JAHP engine
		   	  	startJAHP();
		   	  	//this.pack();
		   	  	//this.setVisible(true);
		   	    
		   	  	// the results should be passed to adxFrame and results frame  
		   	  	//setVisible(false);
			

		   	  
			 }// end actionPerformed
		});
		bottom.add(pane);
		bottom.add(btnGet);
		bottom.add(btnStart);
		//bottom.add(instrG);
		aoContent.add(bottom, BorderLayout.PAGE_END);

		validate();	
	}
	
	public void addCriterion(){	
		numCriteria++;
		uniqueID++;
		// incase of change in the goalstring
    	goalString = goal.getText();
    	
    	// back to the input screen
		firstInput();
	}*/
	
  public void update(){
    	
    	//addCr.addActionListener((ActionListener)EventHandler.create(ActionListener.class, this, "ADD"));
	  	System.out.println("al triggered ");
		//numCriteria++;
		//criteria.remove(numCriteria-1);
		//firstInput();
	}
/*
  public void start(){
	  // activated when startAHP button is pressed
	  // TODO need to set the comments field appropriately
	  
	  // incase of change in the goalstring
	  goalString = goal.getText();
	  
	 
  	
	  // construct a Hierarchy using null hierarchy
	  OwnTest ot = new OwnTest();
	  Hierarchy h = ot.nullHierarchy(goalString);
	  
	  // create a 
	  //Heirarchy h = ot.adxHierarchy(goalString, criteriaStrings, alternatives)
	  for (int i : listIndices){
		  
		  Object elt = aol.getElementAt(i);
		  Alternative alt = new Alternative ();
		  alt.setName(elt.toString());
		  h.addAlternative(alt);
	  }
	  
	  //build and add subcriteria 
	  for (JTextField j: criteria){
		  //Every criterium should contain alternatives...
		    //It's easier to addSubcriterium
		    //and to calculate J(a_i|c_j); J* I I* \pi
		    //I()
	  
		  Criterium c2=new Criterium();
		  c2.setName(j.getText());
		  h.addSubcriterium(h.getGoal(),c2,h.getAlternatives(),h.getNb_alternatives());
	  }
	  
	  // thread should be passed to JAHP engine
	  JAHP mainFrame = new JAHP(h);
	  mainFrame.pack();
	  mainFrame.setVisible(true);
	    
	  // the results should be passed to adxFrame and results frame  
	  setVisible(false);
	  //adxFrame.votingResult(pollList);
  } */
	/*
	// activated when user clicks on update button
	public void update(){
		// reset desired votes on 'update' click
		desiredvotes= (Long) (first_field.getValue());
		//System.out.println("desired votes = " + desiredvotes);
		
		// change what is in the arraylist if necessary
		// locate entries where the rank number is greater than current desired number of votes
		// iterate through and set them back to a 'blank' entry
		
		for (String si: list){
			if (si!=""){ 
				if (Long.parseLong(si)>desiredvotes){
					si = "";
				}
			}
		 }
	
		// set the number of ranks equal to the desired/ default votes - number of votes allowed each voter
		//ranks = desiredvotes;
		
		if(voterID.getText() != list.getNameString()) list.setNameString(voterID.getText());
		
		showAdaptationOption();
	}
	
	
	
	
	/*
	// this is called when the action event occurs on the "Finalise" button on the xml explorer
	public void FINALIZE(){
		// update the voter namestring if necessary
		if(voterID.getText() != list.getNameString()) list.setNameString(voterID.getText());
		
		// add the current vote to the poll and reset several variables
		// Use a copy constructor rather than cloning
		Vote finalVote = new Vote (list, "");
		finalVote.setNameString(list.getNameString());
		
		//pollList.add(finalVote);
		/*
		// after the first vote has been finalised it should no longer be possible to modify the ranking parameters
		if (voter==1){
			finalize = true;
			if (desiredvotes==0) ranks = defaultvotes;
			else ranks = desiredvotes;
			setAwardsByCombo();   // TODO the method of points awards could be done in later options screen?
		}
		*/
		
		//list.print();					// try to print the vote
		// printResultOfVoter(finalVote);
		
		//list.clear();					// clear the  current vote
		//list = new Vote(length, "");
		//voter ++;						// increase the count of votes
		//showing = 0;
	    //showAdaptationOption();
	
	   
	//}
	
	// this is called when the action event occurs on the "Finalise" button on the AHP alternatives panel
	public void COMPUTE(){
		
		
		// Save the result - the options ordered by score
		// the criteria and subcriteria
		// and all of the comparison matrices
		printResultOfAHP(h);
		
		

		// when satisfied with the result call addPreferredOptions method of combined results
		// all pairwise ranked options go the combined results stage 
		// TODO selected ones - selected with CTRL-click
		
		// thread should be passed to the results frame which displays the results
	    setVisible(false);
	    adxFrame.AHPResult(getHierarchy());
	}
	
	
	/**
	   * <code>main</code> method to test this class.
	   * @param Criterium :  command line
	   * 
	   */
	 
	// 1st placed option gets points equal to the number of votes entered in the combo box
	// Other ranked options get uniformly decreasing points
	public void printResultOfAHP(Hierarchy h){
		// h.SortAlternatives(); // check if safe
		/*Vector <Alternative> v = h.getAlternatives();
		
		for(Alternative a: v){
		
			String elt = a.getName();
			if(elt!=""){
				int rank = Integer.parseInt(elt) - 1;   	// rank must take the values 0,1,2 as array arg.
				//System.out.println ("Voter gives " + awards[rank] + " points to option " + i);
			}
			
		}*/
		
	}
	
	public Hierarchy getHierarchy(){
		return h;
	}
	/*
	// would like the results screen to be as responsive as possible to user choices/ modifications
	// so return the pollList object to the results frame 
	public ArrayList<Vote<String>> getPoll(){
		return pollList;
	}
	*/
	// from the results frame the user is able to modify certain parameters eg. ranks, awards and see the effects
}

