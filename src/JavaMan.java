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
  public static JSONArray classArray;
  public static String correctOutputFormatMessage = "The correct format for printing "
		  + "commands is blah blah blah";
  public static String noInternetConnectionMessage = "There is no network connection.  "
		  + "Please connect to the internet so updates can complete.";
  
  //added a new boolean isUI to determine if it's in UI.   @author Yiming Pan. 
  private static boolean isUI = false;
  private static userInterface ui = null;
  
  
  public static void main(String[] args) {
   init();
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
    else if(args[0].equals("accessclass") && args.length>=2)
    {
    	accessClass(args[1]);
    	return;
    }
    else
    {
      notRecognized();
    }
   }
  }

  public static String help()
  {
   String message = "Enter command to update using \"update\" or access a class using \"access <class path>\" where class path is formatted as java.lang.String";
   System.out.println(message);
   return message;
	  
  }

  public static void notRecognized()
  {
   String message = "Command not recognized.";
   print(message);
   //return message;
  }
  public static void access(String path)
  {
    String filePath = path.replaceAll("\\.","/");
    filePath=filePath+".html";
	 for(Object o : classArray)
	 {
	  JSONObject jClass = (JSONObject) o;
	  //System.out.println(jClass.get("name"));
	  if(jClass.get("href").toString().equals(filePath)){
		  print("Class: ");
		  print("\t" + jClass.get("name"));
  		  JSONArray jConstructors = (JSONArray) jClass.get("constructors");
  		  print("Constructors: ");
  		  for(Object b : jConstructors)
  		  {
  			  JSONObject jConstObj = (JSONObject) b;
  			  String constructorName = (String)jConstObj.get("name");
  			  String constructorDesc = (String)jConstObj.get("description");
  			  print("\t" + (constructorName + " : " + constructorDesc).replaceAll("\n|\t| (?= )", ""));
  		  }
  		  
  		  JSONArray jMethods = (JSONArray) jClass.get("methods");
  		  System.out.println("Methods: ");
  		  for(Object b : jMethods)
  		  {
  			  JSONObject jMethodObj = (JSONObject) b;
  			  String methodName = (String) jMethodObj.get("name");
  			  String methodDesc = (String) jMethodObj.get("description");
  			  String modAndType = (String) jMethodObj.get("modAndType");
  			  print("\t" + (methodName + " : " + methodDesc).replaceAll("\t|\n| (?= )", ""));
  			  print("\t\t Returns: " + (modAndType).replaceAll("\n|\t| (?= )", ""));
  		  }
		 }
	  }
	  
  }
  
  public static void accessClass(String classStr){
	  for(Object o : classArray)
	  {
		  JSONObject jClass = (JSONObject) o;
		  //System.out.println(jClass.get("name"));
		  if(jClass.get("name").toString().equals(classStr)){
			  print("Class: ");
			  print("\t" + jClass.get("name"));

    		  JSONArray jConstructors = (JSONArray) jClass.get("constructors");
    		  print("Constructors: ");
    		  for(Object b : jConstructors)
    		  {
    			  JSONObject jConstObj = (JSONObject) b;
    			  String constructorName = (String)jConstObj.get("name");
    			  String constructorDesc = (String)jConstObj.get("description");
    			  print("\t" + (constructorName + " : " + constructorDesc).replaceAll("\n|\t| (?= )", ""));
    		  }
    		  
    		  JSONArray jMethods = (JSONArray) jClass.get("methods");
    		  System.out.println("Methods: ");
    		  for(Object b : jMethods)
    		  {
    			  JSONObject jMethodObj = (JSONObject) b;
    			  String methodName = (String) jMethodObj.get("name");
    			  String methodDesc = (String) jMethodObj.get("description");
    			  String modAndType = (String) jMethodObj.get("modAndType");
    			  print("\t" + (methodName + " : " + methodDesc).replaceAll("\t|\n| (?= )", ""));
    			  print("\t\t Returns: " + (modAndType).replaceAll("\n|\t| (?= )", ""));
    		  }
		  }
	  }
  }

  public static void update()
  {
    Update.update(true);
  }
  
  private static void print(String msg){
	  if(isUI==false){
		  System.out.println(msg);
	  }
	  else{
		  ui.print(msg);
	  }
	  if(Debug.VALIDATEOUTPUT == true)
	  {
		  Debug.captureOutput(msg);
	  }
	}
  
  public void setUI(userInterface importedUI){
	  isUI = true;
	  ui = importedUI;
	  Update.setUI(importedUI);
  }
  
  public static void init()
  {
	  try
      {
    	  JSONParser parser = new JSONParser();
    	  classArray = (JSONArray) parser.parse(new FileReader("Test.json"));
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
  
  	public JavaMan(){
  		init();
  	}
  }
