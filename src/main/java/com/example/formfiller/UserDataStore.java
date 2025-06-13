package com.example.formfiller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;

public class UserDataStore {
    String userHome = System.getProperty("user.home");
    String macDataDir = userHome + "/Library/Application Support/formFiller/";
    private static final String FILE_NAME = "user_data.txt";
    String filePath = macDataDir + FILE_NAME;
    private final UserManager userManager;
    private final ObservableList<UserManager.User> userList;

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

    public ObservableList<UserManager.User> getUserCredentials() {
        return userList;
    }

    public void addUser(String alias, String username, String password) {
        userManager.addUser(alias, username, password);
        userList.add(new UserManager.User(alias, username, password));
        saveData();
    }

    public void removeUser(UserManager.User user) {
        userList.remove(user);
        saveData();
    }

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

    private void saveData() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            for (UserManager.User user : userList) {
                writer.println(user.getAlias() + "," + user.getUsername() + "," + user.getPassword());
            }
        } catch (IOException e) {
            System.err.println("Failed to save user data: " + e.getMessage());
        }
    }
}
