package com.nach.core.util.file;

import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FileUtilMkdirRmdirIntegrationTest {
	private static String DIR = "/integration/files/test-dir/";
	private static String NAME = "test-sub-dir";
	
	@Test
	public void shouldMkdirRmdir() {
		log.info("Starting mkdirs(File) Test...");
		File f = FileUtil.getFile(DIR);
		FileUtil.mkdirs(f);
		assertTrue(f.exists());
		log.info("mkdir Success");
		log.info("Attempting rmdir(file)");
		FileUtil.rmdir(f);
		assertTrue(f.exists() == false);
		log.info("rmdir Success");
		log.info("Done");
	}
	
	@Test
	public void shouldMkdirRmdirWithName() {
		log.info("Starting mkdirs(File, String) Test...");
		File f = FileUtil.getFile(DIR);
		FileUtil.mkdirs(f, NAME);
		File new_f = FileUtil.getFile(DIR + "/" + NAME);
		assertTrue(f.exists());
		assertTrue(new_f.exists());
		log.info("mkdir Success");
		log.info("Attempting rmdir(file)");
		FileUtil.rmdir(f);
		assertTrue(f.exists() == false);
		log.info("rmdir Success");
		log.info("Done");
	}
}
