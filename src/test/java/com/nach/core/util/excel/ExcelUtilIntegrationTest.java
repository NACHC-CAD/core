package com.nach.core.util.excel;

import java.io.File;

import org.junit.Test;

import com.nach.core.util.file.FileUtil;
import com.nach.core.util.file.ZipUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ExcelUtilIntegrationTest {

	private static final String DIR = "/com/nach/core/util/excel";
	
	private static final String SRC_FILE_NAME = DIR + "/large-file-for-excel-test.zip";
	
	private static final String UNZIPPED_DIR_NAME = DIR + "/unzipped";
	
	@Test
	public void shouldParseToCsvFile() throws Exception {
		log.info("Starting test..");
		log.info("Unzipping file");
		File zipFile = FileUtil.getFile(SRC_FILE_NAME);
		File dstDir = FileUtil.getFile(UNZIPPED_DIR_NAME);
		log.info("Writing zip file to: " + dstDir.getCanonicalPath());
		ZipUtil.unzip(zipFile, dstDir);
		log.info("Done.");
	}
	
}
