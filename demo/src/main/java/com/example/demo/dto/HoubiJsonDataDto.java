package com.example.demo.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class HoubiJsonDataDto {
	
	private List<HoubiDto> data;
	private String status;
	private Long ts;
}
