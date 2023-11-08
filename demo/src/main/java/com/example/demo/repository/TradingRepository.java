package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Trading;

@Repository
public interface TradingRepository extends JpaRepository<Trading, Integer>{
	
	Trading findByUserIdAndTradingTicketId(int userId, int tradingTicketId);
}
