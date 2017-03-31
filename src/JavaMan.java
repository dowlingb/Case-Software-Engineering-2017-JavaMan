/*
 * @author Brendan Dowling
 * This is the main class for accessing and running the javaman program
 * */
  import java.io.*;
  import java.io.FileNotFoundException;
  import java.io.FileReader;
  import java.io.IOException;
  import org.json.simple.JSONArray;
  import org.json.simple.JSONObject;
  import org.json.simple.parser.JSONParser;
  import org.json.simple.parser.ParseException;
  
public class JavaMan
{
  public static String home = File.listRoots()[0].toString();//hypothetically should work indepentant of operating system as long as the files are in the home folder
  //"c:" + File.separatorChar; <for windows

  public static void main(String[] args) {
   /*if(args==null||args.length == 0 || args[0] == null)
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
   }*/ init();
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
    //Update.update(true);
  }
  
  public static void init()
  {
	  try
      {
    	  JSONParser parser = new JSONParser();
    	  JSONArray a = (JSONArray) parser.parse(new FileReader("Test.json"));
    	  for(Object o : a)
    	  { 
    		  JSONObject jClass = (JSONObject) o;
    		  String classname = (String)jClass.get("name");
    		  System.out.println("Class: ");
    		  System.out.println("\t" + classname);

    		  JSONArray jConstructors = (JSONArray) jClass.get("constructors");
    		  System.out.println("Constructors: ");
    		  for(Object b : jConstructors)
    		  {
    			  JSONObject jConstObj = (JSONObject) b;
    			  String constructorName = (String)jConstObj.get("name");
    			  String constructorDesc = (String)jConstObj.get("description");
    			  System.out.println("\t" + constructorName + " : " + constructorDesc);
    		  }
    		  
    		  JSONArray jMethods = (JSONArray) jClass.get("methods");
    		  System.out.println("Methods: ");
    		  for(Object b : jMethods)
    		  {
    			  JSONObject jMethodObj = (JSONObject) b;
    			  String methodName = (String) jMethodObj.get("name");
    			  String methodDesc = (String) jMethodObj.get("description");
    			  String modAndType = (String) jMethodObj.get("modAndType");
    			  System.out.println("\t" + methodName + " : " + methodDesc);
    			  System.out.println("\t\t Returns: " + modAndType);
    		  }
          }
      } catch (FileNotFoundException e)
      {
          e.printStackTrace();
      } catch (IOException e)
      {
          e.printStackTrace();
      } catch (ParseException e)
      {
          e.printStackTrace();
      }
    }
  }
