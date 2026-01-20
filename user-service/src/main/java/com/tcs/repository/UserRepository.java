package com.tcs.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tcs.entity.User;
import java.util.List;


public interface UserRepository extends JpaRepository<User, Long> {
	boolean existsByUsername(String username);
    boolean existsByPanNumber(String panNumber);
    boolean existsByPhone(String phone);
    boolean existsByEmail(String email);
    User findByUsername(String username);
}
