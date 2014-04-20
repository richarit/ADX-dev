package shell;

import javax.swing.ListModel;

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

import javax.swing.event.ListDataListener;
// this class is used to create a drop down list of options
public class AOListModel implements ListModel {
	private InputXMLModel aom;
	private int colId;

	public AOListModel(InputXMLModel aoin){
		this.aom = aoin;
		
	}
	
	public void setColumnOfModel(int com){
		colId = com;
	}
	
	
	@Override
	public void addListDataListener(ListDataListener arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public Object getElementAt(int arg0) {
		// TODO Auto-generated method stub
		return aom.getValueAt(arg0, colId);
		//return null;
	}

	@Override
	public int getSize() {
		// TODO Auto-generated method stub
		return aom.getRowCount();
	}

	@Override
	public void removeListDataListener(ListDataListener arg0) {
		// TODO Auto-generated method stub

	}

}
