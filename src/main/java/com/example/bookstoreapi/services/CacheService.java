package com.example.bookstoreapi.services;

import com.example.bookstoreapi.models.Books;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import redis.clients.jedis.Jedis;

public class CacheService {

    private String host = "localhost";
    private int port = 6379;

    public String save(String key, Books books) throws JsonProcessingException {
        Jedis jedis = new Jedis(host, port);

        ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(books);
        String status = jedis.set(key, jsonString);
        return status;
    }

    public String getData(String key){
        Jedis jedis = new Jedis(host, port);
        String value = jedis.get(key);
        return value;
    }

    public String saveToken(String key) {
        Jedis jedis = new Jedis(host, port);
        String status = jedis.set(key, key);
        return status;
    }

    public void deleteKey(String key) {
        Jedis jedis = new Jedis(host, port);
        jedis.del(key);
    }
}
