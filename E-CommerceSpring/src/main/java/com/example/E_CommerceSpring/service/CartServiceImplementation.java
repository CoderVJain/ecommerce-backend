package com.example.E_CommerceSpring.service;

import com.example.E_CommerceSpring.exception.CartItemException;
import com.example.E_CommerceSpring.exception.ProductException;
import com.example.E_CommerceSpring.exception.UserException;
import com.example.E_CommerceSpring.model.Cart;
import com.example.E_CommerceSpring.model.CartItem;
import com.example.E_CommerceSpring.model.Product;
import com.example.E_CommerceSpring.model.User;
import com.example.E_CommerceSpring.repo.CartRepository;
import com.example.E_CommerceSpring.request.AddItemRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CartServiceImplementation implements CartService {

    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private CartItemService cartItemService;
    @Autowired
    private ProductService productService;

    @Override
    public Cart createCart(User user) {
        Cart cart = new Cart();
        cart.setUser(user);
        return cartRepository.save(cart);
    }

    @Override
    public String addCartItem(Long userId, AddItemRequest req) throws ProductException, CartItemException, UserException {
        Cart cart = cartRepository.findByUserId(userId);
        Product product = productService.getProductById(req.getProductId());

        CartItem isCartItemPresent = cartItemService.doesCartItemExist(cart, product, req.getSize(), userId);
        if (isCartItemPresent == null) {
            CartItem cartItem = new CartItem();
            cartItem.setCart(cart);
            cartItem.setProduct(product);
            cartItem.setQuantity(req.getQuantity());
            cartItem.setUserId(userId);
            cartItem.setPrice(product.getPrice() * req.getQuantity());
            cartItem.setDiscountedPrice(product.getDiscountedPrice() * req.getQuantity());
            cartItem.setSize(req.getSize());
            CartItem createdCartItem = cartItemService.createCartItem(cartItem);
            cart.getCartItems().add(createdCartItem);
        }
        else {
            // The item already exists; update its quantity
            CartItem updatedItem = new CartItem();
            updatedItem.setQuantity(isCartItemPresent.getQuantity() + req.getQuantity());
            updatedItem.setPrice(product.getPrice() * updatedItem.getQuantity());
            updatedItem.setDiscountedPrice(product.getDiscountedPrice() * updatedItem.getQuantity());
            cartItemService.updateCartItem(userId,isCartItemPresent.getId(),updatedItem);
        }
        return "Item added to cart successfully";
    }

    @Override
    public Cart getUserCart(Long userId) {
        Cart cart = cartRepository.findByUserId(userId);

        double totalPrice = 0;
        int totalDicountedPrice = 0;
        int totalItems = 0;


            for (CartItem item : cart.getCartItems()) {
                totalPrice += item.getPrice();
                totalDicountedPrice += item.getDiscountedPrice();
                totalItems += item.getQuantity();
            }
            cart.setTotalPrice(totalPrice);
            cart.setTotalDiscountedPrice(totalDicountedPrice);
            cart.setTotalItem(totalItems);
            cart.setDiscount((int) (totalPrice - totalDicountedPrice));
            return cartRepository.save(cart);
    }

}
