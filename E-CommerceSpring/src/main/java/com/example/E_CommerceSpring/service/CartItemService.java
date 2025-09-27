package com.example.E_CommerceSpring.service;

import com.example.E_CommerceSpring.exception.CartItemException;
import com.example.E_CommerceSpring.exception.UserException;
import com.example.E_CommerceSpring.model.Cart;
import com.example.E_CommerceSpring.model.CartItem;
import com.example.E_CommerceSpring.model.Product;

public interface CartItemService {

    public CartItem createCartItem(CartItem cartItem);
    public CartItem updateCartItem(Long userId,Long id,CartItem cartItem) throws CartItemException, UserException;

    public CartItem doesCartItemExist(Cart cart, Product product, String size, Long userId);

    public void removeCartItem(Long userId, Long cartItemId) throws CartItemException, UserException;

    public CartItem getCartItemById(Long cartItemId) throws CartItemException;


}
