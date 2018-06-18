package server.models.services;

import server.Console;
import server.DatabaseConnection;
import server.models.Message;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class MessageService {

    public static String selectAllInto(List<Message> targetList) {
        targetList.clear();
        try {
            PreparedStatement statement = DatabaseConnection.newStatement(
                    "SELECT Id, Text, PostDate, Author FROM Messages"
            );
            if (statement != null) {
                ResultSet results = statement.executeQuery();
                if (results != null) {
                    while (results.next()) {
                        targetList.add(new Message(results.getInt("Id"), results.getString("Text"), results.getString("PostDate"), results.getString("Author")));


                    }
                }
            }
        } catch (SQLException resultsException) {
            String error = "Database error - can't select all from 'Messages' table: " + resultsException.getMessage();

            Console.log(error);
            return error;
        }
        return "OK";
    }

    public static String insert(Message itemToSave) {
        try {
            PreparedStatement statement = DatabaseConnection.newStatement(
                    "INSERT INTO Messages (Id, Text, PostDate, Author) VALUES (?, ?, ?, ?)"
            );
            statement.setInt(1, itemToSave.getId());
            statement.setString(2, itemToSave.getText());
            statement.setString(3, itemToSave.getPostDate());
            statement.setString(4, itemToSave.getAuthor());
            statement.executeUpdate();
            return "OK";
        } catch (SQLException resultsException) {
            String error = "Database error - can't insert into 'Messages' table: " + resultsException.getMessage();

            Console.log(error);
            return error;
        }
    }

    public static String update(Message itemToSave) {
        try {
            PreparedStatement statement = DatabaseConnection.newStatement(
                    "UPDATE Messages SET Text = ?, PostDate = ?, Author = ? WHERE Id = ?"
            );
            statement.setString(1, itemToSave.getText());
            statement.setString(2, itemToSave.getPostDate());
            statement.setString(3, itemToSave.getAuthor());
            statement.setInt(4, itemToSave.getId());
            statement.executeUpdate();
            return "OK";
        } catch (SQLException resultsException) {
            String error = "Database error - can't update 'Messages' table: " + resultsException.getMessage();
            Console.log(error);
            return error;
        }
    }

    public static Message selectById(int id) {
        Message result = null;
        try {
            PreparedStatement statement = DatabaseConnection.newStatement(
                    "SELECT Id, Text, PostDate, Author FROM Messages WHERE Id = ?"
            );
            if (statement != null) {
                statement.setInt(1, id);
                ResultSet results = statement.executeQuery();
                if (results != null && results.next()) {
                    result = new Message(results.getInt("Id"), results.getString("Text"),
                            results.getString("PostDate"), results.getString("Author"));
                }
            }
        } catch (SQLException resultsException) {
            String error = "Database error - can't select by id from 'Messages' table: " + resultsException.getMessage();
            Console.log(error);
        }
        return result;
    }

    public static String deleteById(int id) {
        try {
            PreparedStatement statement = DatabaseConnection.newStatement(
                    "DELETE FROM Messages WHERE Id = ?"
            );
            statement.setInt(1, id);
            statement.executeUpdate();
            return "OK";
        } catch (SQLException resultsException) {
            String error = "Database error - can't delete by id from 'Messages' table: " + resultsException.getMessage();
            Console.log(error);
            return error;
        }
    }

}
