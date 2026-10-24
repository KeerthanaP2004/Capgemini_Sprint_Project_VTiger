
package CRM.BITM.VTiger.HomePage;

import java.time.Duration;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Test;

import CRM.BITM.VTiger.Base_Class.VTiger_CRM_BaseClass;
import CRM.BITM.VTiger.POM.VTiger_CRM_HomePage;

public class VTiger_CRM_HomePage_TS1 extends VTiger_CRM_BaseClass {

	private VTiger_CRM_HomePage homePage;
	private WebDriverWait wait;

	private void log(String message) {
		Reporter.log(message, true);
	}

	private void verifyNavigation(WebElement navigationLink, String moduleName) {

		wait.until(ExpectedConditions.visibilityOf(navigationLink));

		Assert.assertTrue(navigationLink.isDisplayed(), moduleName + " navigation link is not visible.");

		String homePageUrl = driver.getCurrentUrl();

		log("Clicking " + moduleName + " navigation link.");

		wait.until(ExpectedConditions.elementToBeClickable(navigationLink)).click();

		wait.until(currentDriver -> !currentDriver.getCurrentUrl().equals(homePageUrl));

		String moduleUrl = driver.getCurrentUrl();

		Assert.assertNotEquals(moduleUrl, homePageUrl, "Clicking " + moduleName + " did not change the page URL.");

		log(moduleName + " opened. Current URL: " + moduleUrl);

		driver.navigate().back();

		wait.until(ExpectedConditions.urlToBe(homePageUrl));

		// Reinitialize the page object after browser navigation.
		homePage = new VTiger_CRM_HomePage(driver);

		wait.until(ExpectedConditions.visibilityOf(homePage.getUserIcon()));

		log("Returned to Home after checking " + moduleName + ".");
	}

	@Test(description = "TC-VT-HOME-01 - Verify Home page navigation and module opening", groups = { "ui", "navigation",
			"functionality" })
	public void VTiger_CRM_TC_Home_01_HomePageNavigation() {

		long startTime = System.nanoTime();

		log("TEST STARTED: TC-VT-HOME-01");

		try {
			wait = new WebDriverWait(driver, Duration.ofSeconds(10));

			homePage = new VTiger_CRM_HomePage(driver);

			log("STEP 1: Verify user icon visibility.");

			wait.until(ExpectedConditions.visibilityOf(homePage.getUserIcon()));

			Assert.assertTrue(homePage.getUserIcon().isDisplayed(), "User icon is not visible.");

			log("STEP 2: Open Calendar.");

			verifyNavigation(homePage.getCalendar(), "Calendar");

			log("STEP 3: Open Organizations.");

			verifyNavigation(homePage.getOrg(), "Organizations");

			log("STEP 4: Open Contacts.");

			verifyNavigation(homePage.getContact(), "Contacts");

			log("EXPECTED RESULT: Each navigation link opens its module.");
			log("RESULT: PASS - TC-VT-HOME-01");

		} finally {
			log("EXECUTION TIME: " + ((System.nanoTime() - startTime) / 1_000_000_000.0) + " seconds");

			log("TEST FINISHED: TC-VT-HOME-01");
			log("--------------------------------------------");
		}
	}
}
