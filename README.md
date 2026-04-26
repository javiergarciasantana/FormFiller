# FormFiller: Desktop Automation Tool for Insurance Portals

FormFiller is a robust JavaFX-based desktop application designed to streamline and automate data entry processes for generic insurance web portals. By combining a user-friendly graphical interface with the power of Selenium WebDriver, the application allows users to manage multiple credentials and automate complex form-filling workflows with a single click.

## 🚀 Key Features

* **Automated Web Navigation**: Utilizes Selenium and ChromeDriver to navigate through generic insurance portals, handling logins and data entry automatically.
* **User Credential Management**: Features a dedicated Preferences window to store, add, and remove multiple user aliases and credentials locally.
* **Dynamic Data Handling**: Supports complex data structures for insurance forms, including customer details, vehicle information, and policy selections.
* **Cross-Platform Deployment**: Includes specialized scripts for building optimized runtime images (jlink) and macOS installers (DMG).

## 🛠️ Tech Stack

* **Language**: Java 17+
* **UI Framework**: JavaFX (OpenJFX)
* **Automation Engine**: Selenium WebDriver (Chrome)
* **Build Tool**: Apache Maven
* **Data Persistence**: Local JSON-based store for user preferences.

## 📂 Project Structure Overview

* `FormFillerApplication.java`: The main entry point that initializes the JavaFX stage and primary UI.
* `WebScript.java`: Contains the core automation logic, using Selenium to interact with the target insurance website.
* `PreferencesWindow.java`: Handles the UI and logic for managing user credentials and application settings.
* `FormFields.java`: Defines the data model used to map application inputs to web form elements.
* `UserDataStore.java`: Manages the reading and writing of user configurations to local storage.

## ⚙️ Configuration & Execution Scripts

The project includes several shell scripts to simplify development and deployment:

### 🏁 Execution: `start.sh`
The `start.sh` script is designed to ensure a consistent execution environment. Its main functions are:
1.  **Environment Validation**: Checks if `JAVA_HOME` is correctly set and verifies the Java version.
2.  **Module Path Resolution**: Automatically identifies the location of JavaFX modules and required libraries (like Selenium JARs found in `src/main/resources`).
3.  **Application Launch**: Executes the compiled JAR using the specific module-path and add-modules flags required for JavaFX and Selenium interoperation.

### 📦 Build & Packaging Scripts
Located in the `scripts/` directory, these tools facilitate distribution:
* **`build_jlink.sh`**: Uses the `jlink` tool to create a custom, minimal Java Runtime Environment (JRE) containing only the modules needed for FormFiller. This significantly reduces the final package size.
* **`build-dmg.sh`**: A macOS-specific script that packages the jlink output into a `.dmg` installer, including custom branding elements like `icon.icns`.

## 🛠️ Getting Started

### Prerequisites
* JDK 17 or higher.
* Maven 3.6+.
* Google Chrome browser installed (the application uses the system's Chrome version via Selenium).

### Installation
1.  Clone the repository.
2.  Build the project using Maven:
    ```bash
    mvn clean install
    ```
3.  Run the application using the provided script:
    ```bash
    chmod +x start.sh
    ./start.sh
    ```

## 🛡️ Usage Note
The `WebScript` component is configured to interact with standard elements found in generic insurance portals. Users must configure their specific credentials in the **Preferences** menu before initiating an automation task.
