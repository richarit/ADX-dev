package shell;
//package for document class and components
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.jdom.*;
// package for saxbuilder and other document builder
import org.jdom.input.*;
import org.jdom.output.XMLOutputter;
/**
 * Copyright 2014 Richard Taylor
 * 
 * This file is part of the Adaptation Decision Explorer (ADX)
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
public class Loader {
	
	
	private String msg = new String();  // verification message - empty string 
	private StringBuffer log;
	private Document doc;
	private String path;	// path to the working directory
	private String fname;
	//private File file;
	private InputStream is;
	ADxFrame adxframe;   // main part of GUI shell
	
	private File home;
	
	
	// null constructor
	public Loader(){
		doc = null;
	}
	// constructor with string argument
	public Loader(String fname, ADxFrame adxframe){
		this.fname = fname;
		this.adxframe = adxframe;
		log = new StringBuffer(); 
		doc = null;
		// You can learn about the current working directory the following way:
		//System.out.println(new File(".").getAbsolutePath());
		// You can learn about the class path the following way:
		String classpath = System.getProperty("java.class.path");
		System.out.println(classpath);
		if (adxframe.dev) {
			path = "/home/richard/workspace/ADx/related/";  // absolute path
			System.out.println(path.concat(fname));
			//try {
			is = Loader.class.getResourceAsStream(path.concat(fname));
			if (is==null) System.out.println("is is null");
			
			//} catch (NullPointerException ue) {
			//	System.err.println(ue);
				
			//}
			//home = new File ("/home/richard/workspace/ADx/related");  // adx-dev path
		}
		else {
			///path = "//related//";
			//is = Loader.class.getResourceAsStream(path.concat(fname));
		}
		
		
		
		log.append("Thankyou. Choosing: " + fname );
		try{
			// This builds a document of whatever is in the given resource
			SAXBuilder builder = new SAXBuilder();
		
			// the argument to the builder is any url. Here it is passed the file instance
			doc = builder.build(is);
			
		  
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
			
		}
		finally {
			// code that will always execute
			// did it work
			msg = new String();
			if (doc != null) {
				msg = "Loaded file: " + fname + " correctly"; 
				System.out.println(msg);}
			
//			else {
//				msg = "Failed to open file correctly: " + fname;
//				System.out.println(msg);}
			adxframe.chooserButton.setMsg("");
			adxframe.INPUT();
		}
	
	}
	
	// allows ADXFrame to verify the action and enable the OK button to go to engines
	public boolean verifyXML() {
		if (doc != null) {return true;}
		else {return false;}
	}
	public Document getDocument(){
		return doc;
	}
	
	// message to display about the verification status
	public String getMsg() {
		return msg;
	}
	// setter for external access
	public void setMsg (String ma){
		msg = ma;
	}
	
}
