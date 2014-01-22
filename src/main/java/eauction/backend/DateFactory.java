package eauction.backend;

import java.util.Date;

/**
 * Factory for generating all application dates and timestamps.
 */
public class DateFactory {
  
  private static ThreadLocal<Date> dateOverride = new ThreadLocal<Date>();
  
  public static Date currentTimestamp() {
    Date override = dateOverride.get();
    return override != null ? override : new Date();
  }

  public static void overrideDate(Date override) {
    dateOverride.set(override);
  }
}
