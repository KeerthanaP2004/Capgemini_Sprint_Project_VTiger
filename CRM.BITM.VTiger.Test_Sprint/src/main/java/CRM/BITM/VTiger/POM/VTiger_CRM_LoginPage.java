package CRM.BITM.VTiger.POM;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class VTiger_CRM_LoginPage {

	private final WebDriver driver;
	private final WebDriverWait wait;

	public VTiger_CRM_LoginPage(WebDriver driver) {

		if (driver == null) {
			throw new IllegalArgumentException("WebDriver must not be null.");
		}

		this.driver = driver;
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));

		PageFactory.initElements(driver, this);
	}

	// ==================== LOGIN PAGE ELEMENTS ====================

	@FindBy(name = "user_name")
	private WebElement userName;

	@FindBy(name = "user_password")
	private WebElement password;

	@FindBy(id = "submitButton")
	private WebElement loginButton;

	// ==================== GETTERS ====================

	public WebElement getUserName() {
		return userName;
	}

	public WebElement getPassword() {
		return password;
	}

	public WebElement getLoginButton() {
		return loginButton;
	}

	// ==================== LOGIN PAGE VALIDATION ====================

	public boolean isLoginPageDisplayed() {

		try {
			return wait.until(ExpectedConditions.visibilityOf(userName)).isDisplayed() && password.isDisplayed()
					&& loginButton.isDisplayed();

		} catch (NoSuchElementException | StaleElementReferenceException | TimeoutException e) {

			return false;
		}
	}

	// ==================== LOGIN ACTION ====================

	public void login(String username, String passwordValue) {

		if (username == null || username.trim().isEmpty()) {
			throw new IllegalArgumentException("Username must not be null or empty.");
		}

		if (passwordValue == null || passwordValue.isEmpty()) {
			throw new IllegalArgumentException("Password must not be null or empty.");
		}

		WebElement usernameField = wait.until(ExpectedConditions.elementToBeClickable(userName));

		usernameField.clear();
		usernameField.sendKeys(username);

		WebElement passwordField = wait.until(ExpectedConditions.elementToBeClickable(password));

		passwordField.clear();
		passwordField.sendKeys(passwordValue);

		WebElement submitButton = wait.until(ExpectedConditions.elementToBeClickable(loginButton));

		System.out.println("Login button is clickable. Attempting click.");

		submitButton.click();

		System.out.println("Login button click completed.");

		try {
			wait.until(ExpectedConditions.or(ExpectedConditions.invisibilityOfElementLocated(By.name("user_name")),
					ExpectedConditions.urlContains("index.php")));
		} catch (TimeoutException e) {
			throw new TimeoutException("Login did not complete within 15 seconds. "
					+ "Check the credentials, login error message, and application response.", e);
		}
	}

	// ==================== LOGIN RESULT VALIDATION ====================

	public boolean isLoginSuccessful() {

		try {
			wait.until(ExpectedConditions.invisibilityOfElementLocated(By.name("user_name")));

			return true;

		} catch (TimeoutException e) {
			return false;
		}
	}
}