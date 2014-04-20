package shell;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;

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
// Top panel shows a 1 line message response to the user
public class MsgPanel extends JPanel{
	// A string to hold the message
	String line1;
	
	// create panel with a single string
	public MsgPanel(String line1){
		this.line1 = line1;
	}
	
	// This method is in JComponent, it takes one parameter object for drawing images, text etc.
	// The method is called automatically. All drawing in Java must go through a Graphics object.
	public void paintComponent(Graphics g){
		
		// make sure the class paint the panel with the background colour
		super.paintComponent(g);
		
		// method will render some text
		// do neat typesetting
		Graphics2D g2 = (Graphics2D)g;
		
		Font f = new Font("Serif", Font.BOLD, 12);
		g2.setFont(f);
	
		// centre line1
		// context object represents the font characteristics of the screen device
		FontRenderContext context = g2.getFontRenderContext();
		// returns the width (horizontal extent) and height (ascent+descent+leading) of the rectangle
		Rectangle2D bounds = f.getStringBounds(line1, context);
		// distance from baseline to top of ascender
		double ascent = -bounds.getY();
		
		double x = (getWidth()-bounds.getWidth())/2;
		// add the ascent to y to reach the baseline
		double y = (getHeight()-bounds.getHeight())/2 + ascent;
		
		g2.drawString(line1, (int)x, (int) y);
		
	}
}
