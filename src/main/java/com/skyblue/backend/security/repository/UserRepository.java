package com.skyblue.backend.security.repository;

import com.skyblue.backend.security.entity.UserLogin;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserLogin,Integer> {
	Optional<UserLogin> findByNameUser(String nameUser);
	boolean existsByNameUser(String nameUser);
	boolean existsByEmail(String email);
}
