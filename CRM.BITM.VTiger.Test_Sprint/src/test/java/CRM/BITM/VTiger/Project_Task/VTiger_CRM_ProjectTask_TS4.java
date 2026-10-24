package CRM.BITM.VTiger.Project_Task;

import java.io.IOException;

import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Test;

import CRM.BITM.VTiger.Base_Class.VTiger_CRM_BaseClass;
import Screenshot.Screenshot;

public class VTiger_CRM_ProjectTask_TS4 extends VTiger_CRM_BaseClass {

	@Test(groups = { "negative" })
	public void TC_VT_PT_07_VerifyMandatoryTaskNameField() throws IOException {

		Reporter.log("START: TC-VT-PT-07", true);

		homePage.openProjectTasksModule();
		projectTaskPage.openNewTaskForm();

		Assert.assertTrue(driver.findElement(projectTaskPage.taskName).isDisplayed(), "Task Name field is missing.");

		Assert.assertTrue(driver.findElement(projectTaskPage.taskName).getAttribute("name").equals("projecttaskname"),
				"Unexpected Task Name field locator.");

		Screenshot.takeScreenshot(driver, "TC-VT-PT-07_MandatoryTaskNameField");

		Reporter.log("PASS: Task Name field is present. Actual mandatory validation "
				+ "still needs to be verified by submitting an empty form.", true);
	}

	@Test(groups = { "negative", "functionality" })
	public void TC_VT_PT_08_VerifyTaskSearchControlsAvailable() throws IOException {

		Reporter.log("START: TC-VT-PT-08", true);

		homePage.openProjectTasksModule();

		By searchField = By.name("search_text");

		Assert.assertTrue(driver.findElements(searchField).size() > 0,
				"Task search field was not found on the list page.");

		Screenshot.takeScreenshot(driver, "TC-VT-PT-08_TaskSearchControlsAvailable");

		Reporter.log("PASS: Task search field is available.", true);
	}
}