package shell;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JOptionPane;
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
	
	private JFileChooser fc;
    FileFilter filter = new FileNameExtensionFilter("XML for ADX","xml");
    private String msg = new String();  // verification message - empty string 
	ChooserListener cl;
	JPanel parent;
	StringBuffer log;
	private String fname; // name of the selected file e.g. Tanzania NAPA
	ADxFrame adxframe;   // main part of GUI shell
	
	private int returnVal = -1; // returnVal indicates if the operation worked
	private Document doc = null;
	
	
	public ChooserButton (String str, ADxFrame adxframe){
		setText(str);
		this.adxframe = adxframe;
		addActionListener(cl=new ChooserListener());
		
		//log = new StringBuffer();
	}
	public void setFileChooser(JFileChooser f){
		fc = f;
		fc.setFileFilter(filter);  // NEW: add filter to only show xml files
	} 
	
	public void setParent(JPanel jp){
		parent = jp;
	}
	
	// allows ADXFrame to verify the action and enable the OK button to go to engines
	public boolean verifyXML() {
		if (doc != null) {return true;}
		else {return false;}
	}
	// message to display about the verification status
	public String getMsg() {
		return msg;
	}
	// setter for external access
	public void setMsg (String ma){
		msg = ma;
	}
	// Inner classes can simplify code - you don't need a new class for every UI component.
	public class ChooserListener implements ActionListener {
		
		//@Override
		public void actionPerformed(ActionEvent e)  {
			// in response to a button click
			log = new StringBuffer(); 
			doc = null;
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
				aof.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				aof.setVisible(true);   // show method is deprecated
				
				}
				catch (IOException ioe) {
					System.err.println(ioe);
				}
				catch (JDOMException jde) {
					System.err.println(jde);
					// popup box
					JOptionPane.showMessageDialog(parent, "There was a loading error with the file:\n"
							+ fname + "\n"
							+ "Please check your XML file for possible formatting errors.\n"
							+ "For assistance contact: richard.taylor@sei-international.org",
							"Warning Message",
							JOptionPane.ERROR_MESSAGE);
					
				}
				finally {
					// code that will always execute
					// did it work
					msg = new String();
					if (doc != null) {
						msg = "Loaded file: " + fname + " correctly"; 
						System.out.println(msg);}
					else {
						msg = "Failed to open file correctly: " + fname;
						System.out.println(msg);}
					adxframe.loader.setMsg(""); // sets message back to empty string
					adxframe.INPUT();
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
