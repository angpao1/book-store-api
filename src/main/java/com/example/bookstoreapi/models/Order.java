package com.example.bookstoreapi.models;

import java.util.List;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "Orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int order_id;
    private int user_id;
    private float price;
    private String createAt;

    public Order() {
    }

    public Order(int order_id, int user_id, float price, String createAt) {
        this.order_id = order_id;
        this.user_id = user_id;
        this.price = price;
        this.createAt = createAt;
    }

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getCreateAt() {
        return createAt;
    }

    public void setCreateAt(String createAt) {
        this.createAt = createAt;
    }

    @Override
    public String toString() {
        return "Order{" +
                "order_id=" + order_id +
                ", user_id=" + user_id +
                ", price=" + price +
                ", createAt='" + createAt + '\'' +
                '}';
    }
}
