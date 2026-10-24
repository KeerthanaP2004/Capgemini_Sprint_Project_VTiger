package CRM.BITM.VTiger.Project_Task;

import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Test;

import CRM.BITM.VTiger.Base_Class.VTiger_CRM_BaseClass;

public class VTiger_CRM_ProjectTask_TS5 extends VTiger_CRM_BaseClass {

	@Test(groups = { "smoke", "system", "ui" })
	public void TC_VT_PT_05_VerifyNewTaskFormFields() {

		Reporter.log("START: TC-VT-PT-05", true);

		homePage.openProjectTasksModule();
		projectTaskPage.openNewTaskForm();

		Assert.assertTrue(driver.findElement(projectTaskPage.taskName).isDisplayed(),
				"Task Name field is not displayed.");

		Assert.assertTrue(driver.findElement(projectTaskPage.startDate).isDisplayed(),
				"Start Date field is not displayed.");

		Assert.assertTrue(driver.findElement(projectTaskPage.endDate).isDisplayed(),
				"End Date field is not displayed.");

		Assert.assertTrue(driver.findElement(projectTaskPage.relatedProject).isDisplayed(),
				"Related Project field is not displayed.");

		Reporter.log("PASS: TC-VT-PT-05", true);
	}

	@Test(groups = { "smoke", "system", "ui" })
	public void TC_VT_PT_06_VerifyProjectTasksNavigation() {

		Reporter.log("START: TC-VT-PT-06", true);

		homePage.openProjectTasksModule();

		Assert.assertTrue(driver.getCurrentUrl().contains("module=ProjectTask"), "Project Tasks module did not open.");

		Reporter.log("PASS: TC-VT-PT-06", true);
	}

	@Test(groups = { "integration" })
	public void TC_VT_PT_10_VerifyAddIconOpensTaskForm() {

		Reporter.log("START: TC-VT-PT-10", true);

		homePage.openProjectTasksModule();
		projectTaskPage.openNewTaskForm();

		Assert.assertTrue(driver.findElement(projectTaskPage.taskName).isDisplayed(),
				"The new-task form did not open after clicking Add.");

		Reporter.log("PASS: TC-VT-PT-10", true);
	}

	@Test(groups = { "system", "EP", "ui", "usability" })
	public void TC_VT_PT_13_VerifyTaskFormControls() {

		Reporter.log("START: TC-VT-PT-13", true);

		homePage.openProjectTasksModule();
		projectTaskPage.openNewTaskForm();

		Assert.assertTrue(driver.findElement(projectTaskPage.taskName).isEnabled(), "Task Name field is disabled.");

		Assert.assertTrue(driver.findElement(projectTaskPage.startDate).isEnabled(), "Start Date field is disabled.");

		Assert.assertTrue(driver.findElement(projectTaskPage.endDate).isEnabled(), "End Date field is disabled.");

		Assert.assertTrue(driver.findElement(projectTaskPage.saveButton).isDisplayed(),
				"Save button is not displayed.");

		Reporter.log("PASS: TC-VT-PT-13", true);
	}
}