package eauction.backend.entities;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

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

  @OneToMany(cascade = CascadeType.ALL)
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

  public Date getPlannedStart() {
    return plannedStart;
  }

  public static enum AuctionStatus {
    CREATED, PLANNED, IN_PROGRESS, CLOSED
  }

  public List<AuctionItem> getItems() {
    return items;
  }

  public void start(Date now) {
    status = AuctionStatus.IN_PROGRESS;
    start = now;
  }
}
