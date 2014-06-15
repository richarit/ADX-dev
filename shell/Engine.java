package shell;
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
public enum Engine {
	
// constructor holds author information
    VOTING ("Richard Taylor", "richard.taylor@sei-international.org"),
    AHP   ("Maxime MORGE", "morge@emse.fr");
    
    private final String author;   // author's name
    private final String email; //  author's email
    
    Engine(String author, String email) {
        this.author = author;
        this.email = email;
    }
    private String author() { return author; }
    private String email() { return email; }

    // return a string with one whitespace
    String authoremail() {
        return author.concat("").concat(email);
    }
    
 
}