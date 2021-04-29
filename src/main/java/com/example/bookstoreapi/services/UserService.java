package com.example.bookstoreapi.services;

import com.example.bookstoreapi.models.User;
import com.example.bookstoreapi.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    public User createUser(User user) {
        User _user = userRepository.save(user);
        return _user;
    }

    public User findUser(String username, String password) {
       User _user = userRepository.findByUsernameAndPassword(username, password);
       return _user;
    }

    public boolean checkDuplicateUsername(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            return false;
        } else {
            return true;
        }
    }

    public boolean deleteUser(int user_id) {
        boolean flag = userRepository.deleteByUser_id(user_id);
        return flag;
    }

}
