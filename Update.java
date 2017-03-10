/**
 * @author Brennan McFarland
 * Update handles all updates to the documentation database, both automatic
 * and manual, including error checking and handling, logging, and formatting
 * downloaded documentation
 * */
import java.io.*;
import java.time.LocalDateTime;

public class Update
{
  static boolean autoUpdate;
  static ManPage[] docs;
  static int minAutoUpdateTime = 0; //minimum time to trigger automatic update

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
    int timeSinceLastUpdate = LocalDateTime.now().compareTo(LocalDateTime.now());
    //-1 if the datetimes are =
    if(timeSinceLastUpdate >= minAutoUpdateTime || timeSinceLastUpdate == -1)
    {
      Debug.printv("Triggered automatic update...");
      autoUpdate = true;
      return true;
    }
    autoUpdate = false;
    return false;
  }

  public static void update(boolean isManual)
  {
    downloadDocs();
  }

  private static void checkInternetConnection()
  {

  }

  private static void downloadDocs()
  {
    Debug.printv("Downloading documentation...");

    //run the docScraper script to pull webpage data
    Runtime rt = Runtime.getRuntime();
    try{
        Process proc = rt.exec("casperjs docScraper.js");
        BufferedReader input = new BufferedReader(new InputStreamReader(proc.getInputStream()));
        String line=null;
        while((line=input.readLine()) != null) {
          System.out.println(line);
        }
        int exitVal = proc.waitFor();
        System.out.println("Exited with error code "+exitVal);
    }catch(Exception ex){
        ex.printStackTrace();
    }
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

  private static void logUpdate()
  {

  }

  private static void displayErrorNoInternet()
  {

  }

  public static void main(String []args)
  {
    checkAutoUpdateCondition();
    if(autoUpdate == true) //TODO: or manual user input
      update(autoUpdate);
  }
}
