package com.nach.core.util.file;

import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FileUtilGetAsStringIntegrationTest {
	
	private static String FILE_NAME = "/integration/files/simple/test-text-file.txt";
	
	@Test
	public void shouldGetAsString() {
		
		log.info("Starting getAsString(String) Test...");
		String s = FileUtil.getAsString(FILE_NAME);
		log.info("File Contains: " + s.trim());
		assertTrue(s.trim().equals("Hello World"));
		log.info("Done.");
	}
	
	@Test
	public void shouldGetAsStringFromFile() {
		log.info("Starting getAsString(File) Test...");
		File f = FileUtil.getFile(FILE_NAME);
		String s = FileUtil.getAsString(f);
		log.info("File Contains: " + s.trim());
		assertTrue(s.trim().equals("Hello World"));
		log.info("Done.");
	}
}
