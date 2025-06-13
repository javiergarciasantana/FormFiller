package com.example.formfiller;

class UserSelection {
    String alias, username, password;
    void set(UserManager.User user) {
        alias = user.getAlias();
        username = user.getUsername();
        password = user.getPassword();
    }
}