package com.nach.core.util.file;

import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FileUtilGetFromProjectRootIntegrationTest {
	
	private static String FILE_NAME = "/target/test-classes/integration/files/simple/test-text-file.txt";
	
	@Test
	public void shouldGetFromProjectRoot() {
		log.info("Starting getFromProjectRoot() test");
		File f = FileUtil.getFromProjectRoot(FILE_NAME);
		log.info("Getting File:" + f.getPath());
		assertTrue(f.exists());
		log.info("Done.");
	}
}
