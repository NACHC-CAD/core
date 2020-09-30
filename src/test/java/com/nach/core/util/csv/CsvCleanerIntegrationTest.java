package com.nach.core.util.csv;

import java.io.File;

import org.junit.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CsvCleanerIntegrationTest {

	@Test
	public void shouldParseTest() {
		log.info("Starting test...");
		File dir = new File("C:\\_WORKSPACES\\nachc\\_PROJECT\\current\\Womens Health\\demo");
//		File in = new File(dir, "\\thumb\\acdemo-thumbnail-10.csv");
//		File out = new File(dir, "\\test\\acdemo-thumbnail-10_OUT.csv");
		File in = new File(dir, "\\acdemo.csv");
		File out = new File(dir, "\\test\\acdemo.csv");
		CsvCleaner.clean(in, out, ",", '|');
		log.info("Done.");
	}

}
