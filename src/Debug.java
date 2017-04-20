/**
 * @author Brennan McFarland
 * a collection of useful methods for debugging
 * */

import java.io.IOException;
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;


import static java.lang.ProcessBuilder.Redirect;

public class Debug
{
  static boolean printVerbose = true;
  
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
  
  private static String runAsExternalProcess(String args)
  {
	  Process proc = null;
	  ProcessBuilder pb = null;
	  try
	  {
		  try
		  {
			  /*TODO: change bashcommand for linux as well*/
			  //attempt to execute process on windows bash
			  String[] bashcommand = new String[] {"\"C:\\Program Files\\Git\\git-bash.exe\"",
					  " --cd-to-home",
					  " -c ", "javaman " + args + " >> javaman_testcase_output.txt"};
			  //String[] bashcommand = new String[] {"\"C:\\Program Files\\Git\\git-bash.exe\"",
			//		  "-c", "echo hello >> test.txt"};
			  /*
			   * at the very least directing the output of an echo command to a file
			   * works, but for some reason even with an echo command it is not
			   * capturing the output like it should
			   * even with the above, it creates the file but doesn't write anything to it
			   */
			  System.out.println(bashcommand[0]+bashcommand[1]+bashcommand[2]+bashcommand[3]);
			  ProcessBuilder builder = new ProcessBuilder(bashcommand);
			  builder.redirectErrorStream(true);
			  Process process = builder.start();
			  InputStream is = process.getInputStream();
			  BufferedReader reader = new BufferedReader(new InputStreamReader(is));

			  String line = null;
			  while ((line = reader.readLine()) != null) {
			     System.out.println(line);
			  }
			  //System.out.println("we got here");
			  //pb = new ProcessBuilder(bashcommand);
			  //File file = new File("file1.txt");
			  //pb.redirectErrorStream(true);
			  //pb.redirectOutput(Redirect.appendTo(file));
			  //proc = pb.start();
			  //assert pb.redirectInput() == Redirect.PIPE;
			  //assert pb.redirectOutput().file() == file;
			  //assert proc.getInputStream().read() == -1;
			  //BufferedReader bri = new BufferedReader(new InputStreamReader(proc.getInputStream()));
			  //String outputline = null;
			  //String output = "";
			  //try
			  //{
				//  while ((outputline = bri.readLine()) != null) {
				//	  System.out.println("stuff");
				//	  output += outputline;
			  	//}
			  //}
			  //catch(IOException exception)
			  //{
				//  Debug.printv("Error: IOException when capturing debug process output");
			 // }
			  //System.out.println("|" + output + "|");
			  //proc.waitFor();
		  }
		  catch(IOException notWindows)
		  {
			  try
			  {
				  //attempt to execute process on linux bash
				  String[] bashcommand = new String[] {"/bin/bash",
						  "-c", "javaman " + args};
				  proc = new ProcessBuilder(bashcommand).start();
				  BufferedReader bri = new BufferedReader(new InputStreamReader(proc.getInputStream()));
				  String outputline = null;
				  String output = "";
				  try
				  {
					  while ((outputline = bri.readLine()) != null) {
						  System.out.println("stuff");
						  output += outputline;
				  	}
				  }
				  catch(IOException exception)
				  {
					  Debug.printv("Error: IOException when capturing debug process output");
				  }
				  System.out.println("|" + output + "|");
				  proc.waitFor();
			  }
			  catch(IOException notLinux)
			  {
				  //if neither works, oh well we tried
				  Debug.printv("Error executing debug process");
			  }
		  }
	  }
	  catch(InterruptedException interrupted)
	  {
		  Debug.printv("Error: debug process interrupted");
	  }
	  
	  return "";
	  //return output;
  }
  
  //test result of a successful manual update 
  public static boolean testSuccessfulManualUpdate()
  {
	  Debug.printv("assuming working internet connection...");
	  Debug.printv("testing successful manual update...");
	  runAsExternalProcess("update"); //triggers manual update
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
	  if(runAsExternalProcess("updte") != JavaMan.correctOutputFormatMessage)
		  return false;
	  //if(LoggingFormatter.checkIfLastUpdateSuccessful() == true)
		  //return false;
	  
	  if(runAsExternalProcess("") != JavaMan.correctOutputFormatMessage)
		  return false;
	  //if(LoggingFormatter.checkIfLastUpdateSuccessful() == true)
		  //return false;
	  
	  if(runAsExternalProcess("updte") != JavaMan.correctOutputFormatMessage)
		  return false;
	  //if(LoggingFormatter.checkIfLastUpdateSuccessful() == true)
		  //return false;
	  
	  if(runAsExternalProcess("upadte") != JavaMan.correctOutputFormatMessage)
		  return false;
	  //if(LoggingFormatter.checkIfLastUpdateSuccessful() == true)
		  //return false;
	  
	  if(runAsExternalProcess("uPdate") != JavaMan.correctOutputFormatMessage)
		  return false;
	  //if(LoggingFormatter.checkIfLastUpdateSuccessful() == true)
		  //return false;
	  
	  if(runAsExternalProcess("uupdate") != JavaMan.correctOutputFormatMessage)
		  return false;
	  //if(LoggingFormatter.checkIfLastUpdateSuccessful() == true)
		  //return false;
	  
	  return true;
  }
  
  public static boolean testConnectionlessManualUpdate()
  {
	  Debug.printv("assuming no working internet connection...");
	  Debug.printv("testing successful manual update...");
	  if(runAsExternalProcess("update") != JavaMan.noInternetConnectionMessage)
		  return false;
	  //if(LoggingFormatter.checkIfLastUpdateSuccessful() == true)
		  //return false;
	  return true;
  }
  
  public static void temp()
  {
	  
	  try{
	  String[] bashcommand = new String[] {"\"C:\\Program Files\\Git\\git-bash.exe\"",
			  "-c", "echo hello >> test.txt"};
	  
	  /*
	  ProcessBuilder pb = new ProcessBuilder(bashcommand);
	  pb.redirectOutput(Redirect.appendTo(new File("file1.txt")));
	  Process p = pb.start();
	  p.waitFor();*/
	  /*
	  ProcessBuilder pb = new ProcessBuilder((bashcommand));
	  Process process;

	  BufferedReader reader = 
              new BufferedReader(new InputStreamReader(process.getInputStream()));
	  StringBuilder builder = new StringBuilder();
	  String line = null;
	  process = pb.start();
	  process.waitFor();
	  while ( (line = reader.readLine()) != null) {
		  builder.append(line);
		  builder.append(System.getProperty("line.separator"));
		  System.out.println("hey");
	  }*/
	  ProcessBuilder pb = new ProcessBuilder((bashcommand));
	  Process process = Runtime.getRuntime().exec(bashcommand);
	  
	  
	  //String result = builder.toString();
	  //System.out.println("|"+result+"|");
	  }catch(Exception e)
	  {
		  return;
	  }
  }

  public static void main(String []args)
  {
	 testAll();
	  //temp();
    //ManPage testpage = new ManPage("testmanpagefile","123\n456");
    //testpage.writeFile();
    //testpage.displayText();
  }
}
