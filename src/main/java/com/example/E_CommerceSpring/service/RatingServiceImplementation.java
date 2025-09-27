package com.example.E_CommerceSpring.service;

import com.example.E_CommerceSpring.exception.ProductException;
import com.example.E_CommerceSpring.model.Product;
import com.example.E_CommerceSpring.model.Rating;
import com.example.E_CommerceSpring.model.User;
import com.example.E_CommerceSpring.repo.RatingRepository;
import com.example.E_CommerceSpring.request.RatingRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RatingServiceImplementation implements RatingService {

    @Autowired
    private ProductService productService;
    @Autowired
    private RatingRepository ratingRepository;

    @Override
    public Rating createRating(RatingRequest req, User user) throws ProductException {
        Product product = productService.getProductById(req.getProductId());

        Rating rating = new Rating();
        rating.setProduct(product);
        rating.setUser(user);
        rating.setRating(req.getRating());
        rating.setCreatedAt(LocalDateTime.now());
        return ratingRepository.save(rating);
    }

    @Override
    public List<Rating> getAllRatings(Long productId) {
        return ratingRepository.getAllProductRatings(productId);
    }
}
