package CRM.BITM.VTiger.Project_Task;

import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Test;

import CRM.BITM.VTiger.Base_Class.VTiger_CRM_BaseClass;

public class VTiger_CRM_ProjectTask_TS1 extends VTiger_CRM_BaseClass {

	@Test(groups = { "functionality", "smoke" })
	public void TC_VT_PT_01_CreateProjectTask() throws Exception {

		Reporter.log("START: TC-VT-PT-01 - Create Project Task", true);

		String taskName = excel.readDataByHeader("ProjectTask", 1, "Task Name").trim();
		String projectName = excel.readDataByHeader("ProjectTask", 1, "Related Project").trim();
		String startDate = excel.readDataByHeader("ProjectTask", 1, "Start Date").trim();
		String endDate = excel.readDataByHeader("ProjectTask", 1, "End Date").trim();

		Assert.assertFalse(taskName.isEmpty(), "Task Name is empty in the ProjectTask Excel sheet.");
		Assert.assertFalse(projectName.isEmpty(), "Related Project is empty in the ProjectTask Excel sheet.");
		Assert.assertFalse(startDate.isEmpty(), "Start Date is empty in the ProjectTask Excel sheet.");
		Assert.assertFalse(endDate.isEmpty(), "End Date is empty in the ProjectTask Excel sheet.");

		homePage.openProjectTasksModule();

		projectTaskPage.openNewTaskForm();
		projectTaskPage.enterTaskName(taskName);
		projectTaskPage.enterTaskDates(startDate, endDate);
		projectTaskPage.selectRelatedProject(projectName);
		projectTaskPage.saveTask();
		projectTaskPage.verifyTaskSaved(taskName);

		Reporter.log("PASS: TC-VT-PT-01", true);
	}

	@Test(groups = { "functionality" })
	public void TC_VT_PT_11_VerifyTaskDataAvailableInExcel() throws Exception {

		Reporter.log("START: TC-VT-PT-11 - Validate Excel task data", true);

		String taskName = excel.readDataByHeader("ProjectTask", 1, "Task Name").trim();
		String projectName = excel.readDataByHeader("ProjectTask", 1, "Related Project").trim();

		Assert.assertFalse(taskName.isEmpty(), "Task Name must be provided in Excel.");
		Assert.assertFalse(projectName.isEmpty(), "Related Project must be provided in Excel.");

		Reporter.log("PASS: Required task data is available in Excel.", true);
	}
}