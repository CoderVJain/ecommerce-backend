package com.example.E_CommerceSpring.controller;

import com.example.E_CommerceSpring.exception.OrderException;
import com.example.E_CommerceSpring.model.Order;
import com.example.E_CommerceSpring.repo.OrderRepository;
import com.example.E_CommerceSpring.request.ProductRequest;
import com.example.E_CommerceSpring.response.ApiResponse;
import com.example.E_CommerceSpring.response.StripeResponse;
import com.example.E_CommerceSpring.service.OrderService;
import com.example.E_CommerceSpring.service.StripeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class StripeController {

    @Autowired
    private StripeService stripeService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    @PostMapping("/payments/{orderId}")
    public ResponseEntity<StripeResponse> checkoutProducts(@PathVariable Long orderId,
                                                           @RequestHeader("Authorization") String jwt)
            throws OrderException {

        Order order = orderService.getOrderById(orderId);

        // Use order total as the amount
        ProductRequest productRequest = new ProductRequest(
                (long) (order.getTotalDiscountedPrice() * 100), // amount in paise/cents
                1L, // full order as single quantity
                String.valueOf(order.getId()), // use orderId for success URL
                "INR" // or "USD"
        );

        StripeResponse response = stripeService.checkoutProducts(productRequest,orderId);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/payment-success")
    public ResponseEntity<ApiResponse> paymentSuccess(@RequestParam("session_id") String sessionId,
                                                      @RequestParam("order_id") String orderId)
            throws OrderException {

        Order order = orderService.getOrderById(Long.parseLong(orderId));

        order.getPaymentDetails().setPaymentId(sessionId);
        order.getPaymentDetails().setStatus("COMPLETED");
        order.setOrderStatus("PLACED");
        orderRepository.save(order);

        return new ResponseEntity<>(new ApiResponse("Your order has been placed successfully!", true),
                HttpStatus.ACCEPTED);
    }

    @GetMapping("/cancel")
    public ResponseEntity<ApiResponse> paymentCancel() {
        return new ResponseEntity<>(new ApiResponse("Payment was canceled", false), HttpStatus.OK);
    }
}
