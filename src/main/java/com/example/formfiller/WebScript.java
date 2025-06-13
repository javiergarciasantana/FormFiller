package com.example.formfiller;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.support.ui.Select;

import java.util.Map;

public class WebScript {
    public static void Start(String selectedUsername, String selectedPassword, Map<String, String> userData) {
        try {
            WebDriver driver = new SafariDriver();

            // Navigate to the URL of the page you want to fill
            driver.get("https://protec.wertgarantie.es/app/b2b/login"); // Replace with the actual URL
            // Maximize the window
            driver.manage().window().maximize();

            // LogIn Page
            WebElement usernameField = driver.findElement(By.id("username"));
            usernameField.sendKeys(selectedUsername);

            // Find the password field and enter a password
            WebElement passwordField = driver.findElement(By.id("password"));
            passwordField.sendKeys(selectedPassword);

            // Find the submit button and click it
            WebElement submitButton = driver.findElement(By.xpath("//button[text()='Iniciar sesión']"));
            submitButton.click();
            Thread.sleep(5000);

            WebElement smartphoneOption = driver.findElement(By.xpath("/html/body/div/main/div/ol/li[2]/div/div[3]/a"));


//            Actions actions = new Actions(driver);
//            actions.moveToElement(smartphoneOption).click().perform();
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", smartphoneOption);


            Thread.sleep(5000);

            // Price and insurance type selection
            WebElement priceInput = driver.findElement(By.name("objects_value"));
            priceInput.clear(); // Clear any existing value
            priceInput.sendKeys(userData.get("price"));

            if (userData.get("subscriptionType").equals("Yearly")) {
                WebElement yearlyOption = driver.findElement(By.xpath("/html/body/div/main/div/div/form/div[2]/div[3]/fieldset/div/div/div[2]/label/input"));
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", yearlyOption);
            }


            submitButton = driver.findElement(By.xpath("//button[contains(text(), 'Continuar a Datos del dispositivo')]"));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", submitButton);

            Thread.sleep(5000);

            //Device details page
            WebElement manufacturer = driver.findElement(By.xpath("/html/body/div/main/div/form/div[3]/div[1]/div/input"));
            manufacturer.clear(); // Clear any existing value
            manufacturer.sendKeys("Apple");

            WebElement model = driver.findElement(By.xpath("/html/body/div/main/div/form/div[3]/div[2]/div/input"));
            model.clear(); // Clear any existing value
            model.sendKeys(userData.get("model"));

            WebElement serialNum = driver.findElement(By.id("objects-serial-number"));
            serialNum.clear(); // Clear any existing value
            serialNum.sendKeys(userData.get("imei"));

            Select dayDropdown = new Select(driver.findElement(By.xpath("/html/body/div/main/div/form/div[3]/div[4]/div/div/div[1]/select")));
            dayDropdown.selectByValue((userData.get("purchaseDateDay")));

            // Locate the month dropdown and select the desired month
            Select monthDropdown = new Select(driver.findElement(By.xpath("/html/body/div/main/div/form/div[3]/div[4]/div/div/div[2]/select")));
            monthDropdown.selectByValue(userData.get("purchaseDateMonth"));

            // Locate the year dropdown and select the desired year
            Select yearDropdown = new Select(driver.findElement(By.xpath("/html/body/div/main/div/form/div[3]/div[4]/div/div/div[3]/select")));
            yearDropdown.selectByValue(userData.get("purchaseDateYear"));

            submitButton = driver.findElement(By.xpath("//button[contains(text(), 'Añadir y continuar al carro de compra')]"));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", submitButton);

            Thread.sleep(5000);

            // Click using the exact link text
            submitButton = driver.findElement(By.xpath("//a[contains(text(), 'Continuar a datos del cliente')]"));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", submitButton);

            Thread.sleep(2000);

            //Client details page
            WebElement firstName = driver.findElement(By.id("first-name"));
            WebElement lastName = driver.findElement(By.id("last-name"));
            WebElement streetName = driver.findElement(By.id("street-name"));
            WebElement zipCode = driver.findElement(By.id("zip-code"));
            WebElement clientCity = driver.findElement(By.id("city"));
            WebElement clientEmail = driver.findElement(By.id("email"));
            WebElement phoneNumber = driver.findElement(By.id("phone-number"));
            WebElement taxNumber = driver.findElement(By.id("tax-number"));

            WebElement salutationDropdown = driver.findElement(By.id("salutation"));

            // Initialize the Select class with the dropdown element
            Select selectSalutation = new Select(salutationDropdown);

            // Select the option by visible text, like "Sr." or "Sra."
            selectSalutation.selectByVisibleText(userData.get("treatment"));

            firstName.sendKeys(userData.get("name"));
            lastName.sendKeys(userData.get("surname"));
            streetName.sendKeys(userData.get("address"));
            zipCode.sendKeys(userData.get("postalCode"));
            clientCity.sendKeys(userData.get("city"));
            clientEmail.sendKeys(userData.get("email"));  // assuming email needs to be valid format
            phoneNumber.sendKeys(userData.get("phone"));  // example phone number format
            taxNumber.sendKeys(userData.get("nifNie"));


            dayDropdown = new Select(driver.findElement(By.name("birth_date[day]")));
            dayDropdown.selectByValue(userData.get("birthdayDateDay"));

            //Locate the month dropdown and select the desired month
            monthDropdown = new Select(driver.findElement(By.name("birth_date[month]")));
            monthDropdown.selectByValue(userData.get("birthdayDateMonth"));

            // Locate the year dropdown and select the desired year
            yearDropdown = new Select(driver.findElement(By.name("birth_date[year]")));
            yearDropdown.selectByValue(userData.get("birthdayDateYear"));

            submitButton = driver.findElement(By.xpath("//button[contains(text(), 'Continuar a Datos del pago')]"));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", submitButton);
            Thread.sleep(5000);

            //Iban page
            if (userData.get("subscriptionType").equals("Yearly")) {
                WebElement clientIban = driver.findElement(By.xpath("/html/body/div/main/div/form/div[3]/div[2]/div/input"));
                clientIban.clear();
                System.out.println(userData.get("iban"));
                clientIban.sendKeys(userData.get("iban"));
            } else {
                WebElement clientIban = driver.findElement(By.xpath("/html/body/div/main/div/form/div[3]/div[1]/div/input"));
                clientIban.clear();
                System.out.println(userData.get("iban"));
                clientIban.sendKeys(userData.get("iban"));
            }

            WebElement sepaCheckbox = driver.findElement(By.id("customer-has-accepted-sepa-mandate"));
            sepaCheckbox.click();


            WebElement consentCheckbox = driver.findElement(By.id("customer-has-provided-consent"));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", consentCheckbox);
            Thread.sleep(1000);
            consentCheckbox.click();

            WebElement documentsCheckbox = driver.findElement(By.id("customer-has-confirmed-documents"));
            documentsCheckbox.click();

            Thread.sleep(1000);
            submitButton = driver.findElement(By.xpath("//button[contains(text(), 'Continuar a firma de solicitud')]"));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", submitButton);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
