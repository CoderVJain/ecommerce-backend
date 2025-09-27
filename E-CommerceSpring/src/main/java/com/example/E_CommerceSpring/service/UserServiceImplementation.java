package com.example.E_CommerceSpring.service;

import com.example.E_CommerceSpring.config.JwtProvider;
import com.example.E_CommerceSpring.exception.UserException;
import com.example.E_CommerceSpring.model.User;
import com.example.E_CommerceSpring.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class UserServiceImplementation implements UserService{

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtProvider jwtProvider;

    @Override
    public User findUserById(Long userId) throws UserException {
        Optional<User> user = userRepository.findById(userId);
        if(user.isPresent()){
            return user.get();
        }
        else{
            throw new UserException("User not found with id: " + userId);
        }
    }

    @Override
    public User findUserProfileByJwt(String jwt) throws UserException {
        String email = jwtProvider.getEmailFromToken(jwt);

        User user = userRepository.findByEmail(email);

        if(user != null){
            return user;
        }
        else{
            throw new UserException("User not found with email: " + email);
        }

    }
}
