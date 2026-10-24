package CRM.BITM.VTiger.Project;

import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Test;

import CRM.BITM.VTiger.Base_Class.VTiger_CRM_BaseClass;

public class VTiger_CRM_Project_TS5 extends VTiger_CRM_BaseClass {

	@Test(groups = { "functional", "integration" })
	public void TC_VT_PROJ_04_UpdateProjectProgress() throws Exception {

		String projectName = excel.readDataByHeader("Project", 1, "Project Name");

		String updatedProgress = excel.readDataByHeader("Project", 1, "Updated Progress");

		Assert.assertNotNull(projectName, "Project Name is null.");
		Assert.assertNotNull(updatedProgress, "Updated Progress is null.");
		Assert.assertFalse(projectName.trim().isEmpty());
		Assert.assertFalse(updatedProgress.trim().isEmpty());

		homePage.openProjectsModule();
		projectPage.openProjectDetails(projectName);

		projectPage.updateProgress(updatedProgress);

		Assert.assertEquals(projectPage.getProjectProgress(), updatedProgress.trim(),
				"Project progress was not updated correctly.");

		Reporter.log("TC-VT-PROJ-04: Project progress updated and verified.", true);
	}

}
