package com.example.demo.service;

import java.util.List;

import com.example.demo.entity.TradingTicket;
import com.example.demo.response.TradingHistoryResponse;

public interface TradingTicketService {
	
	public void batchReceiveBestTrading() throws Exception;
	public List<TradingTicket> retrieveLatestBestAggregatedPriceTicket();
	public void buyOrder(int tradingTicketId, int userId);
	public void sellOrder(int tradingTicketId, int userId);
	public List<TradingHistoryResponse> getTradingHistory(int userId);
}
