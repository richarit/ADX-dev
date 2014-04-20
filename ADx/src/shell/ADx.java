package shell;
import java.io.*;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.Image;
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.lang.ProcessBuilder;
import javax.swing.*;

/**
 * Copyright 2012, 2014 Richard Taylor
 * 
 * This file is part of the Adaptation Options Explorer (ADX)
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

public class ADx {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// schedule on the event dispatch thread
		javax.swing.SwingUtilities.invokeLater(new Runnable(){
			public void run(){
				// start the GUI
				createAndShowGUI();
			}
		});
	
	}
	
	public static void createAndShowGUI(){
		// Create a new ADx frame and let the application exit on closing the frame
		ADxFrame adxFrame = new ADxFrame();
		adxFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// show method is deprecated
		adxFrame.setVisible(true);
		// activates a user interface thread that keeps the program alive
	}
	
}