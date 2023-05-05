package com.project.crypto.repository;

import com.project.crypto.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {

    User findByUsername(String username);

    User findByRole(String role);
}
