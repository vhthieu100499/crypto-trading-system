package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.TradingHistory;

@Repository
public interface TradingHistoryRepository extends JpaRepository<TradingHistory, Integer> {
	
	List<TradingHistory> findByUserId(int userId);
}
