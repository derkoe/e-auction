package eauction.api;

import java.math.BigDecimal;

import eauction.backend.DateFactory;

public class AuctionUpdate {

  private AuctionUpdateStatus status;
  private int auctionId;
  private Long auctionStartTime;
  private Integer itemNumber;

  private BigDecimal price;

  public AuctionUpdate(int auctionId, long auctionStartTime) {
    this.status = AuctionUpdateStatus.COUNTDOWN;
    this.auctionId = auctionId;
    this.auctionStartTime = auctionStartTime;
  }

  public AuctionUpdateStatus getStatus() {
    return status;
  }

  public int getAuctionId() {
    return auctionId;
  }

  public Long getAuctionStartTime() {
    return auctionStartTime;
  }

  public int getCountdownTimeSeconds() {
    return (int)((auctionStartTime - DateFactory.currentTimestamp().getTime()) / 1000L);
  }

  public Integer getItemNumber() {
    return itemNumber;
  }

  public static enum AuctionUpdateStatus {
    COUNTDOWN, NEXT_ITEM, NEW_BID, GOING_ONCE, GOING_TWICE, SOLD, NO_BID, NO_BID_TWICE, NOT_SOLD
  }
}
