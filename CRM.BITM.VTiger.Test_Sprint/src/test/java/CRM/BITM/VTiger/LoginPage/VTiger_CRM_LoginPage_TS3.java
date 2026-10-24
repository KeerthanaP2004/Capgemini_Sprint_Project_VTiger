package CRM.BITM.VTiger.LoginPage;

import java.io.IOException;
import java.time.Duration;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Test;

import CRM.BITM.VTiger.Base_Class.VTiger_CRM_BaseClass;
import CRM.BITM.VTiger.POM.VTiger_CRM_LoginPage;
import Screenshot.Screenshot;

public class VTiger_CRM_LoginPage_TS3 extends VTiger_CRM_BaseClass {

	private VTiger_CRM_LoginPage loginPage;
	private WebDriverWait wait;

	public VTiger_CRM_LoginPage_TS3() {
		skipAutomaticLoginLogout = true;
	}

	private void log(String message) {
		Reporter.log(message, true);
	}

	private String getRequiredProperty(String key) throws Exception {

		final String value;

		try {
			value = fi.getDataFromPropertyFile(key);
		} catch (IOException e) {
			throw new IllegalStateException("Unable to read configuration property: " + key, e);
		}

		if (value == null || value.trim().isEmpty()) {
			throw new IllegalStateException("Missing or blank property: " + key);
		}

		return value.trim();
	}

	/*
	 * Reads a property that is intentionally allowed to be empty. The
	 * boundaryUsername and boundaryPassword keys must exist in config.properties
	 * even when their values are blank.
	 */

	private String getPropertyAllowBlank(String key) throws Exception {

		final String value;

		try {
			value = fi.getDataFromPropertyFile(key);
		} catch (IOException e) {
			throw new IllegalStateException("Unable to read configuration property: " + key, e);
		}

		if (value == null) {
			throw new IllegalStateException("Missing configuration property: " + key);
		}

		return value.trim();
	}

	private long getWaitSeconds(String key) throws Exception {

		String value = getRequiredProperty(key);

		try {
			long seconds = Long.parseLong(value);

			if (seconds <= 0) {
				throw new IllegalArgumentException("Property '" + key + "' must be greater than zero.");
			}

			return seconds;

		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("Property '" + key + "' must contain a valid whole number.", e);
		}
	}

	private void openLoginPage() throws Exception {

		String appUrl = getRequiredProperty("url");
		long explicitWait = getWaitSeconds("explicitWait");

		wait = new WebDriverWait(driver, Duration.ofSeconds(explicitWait));

		driver.manage().deleteAllCookies();
		driver.get(appUrl);

		loginPage = new VTiger_CRM_LoginPage(driver);

		verifyLoginPageDisplayed();

		log("Login page opened successfully.");
	}

	private void verifyLoginPageDisplayed() {

		WebElement username = wait.until(ExpectedConditions.visibilityOf(loginPage.getUserName()));

		WebElement password = wait.until(ExpectedConditions.visibilityOf(loginPage.getPassword()));

		WebElement loginButton = wait.until(ExpectedConditions.elementToBeClickable(loginPage.getLoginButton()));

		Assert.assertTrue(username.isDisplayed(), "Username field is not displayed.");

		Assert.assertTrue(password.isDisplayed(), "Password field is not displayed.");

		Assert.assertTrue(loginButton.isDisplayed(), "Login button is not displayed.");
	}

	private String getSingleCharacterProperty(String key) throws Exception {

		String value = getRequiredProperty(key);

		if (value.length() != 1) {
			throw new IllegalStateException("Property '" + key + "' must contain exactly one character.");
		}

		return value;
	}

	private void verifyRequiredFields(String testCaseName) throws Exception {

		log("TEST STARTED: " + testCaseName);

		try {
			openLoginPage();

			String usernameValue = getPropertyAllowBlank("boundaryUsername");

			String passwordValue = getPropertyAllowBlank("boundaryPassword");

			log("STEP 1: Entering configured empty boundary values.");

			WebElement username = wait.until(ExpectedConditions.visibilityOf(loginPage.getUserName()));

			WebElement password = wait.until(ExpectedConditions.visibilityOf(loginPage.getPassword()));

			username.clear();

			if (!usernameValue.isEmpty()) {
				username.sendKeys(usernameValue);
			}

			password.clear();

			if (!passwordValue.isEmpty()) {
				password.sendKeys(passwordValue);
			}

			log("STEP 2: Submit the login form.");

			wait.until(ExpectedConditions.elementToBeClickable(loginPage.getLoginButton())).click();

			log("STEP 3: Verify the login form remains available.");

			verifyLoginPageDisplayed();

			log("RESULT: PASS - Login form remains displayed.");

		} finally {
			log("TEST FINISHED: " + testCaseName);
			log("--------------------------------------------");
		}
	}

	@Test(description = "TC-VT-LOGIN-10 - Verify empty input boundary", groups = { "BVA", "negative" })
	public void VTiger_CRM_TC_Login_10_EmptyInputBoundary() throws Exception {

		verifyRequiredFields("TC-VT-LOGIN-10 - Empty Input Boundary");
		Screenshot.takeScreenshot(driver, "TC-VT-LOGIN-10_EmptyInputBoundary");
	}

	@Test(description = "TC-VT-LOGIN-11 - Verify single-character input boundary", groups = { "BVA", "negative" })
	public void VTiger_CRM_TC_Login_11_SingleCharacterInputBoundary() throws Exception {

		log("TEST STARTED: TC-VT-LOGIN-11 - Single Character Boundary");

		try {
			openLoginPage();

			String usernameValue = getSingleCharacterProperty("invalidBoundaryUsername");

			String passwordValue = getSingleCharacterProperty("invalidBoundaryPassword");

			log("STEP 1: Enter configured boundary values.");

			WebElement username = wait.until(ExpectedConditions.visibilityOf(loginPage.getUserName()));

			username.clear();
			username.sendKeys(usernameValue);

			WebElement password = wait.until(ExpectedConditions.visibilityOf(loginPage.getPassword()));

			password.clear();
			password.sendKeys(passwordValue);

			log("STEP 2: Submit the login form.");

			wait.until(ExpectedConditions.elementToBeClickable(loginPage.getLoginButton())).click();

			log("STEP 3: Verify the login form remains available.");

			verifyLoginPageDisplayed();

			// Capture screenshot after the verification.
			Screenshot.takeScreenshot(driver, "TC-VT-LOGIN-11_SingleCharacterBoundary");

			log("RESULT: PASS - Login form remains displayed.");

		} finally {
			log("TEST FINISHED: TC-VT-LOGIN-11");
			log("--------------------------------------------");
		}
	}
}
