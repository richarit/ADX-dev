// Graphical User Interface
package gui;

//imports
import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
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
	//private String reloc_GPL;
	private String reloc_icon;
	private String reloc_elep;
	private String reloc_sei;
	
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
  public About(JFrame frame,String fname_icon,String fname_elep, String fname_sei, String l2string) {//throws IOException  {
		super(frame, "About..", true);

		Toolkit toolkit = Toolkit.getDefaultToolkit();
		//Image image1 = toolkit.getImage(image1ageFile1.toString());

		//System.err.println(file_mail.toString());
		reloc_elep = new String("related" + File.separator + fname_elep); // relative path used by getResourceAsStream
		reloc_icon = new String("related" + File.separator + fname_icon); // 
		reloc_sei = new String("related" + File.separator + fname_sei); // relative path used by getResourceAsStream
		
		// work with inputstreams needed for JAR, or imageinputstreams in this case
		InputStream elep_is = this.getClass().getResourceAsStream(reloc_elep);
		InputStream icon_is = this.getClass().getResourceAsStream(reloc_icon);
		InputStream sei_is  = this.getClass().getResourceAsStream(reloc_sei);
		JPanel panel = new JPanel(new BorderLayout(20,20));
		
		try {
			ImageInputStream elep_iis = ImageIO.createImageInputStream(elep_is);
			ImageInputStream icon_iis = ImageIO.createImageInputStream(icon_is);
			ImageInputStream sei_iis = ImageIO.createImageInputStream(sei_is);
			
			//ImageIcon icon = new ImageIcon(reloc_icon);  //file 0 (weadapt)
		
			
		
		
			
			//System.err.println(imageFile1.toString());
			BufferedImage elepimg = ImageIO.read(elep_iis);
			BufferedImage weADAPTimg = ImageIO.read(icon_iis);
			BufferedImage seiimg = ImageIO.read(sei_iis);
			// use prepared image for the about box - this is the GPL info
			
			
		
			ImageIcon elepII = new ImageIcon(elepimg) ;
			ImageIcon weADAPTII = new ImageIcon(weADAPTimg) ;
			ImageIcon seiII = new ImageIcon(seiimg) ;
			JLabel lel = new JLabel(scale(elepII.getImage(), 0.1));
			
			
			
		

		JLabel lwe = new JLabel(l2string,weADAPTII, JLabel.CENTER); //file 0

		//label1.setBorder(new EmptyBorder(5,5,5,5));
		//g.drawImage(icon, 0, 0, null);

		panel.add(lel,BorderLayout.NORTH);
		panel.add(lwe,BorderLayout.CENTER); //file 0
		} catch (IOException ex) {
	        // no application registered for PDFs
	    	JOptionPane.showMessageDialog(panel, "There was a loading error with the file:\n"
					+ "user-guide.pdf" + "\n"
					+ "It can be found at :\n"
					+ "http://weadapt.org/knowledge-base/adaptation-decision-making/adx-user-guide",
					"Warning Message",
					JOptionPane.ERROR_MESSAGE);
	    }
			
		

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
	
	private ImageIcon scale(Image src, double scale) {
			//System.out.println("Entered scale method");
	        int w = (int)(scale*src.getWidth(this));
	        int h = (int)(scale*src.getHeight(this));
	        int type = BufferedImage.TYPE_INT_RGB;
	        BufferedImage dst = new BufferedImage(w, h, type);
	        Graphics2D g2 = dst.createGraphics();
	        g2.drawImage(src, 0, 0, w, h, this);
	        g2.dispose();
	        return new ImageIcon(dst);
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




