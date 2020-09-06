package com.nach.core.util.excel;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.Test;

import com.nach.core.util.file.FileUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ExcelUtilReadExcelIntegrationTest {

	private static final String DIR = "/com/nach/core/testfiles/excel";

	private static final String SRC_FILE_NAME = DIR + "/medium-file-for-excel-test.xlsx";

	private static final String TARGET_CSV = DIR + "output/csv/medium-file-for-excel-test.csv";

	@Test
	public void shouldParseFile() {
		log.info("Starting test...");
		File file = FileUtil.getFile(SRC_FILE_NAME);
		log.info("Using file: " + FileUtil.getCanonicalPath(file));
		assertTrue(file.exists());
		parseFile(file);
		log.info("Done.");
	}

	private void parseFile(File file) {
		Workbook book = ExcelUtil.getWorkbook(file);
		Sheet sheet = book.getSheetAt(0);
		Iterator<Row> rows = ExcelUtil.getRows(sheet);
		while(rows.hasNext()) {
			Row row = rows.next();
			Iterator<Cell> cells = row.iterator();
			String rowAsString = "";
			while(cells.hasNext()) {
				Cell cell = cells.next();
				String str = ExcelUtil.getStringValue(cell);
				rowAsString += str + "\t";
			}
			log.info(rowAsString);
		}
	}
	
}
