package com.example.E_CommerceSpring.service;


import com.example.E_CommerceSpring.exception.UserException;
import com.example.E_CommerceSpring.model.User;
import org.springframework.stereotype.Service;

@Service
public interface UserService {

    public User findUserById(Long userId) throws UserException;
    public User findUserProfileByJwt(String jwt) throws UserException;
}
