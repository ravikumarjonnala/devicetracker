package com.vodafone.devicetracker.exceptions;

public class InvalidProductIdException extends Exception {
    private static final long serialVersionUID = 2574636319520536079L;
    private String productId;

    public InvalidProductIdException(String productId) {
        this.productId = productId;
    }

    public String getProductId() {
        return productId;
    }
}
