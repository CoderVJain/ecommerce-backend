package com.example.E_CommerceSpring.controller;

import com.example.E_CommerceSpring.exception.CartItemException;
import com.example.E_CommerceSpring.exception.ProductException;
import com.example.E_CommerceSpring.exception.UserException;
import com.example.E_CommerceSpring.model.Cart;
import com.example.E_CommerceSpring.model.User;
import com.example.E_CommerceSpring.request.AddItemRequest;
import com.example.E_CommerceSpring.response.ApiResponse;
import com.example.E_CommerceSpring.service.CartService;
import com.example.E_CommerceSpring.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartService cartService;
    @Autowired
    private UserService userService;

    @GetMapping("/")
    public ResponseEntity<Cart> getUserCart(@RequestHeader("Authorization")String jwt) throws UserException {
        User user = userService.findUserProfileByJwt(jwt);
        Cart cart = cartService.getUserCart(user.getId());

        return new ResponseEntity<Cart>(cart, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addItemToCart(@RequestHeader("Authorization")String jwt,@RequestBody AddItemRequest req) throws UserException, ProductException, CartItemException{
        User user = userService.findUserProfileByJwt(jwt);
        String message = cartService.addCartItem(user.getId(),req);
        ApiResponse response = new ApiResponse();
        response.setMessage(message);
        response.setStatus(true);
        return new ResponseEntity<ApiResponse>(response, HttpStatus.CREATED);
    }
}
