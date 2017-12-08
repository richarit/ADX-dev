package shell;

import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.JSlider;
import javax.swing.ImageIcon;

import javax.imageio.ImageIO;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.awt.GridLayout;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.Vector;
import java.util.Date;
import java.util.Hashtable;
import java.text.SimpleDateFormat;

// package for document class and components
import org.jdom.*;
// package for saxbuilder and other document builder
import org.jdom.input.*;
import org.jdom.output.XMLOutputter;

//Typically connections are made from a user interface bean (the event source) to an application logic bean (the target).
import java.beans.EventHandler;

import javax.swing.JFileChooser;
import javax.swing.JComponent;

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
public class AdaptationOptionsFrame extends JFrame {

	// number of adaptation options
	private int length;
	private int showing=0;		// current option showing
	private Hashtable<Integer, String[]> graphLabels; //hash table for graphical info
	Container aoContent;
	Document doc;
	Element root;
	List <Element> all_options; 
	// colNames is initially set from the DTD but can be expanded in the computational stage.
	// For a dynamic field variable better therefore to use an arraylist rather than static array
	public ArrayList <String> colNames = new ArrayList <String> ();					// initialise colNames
	public ArrayList <Double>columnLengths = new ArrayList <Double> ();


	/*public static final String[] colNames = {
		"descriptor","cost_category","scale","supported_by","goals"//, "rank" , "notes"
	};*/

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

