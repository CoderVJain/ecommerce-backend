package com.example.E_CommerceSpring.repo;

import com.example.E_CommerceSpring.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    public User findByEmail(String email);
}
