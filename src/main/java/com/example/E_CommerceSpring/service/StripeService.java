package com.example.E_CommerceSpring.service;

import com.example.E_CommerceSpring.request.ProductRequest;
import com.example.E_CommerceSpring.response.StripeResponse;

public interface StripeService {

    public StripeResponse checkoutProducts(ProductRequest productRequest,Long orderId);
}
