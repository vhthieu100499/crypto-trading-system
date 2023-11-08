package com.example.demo.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.User;
import com.example.demo.exception.LogicException;
import com.example.demo.repository.UserRepository;
import com.example.demo.response.UserBalanceResponse;
import com.example.demo.service.UserService;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	UserRepository userRepository;

	@Override
	public UserBalanceResponse getBalance(int userId) {
		Optional<User> userOpt = userRepository.findById(userId);
		if(userOpt.isEmpty()) {
			throw new LogicException("Error_2", "User is not existed");
		}
		User user = userOpt.get();
		return new UserBalanceResponse(user.getBalance());
	}
	
}
