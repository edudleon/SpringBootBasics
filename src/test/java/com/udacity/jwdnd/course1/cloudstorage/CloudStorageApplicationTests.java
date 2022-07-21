package com.udacity.jwdnd.course1.cloudstorage;

import com.udacity.jwdnd.course1.cloudstorage.entity.Credential;
import com.udacity.jwdnd.course1.cloudstorage.entity.Note;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import java.io.File;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CloudStorageApplicationTests {

    @LocalServerPort
    private int port;

    private WebDriver driver;

    @Autowired
    private CredentialService credentialService;

    @Autowired
    private NoteService noteService;

    @BeforeAll
    static void beforeAll() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    public void beforeEach() {
        this.driver = new ChromeDriver();
    }

    @AfterEach
    public void afterEach() {
        if (this.driver != null) {
            driver.quit();
        }
    }

    @Test
    public void getLoginPage() {
        driver.get("http://localhost:" + this.port + "/login");
        Assertions.assertEquals("Login", driver.getTitle());
    }

    /**
     * PLEASE DO NOT DELETE THIS method.
     * Helper method for Udacity-supplied sanity checks.
     **/
    private void doMockSignUp(String firstName, String lastName, String userName, String password) {
        // Create a dummy account for logging in later.

        // Visit the sign-up page.
        WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
        driver.get("http://localhost:" + this.port + "/signup");
        webDriverWait.until(ExpectedConditions.titleContains("Sign Up"));

        // Fill out credentials
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputFirstName")));
        WebElement inputFirstName = driver.findElement(By.id("inputFirstName"));
        inputFirstName.click();
        inputFirstName.sendKeys(firstName);

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputLastName")));
        WebElement inputLastName = driver.findElement(By.id("inputLastName"));
        inputLastName.click();
        inputLastName.sendKeys(lastName);

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputUsername")));
        WebElement inputUsername = driver.findElement(By.id("inputUsername"));
        inputUsername.click();
        inputUsername.sendKeys(userName);

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputPassword")));
        WebElement inputPassword = driver.findElement(By.id("inputPassword"));
        inputPassword.click();
        inputPassword.sendKeys(password);

        // Attempt to sign up.
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("buttonSignUp")));
        WebElement buttonSignUp = driver.findElement(By.id("buttonSignUp"));
        buttonSignUp.click();

		/* Check that the sign up was successful. 
		// You may have to modify the element "success-msg" and the sign-up 
		// success message below depening on the rest of your code.
		*/
        Assertions.assertTrue(driver.findElement(By.id("success-msg")).getText().contains("You successfully signed up!"));
    }


    /**
     * PLEASE DO NOT DELETE THIS method.
     * Helper method for Udacity-supplied sanity checks.
     **/
    private void doLogIn(String userName, String password) {
        // Log in to our dummy account.
        driver.get("http://localhost:" + this.port + "/login");
        WebDriverWait webDriverWait = new WebDriverWait(driver, 2);

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputUsername")));
        WebElement loginUserName = driver.findElement(By.id("inputUsername"));
        loginUserName.click();
        loginUserName.sendKeys(userName);

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputPassword")));
        WebElement loginPassword = driver.findElement(By.id("inputPassword"));
        loginPassword.click();
        loginPassword.sendKeys(password);

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("login-button")));
        WebElement loginButton = driver.findElement(By.id("login-button"));
        loginButton.click();

        webDriverWait.until(ExpectedConditions.titleContains("Home"));

    }

    /**
     * PLEASE DO NOT DELETE THIS TEST. You may modify this test to work with the
     * rest of your code.
     * This test is provided by Udacity to perform some basic sanity testing of
     * your code to ensure that it meets certain rubric criteria.
     * <p>
     * If this test is failing, please ensure that you are handling redirecting users
     * back to the login page after a succesful sign up.
     * Read more about the requirement in the rubric:
     * https://review.udacity.com/#!/rubrics/2724/view
     */
    @Test
    public void testRedirection() {
        // Create a test account
        doMockSignUp("Redirection", "Test", "RT", "123");

        // Check if we have been redirected to the log in page.
        Assertions.assertEquals("http://localhost:" + this.port + "/login?success", driver.getCurrentUrl());
    }

    /**
     * PLEASE DO NOT DELETE THIS TEST. You may modify this test to work with the
     * rest of your code.
     * This test is provided by Udacity to perform some basic sanity testing of
     * your code to ensure that it meets certain rubric criteria.
     * <p>
     * If this test is failing, please ensure that you are handling bad URLs
     * gracefully, for example with a custom error page.
     * <p>
     * Read more about custom error pages at:
     * https://attacomsian.com/blog/spring-boot-custom-error-page#displaying-custom-error-page
     */
    @Test
    public void testBadUrl() {
        // Create a test account
        doMockSignUp("URL", "Test", "UT", "123");
        doLogIn("UT", "123");

        // Try to access a random made-up URL.
        driver.get("http://localhost:" + this.port + "/some-random-page");
        Assertions.assertFalse(driver.getPageSource().contains("Whitelabel Error Page"));
    }


    /**
     * PLEASE DO NOT DELETE THIS TEST. You may modify this test to work with the
     * rest of your code.
     * This test is provided by Udacity to perform some basic sanity testing of
     * your code to ensure that it meets certain rubric criteria.
     * <p>
     * If this test is failing, please ensure that you are handling uploading large files (>1MB),
     * gracefully in your code.
     * <p>
     * Read more about file size limits here:
     * https://spring.io/guides/gs/uploading-files/ under the "Tuning File Upload Limits" section.
     */
    @Test
    public void testLargeUpload() {
        // Create a test account
        doMockSignUp("Large File", "Test", "LFT", "123");
        doLogIn("LFT", "123");

        // Try to upload an arbitrary large file
        WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
        String fileName = "upload5m.zip";

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("fileUpload")));
        WebElement fileSelectButton = driver.findElement(By.id("fileUpload"));
        fileSelectButton.sendKeys(new File(fileName).getAbsolutePath());

        WebElement uploadButton = driver.findElement(By.id("uploadButton"));
        uploadButton.click();
        try {
            webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.id("success")));
        } catch (org.openqa.selenium.TimeoutException e) {
            System.out.println("Large File upload failed");
        }
        Assertions.assertFalse(driver.getPageSource().contains("HTTP Status 403 – Forbidden"));

    }

    @Test
    public void testAnonymousHomeAccess() {
        //Go to login page and save state
        driver.get("http://localhost:" + this.port + "/login");
        String initialUrl = driver.getCurrentUrl();
        //try to go to home without loggin and retrieve url, should redirect to login
        driver.get("http://localhost:" + this.port + "/home");
        String finalUrl = driver.getCurrentUrl();
        Assertions.assertEquals(initialUrl, finalUrl);
    }

    @Test
    public void testLoggedInHomeAccess() {
        doMockSignUp("HomeAccess", "Test", "HAT", "123");
        doLogIn("HAT", "123");

        WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
        // Check if we have been redirected to the home page
        Assertions.assertEquals("http://localhost:" + this.port + "/home", driver.getCurrentUrl());

        //logout should redirect to login
        WebElement logoutButton = driver.findElement(By.id("home-logout-button"));
        logoutButton.click();
        Assertions.assertEquals("http://localhost:" + this.port + "/login?logout", driver.getCurrentUrl());

        //verifies home is no longer accesible
        driver.get("http://localhost:" + this.port + "/home");
        String finalUrl = driver.getCurrentUrl();
        Assertions.assertEquals("http://localhost:" + this.port + "/login", finalUrl);
    }

    @Test
    public void testNotesAddFlow() {
        doMockSignUp("HomeAccess", "Test", "NAT", "123");
        doLogIn("NAT", "123");

        //initiate notes module
        WebDriverWait webDriverWait = new WebDriverWait(driver, 100);
        goToNotesModule(webDriverWait);

        //Create note
        String noteTitle = "AutomatedTestTitle";
        fillNoteFormSubmitAndRedirectHome(webDriverWait, noteTitle, "Test Description");

        //Validate notes table content
        WebElement notesTab = driver.findElement(By.id("nav-notes-tab"));
        notesTab.click();
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("noteTable")));
        WebElement notesTable = driver.findElement(By.id("noteTable"));
        Assertions.assertTrue(notesTable.getText().contains(noteTitle));
    }

    private void goToNotesModule(WebDriverWait webDriverWait) {
        WebElement notesTab = driver.findElement(By.id("nav-notes-tab"));
        notesTab.click();
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("new-note-button")));
        WebElement newNoteButton = driver.findElement(By.id("new-note-button"));
        newNoteButton.click();
    }

    private void fillNoteFormSubmitAndRedirectHome(WebDriverWait webDriverWait, String noteTitle, String noteDescription) {
        //Fill notes form
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-title")));
        WebElement noteTitleInput = driver.findElement(By.id("note-title"));
        noteTitleInput.clear();
        noteTitleInput.sendKeys(noteTitle);
        WebElement noteTitleDescription = driver.findElement(By.id("note-description"));
        noteTitleDescription.clear();
        noteTitleDescription.sendKeys(noteDescription);

        //Submit to create note should redirect to /home/result with success
        webDriverWait.until(ExpectedConditions.elementToBeClickable(By.id("noteSubmitModal"))).click();
        webDriverWait.until(ExpectedConditions.titleContains("Result"));
        Assertions.assertTrue(driver.getPageSource().contains("Success"));
        WebElement redirectButton = driver.findElement(By.id("success-redirect"));
        redirectButton.click();
    }

    @Test
    public void testNotesEditFlow() {
        doMockSignUp("HomeAccess", "Test", "NET", "123");
        doLogIn("NET", "123");

        //initiate notes module
        WebDriverWait webDriverWait = new WebDriverWait(driver, 100);
        goToNotesModule(webDriverWait);

        //Create note
        String noteTitle = "AutomatedTestTitle";
        fillNoteFormSubmitAndRedirectHome(webDriverWait, noteTitle, "Test Description");
        Note originalNote = noteService.getNotesByUser("NET").get(0);

        //edit note
        WebElement notesTab = driver.findElement(By.id("nav-notes-tab"));
        notesTab.click();
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("noteTable")));
        List<WebElement> editButtons = driver.findElements(By.id("note-edit-button"));
        editButtons.get(0).click();
        fillNoteFormSubmitAndRedirectHome(webDriverWait, "UpdatedNoteTitle", "Test Description");
        Note updatedNote = noteService.getNotesByUser("NET").get(0);
        Assertions.assertTrue((originalNote.getNoteId() == updatedNote.getNoteId() && !originalNote.getNoteTitle().equals(updatedNote.getNoteTitle())));
    }

    @Test
    public void testNotesDeleteFlow() {
        doMockSignUp("HomeAccess", "Test", "NDT", "123");
        doLogIn("NDT", "123");

        //initiate notes module
        WebDriverWait webDriverWait = new WebDriverWait(driver, 100);
        goToNotesModule(webDriverWait);

        //Create notes
        fillNoteFormSubmitAndRedirectHome(webDriverWait, "AutomatedTestTitle", "Test Description");
        goToNotesModule(webDriverWait);
        fillNoteFormSubmitAndRedirectHome(webDriverWait, "ToDeleteTestTitle", "Test Description");

        List<Note> preDeleteNotes = noteService.getNotesByUser("NDT");

        //Delete second note
        WebElement notesTab = driver.findElement(By.id("nav-notes-tab"));
        notesTab.click();
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("noteTable")));
        List<WebElement> deleteButtons = driver.findElements(By.id("note-delete-button"));

        deleteButtons.get(1).click();
        webDriverWait.until(ExpectedConditions.titleContains("Result"));
        Assertions.assertTrue(driver.getPageSource().contains("Success"));
        List<Note> postDeleteNotes = noteService.getNotesByUser("NDT");
        Assertions.assertTrue(preDeleteNotes.size() > postDeleteNotes.size());
    }

    @Test
    public void testCredentialsAddFlow() {
        doMockSignUp("HomeAccess", "Test", "CAT", "123");
        doLogIn("CAT", "123");

        //initiate credential module
        WebDriverWait webDriverWait = new WebDriverWait(driver, 100);
        goToCredentialsModule(webDriverWait);

        String credentialUrl = "https://www.edeleontest.com";
        fillCredentialFormSubmitAndRedirectHome(webDriverWait, credentialUrl, "edeleon", "TestPass123");

        //Validate notes table content
        WebElement credentialsTab = driver.findElement(By.id("nav-credentials-tab"));
        credentialsTab.click();
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credentialTable")));
        WebElement notesTable = driver.findElement(By.id("credentialTable"));
        Assertions.assertTrue(notesTable.getText().contains(credentialUrl));
    }

    private void goToCredentialsModule(WebDriverWait webDriverWait) {
        WebElement credentialsTab = driver.findElement(By.id("nav-credentials-tab"));
        credentialsTab.click();
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("new-credential-button")));
        WebElement newCredentialButton = driver.findElement(By.id("new-credential-button"));
        newCredentialButton.click();
    }

    private void fillCredentialFormSubmitAndRedirectHome(WebDriverWait webDriverWait, String url, String username, String password) {
        //Fill credentials form
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-url")));
        WebElement credentialUrlInput = driver.findElement(By.id("credential-url"));
        credentialUrlInput.clear();
        credentialUrlInput.sendKeys(url);
        WebElement credentialUsernameInput = driver.findElement(By.id("credential-username"));
        credentialUsernameInput.clear();
        credentialUsernameInput.sendKeys(username);
        WebElement credentialPasswordInput = driver.findElement(By.id("credential-password"));
        credentialPasswordInput.clear();
        credentialPasswordInput.sendKeys(password);

        //Submit to create credential should redirect to /home/result with success
        webDriverWait.until(ExpectedConditions.elementToBeClickable(By.id("credentialSubmitModal"))).click();
        webDriverWait.until(ExpectedConditions.titleContains("Result"));
        Assertions.assertTrue(driver.getPageSource().contains("Success"));
        WebElement redirectButton = driver.findElement(By.id("success-redirect"));
        redirectButton.click();
    }

    @Test
    public void testEditCredentialFlow() {
        doMockSignUp("HomeAccess", "Test", "CET", "123");
        doLogIn("CET", "123");

        //initiate credential module
        WebDriverWait webDriverWait = new WebDriverWait(driver, 100);
        goToCredentialsModule(webDriverWait);

        String credentialUrl = "https://www.edeleontest.com";
        fillCredentialFormSubmitAndRedirectHome(webDriverWait, credentialUrl, "edeleon", "TestPass123");
        Credential originalCredential = credentialService.getCredentialsByUser("CET").get(0);

        //Edit credenctal
        WebElement credentialsTab = driver.findElement(By.id("nav-credentials-tab"));
        credentialsTab.click();
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credentialTable")));
        List<WebElement> editButtons = driver.findElements(By.id("cred-edit-button"));
        editButtons.get(0).click();
        fillCredentialFormSubmitAndRedirectHome(webDriverWait, credentialUrl, "updatededeleon", "TestPass123");
        Credential updatedCredential = credentialService.getCredentialsByUser("CET").get(0);
        Assertions.assertTrue((originalCredential.getCredentialId() == updatedCredential.getCredentialId() && !originalCredential.getUsername().equals(updatedCredential.getUsername())));

    }

    @Test
    public void testDeleteCredentialFlow() {
        doMockSignUp("HomeAccess", "Test", "CDT", "123");
        doLogIn("CDT", "123");

        //initiate credential module
        WebDriverWait webDriverWait = new WebDriverWait(driver, 100);
        goToCredentialsModule(webDriverWait);

        String credentialUrl = "https://www.edeleontest.com";
        fillCredentialFormSubmitAndRedirectHome(webDriverWait, credentialUrl, "edeleon", "TestPass123");
        goToCredentialsModule(webDriverWait);
        fillCredentialFormSubmitAndRedirectHome(webDriverWait, credentialUrl, "testDelete", "TestPass123");

        List<Credential> preDeleteCredentialList = credentialService.getCredentialsByUser("CDT");
        WebElement credentialsTab = driver.findElement(By.id("nav-credentials-tab"));
        credentialsTab.click();
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credentialTable")));
        List<WebElement> deleteButtons = driver.findElements(By.id("cred-delete-button"));

        deleteButtons.get(1).click();
        webDriverWait.until(ExpectedConditions.titleContains("Result"));
        Assertions.assertTrue(driver.getPageSource().contains("Success"));
        List<Credential> postDeleteCredentialList = credentialService.getCredentialsByUser("CDT");
        Assertions.assertTrue(preDeleteCredentialList.size() > postDeleteCredentialList.size());
    }

}
