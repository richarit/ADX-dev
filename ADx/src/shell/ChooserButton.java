package shell;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import java.awt.Component;
import java.awt.event.*;
import java.io.*;
// package for document class and components
import org.jdom.*;
// package for saxbuilder and other document builder
import org.jdom.input.*;
import org.jdom.output.XMLOutputter;

//Typically connections are made from a user interface bean (the event source) to an application logic bean (the target).
import java.beans.EventHandler;
import java.io.File;

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
public class ChooserButton extends JButton {
	
	JFileChooser fc;
    FileFilter filter = new FileNameExtensionFilter("XML for ADX","xml");
	
	ChooserListener cl;
	JPanel parent;
	StringBuffer log;
	String fname; // name of the selected file e.g. Tanzania NAPA
	private int returnVal = -1; // returnVal indicates if the operation worked
	Document doc;
	
	public ChooserButton (String str){
		setText(str);
		addActionListener(cl=new ChooserListener());
		
		log = new StringBuffer();
	}
	public void setFileChooser(JFileChooser f){
		fc = f;
		fc.setFileFilter(filter);  // NEW: add filter to only show xml files
	} 
	
	public void setParent(JPanel jp){
		parent = jp;
	}
	
	
	// Inner classes can simplify code - you don't need a new class for every UI component.
	public class ChooserListener implements ActionListener {

		//@Override
		public void actionPerformed(ActionEvent e) {
			// in response to a button click
		     
			// the argument to the showOpenDialog method specifies the parent component for the dialogue
			returnVal = fc.showOpenDialog(parent);
			// returnVal indicates if the operation worked
			if (returnVal == JFileChooser.APPROVE_OPTION){ 
				File choice = fc.getSelectedFile();
				fname = choice.getName();
				//System.out.println("opening file: " + fname);
				log.append("Thankyou. Choosing: " + fname );
				try{
				// This builds a document of whatever's in the given resource
				SAXBuilder builder = new SAXBuilder();
				// the argument to the builder is any url. Here it is passed the file instance
				doc = builder.build(choice);
				// tell the parser whether to validate against a Document Type Definition (DTD) during the build
				// doc.setDocType(DocType dt = new DocType());
				// setValidation(boolean validate)
				
				// prettyprint the document for human display with the following two lines
				// XMLOutputter outp = new XMLOutputter();
				// outp.output(doc, System.out);
				
				//outp.setNewlines(true);
				//outp.setTextTrim(true);
				//outp.setIndent("  ");
				
				// create a new frame with several views on the data
				// able to flick through "pages" i.e. adaptation options 
				// place back and forward arrow buttons at the bottom
				// pass it the list of options
				AdaptationOptionsFrame aof = new AdaptationOptionsFrame(doc);
				aof.setTitle("Adaptation options  (FILE: " + fname + " )");
				aof.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				// show method is deprecated
				aof.setVisible(true);
				
				}
				catch (IOException ioe) {
				      System.err.println(ioe);
				}
				catch (JDOMException jde) {
				      System.err.println(jde);
				}

			}
			else log.append ("Choosing command cancelled by user.");
			
		}
	}
	
	// methods called from ADxFrame
	public StringBuffer getLog(){
		return log;
	}
	public String getFname(){
		return fname;
	}
	public int getReturnVal(){
		return returnVal;
	}
	
	public Document getDocument(){
		return doc;
	}
}
