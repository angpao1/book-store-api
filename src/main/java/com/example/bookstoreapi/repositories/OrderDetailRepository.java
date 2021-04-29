package com.example.bookstoreapi.repositories;

import com.example.bookstoreapi.models.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public interface OrderDetailRepository extends JpaRepository<OrderDetail, Integer> {

    default boolean deleteAllByOrder_id(int order_id) {
        String url = "jdbc:postgresql://localhost:5432/scb";
        String user = "postgres";
        String password = "postgres";

        Connection conn = null;
        Statement stmt = null;

        boolean flag = false;
        int i = 1;
        try {
            conn = DriverManager.getConnection(url, user, password);
            stmt = conn.createStatement();
            stmt = conn.createStatement();
            String q = "DELETE FROM orderdetail WHERE order_id='" + order_id + "'";
            int result = stmt.executeUpdate(q);

            if(result > 0){
                flag = true;
            }else{
                flag = false;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }// nothing we can do
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

        return flag;
    }

    default List<OrderDetail> findAllByOrder_id(int order_id) {
        String url = "jdbc:postgresql://localhost:5432/scb";
        String user = "postgres";
        String password = "postgres";

        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        OrderDetail order = null;
        List<OrderDetail> orderList = new ArrayList<>();
        try {
            conn = DriverManager.getConnection(url, user, password);
            stmt = conn.createStatement();
            String q = "SELECT * FROM orderdetail WHERE order_id='" + order_id + "'";
            rs = stmt.executeQuery(q);
            while (rs.next()) {
                order = new OrderDetail();
                order.setOrderdetail_id(Integer.parseInt(rs.getString("orderdetail_id")));
                order.setOrder_id(Integer.parseInt(rs.getString("order_id")));
                order.setBook_id(Integer.parseInt(rs.getString("book_id")));
                order.setBook_name(rs.getString("book_name"));
                order.setAuthor(rs.getString("author"));
                order.setPrice(Float.parseFloat(rs.getString("price")));

                orderList.add(order);
            }
        } catch (SQLException ex) {
            System.out.println(ex);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException ex) {
            }// nothing we can do
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException ex) {
            }// nothing we can do
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {

            }
        }

        return orderList;
    }
}
