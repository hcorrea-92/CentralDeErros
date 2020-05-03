package com.v1.CentralDeErros.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.v1.CentralDeErros.models.UserApp;

@Repository
public interface UserRepository extends JpaRepository<UserApp, Integer> {
	UserApp findByUsername(String username);
}