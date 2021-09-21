package com.nach.core.util.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FileReader {

	private BufferedReader bufferedReader;

	private int segmentSize;
	
	private long currentLine = 0;
	
	private boolean isDone = false;

	private long totalLineCount;
	
	public FileReader(File file) {
		this(file, 800000);
	}
	
	public FileReader(File file, int segmentSize) {
		try {
			log.info("Getting line count...");
			this.segmentSize = segmentSize;
			this.bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8));
			this.totalLineCount = FileUtil.getLineCount(file);
			log.info("Line count: " + totalLineCount);
		} catch(Exception exp) {
			throw new RuntimeException(exp);
		}
	}
	
	public long getCurrentLine() {
		return this.currentLine;
	}
	
	public boolean getIsDone() {
		return this.isDone;
	}
	
	public long getTotalLineCount() {
		return this.totalLineCount;
	}
	
	public String getSegment() {
		try {
			StringBuffer msg = new StringBuffer();
			for(String nextLine = this.bufferedReader.readLine();nextLine != null; nextLine = bufferedReader.readLine()) {
				currentLine++;
				msg.append(nextLine);
				msg.append("\n");
				if(msg.length() > segmentSize) {
					return msg.toString();
				} else {
					continue;
				}
			}
			isDone = true;
			return msg.toString();
		} catch(Exception exp) {
			throw new RuntimeException(exp);
		}
	}

}
