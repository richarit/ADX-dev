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
 * <code>CriteriumModifyPanel</code> the Pane to modify the comparisons in a criterium of the  Decisionnal Hierarchy
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
public class CriteriumModifyPanel extends JScrollPane{
  private Hierarchy h;
  private Criterium c;
  private int size;

  private CriteriumShowPanel csp;

  ScrollPaneLayout spl;
  private AHPEngine window ;

  /**
   * Creates a new  <code>CriteriumModifyPanel</code> instance.
   * @param Criterium c
   * @param Hierarchy h
   * @param CriteriumShowPanel csp
   */
  public CriteriumModifyPanel(Criterium c, Hierarchy h,CriteriumShowPanel csp,AHPEngine window2) {
    super();
    this.c=c;
    this.h=h;
    this.csp=csp;
    this.window=window2;
   
    this.spl=new ScrollPaneLayout();
    spl.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    spl.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
    this.setLayout(spl); 
    
    JPanel content=new JPanel(new FlowLayout());
    (getViewport()).add(content);

    //new BorderLayout()

    if (c!=null){
    int n=c.getNb_sons();
    this.size=n;
    //System.out.println("NB SONS"+n);
    n=n*(n-1)/2;
    int k=0;
    //System.out.println("NB PairwiseComparison"+n);
    for(int i=0;i<c.getNb_sons();i++){
      for(int j=i+1; j<c.getNb_sons();j++){
	//System.out.println("i : "+i+"   j : "+j);	  
	  ComparisonPane current_pane;
	  if (k!=(n-1) && k!=0) {current_pane= new ComparisonPane(c,i,j,false,csp,window2);}//Paint label only for the last JSlider
	  else current_pane= new ComparisonPane(c,i,j,true,csp,window2);
	  
	  //if (k!=(n-1)) {current_pane= new ComparisonPane(c,i,j,false,csp,window);}//Paint label only for the last JSlider
	  //else current_pane= new ComparisonPane(c,i,j,true,csp,window);
	  content.add(current_pane);
	  k++;
	}
      }
    }
  }

  /**
   * Describe <code>getPreferredSize</code> method here.
   *
   * @return a <code>Dimension</code> value
   * @see  <code>Container</code>
   */
    public Dimension getPreferredSize(){
      return new Dimension(100*(size-1)+500,400);
    }

  /**
   * Describe <code>getMinimumSize</code> method here.
   *
   * @return a <code>Dimension</code> value
   * @see  <code>Container</code>
   */
  public Dimension getMinimumSize(){
    return new Dimension(100*(size-1)+500,350);
}

  /**
   * <code>main</code> method to test this class.
   * @param Criterium :  command line
   * 
   */
  public static void main(String s[]) {
    JFrame frame = new JFrame("CriteriumPane");
    
    frame.addWindowListener(new WindowAdapter() {
	public void windowClosing(WindowEvent e) {System.exit(0);}
      });

    OwnTest test=new OwnTest();
    Hierarchy h=test.getHierarchyExample();
    CriteriumModifyPanel panel = new CriteriumModifyPanel((h.getGoal()).getSubcriteriumAt(0),h,null,null);	
    frame.getContentPane().add(panel);
    frame.pack();
    frame.setVisible(true);
  }
}
