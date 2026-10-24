package CRM.BITM.VTiger.Project;

import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Test;

import CRM.BITM.VTiger.Base_Class.VTiger_CRM_BaseClass;

public class VTiger_CRM_Project_TS6 extends VTiger_CRM_BaseClass {

	// TC-VT-PROJ-12: End-to-end project workflow
	@Test(groups = { "functional", "integration", "smoke" })
	public void TC_VT_PROJ_12_EndToEndProjectWorkflow() throws Exception {

		String testCaseId = excel.readDataByHeader("Project", 1, "Test Case ID");

		String projectName = excel.readDataByHeader("Project", 1, "Project Name");

		String description = excel.readDataByHeader("Project", 1, "Description");

		Assert.assertNotNull(testCaseId, "Test Case ID is missing.");
		Assert.assertNotNull(projectName, "Project Name is missing.");
		Assert.assertNotNull(description, "Description is missing.");

		Assert.assertFalse(projectName.trim().isEmpty(), "Project Name is blank in Excel.");

		Reporter.log("Executing " + testCaseId, true);

		// Open the Projects module and create the project.
		homePage.openProjectsModule();
		projectPage.openNewProjectForm();

		projectPage.enterProjectName(projectName);
		projectPage.enterDescription(description);
		projectPage.saveProject();

		// Return to the list and search for the saved project.
		homePage.openProjectsModule();
		projectPage.searchProject(projectName);

		Assert.assertTrue(projectPage.isProjectListed(projectName),
				"Project was not found after saving: " + projectName);

		Reporter.log("TC-VT-PROJ-12: Project creation and saved-record " + "search verification completed.", true);
	}

	@Test(groups = { "integration" }, description = "Verify project form supports a related contact", enabled = false)
	public void TC_VT_PROJ_14_VerifyProjectUnderRelatedContact() throws Exception {

		String relatedContact = excel.readDataByHeader("Project", 1, "Related Contact");

		Assert.assertNotNull(relatedContact, "Related Contact must not be null in the Project Excel sheet.");

		Assert.assertFalse(relatedContact.trim().isEmpty(), "Related Contact must not be empty.");

		// Open the new-project form.
		projectPage.openProjectsModule();
		projectPage.openNewProjectForm();

		// Check whether the Related To field is available.
		WebElement relatedToField = wait.until(
				org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated(projectPage.relatedTo));

		Assert.assertTrue(relatedToField.isDisplayed(), "The Related To field is not displayed on the project form.");

		System.out.println("PASS: Related To field is displayed. Contact association still "
				+ "requires selection and saved-record verification for: " + relatedContact);
	}
}
