// Graphical User Interface
package gui;

//imports
import javax.swing.*;          //This is the final package name.
//import com.sun.java.swing.*; //Used by JDK 1.2 Beta 4 and all
//Swing releases before Swing 1.1 Beta 3.
import javax.swing.border.*;
import javax.swing.event.*;
import javax.swing.tree.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;
import java.net.*;

import Jama.*;


// Abstract Data Type
import adt.*;


/**
 * <code>AlternativesPanel</code> the  Pane to show  all alternatives 
 * @author  Maxime MORGE <A HREF="mailto:morge@emse.fr">morge@emse.fr</A> 
 * @version March 26, 2003
 * modified by Richard Taylor 2012 richard.taylor@sei-international.org
 */
/*
 * Copyright 2012 Richard Taylor
 * 
 * This file is part of the Adaptation Decision Explorer (ADx)

    ADx is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    ADx is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with ADx.  If not, see <http://www.gnu.org/licenses/>.
 */
public class AlternativesPanel extends JPanel implements ActionListener{

  // ATTRIBUTS
  private Hierarchy h; // the decision Hierarchy
  private AHPEngine window; // the main window
  private AlternativesTable at; // the table to show Alternatives
  private JButton add; // a button to add an Alternative
  private JButton del;// a button to del an Alternative
  private JButton fin;// a button to finalise AHP

  /**
   * Creates a new  <code>AlternativePanel</code> instance.
   * @param the decision <code>Hierarchy</code>
   * @param the main <code>JAHP</code> window
   */
  public AlternativesPanel(Hierarchy h,AHPEngine ahpEngine) {
    super(new GridLayout(0,1));

    this.h=h;
    this.window=ahpEngine;


    //HierarchyTree
    at =new AlternativesTable(h,ahpEngine);
    
    JScrollPane toppane = new JScrollPane(at);
    this.add("Center",toppane);


    // JButton to add and delete criteria
    JPanel alternativemodifypanel =new JPanel(new GridLayout(0,1));
    add=new JButton("Add Alternative");
    del=new JButton("Delete Alternative");
    alternativemodifypanel.add(add);
    alternativemodifypanel.add(del);
    this.add("South",alternativemodifypanel);

    //add ActionListener to the buttons
    add.addActionListener(this);
    del.addActionListener(this);
    
    
    // add a finish button
    fin = new JButton("Finalise");
    fin.addActionListener(this);
    alternativemodifypanel.add(fin);
  }


  /**
   * a <code>actionPerformed</code> method here.
   * @param the <code>ActionEvent</code>
   */
  public void actionPerformed(ActionEvent e){
    if (e.getSource()==add){
      //Systemout.println("Add criterium");      
      at.addNode();
    }
    else{
    	if (e.getSource()==fin){
    	      //Systemout.println("Add criterium");
    		// close the window. and pass control back to the engine
    		// engine has access to h which contains the AHP result
    		window.setVisible(false);
    		System.out.println("Best alternative is: " + h.bestAlternative());
    	    window.COMPUTE();
    	}
    	else
    		//Systemout.println("Del criterium");
    		at.delNode();
    }
  }

  /**
   * a <code>updateALTERNATIVE</code> method to update this panel.
   * @param the <code>ActionEvent</code>
   */
  public void updateALTERNATIVE(){
    //Systemout.println("AlternativesPanel update alt");
    at.updateALTERNATIVE();
  }

  

  /**
   * Describe <code>getPreferredSize</code> method here.
   *
   * @return a <code>Dimension</code> value
   * @see  <code>Container</code>
   */
    public Dimension getPreferredSize(){
      return new Dimension(150,200);
    }

  /**
   * Describe <code>getMinimumSize</code> method here.
   *
   * @return a <code>Dimension</code> value
   * @see  <code>Container</code>
   */
  public Dimension getMinimumSize(){
    return new Dimension(150,200);
}


  /**
   * <code>main</code> method to test this class.
   * @param command line
   * 
   */
  public static void main(String s[]) {
    JFrame frame = new JFrame("AlternativesPanel");
    
    frame.addWindowListener(new WindowAdapter() {
	public void windowClosing(WindowEvent e) {System.exit(0);}
      });

    OwnTest test=new OwnTest();
    Hierarchy h=test.getHierarchyExample();
    AlternativesPanel asp = new AlternativesPanel(h,null);	
    frame.getContentPane().add(asp);
    frame.pack();
    frame.setVisible(true);
  }
}
