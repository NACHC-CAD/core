package com.nach.core.util.file;

import lombok.extern.slf4j.Slf4j;

import java.io.File;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

@Slf4j
public class FileUtilGetFileIntegrationTest {
	
	private static String TEST_FILE_NAME = "/integration/files/simple/test-text-file.txt";
	private static String MAIN_FILE_NAME = "/logback-example.xml";
	
	@Test
	public void shouldGetFile() {
		log.info("Starting getFile() test...");
		File f = FileUtil.getFile(TEST_FILE_NAME);
		File f2 = FileUtil.getFile(MAIN_FILE_NAME);
		log.info(FileUtil.getCanonicalPath(f));
		log.info(FileUtil.getCanonicalPath(f2));
		log.info("Got File: " + f.getPath());
		assertTrue(f.exists());
		log.info("Done.");
	}
	
	@Test
	public void shouldGetFileAndSwapOutMvnTestClasses() {
		log.info("Starting getFile() test...");
		File f = FileUtil.getFile(MAIN_FILE_NAME, true);
		log.info("Got File: " + f.getPath());
		assertTrue(f.exists());
		log.info("Done.");
	}
}
