package shell;

import java.util.ArrayList;
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
public class CombinedResultsModel extends AbstractTableModel {

	Document doc;
	Element root;
	List <Element> all_options;
	ADxFrame adx;
	
	// Table meta data
	public static final String ROOT_ELEMENT_TAG = "adaptation_option";
	
	// Table data is placed in an ArrayList so that size can be changed
	ArrayList<Object> [] rowdata;
	//int numcols;
	int numengines;
	
	// column names are names of the Elements in adaptation_options Element
	// TODO col names are already known since we made the DTD
	// structure of the document is 2D array. Every row in the TableModel is a preferred option
	// every column is (a data point or) an engine
	public ArrayList <String> colNames;
	
	// use these factors to set the preferred length of columns
	double [] columnLengths = {1.5, 1.0, 1.0, 1.0, 1.5, 1.0, 2.0};
	
	// set the table data here
	public CombinedResultsModel(ADxFrame as){
		
		adx = as;
	}
	
	// pass the entire data set from the original xml file
	// establish the number of columns in the dataset
	public void setInputData(TableModel tm){
		// create the array of vectors, one row for each option in the initial set
		rowdata = new ArrayList [tm.getRowCount()];
		
		// set the colNames 
		colNames = new ArrayList <String> (tm.getColumnCount());
		for(int ci=0; ci<tm.getColumnCount(); ci++) colNames.add(tm.getColumnName(ci));
		
		for(int ini=0; ini<rowdata.length; ini++){
			// create vector objects
			rowdata[ini]=new ArrayList(getColumnCount());
			// fill the columns in the original data set		
			for (int col=0; col<getColumnCount(); col++){
				// get value at grabs the html formatted text
				setValueAt(tm.getValueAt(ini, col), ini, col);
			}
		}
	}
		
	// List of options plus string name of engine
	public void addPreferredOptions(List<Element> po, String str){
		colNames.add(str);
	
		// add this engine as a new column, by adding to the arraylist
		for (int rowi =0; rowi< rowdata.length; rowi++){
			boolean found = false;
			// if vector contains the current row
			for(Element v: po){
				// check the initial strings of the adaptation option
				//Element option = (Element) v.getv.get(0);
				//Element desc = option.getContent(0);
				Element desc = v.getChild("descriptor");
				// Note: datastring has html markup, optstring does not
				String optString = "<HTML>" + desc.getTextTrim() + "</HTML>";
				String dataString = rowdata[rowi].get(0).toString();
				if(dataString.compareTo(optString)==0) found = true;
			}
			// increase by one unit the number of columns of this row
			rowdata[rowi].add("");
			if (found) setValueAt("*", rowi, getColumnCount()-1);
		}
		
		adx.recordCompletedEngine(str);
		//call START
		adx.START();
	}
	
	
	@Override
	public void addTableModelListener(TableModelListener arg0) {
		// TODO Auto-generated method stub

	}

	
	public Class<?> getColumnClass(int arg0) {
	
		return getValueAt(0,arg0).getClass();
	}


	public int getColumnCount() {
		return colNames.size();
	}

	public String getColumnName(int arg0) {
		return colNames.get(arg0);
	}

	public int getRowCount() {
		return rowdata.length;
	}

	// return the value of text at the specified row and column location in the table
	// useful method for printing but not for manipulating data
	public Object getValueAt(int r, int c) {
		// do not need to read in the document again!
		//Element ao = all_options.get(r);
		//Element elt = ao.getChild(colNames[c]);
		//String text = elt.getTextTrim();
		
		// get value for column in this row
		Object text = rowdata[r].get(c);
		
		// HTML format automatically forces line wrapping
		//return "<HTML>" + text + "</HTML>";
		return text;
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
	


}
