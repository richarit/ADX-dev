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
 * <code>Rightpanel</code> is the panel with Alternative/Criterium show/modify
 * @author  Maxime MORGE <A HREF="mailto:morge@emse.fr">morge@emse.fr</A> 
 * @version March 26, 2003 initial version
 */
public class Rightpanel extends JPanel {

  private Hierarchy h;
  private Alternative alt;
  private Criterium c;
  private JAHP window;

  JSplitPane jsp2;

  private CriteriumPanel cp;
  private AlternativePanel ap;

  /*
   *Method to show a new Criterium 
   */
  public void updateSHOWCRITERIUM(Criterium c){
    this.remove(cp);
    cp=new CriteriumPanel(c,h,window);
    this.add("Center",cp);
  }

  /*
   *Method to show a new Alternativ
   */
  public void updateSHOWALTERNATIVE(Alternative alt){
    this.remove(ap);
    ap=new AlternativePanel(h,alt,window);
    this.add("South",ap);
  }

  /*
   *Method to show when a new Alternative is added
   */
  public void updateafteraddALTERNATIVE(Alternative alt){
    this.remove(ap);
    ap=new AlternativePanel(h,alt,window);
    this.add("South",ap);
  }

  /*
   *Method to show when a new Alternative is added
   */
  public void updateafterdelALTERNATIVE(){
    this.remove(ap);
    ap=new AlternativePanel(h,null,window);
    this.add("South",ap);
  }

  /*
   *Method to show when a new  Criterium is added
   */
  public void updateafteraddCRITERIUM(Criterium c){
    this.remove(cp);
    cp=new CriteriumPanel(c,h,window);
    this.add("Center",cp);
  }

  /*
   *Method to show when a new  Criterium is deleted
   */
  public void updateafterdelCRITERIUM(){
    this.remove(cp);
    cp=new CriteriumPanel(null,h,window);
    this.add("Center",cp);
  }

  /**
   * Creates a new  <code>JAHP</code> instance.
   * @param the Decision <code>Hierarchy</code>
   * @param the <code>CriteriumPanel</code>
   * @param the Decision <code>AlternativePanel</code
   * @param the main <code>JAHP</code> window
   */
  public Rightpanel(Hierarchy h,CriteriumPanel cp,AlternativePanel ap,JAHP window) {
    super(new BorderLayout());

    this.window=window;    

    this.cp=cp;
    this.ap=ap;
    this.add("Center",cp);
    this.add("South",ap);
 
    //jsp2 = new JSplitPane(JSplitPane.VERTICAL_SPLIT,cp,ap);
    //jsp2.setOneTouchExpandable(true);
    //jsp2.setDividerLocation(300);
    //this.add(jsp2);

    
  }


  public Dimension getPreferredSize(){
    return new Dimension(800,600);  
  }

  public Dimension getMinimumSize(){
    return new Dimension(700,600);  
  }


  public static void main(String[] args) {
    // create a frame
    OwnTest test=new OwnTest();
    Hierarchy h =new Hierarchy();
    h=test.getHierarchyExample();
    JFrame mainFrame = new JFrame("Rightpanel test");
    mainFrame.setContentPane(new Rightpanel(h,null,null,null));
    mainFrame.pack();
    mainFrame.setVisible(true);
  }
}
