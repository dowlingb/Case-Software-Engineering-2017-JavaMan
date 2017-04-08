/**
 * @author Brennan McFarland
 * a collection of useful methods for debugging
 * */
public class Debug
{
  static boolean printVerbose = true;

  public static void printv(String printMessage)
  {
    if(printVerbose == true)
      System.out.println(printMessage);
  }
  
  public static boolean testSuccessfulManualUpdate()
  {
	  Debug.printv("assuming working internet conneciton...");
	  Debug.printv("testing successful manual update...");
	  JavaMan.update(); //triggers manual update
	  //check log file to see if successful update and return if succeeded
	  return true;
  }

  public static void main(String []args)
  {
    ManPage testpage = new ManPage("testmanpagefile","123\n456");
    testpage.writeFile();
    testpage.displayText();
  }
}
