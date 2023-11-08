package com.example.demo.response;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TradingHistoryResponse {
	private Integer id;
	private Integer userId;
	private Integer tradingTicketId;
	private Date tradingDate;
	private Boolean tradingType;
}
