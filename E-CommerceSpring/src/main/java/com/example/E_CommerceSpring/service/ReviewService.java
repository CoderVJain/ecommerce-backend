package com.example.E_CommerceSpring.service;

import com.example.E_CommerceSpring.exception.ProductException;
import com.example.E_CommerceSpring.model.Review;
import com.example.E_CommerceSpring.model.User;
import com.example.E_CommerceSpring.request.ReviewRequest;

import java.util.List;

public interface ReviewService {

    public Review createReview(ReviewRequest req, User user) throws ProductException;
    public List<Review> getAllReviews(Long productId) throws ProductException;
}
