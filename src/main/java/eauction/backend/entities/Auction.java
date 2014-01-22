package eauction.backend.entities;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import eauction.backend.entities.AuctionItem.AuctionItemStatus;

@Entity
public class Auction {

  @Id
  @GeneratedValue
  private int id;

  @NotEmpty
  @Column(length = 50, nullable = false)
  private String label;

  @NotNull
  @Column(length = 20, nullable = false)
  @Enumerated(EnumType.STRING)
  private AuctionStatus status;

  @Column(name = "planned_start")
  @Temporal(TemporalType.TIMESTAMP)
  private Date plannedStart;

  @Column(name = "start_timestamp")
  @Temporal(TemporalType.TIMESTAMP)
  private Date start;

  @Column(name = "end_timestamp")
  @Temporal(TemporalType.TIMESTAMP)
  private Date end;

  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  @OrderBy("itemNumber")
  private List<AuctionItem> items;

  public int getId() {
    return id;
  }

  public String getLabel() {
    return label;
  }

  public AuctionStatus getStatus() {
    return status;
  }

  public void setStatus(AuctionStatus status) {
    this.status = status;
  }

  public Date getPlannedStart() {
    return plannedStart;
  }

  public static enum AuctionStatus {
    CREATED, PLANNED, IN_PROGRESS, CLOSED
  }

  public AuctionItem addItem(BigDecimal callPrice) {
    if (items == null) {
      items = new ArrayList<AuctionItem>();
    }
    AuctionItem item = new AuctionItem(items.size() + 1, callPrice);
    items.add(item);
    return item;
  }

  public void start(Date startDate) {
    status = AuctionStatus.IN_PROGRESS;
    start = startDate;
  }

  public void end(Date endDate) {
    status = AuctionStatus.CLOSED;
    end = endDate;
  }

  public AuctionItem nextItem() {
    for (AuctionItem item : items) {
      if (item.getStatus() == AuctionItemStatus.OPEN) {
        item.activate();
        return item;
      }
    }
    status = AuctionStatus.CLOSED;
    return null;
  }
}
