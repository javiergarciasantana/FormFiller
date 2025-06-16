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

/**
 * FormUtils provides utility methods for handling form-related operations,
 * such as updating capacity options based on selected iPhone model,
 * converting date formats, creating toggle buttons with string properties,
 * and validating various input formats like IBAN, IMEI, and email.
 */
public class FormUtils {

    /**
     * Updates the capacity options in the given ComboBox based on the selected iPhone model.
     *
     * @param iphoneModelComboBox the ComboBox containing iPhone models
     * @param capacityComboBox    the ComboBox to update with capacity options
     */
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

    /**
     * Sets a custom date format for the DatePicker(non american format).
     *
     * @param date the DatePicker to set the converter for
     */
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

    /**
     * Creates a toggle button group with two buttons and a StringProperty to track the selected toggle.
     * It returns a StringProperty because it is more flexible for binding to UI components.
     *
     * @param firstButton  the first ToggleButton
     * @param toggleGroup  the ToggleGroup to which both buttons will be added
     * @param secondButton the second ToggleButton
     * @param firstText    the text for the first button
     * @param secondText   the text for the second button
     * @return a StringProperty that reflects the selected toggle's text
     */
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

    /**
     * Validates the given IBAN string with a regex.
     * @param iban the IBAN string to validate
     * @return true if the IBAN is valid, false otherwise
     */
     protected boolean isValidIban(String iban) {
        // Example IBAN validation logic (for illustrative purposes) (regular expresion done by jgsantana)
        return iban.matches("ES[0-9]{2}[0-9]{20}");
    }

    /**
     * Validates the given IMEI string with a regex.
     * @param imei the IMEI string to validate
     * @return true if the IMEI is valid, false otherwise
     */
    protected boolean isValidImei(String imei) {
        return imei.matches("[0-9]{15}");
    }

    /**
     * Validates the given email string with a regex.
     * @param email the email string to validate
     * @return true if the email is valid, false otherwise
     */
    protected boolean isValidEmail(String email) {
        return Pattern.compile("^(.+)@(\\S+)$")
                .matcher(email)
                .matches();
    }
}