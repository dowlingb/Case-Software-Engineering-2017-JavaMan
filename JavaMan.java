/**
 * @author Brendan Dowling
 * This is the main class for accessing and running the javaman program
 * */
  import java.io.*;
public class JavaMan
{
  public static String home = "c:" + File.separatorChar;
  
  public static void main(String[] args) {
   if(args==null||args.length == 0 || args[0] == null)
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
    else if(args[0].equals("help"))  
    {
      help();
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
  
  public static void help()
  {
   System.out.println("Enter command to update using \"update\" or access a class using \"access <class path>\" where class path is formatted as java.lang.String");
  }
  
  public static void notRecognized()
  {
   System.out.println("Command not recognized.");
  }
  public static void access(String path)
  {
    String filePath = home + path.replaceAll("\\.","\\"+Character.toString(File.separatorChar));
    filePath=filePath+".txt";
    File inFile = new File(filePath);
    try
    {
      BufferedReader br = new BufferedReader(new FileReader(inFile));
      
      StringBuilder sb = new StringBuilder();
      String line = br.readLine();
      
      while (line != null)
      {
        sb.append(line);
        sb.append(System.lineSeparator());
        line = br.readLine();
      }
      String everything = sb.toString();
      System.out.println(everything);
      br.close();
    } 
    catch (Exception E)
    {
      notRecognized(); // I realize this is a very general fix but if it doesnt conform to the exact format then it wont work and therefore this will show up.
    }
  }
  
  public static void update()
  {
    System.out.println("Not yet implemented");
  }
}
