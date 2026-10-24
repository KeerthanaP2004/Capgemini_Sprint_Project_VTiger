package CRM.BITM.VTiger.FAQ;

import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Test;

import CRM.BITM.VTiger.Base_Class.VTiger_CRM_BaseClass;

public class VTiger_CRM_FAQ_TS1 extends VTiger_CRM_BaseClass {

	@Test(groups = { "functional" })
	public void verifyNewFAQFormFields() {

		Reporter.log("TC-VT-FAQ-04: Verify New FAQ form fields.", true);

		homePage.openFAQModule();

		Assert.assertTrue(faqPage.isFAQListDisplayed(), "FAQ list page was not displayed.");

		faqPage.clickCreateFAQ();

		Assert.assertTrue(faqPage.isNewFAQFormDisplayed(), "New FAQ form was not displayed.");

		Assert.assertNotNull(faqPage.getProductName(), "Product field is missing.");

		Assert.assertNotNull(faqPage.getQuestion(), "Question field is missing.");

		Assert.assertNotNull(faqPage.getAnswer(), "Answer field is missing.");

		Assert.assertNotNull(faqPage.getSelectedStatus(), "FAQ status field is missing.");

		Reporter.log("New FAQ form fields are available.", true);
	}

	@Test(groups = { "functional", "smoke" })
	public void verifyFAQNavigationFromMoreMenu() {

		Reporter.log("TC-VT-FAQ-05: Verify FAQ navigation.", true);

		homePage.openFAQModule();

		Assert.assertTrue(faqPage.isFAQListDisplayed(), "FAQ list was not displayed after navigation.");

		Reporter.log("FAQ navigation completed.", true);
	}

	@Test(groups = { "integration", "smoke" })
	public void verifyFAQListNavigation() {

		Reporter.log("TC-VT-FAQ-08: Verify FAQ list navigation.", true);

		homePage.openFAQModule();

		Assert.assertTrue(faqPage.isFAQListDisplayed(), "FAQ list page was not opened.");

		faqPage.clickCreateFAQ();

		Assert.assertTrue(faqPage.isNewFAQFormDisplayed(), "Create FAQ form was not opened from the list.");

		Reporter.log("FAQ list-to-create-form navigation completed.", true);
	}
}
