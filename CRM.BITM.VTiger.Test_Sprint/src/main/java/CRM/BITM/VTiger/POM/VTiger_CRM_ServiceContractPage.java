package CRM.BITM.VTiger.POM;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Reporter;

public class VTiger_CRM_ServiceContractPage {

	private final WebDriver driver;
	private final WebDriverWait wait;

	// ==================== CONSTRUCTOR ====================

	public VTiger_CRM_ServiceContractPage(WebDriver driver, WebDriverWait wait) {

		if (driver == null) {
			throw new IllegalArgumentException("WebDriver must not be null.");
		}

		if (wait == null) {
			throw new IllegalArgumentException("WebDriverWait must not be null.");
		}

		this.driver = driver;
		this.wait = wait;
	}

	// ==================== LIST PAGE LOCATORS ====================

	private final String serviceContractsUrl = "module=ServiceContracts";

	// Create Service Contract button
	private final By createButton = By.xpath("//img[contains(@src,'btnL3Add.gif')]/ancestor::a[1]"
			+ " | //input[@title='Create Service Contract...']" + " | //input[@value='New Service Contract']");

	// Search field
	private final By searchField = By.name("search_text");

	// Search button candidates
	private final By searchButton = By.xpath(
			"//input[@name='submit' and " + "(@value='Search' or @title='Search')]" + " | //input[@value='Search']");

	// Search criteria dropdown
	private final By searchCriteria = By.xpath("//select[option[normalize-space()='Contract No']]");

	// ==================== PAGE VALIDATION ====================

	public boolean isServiceContractsListDisplayed() {

		try {
			wait.until(ExpectedConditions.urlContains(serviceContractsUrl));

			return true;

		} catch (TimeoutException e) {
			return false;
		}
	}

	// ==================== CREATE FORM NAVIGATION ====================

	public void clickCreateServiceContract() {

		Reporter.log("Opening the New Service Contract form.", true);

		if (!isServiceContractsListDisplayed()) {
			throw new IllegalStateException(
					"Service Contracts list page is not open. " + "Navigate to the module first.");
		}

		WebElement addButton = wait.until(ExpectedConditions.elementToBeClickable(createButton));

		addButton.click();

		wait.until(driver -> driver.getCurrentUrl().contains("module=ServiceContracts")
				&& driver.getCurrentUrl().contains("action=EditView"));

		Reporter.log("New Service Contract form opened. URL: " + driver.getCurrentUrl(), true);
	}

	// ==================== FORM PAGE VALIDATION ====================

	public boolean isNewServiceContractFormDisplayed() {

		try {
			return wait.until(driver -> driver.getCurrentUrl().contains("module=ServiceContracts")
					&& driver.getCurrentUrl().contains("action=EditView"));

		} catch (TimeoutException e) {
			return false;
		}
	}

	// ==================== SEARCH ====================

	public void searchServiceContract(String subject) {

		if (subject == null || subject.trim().isEmpty()) {
			throw new IllegalArgumentException("Service Contract search subject must not be empty.");
		}

		String expectedSubject = subject.trim();

		Reporter.log("Searching Service Contracts by Subject: " + expectedSubject, true);

		// Select Subject instead of Contract No
		WebElement criteriaDropdown = wait.until(ExpectedConditions.elementToBeClickable(searchCriteria));

		Select select = new Select(criteriaDropdown);
		select.selectByVisibleText("Subject");

		// Enter the subject from Excel
		WebElement searchInput = wait.until(ExpectedConditions.visibilityOfElementLocated(searchField));

		searchInput.clear();
		searchInput.sendKeys(expectedSubject);

		// Try clicking the Search button first
		boolean submitted = false;

		try {
			WebElement search = new WebDriverWait(driver, Duration.ofSeconds(3))
					.until(ExpectedConditions.elementToBeClickable(searchButton));

			search.click();
			submitted = true;

			Reporter.log("Search button clicked.", true);

		} catch (TimeoutException e) {
			Reporter.log("Search button not found. Submitting with Enter.", true);

			searchInput.sendKeys(Keys.ENTER);
			submitted = true;
		}

		if (submitted) {
			// Wait for a search-result state, not merely field visibility.
			// This checks for either a result row containing the subject
			// or the application's no-contract-found message.
			By matchingResult = By.xpath("//table//tr/td[normalize-space()=" + xpathLiteral(expectedSubject) + "]"
					+ " | //table//tr/td//a[normalize-space()=" + xpathLiteral(expectedSubject) + "]");

			By noResultsMessage = By.xpath("//*[contains(normalize-space(.)," + "'No Service Contract Found')]");

			try {
				new WebDriverWait(driver, Duration.ofSeconds(10))
						.until(ExpectedConditions.or(ExpectedConditions.presenceOfElementLocated(matchingResult),
								ExpectedConditions.visibilityOfElementLocated(noResultsMessage)));

				Reporter.log("Search result state detected.", true);

			} catch (TimeoutException e) {
				throw new AssertionError("Search did not produce a matching record or "
						+ "a no-results message for subject: " + expectedSubject, e);
			}
		}
	}

	// ==================== RECORD LOOKUP ====================

	public boolean isServiceContractListed(String subject) {

		if (subject == null || subject.trim().isEmpty()) {
			throw new IllegalArgumentException("Subject must not be empty.");
		}

		String expectedSubject = subject.trim();

		By subjectRecord = By.xpath("//table//tr/td[normalize-space()=" + xpathLiteral(expectedSubject) + "]"
				+ " | //table//tr/td//a[normalize-space()=" + xpathLiteral(expectedSubject) + "]");

		try {
			return wait.until(ExpectedConditions.visibilityOfElementLocated(subjectRecord)).isDisplayed();

		} catch (TimeoutException e) {
			return false;
		}
	}

	// ==================== DRIVER ACCESS ====================

	public WebDriver getDriver() {
		return driver;
	}

	// ==================== XPATH STRING HELPER ====================

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
