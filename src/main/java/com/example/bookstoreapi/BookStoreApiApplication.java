package com.example.bookstoreapi;

import com.example.bookstoreapi.services.BookService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import redis.clients.jedis.Jedis;

@SpringBootApplication
public class BookStoreApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(BookStoreApiApplication.class, args);
        fetchBookFormPublisger();
    }

    public static void fetchBookFormPublisger() {
        BookService bookService = new BookService();
        bookService.fetchBookFormPublisger();
    }
}
