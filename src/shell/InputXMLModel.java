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
public class InputXMLModel extends AbstractTableModel {

	private Document doc;
	private Element root;
	private List <Element> all_options;
	
	// Table meta data
	// public static final String ROOT_ELEMENT_TAG = "adaptation_option";
	
	// Table data
	private Vector<Object> [] rowdata;
	private int numcols;
	//int numengines;
	private int dataColumns;  // number of pieces of data in original doc
	
	// colNames is initially set from the DTD but can be expanded in the computational stage.
	// For a dynamic field variable better therefore to use an arraylist rather than static array
	public ArrayList <String> colNames = new ArrayList <String> ();					// initialise colNames
	public ArrayList <Double> columnLengths = new ArrayList <Double> ();
	
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
	
	// constructor : set the table data here
	// TODO resolve the names of the columns from the DTD 
	public InputXMLModel(Document d){
				
		doc = d;

		// initialise root element and list of its child elements
		root = doc.getRootElement();
		all_options = root.getChildren();
		//System.out.println(root.getName());
		

		int length = all_options.size();
		
		// set col names and sizes
		setColNames();
		setColLengths();
		dataColumns = getColumnCount();
		
		// create the array of vectors
		rowdata = new Vector [length];
		
		// create each vector in turn
		for(int ini=0; ini<length; ini++){
			rowdata[ini]=new Vector(getColumnCount());
		}
		
		// this is pasted from AdaptationOptionsModel
		for (Element option: all_options){
			
			// create vector row to hold data for this option
			int i = all_options.indexOf(option);
			
			for (int j=0; j<colNames.size(); j++){
				Element elt = option.getChild(colNames.get(j));
				String str_elt = elt.getTextTrim();
				// TODO add Objects rather than strings to the table data
				setValueAt(str_elt,i, j);
				//rowdata[i].add(j,str_elt);
			}
			
			// this line seems to set back to null the rowdata
			//tm.insertRow(i, rowdata[i]);
			//addRow(rowdata[i]);
		}
	}
	
	
	
	@Override
	public void addTableModelListener(TableModelListener arg0) {
		// TODO Auto-generated method stub

	}

	
	public Class<?> getColumnClass(int arg0) {
	
		return getValueAt(0,arg0).getClass();
	}

    public ArrayList <String> getColumnNames(){
    	return colNames;
    }
    public ArrayList <Double> getColumnLengths(){
    	return columnLengths;
    }
	public int getColumnCount() {
		return colNames.size();
	}
	

//	public void purgeColumns(){
//	   System.out.println("Data Columns " + dataColumns);
//	}


	public String getColumnName(int arg0) {
		return colNames.get(arg0);
	}

	public int getRowCount() {
		return rowdata.length;
	}

	// return the value of text at the specified row and column location in the table in HTML format
	public Object getValueAt(int r, int c) {

		// get value for column in this row
		Object text = rowdata[r].get(c);
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

	// do not need to implement unless table's data can change
	public void setValueAt(Object arg0, int arg1, int arg2) {
		rowdata[arg1].add(arg2, arg0);
	}
	
	public Document getDocument() {
		return doc;
	}
	public List <Element> getAll_options() {
		return all_options;
	}

}
