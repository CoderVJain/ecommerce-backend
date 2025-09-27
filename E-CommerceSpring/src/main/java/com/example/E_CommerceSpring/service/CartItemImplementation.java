    package com.example.E_CommerceSpring.service;

    import com.example.E_CommerceSpring.exception.CartItemException;
    import com.example.E_CommerceSpring.exception.UserException;
    import com.example.E_CommerceSpring.model.Cart;
    import com.example.E_CommerceSpring.model.CartItem;
    import com.example.E_CommerceSpring.model.Product;
    import com.example.E_CommerceSpring.repo.CartItemRepository;
    import com.example.E_CommerceSpring.repo.CartRepository;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.stereotype.Service;

    import java.util.Optional;

    @Service
    public class CartItemImplementation implements CartItemService {

        @Autowired
        private CartItemRepository cartItemRepository;
        @Autowired
        private CartRepository cartRepository;

        @Override
        public CartItem createCartItem(CartItem cartItem) {
            cartItem.setQuantity(1);
            cartItem.setPrice(cartItem.getProduct().getPrice()* cartItem.getQuantity());
            cartItem.setDiscountedPrice(cartItem.getProduct().getDiscountedPrice()* cartItem.getQuantity());
            return cartItemRepository.save(cartItem);
        }

        @Override
        public CartItem updateCartItem(Long userId, Long id, CartItem cartItem) throws CartItemException, UserException {
            CartItem item = getCartItemById(id);

            if (!item.getUserId().equals(userId)) {
                throw new UserException("You are not authorized to update this cart item");
            }
            item.setQuantity(cartItem.getQuantity());
            item.setPrice(item.getProduct().getPrice() * item.getQuantity());
            item.setDiscountedPrice(item.getProduct().getDiscountedPrice() * item.getQuantity());
            return cartItemRepository.save(item);
        }

        @Override
        public CartItem doesCartItemExist(Cart cart, Product product, String size, Long userId) {
            CartItem item = cartItemRepository.isCartItemExist(cart, product, size, userId);
            return item;
        }

        @Override
        public void removeCartItem(Long userId, Long cartItemId) throws CartItemException, UserException {
            CartItem item = getCartItemById(cartItemId);

            if (item.getUserId().equals(userId)) {
                cartItemRepository.deleteById(cartItemId);
            } else {
                throw new UserException("You are not authorized to delete this cart item");
            }
        }

        @Override
        public CartItem getCartItemById(Long cartItemId) throws CartItemException {
            Optional<CartItem> cartItem = cartItemRepository.findById(cartItemId);

            if (cartItem.isPresent()) {
                return cartItem.get();
            } else {
                throw new CartItemException("Cart item not found with id: " + cartItemId);
            }
        }
    }
