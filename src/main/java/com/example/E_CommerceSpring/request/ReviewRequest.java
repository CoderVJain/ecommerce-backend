package com.example.E_CommerceSpring.request;

public class ReviewRequest {

    private Long productId;
    public String reviewText;

    public ReviewRequest() {
    }

    public ReviewRequest(Long productId, String reviewText) {
        this.productId = productId;
        this.reviewText = reviewText;
    }

    public Long getProductId() {
        return productId;
    }
    public void setProductId(Long productId) {
        this.productId = productId;
    }
    public String getReviewText() {
        return reviewText;
    }
    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }

}
