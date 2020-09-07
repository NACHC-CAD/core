package com.nach.core.util.excel;

import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.Test;

import com.monitorjbl.xlsx.StreamingReader;
import com.nach.core.util.file.FileUtil;
import com.nach.core.util.file.ZipUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ExcelUtilLargeFileIntegrationTestWeekly {

	private static final String DIR = "/com/nach/core/testfiles/excel";

	private static final String SRC_FILE_NAME = DIR + "/large-file-for-excel-test.zip";

	private static final String UNZIPPED_DIR_NAME = DIR + "/unzipped";

	private static final String TARGET_CSV = DIR + "/unzipped/large-file-for-excel-test.csv";

	private static final String EXCEL_FILE_NAME = DIR + "/output/large-file-for-exel-text.xlsx";

	@Test
	public void shouldParseToCsvFile() {
		log.info("Starting test..");
		File csvFile = getCsvFile();
		File excelFile = writeCsvToExcel(csvFile);
		log.info("Done with csv to excel, doing excel to csv...");
		readExcelFile(excelFile);
		log.info("Done with excel to csv.");
		log.info("Done.");
	}

	private File writeCsvToExcel(File csvFile) {
		try {
			log.info("Creating workbook");
			Workbook book = ExcelUtil.createNewWorkbook();
			Sheet sheet = book.createSheet("csv");
			log.info("Getting source file");
			InputStream in = new FileInputStream(csvFile);
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			log.info("* * * THIS IS THE CSV VERSION (IT IS MUCH FASTER THAN THE EXCEL VERSION * * *");
			log.info("Reading file...");
			int cnt = 0;
			String str = reader.readLine();
			while (str != null) {
				if (cnt <= 10 || cnt % 1000 == 0) {
					log.info(cnt + " \t" + str);
				}
				cnt++;
				Row row = ExcelUtil.addRow(sheet);
				StringTokenizer tokenizer = new StringTokenizer(str, ",");
				while (tokenizer.hasMoreTokens()) {
					String val = tokenizer.nextToken();
					ExcelUtil.addCol(row, val);
				}
				str = reader.readLine();
			}
			log.info(cnt + "\t" + str);
			File excelFile = FileUtil.getFile(EXCEL_FILE_NAME);
			ExcelUtil.save(book, excelFile);
			log.info("Done with save");
			return excelFile;
		} catch (Exception exp) {
			throw new RuntimeException(exp);
		}
	}

	private File getCsvFile() {
		log.info("Unzipping file");
		File zipFile = FileUtil.getFile(SRC_FILE_NAME);
		File dstDir = FileUtil.getFile(UNZIPPED_DIR_NAME);
		log.info("Writing zip file to: " + FileUtil.getCanonicalPath(dstDir));
		ZipUtil.unzip(zipFile, dstDir);
		File targetFile = FileUtil.getFile(TARGET_CSV);
		log.info("Looking for file at: " + FileUtil.getCanonicalPath(targetFile));
		assertTrue(targetFile.exists());
		return targetFile;
	}

	private void readExcelFileXXX(File file) {
		Workbook book = ExcelUtil.getWorkbook(file);
		Sheet sheet = book.getSheetAt(0);
		int cnt = 0;
		for (int r = 0; r < sheet.getLastRowNum(); r++) {
			Row row = sheet.getRow(r);
			String rowAsString = "";
			for (int c = 0; c < row.getLastCellNum(); c++) {
				if (rowAsString != "") {
					rowAsString += ",";
				}
				rowAsString += ExcelUtil.getStringValue(row, c);
			}
			if (cnt < 100) {
				log.info(cnt + " \t" + rowAsString);
			} else if (cnt <= 1000 && cnt % 100 == 0) {
				log.info(cnt + " \t" + rowAsString);
			} else if (cnt <= 10000 && cnt % 1000 == 0) {
				log.info(cnt + " \t" + rowAsString);
			} else if (cnt % 10000 == 0) {
				log.info(cnt + " \t" + rowAsString);
			}
			cnt++;
		}
	}

	private void readExcelFile(File file) {
		log.info("Parsing excel");
		log.info("Reading file: " + FileUtil.getCanonicalPath(file));
		StreamingReader reader = ExcelUtil.getReader(file);
		log.info("Getting rows");
		int cnt = 0;
		String rowAsString = "";
		log.info("* * * THIS IS THE EXCEL READ (IT IS SLOWER THAN THE CSV READ) * * *");
		log.info("Processing rows...");
		for (Row row : reader) {
			rowAsString = "";
			for (Cell cell : row) {
				String str = ExcelUtil.getStringValue(cell);
				rowAsString += str + "\t";
			}
			if (cnt <= 1000 && cnt % 100 == 0) {
				log.info(cnt + " \t" + rowAsString);
			} else if (cnt % 1000 == 0) {
				log.info(cnt + " \t" + rowAsString);
			}
			cnt++;
		}
		log.info(cnt + " \t" + rowAsString);
	}

}
