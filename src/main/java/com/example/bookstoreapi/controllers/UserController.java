package com.example.bookstoreapi.controllers;

import com.example.bookstoreapi.models.*;
import com.example.bookstoreapi.services.*;
import com.example.bookstoreapi.util.EncryptData;
import com.example.bookstoreapi.util.JwtTokenUtil;
import com.example.bookstoreapi.util.Validator;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    OrderService orderService;

    @Autowired
    OrderDetailService orderDetailService;

    @Autowired
    BookService bookService;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User user) {

        if (user.getUsername().equals("") || user.getUsername() == null) {
            return new ResponseEntity<>("username is empty", HttpStatus.BAD_REQUEST);
        }
        if (user.getPassword().equals("") || user.getPassword() == null) {
            return new ResponseEntity<>("password is empty", HttpStatus.BAD_REQUEST);
        }

        String passwordHash = "";
        try {
            passwordHash = EncryptData.toHexString(EncryptData.getSHA(user.getPassword()));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        User _user = userService.findUser(user.getUsername(), passwordHash);
        if (_user == null) {
            return new ResponseEntity<>("username or password invalid", HttpStatus.FORBIDDEN);
        }

        final String token = JwtTokenUtil.createJWT(_user);
//        System.out.println(token);
        CacheService cacheService = new CacheService();
        cacheService.saveToken(token);

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("token", token);
        return new ResponseEntity<>("", responseHeaders, HttpStatus.OK);
    }

    @PostMapping("/users")
    public ResponseEntity<String> createUser(@RequestBody User user) {

        if (user.getUsername().equals("") || user.getUsername() == null) {
            return new ResponseEntity<>("username is empty", HttpStatus.BAD_REQUEST);
        }
        if (user.getPassword().equals("") || user.getPassword() == null) {
            return new ResponseEntity<>("password is empty", HttpStatus.BAD_REQUEST);
        }
        if (user.getDate_of_birth().equals("") || user.getDate_of_birth() == null) {
            return new ResponseEntity<>("date birth is empty", HttpStatus.BAD_REQUEST);
        }
        if (!Validator.isValidDateFormat("dd/MM/yyyy", user.getDate_of_birth())) {
            return new ResponseEntity<>("date birth invalid format", HttpStatus.BAD_REQUEST);
        }

        boolean duplicate = userService.checkDuplicateUsername(user.getUsername());
        if (duplicate) {
            return new ResponseEntity<>("username already exists", HttpStatus.BAD_REQUEST);
        }

        try {
            String username = user.getUsername();
            String password = user.getPassword();
            String date_of_birth = user.getDate_of_birth();
            String[] fullname = user.getUsername().split("[.]");
            String name = "";
            String surname = "";
            if (fullname.length > 1) {
                name = fullname[0];
                surname = fullname[1];
            } else {
                name = fullname[0];
                surname = fullname[0];
            }

            String passwordHash = EncryptData.toHexString(EncryptData.getSHA(password));

            Date date = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            System.out.println(formatter.format(date));

            userService.createUser(new User(username, passwordHash, name, surname, date_of_birth, formatter.format(date)));
            return new ResponseEntity<>("", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("500 Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/users/orders")
    public ResponseEntity<String> createOrder(@RequestHeader(name = "Authorization", required = true) String token, @RequestBody OrderRequest orders) {

        Jws<Claims> jws;
        try {
            jws = JwtTokenUtil.verifyToken(token);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("access denied", HttpStatus.UNAUTHORIZED);
        }
        if (jws == null) {
            System.out.println("Invalid token");
            return new ResponseEntity<>("access denied", HttpStatus.UNAUTHORIZED);
        }

        boolean tokenExpire = Validator.isTokenExpire(token);
        if (tokenExpire) {
            System.out.println("Token expire");
            return new ResponseEntity<>("access denied", HttpStatus.UNAUTHORIZED);
        }

        int user_id = (int) jws.getBody().get("user_id");
        float price = 0;

        Books books = bookService.getBook();
        List<OrderDetail> bookList = new ArrayList<>();
        for (int i = 0; i < orders.getOrders().size(); ++i) {
            for (int j = 0; j < books.getBooks().size(); ++j) {
                if (orders.getOrders().get(i) == books.getBooks().get(j).getId()) {
                    price += books.getBooks().get(j).getPrice();
                    System.out.println(books.getBooks().get(j).toString());

                    OrderDetail book = new OrderDetail();
                    book.setBook_id(books.getBooks().get(j).getId());
                    book.setBook_name(books.getBooks().get(j).getName());
                    book.setAuthor(books.getBooks().get(j).getAuthor());
                    book.setPrice(books.getBooks().get(j).getPrice());
                    bookList.add(book);
                    break;
                }
            }
        }
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        System.out.println(formatter.format(date));

        Order order = new Order();
        order.setUser_id(user_id);
        order.setPrice(price);
        order.setCreateAt(formatter.format(date));
        Order _order = orderService.createOrder(order);

        for (int i = 0; i < bookList.size(); ++i) {
            bookList.get(i).setOrder_id(_order.getOrder_id());
            System.out.println(bookList.get(i).toString());
        }

        try {
            orderDetailService.createOrderDetail(bookList);
        } catch (Exception e) {
            e.printStackTrace();
        }

        JSONObject response = new JSONObject();
        response.put("price", order.getPrice());

        return new ResponseEntity<>(response.toString(), HttpStatus.OK);
    }

    @DeleteMapping("/users")
    public ResponseEntity<String> deleteUserAndOrder(@RequestHeader(name = "Authorization", required = true) String token) {
        Jws<Claims> jws;
        try {
            jws = JwtTokenUtil.verifyToken(token);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("access denied", HttpStatus.UNAUTHORIZED);
        }
        if (jws == null) {
            System.out.println("Invalid token");
            return new ResponseEntity<>("access denied", HttpStatus.UNAUTHORIZED);
        }

        boolean tokenExpire = Validator.isTokenExpire(token);
        if (tokenExpire) {
            System.out.println("Token expire");
            return new ResponseEntity<>("access denied", HttpStatus.UNAUTHORIZED);
        }

        int user_id = (int) jws.getBody().get("user_id");
        List<Order> orders = new ArrayList<>();
        try {
            orders = orderService.getMyOrders(user_id);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return new ResponseEntity<>("", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (orders.size() != 0) {
            for (int i = 0; i < orders.size(); ++i) {
                orderDetailService.deleteMyOrder(orders.get(i).getOrder_id());
            }
        }

        orderService.deleteMyOrders(user_id);
        userService.deleteUser(user_id);

        CacheService cacheService = new CacheService();
        cacheService.deleteKey(token);
        return new ResponseEntity<>("", HttpStatus.OK);
    }

    @GetMapping("/users")
    public ResponseEntity<String> findUserInformation(@RequestHeader(name = "Authorization", required = true) String token) {
        Jws<Claims> jws;
        try {
            jws = JwtTokenUtil.verifyToken(token);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("access denied", HttpStatus.UNAUTHORIZED);
        }
        if (jws == null) {
            System.out.println("Invalid token");
            return new ResponseEntity<>("access denied", HttpStatus.UNAUTHORIZED);
        }

        boolean tokenExpire = Validator.isTokenExpire(token);
        if (tokenExpire) {
            System.out.println("Token expire");
            return new ResponseEntity<>("access denied", HttpStatus.UNAUTHORIZED);
        }

        int user_id = (int) jws.getBody().get("user_id");
        String name = (String) jws.getBody().get("name");
        String surname = (String) jws.getBody().get("surname");
        String date_of_birth = (String) jws.getBody().get("date_of_birth");
        List<Integer> books = new ArrayList<>();

        List<Order> orders = new ArrayList<>();
        try {
            orders = orderService.getMyOrders(user_id);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        for (int i = 0; i < orders.size(); ++i) {
            List<OrderDetail> orderDetailsList = orderDetailService.getOrderDetailList(orders.get(i).getOrder_id());
            for (int j = 0; j < orderDetailsList.size(); ++j) {
                books.add(orderDetailsList.get(j).getBook_id());
            }
        }

        JSONObject response = new JSONObject();
        response.put("name", name);
        response.put("surname", surname);
        response.put("date_of_birth", date_of_birth);
        response.put("books", books);


        return new ResponseEntity<>(response.toString(), HttpStatus.OK);
    }
}
