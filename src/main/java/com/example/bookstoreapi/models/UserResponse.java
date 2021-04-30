package com.example.bookstoreapi.models;

import java.util.List;

public class UserResponse {

    private String name;
    private String surname;
    private String date_od_birth;
    private List<Integer> books;

    public UserResponse() {
    }

    public UserResponse(String name, String surname, String date_od_birth, List<Integer> books) {
        this.name = name;
        this.surname = surname;
        this.date_od_birth = date_od_birth;
        this.books = books;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getDate_od_birth() {
        return date_od_birth;
    }

    public void setDate_od_birth(String date_od_birth) {
        this.date_od_birth = date_od_birth;
    }

    public List<Integer> getBooks() {
        return books;
    }

    public void setBooks(List<Integer> books) {
        this.books = books;
    }
}
