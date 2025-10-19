package com.example.E_CommerceSpring.service;

import com.example.E_CommerceSpring.request.ProductRequest;
import com.example.E_CommerceSpring.response.StripeResponse;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class StripeServiceImplementation implements StripeService {

    @Value("${stripe.secretKey}")
    private String secretKey;

    @Override
    public StripeResponse checkoutProducts(ProductRequest productRequest,Long orderId) {
        Stripe.apiKey = secretKey;

        try {
            SessionCreateParams.LineItem.PriceData.ProductData productData =
                    SessionCreateParams.LineItem.PriceData.ProductData.builder()
                            .setName(productRequest.getName())
                            .build();

            SessionCreateParams.LineItem.PriceData priceData =
                    SessionCreateParams.LineItem.PriceData.builder()
                            .setCurrency(productRequest.getCurrency() != null ? productRequest.getCurrency() : "usd")
                            .setUnitAmount(productRequest.getAmount()) // in cents/paise
                            .setProductData(productData)
                            .build();

            SessionCreateParams.LineItem lineItem =
                    SessionCreateParams.LineItem.builder()
                            .setQuantity(productRequest.getQuantity())
                            .setPriceData(priceData)
                            .build();

            // Correct success URL: pass both session_id and orderId
            SessionCreateParams params =
                    SessionCreateParams.builder()
                            .setMode(SessionCreateParams.Mode.PAYMENT)
                            .setSuccessUrl("http://localhost:5173/payment-success?session_id={CHECKOUT_SESSION_ID}&order_id=" + orderId)
                            .setCancelUrl("http://localhost:5173/cart")
                            .addLineItem(lineItem)
                            .build();

            Session session = Session.create(params);

            return StripeResponse.builder()
                    .status("SUCCESS")
                    .message("Payment session created")
                    .sessionId(session.getId())
                    .sessionUrl(session.getUrl())
                    .build();

        } catch (StripeException e) {
            return StripeResponse.builder()
                    .status("FAILED")
                    .message(e.getMessage())
                    .build();
        }
    }
}
