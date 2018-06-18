package server;

import java.sql.*;

public class DatabaseConnection {

    private static Connection connection = null;

    public static void open(String dbFile)
    {
        try
        {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:resources/" + dbFile);
            System.out.println("Database connection successfully established.");
        }
        catch (ClassNotFoundException exception)
        {
            System.out.println("Class not found exception: " + exception.getMessage());
        }
        catch (SQLException exception)
        {
            System.out.println("Database connection error: " + exception.getMessage());
        }

    }


    public static PreparedStatement newStatement(String query) throws SQLException {
        return connection.prepareStatement(query);
    }

    public static void close()
    {
        System.out.println("Disconnecting from database.");
        try {
            if (connection != null) connection.close();
        }
        catch (SQLException exception)
        {
            System.out.println("Database disconnection error: " + exception.getMessage());
        }
    }

}


