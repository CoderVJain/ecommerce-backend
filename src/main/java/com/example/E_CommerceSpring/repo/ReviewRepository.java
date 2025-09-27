package com.example.E_CommerceSpring.repo;

import com.example.E_CommerceSpring.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query("SELECT r FROM Review r WHERE r.product.id = :productId")
    public List<Review> getAllByProductsReview(@Param("productId") Long productId);
}
