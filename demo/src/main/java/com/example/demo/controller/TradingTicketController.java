package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.entity.TradingTicket;
import com.example.demo.response.ResultJson;
import com.example.demo.response.TradingHistoryResponse;
import com.example.demo.service.TradingTicketService;

@Controller
@RequestMapping("api/tradingTicket")
public class TradingTicketController {
	
	@Autowired
	TradingTicketService tradingServiceImpl;
	
	@GetMapping("/getLatestBestAggregatedPriceTicket")
	public ResponseEntity<ResultJson> getLatestBestAggregatedPriceTicket() throws Exception{
		List<TradingTicket> tradingTicketList = tradingServiceImpl.retrieveLatestBestAggregatedPriceTicket();
		ResultJson resultJson = new ResultJson();
		resultJson.setData(tradingTicketList);
		return ResponseEntity.ok(resultJson);
	}
	
	@PostMapping("/buy")
	public ResponseEntity<ResultJson> buyOrder(@Param("tradingTicketId") int tradingTicketId, @Param("userId") int userId) throws Exception{
		tradingServiceImpl.buyOrder(tradingTicketId, userId);
		ResultJson resultJson = new ResultJson();
		resultJson.setMessage("Buying the ticket is successfully");
		return ResponseEntity.ok(resultJson);
	}
	
	@PostMapping("/sell")
	public ResponseEntity<ResultJson> sellOrder(@Param("tradingTicketId") int tradingTicketId, @Param("userId") int userId) throws Exception{
		tradingServiceImpl.sellOrder(tradingTicketId, userId);
		ResultJson resultJson = new ResultJson();
		resultJson.setMessage("Selling the ticket is successfully");
		return ResponseEntity.ok(resultJson);
	}
	
	@GetMapping("/getTradingHistory")
	public ResponseEntity<ResultJson> getTradingHistory(@Param("userId") int userId) throws Exception{
		List<TradingHistoryResponse> tradingHistoryList = tradingServiceImpl.getTradingHistory(userId);
		ResultJson resultJson = new ResultJson();
		resultJson.setData(tradingHistoryList);
		return ResponseEntity.ok(resultJson);
	}
}
