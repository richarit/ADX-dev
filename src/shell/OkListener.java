package shell;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
// implement the ActionListener interface by supplying methods with the right signatures
// Note that ActionListeners can often be implemented as inner classes of the Panel they
// will manipulate
public class OkListener implements ActionListener {

	// This is the only method in the interface. It is used for all types of event.
	// ActionEvent object contains information about the event that happened.
	public void actionPerformed(ActionEvent arg0) {
		
		// ActionEvent would be a button click: reaction to button click is to display new panel
		// JButton creates an Action Event object and calls listener.actionPerformed(event)
		// passing that event object
		
		// Remove the "Ok" button and display a new panel with more options

	}
	
	// set properties of the listener here
	public OkListener(){
		
	}

}
