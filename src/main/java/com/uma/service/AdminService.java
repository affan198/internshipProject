package com.uma.service;

import java.util.Optional;

import com.uma.entities.Admin;

public interface AdminService {
	
	public boolean isAdminEmpty(String email);
	public void registerAdmin(Admin admin);
	public Optional<Admin> adminLoginCredential(String email, String password);
	

}
