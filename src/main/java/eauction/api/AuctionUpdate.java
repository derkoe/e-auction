package eauction.api;

import org.springframework.core.style.ToStringCreator;

import eauction.backend.DateFactory;
import eauction.backend.entities.AuctionItem;

public class AuctionUpdate {

  private AuctionUpdateStatus status;
  private int auctionId;
  private Long auctionStartTime;
  private AuctionItem item;

  public AuctionUpdate(AuctionUpdateStatus status, int auctionId, long auctionStartTime) {
    this.status = status;
    this.auctionId = auctionId;
    this.auctionStartTime = auctionStartTime;
  }

  public AuctionUpdate(AuctionUpdateStatus status, int auctionId, AuctionItem currentItem) {
    this.status = status;
    this.auctionId = auctionId;
    this.item = currentItem;
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
    return auctionStartTime == null ? 0 : (int)((auctionStartTime - DateFactory.currentTimestamp().getTime()) / 1000L);
  }

  public AuctionItem getItem() {
    return item;
  }

  public static enum AuctionUpdateStatus {
    COUNTDOWN, NEXT_ITEM, NEW_BID, GOING_ONCE, GOING_TWICE, SOLD, NO_BID, NO_BID_TWICE, NOT_SOLD, END
  }

  @Override
  public String toString() {
    return new ToStringCreator(this).append("auctionId", auctionId).append("status", status).toString();
  }
}
