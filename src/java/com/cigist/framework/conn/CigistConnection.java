/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cigist.framework.conn;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author user
 */
public class CigistConnection {

    private Connection conn = null;

    public Connection getConnection() throws FileNotFoundException, IOException {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Where is your MySQL JDBC Driver?");
            e.printStackTrace();
            return conn;
        }
        try {
            conn = DriverManager.getConnection("jdbc:mysql://202.158.20.139:3306/xpaydb?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", "metapro", "metapro");
        } catch (SQLException e) {
            System.out.println(e);
            System.out.println("Failed to make connection!");
            return conn;
        }
        return conn;
    }

    public void closeConnection() throws SQLException {
        //close DB connection here
        conn.close();
    }
}
