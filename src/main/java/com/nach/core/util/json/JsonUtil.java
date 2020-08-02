package com.nach.core.util.json;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.nach.core.util.file.FileUtil;

public class JsonUtil {

	/**
	 * Convert a json array to an array of Strings.
	 */
	public static List<String> toArrayList(String json) {
		try {
			JSONArray jsonArray = new JSONArray(json);
			List<String> rtn = toArrayList(jsonArray);
			return rtn;
		} catch(Exception exp) {
			try {
				JSONObject obj = new JSONObject(json);
				ArrayList<String> rtn = new ArrayList<String>();
				rtn.add(obj.toString());
				return rtn;
			} catch(Exception exp2) {
				return null;
			}
		}
	}

	/**
	 * Convert a json array to an array of Strings.
	 */
	public static List<String> toArrayList(JSONArray jsonArray) {
		ArrayList<String> rtn = new ArrayList<String>();
		for (Object obj : jsonArray) {
			if (obj instanceof JSONObject) {
				JSONObject jsonObj = (JSONObject) obj;
			}
			if (obj == null) {
				rtn.add(null);
			} else {
				rtn.add(obj.toString());
			}
		}
		return rtn;
	}

	/**
	 * Convert a json array to an array of JSONObjects.
	 */
	public static ArrayList<JSONObject> toJsonObjectArrayList(JSONArray jsonArray) {
		ArrayList<JSONObject> rtn = new ArrayList<JSONObject>();
		for (Object obj : jsonArray) {
			if (obj != null && obj instanceof JSONObject) {
				JSONObject jsonObj = (JSONObject) obj;
				rtn.add(jsonObj);
			}
		}
		return rtn;
	}

	/**
	 * Convert a JSONObject to an array of JSONObjects.
	 */
	public List<JSONObject> getList(JSONObject jsonObj, String name) {
		JSONArray jsonArray = jsonObj.getJSONArray(name);
		ArrayList<JSONObject> rtn = toJsonObjectArrayList(jsonArray);
		return rtn;
	}

	//
	// pretty print
	//

	/**
	 * Get a pretty printed version of a json string.
	 */
	public static String prettyPrint(String json) {
		return prettyPrint(json, 2);
	}

	/**
	 * Get a pretty printed version of a json string.
	 */
	public static String prettyPrint(String json, int spacesToIndentEachLevel) {
		try {
			String response = new JSONObject(json).toString(spacesToIndentEachLevel);
			return response;
		} catch (JSONException exp1) {
			try {
				String response = new JSONArray(json).toString(spacesToIndentEachLevel);
				return response;
			} catch (Exception exp2) {
				return json;
			}
		}
	}

	//
	// remove white spaces
	//

	/**
	 * Unprittyprint.
	 */
	public static String removeWhiteSpaces(String json) {
		try {
			String response = new JSONObject(json).toString();
			return response;
		} catch (JSONException exp1) {
			try {
				String response = new JSONArray(json).toString();
				return response;
			} catch (Exception exp2) {
				return json;
			}
		}
	}

	/**
	 * Unprettyprint a file.
	 */
	public String removeWhiteSpaces(File file) {
		String json = FileUtil.getAsString(file);
		try {
			JSONObject obj = new JSONObject(json);
			return obj.toString();
		} catch (Exception exp) {
			JSONArray array = new JSONArray(json);
			return array.toString();
		}
	}

}
