package com.example.E_CommerceSpring.service;

import com.example.E_CommerceSpring.model.Address;
import com.example.E_CommerceSpring.model.User;

import java.util.List;

public interface AddressService {
    Address saveAddress(User user, Address address);
    List<Address> getUserAddresses(User user);
}
