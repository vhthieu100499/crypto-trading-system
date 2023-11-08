package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.TradingTicket;

@Repository
public interface TradingTicketRepository extends JpaRepository<TradingTicket, Integer> {

	@Query(value = "SELECT id, symbol, ask_price, bid_price, is_available, created_date "
				+ "FROM trading_ticket "
				+ "WHERE bid_price = (SELECT MAX(bid_price) FROM trading_ticket) OR ask_price = (SELECT MIN(ask_price) FROM trading_ticket)", nativeQuery = true)	
	List<TradingTicket> getLatestBestAggregatedPriceTicket();
	
	
}
