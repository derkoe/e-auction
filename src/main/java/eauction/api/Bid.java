package eauction.api;

import java.math.BigDecimal;

public class Bid {

  private int auctionId;
  private int itemNumber;
  private String user;
  private BigDecimal amount;

  Bid() {
  }

  public Bid(int auctionId, String user, int itemNumber, BigDecimal amount) {
    super();
    this.auctionId = auctionId;
    this.user = user;
    this.itemNumber = itemNumber;
    this.amount = amount;
  }

  public int getAuctionId() {
    return auctionId;
  }

  public String getUser() {
    return user;
  }

  public int getItemNumber() {
    return itemNumber;
  }

  public BigDecimal getAmount() {
    return amount;
  }

  @Override
  public String toString() {
    return "Bid [auctionId=" + auctionId + ", itemNumber=" + itemNumber + ", user=" + user + ", amount=" + amount + "]";
  }
}
