package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.response.ResultJson;
import com.example.demo.response.UserBalanceResponse;
import com.example.demo.service.UserService;

@Controller
@RequestMapping("api/user")
public class UserController {
	
	@Autowired
	UserService userServiceImpl;
	
	@GetMapping("/getBalance")
	public ResponseEntity<ResultJson> getBalance(int userId) {
		UserBalanceResponse responseData = userServiceImpl.getBalance(userId);
		ResultJson result = new ResultJson();
		result.setData(responseData);
		return ResponseEntity.ok(result);
	}
}
