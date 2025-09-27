package com.example.E_CommerceSpring.controller;

import com.example.E_CommerceSpring.exception.UserException;
import com.example.E_CommerceSpring.model.Address;
import com.example.E_CommerceSpring.model.User;
import com.example.E_CommerceSpring.service.AddressService;
import com.example.E_CommerceSpring.service.UserServiceImplementation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/addresses")
public class AddressController {

    @Autowired
    private AddressService addressService;

    @Autowired
    private UserServiceImplementation userService;


    @GetMapping
    public List<Address> getUserAddresses(@RequestHeader("Authorization") String jwt) throws UserException {

        User user = userService.findUserProfileByJwt(jwt);
        return addressService.getUserAddresses(user);
    }


    @PostMapping
    public Address saveAddress(@RequestBody Address address,
                               @RequestHeader("Authorization") String jwt) throws UserException {

        User user = userService.findUserProfileByJwt(jwt);
        return addressService.saveAddress(user, address);
    }
}
