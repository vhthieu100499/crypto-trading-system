package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class BinanceDto {
	private String symbol;
	private String bidPrice;
	private String bidQty;
	private String askPrice;
	private String askQty;
}
