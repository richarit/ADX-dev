package shell;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
// Typically connections are made from a user interface bean (the event source) to an application logic bean (the target).
import java.beans.EventHandler;
//import java.awt.event.ActionListener;
import java.awt.BorderLayout;

import java.awt.Font;
import java.awt.font.FontRenderContext;
import java.awt.geom.*;
import java.awt.Graphics2D;
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

public class ADxPanel extends JPanel {
	
	// Three strings
	String line1;
	String line2;
	String line3;
	
	// create an initial panel with a single button
	public ADxPanel(String line1,String line2, String line3){
		this.line1 = line1;
		this.line2 = line2;
		this.line3 = line3;
	}
	
	// Here we are using the single line ActionListener creator
	/*
	// implement the ActionListener interface by supplying methods with the right signatures.
	// ActionListeners can be implemented as inner classes of the Panel they will manipulate
	// Inner classes can simplify code - you don't need a new class for every UI component.
	private class OkListener implements ActionListener {

		// This is the only method in the interface. It is used for all types of event.
		// ActionEvent object contains information about the event that happened.
		public void actionPerformed(ActionEvent arg0) {
			
			// ActionEvent would be a button click: reaction to button click is to display new panel
			// JButton creates an Action Event object and calls listener.actionPerformed(event)
			// passing that event object
			
			// TODO Go back to the frame and prepare the next window
				
			

		}
	}*/
		

	// This method is in JComponent, it takes one parameter object for drawing images, text etc.
	// The method is called automatically. All drawing in Java must go through a Graphics object.
	public void paintComponent(Graphics g){
		
		// make sure the class paint the panel with the background colour
		super.paintComponent(g);
		
		// method will render some text
		// do neat typesetting
		Graphics2D g2 = (Graphics2D)g;
		
		Font f = new Font("Serif", Font.BOLD, 15);
		g2.setFont(f);
	
		// centre line1
		// context object represents the font characteristics of the screen device
		FontRenderContext context = g2.getFontRenderContext();
		// returns the width (horizontal extent) and height (ascent+descent+leading) of the rectangle
		Rectangle2D bounds = f.getStringBounds(line1, context);
		// distance from baseline to top of ascender
		double ascent = -bounds.getY();
		
		double x = (getWidth()-bounds.getWidth())/1.5;
		// add the ascent to y to reach the baseline
		double y = (getHeight()-bounds.getHeight())/1.5 + ascent;
		
		g2.drawString(line1, (int)x, (int) y);
		
		// do the same for lines 2 and 3 in terms of calculating the x value
		bounds = f.getStringBounds(line2, context);
		x = (getWidth()-bounds.getWidth())/1.5;
		y = y + bounds.getHeight();
		g2.drawString(line2, (int)x, (int) y);
		
		bounds = f.getStringBounds(line3, context);
		x = (getWidth()-bounds.getWidth())/1.5;
		y = y + bounds.getHeight();
		g2.drawString(line3, (int)x, (int) y);
		
	}
}
