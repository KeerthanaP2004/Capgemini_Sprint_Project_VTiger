package CRM.BITM.VTiger.FAQ;

import java.io.IOException;

import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Test;

import CRM.BITM.VTiger.Base_Class.VTiger_CRM_BaseClass;
import Screenshot.Screenshot;

public class VTiger_CRM_FAQ_TS3 extends VTiger_CRM_BaseClass {

	@Test(groups = { "functional", "smoke" })
	public void verifyCreateFAQ() throws Exception {

		Reporter.log("TC-VT-FAQ-01: Create a new FAQ.", true);

		String product = excel.readDataByHeader("FAQ", 1, "Product");
		String question = excel.readDataByHeader("FAQ", 1, "Question");
		String answer = excel.readDataByHeader("FAQ", 1, "Answer");
		String status = excel.readDataByHeader("FAQ", 1, "Status");

		homePage.openFAQModule();
		faqPage.clickCreateFAQ();

		Assert.assertTrue(faqPage.isNewFAQFormDisplayed(), "New FAQ form was not displayed.");

		faqPage.selectProduct(product);
		faqPage.enterQuestion(question);
		faqPage.enterAnswer(answer);
		faqPage.selectStatus(status);
		faqPage.clickSave();

		homePage.openFAQModule();
		faqPage.searchFAQ(question);

		Assert.assertTrue(faqPage.isQuestionVisible(question), "Created FAQ was not found: " + question);

		Reporter.log("FAQ creation and search completed.", true);
	}

	@Test(groups = { "functional" })
	public void verifySearchFAQByQuestion() throws Exception {

		Reporter.log("TC-VT-FAQ-02: Search FAQ by question.", true);

		String question = excel.readDataByHeader("FAQ", 1, "Question");

		homePage.openFAQModule();
		faqPage.searchFAQ(question);

		// Diagnostic logging for search results.
		Reporter.log("Search question: " + question, true);
		Reporter.log("Current URL: " + driver.getCurrentUrl(), true);
		Reporter.log("Page title: " + driver.getTitle(), true);

		Reporter.log("Visible page text: " + driver.findElement(By.tagName("body")).getText(), true);

		Assert.assertTrue(faqPage.isQuestionVisible(question), "FAQ was not found using the question: " + question);
	}

	@Test(groups = { "functional", "negative" })
	public void verifyFAQSearchWithNoMatchingQuestion() throws IOException {

		Reporter.log("TC-VT-FAQ-07: Verify search with a question that should not match.", true);

		String question = "NoMatchingFAQQuestion_" + System.currentTimeMillis();

		homePage.openFAQModule();
		faqPage.searchFAQ(question);

		Assert.assertFalse(faqPage.isQuestionVisible(question), "An unexpected FAQ appeared for the test search.");
		
		// Capture screenshot after verifying no matching FAQ is displayed.
	    Screenshot.takeScreenshot(driver, "TC_VT_FAQ_07_No_Matching_Question");

		Reporter.log("No-match FAQ search check completed.", true);
	}
}
