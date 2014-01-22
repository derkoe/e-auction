package eauction.backend;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

import java.math.BigDecimal;
import java.util.Date;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import eauction.api.Bid;
import eauction.backend.entities.Auction;
import eauction.backend.entities.Auction.AuctionStatus;
import eauction.backend.entities.AuctionItem;
import eauction.backend.entities.AuctionItem.AuctionItemStatus;

public class AuctionatorTest {
  private Auctionator auctionator;

  private Auction auction;

  @Before
  public void setup() {
    AuctionRepository auctionRepository = mock(AuctionRepository.class);
    AuctionInfoService auctionInfoService = mock(AuctionInfoService.class);
    auctionator = new Auctionator(auctionRepository, auctionInfoService);
    auction = new Auction();
    auctionator.setup(auction);
  }

  @Test
  public void completeAuction() {
    Date current = new Date();
    DateFactory.overrideDate(current);
    AuctionItem item = auction.addItem(BigDecimal.valueOf(12500));

    auctionator.auctionProcessing();
    assertThat(item.getStatus(), equalTo(AuctionItemStatus.IN_PROGRESS));

    current.setTime(current.getTime() + 10 * 1000);
    auctionator.auctionProcessing();
    assertThat(item.getStatus(), equalTo(AuctionItemStatus.GOING_ONCE));

    auctionator.auctionProcessing();
    assertThat(item.getStatus(), equalTo(AuctionItemStatus.GOING_ONCE));

    current.setTime(current.getTime() + 10 * 1000);
    auctionator.auctionProcessing();
    assertThat(item.getStatus(), equalTo(AuctionItemStatus.GOING_TWICE));

    current.setTime(current.getTime() + 10 * 1000);
    auctionator.auctionProcessing();
    assertThat(item.getStatus(), equalTo(AuctionItemStatus.NOT_SOLD));
}

  // @Test(expected = AuctionException.class)
  // public void handleBidWrongAuctionStatus() throws AuctionException {
  // auctionator.handleBid(new Bid(1, "user", 4, BigDecimal.valueOf(14500)));
  // }

  // @Test(expected = AuctionException.class)
  // public void handleBidWrongItemState() throws AuctionException {
  // auction.setStatus(AuctionStatus.IN_PROGRESS);
  // auctionator.handleBid(new Bid(1, "user", 4, BigDecimal.valueOf(14500)));
  // }
}
