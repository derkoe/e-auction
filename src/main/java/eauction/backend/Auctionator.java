package eauction.backend;

import static eauction.backend.AuctionException.ErrorCode.BID_OBSOLETE;
import static eauction.backend.AuctionException.ErrorCode.NO_AUCTION;
import static eauction.backend.AuctionException.ErrorCode.WRONG_AUCTION;
import static eauction.backend.AuctionException.ErrorCode.WRONG_ITEM;
import static eauction.backend.entities.Auction.AuctionStatus.PLANNED;

import java.util.Date;

import eauction.api.AuctionUpdate;
import eauction.api.AuctionUpdate.AuctionUpdateStatus;
import eauction.api.Bid;
import eauction.backend.entities.Auction;
import eauction.backend.entities.Auction.AuctionStatus;
import eauction.backend.entities.AuctionItem;
import eauction.backend.entities.AuctionItem.AuctionItemStatus;

public class Auctionator {

  private final AuctionRepository auctionRepository;

  private final AuctionInfoService auctionInfo;

  private Auction currentAuction;

  private AuctionItem currentItem;

  private long lastItemChange;

  public Auctionator(AuctionRepository auctionRepository, AuctionInfoService auctionInfo) {
    super();
    this.auctionRepository = auctionRepository;
    this.auctionInfo = auctionInfo;
    lastItemChange = DateFactory.currentTimestamp().getTime();
  }

  public void handleBid(Bid bid) throws AuctionException {

    synchronized (currentAuction) {

      if (currentAuction == null || currentAuction.getStatus() != AuctionStatus.IN_PROGRESS) {
        throw new AuctionException(NO_AUCTION);
      }

      if (currentAuction.getId() != bid.getAuctionId()) {
        throw new AuctionException(WRONG_AUCTION);
      }

      if (bid.getItemNumber() != currentItem.getItemNumber()) {
        throw new AuctionException(WRONG_ITEM);
      }

      if (bid.getAmount().compareTo(currentItem.getHighestBid()) < 0) {
        throw new AuctionException(BID_OBSOLETE);
      }

      currentItem.bid(bid.getAmount());
    }

    auctionRepository.save(currentAuction);
  }

  public void auctionProcessing() {

    if (currentAuction == null) {
      return;
    }

    Date now = DateFactory.currentTimestamp();

    synchronized (currentAuction) {

      if (currentAuction.getStatus() == PLANNED) {
        if (now.before(currentAuction.getPlannedStart())) {
          auctionInfo.send(new AuctionUpdate(AuctionUpdateStatus.COUNTDOWN, currentAuction.getId(), currentAuction.getPlannedStart().getTime()));
          return;
        }
        else {
          currentAuction.start(now);
        }
      }

      if (currentItem == null) {
        handleNextItem();
      }

      if (currentItem != null && now.getTime() > lastItemChange + 5 * 1000) { // TODO randomize time
        lastItemChange = now.getTime();
        AuctionItemStatus itemStatus = currentItem.progress();
        if (itemStatus == AuctionItemStatus.NOT_SOLD) {
          handleNextItem();
        }
      }
    }

    auctionRepository.save(currentAuction);
  }

  private void handleNextItem() {
    currentItem = currentAuction.nextItem();

    // no more items -> end of auction
    if (currentItem == null) {
      currentAuction.end(DateFactory.currentTimestamp());
      auctionInfo.send(new AuctionUpdate(AuctionUpdateStatus.END, currentAuction.getId(), null));
    }
    else {
      auctionInfo.send(new AuctionUpdate(AuctionUpdateStatus.NEXT_ITEM, currentAuction.getId(), currentItem));
    }
  }

  void setup(Auction auction) {
    this.currentAuction = auction;
  }
}
