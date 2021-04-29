package com.example.bookstoreapi.repositories;

import com.example.bookstoreapi.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    User findByUsernameAndPassword(String username, String password);

    User findByUsername(String username);

    default boolean deleteByUser_id(int user_id) {
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
            String q = "DELETE FROM users WHERE user_id='" + user_id + "'";
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
