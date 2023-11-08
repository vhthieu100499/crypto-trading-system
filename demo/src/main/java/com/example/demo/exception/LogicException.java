package com.example.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class LogicException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	private String errNo;
	
	public LogicException(String errNo, String message) {
		super(message);
		this.errNo = errNo;
	}
	
	public LogicException(String errNo) {
		this.errNo = errNo;
	}
	
	public String getErrNo() {
		return this.errNo;
	}
}
