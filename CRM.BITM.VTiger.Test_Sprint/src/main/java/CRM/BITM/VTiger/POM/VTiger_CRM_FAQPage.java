package CRM.BITM.VTiger.POM;

import java.time.Duration;
import java.util.List;
import java.util.Set;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Reporter;

public class VTiger_CRM_FAQPage {

	private final WebDriver driver;
	private final WebDriverWait wait;

	public VTiger_CRM_FAQPage(WebDriver driver, WebDriverWait wait) {
		if (driver == null || wait == null) {
			throw new IllegalArgumentException("WebDriver and WebDriverWait must not be null.");
		}

		this.driver = driver;
		this.wait = wait;
	}

	// --------------------------------------------------
	// FAQ LOCATORS
	// --------------------------------------------------

	private final By createFAQButton = By.xpath("//img[contains(@title,'Create Faq') or contains(@alt,'Create Faq')]");

	private final By productField = By.name("product_name");

	private final By productLookupButton = By.xpath("//img[@title='Select' and contains(@onclick,'module=Products')]");

	private final By questionField = By.name("question");

	private final By answerField = By.name("faq_answer");

	private final By statusDropdown = By.name("faqstatus");

	// Updated Save button locator using the actual HTML attributes.
	private final By saveButton = By.cssSelector("input[name='button'][type='submit'][class*='save']");

	private final By searchField = By.name("query_string");

	private final By searchButton = By.xpath("//input[@value='Search Now' or @title='Search Now']"
			+ " | //button[normalize-space()='Search Now']" + " | //input[@type='image' and contains(@src,'search')]");

	private final By editButton = By.cssSelector("input[name='Edit']");

	private final By deleteButton = By.cssSelector("input[name='Delete']");

	private final By searchInDropdown = By.xpath("//select[option[normalize-space()='Question']]");

	// --------------------------------------------------
	// CREATE FAQ FORM
	// --------------------------------------------------

	public void clickCreateFAQ() {

		Reporter.log("Looking for Create FAQ button...", true);

		List<WebElement> buttons = driver.findElements(By.xpath("//img[contains(@src,'btnL3Add.gif')]"));

		Reporter.log("Create button image count: " + buttons.size(), true);

		for (WebElement button : buttons) {
			Reporter.log("Image HTML: " + button.getAttribute("outerHTML"), true);
		}

		if (buttons.isEmpty()) {
			throw new NoSuchElementException(
					"Create FAQ image not found. Check the current page, frame, or live HTML.");
		}

		WebElement createButton = wait
				.until(ExpectedConditions.elementToBeClickable(By.xpath("//img[contains(@src,'btnL3Add.gif')]")));

		createButton.click();

		wait.until(ExpectedConditions.visibilityOfElementLocated(questionField));

		Reporter.log("Create FAQ form opened.", true);
	}

	public boolean isNewFAQFormDisplayed() {
		try {
			return wait.until(ExpectedConditions.visibilityOfElementLocated(questionField)).isDisplayed()
					&& driver.findElement(answerField).isDisplayed()
					&& driver.findElement(statusDropdown).isDisplayed();

		} catch (TimeoutException | NoSuchElementException e) {
			return false;
		}
	}

	public boolean isFAQListDisplayed() {
		try {
			return wait.until(ExpectedConditions.urlContains("module=Faq"));

		} catch (TimeoutException e) {
			return false;
		}
	}

	// --------------------------------------------------
	// PRODUCT LOOKUP
	// --------------------------------------------------

	public String getProductName() {
		return wait.until(ExpectedConditions.visibilityOfElementLocated(productField)).getAttribute("value");
	}

