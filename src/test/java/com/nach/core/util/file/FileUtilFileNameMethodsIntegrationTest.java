package com.nach.core.util.file;

import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FileUtilFileNameMethodsIntegrationTest {
	private static String FILE_NAME = "/integration/files/simple/test-text-file.txt";
	private static  String NAME = "test-text-file.txt";
	
	@Test
	public void shouldGetPrefixFromString() {
		log.info("Starting getPrefix(String) Test...");
		String s = FileUtil.getPrefix(NAME);
		log.info("Got prefix: " + s);
		assertTrue(s.equals("test-text-file"));
		log.info("Done.");
	}
	
	@Test
	public void shouldGetPrefixFromFile() {
		log.info("Starting getPrefix(File) Test...");
		File f = FileUtil.getFile(FILE_NAME);
		String s = FileUtil.getPrefix(f);
		log.info("Got prefix: " + s);
		assertTrue(s.equals("test-text-file"));
		log.info("Done.");
	}
	
	@Test
	public void shouldGetSuffixFromString() {
		log.info("Starting getSuffix(String) Test...");
		String s = FileUtil.getSuffix(NAME);
		log.info("Got suffix: " + s);
		assertTrue(s.equals("txt"));
		log.info("Done.");
	}
	
	@Test
	public void shouldGetSuffixFromFile() {
		log.info("Starting getSuffix(File) Test...");
		File f = FileUtil.getFile(FILE_NAME);
		String s = FileUtil.getSuffix(f);
		log.info("Got suffix: " + s);
		assertTrue(s.equals("txt"));
		log.info("Done.");
	}

	
	//Doesn't Change the suffix. Just Returns a string with new file name.
	@Test
	public void shouldChangeSuffix() {
		log.info("Starting changeSuffix(File, String) Test...");
		File f = FileUtil.getFile(FILE_NAME);
		String s = FileUtil.changeSuffix(f, "csv");
		log.info("New name: " + s);
		assertTrue(s.equals("test-text-file.csv"));
		log.info("Done.");
	}
}
