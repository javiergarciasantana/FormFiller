package com.example.formfiller;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

import javafx.scene.control.*;
import java.io.File;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ParsingUtils provides utility methods for parsing and extracting data from images using OCR,
 * and filling form fields with the extracted data.
 */
public class ParsingUtils {

    /**
     * Retrieves the application directory path, which is useful for locating resources like Tesseract data files.
     *
     * @return the absolute path to the application directory, or null if an error occurs
     */
    public static String getAppDir() {
        try {
            // Points to either the JAR or the classes/ folder
            Path path = Paths.get(FormFillerApplication.class
                    .getProtectionDomain()
                    .getCodeSource()
                    .getLocation()
                    .toURI());

            // If running from JAR inside .app, go up 3 levels to FormFiller.app
            File file = path.toFile();
            if (file.getName().endsWith(".jar")) {
                return file.getParentFile() // MacOS/
                        .getParentFile()    // Contents/
                        .getParent();       // FormFiller.app
            } else {
                // Probably running from classes/ in dev
                return file.getAbsolutePath();
            }

        } catch (URISyntaxException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Performs Optical Character Recognition (OCR) on the provided image file using Tesseract,
     * and fills the specified form fields with the extracted data.
     *
     * @param file     the image file to perform OCR on
     * @param fieldMap a map of field names to TextField objects where extracted data will be set
     */
    public static void performOCR(File file, Map<String, TextField> fieldMap) {
        // Ensure the correct path to the Tesseract library is set
        String appDir = getAppDir();


        // Set the correct Tesseract data path
        System.setProperty("jna.library.path", appDir + "/Contents/Resources/tesseract/");
        System.setProperty("java.library.path", appDir + "/Contents/Resources/tesseract/");

        ITesseract tesseract = new Tesseract();

        tesseract.setDatapath(appDir + "/Contents/Resources/tesseract/tessdata/");
        tesseract.setLanguage("spa");  // Set the language to Spanish

        try {
            String result = tesseract.doOCR(file);
            System.out.println("Extracted Text: \n" + result);
            parseExtractedData(result, fieldMap);
        } catch (TesseractException e) {
            e.printStackTrace();
        }
    }

    /**
     * Parses the extracted text from OCR and fills the corresponding fields in the provided map.
     *
     * @param text   the extracted text from the OCR process
     * @param fields a map of field names to TextField objects where extracted data will be set
     */
    private static void parseExtractedData(String text, Map<String, TextField> fields) {
        Map<String, String> patterns = Map.of(
                "name", "(?i)(nombre|nomve)\\s*[:\\-]?\\s*(\\w+)",
                "nifNie", "(?i)(número)\\s*[:\\-]?\\s*(\\d{7,8}[A-Z])\\b",
                "surname", "(melios\\s*-\\s*)([A-Za-zÁÉÍÓÚáéíóúÑñ]+\\s+[A-Za-zÁÉÍÓÚáéíóúÑñ]+)",
                "email", "emat \\] (.*?)(\\b\\w*0\\w*\\.\\w+)",
                "address", "(vomiciio\\s+)([A-Za-zÁÉÍÓÚáéíóúÑñ\\s]+,\\s*\\d+)",
                "postalCode", "(c)\\s+(\\d{5})",
                "phone", "(racno)\\s+(\\d{9})"
        );

        for (var entry : patterns.entrySet()) {
            String key = entry.getKey();
            String pattern = entry.getValue();
            String value = matchPattern(text, pattern);

            TextField field = fields.get(key);
            if (field != null) {
                field.setText(value != null ? value : "");
            }
        }

        fields.get("city").setText("Santa Cruz de Tenerife");
        // Populate the fields in the UI
        //System.out.println(name + " " + surname + " " + nifNie + " " + email + " " + phone + " " + postalCode + " " + address);
    }

    /**
     * Matches a given pattern against the provided text and returns the matched group.
     *
     * @param text    the text to search within
     * @param pattern the regex pattern to match
     * @return the matched group if found, or null if no match is found
     */
    private static String matchPattern(String text, String pattern) {
        Pattern regex = Pattern.compile(pattern);
        Matcher matcher = regex.matcher(text);
        if (matcher.find()) {
            return matcher.group(2); // Return the matched group
        } else {
            System.out.println("Pattern did not match the input string.");
        }
        return null;
    }
}
