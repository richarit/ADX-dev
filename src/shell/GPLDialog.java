package shell;
import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.border.EmptyBorder;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
// Typically connections are made from a user interface bean (the event source) to an application logic bean (the target).
import java.beans.EventHandler;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
//import java.awt.event.ActionListener;
import java.awt.BorderLayout;

import java.awt.Font;
import java.awt.font.FontRenderContext;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
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

public class GPLDialog extends JDialog {
	
	private InputStream is;
	private InputStream GPLis;
	private String reloc_GPL;
	private String reloc_icon;
	
	// constructor
	public GPLDialog (JFrame frame,String fname_icon, String fname_GPL) throws IOException {
		super(frame, "About..", true);

		Toolkit toolkit = Toolkit.getDefaultToolkit();
		
		// You cannot use File (or FileInputStream) to reference something from within a jarfile.
		// See getResourceAsStream and the ImageIO methods that read from an InputStream 
		//boolean dev = false;   // switch between development and jar version
		
		
		// path = "/home/richard/workspace/ADx/related/";  // absolute path for adx-dev
		//reloc = new String("related/" + fname);
		reloc_GPL = new String("related" + File.separator + fname_GPL); // relative path used by getResourceAsStream
		reloc_icon = new String("related" + File.separator + fname_icon); // 

		// Obtaining an ImageInputStream given an input source in the form of a File or InputStream
		is = this.getClass().getResourceAsStream(reloc_icon);
		GPLis = this.getClass().getResourceAsStream(reloc_GPL);
		
		if (is==null) System.out.println("is is null");
		
		ImageInputStream iis = ImageIO.createImageInputStream(is);
		ImageInputStream GPLiis = ImageIO.createImageInputStream(GPLis);
		
		// Once a source has been obtained, it may be attached to the reader by calling

		//BufferedReader reader = new BufferedReader(new InputStreamReader(GPLDialog.class.getResourceAsStream(file_icon.getAbsolutePath())));

		//JButton b = new JButton(new ImageIcon(GPLDialog.class.getResource(file_icon.getAbsolutePath())));

		//weadapt icon
		//ImageIcon icon = new ImageIcon(file_icon.toString());
		//System.out.println(file_icon.toString());
	
		JPanel panel = new JPanel(new BorderLayout(20,20));
		//System.err.println(imageFile1.toString());
		BufferedImage GPLI = ImageIO.read(GPLiis);
		BufferedImage weADAPTimg = ImageIO.read(iis);
		// use prepared image for the about box - this is the GPL info
		// BufferedImage GPLI = ImageIO.read(imageFile1);
		
		//BufferedImage GPLI = ImageIO.read(new File(path));
		ImageIcon GPLII = new ImageIcon(GPLI) ;
		ImageIcon weADAPTicon = new ImageIcon(weADAPTimg) ;
		JLabel lGPL = new JLabel(scale(GPLII.getImage(), 0.1));
		//JLabel lGPL = new JLabel(GPLII, JLabel.CENTER);
		lGPL.setBorder(new EmptyBorder(5,5,5,5));
		
		// add further label with credit information and the weadapt icon
		JLabel lweADAPT = new JLabel("Contact weADAPT <info@weadapt.org>",scale(weADAPTicon.getImage(), 0.2), JLabel.CENTER);
		// lweADAPT.setBorder(new EmptyBorder(5,5,5,5));
		// use the scale method
		panel.add(lGPL,BorderLayout.NORTH);
		
		panel.add(lweADAPT, BorderLayout.CENTER);

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
	
	//overloaded constructor. With this one the label can be set
	public GPLDialog(JFrame frame,File file_mail,File imageFile1, String l2string) {
		super(frame, "About..", true);

		Toolkit toolkit = Toolkit.getDefaultToolkit();
		//Image image1 = toolkit.getImage(image1ageFile1.toString());

		//System.err.println(file_mail.toString());
		ImageIcon icon = new ImageIcon(file_mail.toString());

	
		JPanel panel = new JPanel(new BorderLayout(20,20));
		//System.err.println(imageFile1.toString());
		ImageIcon photo =  new ImageIcon(imageFile1.toString());

		JLabel photol= new JLabel(photo);
		JPanel label =  new JPanel(new BorderLayout(20,20));
		JLabel label2 = new JLabel(l2string,icon, JLabel.CENTER);

		//label1.setBorder(new EmptyBorder(5,5,5,5));


		panel.add(photol,BorderLayout.NORTH);
		panel.add(label2,BorderLayout.CENTER);

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

