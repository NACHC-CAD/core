package com.nach.core.util.csv;

import java.io.File;

import org.junit.Test;

import com.nach.core.util.file.FileUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CsvCleanerIntegrationTest {

	@Test
	public void shouldParseTest() {
		log.info("Starting test...");
		String fileName = "acdemo.csv";
		log.info("* * *");
		log.info("*");
		log.info("*   " + fileName);
		log.info("*");
		log.info("* * *");
		File inDir = new File("C:\\_WORKSPACES\\nachc\\_PROJECT\\current\\Womens Health\\demo\\original");
		File outDir = new File("C:\\_WORKSPACES\\nachc\\_PROJECT\\current\\Womens Health\\test\\out");
		File in = new File(inDir, fileName);
		File out = new File(outDir, fileName);
		FileUtil.mkdirs(outDir);
		CsvCleaner.clean(in, out, ",", '|');
		log.info("Done.");
	}

}
