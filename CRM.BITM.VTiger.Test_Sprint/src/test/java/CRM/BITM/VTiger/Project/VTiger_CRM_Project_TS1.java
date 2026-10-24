package CRM.BITM.VTiger.Project;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Test;

import CRM.BITM.VTiger.Base_Class.VTiger_CRM_BaseClass;

public class VTiger_CRM_Project_TS1 extends VTiger_CRM_BaseClass {

	@Test(groups = { "system", "smoke" })
	public void TC_VT_PROJ_06_VerifyProjectsNavigation() {
		homePage.openProjectsModule();

		Assert.assertTrue(driver.getCurrentUrl().matches(".*[?&]module=Project(&.*)?"), "Projects navigation failed.");

		Reporter.log("TC-VT-PROJ-06 passed.", true);
	}

	@Test(groups = { "system", "EP" })
	public void TC_VT_PROJ_05_VerifyNewProjectFormFields() {
		homePage.openProjectsModule();
		projectPage.openNewProjectForm();

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

		By[] fields = { projectPage.projectName, projectPage.startDate, projectPage.targetEndDate,
				projectPage.description, projectPage.saveButton };

		for (By field : fields) {
			Assert.assertTrue(wait.until(ExpectedConditions.visibilityOfElementLocated(field)).isDisplayed(),
					"Expected form element is not visible: " + field);
		}

		Reporter.log("TC-VT-PROJ-05 passed.", true);
	}

	@Test(groups = { "integration", "smoke" })
	public void TC_VT_PROJ_10_VerifyAddProjectFormOpens() {
		homePage.openProjectsModule();
		projectPage.openNewProjectForm();

		Assert.assertTrue(driver.findElement(projectPage.projectName).isDisplayed());

		Assert.assertTrue(driver.findElement(projectPage.saveButton).isDisplayed());

		Reporter.log("TC-VT-PROJ-10 passed.", true);
	}

	@Test(groups = { "system" })
	public void TC_VT_PROJ_09_VerifyProjectsModuleURL() {
		homePage.openProjectsModule();

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

		Assert.assertTrue(wait.until(d -> d.getCurrentUrl().matches(".*[?&]module=Project(&.*)?")),
				"Projects module URL was not reached.");

		Reporter.log("TC-VT-PROJ-09 navigation check passed.", true);
	}
}
