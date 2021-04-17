package com.nach.core.util.file;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.InputStream;

import org.junit.Test;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * Tests method that gets file as input stream based on classpath style name.
 *
 */
@Slf4j
public class FileUtilGetInputStreamIntegrationTest {

	private static String FILE_NAME = "/integration/files/simple/test-text-file.txt";

	
	//TODO Does not work with Backslash Path
	@Test
	public void shouldGetInputStream() {
		log.info("Starting getInputStream(String) test...");
		InputStream in = FileUtil.getInputStream(FILE_NAME);
		log.info("Got inputstream: " + in);
		String str = FileUtil.getAsString(in);
		log.info("Got String: " + str.trim());
		assertTrue("Hello World".equals(str.trim()));
		log.info("Done.");
	}
	
	@Test
	public void shouldGetInputStreamFromFile() {
		log.info("Starting getInputStream(File) test...");
		File f = FileUtil.getFile(FILE_NAME);
		InputStream in = FileUtil.getInputStream(f);
		log.info("Got inputstream: " + in);
		String str = FileUtil.getAsString(in);
		log.info("Got String: " + str.trim());
		assertTrue("Hello World".equals(str.trim()));
		log.info("Done.");
	}
}
