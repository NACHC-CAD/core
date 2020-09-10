package com.nach.core.util.databricks.file;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.yaorma.util.time.Timer;

import com.nach.core.util.databricks.file.exception.DatabricksFileException;
import com.nach.core.util.databricks.file.response.DatabricksFileUtilResponse;
import com.nach.core.util.file.FileUtil;
import com.nach.core.util.http.HttpRequestClient;
import com.nach.core.util.json.JsonUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DatabricksFileUtil {

	private String baseUrl;

	private String token;

	public DatabricksFileUtil(String url, String token) {
		this.baseUrl = url;
		this.token = token;
	}
	
	/**
	 * 
	 * Query if a file exists at the given location on the server. Path can be a
	 * file or dir.
	 * 
	 */
	public DatabricksFileUtilResponse exists(String path) {
		Timer timer = new Timer();
		timer.start();
		DatabricksFileUtilResponse rtn = new DatabricksFileUtilResponse();
		String url = baseUrl + "/dbfs/get-status?path=" + path;
		HttpRequestClient client = new HttpRequestClient(url);
		client.setOauthToken(token);
		client.doGet();
		timer.stop();
		rtn.init(client);
		rtn.init(timer);
		rtn.init(path);
		// modify the status to reflect the file not found status
		int statusCode = client.getStatusCode();
		if (statusCode == 200) {
			rtn.setSuccess(true);
			rtn.setFileExists(true);
		} else {
			rtn.setSuccess(false);
			rtn.setFileExists(false);
		}
		if (statusCode == 404) {
			rtn.setSuccess(true);
			rtn.setFileExists(false);
		}
		return rtn;
	}

	/**
	 * 
	 * Get a directory listing of the given path.
	 * 
	 */
	public String list(String dirPath) {
		String url = baseUrl + "/dbfs/list?path=" + dirPath;
		HttpRequestClient client = new HttpRequestClient(url);
		client.setOauthToken(token);
		client.doGet();
		String response = client.getResponse();
		return response;
	}

	/**
	 * 
	 * Put all the files in a dir that match the pattern on the Databricks server.  
	 * 
	 */	
	public List<DatabricksFileUtilResponse> put(String dirPath, File dir, String pattern) {
		ArrayList<DatabricksFileUtilResponse> rtn = new ArrayList<DatabricksFileUtilResponse>();
		List<File> files = FileUtil.listFiles(dir, pattern);
		int cnt = 0;
		for (File file : files) {
			cnt++;
			log.info("File " + cnt + " of " + files.size());
			DatabricksFileUtilResponse resp = put(dirPath, file);
			rtn.add(resp);
		}
		return rtn;
	}

	/**
	 * 
	 * Method to put a file on the server. The filePath is the path with out the
	 * file name. The file will be placed at filePath/fileName location.
	 * 
	 */
	public DatabricksFileUtilResponse put(String databricksDirPath, File file) {
		Timer timer = new Timer();
		timer.start();
		String url = baseUrl + "/dbfs/put";
		HttpRequestClient client = new HttpRequestClient(url);
		client.setOauthToken(token);
		String filePath = databricksDirPath + "/" + file.getName();
		client.addFormData("path", filePath);
		client.postFile(file, filePath);
		// create rtn object
		timer.stop();
		DatabricksFileUtilResponse rtn = new DatabricksFileUtilResponse();
		rtn.init(client, file, timer, filePath);
		if (rtn.isSuccess() == false) {
			log.info(rtn.isSuccess() + ": (" + rtn.getStatusCode() + ") " + rtn.getDatabricksFilePath() + "\t" + rtn.getResponse());
			throw new RuntimeException("Put failed for " + databricksDirPath, new DatabricksFileException(rtn));
		}
		return rtn;
	}

	/**
	 * 
	 * Method to put a file on the server from an input stream. The filePath is the path with out the
	 * file name. The file will be placed at filePath/fileName location.
	 * 
	 */
	public DatabricksFileUtilResponse put(String databricksDirPath, InputStream in, String fileName) {
		Timer timer = new Timer();
		timer.start();
		String url = baseUrl + "/dbfs/put";
		HttpRequestClient client = new HttpRequestClient(url);
		client.setOauthToken(token);
		String filePath = databricksDirPath + "/" + fileName;
		client.addFormData("path", filePath);
		client.postFile(fileName, in, filePath);
		// create rtn object
		timer.stop();
		DatabricksFileUtilResponse rtn = new DatabricksFileUtilResponse();
		rtn.init(client, null, timer, filePath);
		if (rtn.isSuccess() == false) {
			log.info(rtn.isSuccess() + ": (" + rtn.getStatusCode() + ") " + rtn.getDatabricksFilePath() + "\t" + rtn.getResponse());
			throw new RuntimeException("Put failed for " + databricksDirPath, new DatabricksFileException(rtn));
		}
		return rtn;
	}

	/**
	 * 
	 * Delete a file from the server.
	 * 
	 */
	public DatabricksFileUtilResponse delete(String filePath) {
		String url = baseUrl + "/dbfs/delete";
		HttpRequestClient client = new HttpRequestClient(url);
		client.setOauthToken(token);
		String json = "{\"path\":\"" + filePath + "\"}";
		client.doPost(json);
		DatabricksFileUtilResponse rtn = new DatabricksFileUtilResponse();
		rtn.init(client);
		return rtn;
	}

	/**
	 * 
	 * Remove a directory.
	 * 
	 */
	public DatabricksFileUtilResponse rmdir(String dirPath) {
		Timer timer = new Timer();
		timer.start();
		String url = baseUrl + "/dbfs/delete";
		HttpRequestClient client = new HttpRequestClient(url);
		client.setOauthToken(token);
		String json = "{\"path\":\"" + dirPath + "\", \"recursive\":\"true\"}";
		client.doPost(json);
		timer.stop();
		DatabricksFileUtilResponse rtn = new DatabricksFileUtilResponse();
		rtn.init(client, null, timer, dirPath);
		return rtn;
	}

	/**
	 * 
	 * Download a file from the Databricks server.  
	 * 
	 */
	public DatabricksFileUtilResponse get(String filePath) {
		Timer timer = new Timer();
		timer.start();
		String url = baseUrl + "/dbfs/read";
		url += "?path=" + filePath;
		HttpRequestClient client = new HttpRequestClient(url);
		client.setOauthToken(token);
		String json = "{\"path\":\"" + filePath + "\"}";
		client.doGet();
		timer.stop();
		DatabricksFileUtilResponse rtn = new DatabricksFileUtilResponse();
		rtn.init(client, null, timer, filePath);
		if(rtn.isSuccess()) {
			String data = JsonUtil.getString(rtn.getResponse(), "data");
			rtn.initInputStreamFromBase64String(data);
		}
		return rtn;
	}
	
}
