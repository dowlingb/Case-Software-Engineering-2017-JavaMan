/**
 * @author Brennan McFarland
 * ManPage handles all data associated with a single man page, holds text data
 * to be read/written, and methods to output file contents
 * */

public class ManPage
{
  String pageText;
  String fileName;
 
  public ManPage(String file, String page) 
  {
    pageText = page;
    fileName = file;
  }

  public void displayText()
  {
     System.out.println(pageText);
     System.out.println(pageText);
  }

  public void readFile()
  {

  }

  public void writeFile()
  {

    
  }
}
