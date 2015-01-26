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
 * <code>AlternativesTable</code> the custom  swing.Table
 * @author  Maxime MORGE <A HREF="mailto:morge@emse.fr">morge@emse.fr</A> 
 * @version April 14, 2003
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
public class AlternativesTable extends JTable implements ListSelectionListener{

  //ATTRIBUTS
  private Hierarchy h; // The decision Hierarchy
  private AHPEngine window; // the main JAHP window
  private AlternativesModel am; // the model

  /**
   * Creates a new  <code>AlternativesTable</code> instance.
   * @param Hierarchy h
   * @param JAHP window the main window
   */
    public AlternativesTable(Hierarchy h,AHPEngine ahpEngine) {
        super();
	this.setPreferredScrollableViewportSize(new Dimension(150, 150));
        getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);


	this.h=h;
	this.window=ahpEngine;
	this.am=new AlternativesModel(h,this,ahpEngine);
	this.setModel(am);

	//Listen for when the selection changes.
	this.setColumnSelectionAllowed(false); 
	this.setRowSelectionAllowed(false); 
	getSelectionModel().addListSelectionListener(this);


	// Look and Feel
        //DefaultCellRenderer renderer = new DefaultCellRenderer(red,new ColorRenderer(true));
        //setCellRenderer(renderer);
    }


  /**
   * the <code>valueChanged</code> overide method here.
   *
   * @param ListSelectionModel 
   * @see ListSelectionListener
   *
   */

  public void valueChanged(ListSelectionEvent e) {
    //Systemout.println("Alternatives valueChanged");    
    ListSelectionModel lsm = getSelectionModel();
    if (e.getValueIsAdjusting()) return;
    if (lsm.isSelectionEmpty()) {
      //no rows are selected
    } else {
      int selectedRow = lsm.getMinSelectionIndex();
      Alternative node = (Alternative) (h.getAlternatives()).get(selectedRow);
      // show the criterium in the Main Panel
      //Systemout.println("New Alternative value :"+node.toString());    
      //TO DO
      //Systemout.println("value changed"+node.getName());
      window.updateSHOWALTERNATIVE(node);

    }
  }


  /**
   * the <code>delNode</code> method to delete a node in this table..
   *
   */  

  public void delNode() {
    //Systemout.println("Alternatives valueChanged");    
    ListSelectionModel lsm = getSelectionModel();
    if (lsm.isSelectionEmpty()) {
      //no rows are selected
    } else {
      int selectedRow = lsm.getMinSelectionIndex();
      Alternative node = (Alternative) (h.getAlternatives()).get(selectedRow);
      // show the criterium in the Main Panel
      //Systemout.println("New Alternative value :"+node.toString());    
      am.removeRow(node);
      
    }
  }

  /**
   * the <code>addNode</code> method to add a node in this table..
   *
   */  
  public void addNode() {
    am.addRow();
  }

  /**
   * the <code>updateALTERNATIVE</code> method to update this table.
   *
   */  
  public void updateALTERNATIVE(){
    //Systemout.println("AlternativesTable update alt");
    this.am=new AlternativesModel(h,this,window);
    this.setModel(am);
  }


  /**
   * Describe <code>getPreferredSize</code> method here.
   *
   * @return a <code>Dimension</code> value
   * @see  <code>Container</code>
   */
  public Dimension getpreferredSize(){
    return new Dimension(150,400);  
  }

  /**
   * Describe <code>getMinimumSize</code> method here.
   *
   * @return a <code>Dimension</code> value
   * @see  <code>Container</code>
   */
  public Dimension getMinimumSize(){
    return new Dimension(150,300);  
  }


  /**
   * <code>main</code> method to test this class.
   * @param Criterium :  command line
   * 
   */
 public static void main(String[] args) {
    // create a frame
    OwnTest test=new OwnTest();
    Hierarchy h =new Hierarchy();
    h=test.getHierarchyExample();
    JFrame mainFrame = new JFrame("AlternativesPanel test");
    mainFrame.setContentPane(new AlternativesTable(h,null));
    mainFrame.pack();
    mainFrame.setVisible(true);
  }

}
