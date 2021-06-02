package com.nach.core.util.json;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.nach.core.util.file.FileUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JsonUtilGetStringUnitTest {
	private String FILE_NAME = "/json/sample-json.json";
	
	@Test
	public void shouldGetAsString() {
		log.info("Starting getAsString(String, String) Test...");
		String json = FileUtil.getAsString(FILE_NAME);
		String active = JsonUtil.getAsString(json, "active");
		assertTrue(active.equals("true"));
		log.info("Done.");
	}
	
	@Test
	public void shouldGetString() {
		log.info("Starting getString(String, String) Test...");
		String json = FileUtil.getAsString(FILE_NAME);
		String active = JsonUtil.getString(json, "active");
		assertTrue(active.equals("true"));
		log.info("Done.");
	}
}
