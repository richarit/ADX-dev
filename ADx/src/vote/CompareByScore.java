package vote;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.jdom.Element;

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
public class CompareByScore implements Comparator<Element> {
	//public ArrayList<E> sort (Set set) {
	//	Iterator it = set.iterator();
	//    while (it.hasNext()) {
	//      Map.Entry entry = (Map.Entry) it.next();
	//      System.out.println("Option-" + entry.getKey() + "-->" + entry.getValue());
	//    }
   //     return o1.getName().compareTo(o2.getName());
   // }

		// compare two elements
	@Override
	public int compare(Element arg0, Element arg1) {
		// TODO Auto-generated method stub
		String arg0s = arg0.getChildText("score");
		String arg1s = arg1.getChildText("score");
		Double dd = new Double(Double.parseDouble(arg0s));
		return dd.compareTo(Double.parseDouble(arg1s));
	}

}
