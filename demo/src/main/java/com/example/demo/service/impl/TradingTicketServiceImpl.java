package com.example.demo.service.impl;

import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.example.demo.dto.BinanceDto;
import com.example.demo.dto.HoubiDto;
import com.example.demo.dto.HoubiJsonDataDto;
import com.example.demo.entity.Trading;
import com.example.demo.entity.TradingHistory;
import com.example.demo.entity.TradingTicket;
import com.example.demo.entity.User;
import com.example.demo.exception.LogicException;
import com.example.demo.repository.TradingHistoryRepository;
import com.example.demo.repository.TradingRepository;
import com.example.demo.repository.TradingTicketRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.response.TradingHistoryResponse;
import com.example.demo.service.TradingTicketService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class TradingTicketServiceImpl implements TradingTicketService {
	
	private static final String ETHEREUM = "ETHUSDT";
	private static final String BITCOIN = "BTCUSDT";
	private static final Boolean BUY_ORDER = true;
	private static final Boolean SELL_ORDER = false;
	
	@Autowired
	TradingTicketRepository tradingTicketRepository;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	TradingRepository tradingRepository;
	
	@Autowired
	TradingHistoryRepository tradingHistoryRepository;
	
	@Override
	@Scheduled(fixedRate = 10000)
	public void batchReceiveBestTrading() throws Exception {
		System.out.println("run");
		ObjectMapper mapper = new ObjectMapper();
		URL binanceUrl = new URL("https://api.binance.com/api/v3/ticker/bookTicker");
		List<BinanceDto> binanceList = mapper.readValue(binanceUrl, new TypeReference<List<BinanceDto>>(){});
		
		URL houbiUrl = new URL("https://api.huobi.pro/market/tickers");
		HoubiJsonDataDto houbiJsonData = mapper.readValue(houbiUrl, new TypeReference<HoubiJsonDataDto>(){});
		
		List<BinanceDto> binanceBitEtheList = new ArrayList<>();
		for(BinanceDto binance : binanceList) {
			if(binance.getSymbol().equals(ETHEREUM) || binance.getSymbol().equals(BITCOIN)) {
				binanceBitEtheList.add(binance);
			}
		}
		
		List<HoubiDto> houbiBitEtheList = new ArrayList<>();
		for(HoubiDto houbi : houbiJsonData.getData()) {
			if(houbi.getSymbol().equalsIgnoreCase(ETHEREUM) || houbi.getSymbol().equalsIgnoreCase(BITCOIN)) {
				houbiBitEtheList.add(houbi);
			}
		}
		
		insertTradingWithBestPrice(binanceBitEtheList, houbiBitEtheList);
	}
	
	private void insertTradingWithBestPrice(List<BinanceDto> binanceList, List<HoubiDto> houbiList) {
		Double maxBidPrice = getMaxBidPrice(binanceList, houbiList);
		Double minAskPrice = getMinAskdPrice(binanceList, houbiList);
		Date currentDate = new Date();
		
		List<TradingTicket> tradingTicketMaxBidPriceList = new ArrayList<>();
		for(BinanceDto binance : binanceList) {
			if(Double.compare(Double.valueOf(binance.getBidPrice()), maxBidPrice) == 0 || 
					Double.compare(Double.valueOf(binance.getAskPrice()), minAskPrice) == 0) {
				TradingTicket tradingTicketTemp = new TradingTicket();
				tradingTicketTemp.setSymbol(binance.getSymbol());
				tradingTicketTemp.setAskPrice(Double.valueOf(binance.getAskPrice()));
				tradingTicketTemp.setBidPrice(Double.valueOf(binance.getBidPrice()));
				tradingTicketTemp.setIsAvailable(true);
				tradingTicketTemp.setCreatedDate(currentDate);
				
				tradingTicketMaxBidPriceList.add(tradingTicketTemp);
			}
		}
		
		for(HoubiDto houbi : houbiList) {
			if(Double.compare(Double.valueOf(houbi.getBid()), maxBidPrice) == 0 || 
					Double.compare(Double.valueOf(houbi.getAsk()), minAskPrice) == 0) {
				TradingTicket tradingTicketTemp = new TradingTicket();
				tradingTicketTemp.setSymbol(houbi.getSymbol());
				tradingTicketTemp.setAskPrice(Double.valueOf(houbi.getAsk()));
				tradingTicketTemp.setBidPrice(Double.valueOf(houbi.getBid()));
				tradingTicketTemp.setIsAvailable(true);
				tradingTicketTemp.setCreatedDate(currentDate);
				
				tradingTicketMaxBidPriceList.add(tradingTicketTemp);
			}
		}
		
		tradingTicketRepository.saveAll(tradingTicketMaxBidPriceList);
	}
	
	private Double getMaxBidPrice(List<BinanceDto> binanceList, List<HoubiDto> houbiList) {
		Double max = Double.valueOf(binanceList.get(0).getBidPrice());
		Double valueTemp = max;
		for(BinanceDto binance : binanceList) {
			valueTemp = Double.valueOf(binance.getBidPrice());
			if(Double.compare(max, valueTemp) < 0) {
				max = valueTemp;
			}
		}
		
		for(HoubiDto houbi : houbiList) {
			if(Double.compare(max, houbi.getBid()) < 0) {
				max = houbi.getBid();
			}
		}
		return max;
	}
	
	private Double getMinAskdPrice(List<BinanceDto> binanceList, List<HoubiDto> houbiList) {
		Double min = Double.valueOf(binanceList.get(0).getAskPrice());
		Double valueTemp = min;
		for(BinanceDto binance : binanceList) {
			valueTemp = Double.valueOf(binance.getAskPrice());
			if(Double.compare(min, valueTemp) > 0) {
				min = valueTemp;
			}
		}
		
		for(HoubiDto houbi : houbiList) {
			if(Double.compare(min, houbi.getAsk()) > 0) {
				min = houbi.getAsk();
			}
		}
		return min;
	}

	@Override
	public List<TradingTicket> retrieveLatestBestAggregatedPriceTicket() {
		List<TradingTicket> tradingTicketList = tradingTicketRepository.getLatestBestAggregatedPriceTicket();
		return tradingTicketList;
	}

	@Override
	public void buyOrder(int tradingTicketId, int userId) {
		Optional<TradingTicket> tradingTicketOpt = tradingTicketRepository.findById(tradingTicketId);
		if(tradingTicketOpt.isEmpty()) {
			throw new LogicException("Error_1", "Trading Ticket is invalid");
		}
		
		Optional<User> userOpt = userRepository.findById(userId);
		if(userOpt.isEmpty()) {
			throw new LogicException("Error_2", "User is not existed");
		}
		
		TradingTicket tradingTicket = tradingTicketOpt.get();
		
		User user = userOpt.get();
		if(Double.compare(user.getBalance(), tradingTicket.getAskPrice()) >= 0 && tradingTicket.getIsAvailable() == true) {
			tradingTicket.setIsAvailable(false);
			tradingTicketRepository.save(tradingTicket);
			
			user.setBalance(user.getBalance() - tradingTicket.getAskPrice());
			userRepository.save(user);
			
			Trading trading = new Trading();
			trading.setUserId(user.getId());
			trading.setTradingTicketId(tradingTicket.getId());
			
			tradingRepository.save(trading);
			
			TradingHistory tradingHistory = new TradingHistory();
			tradingHistory.setUserId(user.getId());
			tradingHistory.setTradingTicketId(tradingTicket.getId());
			tradingHistory.setTradingType(BUY_ORDER);
			tradingHistory.setTradingDate(new Date());
			
			tradingHistoryRepository.save(tradingHistory);
			
		} else if (tradingTicket.getIsAvailable() == false){
			throw new LogicException("Error_3", "Trading Ticket is not existed");
		} else {
			throw new LogicException("Error_4", "The balance of user is not enough");
		}
	}

	@Override
	public void sellOrder(int tradingTicketId, int userId) {
		Optional<TradingTicket> tradingTicketOpt = tradingTicketRepository.findById(tradingTicketId);
		if(tradingTicketOpt.isEmpty()) {
			throw new LogicException("Error_1", "Trading Ticket is invalid");
		}
		TradingTicket tradingTicket = tradingTicketOpt.get();
		
		Optional<User> userOpt = userRepository.findById(userId);
		if(userOpt.isEmpty()) {
			throw new LogicException("Error_2", "User is not existed");
		}
		
		Trading trading = tradingRepository.findByUserIdAndTradingTicketId(userId, tradingTicketId);
		if(trading == null) {
			throw new LogicException("Error_5", "The user does not own the trading ticket");
		}
		User user = userOpt.get();
		
		TradingHistory tradingHistory = new TradingHistory();
		tradingHistory.setUserId(user.getId());
		tradingHistory.setTradingTicketId(tradingTicketId);
		tradingHistory.setTradingType(SELL_ORDER);
		tradingHistory.setTradingDate(new Date());
		
		tradingHistoryRepository.save(tradingHistory);

		user.setBalance(user.getBalance() + tradingTicket.getBidPrice());
		userRepository.save(user);
		
		tradingTicket.setIsAvailable(true);
		tradingTicketRepository.save(tradingTicket);
		
	}

	@Override
	public List<TradingHistoryResponse> getTradingHistory(int userId) {
		Optional<User> userOpt = userRepository.findById(userId);
		if(userOpt.isEmpty()) {
			throw new LogicException("Error_2", "User is not existed");
		}
		
		List<TradingHistory> tradingHistoryList = tradingHistoryRepository.findByUserId(userId);
		List<TradingHistoryResponse> dataListResponse = new ArrayList<>();
		for(TradingHistory tradingHistory : tradingHistoryList) {
			TradingHistoryResponse temp = new TradingHistoryResponse();
			temp.setId(tradingHistory.getId());
			temp.setUserId(tradingHistory.getUserId());
			temp.setTradingTicketId(tradingHistory.getTradingTicketId());
			temp.setTradingType(tradingHistory.getTradingType());
			temp.setTradingDate(tradingHistory.getTradingDate());
			
			dataListResponse.add(temp);
		}
		return dataListResponse;
	}
	
}
