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
 * <code>CriteriaPanel</code> is the panel with HierarchyTree + add/del criterium
 * @author  Maxime MORGE <A HREF="mailto:morge@emse.fr">morge@emse.fr</A> 
 * @version March 26, 2003 initial version
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
public class CriteriaPanel extends JPanel implements ActionListener {

  //ATTRIBUTS
  private Hierarchy h; // The Decision hierarchy
  private HierarchyTree ht; // the Jtree 
  private JButton add; // a button to add a criterium
  private JButton del; // a button to delete a criterium
  private AHPEngine window;// the main JAHP window

  /**
   * Creates a new  <code>CriteriaPanel</code> instance.
   * @param the decision <code>Hierarchy</code>
   * @param the main <code>JAHP</code> window
   */
  public CriteriaPanel(Hierarchy h,AHPEngine ahpEngine) {
    super(new BorderLayout());
    this.window=ahpEngine;
    //HierarchyTree
    ht =new HierarchyTree(h,ahpEngine);
    this.add("Center",ht);

    // JButton to add and delete criteria
    JPanel hierarchymodifypanel =new JPanel(new GridLayout(0,1));
    add=new JButton("Add Criterium");
    del=new JButton("Delete Criterium");
    hierarchymodifypanel.add(add);
    hierarchymodifypanel.add(del);
    add.addActionListener(this);
    del.addActionListener(this);

    this.add("South",hierarchymodifypanel);
        
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
   * <code>actionPerformed</code> method invoked when an Alternative is add/dell
   *
   * @param e a <code>ActionEvent</code> value
   */
  public void actionPerformed(ActionEvent e){
    if (e.getSource()==add){
      //Systemout.println("Add criterium");      
      ht.addNode();
      window.updateALTERNATIVE();
    }
    else{
      //Systemout.println("Del criterium");
      ht.delNode();
      window.updateALTERNATIVE();
    }
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
    JFrame mainFrame = new JFrame("CriteriaPanel test");
    mainFrame.setContentPane(new CriteriaPanel(h,null));
    mainFrame.pack();
    mainFrame.setVisible(true);
  }
}
