package com.example.formfiller;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class PreferencesWindow {
    private final UserDataStore userDataStore;
    private final Stage stage;

    public PreferencesWindow(UserDataStore userDataStore) {
        this.userDataStore = userDataStore;
        this.stage = new Stage();
        setupUI();
    }

    private void setupUI() {
        stage.setTitle("Preferences");
        stage.initModality(Modality.APPLICATION_MODAL);

        VBox preferencesLayout = new VBox(10);
        preferencesLayout.setPadding(new Insets(20));
        preferencesLayout.setAlignment(Pos.CENTER);

        TableView<UserManager.User> userTable = new TableView<>();
        TableColumn<UserManager.User, String> aliasColumn = new TableColumn<>("Alias");
        aliasColumn.setCellValueFactory(cellData -> javafx.beans.binding.Bindings.createStringBinding(() -> cellData.getValue().alias()));
        TableColumn<UserManager.User, String> usernameColumn = new TableColumn<>("Username");
        usernameColumn.setCellValueFactory(cellData -> javafx.beans.binding.Bindings.createStringBinding(() -> cellData.getValue().username()));
        TableColumn<UserManager.User, String> passwordColumn = new TableColumn<>("Password");
        passwordColumn.setCellValueFactory(cellData -> javafx.beans.binding.Bindings.createStringBinding(() -> cellData.getValue().password()));
        userTable.getColumns().addAll(aliasColumn, usernameColumn, passwordColumn);
        userTable.setItems(userDataStore.getUserCredentials());

        TextField aliasField = new TextField();
        aliasField.setPromptText("Alias");
        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");
        TextField passwordField = new TextField();
        passwordField.setPromptText("Password");

        Button addButton = new Button("+");
        addButton.setOnAction(e -> {
            String alias = aliasField.getText();
            String username = usernameField.getText();
            String password = passwordField.getText();
            if (!alias.isEmpty() && !username.isEmpty() && !password.isEmpty()) {
                userDataStore.addUser(alias, username, password);
                aliasField.clear();
                usernameField.clear();
                passwordField.clear();
            }
        });

        Button removeButton = new Button("-");
        removeButton.setOnAction(e -> {
            UserManager.User selectedUser = userTable.getSelectionModel().getSelectedItem();
            if (selectedUser != null) {
                userDataStore.removeUser(selectedUser);
            }
        });
        Button backButton = new Button("Back");
        backButton.setOnAction(e -> stage.close()); // This closes the current window

        // Insert the button inside an HBox to align it right
        HBox bottomLayout = new HBox(backButton);
        bottomLayout.setAlignment(Pos.CENTER_RIGHT);
        bottomLayout.setPadding(new Insets(10, 0, 0, 0)); // A bit of top padding

        HBox inputLayout = new HBox(10, aliasField, usernameField, passwordField, addButton, removeButton);
        inputLayout.setAlignment(Pos.CENTER);

        //  We add the bottomLayout to the main container
        preferencesLayout.getChildren().addAll(new Label("Manage Users"), userTable, inputLayout, bottomLayout);

        Scene scene = new Scene(preferencesLayout, 600, 400);
        stage.setScene(scene);
    }

    public void show() {
        stage.showAndWait();
    }

    public Stage getStage() {
        return stage;
    }
}
