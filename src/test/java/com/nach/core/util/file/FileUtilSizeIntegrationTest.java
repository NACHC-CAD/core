package com.nach.core.util.file;

import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FileUtilSizeIntegrationTest {
	private static String FILE_NAME = "/integration/files/simple/test-text-file.txt";
	
	@Test
	public void shouldSize() {
		log.info("Starting size(File) Test...");
		File f = FileUtil.getFile(FILE_NAME);
		long len = FileUtil.size(f);
		log.info("File is " + len + " bytes long");
		assertTrue(len == 11);
		log.info("Done.");
	}
	
	@Test
	public void shouldGetSize() {
		log.info("Starting getSize(File) Test...");
		File f = FileUtil.getFile(FILE_NAME);
		long len = FileUtil.getSize(f);
		log.info("File is " + len + " bytes long");
		assertTrue(len == 11);
		log.info("Done.");
	}
}
