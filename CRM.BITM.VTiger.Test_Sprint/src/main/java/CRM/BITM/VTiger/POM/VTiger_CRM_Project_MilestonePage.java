package CRM.BITM.VTiger.POM;

import java.time.Duration;
import java.util.List;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.Reporter;

import CRM.BITM.VTiger.Utility.VTiger_CRM_WebDriverUtility;

public class VTiger_CRM_Project_MilestonePage {

	public WebDriver driver;
	public WebDriverWait wait;
	public VTiger_CRM_WebDriverUtility wi;

	public VTiger_CRM_Project_MilestonePage(WebDriver driver, WebDriverWait wait, VTiger_CRM_WebDriverUtility wi) {

		if (driver == null || wait == null || wi == null) {
			throw new IllegalArgumentException("Driver, wait and WebDriver utility are required.");
		}

		this.driver = driver;
		this.wait = wait;
		this.wi = wi;
	}

	// New Project Milestone form locators
	public By addIcon = By.xpath("//img[contains(@title,'Create') or contains(@alt,'Create')]");

	public By milestoneName = By.name("projectmilestonename");

	// Project Milestones list-page search field
	public By milestoneSearchField = By.name("search_text");

	// Project Milestones search filter dropdown
	public By milestoneSearchDropdown = By.id("bas_searchfield");

	public By milestoneSearchButton = By.xpath("//input[@name='submit' and @value=' Search Now ']");

	// Related To: visible project name and hidden project ID
	public By relatedProject = By.id("projectid_display");
	public By relatedProjectId = By.id("projectid");

	// Select icon beside Related To
	public By relatedProjectSelectIcon = By.xpath("//img[@title='Select' and @alt='Select']");

	public By assignedUser = By.name("assigned_user_id");
	public By milestoneDate = By.name("projectmilestonedate");
	public By milestoneType = By.name("projectmilestonetype");
	public By description = By.name("description");

	public By saveButton = By
			.xpath("//input[@type='submit' and contains(@value,'Save')]" + " | //button[normalize-space()='Save']");

	public By cancelButton = By
			.xpath("//input[@type='button' and contains(@value,'Cancel')]" + " | //button[normalize-space()='Cancel']");

	// Verify Project Milestones module is open
	public void verifyModuleOpened() {

		wait.until(ExpectedConditions.urlContains("module=ProjectMilestone"));

		Assert.assertTrue(driver.getCurrentUrl().contains("module=ProjectMilestone"),
				"Project Milestones module did not open.");

		Reporter.log("Project Milestones module verified.", true);
	}

	// Open New Project Milestone form
	public void openNewMilestoneForm() {

		Reporter.log("Opening New Project Milestone form.", true);

		WebElement add = wait.until(ExpectedConditions.elementToBeClickable(addIcon));

		add.click();

		wait.until(ExpectedConditions.visibilityOfElementLocated(milestoneName));

		Reporter.log("New Project Milestone form opened.", true);
	}

	// Verify expected form fields
	public void verifyFormFields() {

		verifyVisible(milestoneName, "Milestone Name");
		verifyVisible(relatedProject, "Related Project");
		verifyVisible(assignedUser, "Assigned User");
		verifyVisible(milestoneDate, "Milestone Date");
		verifyVisible(milestoneType, "Milestone Type");
		verifyVisible(description, "Description");
		verifyVisible(saveButton, "Save button");
		verifyVisible(cancelButton, "Cancel button");

		Reporter.log("Milestone form fields verified.", true);
	}

