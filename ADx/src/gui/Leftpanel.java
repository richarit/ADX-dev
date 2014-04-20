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
 * <code>Leftpanel</code> is the panel with HierarchyTree, AlternativeList and button to add/del Alternatives/Criterium
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
public class Leftpanel extends JPanel {

  //ATTRIBUTS
  private Hierarchy h;
  private AHPEngine window;
  private CriteriaPanel csp;
  private AlternativesPanel asp;


  public Leftpanel(Hierarchy h,CriteriaPanel csp, AlternativesPanel asp,AHPEngine ahpEngine) {
    super(new BorderLayout());
    this.csp=csp;
    this.asp=asp;
    this.window=ahpEngine;
    this.add("Center",csp);
    this.add("South",asp);
    
    
  }


  public Dimension getpreferredSize(){
    return new Dimension(150,600);  
  }

  public Dimension getMinimumSize(){
    return new Dimension(150,500);  
  }


  public void updateALTERNATIVE(){
    //Systemout.println("Leftpanel update alt");
    //this.remove(asp);
    //asp=new AlternativesPanel(h,window);
    //this.add("South",asp);
    //return asp;
    asp.updateALTERNATIVE();
  }


/*
  public static void main(String[] args) {
    // create a frame
    OwnTest test=new OwnTest();
    Hierarchy h =new Hierarchy();
    h=test.getHierarchyExample();
    JFrame mainFrame = new JFrame("Leftpanel test");
    mainFrame.setContentPane(new Leftpanel(h,new CriteriaPanel(h,null),null,null));
    mainFrame.pack();
    mainFrame.setVisible(true);
  }*/
}
