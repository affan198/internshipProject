package com.uma.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uma.entities.Admin;
import com.uma.repositories.AdminRepository;

@Service
public class IAdminService implements AdminService {
	
	@Autowired
	private AdminRepository adminRepository;

	@Override
	public boolean isAdminEmpty(String email) {
		
		Optional<Admin> admin = adminRepository.findByEmail(email);
		return admin.isEmpty();
	}

	@Override
	public void registerAdmin(Admin admin) {

		adminRepository.save(admin);
	}

	@Override
	public Optional<Admin> adminLoginCredential(String email, String password) {
		
		return adminRepository.findByEmail(email)
                .filter(user -> user.getPassword().equals(password));
		
	}

	

}
