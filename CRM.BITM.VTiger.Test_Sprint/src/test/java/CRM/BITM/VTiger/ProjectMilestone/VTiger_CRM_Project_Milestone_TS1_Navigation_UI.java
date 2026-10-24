package CRM.BITM.VTiger.ProjectMilestone;

import org.testng.Reporter;
import org.testng.annotations.Test;

import CRM.BITM.VTiger.Base_Class.VTiger_CRM_BaseClass;

public class VTiger_CRM_Project_Milestone_TS1_Navigation_UI extends VTiger_CRM_BaseClass {

	// TC-VT-PM-06
	@Test(groups = { "system" })
	public void TC_VT_PM_06_VerifyProjectMilestonesNavigation() {

		Reporter.log("START: TC-VT-PM-06", true);

		homePage.openProjectMilestonesModule(driver);
		milestonePage.verifyModuleOpened();

		Reporter.log("PASS: TC-VT-PM-06", true);
	}

	// TC-VT-PM-05
	@Test(groups = { "system", "EP" })
	public void TC_VT_PM_05_VerifyNewMilestoneFormFields() {

		Reporter.log("START: TC-VT-PM-05", true);

		homePage.openProjectMilestonesModule(driver);
		milestonePage.openNewMilestoneForm();
		milestonePage.verifyFormFields();

		Reporter.log("PASS: TC-VT-PM-05", true);
	}

	// TC-VT-PM-10
	@Test(groups = { "integration", "smoke" })
	public void TC_VT_PM_10_AddIconOpensNewForm() {

		Reporter.log("START: TC-VT-PM-10", true);

		homePage.openProjectMilestonesModule(driver);
		milestonePage.openNewMilestoneForm();
		milestonePage.verifyVisible(milestonePage.milestoneName, "Milestone Name");

		Reporter.log("PASS: TC-VT-PM-10", true);
	}
}