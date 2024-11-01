package com.uma.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uma.entities.User;
import com.uma.repositories.UserRepository;

@Service
public class IUserService implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public User registerUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public Optional<User> loginUser(String email, String password) {
        return userRepository.findByEmail(email)
                .filter(user -> user.getPassword().equals(password));
    }

	@Override
	public void updateUser(User updatedUser) {

		userRepository.save(updatedUser);
	}

	@Override
	public void deleteUser(Long userId) {

		userRepository.deleteById(userId);
	}

	@Override
	public boolean isUserEmpty(String email) {

		Optional<User> user = userRepository.findByEmail(email);

		return (user.isEmpty());
		
	}

	@Override
	public List<User> getAllUsers() {
		return userRepository.findAll();
	}

	@Override
	public long countNoOfUsers() {
		return userRepository.count();
	}
}


