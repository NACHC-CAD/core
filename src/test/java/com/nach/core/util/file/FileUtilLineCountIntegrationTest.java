package com.nach.core.util.file;

import java.io.File;

import org.junit.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FileUtilLineCountIntegrationTest {

	public static final String FILE_NAME = "C:\\_WORKSPACES\\nachc\\_PROJECT\\cosmos\\omop-concepts\\vocabulary_download_v5_{9259d46c-23e7-4760-b8d6-cddd5d86be7d}_1631306538104\\CONCEPT_RELATIONSHIP.csv";			

	@Test
	public void shouldCountLines() {
		log.info("Starting test...");
		File file = new File(FILE_NAME);
		long lineCount = FileUtil.getLineCount(file);
		log.info("File has " + lineCount + " lines");
		log.info("Done.");
	}
	
}
