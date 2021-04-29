package com.example.bookstoreapi.models;

import java.util.Arrays;
import java.util.List;

public class OrderRequest {

    private List<Integer> orders;

    public OrderRequest() {
    }

    public OrderRequest(List<Integer> orders) {
        this.orders = orders;
    }

    public List<Integer> getOrders() {
        return orders;
    }

    public void setOrders(List<Integer> orders) {
        this.orders = orders;
    }

    @Override
    public String toString() {
        return "OrderRequest{" +
                "orders=" + orders +
                '}';
    }
}
