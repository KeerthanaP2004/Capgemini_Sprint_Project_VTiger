package CRM.BITM.VTiger.ServiceContracts;

import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Test;

import CRM.BITM.VTiger.Base_Class.VTiger_CRM_BaseClass;
import Screenshot.Screenshot;

public class VTiger_CRM_ServiceContracts_TS2 extends VTiger_CRM_BaseClass {

	private static final String SHEET_NAME = "ServiceContracts";
	private static final int DATA_ROW_INDEX = 1;

	// TC-VT-SC-02: Search for an existing contract

	@Test(groups = { "functional" }, description = "TC-VT-SC-02 - Search Service Contract")
	public void verifySearchServiceContract() throws Exception {

		Reporter.log("TC-VT-SC-02: Searching for a contract.", true);

		String subject = excel.readDataByHeader(SHEET_NAME, DATA_ROW_INDEX, "Search Subject");

		Assert.assertNotNull(subject, "Search Subject is null.");
		Assert.assertFalse(subject.trim().isEmpty(), "Search Subject is missing from Excel.");

		homePage.openServiceContractsModule();

		serviceContractPage.searchServiceContract(subject);

		Reporter.log("Current URL after search: " + driver.getCurrentUrl(), true);

		Reporter.log("Page title after search: " + driver.getTitle(), true);

		boolean found = serviceContractPage.isServiceContractListed(subject);

		Reporter.log("Contract subject [" + subject.trim() + "] found in results: " + found, true);

		Assert.assertTrue(found, "Expected contract was not found.");
	}

	// TC-VT-SC-07: Search for a nonmatching contract
	@Test(groups = { "negative" }, description = "TC-VT-SC-07 - Nonmatching contract search")
	public void verifyNonMatchingServiceContractSearch() throws Exception {

		Reporter.log("TC-VT-SC-07: Searching for a nonmatching contract.", true);

		String subject = excel.readDataByHeader(SHEET_NAME, DATA_ROW_INDEX, "Nonmatching Subject");

		Assert.assertNotNull(subject, "Nonmatching Subject is null.");

		Assert.assertFalse(subject.trim().isEmpty(), "Nonmatching Subject is missing from Excel.");

		homePage.openServiceContractsModule();
		serviceContractPage.searchServiceContract(subject);

		Assert.assertFalse(serviceContractPage.isServiceContractListed(subject),
				"A matching contract was found for the negative test.");

		Screenshot.takeScreenshot(driver, "TC-VT-SC-07_NonmatchingContractSearch");

		Reporter.log("PASS: TC-VT-SC-07 - Nonmatching contract search.", true);
	}
}
