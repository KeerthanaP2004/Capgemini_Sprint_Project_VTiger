package CRM.BITM.VTiger.HomePage;

import java.time.Duration;

import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Test;

import CRM.BITM.VTiger.Base_Class.VTiger_CRM_BaseClass;
import CRM.BITM.VTiger.POM.VTiger_CRM_HomePage;
import CRM.BITM.VTiger.POM.VTiger_CRM_LoginPage;

public class VTiger_CRM_HomePage_TS2 extends VTiger_CRM_BaseClass {

	private WebDriverWait wait;

	private void log(String message) {
		Reporter.log(message, true);
	}

	private void initializeWait() {
		wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	}

	private void verifyLoginPage() 
	{
		VTiger_CRM_LoginPage loginPage = new VTiger_CRM_LoginPage(driver);

		try {
			wait.until(d -> loginPage.isLoginPageDisplayed());

			Assert.assertTrue(loginPage.isLoginPageDisplayed(), "Login page is not displayed after logout.");

			log("Login page verified successfully.");

		} catch (org.openqa.selenium.TimeoutException e) {
			log("Login page did not appear after logout.");
			log("Current URL: " + driver.getCurrentUrl());

			Assert.fail("Login page was not displayed within the configured wait time.", e);
		}
	}

	private void verifyHomePage() {
		VTiger_CRM_HomePage homePage = new VTiger_CRM_HomePage(driver);

		wait.until(ExpectedConditions.visibilityOf(homePage.getCalendar()));
		wait.until(ExpectedConditions.visibilityOf(homePage.getOrg()));
		wait.until(ExpectedConditions.visibilityOf(homePage.getContact()));

		Assert.assertTrue(homePage.getCalendar().isDisplayed(), "Calendar link is not displayed.");

		Assert.assertTrue(homePage.getOrg().isDisplayed(), "Organizations link is not displayed.");

		Assert.assertTrue(homePage.getContact().isDisplayed(), "Contacts link is not displayed.");

		log("Home page navigation elements verified.");
	}

	@Test(priority = 1, description = "TC-VT-HOME-02 - Verify Login to Home navigation", groups = { "navigation",
			"functionality", "smoke" })
	public void VTiger_CRM_TC_Home_02_LoginToHomeNavigation() {
		log("TEST STARTED: TC-VT-HOME-02");

		try {
			initializeWait();
			verifyHomePage();

			log("RESULT: PASS - TC-VT-HOME-02");

		} finally {
			log("TEST FINISHED: TC-VT-HOME-02");
		}
	}

	@Test(priority = 2, description = "TC-VT-HOME-03 - Verify navigation and logout flow", groups = { "navigation",
			"functionality" })
	public void VTiger_CRM_TC_Home_03_CriticalLoginFlow() {
		log("TEST STARTED: TC-VT-HOME-03");

		try {
			initializeWait();

			log("STEP 1: Verify Home page.");
			verifyHomePage();

			String homeUrl = driver.getCurrentUrl();

			log("STEP 2: Open Calendar.");

			VTiger_CRM_HomePage homePage = new VTiger_CRM_HomePage(driver);

			wait.until(ExpectedConditions.elementToBeClickable(homePage.getCalendar())).click();

			wait.until(ExpectedConditions.urlContains("module=Calendar"));

			log("Calendar opened: " + driver.getCurrentUrl());

			log("STEP 3: Return to Home.");

			driver.navigate().back();

			wait.until(ExpectedConditions.urlToBe(homeUrl));
			verifyHomePage();

			log("STEP 4: Sign out.");

			homePage = new VTiger_CRM_HomePage(driver);
			homePage.userLogout(driver);

			log("STEP 5: Verify Login page.");

			verifyLoginPage();

			log("RESULT: PASS - TC-VT-HOME-03");

		} finally {
			log("TEST FINISHED: TC-VT-HOME-03");
		}
	}
}
