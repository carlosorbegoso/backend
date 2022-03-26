package com.skyblue.backend.security.service;

import com.skyblue.backend.security.entity.UserLogin;
import com.skyblue.backend.security.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
public class UserService  {
//	@PersistenceContext
//	private EntityManager entityManager;
	@Autowired
	UserRepository userRepository;

	public Optional<UserLogin> getByNameUser(String nameUser){
		return userRepository.findByNameUser(nameUser);
	}
	public boolean existsByNameUser(String nameUser){
		return  userRepository.existsByNameUser(nameUser);
	}
	public boolean existsByEmail(String email){
		return userRepository.existsByEmail(email);
	}
	public void save (UserLogin user){
		userRepository.save(user);
	}
}
