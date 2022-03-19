package com.nach.core.util.file;

import org.junit.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NotJarUtilManualTest {

	@Test
	public void shouldGetDirListing() {
		log.info("Starting test...");
		String dir = "D:\\NACHC\\SYNTHEA\\_DEV\\test-set-10";
		FileUtil.listResources(dir, getClass());
		log.info("Done.");
	}
	
}
