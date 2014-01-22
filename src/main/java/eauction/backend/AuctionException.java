package eauction.backend;

public final class AuctionException extends Exception {

  private static final long serialVersionUID = -8067671830131990133L;

  private final ErrorCode errorCode;

  public AuctionException(ErrorCode errorCode) {
    super();
    this.errorCode = errorCode;
  }

  public ErrorCode getErrorCode() {
    return errorCode;
  }

  public static enum ErrorCode {
    NO_AUCTION, WRONG_AUCTION, WRONG_ITEM, BID_OBSOLETE
  }
}
