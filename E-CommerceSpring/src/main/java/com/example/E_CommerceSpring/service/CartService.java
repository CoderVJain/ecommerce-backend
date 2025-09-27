package com.example.E_CommerceSpring.service;

import com.example.E_CommerceSpring.exception.CartItemException;
import com.example.E_CommerceSpring.exception.ProductException;
import com.example.E_CommerceSpring.exception.UserException;
import com.example.E_CommerceSpring.model.Cart;
import com.example.E_CommerceSpring.model.User;
import com.example.E_CommerceSpring.request.AddItemRequest;

public interface CartService {

    public Cart createCart(User user);
    public String addCartItem(Long userId, AddItemRequest req) throws ProductException, CartItemException, UserException;
    public Cart getUserCart(Long userId);
}
