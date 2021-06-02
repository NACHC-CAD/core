package com.nach.core.util.csv;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.apache.commons.math3.geometry.euclidean.twod.Line;
import org.junit.Test;

import com.nach.core.util.file.FileUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CsvUtilUnitTest {
	private static String FILE_NAME = "/csv/excel_csv.csv";
			
	@Test
	public void shouldParseLine() {
		log.info("Starting parseLine(String, char, char) Test...");
		String csv = FileUtil.getAsString(FILE_NAME);
		String[] csvSplit = csv.split("\n");
		log.info("Reading CSV with header: " + csvSplit[0]);
		List<String> line1 = CsvUtil.parseLine(csvSplit[1], ',', '"');
		assertTrue(line1.get(0).equals("137a5f7e-eb95-4754-8ef7-9be641430660"));
		List<String> line3 = CsvUtil.parseLine(csvSplit[3], ',', '"');
		assertTrue(line3.get(5).equals("555-406-3922"));
		log.info("Done.");
	}
	
	@Test
	public void shouldParseLineDefaults() {
		log.info("Starting parseLine(String) Test...");
		String csv = FileUtil.getAsString(FILE_NAME);
		String[] csvSplit = csv.split("\n");
		log.info("Reading CSV with header: " + csvSplit[0]);
		List<String> line1 = CsvUtil.parseLine(csvSplit[1]);
		assertTrue(line1.get(0).equals("137a5f7e-eb95-4754-8ef7-9be641430660"));
		List<String> line3 = CsvUtil.parseLine(csvSplit[3], ',', '"');
		assertTrue(line3.get(5).equals("555-406-3922"));
		log.info("Done.");
	}
}
