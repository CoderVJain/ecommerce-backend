package com.example.E_CommerceSpring.controller;

import com.example.E_CommerceSpring.exception.ProductException;
import com.example.E_CommerceSpring.exception.UserException;
import com.example.E_CommerceSpring.model.Rating;
import com.example.E_CommerceSpring.model.User;
import com.example.E_CommerceSpring.request.RatingRequest;
import com.example.E_CommerceSpring.service.RatingService;
import com.example.E_CommerceSpring.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ratings")
public class RatingController {

    @Autowired
    private RatingService ratingService;
    @Autowired
    private UserService userService;

    @PostMapping("/create")
    public ResponseEntity<Rating> createRating(@RequestBody RatingRequest req,
                                               @RequestHeader("Authorization") String jwt) throws UserException, ProductException {
        User user = userService.findUserProfileByJwt(jwt);
        Rating rating = ratingService.createRating(req, user);
        return new ResponseEntity<Rating>(rating, HttpStatus.CREATED);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<List<Rating>> getAllProductReviews(@PathVariable("productId") Long productId,
                                                             @RequestHeader("Authorization") String jwt) throws UserException, ProductException {
        userService.findUserProfileByJwt(jwt);
        List<Rating> ratings = ratingService.getAllRatings(productId);
        return new ResponseEntity<List<Rating>>(ratings, HttpStatus.OK);
    }
}
