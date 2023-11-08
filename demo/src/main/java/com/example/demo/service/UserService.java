package com.example.demo.service;

import com.example.demo.response.UserBalanceResponse;

public interface UserService {
	UserBalanceResponse getBalance(int userId);
}
