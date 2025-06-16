package com.example.formfiller;

/**
 * UserSelection is a simple data structure to hold user credentials.
 * It provides a method to set its fields from a UserManager.User object.
 */
class UserSelection {
    String alias, username, password;
    /**
     * Default constructor initializes the fields to null.
     */
    void set(UserManager.User user) {
        alias = user.alias();
        username = user.username();
        password = user.password();
    }
}