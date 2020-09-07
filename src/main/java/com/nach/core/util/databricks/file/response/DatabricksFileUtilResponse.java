package com.nach.core.util.databricks.file.response;

import java.io.File;

import com.nach.core.util.http.HttpRequestClient;
import com.nach.core.util.time.TimeUtil;
import com.nach.core.util.time.Timer;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DatabricksFileUtilResponse {
	
	private String url; 

	private long startTime;
	
	private String startTimeString;
	
	private long endTime;
	
	private String endTimeString;
	
	private long elapsedTime;
	
	private double elapsedSeconds;
	
	private String fileName;
	
	private String filePath;
	
	private String databricksFilePath;
	
	private double fileSize;
	
	private String response;
	
	private int statusCode;
	
	private boolean success = false;
	
	private boolean fileExists = false;
	
	public void init(HttpRequestClient client, File file, Timer timer, String databricksFilePath) {
		init(client);
		init(file);
		init(timer);
		this.databricksFilePath = databricksFilePath;
	}
	
	public void init(HttpRequestClient client) {
		this.url = client.getUrl();
		this.statusCode = client.getStatusCode();
		this.response = client.getResponse().trim();
		this.success = statusCode == 200;
	}
	
	public void init(File file) {
		if(file != null) {
			try {
				this.fileName = file.getName();
				this.filePath = file.getCanonicalPath();
				this.fileSize = file.length()/1000000;
			} catch(Exception exp) {
				throw new RuntimeException(exp);
			}
		}
	}
	
	public void init(Timer timer) {
		this.startTime = timer.getStart();
		this.endTime = timer.getStart();
		this.elapsedTime = timer.getElapsedInMilliseconds();
		this.elapsedSeconds = timer.getElapsed();
		this.startTimeString = TimeUtil.getTimeString(startTime);
		this.endTimeString = TimeUtil.getTimeString(endTime);
	}
	
	public void init(String databricksFilePath) {
		this.databricksFilePath = databricksFilePath;
	}
	
}
