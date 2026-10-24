package CRM.BITM.VTiger.POM;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Reporter;

public class VTiger_CRM_ProjectPage {

	public WebDriver driver;
	public WebDriverWait wait;

	// ==================== Project Form Locators ====================

	public By projectName = By.name("projectname");
	public By startDate = By.name("startdate");
	public By targetEndDate = By.name("targetenddate");
	public By actualEndDate = By.name("actualenddate");
	public By relatedTo = By.id("linktoaccountscontacts_display");
	public By targetBudget = By.name("targetbudget");
	public By projectUrl = By.name("projecturl");
	public By description = By.name("description");

	public By saveButton = By.xpath("//input[@type='submit' and @name='button' and contains(@class,'save')]");

	public By detailSaveButton = By.xpath(
			"//input[@type='submit' and @name='button' " + "and contains(@class,'save') and contains(@title,'Save')]");

	public By detailDeleteButton = By.xpath("//input[@type='button' and @name='Delete' "
			+ "and contains(@class,'delete') and contains(@title,'Delete')]");

	public By detailEditButton = By.xpath(
			"//input[@type='button' and @name='Edit' " + "and contains(@class,'edit') and contains(@title,'Edit')]");

	// ==================== Projects List Locators ====================

	public By projectSearchField = By.name("search_text");

	public By projectResultLinks = By.xpath(
			"//a[@title='Projects' and contains(@href,'module=Project') " + "and contains(@href,'action=DetailView')]");

	// ==================== Inline Progress Locators ====================

	public By progressCell = By.id("mouseArea_Progress");
	public By progressDisplay = By.id("dtlview_Progress");
	public By progressDropdown = By.id("txtbox_Progress");
	public By progressSaveButton = By.name("button_Progress");

	// ==================== Constructor ====================

	public VTiger_CRM_ProjectPage(WebDriver driver, WebDriverWait wait) {
		if (driver == null || wait == null) {
			throw new IllegalArgumentException("Driver and wait are required.");
		}

		this.driver = driver;
		this.wait = wait;
	}

	// ==================== Open Projects Module ====================

	public void openProjectsModule() {
		String currentUrl = driver.getCurrentUrl();
		int index = currentUrl.indexOf("index.php");

		if (index < 0) {
			throw new IllegalStateException("Cannot determine application base URL from: " + currentUrl);
		}

		driver.get(currentUrl.substring(0, index) + "index.php?module=Project&action=index");

		wait.until(ExpectedConditions.urlContains("module=Project"));
		Reporter.log("Projects module opened.", true);
	}

	// ==================== Open New Project Form ====================

	public void openNewProjectForm() {
		By addIcon = By.xpath("//img[contains(@title,'Create') or contains(@alt,'Create')]");

		wait.until(ExpectedConditions.elementToBeClickable(addIcon)).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(projectName));

