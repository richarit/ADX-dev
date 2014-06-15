// Abstract Data Type
package adt ;


//imports
import javax.swing.*;          //This is the final package name.
//import com.sun.java.swing.*; //Used by JDK 1.2 Beta 4 and all
//Swing releases before Swing 1.1 Beta 3.
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;
import java.net.*;

//import Jama.*;


/**
 * Activity class
 * @author  Maxime MORGE <A HREF="mailto:morge@emse.fr">morge@emse.fr</A> 
 * @version Fev 13, 2003
 * @version March 26, 2003 final one
 * @version July 21, 2003
 *   
 * modified by Richard Taylor 2012 richard.taylor@sei-international.org
 */

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
public abstract class Activity implements Serializable,Cloneable{

  //ATTRIBUTS

  // information about the activity
  protected String comment;
  protected String name;
  protected URL url;
  
  /**
   * Get the value of name.
   * @return value of name.
   */
  public String getName() {
    return name;
  }
  
  /**
   * Set the value of name.
   * @param v  Value to assign to name.
   */
  public void setName(String  v) {
    this.name = v;
  }
   
  /**
   * Get the value of url.
   * @return value of url.
   */
  public URL getUrl() {
    return url;
  }
  
  /**
   * Set the value of url.
   * @param v  Value to assign to url.
   */
  public void setUrl(URL  v) {
    this.url = v;
  }
  
  /**
   * Gets the value of comment
   *
   * @return the value of comment
   */
  public String getComment() {
    return this.comment;
  }

  /**
   * Sets the value of comment
   *
   * @param argComment Value to assign to this.comment
   */
  public void setComment(String argComment){
    this.comment = argComment;
  }

  /**
   * Creates a new  <code>Alternative</code> 
   */
  public Activity(){
    comment = new String("Default comment...");
  }


  /**
   * <code>toString</code> Returns a short string representation of this Alternative
   *
   * @return a <code>String</code> value
   */
  public String toString(){
    return this.name;
  }

 
}
