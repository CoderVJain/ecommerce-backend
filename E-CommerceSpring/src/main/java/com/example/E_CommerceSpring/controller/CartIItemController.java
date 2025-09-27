package com.example.E_CommerceSpring.controller;

import com.example.E_CommerceSpring.exception.CartItemException;
import com.example.E_CommerceSpring.exception.UserException;
import com.example.E_CommerceSpring.model.CartItem;
import com.example.E_CommerceSpring.model.User;
import com.example.E_CommerceSpring.response.ApiResponse;
import com.example.E_CommerceSpring.service.CartItemService;
import com.example.E_CommerceSpring.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart_items")
public class CartIItemController {

    @Autowired
    private CartItemService cartItemService;
    @Autowired
    private UserService userService;

    @DeleteMapping("/{cartItemId}")
    public ResponseEntity<ApiResponse> deleteCartItem(@PathVariable Long cartItemId,
                                                      @RequestHeader("Authorization") String jwt) throws UserException, CartItemException {
        User  user = userService.findUserProfileByJwt(jwt);
        cartItemService.removeCartItem(user.getId(), cartItemId);

        ApiResponse res = new ApiResponse();
        res.setMessage("Cart item deleted successfully");
        res.setStatus(true);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PutMapping("/{cartItemId}")
    public ResponseEntity<CartItem> updateCartItem(@PathVariable Long cartItemId,
                                                   @RequestBody CartItem cartItem,
                                                   @RequestHeader("Authorization") String jwt) throws UserException, CartItemException {
        User user = userService.findUserProfileByJwt(jwt);
        CartItem updatedItem = cartItemService.updateCartItem(user.getId(), cartItemId, cartItem);
        return new ResponseEntity<CartItem>(updatedItem, HttpStatus.OK);
    }

    @GetMapping("/{cartItemId}")
    public ResponseEntity<CartItem> getCartItemById(@PathVariable Long cartItemId,
                                                    @RequestHeader("Authorization") String jwt) throws UserException, CartItemException {
        User user = userService.findUserProfileByJwt(jwt);
        CartItem cartItem = cartItemService.getCartItemById(cartItemId);
        return new ResponseEntity<CartItem>(cartItem, HttpStatus.OK);
    }
}
