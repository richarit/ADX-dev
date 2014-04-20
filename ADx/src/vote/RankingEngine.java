package vote;
import gui.About;
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

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
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

import adt.Hierarchy;

import shell.ADxFrame;

//Typically connections are made from a user interface bean (the event source) to an application logic bean (the target).
import java.beans.EventHandler;

import javax.swing.JFileChooser;
import javax.swing.JComponent;
import javax.swing.filechooser.FileFilter;
// does not use a table model and JTable but creates a frame browser
public class RankingEngine extends JFrame implements ActionListener {
	
	private Poll poll;
	Long defaultvotes; 
	Vote<String> list;					// a vote. For each adaptation option store a string (ranking number)
	boolean finalize = false;			// voter 1 is allowed to set the poll parameters
	int voter = 1;						// maintain a count of the number of voters for titling the window
	
	// Long ranks;					//desired/ default votes - number of votes allowed each voter
	
	// TODO input screen for some things like the weighting for 1st and second, modify this section
	// points awarded for each vote cast could be 2 points for 1st place ranking, 1 point for a second place
	// Long awards []= {2,1};
	// or it could follow the number of votes entered at the beginning of vote 1
	public Long [] awards;
	
	// - weighting of each - mechanism
	JMenu mFile,mHelp; // Menus
	JMenuItem miLoad,miSave,miQuit,miAbout, miUG; //MenuItems
	File home = new File ("/related"); ; //home directory where logos, icons etc could be found
	File user_guide;  // file name
	File file_icon;
	File home_icons; // directory where icons could be found
	File home_example; //directory where examples could be found 
	File default_file; //default file loaded
	File file_mail; //mail icon
	File file_edit; //mail icon
	File imageFile1; //photo author
	 
	private int length;			// number of adaptation options
	private int showing=0;		// current option showing
	
	Container aoContent;
	//Document doc;
	Element root;
	List <Element> all_options; 
	// This provides access to the class that creates the engine and processes the poll upon finalization 
	ADxFrame adxFrame;	
	
	Long desiredvotes = new Long (0);								// number of votes - input provided by the user
	JFormattedTextField first_field = new JFormattedTextField(); 	// formatted field for entering number of votes
	JTextField voterID = new JTextField();							// Voter is able to enter a name that is recorded in AOModel
	
	// GridBag the next most powerful and flexible layout manager after GroupLayout
	GridBagLayout gridbag = new GridBagLayout();
	GridBagConstraints c = new GridBagConstraints();
	//For each component to be added to this container:
	//...Create the component...
	//...Set instance variables in the GridBagConstraints instance...
	// pane.add(theComponent, c);

	
	// need a global variable to be able to access contents of the box   
	JComboBox second_combo;
	
	// colNames is initially set from the DTD but can be expanded in the computational stage.
	// For a dynamic field variable better therefore to use an arraylist rather than static array
	public ArrayList <String> colNames = new ArrayList <String> ();					// initialise colNames
	public ArrayList <Double> columnLengths = new ArrayList <Double> ();
	
	// additional **result fields** might be: who voted for the option (voters), score and overall rank
	// these results should be listed in a table similarly to the AdaptationOptionsModel
	
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
	
