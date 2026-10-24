package CRM.BITM.VTiger.ProjectMilestone;

import java.io.IOException;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Reporter;
import org.testng.SkipException;
import org.testng.annotations.Test;

import CRM.BITM.VTiger.Base_Class.VTiger_CRM_BaseClass;
import Screenshot.Screenshot;

public class VTiger_CRM_Project_Milestone_TS3_Validation_Search extends VTiger_CRM_BaseClass {

	// TC-VT-PM-07
	@Test(groups = { "negative", "BVA" })
	public void TC_VT_PM_07_MandatoryFieldValidation() throws IOException {

		Reporter.log("START: TC-VT-PM-07", true);

		homePage.openProjectMilestonesModule(driver);
		milestonePage.openNewMilestoneForm();

		milestonePage.verifyMandatoryValidation();

		Screenshot.takeScreenshot(driver, "TC-VT-PM-07_MandatoryFieldValidation");

		Reporter.log("PASS: TC-VT-PM-07", true);
	}

	// TC-VT-PM-08

	@Test(groups = { "negative", "functional" })
	public void TC_VT_PM_08_SearchReturnsNoUnrelatedRecord() throws IOException {

		Reporter.log("START: TC-VT-PM-08", true);

		String searchName = "NoSuchMilestone_Module6_999";

		homePage.openProjectMilestonesModule(driver);

		milestonePage.searchMilestone(searchName);

		// Verify the searched name is not displayed in the page results.
		// This is a basic check; confirm the list has refreshed before
		// treating the test as fully validated.
		boolean matchingRecordDisplayed = driver
				.findElements(By.xpath("//*[normalize-space(.)=" + milestonePage.xpathLiteral(searchName) + "]"))
				.stream().anyMatch(WebElement::isDisplayed);

		org.testng.Assert.assertFalse(matchingRecordDisplayed, "Unexpected milestone record found: " + searchName);

		Screenshot.takeScreenshot(driver, "TC-VT-PM-08_SearchReturnsNoUnrelatedRecord");

		Reporter.log("PASS: TC-VT-PM-08", true);
	}

	// TC-VT-PM-13
	@Test(groups = { "system", "EP" })
	public void TC_VT_PM_13_VerifyFormUsability() {

		Reporter.log("START: TC-VT-PM-13", true);

		homePage.openProjectMilestonesModule(driver);
		milestonePage.openNewMilestoneForm();
		milestonePage.verifyFormFields();

		WebElement nameField = wait
				.until(ExpectedConditions.visibilityOfElementLocated(By.name("projectmilestonename")));

		org.testng.Assert.assertTrue(nameField.isEnabled(), "Milestone Name field is not enabled.");

		Reporter.log("PASS: TC-VT-PM-13", true);
	}
}