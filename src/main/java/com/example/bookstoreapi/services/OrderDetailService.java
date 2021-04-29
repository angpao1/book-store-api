package com.example.bookstoreapi.services;

import com.example.bookstoreapi.models.OrderDetail;
import com.example.bookstoreapi.repositories.OrderDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderDetailService {

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    public List<OrderDetail> createOrderDetail(List<OrderDetail> bookList) {
        List<OrderDetail> orderDetail = orderDetailRepository.saveAll(bookList);
        return orderDetail;
    }

    public boolean deleteMyOrder(int order_id) {
        boolean flag = orderDetailRepository.deleteAllByOrder_id(order_id);
        return flag;
    }

    public List<OrderDetail> getOrderDetailList(int order_id) {
        List<OrderDetail> orderDetailsList = orderDetailRepository.findAllByOrder_id(order_id);
        return orderDetailsList;
    }

}
