package CRM.BITM.VTiger.Project_Task;

import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Test;

import CRM.BITM.VTiger.Base_Class.VTiger_CRM_BaseClass;

public class VTiger_CRM_ProjectTask_TS3 extends VTiger_CRM_BaseClass {

	@Test(groups = { "functionality" })
	public void TC_VT_PT_03_VerifyTaskFormCanBeOpened() {

		Reporter.log("START: TC-VT-PT-03", true);

		homePage.openProjectTasksModule();
		projectTaskPage.openNewTaskForm();

		Assert.assertTrue(driver.findElement(projectTaskPage.taskName).isDisplayed(),
				"Task Name field is not displayed.");

		Assert.assertTrue(driver.findElement(projectTaskPage.startDate).isDisplayed(),
				"Start Date field is not displayed.");

		Assert.assertTrue(driver.findElement(projectTaskPage.endDate).isDisplayed(),
				"End Date field is not displayed.");

		Reporter.log("PASS: Task form fields are displayed.", true);
	}

	@Test(groups = { "functionality" })
	public void TC_VT_PT_04_VerifyProgressFieldLocatorWhenAvailable() {

		Reporter.log("START: TC-VT-PT-04", true);

		homePage.openProjectTasksModule();
		projectTaskPage.openNewTaskForm();

		org.openqa.selenium.By progressField = org.openqa.selenium.By.name("projecttaskprogress");

		Assert.assertTrue(driver.findElements(progressField).size() > 0,
				"Progress field was not found. Confirm its name in the actual page HTML.");

		Reporter.log("PASS: Progress field is present.", true);
	}
}