	public void selectProduct(String productName) {
		requireNonEmpty(productName, "Product name");

		String parentWindow = driver.getWindowHandle();
		Set<String> previousWindows = driver.getWindowHandles();

		wait.until(ExpectedConditions.elementToBeClickable(productLookupButton)).click();

		wait.until(d -> d.getWindowHandles().size() > previousWindows.size());

		String popupWindow = null;

		for (String window : driver.getWindowHandles()) {
			if (!previousWindows.contains(window)) {
				popupWindow = window;
				break;
			}
		}

		if (popupWindow == null) {
			throw new IllegalStateException("Products popup window did not open.");
		}

		try {
			driver.switchTo().window(popupWindow);

			// Diagnostic logging to identify the actual Products popup.
			Reporter.log("Products popup URL: " + driver.getCurrentUrl(), true);

			Reporter.log("Products popup title: " + driver.getTitle(), true);

			Reporter.log("Products popup body: " + driver.findElement(By.tagName("body")).getText(), true);

			WebDriverWait popupWait = new WebDriverWait(driver, Duration.ofSeconds(15));

			// Keep the existing locator until the popup HTML is inspected.
			By productLink = By.xpath("//a[normalize-space()=" + xpathLiteral(productName) + "]");

			popupWait.until(ExpectedConditions.elementToBeClickable(productLink)).click();

			popupWait.until(ExpectedConditions.numberOfWindowsToBe(previousWindows.size()));

		} finally {
			driver.switchTo().window(parentWindow);
		}

		wait.until(d -> productName.equalsIgnoreCase(getProductName().trim()));

		Reporter.log("Selected FAQ product: " + productName, true);
	}

	// --------------------------------------------------
	// QUESTION
	// --------------------------------------------------

	public void enterQuestion(String question) {
		requireNonEmpty(question, "Question");

		WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(questionField));

