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

        // Prevent identical duplicate addresses for same user
        List<Address> existingAddresses = addressRepository.findByUser(user);
        boolean alreadyExists = existingAddresses.stream().anyMatch(a ->
                a.getStreetAddress().equalsIgnoreCase(address.getStreetAddress()) &&
                        a.getCity().equalsIgnoreCase(address.getCity()) &&
                        a.getState().equalsIgnoreCase(address.getState()) &&
                        a.getZipCode().equalsIgnoreCase(address.getZipCode())
        );

        if (alreadyExists) {
            return existingAddresses.stream().filter(a ->
                    a.getStreetAddress().equalsIgnoreCase(address.getStreetAddress()) &&
                            a.getCity().equalsIgnoreCase(address.getCity()) &&
                            a.getState().equalsIgnoreCase(address.getState()) &&
                            a.getZipCode().equalsIgnoreCase(address.getZipCode())
            ).findFirst().get();
        }

        return addressRepository.save(address);
    }

    @Override
    public List<Address> getUserAddresses(User user) {
        return addressRepository.findByUser(user);
    }
}
