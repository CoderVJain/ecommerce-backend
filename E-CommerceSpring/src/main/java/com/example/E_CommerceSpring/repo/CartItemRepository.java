package com.example.E_CommerceSpring.repo;

import com.example.E_CommerceSpring.model.Cart;
import com.example.E_CommerceSpring.model.CartItem;
import com.example.E_CommerceSpring.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CartItemRepository extends JpaRepository<CartItem,Long> {


    @Query( "SELECT c FROM CartItem c WHERE c.cart=:cart And c.product=:product AND c.size=:size AND c.userId=:userId" )
    public CartItem isCartItemExist(@Param("cart") Cart cart,
                                    @Param("product") Product product,
                                    @Param("size") String size,
                                    @Param("userId") Long userId);
}
