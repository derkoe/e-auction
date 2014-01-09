package eauction.backend;

import java.util.Date;

/**
 * Factory for generating all application dates and timestamps.
 */
public class DateFactory {
  public static Date currentTimestamp() {
    return new Date();
  }
}