	// Verify that a field is visible
	public void verifyVisible(By locator, String fieldName) {

		WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));

		Assert.assertTrue(element.isDisplayed(), fieldName + " is not visible.");

		Reporter.log(fieldName + " is visible.", true);
	}

	// Enter milestone name
	public void enterMilestoneName(String value) {
		enterText(milestoneName, value, "Milestone Name");
	}

	// Enter milestone date
	public void enterMilestoneDate(String value) {
		enterText(milestoneDate, value, "Milestone Date");
	}

	// Enter description
	public void enterDescription(String value) {
		enterText(description, value, "Description");
	}

	// Common method to enter text
	public void enterText(By locator, String value, String fieldName) {

		if (value == null || value.trim().isEmpty()) {
			throw new IllegalArgumentException(fieldName + " must not be blank.");
		}

		WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));

		element.clear();
		element.sendKeys(value.trim());

		Reporter.log(fieldName + " entered.", true);
	}

	// Select Related Project using the lookup popup
	public void selectRelatedProject(String projectName) {

		if (projectName == null || projectName.trim().isEmpty()) {
			throw new IllegalArgumentException("Related Project name must not be blank.");
		}

		String expectedProject = projectName.trim();
		String mainWindow = driver.getWindowHandle();
		Set<String> windowsBefore = driver.getWindowHandles();

		Reporter.log("Opening Project lookup popup for: " + expectedProject, true);

		wait.until(ExpectedConditions.elementToBeClickable(relatedProjectSelectIcon)).click();

		WebDriverWait popupWait = new WebDriverWait(driver, Duration.ofSeconds(15));

		String popupWindow = popupWait.until(d -> {

			Set<String> currentWindows = d.getWindowHandles();

			for (String handle : currentWindows) {
				if (!windowsBefore.contains(handle)) {
					return handle;
				}
			}

			return null;
		});

		try {
			driver.switchTo().window(popupWindow);

			Reporter.log("Project lookup popup opened.", true);

			By searchBoxLocator = By.xpath("//input[@type='text' and not(@readonly)]");

			WebElement searchBox = popupWait.until(ExpectedConditions.visibilityOfElementLocated(searchBoxLocator));

			searchBox.clear();
			searchBox.sendKeys(expectedProject);

			By searchButtonLocator = By.xpath("//input[contains(normalize-space(@value),'Search Now')]"
					+ " | //button[contains(normalize-space(.),'Search Now')]");

			popupWait.until(ExpectedConditions.elementToBeClickable(searchButtonLocator)).click();

			By projectLinkLocator = By.xpath("//a[normalize-space(.)=" + xpathLiteral(expectedProject) + "]");

			WebElement projectLink;

			try {
				projectLink = popupWait.until(ExpectedConditions.elementToBeClickable(projectLinkLocator));
			} catch (org.openqa.selenium.TimeoutException e) {
				throw new AssertionError("Project '" + expectedProject + "' was not found in the Project lookup popup. "
						+ "Create the project or use an existing project name.", e);
			}

			projectLink.click();

			Reporter.log("Project selected from lookup popup: " + expectedProject, true);

		} finally {
			if (driver.getWindowHandles().contains(mainWindow)) {
				driver.switchTo().window(mainWindow);
			}
		}

		wait.until(ExpectedConditions.textToBePresentInElementValue(relatedProject, expectedProject));

		wait.until(d -> {
			String projectId = d.findElement(relatedProjectId).getAttribute("value");

			return projectId != null && !projectId.trim().isEmpty();
		});

		Reporter.log("Related Project successfully populated: " + expectedProject, true);
	}

	// Select Assigned User
	public void selectAssignedUser(String userName) {
		selectOption(assignedUser, userName, "Assigned User");
	}

	// Select milestone type
	public void selectMilestoneType(String type) {
		selectOption(milestoneType, type, "Milestone Type");
	}

	// Select a native dropdown option or a visible custom option
	public void selectOption(By locator, String optionText, String fieldName) {

		if (optionText == null || optionText.trim().isEmpty()) {
			throw new IllegalArgumentException(fieldName + " must not be blank.");
		}

		String expectedText = optionText.trim();

		WebElement field = wait.until(ExpectedConditions.elementToBeClickable(locator));

		if ("select".equalsIgnoreCase(field.getTagName())) {

			Select select = new Select(field);
			select.selectByVisibleText(expectedText);

		} else {

			field.click();

			By optionLocator = By.xpath("//*[self::option or self::a or self::div " + "or self::li or self::span]"
					+ "[normalize-space(.)=" + xpathLiteral(expectedText) + "]");

			WebElement matchingOption = wait.until(d -> {

				List<WebElement> options = d.findElements(optionLocator);

				for (WebElement option : options) {
					if (option.isDisplayed() && option.isEnabled()
							&& option.getText().trim().equalsIgnoreCase(expectedText)) {
						return option;
					}
				}

				return null;
			});

			matchingOption.click();
		}

		Reporter.log(fieldName + " selected: " + expectedText, true);
	}

	// Safely create an XPath string literal
	public String xpathLiteral(String value) {

		if (value == null) {
			throw new IllegalArgumentException("XPath value must not be null.");
		}

		if (!value.contains("'")) {
			return "'" + value + "'";
		}

		if (!value.contains("\"")) {
			return "\"" + value + "\"";
		}

		String[] parts = value.split("'", -1);
		StringBuilder result = new StringBuilder("concat(");

		for (int i = 0; i < parts.length; i++) {

			if (i > 0) {
				result.append(", \"'\", ");
			}

			result.append("'").append(parts[i]).append("'");
		}

		result.append(")");

		return result.toString();
	}

	// Save milestone
	public void saveMilestone() {

		WebElement save = wait.until(ExpectedConditions.elementToBeClickable(saveButton));

		save.click();

		Reporter.log("Save button clicked.", true);
	}

	// Verify saved milestone on the detail page
	public void verifyMilestoneVisible(String name) {

		if (name == null || name.trim().isEmpty()) {
			throw new IllegalArgumentException("Milestone name must not be blank.");
		}

		String expectedName = name.trim();

		wait.until(ExpectedConditions.urlContains("action=DetailView"));

		wait.until(ExpectedConditions.urlContains("module=ProjectMilestone"));

		boolean nameFound = wait.until(d -> {

			String pageText = d.findElement(By.tagName("body")).getText();

			if (pageText != null && pageText.contains(expectedName)) {
				return true;
			}

			for (WebElement element : d.findElements(By.xpath("//*"))) {

				if (!element.isDisplayed()) {
					continue;
				}

				String text = element.getText();

				if (text != null && text.trim().equals(expectedName)) {
					return true;
				}

				String value = element.getAttribute("value");

				if (value != null && value.trim().equals(expectedName)) {
					return true;
				}
			}

			return false;
		});

		Assert.assertTrue(nameFound, "Saved milestone name was not found on the detail page: " + expectedName);

		Reporter.log("Saved milestone verified: " + expectedName, true);
	}

	// Mandatory validation
	public void verifyMandatoryValidation() {

		wait.until(ExpectedConditions.elementToBeClickable(saveButton)).click();

		Reporter.log("Save button clicked for mandatory validation.", true);

		org.openqa.selenium.Alert alert = wait.until(ExpectedConditions.alertIsPresent());

		String alertText = alert.getText();

		Assert.assertTrue(alertText.contains("Milestone Name cannot be empty"),
				"Expected mandatory validation alert was not displayed. " + "Actual alert: " + alertText);

		Reporter.log("Mandatory validation alert verified: " + alertText, true);

		alert.accept();

		Reporter.log("Mandatory validation alert accepted.", true);
	}

	public void searchMilestone(String searchName) {

		if (searchName == null || searchName.trim().isEmpty()) {
			throw new IllegalArgumentException("Milestone search name must not be blank.");
		}

		// Select Milestone Name as the search field
		WebElement dropdown = wait.until(ExpectedConditions.elementToBeClickable(milestoneSearchDropdown));

		new Select(dropdown).selectByVisibleText("Milestone Name");

		// Enter the search value
		WebElement searchField = wait.until(ExpectedConditions.visibilityOfElementLocated(milestoneSearchField));

		searchField.clear();
		searchField.sendKeys(searchName.trim());

		// Click Search Now
		wait.until(ExpectedConditions.elementToBeClickable(milestoneSearchButton)).click();

		Reporter.log("Milestone search submitted for: " + searchName, true);
	}
}
