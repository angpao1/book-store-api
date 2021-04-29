package com.example.bookstoreapi.repositories;

import com.example.bookstoreapi.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Integer> {

    default List<Order> findAllByUser_id(int user_id) throws SQLException {
        String url = "jdbc:postgresql://localhost:5432/scb";
        String user = "postgres";
        String password = "postgres";

        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        Order order = null;
        List<Order> orderList = new ArrayList<>();
        try {
            conn = DriverManager.getConnection(url, user, password);
            stmt = conn.createStatement();
            String q = "SELECT * FROM orders WHERE user_id='" + user_id + "'";
            rs = stmt.executeQuery(q);
            while (rs.next()) {
                order = new Order();
                order.setOrder_id(Integer.parseInt(rs.getString("order_id")));
                order.setUser_id(Integer.parseInt(rs.getString("user_id")));
                order.setCreateAt(rs.getString("create_at"));
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

    default boolean deleteAllByUser_id(int user_id) {
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
            String q = "DELETE FROM orders WHERE user_id='" + user_id + "'";
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
}
