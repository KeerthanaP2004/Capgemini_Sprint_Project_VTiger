package CRM.BITM.VTiger.Project_Task;

import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Test;

import CRM.BITM.VTiger.Base_Class.VTiger_CRM_BaseClass;

public class VTiger_CRM_ProjectTask_TS2 extends VTiger_CRM_BaseClass {

	@Test(groups = { "integration" })
	public void TC_VT_PT_02_VerifyRelatedProjectSelection() throws Exception {

		Reporter.log("START: TC-VT-PT-02", true);

		String projectName = excel.readDataByHeader("ProjectTask", 1, "Related Project").trim();

		Assert.assertFalse(projectName.isEmpty(), "Related Project must be available in Excel.");

		homePage.openProjectTasksModule();
		projectTaskPage.openNewTaskForm();
		projectTaskPage.selectRelatedProject(projectName);

		Assert.assertEquals(driver.findElement(projectTaskPage.relatedProject).getAttribute("value"), projectName,
				"The selected project name does not match the Excel data.");

		Assert.assertFalse(driver.findElement(projectTaskPage.relatedProjectId).getAttribute("value").trim().isEmpty(),
				"The related project's hidden ID was not populated.");

		Reporter.log("PASS: TC-VT-PT-02", true);
	}

	@Test(groups = { "integration", "system" })
	public void TC_VT_PT_12_EndToEndCreateAndVerifyTask() throws Exception {

		Reporter.log("START: TC-VT-PT-12", true);

		String taskName = excel.readDataByHeader("ProjectTask", 1, "Task Name").trim();
		String projectName = excel.readDataByHeader("ProjectTask", 1, "Related Project").trim();
		String startDate = excel.readDataByHeader("ProjectTask", 1, "Start Date").trim();
		String endDate = excel.readDataByHeader("ProjectTask", 1, "End Date").trim();

		Assert.assertFalse(taskName.isEmpty(), "Task Name is empty in Excel.");
		Assert.assertFalse(projectName.isEmpty(), "Related Project is empty in Excel.");
		Assert.assertFalse(startDate.isEmpty(), "Start Date is empty in Excel.");
		Assert.assertFalse(endDate.isEmpty(), "End Date is empty in Excel.");

		homePage.openProjectTasksModule();
		projectTaskPage.openNewTaskForm();
		projectTaskPage.enterTaskName(taskName);
		projectTaskPage.enterTaskDates(startDate, endDate);
		projectTaskPage.selectRelatedProject(projectName);
		projectTaskPage.saveTask();
		projectTaskPage.verifyTaskSaved(taskName);

		Reporter.log("PASS: TC-VT-PT-12", true);
	}
}