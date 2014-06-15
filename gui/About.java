// Graphical User Interface
package gui;

//imports
import javax.imageio.ImageIO;
import javax.swing.*;          //This is the final package name.
//import com.sun.java.swing.*; //Used by JDK 1.2 Beta 4 and all
//Swing releases before Swing 1.1 Beta 3.
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.*;
import java.io.*;



/**
 * Dialog Window to know some information about authors..
 * @author Maxime MORGE <A HREF="mailto:morge@emse.fr">morge@emse.fr</A> 
 * 
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

public class About extends JDialog {


  /**
   * Constructor
   * @param frame the main one
   * @param tree to modify
   * @param file_mail which contain mail icon
   * @param imageFile1 author1
   */
  public About(JFrame frame,File file_mail,File imageFile1) throws IOException {
		super(frame, "About..", true);

		Toolkit toolkit = Toolkit.getDefaultToolkit();
		//Image image1 = toolkit.getImage(image1ageFile1.toString());

		//System.err.println(file_mail.toString());
		ImageIcon icon = new ImageIcon(file_mail.toString());

		
		JPanel panel = new JPanel(new BorderLayout(20,20));
		//System.err.println(imageFile1.toString());
		
		
		// use prepared image in the about box
		String path = "related/GPL2-2.png";
        BufferedImage GPLI = ImageIO.read(new File(path));
        ImageIcon GPLII = new ImageIcon(GPLI) ;
        
        JLabel lGPL = new JLabel(GPLII, JLabel.CENTER);
        //lGPL.
		
		JPanel label =  new JPanel(new BorderLayout(20,20));
		//JLabel label2 = new JLabel("Written by Maxime MORGE  morge@emse.fr",icon, JLabel.CENTER);

		//label1.setBorder(new EmptyBorder(5,5,5,5));
		
		panel.add(lGPL,BorderLayout.NORTH);
		//panel.add(label2,BorderLayout.CENTER);
		lGPL.setPreferredSize(new Dimension(100,100));
		lGPL.revalidate();
		lGPL.repaint();
		JButton ok = new JButton("OK");
		ok.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ferme();
			}
		});
		panel.add(ok,BorderLayout.SOUTH);
		this.setContentPane(panel);
		this.pack();
	}
  // overloaded constructor. With this one the label can be set
  public About(JFrame frame,File file_mail,File imageFile1, String l2string) {//throws IOException  {
		super(frame, "About..", true);

		Toolkit toolkit = Toolkit.getDefaultToolkit();
		//Image image1 = toolkit.getImage(image1ageFile1.toString());

		//System.err.println(file_mail.toString());
		ImageIcon icon = new ImageIcon(file_mail.toString());  //file 0 (weadapt)
		
		JPanel panel = new JPanel(new BorderLayout(20,20));
		//System.err.println(imageFile1.toString());
		ImageIcon photo =  new ImageIcon(imageFile1.toString()); // file 1 - elephant

		JLabel photol= new JLabel(photo);
		//JPanel label =  new JPanel(new BorderLayout(20,20));
		JLabel label2 = new JLabel(l2string,icon, JLabel.CENTER); //file 0

		//label1.setBorder(new EmptyBorder(5,5,5,5));
		//g.drawImage(icon, 0, 0, null);

		panel.add(photol,BorderLayout.NORTH);
		panel.add(label2,BorderLayout.CENTER); //file 0

		JButton ok = new JButton("OK");
		ok.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ferme();
			}
		});
		panel.add(ok,BorderLayout.SOUTH);
		this.setContentPane(panel);
		this.pack();
	}
  /**
   * Method to close the Window 
   * @param Graphics g
   */
  void ferme() {
    this.setVisible(false);
  }
}
/**
 * ImagePanel Class to show picture.
 * @author Maxime MORGE <A HREF="mailto:morge@emse.fr">morge@emse.fr</A> 
 */
class ImagePanel extends JPanel {
    Image image1;
    Image image2;

    public ImagePanel(Image image1,Image image2) {
        this.image1 = image1;
        this.image2 = image2;
    }

  /**
   * Method to paint the Component 
   * @param Graphics g
   */
    public void paintComponent(Graphics g) {
        super.paintComponent(g); //paint background

        //Draw image at its natural size first.
        g.drawImage(image1, 10, 0, 110,140,this); //85x62 image
        g.drawImage(image2, 180, 0, 110,140,this); //85x62 image
        //Now draw the image scaled.
        //g.drawImage(image1, 90, 0, 100, 60, this);
    }

  /**
   * Method to overload container method about size 
   * @see Container
   * @return dimension
   */
  public Dimension getMaximumSize(){
    return (new Dimension(320,140));
  }

  /**
   * Method to overload container method about size 
   * @see Container
   * @return dimension
   */    
  public Dimension getMinimumSize(){
    return (new Dimension(320,140));
  }

  /**
   * Method to overload container method about size 
   * @see Container
   * @return dimension
   */
  public Dimension getPreferredSize(){
    return (new Dimension(320,140));
	  //return (new Dimension(100,90));
  }



}



