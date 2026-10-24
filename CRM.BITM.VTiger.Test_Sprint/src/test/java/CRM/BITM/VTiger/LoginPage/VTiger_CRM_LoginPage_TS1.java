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

public class VTiger_CRM_LoginPage_TS1 extends VTiger_CRM_BaseClass {

	public VTiger_CRM_LoginPage_TS1() {
		skipAutomaticLoginLogout = true;
	}

	private VTiger_CRM_LoginPage loginPage;
	private WebDriverWait wait;

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

		log("--------------------------------------------------");
		log("STEP 1: Opening VTiger CRM login page.");

		long implicitWait = getWaitSeconds("implicitWait");
		long explicitWait = getWaitSeconds("explicitWait");

		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitWait));

		wait = new WebDriverWait(driver, Duration.ofSeconds(explicitWait));

		driver.manage().deleteAllCookies();
		driver.get(appUrl);

		loginPage = new VTiger_CRM_LoginPage(driver);

		log("Application URL: " + driver.getCurrentUrl());
		log("STEP 2: Waiting for login page elements.");

		wait.until(ExpectedConditions.visibilityOf(loginPage.getUserName()));

		wait.until(ExpectedConditions.visibilityOf(loginPage.getPassword()));

		wait.until(ExpectedConditions.elementToBeClickable(loginPage.getLoginButton()));

		log("Login page is ready.");
	}

	private void verifyHomePageNavigation() throws Exception {

		log("STEP 6: Verifying Home page navigation.");

		String expectedModule = getRequiredProperty("homeModule").toLowerCase(Locale.ROOT);

		String expectedAction = getRequiredProperty("homeAction").toLowerCase(Locale.ROOT);

		wait.until(webDriver -> {
			String currentUrl = webDriver.getCurrentUrl().toLowerCase(Locale.ROOT);

			return currentUrl.contains(expectedModule) && currentUrl.contains(expectedAction);
		});

		String actualUrl = driver.getCurrentUrl();
		String normalizedUrl = actualUrl.toLowerCase(Locale.ROOT);

		log("Actual URL: " + actualUrl);

		Assert.assertTrue(normalizedUrl.contains(expectedModule), "Home page module was not found in the URL.");

		Assert.assertTrue(normalizedUrl.contains(expectedAction), "Home page action was not found in the URL.");

		log("RESULT: PASS - TC-VT-LOGIN-01");
	}

	@Test(description = "TC-VT-LOGIN-01 - Verify login with valid credentials", groups = { "functional", "smoke" })
	public void VTiger_CRM_TC_Login_01_ValidLogin() throws Exception {

		long startTime = System.nanoTime();

		log("TEST STARTED: TC-VT-LOGIN-01");

		try {
			openLoginPage();

			String usernameValue = getRequiredProperty("username");
			String passwordValue = getRequiredProperty("password");

			log("STEP 3: Entering username.");

			WebElement username = wait.until(ExpectedConditions.elementToBeClickable(loginPage.getUserName()));

			username.clear();
			username.sendKeys(usernameValue);

			log("STEP 4: Entering password.");

			WebElement password = wait.until(ExpectedConditions.elementToBeClickable(loginPage.getPassword()));

			password.clear();
			password.sendKeys(passwordValue);

			log("Password entered. Sensitive values are not logged.");

			log("STEP 5: Clicking Login.");

			WebElement loginButton = wait.until(ExpectedConditions.elementToBeClickable(loginPage.getLoginButton()));

			loginButton.click();

			verifyHomePageNavigation();

		} finally {
			log("EXECUTION TIME: " + getElapsedSeconds(startTime) + " seconds");
			log("TEST FINISHED: TC-VT-LOGIN-01");
			log("--------------------------------------------------");
		}
	}

	@Test(description = "TC-VT-LOGIN-02 - Verify login page elements visibility", groups = { "system", "EP" })
	public void VTiger_CRM_TC_Login_02_LoginPageElementsVisibility() throws Exception {

		long startTime = System.nanoTime();

		log("TEST STARTED: TC-VT-LOGIN-02");

		try {
			openLoginPage();

			log("STEP 3: Checking username field.");

			WebElement username = wait.until(ExpectedConditions.visibilityOf(loginPage.getUserName()));

			Assert.assertTrue(username.isDisplayed(), "Username field is not visible.");

			log("Username field: VISIBLE.");

			log("STEP 4: Checking password field.");

			WebElement password = wait.until(ExpectedConditions.visibilityOf(loginPage.getPassword()));

			Assert.assertTrue(password.isDisplayed(), "Password field is not visible.");

			log("Password field: VISIBLE.");

			log("STEP 5: Checking Login button.");

			WebElement loginButton = wait.until(ExpectedConditions.visibilityOf(loginPage.getLoginButton()));

			Assert.assertTrue(loginButton.isDisplayed(), "Login button is not visible.");

			Assert.assertTrue(loginButton.isEnabled(), "Login button is disabled.");

			log("Login button: VISIBLE AND ENABLED.");
			log("RESULT: PASS - TC-VT-LOGIN-02");

		} finally {
			log("EXECUTION TIME: " + getElapsedSeconds(startTime) + " seconds");
			log("TEST FINISHED: TC-VT-LOGIN-02");
			log("--------------------------------------------------");
		}
	}

	private double getElapsedSeconds(long startTime) {
		return (System.nanoTime() - startTime) / 1_000_000_000.0;
	}
}
