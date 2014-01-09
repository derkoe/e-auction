package eauction.backend;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import eauction.backend.entities.Auction;

@Repository
public interface AuctionRepository extends PagingAndSortingRepository<Auction, Long> {

  @Query("FROM Auction WHERE status = 'PLANNED' ORDER BY plannedStart")
  public List<Auction> findCurrentAuctionsOrderByStart();

}
