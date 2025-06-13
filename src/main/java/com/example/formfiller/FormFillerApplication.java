package com.example.formfiller;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;


public class FormFillerApplication extends Application {

    private UserSelection userSelection = new UserSelection();
    private final FormFields formFields = new FormFields();
    private final UserDataStore userDataStore = new UserDataStore();
    private final VBox root = new VBox();
    private final Label messageLabel = new Label();


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        stage.setTitle("Form Filler");
        setupMenu(stage);
        root.getChildren().add(createUserSelectionPage());
        stage.setScene(new Scene(root, 500, 600));
        stage.show();
    }

    private void setupMenu(Stage stage) {
        MenuItem preferencesItem = new MenuItem("Preferences");
        preferencesItem.setOnAction(e -> {
            PreferencesWindow prefs = new PreferencesWindow(userDataStore);
            prefs.getStage().setOnHiding(evt -> refreshUserTiles());
            prefs.show();
        });

        MenuBar menuBar = new MenuBar(new Menu("File", null, preferencesItem));
        root.getChildren().add(0, menuBar);
    }

    private void refreshUserTiles() {
        root.getChildren().setAll(root.getChildren().get(0), createUserSelectionPage());
    }

    private VBox createUserSelectionPage() {
        VBox page = createVBox(20, Pos.CENTER, new Insets(20));
        TilePane tilePane = createTilePane();

        for (UserManager.User user : userDataStore.getUserCredentials()) {
            tilePane.getChildren().add(createUserTile(user, messageLabel));
        }

        Button startButton = new Button("Start");
        startButton.setPrefSize(150, 50);
        startButton.setStyle("-fx-background-color: #FFE135;");
        startButton.setOnAction(e -> {
            if (userSelection.username == null) {
                messageLabel.setText("Please select a user before proceeding.");
                messageLabel.setTextFill(Color.RED);
                startButton.setStyle("-fx-background-color: red;");
            } else {
                startButton.setStyle(null);
                openFormPage();
            }
        });

        tilePane.getChildren().add(messageLabel);
        page.getChildren().addAll(tilePane, startButton);
        return page;
    }

    private Button createUserTile(UserManager.User user, Label messageLabel) {
        Button btn = new Button(user.getAlias());
        btn.setPrefSize(150, 100);
        btn.setStyle("-fx-focus-color: yellow; -fx-faint-focus-color: transparent;");
        btn.setOnAction(e -> {
            userSelection.set(user);
            messageLabel.setText("");
        });
        return btn;
    }

    private void openFormPage() {
        root.getChildren().clear();

        ScrollPane formScrollPane = new FormPageBuilder(formFields).build(); // now returns ScrollPane
        VBox formContent = (VBox) formScrollPane.getContent(); // get the VBox inside

        formContent.setAlignment(Pos.CENTER);

        Button proceedButton = createButton("Proceed");
        Button backButton = createButton("Back");
        HBox buttonRow = new HBox(10); // 10px spacing between buttons
        buttonRow.getChildren().addAll(backButton, proceedButton);
        buttonRow.setAlignment(Pos.CENTER);
        VBox.setMargin(buttonRow, new Insets(40, 0, 0, 0));
        // Add buttons at the end of the form
        formContent.getChildren().addAll(buttonRow, messageLabel);


        root.getChildren().add(formScrollPane);
    }

    // UI Helpers
    private VBox createVBox(int spacing, Pos alignment, Insets padding) {
        VBox box = new VBox(spacing);
        box.setAlignment(alignment);
        box.setPadding(padding);
        return box;
    }

    private TilePane createTilePane() {
        TilePane pane = new TilePane(10, 10);
        pane.setPadding(new Insets(10));
        pane.setPrefColumns(2);
        return pane;
    }

    private Button createButton(String opt) {
        if (opt.equals("Back")) {
            Button backButton = new Button("Back");
            backButton.setPrefSize(150, 50);
            backButton.setOnAction(e -> {
                userSelection = new UserSelection();
                root.getChildren().clear();
                root.getChildren().add(createUserSelectionPage());
            });
            return backButton;
        } else {
            Button proceedButton = new Button("Proceed");
            proceedButton.setPrefSize(150, 50);
            proceedButton.setStyle("-fx-background-color: #FFE135;");
            proceedButton.setOnAction(e -> {
                // Store the selected values in class attributes

                // Validate all fields
                if (!formFields.validateForm()) {

                    // Display error message if any field is missing or empty
                    messageLabel.setText("Please fill out all fields correctly before proceeding.");
                    messageLabel.setTextFill(Color.RED);
                } else {
                    // Clear previous messages
                    messageLabel.setText("");
                    WebScript.Start(userSelection.username, userSelection.password, formFields.getFormData());

                }
            });
            return proceedButton;
        }
    }
}

