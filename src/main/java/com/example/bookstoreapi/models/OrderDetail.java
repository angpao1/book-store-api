package com.example.bookstoreapi.models;

import javax.persistence.*;

@Entity
@Table(name = "Orderdetail")
public class OrderDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int orderdetail_id;
    private int order_id;
    private int book_id;
    private String book_name;
    private String author;
    private float price;

    public OrderDetail() {
    }

    public OrderDetail(int orderdetail_id, int order_id, int book_id, String book_name, String author, float price) {
        this.orderdetail_id = orderdetail_id;
        this.order_id = order_id;
        this.book_id = book_id;
        this.book_name = book_name;
        this.author = author;
        this.price = price;
    }

    public int getOrderdetail_id() {
        return orderdetail_id;
    }

    public void setOrderdetail_id(int orderdetail_id) {
        this.orderdetail_id = orderdetail_id;
    }

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public int getBook_id() {
        return book_id;
    }

    public void setBook_id(int book_id) {
        this.book_id = book_id;
    }

    public String getBook_name() {
        return book_name;
    }

    public void setBook_name(String book_name) {
        this.book_name = book_name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }
}
