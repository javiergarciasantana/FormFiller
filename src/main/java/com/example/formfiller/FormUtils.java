package com.example.formfiller;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;

public class FormUtils {
    protected void updateCapacityOptions(ComboBox<String> iphoneModelComboBox, ComboBox<String> capacityComboBox) {
        capacityComboBox.getItems().clear(); // Clear existing items
        String selectedModel = iphoneModelComboBox.getValue();

        switch (selectedModel) {
            case "iPhone SE(2022)":
                capacityComboBox.getItems().addAll("64GB", "128GB", "256GB");
                break;
            case "iPhone 13", "iPhone 14", "iPhone 14 Plus", "iPhone 15", "iPhone 15 Plus", "iPhone 16e", "iPhone 16",
                 "iPhone 16 Plus":
                capacityComboBox.getItems().addAll("128GB", "256GB", "512GB");
                break;
            case "iPhone 16 Pro":
                capacityComboBox.getItems().addAll("128GB", "256GB", "512GB", "1TB");
                break;
            case "iPhone 16 Pro Max":
                capacityComboBox.getItems().addAll("256GB", "512GB", "1TB");
                break;
        }
    }

    protected void dateConverter(DatePicker date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        date.setConverter(new javafx.util.StringConverter<LocalDate>() {
            @Override
            public String toString(LocalDate date) {
                if (date != null) {
                    return date.format(formatter);
                } else {
                    return "";
                }
            }
            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    return LocalDate.parse(string, formatter);
                } else {
                    return null;
                }
            }
        });
    }
    protected StringProperty createFullToggle(ToggleButton firstButton, ToggleGroup toggleGroup, ToggleButton secondButton, String firstText, String secondText) {
        StringProperty selection = new SimpleStringProperty();

        firstButton.setToggleGroup(toggleGroup);
        secondButton.setToggleGroup(toggleGroup);

        toggleGroup.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
            if (newToggle == firstButton) {
                selection.set(firstText);
            } else if (newToggle == secondButton) {
                selection.set(secondText);
            } else {
                selection.set(null); // No toggle selected
            }
        });
        return selection;
    }
    // Method to validate Spanish IBAN format
     protected boolean isValidIban(String iban) {
        // Example IBAN validation logic (for illustrative purposes) (regular expresion done by jgsantana)
        return iban.matches("ES[0-9]{2}[0-9]{20}");
    }

    protected boolean isValidImei(String imei) {
        return imei.matches("[0-9]{15}");
    }
    protected boolean isValidEmail(String email) {
        return Pattern.compile("^(.+)@(\\S+)$")
                .matcher(email)
                .matches();
    }
}