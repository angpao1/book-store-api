package com.example.bookstoreapi.models;

import java.util.Comparator;

public class BookRequest {

    private long id;
    private String book_name;
    private String author_name;
    private float price;
    private boolean is_recommended;

    public BookRequest() {
    }

    public BookRequest(long id, String book_name, String author_name, float price, boolean is_recommended) {
        this.id = id;
        this.book_name = book_name;
        this.author_name = author_name;
        this.price = price;
        this.is_recommended = is_recommended;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getBook_name() {
        return book_name;
    }

    public void setBook_name(String book_name) {
        this.book_name = book_name;
    }

    public String getAuthor_name() {
        return author_name;
    }

    public void setAuthor_name(String author_name) {
        this.author_name = author_name;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public boolean isIs_recommended() {
        return is_recommended;
    }

    public void setIs_recommended(boolean is_recommended) {
        this.is_recommended = is_recommended;
    }

    @Override
    public String toString() {
        return "BookRequest{" +
                "id=" + id +
                ", book_name='" + book_name + '\'' +
                ", author_name='" + author_name + '\'' +
                ", price=" + price +
                ", is_recommended=" + is_recommended +
                '}';
    }

    public static Comparator<BookRequest> BookNameComparator
            = new Comparator<BookRequest>() {

        public int compare(BookRequest book1, BookRequest book2) {

            String bookName1 = book1.getBook_name().toUpperCase();
            String bookName2 = book2.getBook_name().toUpperCase();

            //ascending order
            return bookName1.compareTo(bookName2);

            //descending order
            //return bookName2.compareTo(bookName1);
        }

    };
}
