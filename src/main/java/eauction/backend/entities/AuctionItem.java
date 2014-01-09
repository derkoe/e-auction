package eauction.backend.entities;

import static javax.persistence.FetchType.LAZY;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;

@Entity
public class AuctionItem {

  @Id
  @GeneratedValue
  private Long id;

  @Column(nullable = false)
  private int itemNumber;

  @Lob
  private String descriptionJson;

  @Lob
  @Basic(fetch = LAZY)
  private byte[] image;
}
