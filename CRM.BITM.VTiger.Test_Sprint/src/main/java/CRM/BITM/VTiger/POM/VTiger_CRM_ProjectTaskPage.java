package CRM.BITM.VTiger.POM;

import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.Reporter;

public class VTiger_CRM_ProjectTaskPage {

	public WebDriver driver;
	public WebDriverWait wait;

	public By taskName = By.name("projecttaskname");
	public By relatedProject = By.id("projectid_display");
	public By relatedProjectId = By.id("projectid");
	public By startDate = By.name("startdate");
	public By endDate = By.name("enddate");

	public By saveButton = By.xpath("//input[@type='submit' and @name='button' and contains(@class,'save')]");

	public VTiger_CRM_ProjectTaskPage(WebDriver driver, WebDriverWait wait) {

		if (driver == null || wait == null) {
			throw new IllegalArgumentException("Driver and wait are required.");
		}

		this.driver = driver;
		this.wait = wait;
	}

	public void openNewTaskForm() {

		By addIcon = By.xpath("//img[contains(@title,'Create') or contains(@alt,'Create')]");

		wait.until(ExpectedConditions.elementToBeClickable(addIcon)).click();

		wait.until(ExpectedConditions.visibilityOfElementLocated(taskName));

		Reporter.log("New Project Task form opened.", true);
	}

	public void enterTaskName(String name) {

		if (name == null || name.trim().isEmpty()) {
			throw new IllegalArgumentException("Task name must not be blank.");
		}

		WebElement field = wait.until(ExpectedConditions.elementToBeClickable(taskName));

		field.clear();
		field.sendKeys(name.trim());

		Reporter.log("Task name entered: " + name, true);
	}

	public void enterTaskDates(String start, String end) {

		enterDate(startDate, start, "Task Start Date");
		enterDate(endDate, end, "Task End Date");
	}

	private void enterDate(By locator, String value, String fieldName) {

		if (value == null || value.trim().isEmpty()) {
			throw new IllegalArgumentException(fieldName + " must not be empty.");
		}

		String normalizedDate = value.trim();

		try {
			java.time.LocalDate parsedDate = null;

			java.time.format.DateTimeFormatter[] formats = { java.time.format.DateTimeFormatter.ofPattern("M/d/yy"),
					java.time.format.DateTimeFormatter.ofPattern("M/d/yyyy"),
					java.time.format.DateTimeFormatter.ofPattern("M-d-yyyy"),
					java.time.format.DateTimeFormatter.ISO_LOCAL_DATE };

			for (java.time.format.DateTimeFormatter format : formats) {
				try {
					parsedDate = java.time.LocalDate.parse(normalizedDate, format);
					break;
				} catch (java.time.format.DateTimeParseException ignored) {
					// Try the next supported format.
				}
			}

			if (parsedDate == null) {
				throw new IllegalArgumentException("Unsupported date format for " + fieldName + ": " + normalizedDate);
			}

			normalizedDate = parsedDate.toString();

		} catch (java.time.DateTimeException e) {
			throw new IllegalArgumentException("Invalid date for " + fieldName + ": " + value, e);
		}

		WebElement element = wait
				.until(org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated(locator));

		element.clear();
		element.sendKeys(normalizedDate);

		String actualValue = element.getAttribute("value");

		Reporter.log(fieldName + " entered: " + actualValue, true);

		if (!normalizedDate.equals(actualValue)) {
			throw new AssertionError(fieldName + " mismatch. Expected " + normalizedDate + " but found " + actualValue);
		}
	}

	public void selectRelatedProject(String projectName) {

		if (projectName == null || projectName.trim().isEmpty()) {
			throw new IllegalArgumentException("Related Project must not be blank.");
		}

		String expectedProject = projectName.trim();
		String mainWindow = driver.getWindowHandle();
		Set<String> oldWindows = driver.getWindowHandles();

		/*
		 * VTiger normally opens the project selector using the Select icon beside the
		 * Related Project field.
		 */
		By selectIcon = By.xpath("//input[@id='projectid_display']" + "/following::img[@title='Select'][1]");

		WebElement selectButton = wait.until(ExpectedConditions.elementToBeClickable(selectIcon));

		selectButton.click();

		/*
		 * Wait for the selector popup.
		 */
		wait.until(d -> d.getWindowHandles().size() > oldWindows.size());

		for (String window : driver.getWindowHandles()) {
			if (!oldWindows.contains(window)) {
				driver.switchTo().window(window);
				break;
			}
		}

		try {
			By searchField = By.name("search_text");

			WebElement search = wait.until(ExpectedConditions.visibilityOfElementLocated(searchField));

			search.clear();
			search.sendKeys(expectedProject);

			// Updated: submit the project search using Enter.
			search.sendKeys(Keys.ENTER);

			// Select the exact project from the popup results.
			By projectResult = By.xpath("//a[normalize-space()=" + xpathLiteral(expectedProject) + "]");

			wait.until(ExpectedConditions.elementToBeClickable(projectResult)).click();

		} finally {
			/*
			 * Return to the task form if it is still open.
			 */
			if (driver.getWindowHandles().contains(mainWindow)) {
				driver.switchTo().window(mainWindow);
			}
		}

		/*
		 * Verify both the visible project name and its hidden ID.
		 */
		wait.until(d -> {
			WebElement display = d.findElement(relatedProject);
			WebElement hiddenId = d.findElement(relatedProjectId);

			String displayedName = display.getAttribute("value");
			String idValue = hiddenId.getAttribute("value");

			return displayedName != null && expectedProject.equals(displayedName.trim()) && idValue != null
					&& !idValue.trim().isEmpty();
		});

		Reporter.log("Related Project selected: " + expectedProject, true);
	}

	public void saveTask() {

		wait.until(ExpectedConditions.elementToBeClickable(saveButton)).click();

		Reporter.log("Project Task Save button clicked.", true);

		try {
			org.openqa.selenium.Alert alert = new WebDriverWait(driver, java.time.Duration.ofSeconds(3))
					.until(ExpectedConditions.alertIsPresent());

			String alertText = alert.getText();

			Reporter.log("Application validation alert: " + alertText, true);

			alert.accept();

			throw new AssertionError("Project Task was not saved. Application validation: " + alertText);

		} catch (org.openqa.selenium.TimeoutException e) {
			Reporter.log("No validation alert detected. " + "Verify the task save result separately.", true);
		}
	}

	public void verifyTaskSaved(String expectedTaskName) {

		By taskNameOnPage = By.xpath("//*[normalize-space()=" + xpathLiteral(expectedTaskName) + " or @value="
				+ xpathLiteral(expectedTaskName) + "]");

		wait.until(ExpectedConditions.or(ExpectedConditions.urlContains("action=DetailView"),
				ExpectedConditions.visibilityOfElementLocated(taskNameOnPage)));

		boolean displayed = false;

		for (WebElement element : driver.findElements(taskNameOnPage)) {
			if (element.isDisplayed()) {
				displayed = true;
				break;
			}
		}

		Assert.assertTrue(displayed, "Saved task was not displayed: " + expectedTaskName);

		Reporter.log("Task saved and verified: " + expectedTaskName, true);
	}

	private String xpathLiteral(String value) {

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
}
