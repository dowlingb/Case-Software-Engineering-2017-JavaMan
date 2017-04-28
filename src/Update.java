/**
 * @author Brennan McFarland
 * Update handles all updates to the documentation database, both automatic
 * and manual, including error checking and handling, logging, and formatting
 * downloaded documentation
 * */
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.net.URL;
import java.net.URLConnection;
import java.net.MalformedURLException;
import java.util.logging.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.lang.StringBuilder;

public class Update
{
  static boolean autoUpdate;
  static ManPage[] docs;
  static int minAutoUpdateTime = 0; //minimum time to trigger automatic update
  static Logger logger = Logger.getLogger("JavaMan.Update");
  static Handler fileHandler;
  static LoggingFormatter formatter = new LoggingFormatter();
  static LocalDateTime lastSuccessUpdate;
  //added a new boolean isUI to determine if it's in UI.   @author Yiming Pan. 
  static boolean isUI = false;
  static userInterface ui = null;

/*
CheckAutoUpdateCondition() is called from main before every query to set a flag
if enough time has passed between the time the method is called and the last
successful update in the log file.  It compares the system clock to the
timestamp of the last successful update, if one exists, and if enough time has
elapsed sets the update flag accordingly
*/
  public static boolean checkAutoUpdateCondition()
  {
    //TODO: check system clock with last update time in log file to see if
    //update needed, rn just runs always
	try
	{
		BufferedReader reader = new BufferedReader(new FileReader("update.log"));
		String lastsuccessfulupdatestr;
		lastsuccessfulupdatestr = reader.readLine().substring(0, 27);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		LocalDateTime dateTime = LocalDateTime.parse(lastsuccessfulupdatestr);
	} catch (IOException e) {
		JavaMan.print("Error checking automatic update condition.");
		e.printStackTrace();
	}
	
    int timeSinceLastUpdate = LocalDateTime.now().compareTo(LocalDateTime.now());
    //-1 if the datetimes are =
    if(timeSinceLastUpdate >= minAutoUpdateTime || timeSinceLastUpdate == -1)
    {
    	JavaMan.print("Triggered automatic update...");
      autoUpdate = true;
      return true;
    }
    autoUpdate = false;
    return false;
  }

  public static void update(boolean isManual)
  {
    if(checkInternetConnection())
      downloadDocs();
  }

  /*
  check internet connectivity to determine if updated documentation can be
  downloaded
  */
  private static boolean checkInternetConnection()
  {
    try
    {
      URL url = new URL("http://docs.oracle.com/javase/8/docs/api/");
      try
      {
        URLConnection conn = url.openConnection();
        conn.connect();
      }catch(IOException cantconnect)
      {
        displayErrorNoInternet();
        return false;
      }
    }catch(MalformedURLException malformedurl)
    {
    	JavaMan.print("Error: malformed URL");
      System.exit(1);
    }
    return true;
  }

