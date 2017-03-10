public class Debug
{
  static boolean printVerbose = true;

  public static void printv(String printMessage)
  {
    if(printVerbose == true)
      System.out.println(printMessage);
  }

  public static void main(String []args)
  {
    ManPage testpage = new ManPage("testmanpagefile","123");
    testpage.readFile();
    testpage.displayText();
  }
}