	public RankingEngine(Document doc, ADxFrame adx){
		
		super("Voting Engine");
	    
	    // window 
	    addWindowListener(new WindowAdapter() {
		public void windowClosing(WindowEvent e) {
		  System.exit(0);
		}

		public int getRowCount() {
			return all_options.size();
		}
	      });
	    
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
	    miUG = new JMenuItem("User Guide");
	    miUG.addActionListener(this);
	    mHelp.add(miUG);
	    menuBar.add(mHelp);
	    this.setJMenuBar(menuBar);
	    
	  //File DATA
	    home= new File("..");// export JAHP_PATH
	    home_icons=new File(home,"icons");
	    home_example=new File(home,"examples");
	    default_file= new File(home_example,"essai.ahp");
	    file_mail=new File(home_icons,"ComposeMail24.gif");
	    file_edit=new File(home_icons,"Edit24.gif");
	    imageFile1=new File(home_icons,"morge.png");
	    
		this.adxFrame = adx;
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
		
		aoContent = getContentPane();
		//this.doc = doc;
		
		// get the root element, and get a list of its child elements, set the size
		root = doc.getRootElement();
		all_options = root.getChildren();
		length = all_options.size();
		//setTitle("Adaptation options");
		
		// set col names and sizes
		setColNames();
		setColLengths();
		
		// create vote object to hold rank numbers for the current vote and initialise with blank strings
	    list = new Vote(length, "");
	    
	    ///pollList = new ArrayList<Vote<String>>();		// create an arraylist to record all votes	
	    poll = new Poll();	   
	    poll.setAdaptationOptionsModel(doc);
	    // default votes correlated to number of options -take the sqrt
		double sq = Math.sqrt(length);
		defaultvotes = (long) Math.floor(sq); 
		

		
		for (Element option: all_options){
			
			// create vector row to hold data for this option
			int i = all_options.indexOf(option);
			
			for (int j=0; j<colNames.size(); j++){
				Element elt = option.getChild(colNames.get(j));
				//System.out.println(colNames[j]);
				//elt.getTextTrim();
				String str_elt = elt.getTextTrim();
				// TODO add Objects rather than strings to the table data
				//rowdata[i].add(j,str_elt);
			}
			
			// this line seems to set back to null the rowdata
			//tm.insertRow(i, rowdata[i]);
			//addRow(rowdata[i]);
		}
		setupVoterID();  // add action listener to the text field
		
		
		// use a dialog box for setting the number of votes
        if (!finalize) {
        	
    		Object[] possibilities = new Object[length];// = new [] String();
    		for (int i=1; i<=length; i++) {
    			String str = new String("" + i);
    			possibilities[i-1] = str;
    		}
    		
    		// Every dialog is dependent on a Frame component. When that Frame is destroyed, so are its dependent Dialogs. 
    		//  A Dialog can be modal. When a modal Dialog is visible, it blocks user input to all other windows in the program. JOptionPane creates JDialogs that are modal. 
    		file_icon = new File(home,"weADAPT.jpg");
    		//ImageIcon icon =new ImageIcon(file_icon);
    		ImageIcon icon =new ImageIcon(home + "/weADAPT.jpg");
    		
    		
    		String s = (String)JOptionPane.showInputDialog(
    		                    aoContent,
    		                    "Positional voting system :\n"
    		                    + "Options are ranked and receive points based on their ranked position\n"
    		                    + "There are " + length + " options. How many will each voter rank ?",
    		                    "Input for Voting",
    		                    JOptionPane.QUESTION_MESSAGE,
    		                    icon ,
    		                    possibilities,
    		                    "" + defaultvotes);
    		
    		finalize = true;          // set boolean flag for first voter
    		//Long desiredvotes;

    		//If a string was returned, say so.
    		if ((s != null) && (s.length() > 0)) {
    			desiredvotes= (Long) (Long.valueOf(s));
    		}

    		System.out.println("desired votes = " + desiredvotes);
    		
    		// set the number of ranks equal to the desired/ default votes - number of votes allowed each voter
    		poll.setNb_ranks(desiredvotes.intValue());
    		
        }
		// finally, call the show method with the initial value showing = 0
		showAdaptationOption();
	
	}
	
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
	    } else if (source == miUG)  { //user guide
		  help();
	    }
	  }
	  
	  // set up action listener on voterid
	  public void setupVoterID (){
		voterID.setMinimumSize(new Dimension(getSize().width/3, 60));
    	voterID.setMaximumSize(new Dimension(getSize().width/2, 100));
    	
    	// listener inner class on the textfield
    	voterID.addActionListener(
			    new ActionListener() {
			        public void actionPerformed(ActionEvent e) {
			            // THIS CODE IS EXECUTED WHEN RETURN IS TYPED
			        	//voterID.setText(voterID.getText());	 // voterID is the JTextField
			        	list.setNameString(voterID.getText()); // nameString is the stored datum
			    		
			        	System.out.println("Set voter name: " + voterID.getText());
			        }
			    }
			);
	  }
	  

		


	  /**
	   * <code>ShowAbout</code> method to show a dialog frame (About...).
	   *
	   */
	  void ShowAbout() {
		  String str = "Written by Richard Taylor richard.taylor@sei-international.org";

	    (new About(this,file_mail,imageFile1, str)).setVisible(true);
	  }

	  void help() {
		  // TODO open the bundled user guide pdf document (at the correct page ) with default viewer
		  
	  }

	  /**
	   * <code>save</code> method to show a save dialog frame.
	   *
	   */
	  void save() {
	      JFileChooser JFC = new JFileChooser(home);
	      // try to add a filter
	      FileFilter filter = new FileNameExtensionFilter("ADX poll","pol");   
	      
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
	      FileFilter filter = new FileNameExtensionFilter("ADX poll","pol");
	      //filter.addExtension("pol");
	      //filter.setDescription("ADX poll");
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
		  System.out.println("entered load (file)");
	    try{
	      FileInputStream fis =new FileInputStream(f);
	      ObjectInputStream o = new ObjectInputStream(fis);	
	      this.poll = (Poll) o.readObject();
	      // load a particular voter's details ?
	      voter = poll.getNb_votes();                   // reset the current voter number to the final voter
	      Vote finalvote = poll.getVoteByNumber(voter);
	      // voterID = finalvote.getNameString();
	      //System.out.println("the number of voters is: " + poll.getNb_votes() + ", name of voter to load is: " + finalvote.getNameString());
	      //voter ++;						// increase the count of votes
	      // all of these need to be reloaded ?
	      // desiredvotes = new Long (0);				// number of votes - input provided by the user
	  	  //first_field = new JFormattedTextField(); 	// formatted field for entering number of votes
	  	  // voterID.setText(finalvote.getNameString());
	  	  //Vote<String> finalVote = new Vote (list, "");
		
	  	  list = finalvote;	  // set the list, in which the votes are stored, to make it showing
	  	  // still something is causing error on finalize
		  // showing = 0;
		  showAdaptationOption();
		  
	      o.close();
	      fis.close();
	    } catch (EOFException eofe) {
	    } catch (IOException ioe) {
	      System.err.println(ioe);
	    } catch (ClassNotFoundException cnfe) {
	      System.err.println(cnfe);
	    }
	    
	    

/*
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
	    
	   */ 
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
	      o.writeObject(this.poll);
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

	public void showAdaptationOption(){
		// 
		aoContent.removeAll();
		Element show_element = all_options.get(showing);
		
		// TODO pop-up box for poll parameters 
		
		// create a top panel where the information about the voting is held, with grid layout of 4 columns
		JPanel top = new JPanel();
		
		top.setLayout(new GridLayout(0,4));
		top.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
		
		String[] strings;			// strings used in the combo box
		
		// first row shows the suggested number of votes/desired votes
		if (desiredvotes == 0){
			
			strings = new String[defaultvotes.intValue()+1];
			// first string is a 'blank' to signify no rank assigned
			strings [0] = "";
			for (int i=1; i<= defaultvotes.intValue(); i++){
				strings[i] = (Integer.toString(i));
			}
			first_field.setValue(new Long(defaultvotes));
		}
		else {
			first_field.setValue(desiredvotes);
			strings = new String[desiredvotes.intValue()+1];
			// first string is a 'blank' to signify no rank assigned
			strings [0] = "";
			for (int i=1; i<= desiredvotes.intValue(); i++){
				strings[i] = (Integer.toString(i));
			}
		}
		// Top row:
		// add a textfield showing the voter id number and allowing input
		String recName = list.getNameString();
		if (recName=="") voterID.setText("Type NAME Voter-" + voter + ", press ENTER");
		else voterID.setText(recName);
		
		//JLabel voterID = new JLabel("Voter " + voter,  JLabel.CENTER);
		voterID.setBackground(Color.GRAY);
		// TODO make a colour picker that uses enums
		//Color chosen_col = votercol.picker(voter);
		//voterID.setBackground(chosen_col);
		voterID.setOpaque(true);
		top.add(voterID);
		voterID.setToolTipText("1. Type the name of the current voter here and press ENTER");
		// add a prompt for the number of desired votes
		JLabel first_prompt = new JLabel("Enter num. votes req.:");
		//top.add(first_prompt);
		
		// add the entry field for desired votes
		first_field.setSize(20, 20); //  hardcode the size of the entry field
		//top.add(first_field);
		first_field.setToolTipText("1. The number of votes per voter may be adjusted BEFORE the first vote is finalised)");
		// add the update button for number of desired votes to complete the first row
		JButton first_update = new JButton("Update");
		//top.add(first_update);
		//first_update.addActionListener((ActionListener)EventHandler.create(ActionListener.class, this, "update"));
		
		// make these elements not editable/actionable after the first vote
		//if( voter >1 ) {
			first_field.setEnabled(false);
			first_update.setEnabled(false);
		//}
		
		
		
		// Second row
		// On the left put a label requesting the user to set the combo box and click vote button
		// there should be a text box to take an entry with an update button next to it
		JLabel second_voteLabel = new JLabel ("Select a rank, then VOTE: ");
		top.add(second_voteLabel);
		
		// add a combo box with the appropriate ranks based on the number the desired votes
		second_combo = new JComboBox(strings);
		second_combo.setToolTipText("3. Select a rank from the drop-down box and then click the 'Vote' button. These can be changed, by reselecting, BEFORE finalising the vote.");

		// Box will display the current vote if a selection previously made, otherwise it will be blank
		String current = new String();
		if (list.get(showing)!=""){
		//if (hMap.containsKey( (Integer)showing )) {
			current = (String) (String) list.get(showing);
			second_combo.setSelectedItem(current);
		}
		// add the combo box and the button to the second row
		top.add(second_combo);
		JButton second_voteButton = new JButton("Vote");
		second_voteButton.setToolTipText("3. Select a rank from the drop-down box and then click the 'Vote' button. These can be changed, by reselecting, BEFORE finalising the vote.");

		top.add (second_voteButton);
		second_voteButton.addActionListener((ActionListener)EventHandler.create(ActionListener.class, this, "vote"));
		
		// current votes are recorded in a hashtable with the integer rank (value) next to the option ID string (key)
		
		// In a third row will be placed a single label - list of votes already taken
		// TODO A refined program would allow for the case where two (or more) options are ranked equally 
		// with a number of subsequent ranks not available ...
		
		// a concatenated string will inform the user of ranks previously chosen (with duplicates)
		String chosen = new String("");		
		// collection of all values (ie. ranks) - with duplicates
		//Collection <String> aos = hMap.values();
		// iterate through and add them to a string of chosen ranks
		//Iterator <String> itr = aos.iterator();
		//while (itr.hasNext()) {
		//	String si = itr.next();
		for (String si: list){
			if (si!=""){ 
				chosen = chosen.concat(si);
				chosen = chosen.concat(",");
			}
		 }
		
		// add this label to the second row, far rhs
		JLabel third_taken = new JLabel("Votes taken: " + chosen);
		top.add(third_taken);
		// add a label showing the voter id number
		third_taken.setToolTipText("Any votes already taken are listed here.");

		//JLabel voterID = new JLabel("Voter " + voter,  JLabel.CENTER);
		//voterID.setBackground(Color.RED);
		//voterID.setOpaque(true);
		//top.add(voterID);
		// finally the top panel is added
		aoContent.add(top, BorderLayout.PAGE_START);
		
		//JLabel voterID = new JLabel("Voter " + voter);
		//JPanel voterPanel = new JPanel();
		//voterPanel.add(voterID);
		//aoContent.add(voterPanel, BorderLayout.NORTH);
		
		// create a middle panel for the options browser part of this window
		JPanel aoPanel = new JPanel();
		// create a GridLayout that has two columns and as many rows as necessary.
		aoPanel.setLayout(new GridLayout(0,2));
		
		JLabel ao = new JLabel("Adaptation Option");
		JLabel av = new JLabel(show_element.getAttributeValue("sector"));
		aoPanel.add(ao);
		aoPanel.add(av);
		
		// show the child elements of the adaptation option, with name of the element on lhs and its value on rhs
		List <Element> all_elements = show_element.getChildren();
		for (Element elt:all_elements){
			String name = elt.getName();
			JLabel elt_name = new JLabel (name);
			aoPanel.add(elt_name);
			String text = elt.getTextTrim();
			JLabel elt_value = new JLabel(text);
			elt_value.setOpaque(true);
			elt_value.setBackground(Color.WHITE);
			aoPanel.add(elt_value);
		}
		aoContent.add(aoPanel, BorderLayout.CENTER);
		int pageno = showing + 1;
		JLabel pageLabel = new JLabel("Page " + pageno + " / " + length);
		JButton backButton = new JButton("<");
		JButton nextButton = new JButton(">");
		backButton.setToolTipText("2. Use the arrow buttons to navigate to an option to vote for");
		nextButton.setToolTipText("2. Use the arrow buttons to navigate to an option to vote for");

		JButton undoButton = new JButton("Undo vote");
		JButton closeButton = new JButton("Finalise this vote");
		JButton computeButton = new JButton("Finalise poll");
		closeButton.setToolTipText("Button should be clicked when the CURRENT voter has finished entering information.");
		computeButton.setToolTipText("Button should be clicked when the FINAL voter has finished entering information.");

		// buttons for AO browser	
		backButton.addActionListener((ActionListener)EventHandler.create(ActionListener.class, this, "BACK"));
		nextButton.addActionListener((ActionListener)EventHandler.create(ActionListener.class, this, "NEXT"));
		// buttons for voting in the poll
		undoButton.addActionListener((ActionListener)EventHandler.create(ActionListener.class, this, "UNDO"));
		
		closeButton.addActionListener((ActionListener)EventHandler.create(ActionListener.class, this, "FINALIZE"));
		computeButton.addActionListener((ActionListener)EventHandler.create(ActionListener.class, this, "COMPUTE"));
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
		buttonPanel.add(pageLabel);
		buttonPanel.add(backButton);
		buttonPanel.add(nextButton);
		buttonPanel.add(undoButton);
		buttonPanel.add(closeButton);
		buttonPanel.add(computeButton);
		aoContent.add(buttonPanel, BorderLayout.PAGE_END);
		
		// TODO add a button to end the selection for the last voter and start the count
		//validate();	
		
		
        
        validate();	
	}
	
	
	
	
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
		poll.setNb_ranks(desiredvotes.intValue());
		
		//if(voterID.getText() != list.getNameString()) list.setNameString(voterID.getText());
		
		showAdaptationOption();
	}
	
	// this method is called when casting or changing a vote for an individual option
	public void vote(){
		// get the value from the Jcombo box field (the ranking)
		String value = (String)second_combo.getSelectedItem();
		// update the hashmap with the key (the AO) and the value (the ranking)
		list.set(showing, value);
		
		// update the voter namestring if necessary
		if(voterID.getText() != list.getNameString()) list.setNameString(voterID.getText());
		
		// return to the AO panel with updates as necessary
		showAdaptationOption();
	}
	
	/*public void votingEngine(){
	
	}*/
	
	// skip to the next - update the main panel with current choices
	public void NEXT(){
		
		if(showing<length-1) showing++;
		else if(showing==length-1) showing = 0;
		showAdaptationOption();
	}
	// go to previous entry
	public void BACK(){
		
		if(showing>0) showing--;
		else if(showing==0) showing = length-1;
		showAdaptationOption();
	}
	// this is called when the action event occurs on the "Finalise" (vote) button on the xml explorer
	public void FINALIZE(){
		// update the voter namestring if necessary
		if(voterID.getText() != list.getNameString()) list.setNameString(voterID.getText());
		
		// add the current vote to the poll and reset several variables
		// Use a copy constructor rather than cloning
		Vote<String> finalVote = new Vote<String> (list, "");
		finalVote.setNameString(list.getNameString());
		
		poll.addVote(finalVote);
	
		if (voter==1) {
			finalize = true;
			poll.setAwardsByCombo();
		}
		//list.print();					// try to print the vote
		poll.printResultOfVoter(finalVote);
		
		list.clear();					// clear the  current vote
		list = new Vote(length, "");
		voter ++;						// increase the count of votes
		showing = 0;
	    showAdaptationOption();
	
	   
	}
	public void UNDO(){
		// roll back to the former vote, to before finalize was clicked
		System.out.println("Deleting information: " + voterID.getText() + ", voter number " + voter);
		list.clear();
		list = new Vote(length, "");
		
		if (voter !=1){
			voter--;
			// System.out.println("Showing voter : " + voter);
			Vote former = poll.getVoteByNumber(voter);
			list = former;
			// take former vote out of the poll otherwise it may be counted twice
			poll.removeVote(former);
			System.out.println("Rolling back to voter : " + former.getNameString());
		}
		showAdaptationOption();
	}
	
	
	
	// this is called when the action event occurs on the "Finalise poll" button on the xml explorer
	public void COMPUTE(){
		FINALIZE();    // finalise the current vote
//		// update the voter namestring if necessary
//		if(voterID.getText() != list.getNameString()) list.setNameString(voterID.getText());
//		
//		// add the current vote to the poll and reset several variables
//		// Use a copy constructor rather than cloning
//		Vote<String> finalVote = new Vote<String> (list, "");
//		finalVote.setNameString(list.getNameString());
//		
//		poll.addVote(finalVote);
		
//		if (voter==1) {
//			finalize = true;
//			poll.setAwardsByCombo();
//		}
//		
//		// TODO print the vote to file by default
//		poll.printResultOfVoter(finalVote);
		
		// would like the results screen to be as responsive as possible to user choices/ modifications
		// so pass the results to the results frame 
		
		// TODO use the compute method of poll list class
		// poll.compute();
		
		// when satisfied with the result call addPreferredOptions method of combined results
		
		// thread should be passed to the results frame which displays the results
	    setVisible(false);
	    adxFrame.votingResult(poll);
	}
	/*public void colPicker(int i){
		int len = values().length;
		int number = (int) Math.floor (i/ len);
		return Color.cyan;
		//if(number == VoterCol.valueOf("RED").hashCode()) return Color.RED;
		//else return Color.cyan;
	
		
	}*/
	
	// 1st placed option gets points equal to the number of votes entered in the combo box
	// Other ranked options get uniformly decreasing points
//  method is part of Poll.java
//	public void setAwardsByCombo(){
//		Long s_ranks = new Long(poll.getNb_ranks());
//		awards= new Long[s_ranks.intValue()]; 
//		for (int i=0;i<s_ranks;i++){
//			awards[i]= s_ranks-i;
//		}
//	}
	
	// 1st placed option gets points equal to the number of votes entered in the combo box
	// Other ranked options get uniformly decreasing points
	public void printResultOfVoter(Vote<String> v){
		for (int i=0;i<v.size();i++){
			String elt = v.get(i);
			if(elt!=""){
				int rank = Integer.parseInt(elt) - 1;   	// rank must take the values 0,1,2 as array arg.
				System.out.println ("Voter gives " + awards[rank] + " points to option " + i);
			}
			
		}
		
	}
	
	// would like the results screen to be as responsive as possible to user choices/ modifications
	// so return the pollList object to the results frame 
	//public Vector <Vote> getPoll(){
	//	return poll.
	//}
	public Poll getPoll(){
		return poll;
	}
	
	// from the results frame the user is able to modify certain parameters eg. ranks, awards and see the effects
}

