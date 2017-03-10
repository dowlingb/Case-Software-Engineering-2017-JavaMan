public class Debug
{
  static boolean printVerbose = true;

  public static void printv(String printMessage)
  {
    if(printVerbose == true)
      System.out.println(printMessage);
  }
}
