package server.models;

import java.util.ArrayList;

public class User {

    public static ArrayList<User> users = new ArrayList<>();

    public static int nextId() {
        int id = 0;
        for (User u: users) {
            if (u.getId() > id) {
                id = u.getId();
            }
        }
        return id + 1;
    }

    private int id;
    private String username;
    private String password;
    private String sessionToken;

    public User(int id, String username, String password, String sessionToken) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.sessionToken = sessionToken;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSessionToken() {
        return sessionToken;
    }

    public void setSessionToken(String sessionToken) {
        this.sessionToken = sessionToken;
    }
}