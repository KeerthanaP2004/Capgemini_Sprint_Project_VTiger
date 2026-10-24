package CRM.BITM.VTiger.Project;

import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Test;

import CRM.BITM.VTiger.Base_Class.VTiger_CRM_BaseClass;
import Screenshot.Screenshot;

public class VTiger_CRM_Project_TS4 extends VTiger_CRM_BaseClass {

	@Test(groups = { "functional", "integration" })
	public void TC_VT_PROJ_02_SearchSavedProject() throws Exception {

		String projectName = excel.readDataByHeader("Project", 1, "Project Name");

		Assert.assertNotNull(projectName, "Project Name is null.");
		Assert.assertFalse(projectName.trim().isEmpty(), "Project Name is blank in Excel.");

		homePage.openProjectsModule();
		projectPage.searchProject(projectName);

		Assert.assertTrue(projectPage.isProjectListed(projectName), "Saved project was not found: " + projectName);

		Reporter.log("TC-VT-PROJ-02: Project found in search results.", true);
	}

	@Test(groups = { "negative", "functional" })
	public void TC_VT_PROJ_08_SearchNonexistentProject() throws Exception {

		String searchName = excel.readDataByHeader("Project", 1, "Nonexistent Project Name");

		Assert.assertNotNull(searchName, "Search name is null.");
		Assert.assertFalse(searchName.trim().isEmpty(), "Nonexistent Project Name is blank in Excel.");

		homePage.openProjectsModule();
		projectPage.searchProject(searchName);

		Assert.assertFalse(projectPage.isProjectListed(searchName),
				"A project with the supposedly nonexistent name was found: " + searchName);

		Screenshot.takeScreenshot(driver, "TC-VT-PROJ-08_SearchNonexistentProject");

		Reporter.log("TC-VT-PROJ-08: No matching project was found.", true);
	}
}
