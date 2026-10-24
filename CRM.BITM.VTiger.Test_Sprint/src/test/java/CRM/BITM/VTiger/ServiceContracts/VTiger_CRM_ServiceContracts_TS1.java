package CRM.BITM.VTiger.ServiceContracts;

import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Test;

import CRM.BITM.VTiger.Base_Class.VTiger_CRM_BaseClass;

public class VTiger_CRM_ServiceContracts_TS1 extends VTiger_CRM_BaseClass {

	// TC-VT-SC-01: Open New Service Contract form
	@Test(groups = { "functional" }, description = "TC-VT-SC-01 - Open New Service Contract form")
	public void verifyOpenNewServiceContractForm() {

		Reporter.log("TC-VT-SC-01: Opening Service Contracts.", true);

		homePage.openServiceContractsModule();

		Assert.assertTrue(serviceContractPage.isServiceContractsListDisplayed(),
				"Service Contracts list page was not displayed.");

		serviceContractPage.clickCreateServiceContract();

		Assert.assertTrue(serviceContractPage.isNewServiceContractFormDisplayed(),
				"New Service Contract form was not displayed.");
	}

	// TC-VT-SC-05: Verify Service Contracts navigation
	@Test(groups = { "EP" }, description = "TC-VT-SC-05 - Verify Service Contracts navigation")
	public void verifyServiceContractsNavigation() {

		Reporter.log("TC-VT-SC-05: Verifying module navigation.", true);

		homePage.openServiceContractsModule();

		Assert.assertTrue(serviceContractPage.isServiceContractsListDisplayed(),
				"Service Contracts module was not accessible.");
	}

	// TC-VT-SC-08: Verify list page navigation
	@Test(groups = { "integration" }, description = "TC-VT-SC-08 - Verify Service Contracts list")
	public void verifyServiceContractsListNavigation() {

		homePage.openServiceContractsModule();

		Assert.assertTrue(serviceContractPage.isServiceContractsListDisplayed(),
				"Service Contracts list page was not reached.");
	}

	// TC-VT-SC-09: Verify + button navigation
	@Test(groups = { "integration" }, description = "TC-VT-SC-09 - Verify create button navigation")
	public void verifyCreateButtonOpensForm() {

		homePage.openServiceContractsModule();
		serviceContractPage.clickCreateServiceContract();

		Assert.assertTrue(serviceContractPage.isNewServiceContractFormDisplayed(),
				"Create button did not open the new-record form.");
	}
}