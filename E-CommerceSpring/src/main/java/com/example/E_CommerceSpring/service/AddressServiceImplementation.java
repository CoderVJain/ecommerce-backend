package com.example.E_CommerceSpring.service;

import com.example.E_CommerceSpring.model.Address;
import com.example.E_CommerceSpring.model.User;
import com.example.E_CommerceSpring.repo.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressServiceImplementation implements AddressService {

    @Autowired
    private AddressRepository addressRepository;

    @Override
    public Address saveAddress(User user, Address address) {
        address.setUser(user);
        return addressRepository.save(address);
    }

    @Override
    public List<Address> getUserAddresses(User user) {
        return addressRepository.findByUser(user);
    }
}
