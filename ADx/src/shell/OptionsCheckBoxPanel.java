package shell;
import java.awt.BorderLayout;

import javax.swing.JCheckBox;
import javax.swing.JPanel;
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

public class OptionsCheckBoxPanel extends JPanel {

	public OptionsCheckBoxPanel(){
//Put the check boxes in a column in a panel
		
        //JPanel checkPanel = new JPanel(new GridLayout(0, 1));
        JPanel checkPanel = new JPanel();
        JCheckBox us = new JCheckBox("user");
    	JCheckBox es = new JCheckBox("engine");
        checkPanel.add(us);
        checkPanel.add(es);
        //contentPane.add(checkPanel, BorderLayout.LINE_START);
        
        // You can change a panel's transparency by invoking the setOpaque method. 
        // A transparent panel draws no background, so that any components underneath show through.

	}
}
