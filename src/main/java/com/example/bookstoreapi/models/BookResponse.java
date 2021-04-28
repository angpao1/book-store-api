package com.example.bookstoreapi.models;

import java.util.ArrayList;

public class BookResponse {

    private ArrayList<Book> books;

    public BookResponse() {
    }

    public BookResponse(ArrayList<Book> books) {
        this.books = books;
    }

    public ArrayList<Book> getBooks() {
        return books;
    }

    public void setBooks(ArrayList<Book> books) {
        this.books = books;
    }
}
