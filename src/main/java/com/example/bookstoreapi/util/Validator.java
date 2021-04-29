package com.example.bookstoreapi.util;

import com.example.bookstoreapi.services.CacheService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Validator {

    public static boolean isValidDateFormat(String format, String value) {
        Date date = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            date = sdf.parse(value);
            if (!value.equals(sdf.format(date))) {
                date = null;
            }
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        return date != null;
    }

    public static boolean isTokenExpire(String token) {
        CacheService cacheService = new CacheService();
        // if not found data will return null
        String expire = cacheService.getData(token);
        if(expire == null) {
            return true;
        } else {
            return false;
        }
    }
}