		Reporter.log("New Project form opened successfully.", true);
	}

	// ==================== Enter Project Name ====================

	public void enterProjectName(String name) {
		if (name == null || name.trim().isEmpty()) {
			throw new IllegalArgumentException("Project name must not be blank.");
		}

		WebElement field = wait.until(ExpectedConditions.elementToBeClickable(projectName));

		field.clear();
		field.sendKeys(name.trim());

		Reporter.log("Project name entered.", true);
	}

	// ==================== Enter Project Description ====================

	public void enterDescription(String text) {
		if (text == null) {
			throw new IllegalArgumentException("Project description cannot be null.");
		}

		WebElement field = wait.until(ExpectedConditions.visibilityOfElementLocated(description));

		field.clear();
		field.sendKeys(text);

		Reporter.log("Project description entered.", true);
	}

	// ==================== Save New Project ====================

	public void saveProject() {
		wait.until(ExpectedConditions.elementToBeClickable(saveButton)).click();
		Reporter.log("Project Save button clicked.", true);
	}

	// ==================== Search Project ====================

	public void searchProject(String name) {
		if (name == null || name.trim().isEmpty()) {
			throw new IllegalArgumentException("Project search name must not be blank.");
		}

		WebDriverWait searchWait = new WebDriverWait(driver, Duration.ofSeconds(15));

		WebElement searchField = searchWait.until(ExpectedConditions.visibilityOfElementLocated(projectSearchField));

		searchField.clear();
		searchField.sendKeys(name.trim());

		By searchNowButton = By.xpath("//*[self::input or self::button or self::a]"
				+ "[normalize-space(@value)='Search Now' " + "or normalize-space(.)='Search Now']");

		searchWait.until(ExpectedConditions.elementToBeClickable(searchNowButton)).click();

		searchWait.until(ExpectedConditions.visibilityOfElementLocated(projectSearchField));

		Reporter.log("Project search submitted for: " + name.trim(), true);
	}

	// ==================== Verify Project Exists ====================

	public boolean isProjectListed(String name) {
		if (name == null || name.trim().isEmpty()) {
			throw new IllegalArgumentException("Project name must not be blank.");
		}

		String expectedName = name.trim();
		WebDriverWait resultWait = new WebDriverWait(driver, Duration.ofSeconds(5));

		try {
			return resultWait.until(d -> {
				List<WebElement> links = d.findElements(projectResultLinks);

				for (WebElement link : links) {
					try {
						if (link.isDisplayed() && link.getText().trim().equalsIgnoreCase(expectedName)) {
							return true;
						}
					} catch (StaleElementReferenceException ignored) {
						// Results refreshed during inspection; retry.
					}
				}

				return false;
			});
		} catch (TimeoutException e) {
			return false;
		}
	}

	// ==================== Ensure Project Exists ====================

	/**
	 * Reuses a Project with the expected name and restores its description if
	 * needed. Creates the expected Project if no matching name is found.
	 *
	 * This method cannot identify a renamed record as the original record. It also
	 * does not restore fields other than Description.
	 */
	public void ensureProjectExists(String name, String expectedDescription) {

		if (name == null || name.trim().isEmpty()) {
			throw new IllegalArgumentException("Project name must not be blank.");
		}

		if (expectedDescription == null) {
			throw new IllegalArgumentException("Expected Project description must not be null.");
		}

		String expectedName = name.trim();

		// Search for the expected Project.
		openProjectsModule();
		searchProject(expectedName);

		if (isProjectListed(expectedName)) {
			Reporter.log("Project found: " + expectedName, true);

			// Open the matching Project and inspect its description.
			openProjectDetails(expectedName);
			openEditProjectForm();

			WebElement descriptionField = wait.until(ExpectedConditions.visibilityOfElementLocated(description));

			String actualDescription = descriptionField.getAttribute("value");

			if (actualDescription == null) {
				actualDescription = "";
			}

			if (!actualDescription.equals(expectedDescription)) {
				descriptionField.clear();
				descriptionField.sendKeys(expectedDescription);

				saveProjectDetails();

				wait.until(ExpectedConditions.visibilityOfElementLocated(detailEditButton));

				Reporter.log("Project description restored: " + expectedName, true);
			} else {
				// Leave the edit form without saving.
				openProjectsModule();

				Reporter.log("Project description already matches expected data.", true);
			}

			return;
		}

		// Project is missing: create it using the existing POM methods.
		Reporter.log("Project not found; recreating: " + expectedName, true);

		openProjectsModule();
		openNewProjectForm();
		enterProjectName(expectedName);
		enterDescription(expectedDescription);
		saveProject();

		// Wait for the form to close before returning to the list.
		wait.until(d -> d.findElements(projectName).stream().noneMatch(element -> {
			try {
				return element.isDisplayed();
			} catch (StaleElementReferenceException e) {
				return false;
			}
		}));

		openProjectsModule();
		searchProject(expectedName);

		if (!isProjectListed(expectedName)) {
			throw new AssertionError("Project creation could not be verified: " + expectedName);
		}

		Reporter.log("Project created and verified: " + expectedName, true);
	}

	// ==================== Open Project Details ====================

	public void openProjectDetails(String name) {
		if (name == null || name.trim().isEmpty()) {
			throw new IllegalArgumentException("Project name must not be blank.");
		}

		searchProject(name);

		WebDriverWait resultWait = new WebDriverWait(driver, Duration.ofSeconds(15));

		WebElement matchingLink = resultWait.until(d -> {
			List<WebElement> links = d.findElements(projectResultLinks);

			for (WebElement link : links) {
				try {
					if (link.isDisplayed() && link.getText().trim().equalsIgnoreCase(name.trim())) {
						return link;
					}
				} catch (StaleElementReferenceException ignored) {
					// Retry if the results are refreshed.
				}
			}

			return null;
		});

		matchingLink.click();

		wait.until(ExpectedConditions.visibilityOfElementLocated(detailEditButton));

		Reporter.log("Project detail page opened.", true);
	}

	// ==================== Open Edit Project Form ====================

	public void openEditProjectForm() {
		wait.until(ExpectedConditions.elementToBeClickable(detailEditButton)).click();

		wait.until(ExpectedConditions.visibilityOfElementLocated(projectName));

		Reporter.log("Edit Project form opened.", true);
	}

	// ==================== Save Project Details ====================

	public void saveProjectDetails() {
		wait.until(ExpectedConditions.elementToBeClickable(detailSaveButton)).click();

		Reporter.log("Project detail Save button clicked.", true);
	}

	// ==================== Delete Project ====================

	public void deleteProjectDetails() {
		wait.until(ExpectedConditions.elementToBeClickable(detailDeleteButton)).click();

		Alert confirmation = wait.until(ExpectedConditions.alertIsPresent());

		String message = confirmation.getText();

		if (!message.contains("Are you sure you want to delete this record?")) {
			confirmation.dismiss();
			throw new AssertionError("Unexpected delete confirmation: " + message);
		}

		confirmation.accept();

		Reporter.log("Project deletion confirmation accepted.", true);
	}

	// ==================== Update Project Progress ====================

	public void updateProgress(String progress) {
		if (progress == null || progress.trim().isEmpty()) {
			throw new IllegalArgumentException("Progress value cannot be empty.");
		}

		WebElement cell = wait.until(ExpectedConditions.visibilityOfElementLocated(progressCell));

		new Actions(driver).moveToElement(cell).perform();

		WebElement editArea = driver.findElement(By.id("editarea_Progress"));

		((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].style.display='block';",
				editArea);

		WebElement dropdown = wait.until(ExpectedConditions.visibilityOfElementLocated(progressDropdown));

		new Select(dropdown).selectByVisibleText(progress.trim());

		wait.until(ExpectedConditions.elementToBeClickable(progressSaveButton)).click();

		wait.until(d -> d.findElement(progressDisplay).getText().trim().equals(progress.trim()));

		Reporter.log("Project progress updated successfully to: " + progress.trim(), true);
	}

	// ==================== Read Project Progress ====================

	public String getProjectProgress() {
		return wait.until(ExpectedConditions.visibilityOfElementLocated(progressDisplay)).getText().trim();
	}
}
