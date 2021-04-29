package com.example.bookstoreapi.services;

import com.example.bookstoreapi.models.Order;
import com.example.bookstoreapi.repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    public Order createOrder(Order order) {
        Order _oroder = orderRepository.save(order);
        return _oroder;
    }

    public List<Order> getMyOrders(int user_id) throws SQLException {
        List<Order> orders = orderRepository.findAllByUser_id(user_id);
        return orders;
    }

    public boolean deleteMyOrders(int user_id) {
        boolean flag = orderRepository.deleteAllByUser_id(user_id);
        return flag;
    }
}
