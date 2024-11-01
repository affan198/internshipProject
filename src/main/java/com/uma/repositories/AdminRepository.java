package com.uma.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.uma.entities.Admin;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {
	Optional<Admin> findByEmail(String email);

}
