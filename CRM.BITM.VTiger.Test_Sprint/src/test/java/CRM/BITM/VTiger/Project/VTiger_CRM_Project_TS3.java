package CRM.BITM.VTiger.Project;

import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Test;

import CRM.BITM.VTiger.Base_Class.VTiger_CRM_BaseClass;

public class VTiger_CRM_Project_TS3 extends VTiger_CRM_BaseClass {

	@Test(groups = {"functional", "integration"})
	public void TC_VT_PROJ_01_CreateProject() throws Exception {
		String testCaseId = excel.readDataByHeader("Project", 1, "Test Case ID");

		String projectName = excel.readDataByHeader("Project", 1, "Project Name");

		String description = excel.readDataByHeader("Project", 1, "Description");

		Assert.assertNotNull(projectName);
		Assert.assertFalse(projectName.trim().isEmpty());
		Assert.assertNotNull(description);

		Reporter.log("Executing " + testCaseId, true);

		homePage.openProjectsModule();
		projectPage.openNewProjectForm();

		projectPage.enterProjectName(projectName);
		projectPage.enterDescription(description);
		projectPage.saveProject();

		Reporter.log("Project form submitted. Saved record must be verified.", true);
	}

	@Test(groups = {"smoke", "functional"})
	public void TC_VT_PROJ_11_ProjectCreationSmokeTest() throws Exception {
		String projectName = excel.readDataByHeader("Project", 1, "Project Name");

		String description = excel.readDataByHeader("Project", 1, "Description");

		Assert.assertNotNull(projectName);
		Assert.assertFalse(projectName.trim().isEmpty());

		homePage.openProjectsModule();
		projectPage.openNewProjectForm();

		projectPage.enterProjectName(projectName);
		projectPage.enterDescription(description);
		projectPage.saveProject();

		Reporter.log("Project creation smoke flow submitted.", true);
	}
}
