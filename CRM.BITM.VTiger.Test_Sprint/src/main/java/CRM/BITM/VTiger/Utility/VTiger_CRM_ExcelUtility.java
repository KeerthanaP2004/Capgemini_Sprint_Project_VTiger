package CRM.BITM.VTiger.Utility;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class VTiger_CRM_ExcelUtility {

	private static final String EXCEL_FILE_PATH = "./src/test/resources/CRM.BITM.VTiger.DDT/VTiger_CRM_TestData.xlsx";

	private final DataFormatter formatter = new DataFormatter();

	public FileInputStream fis;

	public int totalNumberOfRows(String sheetName) throws Exception {

		try (FileInputStream input = new FileInputStream(EXCEL_FILE_PATH);
				Workbook workbook = WorkbookFactory.create(input)) {

			Sheet sheet = workbook.getSheet(sheetName);

			if (sheet == null) {
				throw new IllegalArgumentException("Excel sheet not found: " + sheetName);
			}

			return sheet.getPhysicalNumberOfRows();
		}
	}

	public int totalNumberOfColumns(String sheetName) throws Exception {

		try (FileInputStream input = new FileInputStream(EXCEL_FILE_PATH);
				Workbook workbook = WorkbookFactory.create(input)) {

			Sheet sheet = workbook.getSheet(sheetName);

			if (sheet == null || sheet.getRow(0) == null) {
				throw new IllegalArgumentException("Excel sheet or header row not found: " + sheetName);
			}

			return sheet.getRow(0).getPhysicalNumberOfCells();
		}
	}

	public String readDataFromExcel(String sheetName, int row, int cell) throws Exception {

		try (FileInputStream input = new FileInputStream(EXCEL_FILE_PATH);
				Workbook workbook = WorkbookFactory.create(input)) {

			Sheet sheet = workbook.getSheet(sheetName);

			if (sheet == null) {
				throw new IllegalArgumentException("Excel sheet not found: " + sheetName);
			}

			Row dataRow = sheet.getRow(row);

			if (dataRow == null || dataRow.getCell(cell) == null) {
				return "";
			}

			return formatter.formatCellValue(dataRow.getCell(cell)).trim();
		}
	}

	public String readDataByHeader(String sheetName, int dataRowIndex, String columnHeader) throws Exception {

		try (FileInputStream input = new FileInputStream(EXCEL_FILE_PATH);
				Workbook workbook = WorkbookFactory.create(input)) {

			Sheet sheet = workbook.getSheet(sheetName);

			if (sheet == null) {
				throw new IllegalArgumentException("Excel sheet not found: " + sheetName);
			}

			Row headerRow = sheet.getRow(0);

			if (headerRow == null) {
				throw new IllegalArgumentException("Header row is missing in sheet: " + sheetName);
			}

			int columnIndex = -1;

			for (int i = 0; i < headerRow.getLastCellNum(); i++) {

				if (headerRow.getCell(i) != null && formatter.formatCellValue(headerRow.getCell(i)).trim()
						.equalsIgnoreCase(columnHeader.trim())) {

					columnIndex = i;
					break;
				}
			}

			if (columnIndex == -1) {
				throw new IllegalArgumentException("Column '" + columnHeader + "' not found in sheet: " + sheetName);
			}

			Row dataRow = sheet.getRow(dataRowIndex);

			if (dataRow == null || dataRow.getCell(columnIndex) == null) {
				return "";
			}

			return formatter.formatCellValue(dataRow.getCell(columnIndex)).trim();
		}
	}

	public void setCellDataInExcel(String sheetName, int row, int cell, String value) throws Exception {

		updateExcelCell(sheetName, row, cell, value, false);
	}

	public void updateCellDataInExcel(String sheetName, int row, int cell, String value) throws Exception {

		updateExcelCell(sheetName, row, cell, value, true);
	}

	private void updateExcelCell(String sheetName, int rowIndex, int cellIndex, String value,
			boolean requireExistingCell) throws Exception {

		/*
		 * Close the input stream before opening the output stream. This avoids keeping
		 * the workbook file open while writing.
		 */
		try (FileInputStream input = new FileInputStream(EXCEL_FILE_PATH);
				Workbook workbook = WorkbookFactory.create(input)) {

			Sheet sheet = workbook.getSheet(sheetName);

			if (sheet == null) {
				throw new IllegalArgumentException("Excel sheet not found: " + sheetName);
			}

			Row dataRow = sheet.getRow(rowIndex);

			if (dataRow == null) {
				if (requireExistingCell) {
					throw new IllegalArgumentException("Row does not exist: " + rowIndex);
				}

				dataRow = sheet.createRow(rowIndex);
			}

			if (requireExistingCell && dataRow.getCell(cellIndex) == null) {
				throw new IllegalArgumentException("Cell does not exist at row " + rowIndex + ", column " + cellIndex);
			}

			if (dataRow.getCell(cellIndex) == null) {
				dataRow.createCell(cellIndex);
			}

			dataRow.getCell(cellIndex).setCellValue(value);

			/*
			 * The input stream has to be closed before writing to the same workbook file on
			 * Windows.
			 */
			try (FileOutputStream output = new FileOutputStream(EXCEL_FILE_PATH)) {
				workbook.write(output);
			}
		}
	}
}
