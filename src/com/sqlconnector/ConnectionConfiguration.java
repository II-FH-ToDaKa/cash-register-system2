package com.sqlconnector;

import java.sql.*;

import com.sun.xml.internal.ws.api.message.ExceptionHasMessage;

/**
 * Created by Daniel on 10.05.2016.
 */
public class ConnectionConfiguration {
    public static Connection getConnection()
    {
        Connection con=null;
        try{
            Class.forName("com.mysql.jdbc.Driver");
            con= DriverManager.getConnection("jdbc:mysql://localhost:3306/cash-register-system","employee","GUMVNcwufZMYpNpT");

        }
        catch (Exception e)
        {
            e.printStackTrace();

        }


        return con;
    }
}
