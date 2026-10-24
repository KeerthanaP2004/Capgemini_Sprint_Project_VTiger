package CRM.BITM.VTiger.LoginPage;

import java.io.IOException;
import java.time.Duration;
import java.util.Locale;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Test;

import CRM.BITM.VTiger.Base_Class.VTiger_CRM_BaseClass;
import CRM.BITM.VTiger.POM.VTiger_CRM_LoginPage;
import Screenshot.Screenshot;

public class VTiger_CRM_LoginPage_TS2 extends VTiger_CRM_BaseClass {

	private VTiger_CRM_LoginPage loginPage;
	private WebDriverWait wait;

	public VTiger_CRM_LoginPage_TS2() {
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
			throw new IllegalStateException("Missing or blank property '" + key + "'.");
		}

		return value.trim();
	}

	/*
	 * Reads properties that are allowed to contain an empty value. Used for blank
	 * username/password boundary tests.
	 */
	private String getPropertyAllowBlank(String key) throws Exception {

		final String value;

		try {
			value = fi.getDataFromPropertyFile(key);
		} catch (IOException e) {
			throw new IllegalStateException("Unable to read configuration property: " + key, e);
		}

		if (value == null) {
			throw new IllegalStateException("Missing property '" + key + "'.");
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
		long implicitWait = getWaitSeconds("implicitWait");
		long explicitWait = getWaitSeconds("explicitWait");

		log("STEP 1: Opening VTiger CRM login page.");

		driver.manage().deleteAllCookies();

		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitWait));

		wait = new WebDriverWait(driver, Duration.ofSeconds(explicitWait));

		driver.get(appUrl);

		loginPage = new VTiger_CRM_LoginPage(driver);

		wait.until(ExpectedConditions.visibilityOf(loginPage.getUserName()));

		wait.until(ExpectedConditions.visibilityOf(loginPage.getPassword()));

		wait.until(ExpectedConditions.elementToBeClickable(loginPage.getLoginButton()));

		log("Login page is ready: " + driver.getCurrentUrl());
	}

	/*
	 * Null or an empty string means leave the field blank.
	 */
	private void submitLogin(String usernameValue, String passwordValue) {

		WebElement username = wait.until(ExpectedConditions.visibilityOf(loginPage.getUserName()));

		WebElement password = wait.until(ExpectedConditions.visibilityOf(loginPage.getPassword()));

		username.clear();

		if (usernameValue != null && !usernameValue.isEmpty()) {
			username.sendKeys(usernameValue);
		}

		password.clear();

		if (passwordValue != null && !passwordValue.isEmpty()) {
			password.sendKeys(passwordValue);
		}

		log("STEP 2: Login fields prepared.");

		WebElement loginButton = wait.until(ExpectedConditions.elementToBeClickable(loginPage.getLoginButton()));

		loginButton.click();

		log("STEP 3: Login form submitted.");
	}

	private void verifyLoginRejected(String testId) throws Exception {

		log("STEP 4: Verifying unsuccessful login.");

		String expectedHomeModule = getRequiredProperty("homeModule").toLowerCase(Locale.ROOT);

		wait.until(webDriver -> !webDriver.getCurrentUrl().toLowerCase(Locale.ROOT).contains(expectedHomeModule));

		String actualUrl = driver.getCurrentUrl();
		String normalizedUrl = actualUrl.toLowerCase(Locale.ROOT);

		Assert.assertFalse(normalizedUrl.contains(expectedHomeModule),
				testId + ": Unexpected navigation to Home: " + actualUrl);

		Assert.assertTrue(loginPage.getUserName().isDisplayed(), testId + ": Username field is not displayed.");

		Assert.assertTrue(loginPage.getPassword().isDisplayed(), testId + ": Password field is not displayed.");

		log("Login was not redirected to Home.");
		log("RESULT: PASS - " + testId);
	}

	@Test(description = "TC-VT-LOGIN-04 - Verify login with invalid username", groups = { "negative", "functional" })
	public void VTiger_CRM_TC_Login_04_InvalidUsername() throws Exception {

		long startTime = System.nanoTime();
		log("TEST STARTED: TC-VT-LOGIN-04");

		try {
			openLoginPage();

			submitLogin(getRequiredProperty("invalidUsername"), getRequiredProperty("password"));

			verifyLoginRejected("TC-VT-LOGIN-04");

			Screenshot.takeScreenshot(driver, "TC-VT-LOGIN-04_InvalidUsername");

		} finally {
			log("EXECUTION TIME: " + getElapsedSeconds(startTime) + " seconds");
			log("TEST FINISHED: TC-VT-LOGIN-04");
		}
	}

	@Test(description = "TC-VT-LOGIN-05 - Verify login with invalid password", groups = { "negative", "functional" })
	public void VTiger_CRM_TC_Login_05_InvalidPassword() throws Exception {

		long startTime = System.nanoTime();
		log("TEST STARTED: TC-VT-LOGIN-05");

		try {
			openLoginPage();

			submitLogin(getRequiredProperty("username"), getRequiredProperty("invalidPassword"));

			verifyLoginRejected("TC-VT-LOGIN-05");

			Screenshot.takeScreenshot(driver, "TC-VT-LOGIN-05_InvalidPassword");

		} finally {
			log("EXECUTION TIME: " + getElapsedSeconds(startTime) + " seconds");
			log("TEST FINISHED: TC-VT-LOGIN-05");
		}
	}

	@Test(description = "TC-VT-LOGIN-06 - Verify login with blank username", groups = { "negative", "BVA" })
	public void VTiger_CRM_TC_Login_06_BlankUsername() throws Exception {

		long startTime = System.nanoTime();
		log("TEST STARTED: TC-VT-LOGIN-06");

		try {
			openLoginPage();

			submitLogin(getPropertyAllowBlank("boundaryUsername"), getRequiredProperty("password"));

			verifyLoginRejected("TC-VT-LOGIN-06");

			Screenshot.takeScreenshot(driver, "TC-VT-LOGIN-06_BlankUsername");

		} finally {
			log("EXECUTION TIME: " + getElapsedSeconds(startTime) + " seconds");
			log("TEST FINISHED: TC-VT-LOGIN-06");
		}
	}

	@Test(description = "TC-VT-LOGIN-07 - Verify login with blank password", groups = { "negative", "BVA" })
	public void VTiger_CRM_TC_Login_07_BlankPassword() throws Exception {

		long startTime = System.nanoTime();
		log("TEST STARTED: TC-VT-LOGIN-07");

		try {
			openLoginPage();

			submitLogin(getRequiredProperty("username"), getPropertyAllowBlank("boundaryPassword"));

			verifyLoginRejected("TC-VT-LOGIN-07");

			Screenshot.takeScreenshot(driver, "TC-VT-LOGIN-07_BlankPassword");

		} finally {
			log("EXECUTION TIME: " + getElapsedSeconds(startTime) + " seconds");
			log("TEST FINISHED: TC-VT-LOGIN-07");
		}
	}

	private double getElapsedSeconds(long startTime) {
		return (System.nanoTime() - startTime) / 1_000_000_000.0;
	}
}
