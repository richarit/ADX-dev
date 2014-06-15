package shell;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.util.ArrayList;
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
/*
 * this object returns information on how to position the panels each time step
 */
public enum GUIStep {
	
	// private static final JFrame adxFrame;
	
	// enumerate the order in which GUI screens will be displayed : 
	// 		about options input engines
	// Following, in pairs of arguments, are string name and string position of panels in border layout
	// ----		PAGE_START	-----
    // LINE_START CENTER LINE_END
	// ----		PAGE_END	-----

	// NOTE panels will be created in the frame class, this just sets the layout
	ABOUT (BorderLayout.PAGE_END, BorderLayout.CENTER),				// an ok button and some text
	OPTIONS (BorderLayout.PAGE_END, BorderLayout.LINE_START,BorderLayout.CENTER),			// user or ADx to select
	INPUT (BorderLayout.PAGE_END, BorderLayout.PAGE_START, BorderLayout.LINE_START, BorderLayout.CENTER, BorderLayout.LINE_END),// browse to xml files
	ENGINES (BorderLayout.PAGE_END, BorderLayout.PAGE_START, BorderLayout.CENTER),			// check boxes to start engines
	FINAL();
	
	ArrayList<String> positions;
	
	GUIStep(){};
	
	// constructor for Enum when frame has two panels
	GUIStep(String s1, String s2){
		positions = new ArrayList<String>();
		positions.add(s1);
		positions.add(s2);
		
	}
	
	// constructor for Enum when frame has three panels
	GUIStep(String s1, String s2, String s3){
		positions = new ArrayList<String>(3);
		positions.add(s1);
		positions.add(s2);
		positions.add(s3);
	}
	
	// constructor for Enum when frame has four panels
	GUIStep(String s1, String s2, String s3, String s4){
		positions = new ArrayList<String>(4);
		positions.add(s1);
		positions.add(s2);
		positions.add(s3);
		positions.add(s4);
	}
	
	// constructor for Enum when frame has all (=five) panels
	GUIStep(String s1, String s2, String s3, String s4, String s5){
		positions = new ArrayList<String>(5);
		positions.add(s1);
		positions.add(s2);
		positions.add(s3);
		positions.add(s4);
		positions.add(s5);
	}
	
	// return instructions on which panels to display
	public ArrayList<String> getPanels(){
		
		// pass an array of strings
		return positions;
	}

}
