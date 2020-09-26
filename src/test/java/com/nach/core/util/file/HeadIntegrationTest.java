package com.nach.core.util.file;

import java.io.File;

import org.junit.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HeadIntegrationTest {

	@Test
	public void shouldGetFirstRows() {
		log.info("Starting test...");
		log.info("Getting file");
		String name = "/com/nach/core/testfiles/excel/medium-file-for-excel-test.csv";
		File file = FileUtil.getFile(name);
		log.info("Reading file");
		String str = FileUtil.head(file, 5);
		log.info("Got String: \n" + str);
		log.info("Done.");
	}

}
