package CRM.BITM.VTiger.Project;

import java.io.IOException;
import java.time.Duration;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Test;

import CRM.BITM.VTiger.Base_Class.VTiger_CRM_BaseClass;
import Screenshot.Screenshot;

public class VTiger_CRM_Project_TS2 extends VTiger_CRM_BaseClass {

	@Test(groups = { "negative", "BVA" })
	public void TC_VT_PROJ_07_VerifyBlankProjectNameValidation() throws IOException {

		homePage.openProjectsModule();
		projectPage.openNewProjectForm();

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

		WebElement nameField = wait.until(ExpectedConditions.visibilityOfElementLocated(projectPage.projectName));

		// Leave Project Name blank
		nameField.clear();

		// Submit the form; VTiger displays a JavaScript alert
		projectPage.saveProject();

		// Wait for the validation alert
		org.openqa.selenium.Alert alert = wait.until(ExpectedConditions.alertIsPresent());

		// Verify the actual application validation message
		String alertMessage = alert.getText();

		Assert.assertEquals(alertMessage.trim(), "Project Name cannot be empty", "Unexpected validation message.");

		Reporter.log("TC-VT-PROJ-07: Correct blank-name validation alert displayed.", true);

		// Dismiss the alert so the browser can continue
		alert.accept();

		// Confirm the user remains on the project form
		wait.until(ExpectedConditions.visibilityOfElementLocated(projectPage.projectName));

		Screenshot.takeScreenshot(driver, "TC-VT-PROJ-07_BlankProjectNameValidation");

		Reporter.log("TC-VT-PROJ-07 passed: Blank Project Name was rejected.", true);
	}

	@Test(groups = { "system", "EP" })
	public void TC_VT_PROJ_13_VerifyProjectFormUsability() {
		homePage.openProjectsModule();
		projectPage.openNewProjectForm();

		Assert.assertTrue(driver.findElement(projectPage.projectName).isEnabled());

		Assert.assertTrue(driver.findElement(projectPage.startDate).isEnabled());

		Assert.assertTrue(driver.findElement(projectPage.targetEndDate).isEnabled());

		Assert.assertTrue(driver.findElement(projectPage.description).isEnabled());

		Assert.assertTrue(driver.findElement(projectPage.saveButton).isEnabled());

		Reporter.log("TC-VT-PROJ-13 basic usability checked.", true);
	}
}
