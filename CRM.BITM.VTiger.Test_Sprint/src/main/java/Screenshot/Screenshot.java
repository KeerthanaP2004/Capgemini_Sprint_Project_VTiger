package Screenshot;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

public class Screenshot {

	public static void takeScreenshot(WebDriver driver, String screenshotName) throws IOException {

		if (driver == null) {
			throw new IllegalArgumentException("WebDriver cannot be null.");
		}

		// Capture screenshot
		File source = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

		// Create folder if it does not exist
		File folder = new File("./Screenshot");

		if (!folder.exists()) {
			folder.mkdirs();
		}

		// Destination file
		File destination = new File(folder, screenshotName + ".png");

		// Copy screenshot and overwrite existing file
		Files.copy(source.toPath(), destination.toPath(), StandardCopyOption.REPLACE_EXISTING);

		System.out.println("Screenshot saved: " + destination.getAbsolutePath());
	}
}
