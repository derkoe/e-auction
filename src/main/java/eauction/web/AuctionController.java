package eauction.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import eauction.api.Bid;
import eauction.backend.AuctionService;

@Controller
public class AuctionController {

  @Autowired
  private AuctionService auctionService;

  @RequestMapping("/login.html")
  public String login() {
    return "login";
  }

  @RequestMapping({"/", "/index.html"})
  public String index() {
    return "index";
  }

  @MessageMapping("/auction/bid")
  public void bid(Bid bid) throws Exception {
    auctionService.placeBid(bid);
  }

}
