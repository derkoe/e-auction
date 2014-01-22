package eauction.backend;

import static eauction.AuctionApplication.LOGGER;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import eauction.api.AuctionUpdate;

@Service
public class AuctionInfoService {

  @Autowired
  private SimpMessagingTemplate messagingTemplate;

  public void send(AuctionUpdate auctionUpdate) {

    messagingTemplate.convertAndSend("/topic/auction", auctionUpdate);

    LOGGER.info("Auction update {}", auctionUpdate);
  }

}
