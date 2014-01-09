package eauction.api;

public class ErrorMessage {
  private String message;

  public ErrorMessage(String message) {
    super();
    this.message = message;
  }

  public String getMessage() {
    return message;
  }
}
