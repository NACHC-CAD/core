package com.nach.core.util.csv;



import java.io.File;
import java.io.InputStream;

import org.junit.Test;

import com.nach.core.util.file.FileUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CsvToSimpleHtmlTableIntegrationTest {

	public static final File FILE = FileUtil.getFile("/csv/vocabulary.csv");

	@Test
	public void shouldGetHtmlTable() {
		try {
			log.info("Starting test...");
			InputStream is = FileUtil.getInputStream(FILE);
			String html = CsvToSimpleHtmlTable.exec(is);
			log.info("Got html: \n\n" + html + "\n\n");
			log.info("Done.");
		} catch(Exception exp) {
			throw new RuntimeException(exp);
		}
	}
	
}
