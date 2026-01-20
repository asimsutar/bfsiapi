package com.tcs.repository;



import org.springframework.data.jpa.repository.JpaRepository;

import com.tcs.entity.Account;

public interface AccountRepository extends JpaRepository<Account, Long> {
	Account findByUserId(Long userId);

	boolean existsByUserId(Long userId);
}
