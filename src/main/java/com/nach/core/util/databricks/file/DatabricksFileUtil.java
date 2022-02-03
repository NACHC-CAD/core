package com.nach.core.util.databricks.file;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.yaorma.util.time.Timer;

import com.nach.core.util.databricks.file.exception.DatabricksFileException;
import com.nach.core.util.databricks.file.response.DatabricksFileUtilResponse;
import com.nach.core.util.file.FileUtil;
import com.nach.core.util.http.HttpFileUpload3;
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

	public List<String> listFileNames(String dirPath) {
		ArrayList<String> rtn = new ArrayList<String>();
		String json = list(dirPath);
		List<String> files = JsonUtil.getJsonArray(json, "files");
		for (String fileJson : files) {
			String path = JsonUtil.getString(fileJson, "path");
			path = path.substring(path.lastIndexOf("/") + 1, path.length());
			rtn.add(path);
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
		// return put(databricksDirPath, file, false);
		return putLargeFile(databricksDirPath, file);
	}

	public DatabricksFileUtilResponse putLargeFile(String databricksDirPath, File file) {
		log.info("* * * UPLOADING FILE USING METHOD FOR LARGE FILES * * *");
		log.info("File Path: " + FileUtil.getCanonicalPath(file));
		log.info("File: " + file.getName());
		log.info("SIZE: " + FileUtil.getSize(file));
		Timer timer = new Timer();
		timer.getStart();
		String databricksFilePath = databricksDirPath + "/" + file.getName();
		boolean success = HttpFileUpload3.uploadFile(this.baseUrl, token, databricksFilePath, file);
		timer.getStop();
		DatabricksFileUtilResponse resp = new DatabricksFileUtilResponse();
		resp.setStartTime(timer.getStart());
		resp.setEndTime(timer.getStop());
		resp.setDatabricksFilePath(databricksDirPath + "/" + file.getName());
		resp.setElapsedSeconds(timer.getElapsed());
		resp.setElapsedTime(timer.getElapsedInMilliseconds());
		resp.setEndTimeString(timer.getElapsedString());
		resp.setFileExists(file.exists());
		resp.setFileName(file.getName());
		resp.setFilePath(FileUtil.getCanonicalPath(file));
		resp.setFileSize(file.length());
		resp.setResponse("Not Implemented");
		resp.setStartTimeString(timer.getStartAsString());
		resp.setStatusCode(0);
		resp.setSuccess(success);
		resp.setUrl(this.baseUrl);
		return resp;
	}

	/**
	 * 
	 * Put a file on the server. Replace existing file if replace if file exists on
	 * the server.
	 * 
	 */
	public DatabricksFileUtilResponse replace(String databricksDirPath, File file) {
		return put(databricksDirPath, file, true);
	}

	/**
	 * 
	 * Put a file on the server. Replace existing file if replace is set to true.
	 * 
	 */
	public DatabricksFileUtilResponse put(String databricksDirPath, File file, boolean replace) {
		Timer timer = new Timer();
		timer.start();
		String url = baseUrl + "/dbfs/put";
		HttpRequestClient client = new HttpRequestClient(url);
		client.setOauthToken(token);
		String filePath = databricksDirPath + "/" + file.getName();
		client.addFormData("path", filePath);
		if (replace == true) {
			client.addFormData("overwrite", "true");
		}
		log.info("Doing post");
		client.postFile(file, filePath, replace);
		log.info("Done with post");
		// create rtn object
		timer.stop();
		log.info("Getting response");
		DatabricksFileUtilResponse rtn = new DatabricksFileUtilResponse();
		rtn.init(client, file, timer, filePath);
		if (rtn.isSuccess() == false) {
			log.info(rtn.isSuccess() + ": (" + rtn.getStatusCode() + ") " + rtn.getDatabricksFilePath() + "\t" + rtn.getResponse());
			throw new RuntimeException("Put failed for " + databricksDirPath, new DatabricksFileException(rtn));
		}
		log.info("Got response");
		return rtn;
	}

	/**
	 * 
	 * Method to put a file on the server from an input stream. The filePath is the
	 * path with out the file name. The file will be placed at filePath/fileName
	 * location.
	 * 
	 */
	public DatabricksFileUtilResponse put(String databricksDirPath, InputStream in, String fileName, boolean overwrite) {
		Timer timer = new Timer();
		timer.start();
		String url = baseUrl + "/dbfs/put";
		HttpRequestClient client = new HttpRequestClient(url);
		client.setOauthToken(token);
		String filePath = databricksDirPath + "/" + fileName;
		client.addFormData("path", filePath);
		client.postFile(fileName, in, filePath, overwrite);
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

	public DatabricksFileUtilResponse put(String databricksDirPath, String fileAsString, String fileName, boolean overwrite) {
		InputStream in = new ByteArrayInputStream(fileAsString.getBytes());
		return put(databricksDirPath, in, fileName, overwrite);
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
		return get(filePath, null, null);
	}

	public DatabricksFileUtilResponse get(String filePath, Integer offset, Integer length) {
		Timer timer = new Timer();
		timer.start();
		String url = baseUrl + "/dbfs/read";
		url += "?path=" + filePath;
		if (offset != null && length != null) {
			url += "&offset=" + offset;
			url += "&length=" + length;
		}
		HttpRequestClient client = new HttpRequestClient(url);
		client.setOauthToken(token);
		// String json = "{\"path\":\"" + filePath + "\"}";
		log.info("URL: \n" + client.getUrl());
		client.doGet();
		timer.stop();
		DatabricksFileUtilResponse rtn = new DatabricksFileUtilResponse();
		rtn.init(client, null, timer, filePath);
		if (rtn.isSuccess()) {
			String data = JsonUtil.getString(rtn.getResponse(), "data");
			rtn.initInputStreamFromBase64String(data);
			String bytesRead = JsonUtil.getString(rtn.getResponse(), "bytes_read");
			rtn.setBytesReadString(bytesRead);
		}
		return rtn;
	}

	public void writeLargeFile(String url, File target) {
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(target);
			boolean moreData = true;
			int offset = 0;
			int length = 1000000;
			while (moreData) {
				DatabricksFileUtilResponse resp = get(url, offset, length);
				Integer bytesRead = Integer.parseInt(resp.getBytesReadString());
				offset += bytesRead;
				log.info("\tBYTES READ: " + bytesRead);
				log.info("TOTAL BYTES:  " + offset);
				InputStream in = resp.getInputStream();
				writeLines(in, out);
				if (bytesRead < length) {
					moreData = false;
				}
			}
		} catch (Exception exp) {
			throw new RuntimeException(exp);
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (Exception exp) {
					throw new RuntimeException(exp);
				}
			}
		}
	}

	private void writeLines(InputStream source, OutputStream target) {
		try {
		    byte[] buf = new byte[8192];
		    int length;
		    while ((length = source.read(buf)) > 0) {
		        target.write(buf, 0, length);
		    }
		} catch(Exception exp) {
			throw new RuntimeException(exp);
		}
	}

}
