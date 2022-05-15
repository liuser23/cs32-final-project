package edu.brown.cs.student.main;

//import edu.brown.cs.student.commands.LoadTable;
import edu.brown.cs.student.main.Main;
//import edu.brown.cs.student.proj1.database.Database;
//import edu.brown.cs.student.proj1.repl.param.Param;
//import edu.brown.cs.student.proj1.repl.param.paramTypes.StringParam;
import io.github.bonigarcia.wdm.WebDriverManager;
import junit.framework.TestCase;
import org.junit.jupiter.api.BeforeAll;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.util.*;

/**
 * Selenium JUnit testing
 * FOR DEV: to click a clickable WebElement, call element.click()
 * FOR DEV: to type in a WebElement input field, call element.sendKeys("text here")
 * FOR DEV: At beginning of each test, call setupHelper() [and loginHelper() to navigate through the authentication]
 * FOR DEV: At the end of each test, call teardownHelper()
 */
public class UITest extends TestCase {

    // Stores the web driver
    WebDriver driver;
    // Tracks whether the initial setup has been done
    private static boolean is_setup_done = false;
    static String BASE_URI = "http://localhost:";
    static int FRONTEND_PORT = 3000;
    static String PAGE_URL = BASE_URI + FRONTEND_PORT;

    static String LOGIN_EMAIL;
    static String LOGIN_PASSWORD;

    static void setupOnce() {
        // Setup driver and start backend server
        // This should only be done once
        WebDriverManager.chromedriver().setup();
        backendServerHelper();
        try {
            LOGIN_EMAIL = Files.readString(Path.of("../secret/spotify_email.txt"));
            LOGIN_PASSWORD = Files.readString(Path.of("../secret/spotify_password.txt"));
        } catch (IOException e) {
            System.out.println("Could not read config files");
            e.printStackTrace();
            return;
        }
    }

    /**
     * Sets up the backend and Chrome driver for each test.
     */
    private void setupHelper() {

        if (!is_setup_done) {
            setupOnce();
            is_setup_done = true;
        }

        // This should be done before each test
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofMillis(500));
        driver.get(PAGE_URL);
    }

    /**
     * Starts the server, loads a table.
     */
    static void backendServerHelper() {
        String[] args = {"../secret/client_id.txt", "../secret/client_id.txt", "../secret/known.sqlite3"};
        Main.main(args);
    }


    /**
     * Quits the driver after each test.
     */
    private void teardownHelper() {
        driver.close();
    }

    private void loginHelper() {
        WebElement root = driver.findElement(By.id("root"));
        WebElement form = root.findElement(By.tagName("form"));
        WebElement button = form.findElement(By.tagName("button"));
        //Click the login button
        button.click();
        //Now redirected to Spotify's login

        WebElement root2 = driver.findElement(By.id("root"));
        WebElement div1 =  root2.findElement(By.tagName("div"));
        WebElement div2 = div1.findElements(By.tagName("div")).get(2);
        WebElement div3 = div2.findElement(By.tagName("div"));
        WebElement div4 = div3.findElement(By.tagName("div"));
        WebElement loginForm = div4.findElements(By.tagName("div")).get(1);
        List<WebElement> loginFormFields = loginForm.findElements(By.tagName("div"));
        WebElement loginEmailInput = loginFormFields.get(0).findElement(By.tagName("input"));
        WebElement loginPasswordInput = loginFormFields.get(2).findElement(By.tagName("input"));
        WebElement loginButton = loginFormFields.get(4).findElements(By.tagName("div")).get(2)
                .findElement(By.tagName("button"));

        loginEmailInput.sendKeys(LOGIN_EMAIL);
        loginPasswordInput.sendKeys(LOGIN_PASSWORD);
        loginButton.click();
    }



    public void testLandingPageHeader() {
        setupHelper();

        WebElement root = driver.findElement(By.id("root"));
        WebElement divForFullScreen = root.findElement(By.id("full-screen"));
        WebElement divForHeader = divForFullScreen.findElement(By.id("welcome-line"));
        WebElement header = divForHeader.findElement(By.tagName("h1"));


        System.out.println(header.getText());
        assertTrue(header.getText().equals("Welcome to"));



        WebElement form = root.findElement(By.tagName("form"));
        WebElement button = form.findElement(By.tagName("button"));
        button.click();


        teardownHelper();
    }

    public void testAllOfLandingPageHeader() {
        setupHelper();

        // get web elements
        WebElement root = driver.findElement(By.id("root"));
        WebElement divForFullScreen = root.findElement(By.id("full-screen"));
        WebElement divForLoginScreen = divForFullScreen.findElement(By.id("log-in-screen"));
        WebElement divForHeader = divForLoginScreen.findElement(By.id("welcomeLine"));
        WebElement header = divForHeader.findElement(By.tagName("h1"));
        WebElement header2 = divForLoginScreen.findElement(By.id("theBest"));
        WebElement appName = divForHeader.findElement(By.tagName("h4"));


        System.out.println(header.getText());
        assertTrue(header.getText().equals("Welcome to"));

        System.out.println(appName.getText());
        assertTrue(appName.getText().equals("Friendify"));

        System.out.println(header2.getText());
        assertTrue(header2.getText().equals("the best Spotify social media platform"));


        WebElement form = root.findElement(By.tagName("form"));
        WebElement button = form.findElement(By.tagName("button"));
        button.click();


        teardownHelper();
    }


    public void testLoginBasic() {
        setupHelper();

        loginHelper();

        teardownHelper();
    }

//    public void testFindFriends() {
//        setupHelper();
//
//        WebElement root = driver.findElement(By.id("root"));
//        WebElement menuOptions = root.findElement(By.id("menu-options"));
//
//        WebElement sideBar = root.findElement(By.id("sideBar"));
//        WebElement profilePic = root.findElement(By.id("hwai"));
//
//        WebElement menu = sideBar.findElement(By.id("accountMenu"));
//        WebElement menuOptions = menu.findElement(By.id("menu-options"));
//
//        System.out.println(header.getText());
//        assertTrue(header.getText().equals("Welcome to the best Spotify social media platform"));
//
//        WebElement form = root.findElement(By.tagName("form"));
//        WebElement button = form.findElement(By.tagName("button"));
//        button.click();
//
//        teardownHelper();
//    }
//
//    public void testFriendsList() {
//        setupHelper();
//
//        WebElement root = driver.findElement(By.id("root"));
//        WebElement sideBar = root.findElement(By.id("sideBar"));
//        WebElement menu = sideBar.findElement(By.id("accountMenu"));
//        WebElement menuOptions = menu.findElement(By.id("menu-options"));
//        // get Find Friends button
//
//        WebElement friendsButton = menu.findElement(By.id("findFriends"));
//
//        System.out.println(friendsButton.getText());
//        assertTrue(friendsButton.getText().equals("My Friends"));
//
//
//        teardownHelper();
//    }
//
//    public void testSettingsButton() {
//        setupHelper();
//
//        WebElement root = driver.findElement(By.id("root"));
//        WebElement sideBar = root.findElement(By.id("sideBar"));
//        WebElement menu = sideBar.findElement(By.id("accountMenu"));
//        WebElement menuOptions = menu.findElement(By.id("menu-options"));
//        // get Find Settings button
//
//        WebElement settingsButton = menu.findElement(By.id("settingsButton"));
//
//        System.out.println(settingsButton.getText());
//        assertTrue(settingsButton.getText().equals("Settings"));
//
//
//        teardownHelper();
//    }

}
