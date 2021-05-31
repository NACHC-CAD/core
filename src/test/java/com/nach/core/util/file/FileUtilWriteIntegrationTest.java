package com.nach.core.util.file;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.InputStream;

import org.junit.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FileUtilWriteIntegrationTest {
	private static String FILE_NAME = "/integration/files/simple/writable-file.txt";
	private static String INPUT_FILE_NAME = "/integration/files/simple/test-text-file.txt";

	@Test
	public void shouldWriteFile() {
		log.info("Starting write(String, File) Test...");
		File f = FileUtil.getFile(FILE_NAME);
		if (f.exists()) {
			log.info("File already exists, Deleting...");
			f.delete();
		}
		FileUtil.write("Hello World", f);
		log.info("Wrote to: " + f.getPath());

		String s = FileUtil.getAsString(f);
		assertTrue(s.trim().equals("Hello World"));
		log.info("Wrote String: " + s.trim());
		log.info("Deleting Test File...");
		f.delete();
		log.info("Done.");
	}

	@Test
	public void shouldWriteFileFromInputStream() {
		log.info("Starting write(InputStream, File) Test...");
		File f = FileUtil.getFile(FILE_NAME);
		if (f.exists()) {
			log.info("File already exists, Deleting...");
			f.delete();
		}

		InputStream in = FileUtil.getInputStream(INPUT_FILE_NAME);

		FileUtil.write(in, f);
		log.info("Wrote to: " + f.getPath());

		String s = FileUtil.getAsString(f);
		assertTrue(s.trim().equals("Hello World"));
		log.info("Wrote String: " + s.trim());
		log.info("Deleting Test File...");
		f.delete();
		log.info("Done.");
	}
}