  private static void downloadDocs()
  {

	  JavaMan.print("Downloading documentation...");
    //run the docScraper script to pull webpage data
    Runtime rt = Runtime.getRuntime();
    //try to run the windows version, if that fails try linux
    try{
        Process proc = rt.exec("cmd /c cd battest & start build.bat & exit");
        BufferedReader input = new BufferedReader(new InputStreamReader(proc.getInputStream()));
        String line=null;
        while((line=input.readLine()) != null) {
          JavaMan.print(line);
        }
        int exitVal = proc.waitFor();
        JavaMan.print("Exited with error code "+exitVal);
    }catch(Exception failedWindows){
    	try
    	{
    		Process proc = rt.exec("cmd /c cd battest & start build.bat");
            BufferedReader input = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            String line=null;
            while((line=input.readLine()) != null) {
              JavaMan.print(line);
            }
            int exitVal = proc.waitFor();
            JavaMan.print("Exited with error code "+exitVal);
    	}catch(Exception failedLinux)
    	{
    		logUpdate(false);
    		failedLinux.printStackTrace();
    		return;
    	}
    }
    
    JavaMan.print("Formatting documentation...");
    //read from that JSON file and convert to plaintext
    try
    {
    	//standard terminal window is 80 chars across
    	JSONParser parser = new JSONParser();
    	String manpagetext;
    	ManPage manpage;
    	JSONArray a = (JSONArray) parser.parse(new FileReader("Test.json"));
    	for(Object o : a)
    	{
    		//set up the manpage object and json object
    		JSONObject jClass = (JSONObject) o;
    		String classname = jClass.get("href").toString().replaceAll("/",".");
    		classname = classname.substring(0, classname.length() - 5);
    		//JavaMan.print(classname);
    		classname = processDocString(classname);
    		manpage = new ManPage(classname,"");
    		
    		//class information
    		manpage.append(classname.toUpperCase()+"(JAVA)", Justification.LEFT);
    		manpage.append(classname+"Class", Justification.CENTER);
    		manpage.append(classname.toUpperCase()+"(JAVA)", Justification.RIGHT);
    		manpage.append("\n\n", Justification.LEFT);
    		
    		String pathname = (String)jClass.get("href");
    		
    		//path information
    		manpage.append("PATH\n", Justification.LEFT);
    		manpage.append(formatPath(pathname), Justification.FIRSTINDENT);
    		manpage.append("\n\n", Justification.LEFT);
    		
    		JSONArray jConstructors = (JSONArray) jClass.get("constructors");
    		
    		//constructor information
    		manpage.append("CONSTRUCTORS\n", Justification.LEFT);
    		for(Object b : jConstructors)
    		{
    			JSONObject jConstObj = (JSONObject) b;
    			String constructorName = (String)jConstObj.get("name");
    			constructorName = processDocString(constructorName);
    			String constructorDesc = (String)jConstObj.get("description");
    			constructorDesc = processDocString(constructorDesc);
    			
    			manpage.append(constructorName+"\n", Justification.FIRSTINDENT);
    			manpage.append(constructorDesc+"\n", Justification.SECONDINDENT);
    		}
    		
    		JSONArray jMethods = (JSONArray) jClass.get("methods");
    		
    		//method information
    		manpage.append("\nMETHODS\n", Justification.LEFT);
    		for(Object b : jMethods)
    		{
    			JSONObject jMethodObj = (JSONObject) b;
    			String methodName = (String) jMethodObj.get("name");
    			methodName = processDocString(methodName);
    			String methodDesc = (String) jMethodObj.get("description");
    			methodDesc = processDocString(methodDesc);
    			String modAndType = (String) jMethodObj.get("modAndType");
    			modAndType = processDocString(modAndType);
    			
    			manpage.append(modAndType+" "+methodName+"\n", Justification.FIRSTINDENT);
    			manpage.append(methodDesc+"\n", Justification.SECONDINDENT);
    		}
    		manpage.writeFile();
    	}
    } catch (FileNotFoundException e)
    {
    	JavaMan.print("Error: JSON file not found");
    	logUpdate(false);
    	return;
    } catch (IOException e)
    {
    	JavaMan.print("Error: IOException reading from JSON file");
    	logUpdate(false);
    	return;
    } catch (ParseException e)
    {
    	JavaMan.print("Error: Cannot parse JSON file");
    	logUpdate(false);
    	return;
    }
    
    //put that formatted text data into a ManPage object and call its write method
    logUpdate(true);
    JavaMan.print("Finished updating documentation.");
  }

  //scrubs a scraped doc string of newlines and other unwanted formatting
  private static String processDocString(String rawDocText)
  {
	  String processedDocText = rawDocText;
	  processedDocText = processedDocText.replace("\n", " ");
	  processedDocText = processedDocText.replace("\t", " ");
	  processedDocText = processedDocText.replaceAll("( )+", " ");
	  return processedDocText;
  }
  
  //converts system path from href to library path format
  private static String formatPath(String href)
  {
	  String formattedPath = href.replace('/', '.');
	  formattedPath = formattedPath.substring(0, formattedPath.length()-5); //.html
	  return formattedPath;
  }
  
  private void downloadDoc(String url)
  {

  }

  private static void formatDocs()
  {

  }

  private void formatDoc()
  {

  }

  private static void updateLocalDocs()
  {

  }

  private void updateLocalDoc()
  {

  }

  private static void storeIncompleteUpdatedDocs()
  {

  }

  private void storeIncompleteUpdatedDoc()
  {

  }

  /**
   * Logs if the update is successful or unsuccessful
   */
  private static void logUpdate(boolean success)
  {
    if(fileHandler == null)
    {
      try{
        fileHandler = new FileHandler("update.log");
        fileHandler.setFormatter(formatter);
        logger.addHandler(fileHandler);
      } catch(Exception e){
        e.printStackTrace();
      }
    }
    logger.info("Was successful: " + success);
    if(success == false)
      lastSuccessUpdate = LocalDateTime.now();
  }

  private static void displayErrorNoInternet()
  {
	  JavaMan.print("Error: Cannot connect to the online documentation for"
      + " update");
  }

  public static void setUI(userInterface importedUI){
	  isUI = true;
	  ui = importedUI;
  }
  
  public static void main(String []args)
  {
	downloadDocs();
    /*checkAutoUpdateCondition();
    if(autoUpdate == true) //TODO: or manual user input
    {
      if(checkInternetConnection() == true)
        update(autoUpdate);
    }*/
  }
}
