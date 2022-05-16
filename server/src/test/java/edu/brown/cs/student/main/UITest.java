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
import java.util.concurrent.TimeUnit;

/**
 * Selenium JUnit testing
 * FOR DEV: to click a clickable WebElement, call element.click()
 * FOR DEV: to type in a WebElement input field, call element.sendKeys("text here")
 * FOR DEV: At beginning of each test, call setupHelper() [and loginHelper() to navigate through the authentication]
 * FOR DEV: At the end of each test, call teardownHelper()
 * FOR DEV: When testing all tests at once, make sure screen is in full screen, or else certain elements
 * are unable to be clicked because they overlap with side bar
 * Some of these songs may fail in future when user data has changed (top songs, top artist etc). Check tests
 * with "NoNames" tag for solution.
 *
 * Tests and lines they start on:
 * testLandingPageHeader (start line 152)
 * testAllOfLandingPageHeader (start line 173)
 * testLoginBasic (start line 204)
 * testHomeScreen (start line 217)
 * testHomeScreenButtons (start line 255)
 * testSearchSongsPage (start line 343)
 * testHomePagePlayer (start line 692)
 * testDashboard (start line 875)
 * testSongRecRankings (start line 943)
 * testMyRecsButton (start line 1277)
 * testHomePageTopArtists (start line 1330)
 * testSettingsButton (start line 1550)
 * testSignOutButton (start line 1527)
 * testFriendsListButton (start line 1554)
 * testHomePagePlayerNoNames (start line 1598)
 * testHomePageTopArtistsNoNames (start line 1779)
 *
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

    static String USERNAME;

    static void setupOnce() {
        // Setup driver and start backend server
        // This should only be done once
        WebDriverManager.chromedriver().setup();
        backendServerHelper();
        try {
            LOGIN_EMAIL = Files.readString(Path.of("../secret/spotify_email.txt"));
            LOGIN_PASSWORD = Files.readString(Path.of("../secret/spotify_password.txt"));
            USERNAME = Files.readString(Path.of("../secret/spotify_username.txt"));

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
        driver.manage().timeouts().implicitlyWait(Duration.ofMillis(2000));
        driver.get(PAGE_URL);
    }

    /**
     * Starts the server, loads a table.
     */
    static void backendServerHelper() {
        String[] args = {"../secret/client_id.txt", "../secret/client_secret.txt", "../secret/known.sqlite3"};
        Main.main(args);
    }


    /**
     * Quits the driver after each test.
     */
    private void teardownHelper() {
        driver.close();
    }

    /**
     * Logs the user in using information in secret file
     */
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


    /**
     * Tests the title on the first page of website before you log in
     */
    public void testLandingPageHeader() {
        setupHelper();

        WebElement root = driver.findElement(By.id("root"));
        WebElement divForFullScreen = root.findElement(By.id("full-screen"));
        WebElement divForHeader = divForFullScreen.findElement(By.id("welcomeLine"));
        WebElement header = divForHeader.findElement(By.tagName("h1"));

        System.out.println(header.getText());
        assertTrue(header.getText().equals("Welcome to"));

        WebElement form = root.findElement(By.tagName("form"));
        WebElement button = form.findElement(By.tagName("button"));
        button.click();

        teardownHelper();
    }

    /**
     * Tests all the first page elements of website before you log in
     */
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

        WebElement buttonGifDiv = divForLoginScreen.findElement(By.id("buttonGif"));
        WebElement buttonDiv = buttonGifDiv.findElement(By.id("buttonDiv"));
        WebElement form = buttonDiv.findElement(By.tagName("form"));
        WebElement button = form.findElement(By.tagName("button"));
        button.click();

        teardownHelper();
    }


    public void testLoginBasic() {
        setupHelper();

        loginHelper();

        teardownHelper();
    }

    /**
     * Tests the home-screen (after login) and checks username is correct and
     * pictures are accessibility friendly,
     * @throws InterruptedException
     */
    public void testHomeScreen() throws InterruptedException {
        setupHelper();

        loginHelper();

        // these dont work, use Thread.sleep()
        // driver.manage().timeouts().implicitlyWait(Duration.ofMillis(10000));
        //driver.manage().timeouts().setScriptTimeout(10,TimeUnit.SECONDS);
        // driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
        Thread.sleep(10000); // use sleep to prevent stale elements


        WebElement root2 = driver.findElement(By.id("root"));
        WebElement sideBar = root2.findElement(By.id("sideNavBar"));

        WebElement profilePicture = sideBar.findElement(By.id("profilePic"));
        WebElement profilePictureImg = profilePicture.findElement(By.tagName("img"));


        System.out.println(profilePictureImg.getAccessibleName());
        assertTrue(profilePictureImg.getAccessibleName().equals("Your profile photo!"));

        WebElement mainWindow = root2.findElement(By.id("mainWindow"));
        WebElement profileDiv = mainWindow.findElement(By.id("profileDiv"));
        WebElement profileHeader = profileDiv.findElement(By.id("userNameText"));

        // check the username is correct
        System.out.println(profileHeader.getText());
        assertTrue(profileHeader.getText().equals(USERNAME));

        teardownHelper();
    }

    /**
     * This tests all the buttons on the side nav bar.
     * It makes sure they are all accessibility friendly.
     * @throws InterruptedException
     */
    public void testHomeScreenButtons() throws InterruptedException {
        setupHelper();
        loginHelper();
        Thread.sleep(10000); // use sleep to prevent stale elements

        WebElement root2 = driver.findElement(By.id("root"));
        WebElement sideBar = root2.findElement(By.id("sideNavBar"));
        WebElement menu = sideBar.findElement(By.id("accountMenu"));
        WebElement menuOptions = menu.findElement(By.id("menu-options"));

        // account overview button
        WebElement accountOverviewButton = menuOptions.findElements(By.tagName("a")).get(0);
        WebElement accountOverviewButtonImage = accountOverviewButton.findElement(By.tagName("img"));
        System.out.println(accountOverviewButtonImage.getAccessibleName());
        assertTrue(accountOverviewButtonImage.getAccessibleName().equals("house"));
        WebElement accountOverviewButtonText = accountOverviewButton.findElement(By.tagName("p"));
        System.out.println(accountOverviewButtonText.getText());
        assertTrue(accountOverviewButtonText.getText().equals("Account Overview"));

        // search songs button
        WebElement searchSongButton = menuOptions.findElements(By.tagName("a")).get(1);
        WebElement searchSongButtonImage = searchSongButton.findElement(By.tagName("img"));
        System.out.println(searchSongButtonImage.getAccessibleName());
        assertTrue(searchSongButtonImage.getAccessibleName().equals("search icon"));
        WebElement searchSongButtonText = searchSongButton.findElement(By.tagName("p"));
        System.out.println(searchSongButtonText.getText());
        assertTrue(searchSongButtonText.getText().equals("Search Songs"));

        // my recommendations button
        WebElement myRecsButton = menuOptions.findElements(By.tagName("a")).get(2);
        WebElement myRecsButtonImage = myRecsButton.findElement(By.tagName("img"));
        System.out.println(myRecsButtonImage.getAccessibleName());
        assertTrue(myRecsButtonImage.getAccessibleName().equals("list icon"));
        WebElement myRecsButtonText = myRecsButton.findElement(By.tagName("p"));
        System.out.println(myRecsButtonText.getText());
        assertTrue(myRecsButtonText.getText().equals("My Recommendations"));

        // find friends button
        WebElement findFriendsButton = menuOptions.findElements(By.tagName("a")).get(3);
        WebElement findFriendsButtonImage = findFriendsButton.findElement(By.tagName("img"));
        System.out.println(findFriendsButtonImage.getAccessibleName());
        assertTrue(findFriendsButtonImage.getAccessibleName().equals("search icon"));
        WebElement findFriendsButtonText = findFriendsButton.findElement(By.tagName("p"));
        System.out.println(findFriendsButtonText.getText());
        assertTrue(findFriendsButtonText.getText().equals("Find Friends"));

        // friends list button
        WebElement friendsListButton = menuOptions.findElements(By.tagName("a")).get(4);
        WebElement friendsListButtonImage = friendsListButton.findElement(By.tagName("img"));
        System.out.println(friendsListButtonImage.getAccessibleName());
        assertTrue(friendsListButtonImage.getAccessibleName().equals("friends icon"));
        WebElement friendsListButtonText = friendsListButton.findElement(By.tagName("p"));
        System.out.println(friendsListButtonText.getText());
        assertTrue(friendsListButtonText.getText().equals("Friends List"));

        // settings button
        WebElement settingsButton = menuOptions.findElements(By.tagName("a")).get(5);
        WebElement settingsButtonImage = settingsButton.findElement(By.tagName("img"));
        System.out.println(settingsButtonImage.getAccessibleName());
        assertTrue(settingsButtonImage.getAccessibleName().equals("lock icon"));
        WebElement settingsButtonText = settingsButton.findElement(By.tagName("p"));
        System.out.println(settingsButtonText.getText());
        assertTrue(settingsButtonText.getText().equals("Settings"));

        // sign out button
        WebElement signOutButton = menuOptions.findElements(By.tagName("a")).get(6);
        WebElement signOutButtonImage = signOutButton.findElement(By.tagName("img"));
        System.out.println(signOutButtonImage.getAccessibleName());
        assertTrue(signOutButtonImage.getAccessibleName().equals("exit icon"));
        WebElement signOutButtonText = signOutButton.findElement(By.tagName("p"));
        System.out.println(signOutButtonText.getText());
        assertTrue(signOutButtonText.getText().equals("Sign Out"));
    }

    /**
     * This tests the dashboard and all of its features.
     * It tests:
     *  - searching for song, the output is correct and when you select song, song starts to play
     *  - checks that by clicking "play" on one of the top song recommendations, you go to new page for that song
     *  - checks that the playing song updates when you press on other songs
     *  - checks that you can add your own recommendations
     *  - checks that if there are no top songs recs, your recommendations go to the top songs list
     *  - checks that you can delete your own recommendations
     *
     *  NOTE: possible that it fails due to fact that search results change order occasionally randomly
     *  and this test checks which results appear and has to call on them in an order
     * @throws InterruptedException
     */
    public void testSearchSongsPage() throws InterruptedException {
        setupHelper();
        loginHelper();
        Thread.sleep(10000); // use sleep to prevent stale elements

        WebElement root2 = driver.findElement(By.id("root"));
        WebElement sideBar = root2.findElement(By.id("sideNavBar"));
        WebElement menu = sideBar.findElement(By.id("accountMenu"));
        WebElement menuOptions = menu.findElement(By.id("menu-options"));

        // navigate to dashboard from home page
        WebElement searchSongButton = menuOptions.findElements(By.tagName("a")).get(1);
        searchSongButton.click();
        Thread.sleep(10000); // use sleep to prevent stale elements

        WebElement root3 = driver.findElement(By.id("root"));
        WebElement mainWindow = root3.findElement(By.id("mainWindow"));
        WebElement mainDiv = mainWindow.findElement(By.id("mainDiv"));
        WebElement searchBarDiv = mainDiv.findElement(By.id("searchBarDiv"));
        WebElement searchBarInput = searchBarDiv.findElement(By.tagName("input"));

        // check that it is correct form input
        System.out.println(searchBarInput.getAttribute("placeholder"));
        assertTrue(searchBarInput.getAttribute("placeholder").equals("Search Songs/Albums"));
        WebElement findSongsDiv = mainDiv.findElement(By.id("searchResultsFindSongDiv"));

        WebElement topSongsAndSearchDiv = findSongsDiv.findElement(By.id("topRecsAndSearchBarDiv"));
        WebElement addingSongDiv = topSongsAndSearchDiv.findElement(By.id("addingSong"));
        WebElement searchRecsInput = addingSongDiv.findElement(By.tagName("input"));

        // check other form input placeholder is correct
        System.out.println(searchRecsInput.getAttribute("placeholder"));
        assertTrue(searchRecsInput.getAttribute("placeholder").equals("Search Songs to Recommend"));


        // search for song using "a"
        searchBarInput.sendKeys("a");
        List<WebElement> songResults = findSongsDiv.findElements(By.id("songResult"));
        WebElement firstSongResult = songResults.get(0).findElement(By.id("songInfo"));
        WebElement firstSongName = firstSongResult.findElement(By.id("songName"));
        WebElement firstSongArtist = firstSongResult.findElement(By.id("songArtist"));

        // check that the first song result is "As It Was" by "Harry Styles"
        System.out.println("first song result name: " + firstSongName.getText());
        assertTrue(firstSongName.getText().equals("As It Was"));

        System.out.println("first song result artist: " +firstSongArtist.getText());
        assertTrue(firstSongArtist.getText().equals("Harry Styles"));

        // get the second song result in list for search 'a'
        WebElement secondSongResult = songResults.get(1).findElement(By.id("songInfo"));
        WebElement secondSongName = secondSongResult.findElement(By.id("songName"));
        WebElement secondSongArtist = secondSongResult.findElement(By.id("songArtist"));

        // check that the second song result is "About Damn Time" by "Lizzo"
        System.out.println("second song result name: " +secondSongName.getText());
        assertTrue(secondSongName.getText().equals("About Damn Time"));

        System.out.println("second song result artist: " + secondSongArtist.getText());
        assertTrue(secondSongArtist.getText().equals("Lizzo"));

        // press on As It Was song to start playing
        firstSongResult.click();
        WebElement playerBox = sideBar.findElement(By.id("Player-box"));
        WebElement playerPlayButton = playerBox.findElement(By.tagName("button"));
        playerPlayButton.click(); // start player

        // check that As It Was started playing and screen is updated with top songs and recs

        // get the name and artist of what is currently playing (As it Was)
        WebElement playingAndTopRecs = findSongsDiv.findElement(By.id("playing-and-top-recs"));
        WebElement currentlyPlaying = playingAndTopRecs.findElement(By.id("currently-playing"));

        // check image source, should be https://i.scdn.co/image/ab67616d0000b273b46f74097655d7f353caab14
        WebElement playingTrackImage = currentlyPlaying.findElement(By.tagName("img"));
        System.out.println("playing song img src: " + playingTrackImage.getAttribute("src"));
        assertTrue(playingTrackImage.getAttribute("src").equals("https://i.scdn.co/image/ab67616d0000b273b46f74097655d7f353caab14"));

        // get name and artist
        WebElement playingTrackInfoList = currentlyPlaying.findElement(By.tagName("ul"));
        WebElement playingTrackInfoListItem = playingTrackInfoList.findElement(By.tagName("li"));
        WebElement playingTrackInfoListItemTextDiv = playingTrackInfoListItem.findElement(By.tagName("div"));
        WebElement playingTrackSongName = playingTrackInfoListItemTextDiv.findElement(By.tagName("span"));
        WebElement playingTrackSongArtist = playingTrackInfoListItemTextDiv.findElement(By.tagName("p"));

        // check song name is correct
        System.out.println("playing track song name: "+ playingTrackSongName.getText());
        assertTrue(playingTrackSongName.getText().equals("As It Was"));

        // check song artist is correct
        System.out.println("playing track artist: " + playingTrackSongArtist.getText());
        assertTrue(playingTrackSongArtist.getText().equals("Harry Styles"));

        // now check top three songs: should be Summertime in Paris, Watermelon Sugar, and Hello
        WebElement topSongsListDiv = playingAndTopRecs.findElement(By.id("topSongsList"));
        WebElement topSongsList = topSongsListDiv.findElement(By.tagName("ul"));

        // get list of song items
        List<WebElement> threeTopSongs = topSongsList.findElements(By.tagName("li"));

        // get song # 1 info (in list for, first div = play icon, second div = number icon, third div = name and artist
        List<WebElement> firstTopSong = threeTopSongs.get(0).findElements(By.tagName("div"));

        // get 2nd element, song name and artist info
        WebElement firstTopSongName = firstTopSong.get(2).findElement(By.tagName("span"));
        WebElement firstTopSongArtist = firstTopSong.get(2).findElement(By.tagName("p"));

        // check top song #1 name and artist
        System.out.println("firstTopSongName: " + firstTopSongName.getText());
        assertTrue(firstTopSongName.getText().equals("Summertime In Paris"));

        System.out.println("firstTopSongArtist: " + firstTopSongArtist.getText());
        assertTrue(firstTopSongArtist.getText().equals("Jaden"));

        // get song # 2 Watermelon Sugar info (in list for, first div = play icon, second div = number icon, third div = name and artist
        List<WebElement> secondTopSong = threeTopSongs.get(1).findElements(By.tagName("div"));

        // get 2nd element, song name and artist info
        WebElement secondTopSongName = secondTopSong.get(2).findElement(By.tagName("span"));
        WebElement secondTopSongArtist = secondTopSong.get(2).findElement(By.tagName("p"));

        // check top song #2 name and artist
        System.out.println("secondTopSongName: " + secondTopSongName.getText());
        assertTrue(secondTopSongName.getText().equals("Watermelon Sugar"));

        System.out.println("secondTopSongArtist: " + secondTopSongArtist.getText());
        assertTrue(secondTopSongArtist.getText().equals("Harry Styles"));

        // get song # 3 Hello info (in list for, first div = play icon, second div = number icon, third div = name and artist
        List<WebElement> thirdTopSong = threeTopSongs.get(2).findElements(By.tagName("div"));

        // get 2nd element, song name and artist info
        WebElement thirdTopSongName = thirdTopSong.get(2).findElement(By.tagName("span"));
        WebElement thirdTopSongArtist = thirdTopSong.get(2).findElement(By.tagName("p"));

        // check top song #3 name and artist
        System.out.println("thirdTopSongName: " + thirdTopSongName.getText());
        assertTrue(thirdTopSongName.getText().equals("Hello (feat. Dragonette)"));

        System.out.println("thirdTopSongArtist: " + thirdTopSongArtist.getText());
        assertTrue(thirdTopSongArtist.getText().equals("Martin Solveig"));


        // **** Check that you can switch what song is playing and lists are updated ****
        // Play song from top recs, let's play 'Summertime in Paris'
        // need to get icons, first is play song, second is add to playlist
        WebElement firstTopSongButtonsDiv = threeTopSongs.get(0).findElement(By.id("topSongsButtons"));
        WebElement playButton = firstTopSongButtonsDiv.findElement(By.id("playSongButton"));

        // press button to play song
        playButton.click();
        Thread.sleep(10000); // use sleep to prevent stale elements

        WebElement root4 = driver.findElement(By.id("root"));
        WebElement mainWindow2 = root4.findElement(By.id("mainWindow"));
        WebElement mainDiv2 = mainWindow2.findElement(By.id("mainDiv"));
        WebElement findSongsDiv2 = mainDiv2.findElement(By.id("searchResultsFindSongDiv"));
        WebElement topSongsAndSearchDiv2 = findSongsDiv2.findElement(By.id("topRecsAndSearchBarDiv"));
        WebElement addingSongDiv2 = topSongsAndSearchDiv2.findElement(By.id("addingSong"));
        WebElement searchRecsInput2 = addingSongDiv2.findElement(By.tagName("input"));

        // get the name and artist of what is now currently playing, should be Summertime in paris
        WebElement playingAndTopRecs2 = findSongsDiv2.findElement(By.id("playing-and-top-recs"));
        WebElement currentlyPlaying2 = playingAndTopRecs2.findElement(By.id("currently-playing"));

        // check image source, should be https://i.scdn.co/image/ab67616d0000b27360ec4df52c2d724bc53ffec5
        WebElement playingTrackImage2 = currentlyPlaying2.findElement(By.tagName("img"));
        System.out.println("new playing song img src: " + playingTrackImage2.getAttribute("src"));
        assertTrue(playingTrackImage2.getAttribute("src").equals("https://i.scdn.co/image/ab67616d0000b27360ec4df52c2d724bc53ffec5"));

        // get name and artist
        WebElement playingTrackInfoList2 = currentlyPlaying2.findElement(By.tagName("ul"));
        WebElement playingTrackInfoListItem2 = playingTrackInfoList2.findElement(By.tagName("li"));
        WebElement playingTrackInfoListItemTextDiv2 = playingTrackInfoListItem2.findElement(By.tagName("div"));
        WebElement playingTrackSongName2 = playingTrackInfoListItemTextDiv2.findElement(By.tagName("span"));
        WebElement playingTrackSongArtist2 = playingTrackInfoListItemTextDiv2.findElement(By.tagName("p"));

        // check playing song has updated
        System.out.println("new playing track song name: "+ playingTrackSongName2.getText());
        assertTrue(playingTrackSongName2.getText().equals("Summertime In Paris"));

        System.out.println("new playing track artist: " + playingTrackSongArtist2.getText());
        assertTrue(playingTrackSongArtist2.getText().equals("Jaden"));


        // ******** check you can add recommendations *******

        // search for artist, not song, in second input bar
        searchRecsInput2.sendKeys("frank ocean nigh");
        List<WebElement> songRecResults = findSongsDiv.findElements(By.id("songResult"));
        WebElement firstSongRecResult = songRecResults.get(0).findElement(By.id("songInfo"));
        WebElement firstSongRecName = firstSongRecResult.findElement(By.id("songName"));
        WebElement firstSongRecArtist = firstSongRecResult.findElement(By.id("songArtist"));

        // check that the first song result is "Nights" by "Frank Ocean"
        System.out.println("first song result name: " + firstSongRecName.getText());
        assertTrue(firstSongRecName.getText().equals("Nights"));

        System.out.println("first song result artist: " + firstSongRecArtist.getText());
        assertTrue(firstSongRecArtist.getText().equals("Frank Ocean"));

        WebElement secondSongRecResult = songRecResults.get(1).findElement(By.id("songInfo"));
        WebElement secondSongRecName = secondSongRecResult.findElement(By.id("songName"));
        WebElement secondSongRecArtist = secondSongRecResult.findElement(By.id("songArtist"));

        // check that the second song result is "Night to Remember" by "Shravya Kamaraju"
        System.out.println("second song result name: " + secondSongRecName.getText());
        assertTrue(secondSongRecName.getText().equals("Night to Remember"));

        System.out.println("second song result artist: " + secondSongRecArtist.getText());
        assertTrue(secondSongRecArtist.getText().equals("Shravya Kamaraju"));

        WebElement seventhSongRecResult = songRecResults.get(6).findElement(By.id("songInfo"));
        WebElement seventhSongRecName = seventhSongRecResult.findElement(By.id("songName"));
        WebElement seventhSongRecArtist = seventhSongRecResult.findElement(By.id("songArtist"));

        // check that the 7th song result is "Nikes" by "Frank Ocean"
        System.out.println("seventh song result name: " + seventhSongRecName.getText());
        assertTrue(seventhSongRecName.getText().equals("Nikes"));

        System.out.println("eighth song result artist: " + seventhSongRecArtist.getText());
        assertTrue(seventhSongRecArtist.getText().equals("Frank Ocean"));

        // press on Nights to add to your recommendations
        firstSongRecResult.click();

        Thread.sleep(5000);

        // now add 2nd recommendation
        // search by song name (ear --> EARFQUAKE)
        searchRecsInput2.sendKeys("ear");
        List<WebElement> songRecResults2 = findSongsDiv.findElements(By.id("songResult"));
        WebElement firstSongRecResult2 = songRecResults2.get(0).findElement(By.id("songInfo"));
        WebElement firstSongRecName2 = firstSongRecResult2.findElement(By.id("songName"));
        WebElement firstSongRecArtist2 = firstSongRecResult2.findElement(By.id("songArtist"));

        // check that the first song result is "EARFQUAKE" by "Tyler, The Creator"
        System.out.println("first song result name: " + firstSongRecName2.getText());
        assertTrue(firstSongRecName2.getText().equals("EARFQUAKE"));

        System.out.println("first song result artist: " + firstSongRecArtist2.getText());
        assertTrue(firstSongRecArtist2.getText().equals("Tyler, The Creator"));

        // click on song to add as recommendation
        firstSongRecResult2.click();

        Thread.sleep(5000); // use sleep to prevent stale elements

        // ***** Check songs were added to top songs rec list ***

        // now check the two recommendations user has made: should be Nights and EARFQUAKE
        WebElement recsList = topSongsAndSearchDiv2.findElement(By.id("recsList"));
        WebElement songRecsList = recsList.findElement(By.tagName("ul"));

        // get list of top song items
        List<WebElement> threeRecSongs = songRecsList.findElements(By.tagName("li"));

        // get song # 1 info (in list for, first div = play icon, second div = number icon, third div = name and artist
        List<WebElement> firstRecSong = threeRecSongs.get(0).findElements(By.tagName("div"));

        // get 2nd element, song name and artist info
        WebElement firstRecSongName = firstRecSong.get(2).findElement(By.tagName("span"));
        WebElement firstRecSongArtist = firstRecSong.get(2).findElement(By.tagName("p"));

        // first rec should be Nights
        System.out.println("firstRecSongName: " + firstRecSongName.getText());
        assertTrue(firstRecSongName.getText().equals("Nights"));

        System.out.println("firstRecSongArtist: " + firstRecSongArtist.getText());
        assertTrue(firstRecSongArtist.getText().equals("Frank Ocean"));

        // get song # 2 EARFQUAKE info (in list for, first div = play icon, second div = number icon, third div = name and artist
        List<WebElement> secondRecSong = threeRecSongs.get(1).findElements(By.tagName("div"));

        // get 2nd element, song name and artist info
        WebElement secondRecSongName = secondRecSong.get(2).findElement(By.tagName("span"));
        WebElement secondRecSongArtist = secondRecSong.get(2).findElement(By.tagName("p"));

        // second rec should be EARFQUAKE
        System.out.println("secondRecSongName: " + secondRecSongName.getText());
        assertTrue(secondRecSongName.getText().equals("EARFQUAKE"));

        System.out.println("secondRecSongArtist: " + secondRecSongArtist.getText());
        assertTrue(secondRecSongArtist.getText().equals("Tyler, The Creator"));

        // ****** Check you can Delete Recommendation ******
        // delete EARFQUAKE song from your recommendations
        // need to get icons, first is play song, second is add to playlist
        WebElement songRecsButtonsDiv = threeRecSongs.get(1).findElement(By.id("songRecsButtons"));
        WebElement deleteRecButton = songRecsButtonsDiv.findElement(By.id("deleteRecButton"));

        // play Nights song from your recommendations
        // need to get icons, first is play song, second is add to playlist
        WebElement songRecsButtonsDiv2 = threeRecSongs.get(0).findElement(By.id("songRecsButtons"));
        WebElement playSongButton2 = songRecsButtonsDiv2.findElement(By.id("playSongButton"));

        // delete song
        deleteRecButton.click();

        // check if play button for songs under my recommendations list works
        // play 'Nights'
        playSongButton2.click();
        Thread.sleep(5000); // use sleep to prevent stale elements

        // check what song is now playing
        WebElement root5 = driver.findElement(By.id("root"));
        WebElement mainWindow3 = root5.findElement(By.id("mainWindow"));
        WebElement mainDiv3 = mainWindow3.findElement(By.id("mainDiv"));
        WebElement findSongsDiv3 = mainDiv3.findElement(By.id("searchResultsFindSongDiv"));
        WebElement topSongsAndSearchDiv3 = findSongsDiv3.findElement(By.id("topRecsAndSearchBarDiv"));
        WebElement addingSongDiv3 = topSongsAndSearchDiv3.findElement(By.id("addingSong"));
        WebElement searchRecsInput3 = addingSongDiv3.findElement(By.tagName("input"));

        // get the name and artist of what is now currently playing,
        WebElement playingAndTopRecs3 = findSongsDiv3.findElement(By.id("playing-and-top-recs"));
        WebElement currentlyPlaying3 = playingAndTopRecs3.findElement(By.id("currently-playing"));

        // check image source, should be https://i.scdn.co/image/ab67616d0000b273c5649add07ed3720be9d5526
        WebElement playingTrackImage3 = currentlyPlaying3.findElement(By.tagName("img"));
        System.out.println("new playing song img src: " + playingTrackImage3.getAttribute("src"));
        assertTrue(playingTrackImage3.getAttribute("src").equals("https://i.scdn.co/image/ab67616d0000b273c5649add07ed3720be9d5526"));

        // get name and artist
        WebElement playingTrackInfoList3 = currentlyPlaying3.findElement(By.tagName("ul"));
        WebElement playingTrackInfoListItem3 = playingTrackInfoList3.findElement(By.tagName("li"));
        WebElement playingTrackInfoListItemTextDiv3 = playingTrackInfoListItem3.findElement(By.tagName("div"));
        WebElement playingTrackSongName3 = playingTrackInfoListItemTextDiv3.findElement(By.tagName("span"));
        WebElement playingTrackSongArtist3 = playingTrackInfoListItemTextDiv3.findElement(By.tagName("p"));

        System.out.println("new playing track song name: "+ playingTrackSongName3.getText());
        assertTrue(playingTrackSongName3.getText().equals("Nights"));

        System.out.println("new playing track artist: " + playingTrackSongArtist3.getText());
        assertTrue(playingTrackSongArtist3.getText().equals("Frank Ocean"));

        teardownHelper();
    }


    /**
     * This tests the functionality of the song player on the homepage
     * It tests that the right top songs for the user are displayed,
     * that clicking a song will play it, and using the right and left side-bars displays
     * new songs and goes back to old ones.
     *
     * NOTE: run this test in full screen, or the left side-bar for top songs can't be clicked as it
     * is too close to the side nav bar when the screen is compressed
     * @throws InterruptedException
     */
    public void testHomePagePlayer() throws InterruptedException {
        setupHelper();
        loginHelper();
        Thread.sleep(10000); // use sleep to prevent stale elements

        WebElement root2 = driver.findElement(By.id("root"));
        WebElement sideNavBar = root2.findElement(By.id("sideNavBar"));

        WebElement mainWindow = root2.findElement(By.id("mainWindow"));

        WebElement topSongsBox = mainWindow.findElement(By.id("topSongsBox"));
        WebElement topSongsText = topSongsBox.findElement(By.tagName("p"));

        System.out.println(topSongsText.getText());
        assertTrue(topSongsText.getText().equals("Top Songs"));

        WebElement topSongsButtonRight = topSongsBox.findElement(By.id("songNavButtonRight"));
        WebElement topSongsButtonLeft = topSongsBox.findElement(By.id("songNavButtonLeft"));
        WebElement topSongsItemsList = topSongsBox.findElement(By.id("topSongsList"));
        List<WebElement> topSongsList = topSongsItemsList.findElements(By.id("songBox"));

        WebElement songOne = topSongsList.get(0);
        String songOneName = songOne.getText();
        System.out.println("songOneName: " + songOneName);
        assertTrue(songOneName.equals("As It Was"));

        WebElement songTwo = topSongsList.get(1);
        String songTwoName = songTwo.getText();
        System.out.println("songTwoName: " + songTwoName);
        assertTrue(songTwoName.equals("Nothing New (feat. Phoebe Bridgers) (Taylor’s Version) (From The Vault)"));

        WebElement songThree = topSongsList.get(2);
        String songThreeName = songThree.getText();
        System.out.println("songThreeName: " + songThreeName);
        assertTrue(songThreeName.equals("It Never Rains in Southern California"));

        WebElement songFour = topSongsList.get(3);
        String songFourName = songFour.getText();
        System.out.println("songFourName: " + songFourName);
        assertTrue(songFourName.equals("Dead of Night"));

        // play song 3: It Never Rains in Southern California
        songThree.click();
        Thread.sleep(5000); // use sleep to prevent stale elements

        WebElement menu = sideNavBar.findElement(By.id("accountMenu"));
        WebElement playerBox = menu.findElement(By.id("Player-box"));
        WebElement playerPlayButton = playerBox.findElement(By.tagName("button"));
        playerPlayButton.click(); // start player
        Thread.sleep(5000); // use sleep to prevent stale elements

        // press the right side bar button to see new song
        topSongsButtonRight.click();

        // get new list of Songs
        List<WebElement> topSongsListRightOne = topSongsItemsList.findElements(By.id("songBox"));

        // check songs have been shifted one and there is a new song
        WebElement songFive = topSongsListRightOne.get(0);
        System.out.println(songFive.getText());
        assertTrue(songFive.getText().equals("Nothing New (feat. Phoebe Bridgers) (Taylor’s Version) (From The Vault)"));

        WebElement songSix = topSongsListRightOne.get(1);
        System.out.println(songSix.getText());
        assertTrue(songSix.getText().equals("It Never Rains in Southern California"));

        WebElement songSeven = topSongsListRightOne.get(2);
        System.out.println(songSeven.getText());
        assertTrue(songSeven.getText().equals("Dead of Night"));

        WebElement songEight = topSongsListRightOne.get(3);
        System.out.println(songEight.getText());
        assertTrue(songEight.getText().equals("Watercolor Eyes - From “Euphoria” An HBO Original Series"));

        // press the right sidebar button 3 more times, the new four should be
        // completely different then first four and there should have been 8 songs total
        topSongsButtonRight.click();
        topSongsButtonRight.click();
        topSongsButtonRight.click();

        Thread.sleep(10000); // use sleep to prevent stale elements

        // get new list of Songs
        List<WebElement> topSongsListRightFour = topSongsItemsList.findElements(By.id("songBox"));

        // check songs have been shifted one and there is a new song
        WebElement songNine = topSongsListRightFour.get(0);
        System.out.println(songNine.getText());
        assertTrue(songNine.getText().equals("Watercolor Eyes - From “Euphoria” An HBO Original Series"));

        // song in the first slot, should not equal the first song in first slot
        assertFalse(songNine.getText().equals(songOneName));

        WebElement songTen = topSongsListRightFour.get(1);
        System.out.println(songTen.getText());
        assertTrue(songTen.getText().equals("Stubborn Love"));

        // song in the second slot, should not equal the first song in second slot
        assertFalse(songTen.getText().equals(songTwoName));

        WebElement songEleven = topSongsListRightFour.get(2);
        System.out.println(songEleven.getText());
        assertTrue(songEleven.getText().equals("Right Down the Line"));

        // song in the third slot, should not equal the first song in third slot
        assertFalse(songEleven.getText().equals(songThreeName));

        WebElement songTwelve = topSongsListRightFour.get(3);
        System.out.println(songTwelve.getText());
        assertTrue(songTwelve.getText().equals("deja vu"));

        // song in the fourth slot, should not equal the first song in fourth slot
        assertFalse(songTwelve.getText().equals(songFourName));

        // now check that none of the songs from first four are equal to last four
        assertFalse(songNine.getText().equals(songTwoName));
        assertFalse(songNine.getText().equals(songThreeName));
        assertFalse(songNine.getText().equals(songFourName));

        assertFalse(songTen.getText().equals(songThreeName));
        assertFalse(songTen.getText().equals(songFourName));
        assertFalse(songTen.getText().equals(songOneName));

        assertFalse(songEleven.getText().equals(songFourName));
        assertFalse(songEleven.getText().equals(songOneName));
        assertFalse(songEleven.getText().equals(songTwoName));

        assertFalse(songTwelve.getText().equals(songOneName));
        assertFalse(songTwelve.getText().equals(songTwoName));
        assertFalse(songTwelve.getText().equals(songThreeName));

        // play song 12: Deja Vu to check new songs play function works
        songTwelve.click();
        Thread.sleep(5000); // use sleep to prevent stale elements

        // test opposite direction
        // press the left sidebar button 4  times, the new four should be
        // the same as the first four
        topSongsButtonLeft.click();
        topSongsButtonLeft.click();
        topSongsButtonLeft.click();
        topSongsButtonLeft.click();

        Thread.sleep(10000); // use sleep to prevent stale elements

        // get new list of Songs
        List<WebElement> topSongsListLeftFour = topSongsItemsList.findElements(By.id("songBox"));

        WebElement songOneLeft = topSongsListLeftFour.get(0);
        String songOneNameLeft = songOneLeft.getText();
        System.out.println("songOneNameLeft: " + songOneNameLeft);
        assertTrue(songOneNameLeft.equals("As It Was"));
        // check it is the same as first song one
        assertTrue(songOneNameLeft.equals(songOneName));

        WebElement songTwoLeft = topSongsListLeftFour.get(1);
        String songTwoNameLeft = songTwoLeft.getText();
        System.out.println("songTwoNameLeft: " + songTwoNameLeft);
        assertTrue(songTwoNameLeft.equals("Nothing New (feat. Phoebe Bridgers) (Taylor’s Version) (From The Vault)"));
        // check it is the same as first song two
        assertTrue(songTwoNameLeft.equals(songTwoName));

        WebElement songThreeLeft = topSongsListLeftFour.get(2);
        String songThreeNameLeft = songThreeLeft.getText();
        System.out.println("songThreeNameLeft: " + songThreeNameLeft);
        assertTrue(songThreeNameLeft.equals("It Never Rains in Southern California"));
        // check it is the same as first song three
        assertTrue(songThreeNameLeft.equals(songThreeName));

        WebElement songFourLeft = topSongsListLeftFour.get(3);
        String songFourNameLeft = songFourLeft.getText();
        System.out.println("songFourNameLeft: " + songFourNameLeft);
        assertTrue(songFourNameLeft.equals("Dead of Night"));
        // check it is the same as first song four
        assertTrue(songFourNameLeft.equals(songFourName));

        teardownHelper();
    }


    /**
     * Test the UI of Dashboard sections
     */
    public void testDashboard() throws InterruptedException {
        setupHelper();
        loginHelper();
        Thread.sleep(10000); // use sleep to prevent stale elements

        WebElement root2 = driver.findElement(By.id("root"));
        WebElement sideBar = root2.findElement(By.id("sideNavBar"));
        WebElement menu = sideBar.findElement(By.id("accountMenu"));
        WebElement menuOptions = menu.findElement(By.id("menu-options"));

        // navigate to dashboard from home page
        WebElement searchSongButton = menuOptions.findElements(By.tagName("a")).get(1);
        searchSongButton.click();
        Thread.sleep(10000); // use sleep to prevent stale elements

        // find some recommendations
        WebElement root3 = driver.findElement(By.id("root"));
        WebElement mainWindow = root3.findElement(By.id("mainWindow"));
        WebElement mainDiv = mainWindow.findElement(By.id("mainDiv"));
        WebElement songRecsBar = mainDiv.findElement(By.id("findSongRecsBar"));
        WebElement findSongRecBarPart = songRecsBar.findElement(By.id("findSongRecsBarHeader"));
        WebElement findSongRecBarPartText = findSongRecBarPart.findElement(By.tagName("h3"));
        System.out.println("findSongRecBarPartText.getText(): " + findSongRecBarPartText.getText());
        assertTrue(findSongRecBarPartText.getText().equals("Find Song Recommendations"));

        // top recommendations
        WebElement searchResultsFindSongDiv = mainDiv.findElement(By.id("searchResultsFindSongDiv"));
        WebElement topRecsBar = searchResultsFindSongDiv.findElement(By.id("topRecsBar"));
        WebElement topRecsBarHeader = topRecsBar.findElement(By.id("topRecsBarHeader"));
        WebElement topRecsBarPartText = topRecsBarHeader.findElement(By.tagName("h3"));
        System.out.println("topRecsBarPartText.getText(): " + topRecsBarPartText.getText());
        assertTrue(topRecsBarPartText.getText().equals("Top Recommendations"));

        // make recommendations
        WebElement topRecsAndSearchBarDiv = searchResultsFindSongDiv.findElement(By.id("topRecsAndSearchBarDiv"));
        WebElement makeRecsBar = topRecsAndSearchBarDiv.findElement(By.id("makeRecsBar"));
        WebElement makeRecsBarHeader = makeRecsBar.findElement(By.id("makeRecsBarHeader"));
        WebElement makeRecsBarHeaderText = makeRecsBarHeader.findElement(By.tagName("h3"));
        System.out.println("makeRecsBarHeaderText.getText(): " + makeRecsBarHeaderText.getText());
        assertTrue(makeRecsBarHeaderText.getText().equals("Make Recommendations"));

        // lyrics
        WebElement lyricsBarDiv = searchResultsFindSongDiv.findElement(By.id("lyricsBarDiv"));
        WebElement lyricsBar = lyricsBarDiv.findElement(By.id("lyricsBar"));
        WebElement lyricsBarHeader = lyricsBar.findElement(By.id("lyricsBarHeader"));
        WebElement lyricsBarHeaderText = lyricsBarHeader.findElement(By.tagName("h3"));
        System.out.println("lyricsBarHeaderText.getText(): " + lyricsBarHeaderText.getText());
        assertTrue(lyricsBarHeaderText.getText().equals("Lyrics"));

        teardownHelper();

    }

    /**
     * TEST TOP SONGS RANKINGS AND THAT THEY CHANGE
     * Tests that the order of top songs changes when user adds recs to song that has top recs.
     * Song being tested: Feel So Close - Radio Edit by Calvin Harris
     * At start: user has no recs, the top songs are 1) I love it, 2) Cool for Summer, 3) Congratulations
     * User recommends a) Congratulations and b) First Class
     * Top Songs should update to be --> 1) I love it, 2) Congratulations, 3) First Class
     * User deletes their recommendations, top songs should update again
     * Top songs should go back to 1) I love it, 2) Cool for Summer, 3) Congratulations
     * It is important to delete songs also because test cannot be run again without manually
     * logging into account and deleting the recs if the recommendations that
     * the users starts with are not the same as it ends with .
     *
     * @throws InterruptedException
     */
    public void testSongRecRankings() throws InterruptedException {
        setupHelper();
        loginHelper();
        Thread.sleep(10000); // use sleep to prevent stale elements

        WebElement root2 = driver.findElement(By.id("root"));
        WebElement sideBar = root2.findElement(By.id("sideNavBar"));
        WebElement menu = sideBar.findElement(By.id("accountMenu"));
        WebElement menuOptions = menu.findElement(By.id("menu-options"));

        // navigate to dashboard from home page
        WebElement searchSongButton = menuOptions.findElements(By.tagName("a")).get(1);
        searchSongButton.click();
        Thread.sleep(10000); // use sleep to prevent stale elements

        WebElement root3 = driver.findElement(By.id("root"));
        WebElement mainWindow = root3.findElement(By.id("mainWindow"));
        WebElement mainDiv = mainWindow.findElement(By.id("mainDiv"));
        WebElement searchBarDiv = mainDiv.findElement(By.id("searchBarDiv"));
        WebElement searchBarInput = searchBarDiv.findElement(By.tagName("input"));
        WebElement findSongsDiv = mainDiv.findElement(By.id("searchResultsFindSongDiv"));

        WebElement topSongsAndSearchDiv = findSongsDiv.findElement(By.id("topRecsAndSearchBarDiv"));
        WebElement addingSongDiv = topSongsAndSearchDiv.findElement(By.id("addingSong"));
        WebElement searchRecsInput = addingSongDiv.findElement(By.tagName("input"));


        // search for song using "feel so close"
        searchBarInput.sendKeys("feel so close");
        List<WebElement> songResults = findSongsDiv.findElements(By.id("songResult"));
        WebElement firstSongResult = songResults.get(0).findElement(By.id("songInfo"));
        WebElement firstSongName = firstSongResult.findElement(By.id("songName"));
        WebElement firstSongArtist = firstSongResult.findElement(By.id("songArtist"));

        // check that the first song result is "Feel So Close" by "Calvin Harris"
        System.out.println("first song result name: " + firstSongName.getText());
        assertTrue(firstSongName.getText().equals("Feel So Close - Radio Edit"));

        System.out.println("first song result artist: " +firstSongArtist.getText());
        assertTrue(firstSongArtist.getText().equals("Calvin Harris"));

        // press on Feel So Close song to start playing
        firstSongResult.click();
        WebElement playerBox = sideBar.findElement(By.id("Player-box"));
        WebElement playerPlayButton = playerBox.findElement(By.tagName("button"));
        playerPlayButton.click(); // start player
        Thread.sleep(3000); // use sleep to prevent stale elements

        WebElement root4 = driver.findElement(By.id("root"));
        WebElement mainWindow2 = root4.findElement(By.id("mainWindow"));
        WebElement mainDiv2 = mainWindow2.findElement(By.id("mainDiv"));
        WebElement findSongsDiv2 = mainDiv2.findElement(By.id("searchResultsFindSongDiv"));
        WebElement topSongsAndSearchDiv2 = findSongsDiv2.findElement(By.id("topRecsAndSearchBarDiv"));
        WebElement addingSongDiv2 = topSongsAndSearchDiv2.findElement(By.id("addingSong"));
        WebElement searchRecsInput2 = addingSongDiv2.findElement(By.tagName("input"));


        // check that Feel So Close - Radio Edit started playing and screen is updated with top songs and recs

        // get the name and artist of what is currently playing (As it Was)
        WebElement playingAndTopRecs = findSongsDiv.findElement(By.id("playing-and-top-recs"));
        WebElement currentlyPlaying = playingAndTopRecs.findElement(By.id("currently-playing"));

        // check image source, should be 	https://i.scdn.co/image/ab67616d0000b273dcef905cb144d4867119850b
        WebElement playingTrackImage = currentlyPlaying.findElement(By.tagName("img"));
        System.out.println("playing song img src: " + playingTrackImage.getAttribute("src"));
        assertTrue(playingTrackImage.getAttribute("src").equals("https://i.scdn.co/image/ab67616d0000b273dcef905cb144d4867119850b"));

        // get name and artist
        WebElement playingTrackInfoList = currentlyPlaying.findElement(By.tagName("ul"));
        WebElement playingTrackInfoListItem = playingTrackInfoList.findElement(By.tagName("li"));
        WebElement playingTrackInfoListItemTextDiv = playingTrackInfoListItem.findElement(By.tagName("div"));
        WebElement playingTrackSongName = playingTrackInfoListItemTextDiv.findElement(By.tagName("span"));
        WebElement playingTrackSongArtist = playingTrackInfoListItemTextDiv.findElement(By.tagName("p"));

        // check song name is correct
        System.out.println("playing track song name: "+ playingTrackSongName.getText());
        assertTrue(playingTrackSongName.getText().equals("Feel So Close - Radio Edit"));

        // check song artist is correct
        System.out.println("playing track artist: " + playingTrackSongArtist.getText());
        assertTrue(playingTrackSongArtist.getText().equals("Calvin Harris"));

        // now check top three songs: should be I Love It, Cool For the Summer, and Congratulations
        WebElement topSongsListDiv = playingAndTopRecs.findElement(By.id("topSongsList"));
        WebElement topSongsList = topSongsListDiv.findElement(By.tagName("ul"));

        // get list of top song items
        List<WebElement> threeTopSongs = topSongsList.findElements(By.tagName("li"));

        // get song # 1 info (in list for, first div = play icon, second div = number icon, third div = name and artist
        List<WebElement> firstTopSong = threeTopSongs.get(0).findElements(By.tagName("div"));

        // get 2nd element, song name and artist info
        WebElement firstTopSongName = firstTopSong.get(2).findElement(By.tagName("span"));
        WebElement firstTopSongArtist = firstTopSong.get(2).findElement(By.tagName("p"));

        // check top song #1 name and artist
        System.out.println("firstTopSongName: " + firstTopSongName.getText());
        assertTrue(firstTopSongName.getText().equals("I Love It (feat. Charli XCX)"));

        System.out.println("firstTopSongArtist: " + firstTopSongArtist.getText());
        assertTrue(firstTopSongArtist.getText().equals("Icona Pop"));

        // get song # 2  info (in list for, first div = play icon, second div = number icon, third div = name and artist
        List<WebElement> secondTopSong = threeTopSongs.get(1).findElements(By.tagName("div"));

        // get 2nd element, song name and artist info
        WebElement secondTopSongName = secondTopSong.get(2).findElement(By.tagName("span"));
        WebElement secondTopSongArtist = secondTopSong.get(2).findElement(By.tagName("p"));

        // check top song #2 name and artist
        System.out.println("secondTopSongName: " + secondTopSongName.getText());
        assertTrue(secondTopSongName.getText().equals("Cool for the Summer"));

        System.out.println("secondTopSongArtist: " + secondTopSongArtist.getText());
        assertTrue(secondTopSongArtist.getText().equals("Demi Lovato"));

        // get song # 3 info (in list for, first div = play icon, second div = number icon, third div = name and artist
        List<WebElement> thirdTopSong = threeTopSongs.get(2).findElements(By.tagName("div"));

        // get 2nd element, song name and artist info
        WebElement thirdTopSongName = thirdTopSong.get(2).findElement(By.tagName("span"));
        WebElement thirdTopSongArtist = thirdTopSong.get(2).findElement(By.tagName("p"));

        // check top song #3 name and artist
        System.out.println("thirdTopSongName: " + thirdTopSongName.getText());
        assertTrue(thirdTopSongName.getText().equals("Congratulations"));

        System.out.println("thirdTopSongArtist: " + thirdTopSongArtist.getText());
        assertTrue(thirdTopSongArtist.getText().equals("Post Malone"));

        // add congratulations as one of your recs
        searchRecsInput2.sendKeys("congratu");
        List<WebElement> songRecResults = findSongsDiv.findElements(By.id("songResult"));
        WebElement firstSongRecResult = songRecResults.get(0).findElement(By.id("songInfo"));
        WebElement firstSongRecName = firstSongRecResult.findElement(By.id("songName"));
        WebElement firstSongRecArtist = firstSongRecResult.findElement(By.id("songArtist"));

        // check that the first song result is "Congratulations" by "Post Maloen"
        System.out.println("first song result name: " + firstSongRecName.getText());
        assertTrue(firstSongRecName.getText().equals("Congratulations"));

        System.out.println("first song result artist: " + firstSongRecArtist.getText());
        assertTrue(firstSongRecArtist.getText().equals("Post Malone"));

        // press on Congratulations to add to your recommendations
        firstSongRecResult.click();

        Thread.sleep(5000);

        // search for "First Class"
        searchRecsInput2.sendKeys("first class");
        List<WebElement> songRecResults2 = findSongsDiv.findElements(By.id("songResult"));
        WebElement firstSongRecResult2 = songRecResults2.get(0).findElement(By.id("songInfo"));
        WebElement firstSongRecName2 = firstSongRecResult2.findElement(By.id("songName"));
        WebElement firstSongRecArtist2 = firstSongRecResult2.findElement(By.id("songArtist"));

        // check that the first song result is "First Class" by "Jack Harlow"
        System.out.println("first song result name: " + firstSongRecName2.getText());
        assertTrue(firstSongRecName2.getText().equals("First Class"));

        System.out.println("first song result artist: " + firstSongRecArtist2.getText());
        assertTrue(firstSongRecArtist2.getText().equals("Jack Harlow"));

        // press on First Class to add to your recommendations
        firstSongRecResult2.click();

        Thread.sleep(5000);

        // check to see rankings changed from I Love It, Cool For the Summer, and Congratulations
        // to I Love It, Congratulations, and First Class
        // need to get top songs list
        WebElement topSongsListDiv2 = playingAndTopRecs.findElement(By.id("topSongsList"));
        WebElement topSongsList2 = topSongsListDiv2.findElement(By.tagName("ul"));

        // get list of top song items
        List<WebElement> threeTopSongs2 = topSongsList2.findElements(By.tagName("li"));

        // get song # 1 info (in list for, first div = play icon, second div = number icon, third div = name and artist
        List<WebElement> firstTopSong2 = threeTopSongs2.get(0).findElements(By.tagName("div"));

        // get 2nd element, song name and artist info
        WebElement firstTopSongName2 = firstTopSong2.get(2).findElement(By.tagName("span"));
        WebElement firstTopSongArtist2 = firstTopSong2.get(2).findElement(By.tagName("p"));

        // check top song #1 name and artist
        System.out.println("firstTopSongName2: " + firstTopSongName2.getText());
        assertTrue(firstTopSongName2.getText().equals("I Love It (feat. Charli XCX)"));

        System.out.println("firstTopSongArtist2: " + firstTopSongArtist2.getText());
        assertTrue(firstTopSongArtist2.getText().equals("Icona Pop"));

        // get song # 2 info (in list for, first div = play icon, second div = number icon, third div = name and artist
        List<WebElement> secondTopSong2 = threeTopSongs2.get(1).findElements(By.tagName("div"));

        // get 2nd element, song name and artist info
        WebElement secondTopSongName2 = secondTopSong2.get(2).findElement(By.tagName("span"));
        WebElement secondTopSongArtist2 = secondTopSong2.get(2).findElement(By.tagName("p"));

        // check top song #2 name and artist
        System.out.println("secondTopSongName2: " + secondTopSongName2.getText());
        assertTrue(secondTopSongName2.getText().equals("Congratulations"));

        System.out.println("secondTopSongArtist2: " + secondTopSongArtist2.getText());
        assertTrue(secondTopSongArtist2.getText().equals("Post Malone"));

        // get song # 3 info (in list for, first div = play icon, second div = number icon, third div = name and artist
        List<WebElement> thirdTopSong2 = threeTopSongs2.get(2).findElements(By.tagName("div"));

        // get 2nd element, song name and artist info
        WebElement thirdTopSongName2 = thirdTopSong2.get(2).findElement(By.tagName("span"));
        WebElement thirdTopSongArtist2 = thirdTopSong2.get(2).findElement(By.tagName("p"));

        // check top song #3 name and artist
        System.out.println("thirdTopSongName2: " + thirdTopSongName2.getText());
        assertTrue(thirdTopSongName2.getText().equals("First Class"));

        System.out.println("thirdTopSongArtist2: " + thirdTopSongArtist2.getText());
        assertTrue(thirdTopSongArtist2.getText().equals("Jack Harlow"));

        // check that Congratulations and First Class were added to users rec list under
        // 'My Recommendations' and they can be deleted
        WebElement recsList = topSongsAndSearchDiv2.findElement(By.id("recsList"));
        WebElement songRecsList = recsList.findElement(By.tagName("ul"));

        // get list of recs
        List<WebElement> threeRecSongs = songRecsList.findElements(By.tagName("li"));

        // get rec song # 1 info (in list for, first div = play icon, second div = number icon, third div = name and artist
        List<WebElement> firstRecSong = threeRecSongs.get(0).findElements(By.tagName("div"));

        // get 2nd element, song name and artist info
        WebElement firstRecSongName = firstRecSong.get(2).findElement(By.tagName("span"));
        WebElement firstRecSongArtist = firstRecSong.get(2).findElement(By.tagName("p"));

        // first rec should be Congratulations
        System.out.println("firstRecSongName: " + firstRecSongName.getText());
        assertTrue(firstRecSongName.getText().equals("Congratulations"));

        System.out.println("firstRecSongArtist: " + firstRecSongArtist.getText());
        assertTrue(firstRecSongArtist.getText().equals("Post Malone"));

        // get rec song # 2 info (in list for, first div = play icon, second div = number icon, third div = name and artist
        List<WebElement> secondRecSong = threeRecSongs.get(1).findElements(By.tagName("div"));

        // get 2nd element, song name and artist info
        WebElement secondRecSongName = secondRecSong.get(2).findElement(By.tagName("span"));
        WebElement secondRecSongArtist = secondRecSong.get(2).findElement(By.tagName("p"));

        // second rec should be First Class
        System.out.println("secondRecSongName: " + secondRecSongName.getText());
        assertTrue(secondRecSongName.getText().equals("First Class"));

        System.out.println("secondRecSongArtist: " + secondRecSongArtist.getText());
        assertTrue(secondRecSongArtist.getText().equals("Jack Harlow"));

        // ****** Check you can Delete Recommendation ******
        // delete First Class from your recommendations
        // need to get icons, first is play song, second is add to playlist
        WebElement songRecsButtonsDiv = threeRecSongs.get(1).findElement(By.id("songRecsButtons"));
        WebElement deleteRecButton = songRecsButtonsDiv.findElement(By.id("deleteRecButton"));

        // delete Congratulations song from your recommendations
        // need to get icons, first is play song, second is add to playlist
        WebElement songRecsButtonsDiv2 = threeRecSongs.get(0).findElement(By.id("songRecsButtons"));
        WebElement deleteRecButton2 = songRecsButtonsDiv2.findElement(By.id("deleteRecButton"));

        // delete songs
        deleteRecButton.click();
        deleteRecButton2.click();

        Thread.sleep(3000);

        // check rankings changed after deletion: should go back to -->
        // 1: I love it, 2: cool for the summer, 3: congratulations
        WebElement topSongsListDiv3 = playingAndTopRecs.findElement(By.id("topSongsList"));
        WebElement topSongsList3 = topSongsListDiv3.findElement(By.tagName("ul"));

        // get list of song items
        List<WebElement> threeTopSongs3 = topSongsList3.findElements(By.tagName("li"));

        // get song # 1 info (in list for, first div = play icon, second div = number icon, third div = name and artist
        List<WebElement> firstTopSong3 = threeTopSongs3.get(0).findElements(By.tagName("div"));

        // get 2nd element, song name and artist info
        WebElement firstTopSongName3 = firstTopSong3.get(2).findElement(By.tagName("span"));
        WebElement firstTopSongArtist3 = firstTopSong3.get(2).findElement(By.tagName("p"));

        // check top song #1 name and artist
        System.out.println("firstTopSongName3: " + firstTopSongName3.getText());
        assertTrue(firstTopSongName3.getText().equals("I Love It (feat. Charli XCX)"));

        System.out.println("firstTopSongArtist3: " + firstTopSongArtist3.getText());
        assertTrue(firstTopSongArtist3.getText().equals("Icona Pop"));

        // get song # 2 info (in list for, first div = play icon, second div = number icon, third div = name and artist
        List<WebElement> secondTopSong3 = threeTopSongs3.get(1).findElements(By.tagName("div"));

        // get 2nd element, song name and artist info
        WebElement secondTopSongName3 = secondTopSong3.get(2).findElement(By.tagName("span"));
        WebElement secondTopSongArtist3 = secondTopSong3.get(2).findElement(By.tagName("p"));

        // check top song #2 name and artist
        System.out.println("secondTopSongName3: " + secondTopSongName3.getText());
        assertTrue(secondTopSongName3.getText().equals("Cool for the Summer"));

        System.out.println("secondTopSongArtist3: " + secondTopSongArtist3.getText());
        assertTrue(secondTopSongArtist3.getText().equals("Demi Lovato"));

        // get song # 3 info (in list for, first div = play icon, second div = number icon, third div = name and artist
        List<WebElement> thirdTopSong3 = threeTopSongs3.get(2).findElements(By.tagName("div"));

        // get 2nd element, song name and artist info
        WebElement thirdTopSongName3 = thirdTopSong3.get(2).findElement(By.tagName("span"));
        WebElement thirdTopSongArtist3 = thirdTopSong3.get(2).findElement(By.tagName("p"));

        // check top song #3 name and artist
        System.out.println("thirdTopSongName3: " + thirdTopSongName3.getText());
        assertTrue(thirdTopSongName3.getText().equals("Congratulations"));

        System.out.println("thirdTopSongArtist3: " + thirdTopSongArtist3.getText());
        assertTrue(thirdTopSongArtist3.getText().equals("Post Malone"));

        Thread.sleep(1000);

        teardownHelper();
    }

    /**
     * Tests that the 'My Recommendations' button in the side-bar brings you to correct page
     * with the correct information loaded.
     * @throws InterruptedException
     */
    public void testMyRecsButton() throws InterruptedException {
        setupHelper();
        loginHelper();
        Thread.sleep(10000); // use sleep to prevent stale elements

        WebElement root2 = driver.findElement(By.id("root"));
        WebElement sideBar = root2.findElement(By.id("sideNavBar"));
        WebElement menu = sideBar.findElement(By.id("accountMenu"));
        WebElement menuOptions = menu.findElement(By.id("menu-options"));
        WebElement findFriendsButton = menuOptions.findElements(By.tagName("a")).get(3);

        findFriendsButton.click();

        Thread.sleep(1000); // use sleep to prevent stale elements
        WebElement root3 = driver.findElement(By.id("root"));
        WebElement mainWindow = root3.findElement(By.id("mainWindow"));
        WebElement recsHeader = mainWindow.findElement(By.id("recsHeader"));

        System.out.println(recsHeader.getText());
        assertTrue(recsHeader.getText().equals("My Recommendations"));

        List<WebElement> recInputNames = mainWindow.findElements(By.id("recInput"));

        WebElement firstInput = recInputNames.get(0);
        System.out.println(firstInput.findElement(By.id("recInputTitle")).getText());
        assertTrue(firstInput.findElement(By.id("recInputTitle")).getText().equals("Find users whose music taste is..."));

        WebElement secondInput = recInputNames.get(1);
        System.out.println(secondInput.findElement(By.id("recInputTitle")).getText());
        assertTrue(secondInput.findElement(By.id("recInputTitle")).getText().equals("Importance of songs:"));

        WebElement thirdInput = recInputNames.get(2);
        System.out.println(thirdInput.findElement(By.id("recInputTitle")).getText());
        assertTrue(thirdInput.findElement(By.id("recInputTitle")).getText().equals("Importance of artists:"));

        WebElement fourthInput = recInputNames.get(3);
        System.out.println(fourthInput.findElement(By.id("recInputTitle")).getText());
        assertTrue(fourthInput.findElement(By.id("recInputTitle")).getText().equals("Importance of genres:"));


        teardownHelper();
    }

    /**
     * This tests the functionality of the top artists bar on the homepage
     * It tests that the right top artists for the user are displayed,
     * and using the right and left side-bars displays
     * new artists and goes back to old ones.
     *
     * NOTE: run this test in full screen, or the left side-bar for top artists can't be clicked as it
     * is too close to the side nav bar when the screen is compressed
     * @throws InterruptedException
     */
    public void testHomePageTopArtists() throws InterruptedException {
        setupHelper();
        loginHelper();
        Thread.sleep(10000); // use sleep to prevent stale elements

        WebElement root2 = driver.findElement(By.id("root"));
        WebElement sideNavBar = root2.findElement(By.id("sideNavBar"));

        WebElement mainWindow = root2.findElement(By.id("mainWindow"));

        WebElement topArtistsBox = mainWindow.findElement(By.id("topArtistsBox"));
        WebElement topArtistsHeaderText = topArtistsBox.findElement(By.tagName("p"));

        System.out.println(topArtistsHeaderText.getText());
        assertTrue(topArtistsHeaderText.getText().equals("Top Artists"));

        WebElement topArtistsButtonRight = topArtistsBox.findElement(By.id("songNavButtonRight"));
        WebElement topArtistsButtonLeft = topArtistsBox.findElement(By.id("songNavButtonLeft"));
        WebElement topArtistsItemsList = topArtistsBox.findElement(By.id("topArtistsList"));
        List<WebElement> topArtistsList = topArtistsItemsList.findElements(By.id("artistBox"));

        WebElement artistOne = topArtistsList.get(0);
        String artistOneName = artistOne.getText();
        System.out.println("artistOneName: " + artistOneName);
        assertTrue(artistOneName.equals("Taylor Swift"));

        WebElement artistTwo = topArtistsList.get(1);
        String artistTwoName = artistTwo.getText();
        System.out.println("artistTwoName: " + artistTwo);
        assertTrue(artistTwoName.equals("Jack Johnson"));

        WebElement artistThree = topArtistsList.get(2);
        String artistThreeName = artistThree.getText();
        System.out.println("artistThreeName: " + artistThreeName);
        assertTrue(artistThreeName.equals("Billie Eilish"));

        WebElement artistFour = topArtistsList.get(3);
        String artistFourName = artistFour.getText();
        System.out.println("artistFourName: " + artistFourName);
        assertTrue(artistFourName.equals("Lorde"));

        // press the right side bar button to see new artist
        topArtistsButtonRight.click();

        // get new list of artists
        List<WebElement> topArtistsListRightOne = topArtistsItemsList.findElements(By.id("artistBox"));

        // check artists have been shifted one and there is a new artist
        WebElement artistFive = topArtistsListRightOne.get(0);
        System.out.println("artistFiveName: " + artistFive.getText());
        assertTrue(artistFive.getText().equals("Jack Johnson"));

        WebElement artistSix = topArtistsListRightOne.get(1);
        System.out.println("artistSixName: " + artistSix.getText());
        assertTrue(artistSix.getText().equals("Billie Eilish"));

        WebElement artistSeven = topArtistsListRightOne.get(2);
        System.out.println("artistSevenName: " + artistSeven.getText());
        assertTrue(artistSeven.getText().equals("Lorde"));

        WebElement artistEight = topArtistsListRightOne.get(3);
        System.out.println("artistEightName: " + artistEight.getText());
        assertTrue(artistEight.getText().equals("Olivia Rodrigo"));

        // press the right sidebar button 3 more times, the new four should be
        // completely different then first four and there should have been 8 artists total
        topArtistsButtonRight.click();
        topArtistsButtonRight.click();
        topArtistsButtonRight.click();

        Thread.sleep(3000); // use sleep to prevent stale elements

        // get new list of artists
        List<WebElement> topArtistsListRightFour = topArtistsItemsList.findElements(By.id("artistBox"));

        // check artists have been shifted one and there is a new artist
        WebElement artistNine = topArtistsListRightFour.get(0);
        System.out.println("artistNineName: " + artistNine.getText());
        assertTrue(artistNine.getText().equals("Olivia Rodrigo"));

        // artist in the first slot, should not equal the first artist in first slot
        assertFalse(artistNine.getText().equals(artistOneName));

        WebElement artistTen = topArtistsListRightFour.get(1);
        System.out.println("artistTenName: " + artistTen.getText());
        assertTrue(artistTen.getText().equals("Lana Del Rey"));

        // artist in the second slot, should not equal the first artist in second slot
        assertFalse(artistTen.getText().equals(artistTwoName));

        WebElement artistEleven = topArtistsListRightFour.get(2);
        System.out.println("artistElevenName: " + artistEleven.getText());
        assertTrue(artistEleven.getText().equals("Minnz Piano"));

        // artist in the third slot, should not equal the first artist in third slot
        assertFalse(artistEleven.getText().equals(artistThreeName));

        WebElement artistTwelve = topArtistsListRightFour.get(3);
        System.out.println("artistTwelveName: " + artistTwelve.getText());
        assertTrue(artistTwelve.getText().equals("Norah Jones"));

        // artist in the fourth slot, should not equal the first artist in fourth slot
        assertFalse(artistTwelve.getText().equals(artistFourName));

        // now check that none of the artists from first four are equal to last four
        assertFalse(artistNine.getText().equals(artistTwoName));
        assertFalse(artistNine.getText().equals(artistThreeName));
        assertFalse(artistNine.getText().equals(artistFourName));

        assertFalse(artistTen.getText().equals(artistThreeName));
        assertFalse(artistTen.getText().equals(artistFourName));
        assertFalse(artistTen.getText().equals(artistOneName));

        assertFalse(artistEleven.getText().equals(artistFourName));
        assertFalse(artistEleven.getText().equals(artistOneName));
        assertFalse(artistEleven.getText().equals(artistTwoName));

        assertFalse(artistTwelve.getText().equals(artistOneName));
        assertFalse(artistTwelve.getText().equals(artistTwoName));
        assertFalse(artistTwelve.getText().equals(artistThreeName));


        // test opposite direction
        // press the left sidebar button 4  times, the new four should be
        // the same as the first four
        topArtistsButtonLeft.click();
        topArtistsButtonLeft.click();
        topArtistsButtonLeft.click();
        topArtistsButtonLeft.click();

        Thread.sleep(3000); // use sleep to prevent stale elements

        // get new list of artists
        List<WebElement> topArtistsListLeftFour = topArtistsItemsList.findElements(By.id("artistBox"));

        WebElement artistOneLeft = topArtistsListLeftFour.get(0);
        String artistOneNameLeft = artistOneLeft.getText();
        System.out.println("artistOneNameLeft: " + artistOneNameLeft);
        assertTrue(artistOneNameLeft.equals("Taylor Swift"));
        // check it is the same as first artist one
        assertTrue(artistOneNameLeft.equals(artistOneName));

        WebElement artistTwoLeft = topArtistsListLeftFour.get(1);
        String artistTwoNameLeft = artistTwoLeft.getText();
        System.out.println("artistTwoNameLeft: " + artistTwoNameLeft);
        assertTrue(artistTwoNameLeft.equals("Jack Johnson"));
        // check it is the same as first artist two
        assertTrue(artistTwoNameLeft.equals(artistTwoName));

        WebElement artistThreeLeft = topArtistsListLeftFour.get(2);
        String artistThreeNameLeft = artistThreeLeft.getText();
        System.out.println("artistThreeNameLeft: " + artistThreeNameLeft);
        assertTrue(artistThreeNameLeft.equals("Billie Eilish"));
        // check it is the same as first artist three
        assertTrue(artistThreeNameLeft.equals(artistThreeName));

        WebElement artistFourLeft = topArtistsListLeftFour.get(3);
        String artistFourNameLeft = artistFourLeft.getText();
        System.out.println("artistFourNameLeft: " + artistFourNameLeft);
        assertTrue(artistFourNameLeft.equals("Lorde"));
        // check it is the same as first artist four
        assertTrue(artistFourNameLeft.equals(artistFourName));

        teardownHelper();
    }

    /**
     * Tests that the Settings button in the side bar brings you to correct page.
     * @throws InterruptedException
     */
    public void testSettingsButton() throws InterruptedException {
        setupHelper();
        loginHelper();
        Thread.sleep(10000); // use sleep to prevent stale elements

        WebElement root2 = driver.findElement(By.id("root"));
        WebElement sideBar = root2.findElement(By.id("sideNavBar"));
        WebElement menu = sideBar.findElement(By.id("accountMenu"));
        WebElement menuOptions = menu.findElement(By.id("menu-options"));
        WebElement settingsButton = menuOptions.findElements(By.tagName("a")).get(5);

        settingsButton.click();

        Thread.sleep(1000); // use sleep to prevent stale elements
        WebElement root3 = driver.findElement(By.id("root"));
        WebElement emptyBackground = root3.findElement(By.id("emptyBackground"));
        WebElement settingsHomeButton = emptyBackground.findElement(By.tagName("Button"));

        System.out.println(settingsHomeButton.getText());
        assertTrue(settingsHomeButton.getText().equals("SETTINGS"));
        teardownHelper();
    }

    /**
     * Tests that the Sign Out button in the side bar brings you to correct page.
     * @throws InterruptedException
     */
    public void testSignOutButton() throws InterruptedException {
        setupHelper();
        loginHelper();
        Thread.sleep(10000); // use sleep to prevent stale elements

        WebElement root2 = driver.findElement(By.id("root"));
        WebElement sideBar = root2.findElement(By.id("sideNavBar"));
        WebElement menu = sideBar.findElement(By.id("accountMenu"));
        WebElement menuOptions = menu.findElement(By.id("menu-options"));
        WebElement signOutButton = menuOptions.findElements(By.tagName("a")).get(6);

        signOutButton.click();

        Thread.sleep(1000); // use sleep to prevent stale elements
        WebElement root3 = driver.findElement(By.id("root"));
        WebElement emptyBackground = root3.findElement(By.id("emptyBackground"));
        WebElement settingsHomeButton = emptyBackground.findElement(By.tagName("Button"));

        System.out.println(settingsHomeButton.getText());
        assertTrue(settingsHomeButton.getText().equals("SIGN OUT"));
        teardownHelper();
    }

    /**
     * Tests that the FriendsList button in the side bar brings you to correct page.
     * @throws InterruptedException
     */
    public void testFriendsListButton() throws InterruptedException {
        setupHelper();
        loginHelper();
        Thread.sleep(10000); // use sleep to prevent stale elements

        WebElement root2 = driver.findElement(By.id("root"));
        WebElement sideBar = root2.findElement(By.id("sideNavBar"));
        WebElement menu = sideBar.findElement(By.id("accountMenu"));
        WebElement menuOptions = menu.findElement(By.id("menu-options"));
        WebElement friendsListButton = menuOptions.findElements(By.tagName("a")).get(4);

        friendsListButton.click();

        Thread.sleep(1000); // use sleep to prevent stale elements
        WebElement root3 = driver.findElement(By.id("root"));
        WebElement emptyBackground = root3.findElement(By.id("emptyBackground"));
        WebElement settingsHomeButton = emptyBackground.findElement(By.tagName("Button"));

        System.out.println(settingsHomeButton.getText());
        assertTrue(settingsHomeButton.getText().equals("MY FRIENDS"));
        teardownHelper();
    }

    // testing without specific song / artist names
    // this works best for the future in case users top songs and artists have changed

    /**
     * *** THIS IS DIFFERENT FROM testHomePagePlayer()
     *
     * This test doesn't check the values of the data, because top songs can change
     * over time for a user. This just checks that the songs stay the same
     * after using the left and right buttons and that the songs can be played.
     *
     * Ideal for testing in the future.
     *
     * This tests the functionality of the song player on the homepage
     * It tests that clicking a song will play it, and using the right and left side-bars
     * displays new songs and goes back to old ones.
     *
     *
     * NOTE: run this test in full screen, or the left side-bar for top songs can't be clicked as it
     * is too close to the side nav bar when the screen is compressed
     * @throws InterruptedException
     */
    public void testHomePagePlayerNoNames() throws InterruptedException {
        setupHelper();
        loginHelper();
        Thread.sleep(10000); // use sleep to prevent stale elements

        WebElement root2 = driver.findElement(By.id("root"));
        WebElement sideNavBar = root2.findElement(By.id("sideNavBar"));

        WebElement mainWindow = root2.findElement(By.id("mainWindow"));

        WebElement topSongsBox = mainWindow.findElement(By.id("topSongsBox"));
        WebElement topSongsText = topSongsBox.findElement(By.tagName("p"));

        System.out.println(topSongsText.getText());
        assertTrue(topSongsText.getText().equals("Top Songs"));

        WebElement topSongsButtonRight = topSongsBox.findElement(By.id("songNavButtonRight"));
        WebElement topSongsButtonLeft = topSongsBox.findElement(By.id("songNavButtonLeft"));
        WebElement topSongsItemsList = topSongsBox.findElement(By.id("topSongsList"));
        List<WebElement> topSongsList = topSongsItemsList.findElements(By.id("songBox"));

        WebElement songOne = topSongsList.get(0);
        String songOneName = songOne.getText();
        System.out.println("songOneName: " + songOneName);

        WebElement songTwo = topSongsList.get(1);
        String songTwoName = songTwo.getText();
        System.out.println("songTwoName: " + songTwoName);

        WebElement songThree = topSongsList.get(2);
        String songThreeName = songThree.getText();
        System.out.println("songThreeName: " + songThreeName);

        WebElement songFour = topSongsList.get(3);
        String songFourName = songFour.getText();
        System.out.println("songFourName: " + songFourName);

        // play song 3: ?
        songThree.click();
        Thread.sleep(5000); // use sleep to prevent stale elements

        WebElement menu = sideNavBar.findElement(By.id("accountMenu"));
        WebElement playerBox = menu.findElement(By.id("Player-box"));
        WebElement playerPlayButton = playerBox.findElement(By.tagName("button"));
        playerPlayButton.click(); // start player
        Thread.sleep(5000); // use sleep to prevent stale elements

        // press the right side bar button to see new song
        topSongsButtonRight.click();

        // get new list of Songs
        List<WebElement> topSongsListRightOne = topSongsItemsList.findElements(By.id("songBox"));

        // check songs have been shifted one and there is a new song
        WebElement songFive = topSongsListRightOne.get(0);
        System.out.println("songFive.getText(): " + songFive.getText());

        WebElement songSix = topSongsListRightOne.get(1);
        System.out.println("songSix.getText(): " + songSix.getText());

        WebElement songSeven = topSongsListRightOne.get(2);
        System.out.println("songSeven.getText(): " + songSeven.getText());

        WebElement songEight = topSongsListRightOne.get(3);
        System.out.println("songEight.getText(): " + songEight.getText());

        // press the right sidebar button 3 more times, the new four should be
        // completely different then first four and there should have been 8 songs total
        topSongsButtonRight.click();
        topSongsButtonRight.click();
        topSongsButtonRight.click();

        Thread.sleep(10000); // use sleep to prevent stale elements

        // get new list of Songs
        List<WebElement> topSongsListRightFour = topSongsItemsList.findElements(By.id("songBox"));

        // check songs have been shifted one and there is a new song
        WebElement songNine = topSongsListRightFour.get(0);
        System.out.println("songNine.getText(): " + songNine.getText());

        // song in the first slot, should not equal the first song in first slot
        assertFalse(songNine.getText().equals(songOneName));

        WebElement songTen = topSongsListRightFour.get(1);
        System.out.println("songTen.getText(): " + songTen.getText());

        // song in the second slot, should not equal the first song in second slot
        assertFalse(songTen.getText().equals(songTwoName));

        WebElement songEleven = topSongsListRightFour.get(2);
        System.out.println("songEleven.getText(): " + songEleven.getText());

        // song in the third slot, should not equal the first song in third slot
        assertFalse(songEleven.getText().equals(songThreeName));

        WebElement songTwelve = topSongsListRightFour.get(3);
        System.out.println("songTwelve.getText(): " + songTwelve.getText());

        // song in the fourth slot, should not equal the first song in fourth slot
        assertFalse(songTwelve.getText().equals(songFourName));

        // now check that none of the songs from first four are equal to last four
        assertFalse(songNine.getText().equals(songTwoName));
        assertFalse(songNine.getText().equals(songThreeName));
        assertFalse(songNine.getText().equals(songFourName));

        assertFalse(songTen.getText().equals(songThreeName));
        assertFalse(songTen.getText().equals(songFourName));
        assertFalse(songTen.getText().equals(songOneName));

        assertFalse(songEleven.getText().equals(songFourName));
        assertFalse(songEleven.getText().equals(songOneName));
        assertFalse(songEleven.getText().equals(songTwoName));

        assertFalse(songTwelve.getText().equals(songOneName));
        assertFalse(songTwelve.getText().equals(songTwoName));
        assertFalse(songTwelve.getText().equals(songThreeName));

        // play song 12: ?  to check new songs play function works
        songTwelve.click();
        Thread.sleep(5000); // use sleep to prevent stale elements

        // test opposite direction
        // press the left sidebar button 4  times, the new four should be
        // the same as the first four
        topSongsButtonLeft.click();
        topSongsButtonLeft.click();
        topSongsButtonLeft.click();
        topSongsButtonLeft.click();

        Thread.sleep(10000); // use sleep to prevent stale elements

        // get new list of Songs
        List<WebElement> topSongsListLeftFour = topSongsItemsList.findElements(By.id("songBox"));

        WebElement songOneLeft = topSongsListLeftFour.get(0);
        String songOneNameLeft = songOneLeft.getText();
        System.out.println("songOneNameLeft: " + songOneNameLeft);
        // check it is the same as first song one
        assertTrue(songOneNameLeft.equals(songOneName));

        WebElement songTwoLeft = topSongsListLeftFour.get(1);
        String songTwoNameLeft = songTwoLeft.getText();
        System.out.println("songTwoNameLeft: " + songTwoNameLeft);
        // check it is the same as first song two
        assertTrue(songTwoNameLeft.equals(songTwoName));

        WebElement songThreeLeft = topSongsListLeftFour.get(2);
        String songThreeNameLeft = songThreeLeft.getText();
        System.out.println("songThreeNameLeft: " + songThreeNameLeft);
        // check it is the same as first song three
        assertTrue(songThreeNameLeft.equals(songThreeName));

        WebElement songFourLeft = topSongsListLeftFour.get(3);
        String songFourNameLeft = songFourLeft.getText();
        System.out.println("songFourNameLeft: " + songFourNameLeft);
        // check it is the same as first song four
        assertTrue(songFourNameLeft.equals(songFourName));

        teardownHelper();
    }

    /**
     * *** THIS IS DIFFERENT FROM testHomePageTopArtists()
     *      *
     *      * This test doesn't check the values of the data, because top artists can change
     *      * over time for a user. This just checks that the artists stay the same
     *      * after using the left and right buttons and that the artists can be seen
     *      *
     *      * Ideal for testing in the future.
     *
     * This tests the functionality of the top artists bar on the homepage
     * It tests that the top artists for the user are displayed,
     * and using the right and left side-bars displays
     * new artists and goes back to old ones.
     *
     * NOTE: run this test in full screen, or the left side-bar for top artists can't be clicked as it
     * is too close to the side nav bar when the screen is compressed
     * @throws InterruptedException
     */
    public void testHomePageTopArtistsNoNames() throws InterruptedException {
        setupHelper();
        loginHelper();
        Thread.sleep(10000); // use sleep to prevent stale elements

        WebElement root2 = driver.findElement(By.id("root"));
        WebElement sideNavBar = root2.findElement(By.id("sideNavBar"));

        WebElement mainWindow = root2.findElement(By.id("mainWindow"));

        WebElement topArtistsBox = mainWindow.findElement(By.id("topArtistsBox"));
        WebElement topArtistsHeaderText = topArtistsBox.findElement(By.tagName("p"));

        System.out.println(topArtistsHeaderText.getText());
        assertTrue(topArtistsHeaderText.getText().equals("Top Artists"));

        WebElement topArtistsButtonRight = topArtistsBox.findElement(By.id("songNavButtonRight"));
        WebElement topArtistsButtonLeft = topArtistsBox.findElement(By.id("songNavButtonLeft"));
        WebElement topArtistsItemsList = topArtistsBox.findElement(By.id("topArtistsList"));
        List<WebElement> topArtistsList = topArtistsItemsList.findElements(By.id("artistBox"));

        WebElement artistOne = topArtistsList.get(0);
        String artistOneName = artistOne.getText();
        System.out.println("artistOneName: " + artistOneName);

        WebElement artistTwo = topArtistsList.get(1);
        String artistTwoName = artistTwo.getText();
        System.out.println("artistTwoName: " + artistTwo);

        WebElement artistThree = topArtistsList.get(2);
        String artistThreeName = artistThree.getText();
        System.out.println("artistThreeName: " + artistThreeName);

        WebElement artistFour = topArtistsList.get(3);
        String artistFourName = artistFour.getText();
        System.out.println("artistFourName: " + artistFourName);

        // press the right side bar button to see new artist
        topArtistsButtonRight.click();

        // get new list of artists
        List<WebElement> topArtistsListRightOne = topArtistsItemsList.findElements(By.id("artistBox"));

        // check artists have been shifted one and there is a new artist
        WebElement artistFive = topArtistsListRightOne.get(0);
        System.out.println("artistFiveName: " + artistFive.getText());

        WebElement artistSix = topArtistsListRightOne.get(1);
        System.out.println("artistSixName: " + artistSix.getText());

        WebElement artistSeven = topArtistsListRightOne.get(2);
        System.out.println("artistSevenName: " + artistSeven.getText());

        WebElement artistEight = topArtistsListRightOne.get(3);
        System.out.println("artistEightName: " + artistEight.getText());

        // press the right sidebar button 3 more times, the new four should be
        // completely different then first four and there should have been 8 artists total
        topArtistsButtonRight.click();
        topArtistsButtonRight.click();
        topArtistsButtonRight.click();

        Thread.sleep(3000); // use sleep to prevent stale elements

        // get new list of artists
        List<WebElement> topArtistsListRightFour = topArtistsItemsList.findElements(By.id("artistBox"));

        // check artists have been shifted one and there is a new artist
        WebElement artistNine = topArtistsListRightFour.get(0);
        System.out.println("artistNineName: " + artistNine.getText());

        // artist in the first slot, should not equal the first artist in first slot
        assertFalse(artistNine.getText().equals(artistOneName));

        WebElement artistTen = topArtistsListRightFour.get(1);
        System.out.println("artistTenName: " + artistTen.getText());

        // artist in the second slot, should not equal the first artist in second slot
        assertFalse(artistTen.getText().equals(artistTwoName));

        WebElement artistEleven = topArtistsListRightFour.get(2);
        System.out.println("artistElevenName: " + artistEleven.getText());

        // artist in the third slot, should not equal the first artist in third slot
        assertFalse(artistEleven.getText().equals(artistThreeName));

        WebElement artistTwelve = topArtistsListRightFour.get(3);
        System.out.println("artistTwelveName: " + artistTwelve.getText());

        // artist in the fourth slot, should not equal the first artist in fourth slot
        assertFalse(artistTwelve.getText().equals(artistFourName));

        // now check that none of the artists from first four are equal to last four
        assertFalse(artistNine.getText().equals(artistTwoName));
        assertFalse(artistNine.getText().equals(artistThreeName));
        assertFalse(artistNine.getText().equals(artistFourName));

        assertFalse(artistTen.getText().equals(artistThreeName));
        assertFalse(artistTen.getText().equals(artistFourName));
        assertFalse(artistTen.getText().equals(artistOneName));

        assertFalse(artistEleven.getText().equals(artistFourName));
        assertFalse(artistEleven.getText().equals(artistOneName));
        assertFalse(artistEleven.getText().equals(artistTwoName));

        assertFalse(artistTwelve.getText().equals(artistOneName));
        assertFalse(artistTwelve.getText().equals(artistTwoName));
        assertFalse(artistTwelve.getText().equals(artistThreeName));


        // test opposite direction
        // press the left sidebar button 4  times, the new four should be
        // the same as the first four
        topArtistsButtonLeft.click();
        topArtistsButtonLeft.click();
        topArtistsButtonLeft.click();
        topArtistsButtonLeft.click();

        Thread.sleep(3000); // use sleep to prevent stale elements

        // get new list of artists
        List<WebElement> topArtistsListLeftFour = topArtistsItemsList.findElements(By.id("artistBox"));

        WebElement artistOneLeft = topArtistsListLeftFour.get(0);
        String artistOneNameLeft = artistOneLeft.getText();
        System.out.println("artistOneNameLeft: " + artistOneNameLeft);
        // check it is the same as first artist one
        assertTrue(artistOneNameLeft.equals(artistOneName));

        WebElement artistTwoLeft = topArtistsListLeftFour.get(1);
        String artistTwoNameLeft = artistTwoLeft.getText();
        System.out.println("artistTwoNameLeft: " + artistTwoNameLeft);
        // check it is the same as first artist two
        assertTrue(artistTwoNameLeft.equals(artistTwoName));

        WebElement artistThreeLeft = topArtistsListLeftFour.get(2);
        String artistThreeNameLeft = artistThreeLeft.getText();
        System.out.println("artistThreeNameLeft: " + artistThreeNameLeft);
        // check it is the same as first artist three
        assertTrue(artistThreeNameLeft.equals(artistThreeName));

        WebElement artistFourLeft = topArtistsListLeftFour.get(3);
        String artistFourNameLeft = artistFourLeft.getText();
        System.out.println("artistFourNameLeft: " + artistFourNameLeft);
        // check it is the same as first artist four
        assertTrue(artistFourNameLeft.equals(artistFourName));

        teardownHelper();
    }
}



