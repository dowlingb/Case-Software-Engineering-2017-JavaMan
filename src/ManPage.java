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
     JavaMan.print(pageText);
  }

  public void displayMethodText(String methodName)
  {
	  /* find the beginning of the method's section */
	  //continue to find the next occurrence of the method name until line at right justification level
	  int pageMethodStartIndex = 0;
	  int numNewlineSpaces = 0;
	  int temploopcounter = 0;
	  do
	  {  
		  //it's going back up to the top, perhaps indexof returning -1, not finding it?
		  if(pageText.substring(pageMethodStartIndex).indexOf("\n") == -1)
		  {
			  JavaMan.print("Error printing method: B");
			  return;
		  }
		  pageMethodStartIndex = pageMethodStartIndex + pageText.substring(pageMethodStartIndex).indexOf("\n");
		  if(pageText.substring(pageMethodStartIndex).indexOf(methodName+"(") == -1)
		  {
			  JavaMan.print("Error printing method: A");
			  return;
		  }
		  pageMethodStartIndex = pageMethodStartIndex + pageText.substring(pageMethodStartIndex).indexOf(methodName+"(");
		  
		  //if it can't be found, print the message
		  if(pageMethodStartIndex == -1 || pageMethodStartIndex+1 >= pageText.length())
		  {
			  JavaMan.print("Class found, but method does not exist");
			  return;
		  }
		  //determine number of indents, should match
		  int pageMethodNewlineIndex = pageText.lastIndexOf("\n", pageMethodStartIndex);
		  numNewlineSpaces = 0;
		  while(pageText.charAt(pageMethodNewlineIndex+numNewlineSpaces+1) == ' ')
			  numNewlineSpaces++;
		  if(temploopcounter++ == 4)
			  return;
	  }while(numNewlineSpaces != FIRSTINDENT);
	  //go back to the beginning of the line so we get the scope and return value
	  pageMethodStartIndex = pageText.lastIndexOf("\n", pageMethodStartIndex);
	  	  
	  //find the end of the method's section
	  //go to the end of the method definition marked by ')'
	  int pageMethodEndIndex = pageMethodStartIndex;
	  while(pageMethodEndIndex < pageText.length()
			  && pageText.charAt(pageMethodEndIndex) != ')')
		  pageMethodEndIndex++;
	  //and stop when we reach the method definition indent level again
	  do
	  {
		  if(pageMethodEndIndex+1 >= pageText.length())
		  {
			  JavaMan.print("Error displaying method");
			  return;
		  }
		  int pageMethodNewlineOffset = (pageText.substring(pageMethodEndIndex+1)).indexOf("\n")+1;
		  //if it can't be found, print the message
		  if(pageMethodNewlineOffset == -1
				  || pageMethodEndIndex+pageMethodNewlineOffset+1 >= pageText.length())
		  {
			  JavaMan.print("Error displaying method");
			  return;
		  }
		  pageMethodEndIndex = pageMethodEndIndex + pageMethodNewlineOffset;
		  
		  //determine number of indents, should match
		  int pageMethodNewlineIndex = pageMethodEndIndex;//pageText.lastIndexOf("\n", pageMethodEndIndex);
		  numNewlineSpaces = 0;
		  while(pageText.charAt(pageMethodNewlineIndex+numNewlineSpaces+1) == ' ') //do we need the +1?
			  numNewlineSpaces++;
	  }while(numNewlineSpaces != FIRSTINDENT && numNewlineSpaces != 0);
	  Debug.printv("Substring indices: " + pageMethodStartIndex + " " + pageMethodEndIndex);
	  JavaMan.print(pageText.substring(pageMethodStartIndex, pageMethodEndIndex));
  }
  
  /*
   * NOTE: change this to a stringbuilder later for efficiency
   * appends new lines of text to the page text
   */
  public void append(String additionalPageText, Justification justification)
  {
	  //format justification
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
		  additionalPageText = wrapTextIndents(additionalPageText, Justification.FIRSTINDENT);
		  break;
	  case SECONDINDENT:
		  for(int i=0; i<FIRSTINDENT+SECONDINDENT; i++)
			  pageText += " ";
		  additionalPageText = wrapTextIndents(additionalPageText, Justification.SECONDINDENT);
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
      JavaMan.print("Error reading from file");
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
  
  //preserves indentation for wrapped text
  private String wrapTextIndents(String unwrappedText, Justification indent)
  {
	  //Debug.printv("wrapping text");
	  //it's not working on the first indent, indentValue is coming back 0
	  int indentValue;
	  if(indent == Justification.FIRSTINDENT)
		  indentValue = FIRSTINDENT;
	  else if(indent == Justification.SECONDINDENT)
		  indentValue = FIRSTINDENT+SECONDINDENT;
	  else
		  indentValue = 0;
	  
	  String wrappedText = "";
	  while(unwrappedText.length() > WINDOWWIDTH)
	  {
		  int lastIndex = unwrappedText.lastIndexOf(" ", WINDOWWIDTH-indentValue);
		  wrappedText = wrappedText.concat(
				  unwrappedText.substring(0, lastIndex));
		  wrappedText = wrappedText.concat("\n");
		  for(int i=0; i<indentValue; i++)
			  wrappedText = wrappedText.concat(" ");
		  unwrappedText = unwrappedText.substring(lastIndex+1,
				  unwrappedText.length());
	  }
	  wrappedText += unwrappedText;
	  return wrappedText;
  }
}
