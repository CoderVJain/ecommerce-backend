package com.example.E_CommerceSpring.controller;

import com.example.E_CommerceSpring.exception.ProductException;
import com.example.E_CommerceSpring.exception.UserException;
import com.example.E_CommerceSpring.model.Review;
import com.example.E_CommerceSpring.model.User;
import com.example.E_CommerceSpring.request.ReviewRequest;
import com.example.E_CommerceSpring.service.ReviewService;
import com.example.E_CommerceSpring.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;
    @Autowired
    private UserService userService;

    @PostMapping("/create")
    public ResponseEntity<Review> createReview(@RequestBody ReviewRequest req, @RequestHeader("Authorization") String jwt) throws UserException, ProductException {
        User user = userService.findUserProfileByJwt(jwt);
        Review review = reviewService.createReview(req, user);
        return new ResponseEntity<Review>(review, HttpStatus.CREATED);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<List<Review>> getAllProductReviews(@PathVariable Long productId,
                                                             @RequestHeader("Authorization") String jwt) throws UserException, ProductException {
        User user = userService.findUserProfileByJwt(jwt);
        List<Review> reviews = reviewService.getAllReviews(productId);
        return new ResponseEntity<List<Review>>(reviews, HttpStatus.OK);
    }

}
