package com.nach.core.util.file;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.List;

import org.junit.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FileUtilListFilesIntegrationTest {
	private static String DIR = "/integration/files/sample-dir";
	
	@Test
	public void shouldList() {
		log.info("Starting list(File) Test...");
		File f = FileUtil.getFile(DIR);
		List<File> l = FileUtil.list(f);
		log.info("Found " + l.size() + " files in " + f.getPath());
		assertTrue(l.get(0).getName().equals("1.csv"));
		assertTrue(l.get(1).getName().equals("1.txt"));
		assertTrue(l.get(2).getName().equals("1.zip"));
		assertTrue(l.get(3).getName().equals("2.txt"));
		assertTrue(l.get(4).getName().equals("3.csv"));
		log.info("Done.");
	}
	
	@Test
	public void shouldListFilesWithPattern() {
		log.info("Starting listFiles(File, String) Test...");
		File f = FileUtil.getFile(DIR);
		List<File> l = FileUtil.listFiles(f, "1.*");
		log.info("Found " + l.size() + " files matching pattern");
		assertTrue(l.get(0).getName().equals("1.csv"));
		assertTrue(l.get(1).getName().equals("1.txt"));
		assertTrue(l.get(2).getName().equals("1.zip"));
		log.info("Done.");
	}
	
	@Test
	public void shouldListFilesWithPatternAndExclusion() {
		log.info("Starting listFiles(File, String, String) Test...");
		File f = FileUtil.getFile(DIR);
		List<File> l = FileUtil.listFiles(f, "1.*", "*.zip");
		log.info("Found " + l.size() + " files matching pattern");
		assertTrue(l.get(0).getName().equals("1.csv"));
		assertTrue(l.get(1).getName().equals("1.txt"));
		assertTrue(l.size() == 2);
		log.info("Done.");
	}
	
	@Test
	public void shouldListFilesWithIncludes() {
		log.info("Starting listFiles(File, String[]) Test...");
		File f = FileUtil.getFile(DIR);
		String[] pattern = {"1.*"};
		List<File> l = FileUtil.listFiles(f, pattern);
		log.info("Found " + l.size() + " files matching pattern");
		assertTrue(l.get(0).getName().equals("1.csv"));
		assertTrue(l.get(1).getName().equals("1.txt"));
		assertTrue(l.get(2).getName().equals("1.zip"));
		log.info("Done.");
	}
	
	@Test
	public void shouldListFiles() {
		log.info("Starting listFiles(File) Test...");
		File f = FileUtil.getFile(DIR);
		List<File> l = FileUtil.list(f);
		log.info("Found " + l.size() + " files in " + f.getPath());
		assertTrue(l.get(0).getName().equals("1.csv"));
		assertTrue(l.get(1).getName().equals("1.txt"));
		assertTrue(l.get(2).getName().equals("1.zip"));
		assertTrue(l.get(3).getName().equals("2.txt"));
		assertTrue(l.get(4).getName().equals("3.csv"));
		log.info("Done.");
	}
	
	@Test
	public void shouldRemoveStartsWith() {
		log.info("Starting removeStartsWith(File) Test...");
		File f = FileUtil.getFile(DIR);
		List<File> l = FileUtil.list(f);
		int s0 = l.size();
		log.info("Found " + s0 + " files in " + f.getPath());
		l = FileUtil.removeStartsWith(l, "1");
		log.info("Removed " + (s0 - l.size()) + " files from list");
		assertTrue(l.get(0).getName().equals("2.txt"));
		assertTrue(l.get(1).getName().equals("3.csv"));
		log.info("Done.");
	}
}
