package com.example.E_CommerceSpring.repo;

import com.example.E_CommerceSpring.model.Address;
import com.example.E_CommerceSpring.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
    List<Address> findByUser(User user);
}
