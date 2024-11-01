package com.uma.service;

import java.util.List;
import java.util.Optional;

import com.uma.entities.User;

public interface UserService {
	
	public User registerUser(User user);
	public Optional<User> loginUser(String email, String password);
	public void updateUser(User updatedUser);
	public void deleteUser(Long userId);
	public boolean isUserEmpty(String email);
	public List<User> getAllUsers();
	public long countNoOfUsers();

}
