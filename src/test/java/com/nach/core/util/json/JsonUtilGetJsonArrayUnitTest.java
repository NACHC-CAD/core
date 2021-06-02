package com.nach.core.util.json;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import com.nach.core.util.file.FileUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JsonUtilGetJsonArrayUnitTest {
	
	private String FILE_NAME = "/json/sample-json.json";

	@Test
	public void shouldGetJsonArray() {
		log.info("Starting getJsonArray(String, String) Test...");
		String json = FileUtil.getAsString(FILE_NAME);
		List<String> jsonList = JsonUtil.getJsonArray(json, "name");
		log.info("First element in list is: " + jsonList.get(0));
		assertTrue(jsonList.get(0).equals("{\"given\":[\"Peter\",\"James\"],\"use\":\"official\",\"family\":\"Chalmers\"}"));
		log.info("Done.");
	}
	
	@Test
	public void shouldToArrayList() {
		log.info("Starting ToArrayList(String) Test...");
		String json = FileUtil.getAsString(FILE_NAME);
		String jsonListString = JsonUtil.getAsString(json, "name");
		List<String> jsonList = JsonUtil.toArrayList(jsonListString);
		log.info("First element in list is: " + jsonList.get(0));
		assertTrue(jsonList.get(0).equals("{\"given\":[\"Peter\",\"James\"],\"use\":\"official\",\"family\":\"Chalmers\"}"));
		log.info("Done.");
	}
	
	@Test
	public void shouldToArrayListJsonArray() {
		log.info("Starting toArrayList(JsonArray) Test...");
		String json = FileUtil.getAsString(FILE_NAME);
		try {
			JSONObject obj = new JSONObject(json);
			JSONArray arr = obj.getJSONArray("name");
			List<String> jsonList = JsonUtil.toArrayList(arr);
			log.info("First element in list is: " + jsonList.get(0));
			assertTrue(jsonList.get(0).equals("{\"given\":[\"Peter\",\"James\"],\"use\":\"official\",\"family\":\"Chalmers\"}"));
			log.info("Done.");
		} catch (JSONException e) {
			log.info("Failed to make JSON Object");
		}
	}
	
	@Test
	public void shouldToJsonObjectArrayList() {
		log.info("Starting toJsonObjectArrayList(JsonArray) Test...");
		String json = FileUtil.getAsString(FILE_NAME);
		try {
			JSONObject obj = new JSONObject(json);
			JSONArray arr = obj.getJSONArray("name");
			ArrayList<JSONObject> jsonList = JsonUtil.toJsonObjectArrayList(arr);
			String entry = jsonList.get(0).toString();
			log.info("First element in list is: " + entry);
			assertTrue(entry.equals("{\"given\":[\"Peter\",\"James\"],\"use\":\"official\",\"family\":\"Chalmers\"}"));
			log.info("Done.");
		} catch (JSONException e) {
			log.info("Failed to make JSON Object");
		}
	}
}
