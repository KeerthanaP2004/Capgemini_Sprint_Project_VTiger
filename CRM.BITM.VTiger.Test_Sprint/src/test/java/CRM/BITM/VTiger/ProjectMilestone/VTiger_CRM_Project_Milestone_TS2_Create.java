package CRM.BITM.VTiger.ProjectMilestone;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import org.testng.Reporter;
import org.testng.annotations.Test;

import CRM.BITM.VTiger.Base_Class.VTiger_CRM_BaseClass;

public class VTiger_CRM_Project_Milestone_TS2_Create extends VTiger_CRM_BaseClass {

	public String sheetName = "ProjectMilestone";

	// TC-VT-PM-03
	@Test(groups = { "functional", "integration" })
	public void TC_VT_PM_03_CreateMilestoneWithValidDetails() throws Exception {

		int rowIndex = findTestCaseRow("TC-VT-PM-03");

		createMilestoneFromExcel(rowIndex, "TC-VT-PM-03");
	}

	// TC-VT-PM-11
	@Test(groups = { "smoke", "functional" })
	public void TC_VT_PM_11_SmokeCreateMilestone() throws Exception {

		int rowIndex = findTestCaseRow("TC-VT-PM-11");

		createMilestoneFromExcel(rowIndex, "TC-VT-PM-11");
	}

	// Find the Excel row using Test Case ID
	public int findTestCaseRow(String testCaseId) throws Exception {

		int totalRows = excel.totalNumberOfRows(sheetName);

		for (int row = 1; row < totalRows; row++) {

			String excelTestCaseId = excel.readDataByHeader(sheetName, row, "Test Case ID");

			if (testCaseId.equalsIgnoreCase(excelTestCaseId.trim())) {
				return row;
			}
		}

		throw new IllegalArgumentException("Test Case ID not found in Excel: " + testCaseId);
	}

	// Create milestone using Excel data
	public void createMilestoneFromExcel(int rowIndex, String testCaseId) throws Exception {

		String name = excel.readDataByHeader(sheetName, rowIndex, "Milestone Name");

		String projectName = excel.readDataByHeader(sheetName, rowIndex, "Related Project");

		String assignedUser = excel.readDataByHeader(sheetName, rowIndex, "Assigned User");

		String milestoneDate = excel.readDataByHeader(sheetName, rowIndex, "Milestone Date");

		String type = excel.readDataByHeader(sheetName, rowIndex, "Type");

		String descriptionText = excel.readDataByHeader(sheetName, rowIndex, "Description");

		validateTestData(name, projectName, assignedUser, milestoneDate, type);

		// Convert Excel date into VTiger's yyyy-MM-dd format
		String formattedDate = convertMilestoneDate(milestoneDate);

		Reporter.log("START: " + testCaseId, true);
		Reporter.log("Milestone date entered in yyyy-MM-dd format: " + formattedDate, true);

		homePage.openProjectMilestonesModule(driver);
		milestonePage.openNewMilestoneForm();

		milestonePage.enterMilestoneName(name);
		milestonePage.selectRelatedProject(projectName);
		milestonePage.selectAssignedUser(assignedUser);
		milestonePage.enterMilestoneDate(formattedDate);
		milestonePage.selectMilestoneType(type);

		if (descriptionText != null && !descriptionText.trim().isEmpty()) {
			milestonePage.enterDescription(descriptionText);
		}

		milestonePage.saveMilestone();

		// Verify the saved milestone using the existing POM method
		milestonePage.verifyMilestoneVisible(name);

		Reporter.log("PASS: " + testCaseId, true);
	}

	// Convert supported Excel date formats to yyyy-MM-dd

	public String convertMilestoneDate(String dateValue) {

		if (dateValue == null || dateValue.trim().isEmpty()) {
			throw new IllegalArgumentException("Milestone Date is blank in Excel.");
		}

		String date = dateValue.trim();

		DateTimeFormatter[] acceptedFormats = { DateTimeFormatter.ofPattern("dd-MM-yyyy"),
				DateTimeFormatter.ofPattern("yyyy-MM-dd"), DateTimeFormatter.ofPattern("dd/MM/yyyy"),
				DateTimeFormatter.ofPattern("MM/dd/yy"), DateTimeFormatter.ofPattern("M/d/yy"),
				DateTimeFormatter.ofPattern("MM/dd/yyyy"), DateTimeFormatter.ofPattern("M/d/yyyy") };

		for (DateTimeFormatter format : acceptedFormats) {
			try {
				LocalDate parsedDate = LocalDate.parse(date, format);

				return parsedDate.format(DateTimeFormatter.ISO_LOCAL_DATE);

			} catch (DateTimeParseException e) {
				// Try the next supported format.
			}
		}

		throw new IllegalArgumentException(
				"Invalid Milestone Date in Excel: " + date + ". Supported formats include 30-11-2026 and 11/30/26.");
	}

	// Validate mandatory Excel test data
	public void validateTestData(String name, String projectName, String assignedUser, String milestoneDate,
			String type) {

		if (name == null || name.trim().isEmpty() || projectName == null || projectName.trim().isEmpty()
				|| assignedUser == null || assignedUser.trim().isEmpty() || milestoneDate == null
				|| milestoneDate.trim().isEmpty() || type == null || type.trim().isEmpty()) {

			throw new IllegalArgumentException(
					"Milestone test data contains blank mandatory values. " + "Check the Excel sheet: " + sheetName);
		}
	}
}
