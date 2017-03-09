/**
 * @author Brendan Dowling
 * This is the main class for accessing and running the javaman program
 * */
  import java.io.File;
public class JavaMan
{
  
  public static void main(String[] args) {
   if(args==null || args[0].isEmpty())
   {
     notRecognized();
     return;
   }
   else
   {
    if(args[0].equals("update"))
    {
     update();
     return;
    }
    else if(args[0].equals("access")&&args.length>=2)
    {
      access(args[1]);
      return;
    }
    else
    {
      notRecognized();
    }
   }
  }
  
  public static void notRecognized()
  {
   System.out.println("Command not recognized. Please enter command to update using \"update\" or access a class using \"access <class path>\" where class path is formatted as java.lang.String");
  }
  
  public static void access(String path)
  {
    System.out.println(path);
  }
  
  public static void update()
  {
    System.out.println("Not yet implemented");
  }
}