	public AdaptationOptionsFrame(Document doc){

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

		//setSize(screenWidth / 2, screenHeight / 2);
		//setLocation(screenWidth * 3/8, screenHeight * 3 / 8);
		setSize(screenWidth, screenHeight);

		aoContent = getContentPane();
		this.doc = doc;
		System.out.println(doc);
		// get the root element, and get a list of its child elements, set the size
		root = doc.getRootElement();
		System.out.println(root.getName());

		all_options = root.getChildren();
		length = all_options.size();

		// setTitle("Adaptation options");
		// set col names and sizes
		setColNames();
		setColLengths();

		// Check if there is a txt file sharing the name of the xml file that
		// contains "instructions" for displaying graphical information 
		File txtFile = new File("src/shell/related/" + root.getName() + ".txt");
		if (txtFile.exists()) {

			// read in the file and put contents in a hash table
			graphLabels = new Hashtable<Integer, String[]>();
			BufferedReader reader = null;
			try {
				reader = new BufferedReader(new FileReader(txtFile));
				int optionNum = 0;
				String line;
				//the txt file contains an int for every option describing how
				//many graphs are available for every option. if there are n 
				//graphs, the next n lines contain the slider label for each
				while ((line = reader.readLine()) != null) {
					int listLength = Integer.parseInt(line);
					String[] labels = new String[listLength]; 
					for (int i = 0; i < listLength; i++) {
						labels[i] = reader.readLine();
					}
					if (listLength > 0)
						graphLabels.put(optionNum, labels);
					optionNum++;
				}

			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		// call the show method with the initial value showing = 0
		showAdaptationOption(-1);


		// TODO can they be sorted first before adding? 

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

	// takes an integer that specifies the position of the slider
	// for graphical display, otherwise pass -1 as argument
	public void showAdaptationOption(int v){
		aoContent.removeAll();
		Element show_element = all_options.get(showing);
		// create a new panel each time this method is called
		JPanel aoPanel = new JPanel();

		// check if there is graphical display for this option
		if (graphLabels.get(showing) != null) {
			aoPanel.setLayout(new GridBagLayout()); // fancier layout for advanced display

			// column headings
			JLabel ao = new JLabel("Adaptation Option");
			JLabel av = new JLabel(show_element.getAttributeValue("sector"));
			JLabel ay = new JLabel("Supporting Data");
			GridBagConstraints c = new GridBagConstraints();
			c.weighty = 0.2;
			c.gridx = 0;
			c.gridy = 0;
			c.anchor = GridBagConstraints.FIRST_LINE_START;
			aoPanel.add(ao,c);
			c.gridx = 1;
			aoPanel.add(av,c);
			c.gridx = 2;
			c.anchor = GridBagConstraints.PAGE_START;
			aoPanel.add(ay,c);

			//qualitative table
			GridBagConstraints cTabL = new GridBagConstraints();
			cTabL.gridx = 0;
			cTabL.weightx = 0.5;
			cTabL.weighty = 0.5;
			cTabL.fill = GridBagConstraints.HORIZONTAL;
			GridBagConstraints cTabR = new GridBagConstraints();
			cTabR.gridx = 1;
			cTabR.weightx = 0.5;
			cTabR.weighty = 0.5;
			cTabR.fill = GridBagConstraints.BOTH;
			int row = 1;
			List <Element> all_elements = show_element.getChildren();
			for (Element elt:all_elements){
				String name = elt.getName();
				JLabel elt_name = new JLabel (name);
				cTabL.gridy = row;
				aoPanel.add(elt_name,cTabL);
				String text = elt.getTextTrim();
				JLabel elt_value = new JLabel(text);
				elt_value.setOpaque(true);
				elt_value.setBackground(Color.WHITE);
				cTabR.gridy = row;
				aoPanel.add(elt_value,cTabR);
				row++;
			}

			//slider

			String[] tickLabels = graphLabels.get(showing); //tick labels from hash table

			GridBagConstraints cSlide = new GridBagConstraints();
			cSlide.gridx = 2;
			cSlide.gridy = row-1;
			cSlide.weighty = 0.8;

			JSlider slider = new JSlider(0,tickLabels.length-1);
			//another hash table to serve as the dictionary for the tick labels
			Hashtable<Integer, Object> labelTable = new Hashtable<Integer, Object>();
			for (int i = 0; i < tickLabels.length; i++) {
				labelTable.put(i, new JLabel(tickLabels[i]));
			}
			if (v != -1)
				slider.setValue(v); //here is where the argument to the function is used
			slider.setLabelTable(labelTable);
			slider.setPaintTicks(true);
			slider.setPaintLabels(true);
			slider.setPreferredSize(new Dimension(500, 100));
			//refresh if user changes the slide
			slider.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent e) {
					showAdaptationOption(slider.getValue());
				}
			});
			aoPanel.add(slider,cSlide);

			//graphical display
			GridBagConstraints cPic = new GridBagConstraints();
			cPic.gridx = 2;
			cPic.gridy = 1;
			cPic.gridheight = 5;
			cPic.weightx = 0.8;
			cPic.fill = GridBagConstraints.HORIZONTAL;
			cPic.anchor = GridBagConstraints.CENTER;
			//check which graph to display based on slider position
			int sliderValue = slider.getValue(); 
			BufferedImage myPicture = null;
			//picture filename format is [option #]-[slider position].jpg within a folder
			//with the name matching the xml file
			try {
				myPicture = ImageIO.read(new File("src/shell/related/" + root.getName()
				+ "/" + showing + "-" + sliderValue + ".jpg"));
			} catch (IOException e) {
				e.printStackTrace();
			}
			JLabel picLabel = new JLabel(new ImageIcon(myPicture));
			aoPanel.add(picLabel,cPic);

			//dummy panel to fix the width of column 2 so columns maintain size
			//this makes sense when switching between 2 consecutive options that have graphs
			JPanel dummyPanel = new JPanel();
			dummyPanel.setPreferredSize(new Dimension(500, 0));
			GridBagConstraints cDummy = new GridBagConstraints();
			cDummy.gridx = 1;
			cDummy.gridy = row;
			aoPanel.add(dummyPanel, cDummy);


		} else {
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
				//System.out.println(text);
				JLabel elt_value = new JLabel(text);
				elt_value.setOpaque(true);
				elt_value.setBackground(Color.WHITE);
				aoPanel.add(elt_value);
			}
		}
		aoContent.add(aoPanel, BorderLayout.CENTER);
		int pageno = showing + 1;
		JLabel pageLabel = new JLabel("Page " + pageno + " out of " + length);
		JButton backButton = new JButton("<");
		JButton nextButton = new JButton(">");
		JButton editButton = new JButton("Edit");
		JButton newButton = new JButton("Add Option");
		JButton saveButton = new JButton("Save");
		JButton closeButton = new JButton("Close");
		backButton.addActionListener((ActionListener)EventHandler.create(ActionListener.class, this, "BACK"));
		nextButton.addActionListener((ActionListener)EventHandler.create(ActionListener.class, this, "NEXT"));
		editButton.addActionListener((ActionListener)EventHandler.create(ActionListener.class, this, "EDIT"));
		newButton.addActionListener((ActionListener)EventHandler.create(ActionListener.class, this, "newAdaptationOption"));
		saveButton.addActionListener((ActionListener)EventHandler.create(ActionListener.class, this, "SAVE"));
		closeButton.addActionListener((ActionListener)EventHandler.create(ActionListener.class, this, "CLOSE"));
		JPanel buttonPanel = new JPanel();
		buttonPanel.add(pageLabel);
		buttonPanel.add(backButton);
		buttonPanel.add(nextButton);
		buttonPanel.add(editButton);
		buttonPanel.add(newButton);
		buttonPanel.add(saveButton);
		buttonPanel.add(closeButton);
		aoContent.add(buttonPanel, BorderLayout.PAGE_END);
		validate();	
	}
	// show the adaptation option so that the parameter values are editable
	// a lot of repetition from showAdaptationOption()
	public void editAdaptationOption(){
		// 
		aoContent.removeAll();
		Element show_element = all_options.get(showing);
		// create a new panel each time this method is called
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
			JTextField elt_value = new JTextField(text); // make the text editable
			elt_value.setOpaque(true);
			elt_value.setBackground(Color.WHITE);
			aoPanel.add(elt_value);

			// action once the text has been edited, updates immediately upon click out of text field
			elt_value.addFocusListener(
					new FocusListener() {
						public void focusGained(FocusEvent e) {
						}
						public void focusLost(FocusEvent e) {
							// elt_value.setText(elt_value.getText());	 // voterID is the JTextField
							//list.setNameString(elt_value.getText()); // nameString is the stored datum
							elt.setText(elt_value.getText());
							//System.out.println("New text: " + elt_value.getText());
						}
					}
					);

		}
		aoContent.add(aoPanel, BorderLayout.CENTER);
		int pageno = showing + 1;
		JLabel pageLabel = new JLabel("Page " + pageno + " out of " + length);
		JButton backButton = new JButton("<");
		JButton nextButton = new JButton(">");
		JButton doneButton = new JButton("Done");
		JButton closeButton = new JButton("Close");
		backButton.addActionListener((ActionListener)EventHandler.create(ActionListener.class, this, "BACK"));
		nextButton.addActionListener((ActionListener)EventHandler.create(ActionListener.class, this, "NEXT"));
		doneButton.addActionListener((ActionListener)EventHandler.create(ActionListener.class, this, "DONE"));
		closeButton.addActionListener((ActionListener)EventHandler.create(ActionListener.class, this, "CLOSE"));
		JPanel buttonPanel = new JPanel();
		buttonPanel.add(pageLabel);
		buttonPanel.add(backButton);
		buttonPanel.add(nextButton);
		buttonPanel.add(doneButton);
		buttonPanel.add(closeButton);
		aoContent.add(buttonPanel, BorderLayout.PAGE_END);
		validate();	
	}

	// adds a new adaptation option at the end of the list of options and
	// immediately allows user input in fields
	public void newAdaptationOption(){
		Element copy_element = all_options.get(showing);
		root.addContent((Element) copy_element.clone());
		showing = length;
		length++;
		all_options = doc.getRootElement().getChildren();
		Element show_element = all_options.get(showing);
		List <Element> all_elements = show_element.getChildren();
		for (Element elt:all_elements) {
			elt.setText("");
		}
		editAdaptationOption();
	}

	// skip to the next
	public void NEXT(){

		if(showing<length-1) showing++;
		else if(showing==length-1) showing = 0;
		showAdaptationOption(-1);
	}
	// go to previous entry
	public void BACK(){

		if(showing>0) showing--;
		else if(showing==0) showing = length-1;
		showAdaptationOption(-1);
	}
	// allow the user to edit the entry
	public void EDIT() {
		System.out.println("editing");
		editAdaptationOption();
	}

	// stop editing the option
	public void DONE() {
		showAdaptationOption(-1);
	}

	// save the XML file in the "output" folder with a unique time stamp
	public void SAVE(){
		String fileName = new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss'.xml'").format(new Date());
		XMLOutputter xmlOutput = new XMLOutputter();
		try {
			xmlOutput.output(doc, new FileWriter("output" + "/" + root.getName() + "_" + fileName));
		} catch (IOException io) {
			System.out.println(io.getMessage());
		}

	}

	// this is called when the action event occurs on the "Close" button on the xml explorer
	public void CLOSE() {
		this.setVisible(false);
	}
}
