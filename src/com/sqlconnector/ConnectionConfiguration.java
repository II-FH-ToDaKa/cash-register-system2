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
    public boolean Update(String SQLQuery)
    {
        Connection connection=null;

        boolean resume=false;
        try{
            connection= ConnectionConfiguration.getConnection();
            if(connection!=null)
            {
                Statement stmt;

                stmt = connection.createStatement();
                stmt.executeUpdate(SQLQuery);
                connection.close();
                resume=true;
                stmt.close();

            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return  resume;
    }
    public String OnResult(String SQLQuery)
    {
        Connection connection=null;
        String ReturnResult="-1";
        try{
            connection= ConnectionConfiguration.getConnection();
            if(connection!=null)
            {
                Statement stmt;

                stmt = connection.createStatement();

                ResultSet Result= stmt.executeQuery(SQLQuery);

                if(Result.next())
                {
                    ReturnResult= Result.getString(Result.getMetaData().getColumnName(1));
                }
                else
                {
                    ReturnResult="-1";
                }
                stmt.close();

            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return ReturnResult;

    }
}