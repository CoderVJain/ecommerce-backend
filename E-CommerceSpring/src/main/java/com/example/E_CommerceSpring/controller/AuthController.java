package com.example.E_CommerceSpring.controller;

import com.example.E_CommerceSpring.config.JwtProvider;
import com.example.E_CommerceSpring.exception.UserException;
import com.example.E_CommerceSpring.model.Cart;
import com.example.E_CommerceSpring.model.User;
import com.example.E_CommerceSpring.service.CartService;
import com.example.E_CommerceSpring.service.CustomUserServiceImplementation;
import com.example.E_CommerceSpring.repo.UserRepository;
import com.example.E_CommerceSpring.response.AuthResponse;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CustomUserServiceImplementation customUserServiceImplementation;

    @Autowired
    private CartService cartService;

    @PostMapping("/signup")

    public ResponseEntity<AuthResponse> createUser(@RequestBody User user) throws UserException {
        String email = user.getEmail();
        String password = user.getPassword();
        String firstName = user.getFirstName();
        String lastName = user.getLastName();

        User isEmailExit = userRepository.findByEmail(email);

        if (isEmailExit != null) {
            throw new UserException("User already exists with this email");
        }

        if (email == null || email.isEmpty() || password == null || password.isEmpty() || firstName == null || firstName.isEmpty() || lastName == null || lastName.isEmpty()) {
            throw new UserException("Email, Password, First Name, and Last Name are required");
        }

        // Hash the password for storage
        String encodedPassword = passwordEncoder.encode(password);

        // Create and save the new user
        User newUser = new User();
        newUser.setEmail(email);
        newUser.setPassword(encodedPassword);
        newUser.setFirstName(firstName);
        newUser.setLastName(lastName);
        newUser.setRole("USER");
        User savedUser = userRepository.save(newUser);

        // Create a cart for the new user
        Cart cart = cartService.createCart(savedUser);

        // Directly authenticate the new user by loading their UserDetails
        UserDetails userDetails = customUserServiceImplementation.loadUserByUsername(email);

        // Create the authentication token using the userDetails object
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        // Set the authentication in the security context
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Generate the JWT
        String token = jwtProvider.generateToken(authentication);

        // Return the successful response
        AuthResponse authResponse = new AuthResponse(token, "User signed up successfully");
        return new ResponseEntity<AuthResponse>(authResponse, HttpStatus.CREATED);
    }

    @PostMapping("/admin/signup")
    public ResponseEntity<AuthResponse> createAdmin(@RequestBody User user) throws UserException {

        String email = user.getEmail();
        String password = user.getPassword();
        String firstName = user.getFirstName();
        String lastName = user.getLastName();

        User isEmailExit = userRepository.findByEmail(email);

        if (isEmailExit != null) {
            throw new UserException("User already exit with this email");
        }

        if (email == null || email.isEmpty() || password == null || password.isEmpty() || firstName == null || firstName.isEmpty() || lastName == null || lastName.isEmpty()) {
            throw new UserException("Email, Password, First Name and Last Name are required");
        }


        String encodedPassword = passwordEncoder.encode(password);

        User newUser = new User();
        newUser.setEmail(email);
        newUser.setPassword(encodedPassword);
        newUser.setFirstName(firstName);
        newUser.setLastName(lastName);
        newUser.setRole("ADMIN");

        userRepository.save(newUser);

        Authentication auth = new UsernamePasswordAuthenticationToken(email, password);
        SecurityContextHolder.getContext().setAuthentication(auth);

        String token = jwtProvider.generateToken(auth);

        AuthResponse authResponse = new AuthResponse(token, "Admin sign up successfully");
        return new ResponseEntity<AuthResponse>(authResponse, HttpStatus.CREATED);
    }


//    @PostMapping("/login")
//    public ResponseEntity<AuthResponse> loginUser(@RequestBody User user) throws UserException {
//        String email = user.getEmail();
//        String password = user.getPassword();
//
//        if(email == null || email.isEmpty() || password == null || password.isEmpty()){
//            throw new UserException("Email and Password are required");
//        }
//
//        Authentication auth = authenticate(email, password);
//        SecurityContextHolder.getContext().setAuthentication(auth);
//
//        String token = jwtProvider.generateToken(auth);
//
//        AuthResponse authResponse = new AuthResponse(token, "User logged in successfully");
//        return new ResponseEntity<AuthResponse>(authResponse, HttpStatus.OK);
//    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> loginUser(@RequestBody User user) {
        String email = user.getEmail();
        String password = user.getPassword();

        if (email == null || email.isEmpty() || password == null || password.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new AuthResponse(null, "Email and Password are required"));
        }

        try {
            Authentication auth = authenticate(email, password);
            SecurityContextHolder.getContext().setAuthentication(auth);

            String token = jwtProvider.generateToken(auth);

            return ResponseEntity.ok(new AuthResponse(token, "User logged in successfully"));
        } catch (AuthenticationException e) {
            // wrong email or password
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(new AuthResponse(null, "Invalid email or password"));
        }
    }


    private Authentication authenticate(String email, String password) {
        UserDetails userDetails = customUserServiceImplementation.loadUserByUsername(email);

        if (userDetails == null) {
            throw new BadCredentialsException("User not found");
        }
        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("Invalid password");
        }
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
}
