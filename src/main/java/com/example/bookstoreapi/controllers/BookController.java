package com.example.bookstoreapi.controllers;

import com.example.bookstoreapi.models.Books;
import com.example.bookstoreapi.services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BookController {

    @Autowired
    private BookService bookService;

    @GetMapping("/books")
    public ResponseEntity<Books> getBooks() {

        Books books = bookService.getBook();

        return new ResponseEntity<>(books, HttpStatus.OK);
    }
}
