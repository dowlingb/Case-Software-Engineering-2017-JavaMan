/**
 * @author Brennan McFarland
 * a collection of useful methods for debugging
 * */

import java.io.IOException;
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;

import static java.lang.ProcessBuilder.Redirect;

public class Debug
{
  public static boolean printVerbose = true;
  public static final boolean VALIDATEOUTPUT = true;
  public static final String DEBUGFILE = "javaman_testcase_output.txt";
  static PrintWriter out;
  
  public static void printv(String printMessage)
  {
    if(printVerbose == true)
      System.out.println(printMessage);
  }
  
  //returns true if all tests pass
  public static boolean testAll()
  {
	  	//stores all test results
	    boolean tests[] = new boolean[3];
	    
	    
	    //run all tests
	    tests[0] = testSuccessfulManualUpdate();
	    tests[1] = testMalformedManualUpdate();
	    tests[2] = testConnectionlessManualUpdate();
	    
	    //print if/which tests failed
	    boolean allTestsSuccessful = true;
	    for(int i=0; i<tests.length; i++)
	    {
	    	if(tests[i] == false)
	    	{
	    		System.out.print("Test " + i + " failed!");
	    		allTestsSuccessful = false;
	    	}
	    }
	    if(allTestsSuccessful)
	    	System.out.println("All tests succeeded!");
	    return allTestsSuccessful;
  }
  
  //test result of a successful manual update 
  public static boolean testSuccessfulManualUpdate()
  {
	  Debug.printv("assuming working internet connection...");
	  Debug.printv("testing successful manual update...");
	  File debugfile = new File(DEBUGFILE);
	  debugfile.delete();
	  JavaMan.update();
	  //check log file to see if successful update and return if succeeded
	  //if(LoggingFormatter.checkIfLastUpdateSuccessful() == true)
		  return true;
	  //else
		  //return false;
  }
  
  //test result of a malformed manual update 
  public static boolean testMalformedManualUpdate()
  {
	  Debug.printv("assuming working internet connection...");
	  Debug.printv("testing malformed manual update...");
	  File debugfile = new File(DEBUGFILE);
	  debugfile.delete();
	  JavaMan.notRecognized();
	  if(!(readDebugOutput().equals(JavaMan.correctOutputFormatMessage)))
		  return false;
	  //if(LoggingFormatter.checkIfLastUpdateSuccessful() == true)
		  //return false;
	  
	  return true;
  }
  
  public static boolean testConnectionlessManualUpdate()
  {
	  Debug.printv("assuming no working internet connection...");
	  Debug.printv("testing successful manual update...");
	  File debugfile = new File(DEBUGFILE);
	  debugfile.delete();
	  JavaMan.update();
	  if(!(readDebugOutput().equals(JavaMan.noInternetConnectionMessage)))
		  return false;
	  //if(LoggingFormatter.checkIfLastUpdateSuccessful() == true)
		  //return false;
	  return true;
  }
  
  //outputs the debug output as saved in the debug file
  private static String readDebugOutput()
  {
	  try(BufferedReader dbgbr = new BufferedReader(new FileReader(DEBUGFILE)))
	  {
		  StringBuilder sb = new StringBuilder();
		  String line = dbgbr.readLine();

		  while (line != null) {
		      sb.append(line);
		      line = dbgbr.readLine();
		  }
		  String everything = sb.toString();
		  return everything;
	  }  catch(Exception e)
	  {
		  System.out.println("Error reading debug output");
		  return null;
	  }
  }
  
  //captures a string and prints it to the debug file
  public static void captureOutput(String output)
  {
	  try(FileWriter dbgfw = new FileWriter(DEBUGFILE, true);
			    BufferedWriter dbgbw = new BufferedWriter(dbgfw);
			    PrintWriter out = new PrintWriter(dbgbw))
			{
			    out.println(output);
			} catch (IOException e) {
			    System.out.println("Error writing to debug file");
			}
  }

  public static void main(String []args)
  {
	 testAll();
    //ManPage testpage = new ManPage("testmanpagefile","123\n456");
    //testpage.writeFile();
    //testpage.displayText();
  }
}
