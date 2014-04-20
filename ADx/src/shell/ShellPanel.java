package shell;

import javax.swing.*;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
// Typically connections are made from a user interface bean (the event source) to an application logic bean (the target).
import java.beans.EventHandler;
import java.awt.event.ActionListener;
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
public class ShellPanel extends JPanel {
	
	// A listener object is an instance of a class that implements a special interface
	OkListener listener;
	JButton ok;
	JCheckBox us;
	JCheckBox es;
	
	// Three strings
	String line1;
	String line2;
	String line3;
	
	
	public ShellPanel(){
		
	}

}