// can't get test to work
// inaccesible data bc of nest divs w no ids from spotify player
// save code for later
// test used to verify home screen player works when top song is pressed
// by checking currently playing song

//        WebElement playingInfoDiv = playerBox.findElement(By.className("rswp__active _InfoRSWP __mvqn38"));
//        WebElement PlayerRSWP = playerBox.findElement(By.tagName("div"));
//        List<WebElement> PlayerRSWPList = PlayerRSWP.findElements(By.tagName("div"));
//        WebElement ContentRSWP = PlayerRSWPList.get(1);
//        List<WebElement> ContentRSWP = (List<WebElement>) PlayerRSWPList.get(1);
//        WebElement ContentRSWPNextDiv = ContentRSWP.get(0); // empty div
//        WebElement RSWPACTIVE = ContentRSWPNextDiv.findElement(By.tagName("div"));
//        WebElement NNVDCQ = RSWPACTIVE.findElement(By.tagName("div")); // this instead of below option
//        WebElement playingInfoDivDiv = PlayerRSWP.findElement(By.className(" __nnvdcq __j3ulwq"));







// check currently playing song
// get the name and artist of what is currently playing (It Never Rains in Southern California)
//        List<WebElement> playingTrackInfoList = ContentRSWP.findElements(By.tagName("p"));
//        // check name
//        WebElement playingSongTitle = playingTrackInfoList.get(0).findElement(By.tagName("span"));
//        System.out.println("Song playing name: " + playingSongTitle.getText());
//        assertTrue(playingSongTitle.getText().equals("It Never Rains in Southern California"));
//        // check artist
//        WebElement playingArtist = playingTrackInfoList.get(0);
//        System.out.println("Song playing artist: " + playingArtist.getText());
//        assertTrue(playingArtist.getText().equals("Albert Hammond"));


