package com.example.bookstoreapi.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("book-store-api")
public class Configuration {

    private String URI_BOOKS;
    private String URI_BOOKS_RECOMMEN;

    public String getURI_BOOKS() {
        return URI_BOOKS;
    }

    public void setURI_BOOKS(String URI_BOOKS) {
        this.URI_BOOKS = URI_BOOKS;
    }

    public String getURI_BOOKS_RECOMMEN() {
        return URI_BOOKS_RECOMMEN;
    }

    public void setURI_BOOKS_RECOMMEN(String URI_BOOKS_RECOMMEN) {
        this.URI_BOOKS_RECOMMEN = URI_BOOKS_RECOMMEN;
    }
}
