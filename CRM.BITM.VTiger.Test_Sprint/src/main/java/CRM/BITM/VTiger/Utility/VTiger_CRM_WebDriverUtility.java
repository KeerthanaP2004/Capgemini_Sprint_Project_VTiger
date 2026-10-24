package CRM.BITM.VTiger.Utility;

import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class VTiger_CRM_WebDriverUtility {

	WebDriver driver = null;

	public WebDriver launchBrowser(String browser) {

		if (browser == null || browser.trim().isEmpty()) {
			throw new IllegalArgumentException("Browser name must not be empty.");
		}

		if (browser.equalsIgnoreCase("chrome")) {
			driver = new ChromeDriver();
		} else if (browser.equalsIgnoreCase("edge")) {
			driver = new EdgeDriver();
		} else if (browser.equalsIgnoreCase("firefox")) {
			driver = new FirefoxDriver();
		} else {
			throw new IllegalArgumentException("Unsupported browser: " + browser);
		}

		try {
			driver.manage().window().maximize();

			// Keep implicit wait disabled when using explicit waits.
			driver.manage().timeouts().implicitlyWait(Duration.ZERO);

			return driver;

		} catch (RuntimeException e) {
			try {
				driver.quit();
			} catch (Exception ignored) {
				// Preserve the original browser setup exception.
			}

			driver = null;
			throw e;
		}
	}

	public void genericWait(WebDriver driver) {

		if (driver == null) {
			throw new IllegalArgumentException("WebDriver must not be null.");
		}

		driver.manage().timeouts().implicitlyWait(Duration.ZERO);
	}

	public void moveToAnElement(WebDriver driver, WebElement element) {

		if (driver == null || element == null) {
			throw new IllegalArgumentException("WebDriver and WebElement must not be null.");
		}

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

		WebElement visibleElement = wait.until(ExpectedConditions.visibilityOf(element));

		new Actions(driver).moveToElement(visibleElement).perform();
	}
}
