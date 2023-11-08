package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class HoubiDto {
	
	private String symbol;
	private Double open;
	private Double high;
	private Double low;
	private Double close;
	private Double amount;
	private Double vol;
	private Double count;
	private Double bid;
	private Double bidSize;
	private Double ask;
	private Double askSize;

}
