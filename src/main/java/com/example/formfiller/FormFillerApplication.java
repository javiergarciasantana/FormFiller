// Author: Javier Garcia Santana
// Description: This JavaFX application allows users to select their credentials and fill out a form using
// OCR and selenium for web automation.
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

/**
 * The FormFillerApplication class is the main entry point for the JavaFX application.
 * It allows users to select their credentials and fill out a form using OCR and selenium for web automation.
 */
public class FormFillerApplication extends Application {

    private UserSelection userSelection = new UserSelection();
    private final FormFields formFields = new FormFields();
    private final UserDataStore userDataStore = new UserDataStore();
    private final VBox root = new VBox();
    private final Label messageLabel = new Label();
    private final  Button startButton = new Button("Start");

    /**
     * The main entry point of the application.
     * Launches the JavaFX application with the given command-line arguments.
     *
     * @param args the command-line arguments passed to the program
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * The start method is called after the application has been launched.
     * Its function is to set up the primary stage and initialize the user interface(tiles).
     *
     * @param stage the primary stage for this application, onto which the application scene can be set
     */
    @Override
    public void start(Stage stage) {
        stage.setTitle("Form Filler");
        setupMenu();
        root.getChildren().add(createUserSelectionPage());
        stage.setScene(new Scene(root, 512, 600));
        stage.show();
    }

    /**
     * Sets up the menu bar with a "Preferences" item.
     * When clicked, it opens a PreferencesWindow where users can manage their preferences.
     */
    private void setupMenu() {
        MenuItem preferencesItem = new MenuItem("Preferences");
        preferencesItem.setOnAction(e -> {
            PreferencesWindow prefs = new PreferencesWindow(userDataStore);
            prefs.getStage().setOnHiding(evt -> refreshUserTiles());
            prefs.show();
        });

        MenuBar menuBar = new MenuBar(new Menu("File", null, preferencesItem));
        root.getChildren().add(0, menuBar);
    }

    /**
     * Refreshes the user tiles displayed in the user selection page.
     * This method clears the current user tiles and re-populates them based on the current user data store.
     */
    private void refreshUserTiles() {
        root.getChildren().setAll(root.getChildren().get(0), createUserSelectionPage());
    }

    /**
     * Creates the user selection page where users can select their credentials.
     * It displays a list of user tiles and a button to proceed to the form page.
     *
     * @return a VBox containing the user selection UI
     */
    private VBox createUserSelectionPage() {
        VBox page = createVBox(20, Pos.CENTER, new Insets(20));
        TilePane tilePane = createTilePane();

        for (UserManager.User user : userDataStore.getUserCredentials()) {
            tilePane.getChildren().add(createUserTile(user));
        }

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

    /**
     * Creates a user tile button for the given user.
     * The button displays the user's alias and sets up an action to select the user when clicked.
     *
     * @param user the user for whom the tile is created
     * @return a Button representing the user tile
     */
    private Button createUserTile(UserManager.User user) {
        Button btn = new Button(user.alias());
        btn.setPrefSize(150, 100);
        btn.setStyle("-fx-focus-color: yellow; -fx-faint-focus-color: transparent;");
        btn.setOnAction(e -> {
            userSelection.set(user);
            messageLabel.setText("");
            startButton.setStyle("-fx-background-color: #FFE135;");

        });
        return btn;
    }

    /**
     * Opens the form page where users can fill out their details.
     * It clears the current content and adds the form fields to the root layout.
     */
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

    /**
     * Creates a VBox with specified spacing, alignment, and padding.
     *
     * @param spacing   the spacing between children in the VBox
     * @param alignment the alignment of the children within the VBox
     * @param padding   the padding around the VBox
     * @return a VBox with the specified properties
     */
    private VBox createVBox(int spacing, Pos alignment, Insets padding) {
        VBox box = new VBox(spacing);
        box.setAlignment(alignment);
        box.setPadding(padding);
        return box;
    }

    /**
     * Creates a TilePane with specified spacing and padding.
     *
     * @return a TilePane with the specified properties
     */
    private TilePane createTilePane() {
        TilePane pane = new TilePane(10, 10);
        pane.setPadding(new Insets(10));
        pane.setPrefColumns(2);
        return pane;
    }

    /**
     * Creates a button for either proceeding to the next step or going back to the user selection page.
     * The button's action is set based on the option provided.
     *
     * @param opt the option for the button ("Proceed" or "Back")
     * @return a Button configured with the specified action
     */
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

