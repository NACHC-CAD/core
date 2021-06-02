package com.nach.core.util.json;

import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.Test;

import com.nach.core.util.file.FileUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JsonUtilPrettyPrintUnitTest {
	private String FILE_NAME = "/json/sample-json.json";
	private String FILE_NAME_FLAT = "/json/sample-json-flat.json";
	
	@Test
	public void shouldPrettyPrint() {
		log.info("Starting prettyPrint(String) Test...");
		String json = FileUtil.getAsString(FILE_NAME_FLAT);
		json = JsonUtil.prettyPrint(json);
		log.info("Done.");
	}
	
	@Test
	public void shouldRemoveWhiteSpaces() {
		log.info("Starting removeWhiteSpaces(String) Test...");
		String json = FileUtil.getAsString(FILE_NAME);
		json = JsonUtil.removeWhiteSpaces(json);
		String flat_json = FileUtil.getAsString(FILE_NAME_FLAT);
		assertTrue(flat_json.contains(json));
		log.info("Done.");
	}
	
	@Test
	public void shouldRemoveWhiteSpacesFile() {
		log.info("Starting removeWhiteSpaces(File) Test...");
		File jsonFile = FileUtil.getFile(FILE_NAME);
		String json = JsonUtil.removeWhiteSpaces(jsonFile);
		String flat_json = FileUtil.getAsString(FILE_NAME_FLAT);
		assertTrue(flat_json.contains(json));
		log.info("Done.");
	}
	
}
