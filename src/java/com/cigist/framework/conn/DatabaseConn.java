package com.cigist.framework.conn;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author irwan cigist /irwancigist@gmail.com
 */
public class DatabaseConn {

 
    Connection conn = null;
    Driver driver = null;

    public DatabaseConn() {
    }

    public Connection getConnection() {
        try {
            ResourceBundle prop = ResourceBundle.getBundle("config");
            String sDriverClass = prop.getString("driver");
            String sUrl = prop.getString("url");
            String sUsername = prop.getString("username");
            String sPassword = prop.getString("password");
            Class.forName(sDriverClass);
            conn = DriverManager.getConnection(sUrl, sUsername, sPassword);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DatabaseConn.class.getName()).log(Level.SEVERE, null, ex);
        }
        return conn;
    }

    public void closeConnection() throws SQLException {
        //close DB connection here
        conn.close();
    }

}
