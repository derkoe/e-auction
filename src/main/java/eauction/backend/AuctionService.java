package eauction.backend;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import eauction.api.Bid;
import eauction.backend.entities.Auction;

@Service
public class AuctionService {

  private final AuctionRepository auctionRepository;

  private final Auctionator auctionator;

  @Autowired
  public AuctionService(AuctionRepository auctionRepository, AuctionInfoService auctionInfo) {
    this.auctionRepository = auctionRepository;
    this.auctionator = new Auctionator(auctionRepository, auctionInfo);
  }

  public List<Auction> listRecentAuctions() {
    return auctionRepository.findAll(new PageRequest(0, 10, Direction.DESC, "plannedStart")).getContent();
  }

  public Auction getCurrentAuction() {
    List<Auction> currentAuctionsOrderByStart = auctionRepository.findCurrentAuctionsOrderByStart();
    return currentAuctionsOrderByStart.size() >= 1 ? currentAuctionsOrderByStart.get(0) : null;
  }

  @Scheduled(fixedDelay = 30 * 1000)
  public void aaa() {
    List<Auction> openAuctions = auctionRepository.findCurrentAuctionsOrderByStart();
    if (openAuctions.size() > 0) {
      auctionator.setup(openAuctions.get(0));
    }
  }

  @Scheduled(fixedDelay = 1000)
  public void auctionProcessing() {
    auctionator.auctionProcessing();
  }

  public void placeBid(Bid bid) {
    try {
      auctionator.handleBid(bid);
    }
    catch (AuctionException e) {
      // TODO handle bid failed
    }
  }
}
