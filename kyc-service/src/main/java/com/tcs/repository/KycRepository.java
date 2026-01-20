package com.tcs.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tcs.entity.KycRequest;

public interface KycRepository extends JpaRepository<KycRequest, Long> {
	boolean existsByUserIdAndStatusIn(
	        Long userId, List<String> status
	    );
}
