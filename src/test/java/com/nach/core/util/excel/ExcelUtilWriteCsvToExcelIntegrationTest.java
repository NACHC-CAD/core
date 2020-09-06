package com.nach.core.util.excel;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.Test;

import com.nach.core.util.file.FileUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ExcelUtilWriteCsvToExcelIntegrationTest {

	private static final String DIR = "/com/nach/core/testfiles/excel";

	private static final String SRC_FILE_NAME = DIR + "/medium-file-for-excel-test.csv";

	private static final String EXCEL_FILE_NAME = DIR + "/output/medium-file-for-excel-text.xlsx";

	@Test
	public void shouldWriteExcelFile() {
		log.info("Starting test...");
		File file = writeCsvToExcel();
		log.info("Wrote file to: " + FileUtil.getCanonicalPath(file));
		log.info("Done.");
	}

	private File writeCsvToExcel() {
		try {
			log.info("Creating workbook");
			Workbook book = ExcelUtil.createNewWorkbook();
			Sheet sheet = book.createSheet("csv");
			log.info("Getting source file");
			File csvFile = FileUtil.getFile(SRC_FILE_NAME);
			InputStream in = new FileInputStream(csvFile);
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			log.info("Reading file...");
			int cnt = 0;
			String str = reader.readLine();
			while (str != null) {
				cnt++;
				log.info(cnt + " \t" + str);
				Row row = ExcelUtil.addRow(sheet);
				StringTokenizer tokenizer = new StringTokenizer(str, ",");
				while (tokenizer.hasMoreTokens()) {
					String val = tokenizer.nextToken();
					ExcelUtil.addCol(row, val);
				}
				str = reader.readLine();
			}
			File excelFile = FileUtil.getFile(EXCEL_FILE_NAME);
			ExcelUtil.save(book, excelFile);
			return excelFile;
		} catch (Exception exp) {
			throw new RuntimeException(exp);
		}
	}

}
