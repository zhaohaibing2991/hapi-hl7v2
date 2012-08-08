/**
The contents of this file are subject to the Mozilla Public License Version 1.1 
(the "License"); you may not use this file except in compliance with the License. 
You may obtain a copy of the License at http://www.mozilla.org/MPL/ 
Software distributed under the License is distributed on an "AS IS" basis, 
WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License for the 
specific language governing rights and limitations under the License. 

The Original Code is "ConfGen.java".  Description: 
"This Class is used to Generate a Class" 

The Initial Developer of the Original Code is University Health Network. Copyright (C) 
2001.  All Rights Reserved. 

Contributor(s): James Agnew
                Paul Brohman
                Mitch Delachevrotiere
                Shawn Dyck
  				Cory Metcalf
  				
Alternatively, the contents of this file may be used under the terms of the 
GNU General Public License (the  ?GPL?), in which case the provisions of the GPL are 
applicable instead of those above.  If you wish to allow use of your version of this 
file only under the terms of the GPL and not to allow others to use your version 
of this file under the MPL, indicate your decision by deleting  the provisions above 
and replace  them with the notice and other provisions required by the GPL License.  
If you do not delete the provisions above, a recipient may use your version of 
this file under either the MPL or the GPL. 

*/
package ca.uhn.hl7v2.conf.classes.generator.builders;

import java.io.*;
import ca.uhn.hl7v2.conf.classes.exceptions.*;

/** This Class is used to Generate a Class
 * @author <table><tr>James Agnew</tr>
 *                <tr>Paul Brohman</tr>
 *                <tr>Mitch Delachevrotiere</tr>
 *                <tr>Shawn Dyck</tr>
 * 				  <tr>Cory Metcalf</tr></table>
 */
public class ConfGen {

   /** this is the main method to start the Conformance Generator 
    * @param args the command line argument
    */
   public static void main(String[] args) {
	  ConfGen gc = new ConfGen();
      CommandParser cp = new CommandParser();

      cp.parse(args);

      if (cp.getHelpFlag()) {
         System.out.println("Usage: ConfGen [-vht] SOURCE DESTINATION PACKAGENAME");
         return;
      }

      if (cp.getErrFlag()) {
         System.out.println("ConfGen: command line parse error");
         System.out.println("ConfGen: " + cp.getError());
         return;
      }

      DeploymentManager dm = new DeploymentManager(cp.getDest(), cp.getPackage());
      if (cp.getTestFlag()) {
         System.out.println("ConfGen: system test enabled");
         gc.test();
         return;
      }
      if (cp.getVerbFlag()) {
         System.out.println("ConfGen: verbose display enabled");
         dm.setVerbose(true);
      }

      System.out.println("Generating Source...");
      gc.generateConf(dm, cp);
      System.out.println("Done.");
   }

   /** this method generates conformance
    * @param dm the DeploymentManager
    * @param cp the CommandParser which parses the command line argument of ConfGen 
    */
   public void generateConf(DeploymentManager dm, CommandParser cp) {
      try {
         File f = new File(cp.getSource());
         BufferedReader in = new BufferedReader(new FileReader(f));
         char[] cbuf = new char[(int) f.length()];
         in.read(cbuf, 0, (int) f.length());
         dm.generate(String.valueOf(cbuf));
      } catch (FileNotFoundException e) {
         System.out.println("Filenotfoundexception: " + e.toString());
      } catch (IOException e) {
         System.out.println("IOexception:\n" + e.toString() + "\n");
      } catch (ConformanceError e) {
         System.out.println("ConformanceError:\n" + e.toString() + "\n");
      } catch (ConformanceException e) {
      	 System.out.println("ConformanceException:\n" + e.toString() + "\n");
      }
   }

   /** this method tests the conformance generator to see if it is working
    */
   public void test() {
      try {
         System.out.print("Checking for XML Parser:");
         Class.forName("org.w3c.dom.Element");
         System.out.println("           PASS");
      } catch (ClassNotFoundException e) {
         System.out.println("           FAIL");
      }

      try {
         System.out.print("Checking for Xerces:");
         Class.forName("com.sun.org.apache.xerces.internal.parsers.DOMParser");
         System.out.println("               PASS");
      } catch (ClassNotFoundException e) {
         System.out.println("               FAIL");
      }

      try {
         System.out.print("Checking for Conformance Classes:");
         Class.forName("ca.uhn.hl7v2.conf.classes.generator.builders.ConformanceMessageBuilder");
         System.out.println("  PASS");
      } catch (ClassNotFoundException e) {
         System.out.println("  FAIL");
      }

      try {
         System.out.print("Checking for Apache Ant:");
         Class.forName("org.apache.tools.ant.Main");
         System.out.println("           PASS");
      } catch (ClassNotFoundException e) {
         System.out.println("           FAIL");
      }
   }
}
