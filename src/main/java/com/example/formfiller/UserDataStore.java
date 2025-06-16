package com.example.formfiller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;

/**
 * UserDataStore is responsible for managing user credentials.
 * It handles loading and saving user data to a file in the user's home directory.
 */
public class UserDataStore {
    String userHome = System.getProperty("user.home");
    String macDataDir = userHome + "/Library/Application Support/formFiller/";
    private static final String FILE_NAME = "user_data.txt";
    String filePath = macDataDir + FILE_NAME;
    private final UserManager userManager;
    private final ObservableList<UserManager.User> userList;

    /**
     * Constructor initializes the UserDataStore, creates necessary directories and files,
     * and loads existing user data from the file.
     */
    public UserDataStore() {
        userManager = new UserManager();
        userList = FXCollections.observableArrayList();

        File dir = new File(macDataDir);

        if (!dir.exists()) {
            dir.mkdirs(); // create directory if it doesn't exist
        }

        File DATA_FILE = new File(dir, FILE_NAME);

        if (!DATA_FILE.exists()) {
            try {
                DATA_FILE.createNewFile(); // create the file if it doesn't exist
            } catch (IOException e) {
                e.printStackTrace(); // handle this appropriately in real app
            }
        }
        loadData();
    }

    /**
     * Returns an observable list of user credentials.
     *
     * @return ObservableList of UserManager.User objects
     */
    public ObservableList<UserManager.User> getUserCredentials() {
        return userList;
    }

    /**
     * Adds a new user to the user manager and the user list, then saves the updated data to the file.
     *
     * @param alias    The alias for the user
     * @param username The username for the user
     * @param password The password for the user
     */
    public void addUser(String alias, String username, String password) {
        userManager.addUser(alias, username, password);
        userList.add(new UserManager.User(alias, username, password));
        saveData();
    }
    /**
     * Removes a user from the user manager and the user list, then saves the updated data to the file.
     *
     * @param user The UserManager.User object to be removed
     */
    public void removeUser(UserManager.User user) {
        userList.remove(user);
        saveData();
    }

    /**
     * Loads user data from the file into the user manager and user list.
     */
    private void loadData() {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) { // Expecting alias, username, password
                    userManager.addUser(parts[0], parts[1], parts[2]);
                    userList.add(new UserManager.User(parts[0], parts[1], parts[2]));
                }
            }
        } catch (IOException e) {
            System.err.println("Failed to load user data: " + e.getMessage());
        }
    }

    /**
     * Saves the current user data from the user list to the file.
     */
    private void saveData() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            for (UserManager.User user : userList) {
                writer.println(user.alias() + "," + user.username() + "," + user.password());
            }
        } catch (IOException e) {
            System.err.println("Failed to save user data: " + e.getMessage());
        }
    }
}
