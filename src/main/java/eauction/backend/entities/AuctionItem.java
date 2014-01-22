package eauction.backend.entities;

import static eauction.backend.entities.AuctionItem.AuctionItemStatus.GOING_ONCE;
import static eauction.backend.entities.AuctionItem.AuctionItemStatus.GOING_TWICE;
import static eauction.backend.entities.AuctionItem.AuctionItemStatus.IN_PROGRESS;
import static eauction.backend.entities.AuctionItem.AuctionItemStatus.NOT_SOLD;
import static eauction.backend.entities.AuctionItem.AuctionItemStatus.OPEN;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;

@Entity
public class AuctionItem {

  @Id
  @GeneratedValue
  private Long id;

  @Column(name = "item_no", nullable = false)
  private int itemNumber;

  @Column(length = 200, nullable = false)
  private String name;

  @Column(name = "desc_json")
  @Lob
  private String descriptionJson;

  @Column(name = "call_price", precision = 10, scale = 2)
  private BigDecimal callPrice;

  @Column(name = "bid", precision = 10, scale = 2)
  private BigDecimal currentBid;

  @Column
  @Enumerated(EnumType.STRING)
  private AuctionItemStatus status;

  public AuctionItem(int itemNumber, BigDecimal callPrice) {
    this.itemNumber = itemNumber;
    this.callPrice = callPrice;
    this.status = OPEN;
  }

  public static enum AuctionItemStatus {
    OPEN, IN_PROGRESS, GOING_ONCE, GOING_TWICE, SOLD, NOT_SOLD
  }

  public int getItemNumber() {
    return itemNumber;
  }

  public AuctionItemStatus getStatus() {
    return status;
  }

  public void setCallPrice(BigDecimal callPrice) {
    this.callPrice = callPrice;
  }

  public BigDecimal getCallPrice() {
    return callPrice;
  }

  public BigDecimal getHighestBid() {
    return currentBid != null ? currentBid : callPrice;
  }

  void activate() {
    if (status != OPEN) {
      throw new IllegalStateException("Auction is not in state " + OPEN);
    }
    this.status = IN_PROGRESS;
  }

  public AuctionItemStatus progress() {
    switch (status) {
      case IN_PROGRESS:
        return (status = GOING_ONCE);
      case GOING_ONCE:
        return (status = GOING_TWICE);
      case GOING_TWICE:
        return (status = NOT_SOLD);
      default:
        throw new IllegalStateException("Item is in wrong status:" + status);
    }
  }

  public void bid(BigDecimal bid) {
    if (status != IN_PROGRESS && status != GOING_ONCE && status != GOING_TWICE) {
      throw new IllegalStateException("Item is in wrong status:" + status);
    }
    this.currentBid = bid;
    this.status = IN_PROGRESS;
  }
}
