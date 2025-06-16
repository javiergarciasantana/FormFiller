package com.example.formfiller;

import java.util.ArrayList;
import java.util.List;

/**
 * UserManager class to manage user data.
 * It allows adding users and retrieving the list of users.
 */
public class UserManager {
    private final List<User> users = new ArrayList<>();

    /**
     * Adds a new user to the list.
     *
     * @param alias    the alias of the user
     * @param username the username of the user
     * @param password the password of the user
     */
    public void addUser(String alias, String username, String password) {
        users.add(new User(alias, username, password));
    }

    /**
     * User class representing a user with an alias, username, and password.
     */
    public record User(String alias, String username, String password) {
    }
}