		element.clear();
		element.sendKeys(question);
	}

	public String getQuestion() {
		return wait.until(ExpectedConditions.visibilityOfElementLocated(questionField)).getAttribute("value");
	}

	// --------------------------------------------------
	// ANSWER
	// --------------------------------------------------

	public void enterAnswer(String answer) {
		requireNonEmpty(answer, "Answer");

		WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(answerField));

		element.clear();
		element.sendKeys(answer);
	}

	public String getAnswer() {
		return wait.until(ExpectedConditions.visibilityOfElementLocated(answerField)).getAttribute("value");
	}

	// --------------------------------------------------
	// STATUS
	// --------------------------------------------------

	public void selectStatus(String status) {
		requireNonEmpty(status, "Status");

		WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(statusDropdown));

		new Select(element).selectByVisibleText(status);
	}

	public String getSelectedStatus() {
		WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(statusDropdown));

		return new Select(element).getFirstSelectedOption().getText().trim();
	}

	// --------------------------------------------------
	// SAVE FAQ
	// --------------------------------------------------

	public void clickSave() {
		wait.until(ExpectedConditions.elementToBeClickable(saveButton)).click();

		Reporter.log("Clicked Save on FAQ form.", true);
	}

	// --------------------------------------------------
	// SEARCH FAQ
	// --------------------------------------------------

	public void searchFAQ(String searchText) {
		requireNonEmpty(searchText, "Search text");

		wait.until(ExpectedConditions.urlContains("module=Faq"));

		WebElement searchInput = wait.until(ExpectedConditions.visibilityOfElementLocated(searchField));

		WebElement searchDropdown = wait.until(ExpectedConditions.visibilityOfElementLocated(searchInDropdown));

		new Select(searchDropdown).selectByVisibleText("Question");

		searchInput.clear();
		searchInput.sendKeys(searchText);

		// Find the visible Search Now button directly on the page.
		By searchNowButton = By
				.xpath("//input[contains(@value,'Search Now') or @alt='Search Now' or @title='Search Now']"
						+ " | //button[normalize-space()='Search Now']"
						+ " | //input[@type='image' and contains(@src,'search')]");

		List<WebElement> buttons = driver.findElements(searchNowButton);
		WebElement buttonToClick = null;

		for (WebElement button : buttons) {
			if (button.isDisplayed() && button.isEnabled()) {
				buttonToClick = button;
				break;
			}
		}

		if (buttonToClick == null) {
			throw new NoSuchElementException(
					"Visible Search Now button not found. Inspect the FAQ search button HTML.");
		}

		buttonToClick.click();

		Reporter.log("FAQ search submitted for: " + searchText, true);
		Reporter.log("Current URL after search: " + driver.getCurrentUrl(), true);
	}

	// --------------------------------------------------
	// FAQ LIST ROW
	// --------------------------------------------------

	private WebElement findFAQRow(String question) {
		requireNonEmpty(question, "Question");

		String normalizedQuestion = question.trim();

		// Match the complete question first.
		By exactRow = By.xpath("//tr[.//td[normalize-space(.)=" + xpathLiteral(normalizedQuestion) + "]]");

		List<WebElement> rows = driver.findElements(exactRow);

		for (WebElement row : rows) {
			if (row.isDisplayed()) {
				return row;
			}
		}

		// If the list truncates a long question, match its distinctive prefix.
		if (normalizedQuestion.length() > 40) {
			String prefix = normalizedQuestion.substring(0, 40);

			By prefixRow = By.xpath("//tr[.//td[contains(normalize-space(.), " + xpathLiteral(prefix) + ")]]");

			rows = driver.findElements(prefixRow);

			for (WebElement row : rows) {
				if (row.isDisplayed()) {
					return row;
				}
			}
		}

		throw new NoSuchElementException("FAQ row not found for question: " + normalizedQuestion);
	}

	public boolean isQuestionVisible(String question) {
		requireNonEmpty(question, "Question");

		String normalizedQuestion = question.trim();

		// First, look for an exact match in a visible table cell.
		By exactMatch = By.xpath("//tr[.//td[normalize-space(.)=" + xpathLiteral(normalizedQuestion) + "]]");

		for (WebElement row : driver.findElements(exactMatch)) {
			if (row.isDisplayed()) {
				return true;
			}
		}

		// For long questions, the CRM may truncate the displayed text.
		// Use a prefix only when it is sufficiently distinctive.
		if (normalizedQuestion.length() > 40) {
			String prefix = normalizedQuestion.substring(0, 40);

			By prefixMatch = By.xpath("//tr[.//td[contains(normalize-space(.), " + xpathLiteral(prefix) + ")]]");

			for (WebElement row : driver.findElements(prefixMatch)) {
				if (row.isDisplayed()) {
					return true;
				}
			}
		}

		return false;
	}

	public void openFAQDetails(String question) {
		requireNonEmpty(question, "Question");

		WebElement row = findFAQRow(question);

		List<WebElement> links = row.findElements(By.xpath(".//a"));

		WebElement questionLink = null;

		for (WebElement link : links) {
			String linkText = link.getText().trim();

			if (question.equals(linkText)) {
				questionLink = link;
				break;
			}
		}

		// The CRM may truncate long questions in the list.
		if (questionLink == null && question.trim().length() > 40) {
			String prefix = question.trim().substring(0, 40);

			for (WebElement link : links) {
				if (link.getText().trim().contains(prefix)) {
					questionLink = link;
					break;
				}
			}
		}

		if (questionLink == null) {
			throw new NoSuchElementException(
					"Question link not found in FAQ row. Question: " + question + "; Row text: " + row.getText());
		}

		wait.until(ExpectedConditions.elementToBeClickable(questionLink)).click();

		wait.until(ExpectedConditions.visibilityOfElementLocated(editButton));

		Reporter.log("Opened FAQ details: " + question, true);
	}

	// --------------------------------------------------
	// EDIT
	// --------------------------------------------------

	public void clickEdit() {
		wait.until(ExpectedConditions.elementToBeClickable(editButton)).click();

		wait.until(ExpectedConditions.visibilityOfElementLocated(questionField));

		Reporter.log("FAQ Edit form opened.", true);
	}

	public void clickEditForQuestion(String question) {
		openFAQDetails(question);
		clickEdit();
	}

	// --------------------------------------------------
	// DELETE
	// --------------------------------------------------

	public void clickDelete() {
		wait.until(ExpectedConditions.elementToBeClickable(deleteButton)).click();

		Alert alert = wait.until(ExpectedConditions.alertIsPresent());

		alert.accept();

		Reporter.log("FAQ deletion confirmation accepted.", true);
	}

	public void clickDeleteForQuestion(String question) {
		openFAQDetails(question);
		clickDelete();
	}

	// --------------------------------------------------
	// VALIDATION FEEDBACK
	// --------------------------------------------------

	public boolean isValidationMessageDisplayed() {
		By validationMessage = By.xpath("//*[contains(translate(normalize-space(.)," + "'ABCDEFGHIJKLMNOPQRSTUVWXYZ',"
				+ "'abcdefghijklmnopqrstuvwxyz'),'required')" + " or contains(translate(normalize-space(.),"
				+ "'ABCDEFGHIJKLMNOPQRSTUVWXYZ'," + "'abcdefghijklmnopqrstuvwxyz'),'mandatory')]");

		return !driver.findElements(validationMessage).isEmpty();
	}

	// --------------------------------------------------
	// COMMON HELPERS
	// --------------------------------------------------

	private void requireNonEmpty(String value, String fieldName) {
		if (value == null || value.trim().isEmpty()) {
			throw new IllegalArgumentException(fieldName + " must not be empty.");
		}
	}

	private String xpathLiteral(String value) {
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

		return result.append(")").toString();
	}
}
