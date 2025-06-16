package com.example.formfiller;

import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * FormPageBuilder is responsible for constructing the form page layout,
 * including drag-and-drop functionality for image uploads and field population.
 * It uses FormFields to manage the individual form fields and their properties.
 */
class FormPageBuilder {
    private final FormFields fields;
    private final ScrollPane scrollPane = new ScrollPane();

    /**
     * Constructor initializes the FormPageBuilder with the provided FormFields.
     *
     * @param fields The FormFields instance containing all the form fields to be displayed.
     */
    FormPageBuilder(FormFields fields) {
        this.fields = fields;
    }

    /**
     * Builds the form page layout with all the necessary fields and drag-and-drop functionality.
     *
     * @return A ScrollPane containing the constructed form layout so that the vbox is inside a scrollable area.
     */
    ScrollPane build() {
        VBox box = new VBox(10);
        box.setPadding(new Insets(20));

        HBox buttonRow = new HBox(10);
        buttonRow.getChildren().addAll(fields.srButton, fields.sraButton);
        buttonRow.setAlignment(Pos.CENTER);

        this.fields.addDescriptions();
        this.fields.format();
        box.getChildren().addAll(
                createDragAndDropArea(),
                buttonRow,
                fields.name, fields.surname, fields.nifNie, fields.email,
                fields.phoneNumber, fields.address, fields.city, fields.postalCode,
                fields.birthDate, fields.iban,
                fields.model, fields.capacity, fields.price, fields.imei, fields.purchaseDate,
                fields.monthlyButton, fields.yearlyButton
        );

        int startIndex = box.getChildren().indexOf(fields.birthDate);

        // Loop from birthDate onward
        for (int i = startIndex; i < box.getChildren().size(); i++) {
            Node child = box.getChildren().get(i);
            if (child instanceof TextField textField) {
                textField.setPrefHeight(40); // Bigger height
                textField.setStyle("-fx-font-size: 14px;"); // Larger text
            } else if (child instanceof ComboBox comboBox) {
                comboBox.setPrefHeight(40);
                comboBox.setStyle("-fx-font-size: 14px;");
            } else if (child instanceof DatePicker datePicker) {
                datePicker.setPrefHeight(40);
                datePicker.setStyle("-fx-font-size: 14px;");
            }
        }

        scrollPane.setContent(box);
        scrollPane.setFitToWidth(true);
        scrollPane.setPadding(new Insets(10));

        return scrollPane;
    }

    /**
     * Creates a drag-and-drop area for image uploads, allowing users to paste or drop images
     * and automatically populates the form fields with extracted data using OCR.
     *
     * @return A StackPane containing the drag-and-drop area with an image view.
     */
    private Node createDragAndDropArea() {
        ImageView imageView = new ImageView();
        imageView.setFitWidth(200);
        imageView.setFitHeight(200);
        imageView.setPreserveRatio(true);

        VBox imageBox = new VBox(imageView);
        imageBox.setAlignment(Pos.CENTER);
        TitledPane titledPane = new TitledPane("Paste Client's Sheet", imageBox);
        titledPane.setCollapsible(false);

        StackPane imagePane = new StackPane(titledPane);
        imagePane.setPadding(new Insets(10));

        Map<String, TextField> fieldMap = Map.of(
                "name", fields.name,
                "surname", fields.surname,
                "nifNie", fields.nifNie,
                "email", fields.email,
                "address", fields.address,
                "postalCode", fields.postalCode,
                "phone", fields.phoneNumber,
                "city", fields.city
        );

        imagePane.setOnDragOver(e -> {
            if (e.getGestureSource() != imagePane && e.getDragboard().hasFiles())
                e.acceptTransferModes(TransferMode.COPY);
            e.consume();
        });

        imagePane.setOnDragDropped(e -> {
            Dragboard db = e.getDragboard();
            if (db.hasFiles()) {
                File file = db.getFiles().get(0);
                try (InputStream is = new FileInputStream(file)) {
                    imageView.setImage(new Image(is));

                    ParsingUtils.performOCR(file, fieldMap);
                    scrollPane.setVvalue(1.0);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            e.setDropCompleted(true);
            e.consume();
        });

        imagePane.setOnKeyPressed(e -> {
            if (e.isMetaDown() && e.getCode().toString().equals("V")) {
                //System.out.println("V is pressed");
                Clipboard clipboard = Clipboard.getSystemClipboard();

                Image image = clipboard.getImage();
                imageView.setImage(image);

                File temp = saveClipboardImageToTempFile(image);
                if (temp != null) {
                    ParsingUtils.performOCR(temp, fieldMap);  // Your existing method to do OCR etc.
                }

            }
        });

        return imagePane;
    }

    /**
     * Saves the provided image from the clipboard to a temporary file in PNG format. Helper function.
     *
     * @param image The Image object to be saved.
     * @return A File object pointing to the temporary file containing the image, or null if an error occurs.
     */
    private File saveClipboardImageToTempFile(Image image) {
        try {
            BufferedImage bImage = SwingFXUtils.fromFXImage(image, null);
            File tempFile = File.createTempFile("clipboard_image", ".png");
            ImageIO.write(bImage, "png", tempFile);
            return tempFile;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