//        // check image source, should be https://i.scdn.co/image/ab67616d000048517a26b62978e61d5068164a63
//        WebElement playingTrackImage = ContentRSWP.findElement(By.tagName("img"));
//        System.out.println("playing song img src: " + playingTrackImage.getAttribute("src"));
//        assertTrue(playingTrackImage.getAttribute("src").equals("https://i.scdn.co/image/ab67616d000048517a26b62978e61d5068164a63"));
//        // check alt description has song name
//        System.out.println("playing song alt description: " + playingTrackImage.getAccessibleName());
//        assertTrue(playingTrackImage.getAccessibleName().equals("It Never Rains in Southern California"));


// get name and artist
//        WebElement playingTrackInfoList = currentlyPlaying.findElement(By.tagName("ul"));
//        WebElement playingTrackInfoListItem = playingTrackInfoList.findElement(By.tagName("li"));
//        WebElement playingTrackInfoListItemTextDiv = playingTrackInfoListItem.findElement(By.tagName("div"));
//        WebElement playingTrackSongName = playingTrackInfoListItemTextDiv.findElement(By.tagName("span"));
//        WebElement playingTrackSongArtist = playingTrackInfoListItemTextDiv.findElement(By.tagName("p"));
//
//        // check song name is correct
//        System.out.println("playing track song name: "+ playingTrackSongName.getText());
//        assertTrue(playingTrackSongName.getText().equals("Nothing New (feat. Phoebe Bridgers) (Taylor’s Version) (From The Vault)"));
//
//        // check song artist is correct
//        System.out.println("playing track artist: " + playingTrackSongArtist.getText());
//        assertTrue(playingTrackSongArtist.getText().equals("Harry Styles"));



//        // press on As It Was song to start playing
//        firstSongResult.click();
//        WebElement playerBox = sideBar.findElement(By.id("Player-box"));
//        WebElement playerPlayButton = playerBox.findElement(By.tagName("button"));
//        playerPlayButton.click(); // start player