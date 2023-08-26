/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author VICTUS
 */
public class DatabaseConnection {

    public static Connection get_connection() {
        Connection connection = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/java3_project?serverTimezone=UTC", "root", "");

        } catch (ClassNotFoundException | SQLException ex) {
            System.out.println("Connection failed");
        }

        return connection;
    }
}
