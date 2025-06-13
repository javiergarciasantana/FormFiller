package com.example.formfiller;

import java.util.ArrayList;
import java.util.List;

public class UserManager {
    private final List<User> users = new ArrayList<>();

    public void addUser(String alias, String username, String password) {
        users.add(new User(alias, username, password));
    }

    public List<User> getUsers() {
        return users;
    }

    // Simple User class
    public static class User {
        private final String alias;
        private final String username;
        private final String password;

        public User(String alias, String username, String password) {
            this.alias = alias;
            this.username = username;
            this.password = password;
        }

        public String getAlias() {
            return alias;
        }

        public String getUsername() {
            return username;
        }

        public String getPassword() {
            return password;
        }
    }
}
