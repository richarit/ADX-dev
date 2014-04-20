package shell;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import javax.swing.table.AbstractTableModel;
import org.jdom.Document;
import org.jdom.Element;

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

// AdaptationOptionsModel is the model used with each engine, eg. to hold the results of the mock voting
public class AdaptationOptionsModel extends AbstractTableModel {

	Document doc;
	Element root;
	private List <Element> all_options;
	private int numOptions;
	
	// Table meta data
	//public static final String ROOT_ELEMENT_TAG = "adaptation_option";
	
	// colNames is initially set from the DTD but can be expanded in the computational stage.
	// For a dynamic field variable better therefore to use an arraylist rather than static array
	public ArrayList <String> colNames = new ArrayList <String> ();					// initialise colNames
	public ArrayList <Double>columnLengths = new ArrayList <Double> ();
	
	// TODO setRootElementTag ??
	
	
	// obtain the column names from the DTD
	public void setColNames(){
		// Access one of the DTD elements (the first element) and obtain the field names from it
		Element first_ao = getAll_options().get(0);
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
		Element first_ao = getAll_options().get(0);
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
	
	// custom constructor where a reference is passed to the Document
	public AdaptationOptionsModel(Document d){
		doc = d;
		// initialise root element and list of its child elements
		root = doc.getRootElement();
		setAll_options(root.getChildren());
		numOptions = getAll_options().size();
		
		setColNames();
		setColLengths();
		
	}
	
	/*
	@Override
	public void addTableModelListener(TableModelListener arg0) {
		// TODO Auto-generated method stub

	}*/

	
	public Class<?> getColumnClass(int arg0) {
	
		return getValueAt(0,arg0).getClass();
	}

	// add a new column such as "points" for the ranking exercise 
	public void addColumn(String cname, double clen){
		colNames.add(cname);
		columnLengths.add(clen);
		
		//Element child = new Element(cname);
		//root.addContent(child);
		
		//root.addContent(new Element(cname));
		// initialise entries with empty string
		//for(Vector<Object> row: rowdata){
		//	row.add("");
		//}
		// initialise entries with empty string
		// must get row first
		for(Element ao: getAll_options()){
			Element child = new Element(cname);
			//child.setAttribute(cname,"");
		    child.setText("");
			ao.addContent(child);
		}
	
	}
	
	public int getColumnCount() {
		return colNames.size();
	}

	public String getColumnName(int arg0) {
		return colNames.get(arg0);
	}

	public int getRowCount() {
		return getAll_options().size();
	}

	// return the value of text at the specified row and column location in the table
	public Object getValueAt(int r, int c) {
		// must get row first
		Element ao = getAll_options().get(r);
		Element elt = ao.getChild(colNames.get(c));
		
		String text = elt.getTextTrim();
		// System.out.println(text);
		//if(text.contains("unranked"))rank = length;
		//else rank = (int) Integer.parseInt(text);
		//ordered_opt[rank-1]=option;
		
		// get value for column in this row
		// HTML format automatically forces line wrapping
		return "<HTML>" + text + "</HTML>";
	}

	// Do not need to implement unless table needs to be editable
	public boolean isCellEditable(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	/*@Override
	public void removeTableModelListener(TableModelListener arg0) {
		// TODO Auto-generated method stub

	}*/
	/*
	public void addVoterID(String strv, int optionID){
		int c;
		for (int i = 0; i< colNames.size(); i++){
			if (colNames.get(i)== "notes") c = i;
		}
		
		
	}*/
	
	// do not need to implement unless table's data can change
	public void setValueAt(Object arg0, int r, int c) {
		// TODO Auto-generated method stub
		// must get row first
		Element ao = getAll_options().get(r);
		Element elt = ao.getChild(colNames.get(c));
		String str_elt = elt.getTextTrim();
		// TODO add Objects rather than strings to the table data
		
		elt.setText(arg0.toString());
		//rowdata[r].setElementAt(str_elt + arg0.toString(), c);
		
	}

	public int getNumOptions(){
		return numOptions;
	}

	public void setAll_options(List <Element> all_options) {
		this.all_options = all_options;
	}

	public List <Element> getAll_options() {
		return all_options;
	}
	
}
