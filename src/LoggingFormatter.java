/**
 * @author Yan Ling Cheung
 * LoggingFormatter is used by the FileHandler class and it sets the format
 * of how the message will be logged in the log file.
 */

import java.time.LocalDateTime;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class LoggingFormatter extends Formatter
{
  
  /**
   * Returns the String that will be recorded in the log file
   * @param record the log record of the event being logged
   */
  @Override
  public String format(LogRecord record)
  {
    return LocalDateTime.now().toString() + ": " 
      + record.getMessage() + "\n";
  }
}