package CRM.BITM.VTiger.POM;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Reporter;

import CRM.BITM.VTiger.Utility.VTiger_CRM_WebDriverUtility;

public class VTiger_CRM_HomePage {

	private final WebDriver driver;
	private final VTiger_CRM_WebDriverUtility wi;

	public VTiger_CRM_HomePage(WebDriver driver) {

		if (driver == null) {
			throw new IllegalArgumentException("WebDriver must not be null.");
		}

		this.driver = driver;
		this.wi = new VTiger_CRM_WebDriverUtility();

		PageFactory.initElements(driver, this);
	}

	// ==================== HOME PAGE ELEMENTS ====================

	@FindBy(linkText = "Calendar")
	private WebElement calendar;

	@FindBy(linkText = "Organizations")
	private WebElement org;

	@FindBy(linkText = "Contacts")
	private WebElement contact;

	@FindBy(xpath = "//img[contains(@src,'user.PNG')]")
	private WebElement userIcon;

	@FindBy(linkText = "Sign Out")
	private WebElement logout;

	// ==================== NAVIGATION LOCATORS ====================

	public final By moreMenu = By.xpath("//a[normalize-space()='More']");

	public final By projectMilestonesMenu = By
			.xpath("//a[contains(@href,'module=ProjectMilestone')" + " and normalize-space()='Project Milestones']");

	public final By projectTasksMenu = By.xpath("//a[normalize-space()='Project Tasks']");

	public final By projectsMenu = By.xpath("//a[normalize-space()='Projects']");

	public final By serviceContractsMenu = By
			.xpath("//a[@name='Service Contracts'" + " and contains(@href,'module=ServiceContracts')]");

	// ==================== GETTERS ====================

	public WebElement getCalendar() {
		return calendar;
	}

	public WebElement getOrg() {
		return org;
	}

	public WebElement getContact() {
		return contact;
	}

	public WebElement getUserIcon() {
		return userIcon;
	}

	public WebElement getLogout() {
		return logout;
	}

	// ==================== EXPLICIT WAIT HELPER ====================

	private WebDriverWait getWait() {
		return new WebDriverWait(driver, Duration.ofSeconds(15));
	}

	// ==================== PROJECT MILESTONES ====================

	public void openProjectMilestonesModule(WebDriver suppliedDriver) {

		validateDriver(suppliedDriver);

		Reporter.log("Opening Project Milestones module.", true);

		WebDriverWait localWait = getWait();

		WebElement more = localWait.until(ExpectedConditions.elementToBeClickable(moreMenu));

		wi.moveToAnElement(driver, more);

		WebElement milestonesLink = localWait.until(ExpectedConditions.elementToBeClickable(projectMilestonesMenu));

		milestonesLink.click();

		localWait.until(ExpectedConditions.urlContains("module=ProjectMilestone"));

		Reporter.log("Project Milestones module opened successfully.", true);
	}

	// ==================== PROJECT TASKS ====================

	public void openProjectTasksModule() {

		Reporter.log("Opening Project Tasks module.", true);

		WebDriverWait localWait = getWait();

		WebElement more = localWait.until(ExpectedConditions.elementToBeClickable(moreMenu));

		new Actions(driver).moveToElement(more).perform();

		WebElement projectTasksLink = localWait.until(ExpectedConditions.elementToBeClickable(projectTasksMenu));

		projectTasksLink.click();

		localWait.until(ExpectedConditions.urlContains("module=ProjectTask"));

		Reporter.log("Project Tasks module opened successfully.", true);
	}

	// ==================== PROJECTS ====================

	public void openProjectsModule() {

		Reporter.log("Opening Projects module.", true);

		WebDriverWait localWait = getWait();

		WebElement more = localWait.until(ExpectedConditions.elementToBeClickable(moreMenu));

		wi.moveToAnElement(driver, more);

		WebElement projectsLink = localWait.until(ExpectedConditions.elementToBeClickable(projectsMenu));

		projectsLink.click();

		localWait.until(ExpectedConditions.urlContains("module=Project"));

		Reporter.log("Projects module opened successfully.", true);
	}

	// ==================== SERVICE CONTRACTS ====================

	public void openServiceContractsModule() {

		Reporter.log("Opening Service Contracts module from More menu.", true);

		WebDriverWait localWait = getWait();

		// Open the More menu.
		WebElement more = localWait.until(ExpectedConditions.elementToBeClickable(moreMenu));

		wi.moveToAnElement(driver, more);

		// Wait for the Service Contracts link to become clickable.
		WebElement serviceContractsLink = localWait
				.until(ExpectedConditions.elementToBeClickable(serviceContractsMenu));

		// Open Service Contracts.
		serviceContractsLink.click();

		// Confirm navigation to the Service Contracts module.
		localWait.until(ExpectedConditions.urlContains("module=ServiceContracts"));

		Reporter.log("Service Contracts module opened successfully. URL: " + driver.getCurrentUrl(), true);
	}

	// ==================== FAQ ====================

	public void openFAQModule() {

		Reporter.log("Opening FAQ module from More menu.", true);

		WebDriverWait localWait = getWait();

		// Open the More menu.
		WebElement more = localWait.until(ExpectedConditions.elementToBeClickable(moreMenu));

		new Actions(driver).moveToElement(more).perform();

		// Hover over Support to reveal the submenu.
		By supportMenu = By.xpath("//*[self::a or self::span]" + "[normalize-space()='Support']");

		WebElement support = localWait.until(ExpectedConditions.visibilityOfElementLocated(supportMenu));

		new Actions(driver).moveToElement(support).perform();

		// Click the FAQ link.
		By faqLink = By.xpath("//a[normalize-space()='FAQ'" + " or normalize-space()='FAQs']");

		localWait.until(ExpectedConditions.elementToBeClickable(faqLink)).click();

		// Verify that the FAQ page's search controls are available.
		localWait.until(ExpectedConditions.visibilityOfElementLocated(By.name("query_string")));

		localWait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("input.searchBtn[alt='Find']")));

		Reporter.log("FAQ module opened successfully. URL: " + driver.getCurrentUrl(), true);
	}

	// ==================== LOGOUT ====================

	public void userLogout(WebDriver suppliedDriver) {

		validateDriver(suppliedDriver);

		Reporter.log("Starting logout process.", true);

		WebDriverWait localWait = getWait();

		WebElement icon = localWait
				.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//img[contains(@src,'user.PNG')]")));

		wi.moveToAnElement(driver, icon);

		Reporter.log("Hovered over the user icon.", true);

		WebElement signOut = localWait.until(ExpectedConditions.elementToBeClickable(By.linkText("Sign Out")));

		signOut.click();

		Reporter.log("Sign Out clicked. Waiting for login page.", true);

		localWait.until(ExpectedConditions.visibilityOfElementLocated(By.name("user_name")));

		Reporter.log("Logout successful. Login page is displayed.", true);
	}

	// ==================== DRIVER VALIDATION ====================

	private void validateDriver(WebDriver suppliedDriver) {

		if (suppliedDriver == null) {
			throw new IllegalArgumentException("WebDriver must not be null.");
		}

		if (suppliedDriver != driver) {
			throw new IllegalArgumentException("The supplied WebDriver does not match " + "the HomePage WebDriver.");
		}
	}
}