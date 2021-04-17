package com.nach.core.util.file;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.InputStream;

import org.junit.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FileUtilHeadIntegrationTest {
	
	private static String FILE_NAME = "/integration/files/simple/test-multi-line-text-file.txt";
	
	@Test
	public void shouldHead() {
		log.info("Starting head(File, int) Test...");
		File f = FileUtil.getFile(FILE_NAME);
		String h = FileUtil.head(f, 2);
		String[] split = h.split("\n");
		String lastLine = split[split.length - 1].trim();
		log.info("Last line in head is: " + lastLine);
		assertTrue(lastLine.equals("This is World"));
	}
	
	@Test
	public void shouldHeadFromInputStream() {
		log.info("Starting head(InputStream, int) Test...");
		InputStream in = FileUtil.getInputStream(FILE_NAME);
		String h = FileUtil.head(in, 2);
		String[] split = h.split("\n");
		String lastLine = split[split.length - 1].trim();
		log.info("Last line in head is: " + lastLine);
		assertTrue(lastLine.equals("This is World"));
	}
}
