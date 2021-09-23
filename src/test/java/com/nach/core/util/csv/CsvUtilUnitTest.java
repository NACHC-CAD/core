package com.nach.core.util.csv;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import com.nach.core.util.file.FileUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CsvUtilUnitTest {
	private static final String FILE_NAME = "/csv/excel_csv.csv";
	private static final String recordId = "137a5f7e-eb95-4754-8ef7-9be641430660";
	private static final String phoneNumber = "555-406-3922";

	@Test
	public void csvShouldParseLine() {

		//Arrange
		String csv = FileUtil.getAsString(FILE_NAME);
		String[] csvSplit = csv.split("\n");

		//Act
		List<String> line1 = CsvUtil.parseLine(csvSplit[1], ',', '"');
		List<String> line3 = CsvUtil.parseLine(csvSplit[3], ',', '"');

		//Assert
		assertEquals(line1.get(0), recordId);
		assertEquals(line3.get(5), phoneNumber);
	}

	@Test
	public void csvShouldParseLineDefaults() {

		//Arrange
		String csv = FileUtil.getAsString(FILE_NAME);
		String[] csvSplit = csv.split("\n");

		//Act
		List<String> line1 = CsvUtil.parseLine(csvSplit[1]);
		List<String> line3 = CsvUtil.parseLine(csvSplit[3]);

		//Assert
		assertEquals(line1.get(0), recordId);
		assertEquals(line3.get(5), phoneNumber);
	}
}
