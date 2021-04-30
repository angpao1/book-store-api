package com.example.bookstoreapi.controllers;

import com.example.bookstoreapi.models.Order;
import com.example.bookstoreapi.models.OrderRequest;
import com.example.bookstoreapi.models.Price;
import com.example.bookstoreapi.models.User;
import com.example.bookstoreapi.repositories.OrderDetailRepository;
import com.example.bookstoreapi.repositories.OrderRepository;
import com.example.bookstoreapi.repositories.UserRepository;
import com.example.bookstoreapi.util.EncryptData;
import com.google.gson.Gson;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerTest {

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void canRegister() throws Exception {
        JSONObject json = new JSONObject();
        json.put("username", "john.doe");
        json.put("password", "thisismysecret");
        json.put("date_of_birth", "15/01/1985");

        // given
        given(userRepository.save(new User(0, "john.doe", "thisismysecret", "", "", "15/01/1985", "")))
                .willReturn(new User());

        // when
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<String>(json.toString() ,headers);
        ResponseEntity<String> response = restTemplate.postForEntity("/users", entity, String.class);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void notSendUsernameToRegister() throws Exception {
        JSONObject json = new JSONObject();
        json.put("username", "");
        json.put("password", "thisismysecret");
        json.put("date_of_birth", "15/01/1985");

        // given
        given(userRepository.save(new User(0, "john.doe", "thisismysecret", "", "", "15/01/1985", "")))
                .willReturn(new User());

        // when
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<String>(json.toString() ,headers);
        ResponseEntity<String> response = restTemplate.postForEntity("/users", entity, String.class);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void notSendPasswordToRegister() throws Exception {
        JSONObject json = new JSONObject();
        json.put("username", "john.doe");
        json.put("password", "");
        json.put("date_of_birth", "15/01/1985");

        // given
        given(userRepository.save(new User(0, "john.doe", "thisismysecret", "", "", "15/01/1985", "")))
                .willReturn(new User());

        // when
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<String>(json.toString() ,headers);
        ResponseEntity<String> response = restTemplate.postForEntity("/users", entity, String.class);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void notSendDateOfBirthToRegister() throws Exception {
        JSONObject json = new JSONObject();
        json.put("username", "john.doe");
        json.put("password", "thisismysecret");
        json.put("date_of_birth", "");

        // given
        given(userRepository.save(new User(0, "john.doe", "thisismysecret", "", "", "15/01/1985", "")))
                .willReturn(new User());

        // when
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<String>(json.toString() ,headers);
        ResponseEntity<String> response = restTemplate.postForEntity("/users", entity, String.class);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void canLogin() throws Exception {
        JSONObject json = new JSONObject();
        json.put("username", "john.doe");
        json.put("password", "thisismysecret");

        String passwordHash = EncryptData.toHexString(EncryptData.getSHA("thisismysecret"));

        // given
        given(userRepository.findByUsernameAndPassword("john.doe", passwordHash))
                .willReturn(new User());

        // when
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<String>(json.toString() ,headers);
        ResponseEntity<String> response = restTemplate.postForEntity("/login", entity, String.class);

        // then
        assertThat(response.getHeaders().containsKey("token")).isTrue();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void loginInvalidPassword() throws Exception {
        JSONObject json = new JSONObject();
        json.put("username", "john.doe");
        json.put("password", "thisismysecret123");

        String passwordHash = EncryptData.toHexString(EncryptData.getSHA("thisismysecret"));

        // given
        given(userRepository.findByUsernameAndPassword("john.doe", passwordHash))
                .willReturn(new User());

        // when
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<String>(json.toString() ,headers);
        ResponseEntity<String> response = restTemplate.postForEntity("/login", entity, String.class);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    public void canInsertOrder() throws Exception {
        JSONObject json = new JSONObject();
        json.put("username", "john.doe");
        json.put("password", "thisismysecret");

        String passwordHash = EncryptData.toHexString(EncryptData.getSHA("thisismysecret"));

        given(userRepository.findByUsernameAndPassword("john.doe", passwordHash))
                .willReturn(new User(1, "john.doe", "", "john", "doe", "15/01/1985", ""));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<String>(json.toString() ,headers);
        ResponseEntity<String> response = restTemplate.postForEntity("/login", entity, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        headers.clear();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", response.getHeaders().get("token").get(0));
        System.out.println(response.getHeaders().get("token").get(0));
        System.out.println(headers);

        List<Integer> orders = new ArrayList<>();
        orders.add(1);
        orders.add(4);

        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setOrders(orders);

        HttpEntity<OrderRequest> entity2 = new HttpEntity<>(orderRequest, headers);
        response = restTemplate.postForEntity("/users/orders", entity2, String.class);
        System.out.println("1---------"+response.getBody());

        Gson g = new Gson();
        Price price = g.fromJson(response.getBody(), Price.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(price.getPrice()).isNotEqualTo(0);

    }

    @Test
    public void notSendOrderToInsertOrder() throws Exception{
        JSONObject json = new JSONObject();
        json.put("username", "john.doe");
        json.put("password", "thisismysecret");

        String passwordHash = EncryptData.toHexString(EncryptData.getSHA("thisismysecret"));

        given(userRepository.findByUsernameAndPassword("john.doe", passwordHash))
                .willReturn(new User(1, "john.doe", "", "john", "doe", "15/01/1985", ""));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<String>(json.toString() ,headers);
        ResponseEntity<String> response = restTemplate.postForEntity("/login", entity, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        headers.clear();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", response.getHeaders().get("token").get(0));
        System.out.println(response.getHeaders().get("token").get(0));
        System.out.println(headers);

        List<Integer> orders = new ArrayList<>();

        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setOrders(orders);

        HttpEntity<OrderRequest> entity2 = new HttpEntity<>(orderRequest, headers);
        System.out.println(entity2);
        response = restTemplate.postForEntity("/users/orders", entity2, String.class);
        System.out.println("1---------"+response.getBody());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isEqualTo("orders empty");
    }

    @Test
    public void orderBooksAndSeeInformation() throws Exception {
        JSONObject json = new JSONObject();
        json.put("username", "john.doe");
        json.put("password", "thisismysecret");

        String passwordHash = EncryptData.toHexString(EncryptData.getSHA("thisismysecret"));

        given(userRepository.findByUsernameAndPassword("john.doe", passwordHash))
                .willReturn(new User(1, "john.doe", "", "john", "doe", "15/01/1985", ""));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<String>(json.toString() ,headers);
        ResponseEntity<String> response = restTemplate.postForEntity("/login", entity, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        headers.clear();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", response.getHeaders().get("token").get(0));
        System.out.println(response.getHeaders().get("token").get(0));
        System.out.println(headers);
        List<Integer> orders = new ArrayList<>();
        orders.add(1);
        orders.add(4);
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setOrders(orders);
        HttpEntity<OrderRequest> entity2 = new HttpEntity<>(orderRequest, headers);
        response = restTemplate.postForEntity("/users/orders", entity2, String.class);
        System.out.println("1---------"+response.getBody());

        Gson g = new Gson();
        Price price = g.fromJson(response.getBody(), Price.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(price.getPrice()).isNotEqualTo(0);

        HttpEntity<String> entity3 = new HttpEntity<>(headers);
        response = restTemplate.exchange("/users", HttpMethod.GET, entity3, String.class);
        System.out.println("1---------"+response.getBody());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void deleteUser() throws Exception {
        JSONObject json = new JSONObject();
        json.put("username", "john.doe");
        json.put("password", "thisismysecret");

        String passwordHash = EncryptData.toHexString(EncryptData.getSHA("thisismysecret"));

        given(userRepository.findByUsernameAndPassword("john.doe", passwordHash))
                .willReturn(new User(1, "john.doe", "", "john", "doe", "15/01/1985", ""));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<String>(json.toString() ,headers);
        ResponseEntity<String> response = restTemplate.postForEntity("/login", entity, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        headers.clear();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", response.getHeaders().get("token").get(0));
        System.out.println(response.getHeaders().get("token").get(0));

        HttpEntity<String> entity3 = new HttpEntity<>(headers);
        response = restTemplate.exchange("/users", HttpMethod.DELETE, entity3, String.class);
        System.out.println("1---------"+response.getBody());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

}
