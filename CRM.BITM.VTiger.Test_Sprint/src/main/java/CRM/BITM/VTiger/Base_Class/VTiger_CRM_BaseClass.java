package CRM.BITM.VTiger.Base_Class;

import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Reporter;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import CRM.BITM.VTiger.POM.VTiger_CRM_LoginPage;
import CRM.BITM.VTiger.POM.VTiger_CRM_HomePage;
import CRM.BITM.VTiger.POM.VTiger_CRM_Project_MilestonePage;
import CRM.BITM.VTiger.POM.VTiger_CRM_ProjectPage;
import CRM.BITM.VTiger.POM.VTiger_CRM_ProjectTaskPage;
import CRM.BITM.VTiger.POM.VTiger_CRM_FAQPage;
import CRM.BITM.VTiger.POM.VTiger_CRM_ServiceContractPage;
import CRM.BITM.VTiger.Utility.VTiger_CRM_WebDriverUtility;
import CRM.BITM.VTiger.Utility.VTiger_CRM_FileUtility;
import CRM.BITM.VTiger.Utility.VTiger_CRM_ExcelUtility;

public class VTiger_CRM_BaseClass {

	public WebDriver driver = null;

	public boolean skipAutomaticLoginLogout = false;

	public VTiger_CRM_WebDriverUtility wi = new VTiger_CRM_WebDriverUtility();

	public VTiger_CRM_FileUtility fi = new VTiger_CRM_FileUtility();

	public VTiger_CRM_ExcelUtility excel = new VTiger_CRM_ExcelUtility();

	// Shared Home Page POM object
	public VTiger_CRM_HomePage homePage;

	// Shared WebDriverWait object
	public WebDriverWait wait;

	// Shared Project Milestone POM object
	public VTiger_CRM_Project_MilestonePage milestonePage;

	// Shared Projects POM object
	public VTiger_CRM_ProjectPage projectPage;

	// Shared Project Tasks POM object
	public VTiger_CRM_ProjectTaskPage projectTaskPage;

	// Shared FAQ POM object
	public VTiger_CRM_FAQPage faqPage;

	// Shared Service Contract POM object
	public VTiger_CRM_ServiceContractPage serviceContractPage;

	@BeforeSuite(alwaysRun = true)
	public void dbConnection() {
		Reporter.log("Before Suite", true);
		// Database connection is not required for this test project.
	}

	@BeforeTest(alwaysRun = true)
	public void bt() {
		Reporter.log("Before Test", true);
	}

	@Parameters("browser")
	@BeforeClass(alwaysRun = true)
	public void navigateTOApp(@Optional("chrome") String browser) throws Exception {

		Reporter.log("Before Class: Launching browser", true);

		driver = wi.launchBrowser(browser);

		if (driver == null) {
			throw new IllegalStateException("Browser launch failed. WebDriver is null.");
		}

		driver.get(fi.getDataFromPropertyFile("url"));

		Reporter.log("Application URL opened: " + driver.getCurrentUrl(), true);

		if (driver.getWindowHandles().isEmpty()) {
			throw new IllegalStateException("No browser window is available after opening the URL.");
		}

		Reporter.log("Browser window verified. Window count: " + driver.getWindowHandles().size(), true);

		// Initialize shared Page Object Model objects.
		homePage = new VTiger_CRM_HomePage(driver);

		wait = new WebDriverWait(driver, Duration.ofSeconds(15));

		milestonePage = new VTiger_CRM_Project_MilestonePage(driver, wait, wi);

		projectPage = new VTiger_CRM_ProjectPage(driver, wait);

		projectTaskPage = new VTiger_CRM_ProjectTaskPage(driver, wait);

		faqPage = new VTiger_CRM_FAQPage(driver, wait);

		// Initialize Service Contract POM.
		serviceContractPage = new VTiger_CRM_ServiceContractPage(driver, wait);

		Reporter.log("All shared Page Object Model objects initialized.", true);
	}

	@BeforeMethod(alwaysRun = true)
	public void login() throws Exception {

		if (skipAutomaticLoginLogout) {
			Reporter.log("Automatic login skipped for login-page tests.", true);
			return;
		}

		if (driver == null) {
			throw new IllegalStateException("WebDriver is null before login. Check @BeforeClass execution.");
		}

		try {
			driver.getCurrentUrl();
		} catch (org.openqa.selenium.WebDriverException e) {
			throw new IllegalStateException("Browser session is unavailable before login.", e);
		}

		Reporter.log("Before Method: Logging in", true);

		VTiger_CRM_LoginPage loginPage = new VTiger_CRM_LoginPage(driver);

		String username = fi.getDataFromPropertyFile("username");
		String password = fi.getDataFromPropertyFile("password");

		loginPage.login(username, password);

		Reporter.log("Login method completed.", true);
	}

	@AfterMethod(alwaysRun = true)
	public void logout() {

		if (skipAutomaticLoginLogout) {
			Reporter.log("Automatic logout skipped for login-page tests.", true);
			return;
		}

		Reporter.log("After Method: Logging out", true);

		if (driver == null) {
			Reporter.log("Driver is null; logout skipped.", true);
			return;
		}

		try {
			driver.getCurrentUrl();

			VTiger_CRM_LoginPage loginPage = new VTiger_CRM_LoginPage(driver);

			if (loginPage.isLoginPageDisplayed()) {
				Reporter.log("Already on Login page. Duplicate logout skipped.", true);
				return;
			}

			Reporter.log("Starting logout process.", true);

			VTiger_CRM_HomePage currentHomePage = new VTiger_CRM_HomePage(driver);

			currentHomePage.userLogout(driver);

			Reporter.log("Logout action completed.", true);

		} catch (org.openqa.selenium.NoSuchSessionException e) {

			Reporter.log("Browser session already closed; logout skipped.", true);

		} catch (org.openqa.selenium.WebDriverException e) {

			Reporter.log("WebDriver cleanup issue: " + e.getMessage(), true);
		}
	}

	@AfterClass(alwaysRun = true)
	public void closeBrowser() {

		Reporter.log("After Class: Closing browser", true);

		if (driver != null) {
			try {
				driver.quit();

				Reporter.log("Browser closed", true);

			} catch (Exception e) {

				Reporter.log("Browser cleanup issue: " + e.getMessage(), true);

			} finally {

				driver = null;
				homePage = null;
				milestonePage = null;
				projectPage = null;
				projectTaskPage = null;
				faqPage = null;
				serviceContractPage = null;
				wait = null;
			}
		}
	}

	@AfterTest(alwaysRun = true)
	public void at() {
		Reporter.log("After Test", true);
	}

	@AfterSuite(alwaysRun = true)
	public void closeConnection() {
		Reporter.log("After Suite", true);
		// No database connection was opened.
	}
}