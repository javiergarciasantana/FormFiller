package com.example.formfiller;

import javafx.beans.property.StringProperty;
import javafx.scene.control.*;
import javafx.scene.control.ToggleGroup;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

/**
 * FormFields class extends FormUtils to provide a structured way to manage form fields
 * for a user interface, including text fields, date pickers, toggle buttons, and combo boxes.
 * It also includes methods for formatting, validating, and retrieving form data.
 */
class FormFields extends FormUtils {
    TextField name = new TextField();
    TextField surname = new TextField();
    TextField nifNie = new TextField();
    TextField email = new TextField();
    TextField postalCode = new TextField();
    TextField city = new TextField();
    TextField address = new TextField();
    TextField phoneNumber = new TextField();
    TextField iban = new TextField();
    DatePicker birthDate = new DatePicker();
    DatePicker purchaseDate = new DatePicker();
    ToggleButton srButton = new ToggleButton("Sr.");
    ToggleButton sraButton = new ToggleButton("Sra.");
    ToggleButton monthlyButton = new ToggleButton("Monthly");
    ToggleButton yearlyButton = new ToggleButton("Yearly");
    ComboBox<String> model = new ComboBox<>();
    ComboBox<String> capacity = new ComboBox<>();
    TextField price = new TextField();
    TextField imei = new TextField();
    StringProperty subscriptionType;
    StringProperty treatment;

    ToggleGroup treatmentToggle = new ToggleGroup();
    ToggleGroup subscriptionToggle = new ToggleGroup();

    /**
     * Method to add descriptions (placeholders) to the form fields.
     */
    public void addDescriptions() {
        this.name.setPromptText("Name");
        this.surname.setPromptText("Surname");
        this.nifNie.setPromptText("Nif/Nie");
        this.email.setPromptText("Email");
        this.postalCode.setPromptText("Postal Code");
        this.city.setPromptText("City");
        this.address.setPromptText("Address");
        this.phoneNumber.setPromptText("Phone Number");
        this.iban.setPromptText("IBAN");
        this.birthDate.setPromptText("Birth Date");
        this.purchaseDate.setPromptText("Purchase Date");
        this.model.setPromptText("iPhone Model");
        this.capacity.setPromptText("Gb");
        this.price.setPromptText("Price(EUR)");
        this.imei.setPromptText("IMEI");
    }

    /**
     * Method to format the form fields, set up toggle buttons, and format date pickers.
     */
    public void format() {
        treatment = createFullToggle(srButton, treatmentToggle, sraButton, "Sr.", "Sra.");

        dateConverter(birthDate);

        this.model.getItems().addAll(
                "iPhone 16 Pro Max", "iPhone 16 Pro", "iPhone 16 Plus", "iPhone 16",
                "iPhone 16e", "iPhone 15 Plus", "iPhone 15", "iPhone 14 Plus", "iPhone 14",
                "iPhone 13", "iPhone SE(2022)"
        );
        this.model.setOnAction(e -> updateCapacityOptions(model, capacity));
        email.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!isValidEmail(newValue)) {
                email.setStyle("-fx-border-color: red");
            } else {
                email.setStyle("");
            }
        });
        imei.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!isValidImei(newValue)) {
                imei.setStyle("-fx-border-color: red;");
            } else {
                imei.setStyle("");
            }
        });
        iban.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!isValidIban(newValue)) {
                iban.setStyle("-fx-border-color: red;");
            } else {
                iban.setStyle("");
            }
        });


        purchaseDate.setValue(LocalDate.now());
        dateConverter(purchaseDate);

        subscriptionType = createFullToggle(monthlyButton, subscriptionToggle, yearlyButton, "Monthly", "Yearly");

    }

    /**
     * Retrieves the form data as a Map<String, String>.
     * The keys are the field names and the values are the user inputs.
     *
     * @return a Map containing the form data
     */
    public Map<String, String> getFormData() {
        Map<String, String> data = new HashMap<>();

        data.put("model", model.getValue() + " " + capacity.getValue());
        data.put("price", price.getText());
        data.put("imei", imei.getText());
        data.put("purchaseDateDay", String.format("%02d", purchaseDate.getValue().getDayOfMonth()));
        data.put("purchaseDateMonth", String.format("%02d", purchaseDate.getValue().getMonthValue()));
        data.put("purchaseDateYear", String.valueOf(purchaseDate.getValue().getYear()));
        data.put("subscriptionType", subscriptionType.get());
        data.put("treatment", treatment.get());
        data.put("name", name.getText());
        data.put("surname", surname.getText());
        data.put("address", address.getText());
        data.put("postalCode", postalCode.getText());
        data.put("city", city.getText());
        data.put("email", email.getText());
        data.put("phone", phoneNumber.getText());
        data.put("nifNie", nifNie.getText());
        data.put("birthdayDateDay", String.format("%02d", birthDate.getValue().getDayOfMonth()));
        data.put("birthdayDateMonth", String.format("%02d", birthDate.getValue().getMonthValue()));
        data.put("birthdayDateYear", String.valueOf(birthDate.getValue().getYear()));
        data.put("iban", iban.getText());

        return data;
    }

    /**
     * Validates the form fields to ensure all required fields are filled and formatted correctly.
     * It also prints the selections made by the user.
     *
     * @return true if the form is valid, false otherwise
     */
    public boolean validateForm() {
        String selections = "Model: " + model.getValue() + " " + capacity.getValue() + "Gb, " +
                "IMEI: " + imei.getText() + ", " +
                "Purchase Date: " + purchaseDate.getValue() + ", " +
                "Subscription: " + subscriptionType;
        System.out.println("Selections: " + selections);
        return model.getValue() != null &&
                capacity.getValue() != null &&
                !imei.getText().isEmpty() &&
                purchaseDate.getValue() != null &&
                subscriptionType != null &&
                !price.getText().isEmpty() &&
                !imei.getStyle().equals("-fx-border-color: red;") &&
                !iban.getText().isEmpty() &&
                !iban.getStyle().equals("-fx-border-color: red;") &&
                !name.getText().isEmpty() &&
                !surname.getText().isEmpty() &&
                !address.getText().isEmpty() &&
                !postalCode.getText().isEmpty() &&
                !city.getText().isEmpty() &&
                !email.getText().isEmpty() &&
                !phoneNumber.getText().isEmpty() &&
                !nifNie.getText().isEmpty() &&
                birthDate.getValue() != null &&
                treatment != null;
    }
}