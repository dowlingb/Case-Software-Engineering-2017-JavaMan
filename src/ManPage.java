/**
 * @author Brennan McFarland
 * ManPage handles all data associated with a single man page, holds text data
 * to be read/written, and methods to output file contents
 * */
import java.io.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.lang.StringBuilder;

public class ManPage
{
  String pageText; //holds the formatted plaintext man page file contents
  String fileName;
  static final int WINDOWWIDTH = 80;
  static final int FIRSTINDENT = 7;
  static final int SECONDINDENT = 4;

  public ManPage(String file, String page)
  {
    pageText = page;
    fileName = file;
  }

  public void displayText()
  {
     System.out.println(pageText);
  }

  /*
   * NOTE: change this to a stringbuilder later for efficiency
   */
  public void append(String additionalPageText, Justification justification)
  {
	  switch(justification)
	  {
	  case LEFT:
		  break;
	  case CENTER:
		  for(int i=pageText.length()-pageText.lastIndexOf('\n');
				  i<(WINDOWWIDTH-additionalPageText.length())/2; i++)
			  pageText += " ";
		  break;
	  case RIGHT:
		  for(int i=pageText.length()-pageText.lastIndexOf('\n');
				  i<=WINDOWWIDTH-additionalPageText.length(); i++)
			  pageText += " ";
		  break;
	  case FIRSTINDENT:
		  for(int i=0; i<FIRSTINDENT; i++)
			  pageText += " ";
		  break;
	  case SECONDINDENT:
		  for(int i=0; i<FIRSTINDENT+SECONDINDENT; i++)
			  pageText += " ";
		  break;
	  }
	  pageText += additionalPageText;
  }
  
  public void readFile()
  {
    String filelinestring = null;
    StringBuilder filetextstringbuilder = new StringBuilder(2048);
    try
    {
      BufferedReader reader = new BufferedReader(new FileReader(fileName));
      while((filelinestring = reader.readLine()) != null)
      {
        //this appends an extra line at the end of the file, but it looks
        //nice for formatting anyway
        filetextstringbuilder.append(filelinestring + "\n");
      }
      reader.close();
    }catch(IOException exception)
    {
      System.out.println("Error reading from file");
      pageText = "";
      return;
    }
    pageText = filetextstringbuilder.toString();
  }

  public void writeFile()
  {
    try
    {
      BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
      writer.write(pageText);
      writer.close();
    }catch(IOException exception)
    {
      System.out.println("Error writing to file");
    }
  }
}
