package CRM.BITM.VTiger.FAQ;

import java.io.IOException;

import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Test;

import CRM.BITM.VTiger.Base_Class.VTiger_CRM_BaseClass;
import Screenshot.Screenshot;

public class VTiger_CRM_FAQ_TS2 extends VTiger_CRM_BaseClass {

	@Test(groups = { "functional", "negative", "EP" })
	public void verifyEmptyFAQValidation() throws IOException {

		Reporter.log("TC-VT-FAQ-06: Verify mandatory-field validation.", true);

		homePage.openFAQModule();
		faqPage.clickCreateFAQ();

		Assert.assertTrue(faqPage.isNewFAQFormDisplayed(), "New FAQ form was not displayed.");

		faqPage.clickSave();

		// Handle the application's JavaScript validation alert.
		org.openqa.selenium.Alert alert = wait
				.until(org.openqa.selenium.support.ui.ExpectedConditions.alertIsPresent());

		String alertMessage = alert.getText();

		Reporter.log("Validation alert: " + alertMessage, true);

		Assert.assertEquals(alertMessage.trim(), "Question cannot be none",
				"Unexpected validation message for an empty FAQ.");

		alert.accept();

		// Capture screenshot after the alert is dismissed.
		Screenshot.takeScreenshot(driver, "TC_VT_FAQ_06_Empty_FAQ_Validation");

		// Verify that the form remains open after dismissing the alert.
		Assert.assertTrue(faqPage.isNewFAQFormDisplayed(), "FAQ form was closed after validation.");

		Reporter.log("Empty FAQ validation passed. The expected alert was displayed " + "and the form remained open.",
				true);
	}

	@Test(groups = { "functional", "integration" })
	public void verifyCreateFAQButton() {

		Reporter.log("TC-VT-FAQ-09: Verify Create FAQ button.", true);

		homePage.openFAQModule();
		faqPage.clickCreateFAQ();

		Assert.assertTrue(faqPage.isNewFAQFormDisplayed(), "Create FAQ button did not open the New FAQ form.");

		Reporter.log("Create FAQ button works.", true);
	}

	@Test(groups = { "functional" })
	public void verifyFAQFormUsability() {

		Reporter.log("TC-VT-FAQ-12: Verify FAQ form usability.", true);

		homePage.openFAQModule();
		faqPage.clickCreateFAQ();

		Assert.assertTrue(faqPage.isNewFAQFormDisplayed(), "New FAQ form was not displayed.");

		Assert.assertNotNull(faqPage.getProductName(), "Product field is missing.");

		Assert.assertNotNull(faqPage.getQuestion(), "Question field is missing.");

		Assert.assertNotNull(faqPage.getAnswer(), "Answer field is missing.");

		Assert.assertNotNull(faqPage.getSelectedStatus(), "Status dropdown is missing.");

		Reporter.log("FAQ form fields are available. Tab-order and save-feedback "
				+ "checks require additional assertions against the actual page.", true);
	}
}