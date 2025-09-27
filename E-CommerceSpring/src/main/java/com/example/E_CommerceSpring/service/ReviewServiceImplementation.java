package com.example.E_CommerceSpring.service;

import com.example.E_CommerceSpring.exception.ProductException;
import com.example.E_CommerceSpring.model.Product;
import com.example.E_CommerceSpring.model.Rating;
import com.example.E_CommerceSpring.model.Review;
import com.example.E_CommerceSpring.model.User;
import com.example.E_CommerceSpring.repo.ProductRepository;
import com.example.E_CommerceSpring.repo.ReviewRepository;
import com.example.E_CommerceSpring.request.ReviewRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReviewServiceImplementation implements ReviewService {

    @Autowired
    private ProductService productService;
    @Autowired
    private ReviewRepository reviewRepository;

    @Override
    public Review createReview(ReviewRequest req, User user) throws ProductException {
        Product product = productService.getProductById(req.getProductId());

        Review review = new Review();
        review.setUser(user);
        review.setProduct(product);
        review.setReview(req.getReviewText());
        review.setCreatedAt(LocalDateTime.now());

        return reviewRepository.save(review);
    }

    @Override
    public List<Review> getAllReviews(Long productId) throws ProductException {
        return reviewRepository.getAllByProductsReview(productId);
    }
}
