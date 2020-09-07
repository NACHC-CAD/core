package com.nach.core.util.file;

import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ZipUtilUnzipIntegrationTest {

	private static final String DIR = "/com/nach/core/testfiles/excel";

	private static final String SRC_FILE_NAME = DIR + "/large-file-for-excel-test.zip";

	private static final String UNZIPPED_DIR_NAME = DIR + "/unzipped";

	private static final String TARGET_CSV = DIR + "/unzipped/large-file-for-excel-test.csv";
	
	@Test
	public void shouldParseToCsvFile() throws Exception {
		log.info("Starting test..");
		log.info("Unzipping file");
		File zipFile = FileUtil.getFile(SRC_FILE_NAME);
		File dstDir = FileUtil.getFile(UNZIPPED_DIR_NAME);
		log.info("Writing zip file to: " + dstDir.getCanonicalPath());
		ZipUtil.unzip(zipFile, dstDir);
		File targetFile = FileUtil.getFile(TARGET_CSV);
		log.info("Looking for file at: " + targetFile.getCanonicalPath());
		assertTrue(targetFile.exists());
		log.info("Done.");
	}

}
