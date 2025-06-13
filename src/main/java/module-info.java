module com.example.formfiller {
    // Export the main package to make it accessible to the JavaFX framework
    exports com.example.formfiller;

    requires io.opentelemetry.sdk.trace;
    requires io.opentelemetry.api;
    requires io.opentelemetry.context;

    // JavaFX modules needed for the application
    requires javafx.controls;
    requires javafx.fxml;

    // Selenium WebDriver modules for automation

    requires org.seleniumhq.selenium.support;

    // Required for using Java's AWT Clipboard (optional based on usage)
    requires org.seleniumhq.selenium.os;
    requires junit;
    requires org.seleniumhq.selenium.chrome_driver;
    requires tess4j;
    requires org.seleniumhq.selenium.safari_driver;
    requires dev.failsafe.core;
    requires javafx.swing;
}
