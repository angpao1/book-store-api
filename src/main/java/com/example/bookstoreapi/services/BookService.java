package com.example.bookstoreapi.services;

import com.example.bookstoreapi.models.Book;
import com.example.bookstoreapi.models.BookRequest;
import com.example.bookstoreapi.models.Books;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.Gson;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;

@Service
public class BookService {

    public void fetchBookFormPublisger() {
        // Build HTTP Request to External Book Publisher
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<BookRequest[]> booksRecommenResp= restTemplate.getForEntity("https://scb-test-book-publisher.herokuapp.com/books/recommendation", BookRequest[].class);
        ResponseEntity<BookRequest[]> booksResp = restTemplate.getForEntity("https://scb-test-book-publisher.herokuapp.com/books", BookRequest[].class);

        // Set Response Body to array
        BookRequest[] booksRecommen = booksRecommenResp.getBody();
        BookRequest[] booksTotal = booksResp.getBody();

        // Sort Book Name
        Arrays.sort(booksRecommen, BookRequest.BookNameComparator);
        Arrays.sort(booksTotal, BookRequest.BookNameComparator);

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
        for(int i = 0; i < booksTotal.length; ++i) {
            // Check duplicate
            if(contains(booksRecommenID, (int) booksTotal[i].getId())) {
                continue;
            }

            Book book = new Book();
            book.setId(booksTotal[i].getId());
            book.setName(booksTotal[i].getBook_name());
            book.setAuthor(booksTotal[i].getAuthor_name());
            book.setPrice(booksTotal[i].getPrice());
            book.setIs_recommended(false);

            ListBooks.add(book);
        }

        // Create Response Message
        Books bookResponse = new Books();
        bookResponse.setBooks(ListBooks);

        CacheService cacheService = new CacheService();
        String status = null;
        try {
            status = cacheService.save("books", bookResponse);
            System.out.println("Save books to redis : " + status);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public Books getBook() {
        CacheService cacheService = new CacheService();
        String booksJson = cacheService.getData("books");

        Gson gson = new Gson();
        Books books = gson.fromJson(booksJson, Books.class);
        return books;
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
