package eauction.backend;

import static eauction.backend.entities.Auction.AuctionStatus.IN_PROGRESS;
import static eauction.backend.entities.Auction.AuctionStatus.PLANNED;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import eauction.api.AuctionUpdate;
import eauction.api.Bid;
import eauction.api.ErrorMessage;
import eauction.backend.entities.Auction;

@Service
public class AuctionService {

  private Logger LOG = LoggerFactory.getLogger(AuctionService.class);

  @Autowired
  private AuctionRepository auctionRepository;

  @Autowired
  private SimpMessagingTemplate messagingTemplate;

  private Auction currentAuction;

  public List<Auction> listRecentAuctions() {
    return auctionRepository.findAll(new PageRequest(0, 10, Direction.DESC, "plannedStart")).getContent();
  }

  // @Scheduled(fixedRate = 30 * 1000)
  // void updateCurrentAuction() {
  // List<Auction> currentAuctionsOrderByStart = auctionRepository.findCurrentAuctionsOrderByStart();
  // if (currentAuction != null) {
  //
  // }
  // else {
  // currentAuction = currentAuctionsOrderByStart.size() >= 1 ? currentAuctionsOrderByStart.get(0) : null;
  // }
  // }

  public Auction getCurrentAuction() {
    List<Auction> currentAuctionsOrderByStart = auctionRepository.findCurrentAuctionsOrderByStart();
    return currentAuctionsOrderByStart.size() >= 1 ? currentAuctionsOrderByStart.get(0) : null;
  }

  @Scheduled(fixedDelay = 1000)
  public void auctionProcessing() {
    if (currentAuction == null) {
      currentAuction = getCurrentAuction();
    }

    if (currentAuction.getStatus() == IN_PROGRESS) {
      
    }
    else if (currentAuction.getStatus() == PLANNED) {
      Date now = DateFactory.currentTimestamp();
      if (now.after(currentAuction.getPlannedStart())) {
        currentAuction.start(now);
        auctionRepository.save(currentAuction);
      }
      else {
        messagingTemplate.convertAndSend("/topic/auction", new AuctionUpdate(currentAuction.getId(), currentAuction.getPlannedStart().getTime()));
      }
    }
  }

  public void placeBid(Bid bid) {
    if (bid.getAuctionId() != currentAuction.getId()) {
      messagingTemplate.convertAndSendToUser(bid.getUser(), "errors", new ErrorMessage("Bid for wrong auction, please refresh your browser!"));
    }
    else {

    }

    messagingTemplate.convertAndSend("/topic/auction", bid);
  }
}
