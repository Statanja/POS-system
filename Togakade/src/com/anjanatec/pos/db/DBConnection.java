package com.anjanatec.pos.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection{

    //1 rule
    private static DBConnection dbConnection;

    private Connection connection;
    //2 rule
    private DBConnection() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/Thogakade","root","1234");
    }
    //3 rule
    public static DBConnection getInstance() throws SQLException, ClassNotFoundException {
            if(dbConnection==null){
                dbConnection = new DBConnection();
            }
            return dbConnection;
    }

    public Connection getConnection(){
        return connection;
    }

}
