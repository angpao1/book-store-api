package com.example.bookstoreapi.controllers;

import com.example.bookstoreapi.config.Configuration;
import com.example.bookstoreapi.models.Book;
import com.example.bookstoreapi.models.BookRequest;
import com.example.bookstoreapi.models.BookResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;

@RestController
public class BookController {

    @Autowired
    private Configuration externalBookPublisher;

    @GetMapping("/books")
    public ResponseEntity<BookResponse> getBooks() {

        // Build HTTP Request to External Book Publisher
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<BookRequest[]> booksRecommenResp= restTemplate.getForEntity(externalBookPublisher.getURI_BOOKS_RECOMMEN(), BookRequest[].class);
        ResponseEntity<BookRequest[]> booksResp = restTemplate.getForEntity(externalBookPublisher.getURI_BOOKS(), BookRequest[].class);

        // Set Response Body to array
        BookRequest[] booksRecommen = booksRecommenResp.getBody();
        BookRequest[] books = booksResp.getBody();

        // Sort Book Name
        Arrays.sort(booksRecommen, BookRequest.BookNameComparator);
        Arrays.sort(books, BookRequest.BookNameComparator);

        // Keep book id for check duplicate
        long[] booksRecommenID = new long[booksRecommen.length];

        ArrayList<Book> ListBooks = new ArrayList<>();
        // Loop save Book recommendation to ListBooks
        for (int i = 0; i < booksRecommen.length; ++i) {
            Book book = new Book();
            book.setId(booksRecommen[i].getId());
            book.setName(booksRecommen[i].getBook_name());
            book.setAuthor(booksRecommen[i].getAuthor_name());
            book.setPrice(booksRecommen[i].getPrice());
            book.setIs_recommended(true);

            ListBooks.add(book);
            booksRecommenID[i] = booksRecommen[i].getId();
        }
        // Loop save Book to ListBooks
        for(int i = 0; i < books.length; ++i) {
            // Check duplicate
            if(contains(booksRecommenID, (int) books[i].getId())) {
                continue;
            }

            Book book = new Book();
            book.setId(books[i].getId());
            book.setName(books[i].getBook_name());
            book.setAuthor(books[i].getAuthor_name());
            book.setPrice(books[i].getPrice());
            book.setIs_recommended(false);

            ListBooks.add(book);
        }

        // Create Response Message
        BookResponse bookResponse = new BookResponse();
        bookResponse.setBooks(ListBooks);

        return new ResponseEntity<>(bookResponse, HttpStatus.OK);
    }

    public static boolean contains(final long[] array, final int v) {

        boolean result = false;

        for(long i : array){
            if(i == v){
                result = true;
                break;
            }
        }

        return result;
    }
}
