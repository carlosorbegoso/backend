package com.skyblue.backend.security.service;

import com.skyblue.backend.security.entity.Role;
import com.skyblue.backend.security.enums.RoleName;
import com.skyblue.backend.security.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
public class RoleService {
	@Autowired
	RoleRepository roleRepository;

	public Optional<Role>getByRolName(RoleName roleName){
		return  roleRepository.findByRoleName(roleName);
	}
	public void save(Role role){
		roleRepository.save(role);
	}
}
