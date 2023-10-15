package com.nach.core.util.http;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Base64;

import com.nach.core.util.file.FileReader;
import com.nach.core.util.file.FileUtil;
import com.nach.core.util.json.JsonUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HttpFileUpload3 {

	public static boolean uploadFile(String url, String token, String path, File file) {
		// write the file
		String handleMsg = openConnection(url, token, path);
		writeFile(url, token, handleMsg, file);
		String closeResp = closeConnection(url, token, handleMsg);
		// done
		log.info("Close response: " + closeResp);
		log.info("--- DONE ---");
		log.info("Done.");
		return true;
	}

	private static String openConnection(String url, String token, String path) {
		// open connection
		log.info("--- OPEN CONNECTION ---");
		String createMsg = "{\"path\":\"" + path + "\",\"overwrite\":\"true\"}";
		String createUrl = url + "/dbfs/create";
		HttpRequestClient client = new HttpRequestClient(createUrl);
		client.setOauthToken(token);
		client.doPost(createMsg);
		String response = client.getResponse();
		response = response.trim();
		log.info("STATUS: " + client.getStatusCode());
		log.info("Got response: " + response);
		return response;
	}

	private static void writeFile(String url, String token, String handleMsg, File file) {
		// deal with getting the handle
		log.info("--------------------");
		log.info("handleMsg: " + handleMsg);
		log.info("parsing json");
		String handle = JsonUtil.getAsString(handleMsg, "handle");
		log.info("json successfully parsed");
		log.info("HANDLE: " + handle);
		// write the file
		int messageSize = 800000;
		FileReader reader = new FileReader(file);
		int segmentCount = 0;
		long bytesSent = 0;
		while(reader.getIsDone() == false) {
			if(segmentCount % 25 == 0) {
				System.out.println("");
			}
			if(segmentCount % 100 == 0) {
				log.info("Current Line: " + reader.getCurrentLine());
				log.info("Total Lines:  " + reader.getTotalLineCount());
				log.info("Segment:      " + segmentCount);
				log.info("Bytes Sent:   " + bytesSent);
			}
			String msg = reader.getSegment();
			String msgString = Base64.getEncoder().encodeToString(msg.getBytes());
			System.out.print(".");
			msgString = "{\"handle\":\"" + handle + "\",\"data\":\"" + msgString + "\"}";
			boolean success = false;
			for(int i=0;i<3;i++) {
				System.out.print("s");
				int status = sendBytes(url, token, handle, msgString);
				if(status == 200) {
					System.out.print("d");
					success = true;
					bytesSent = bytesSent + msgString.length();
					break;
				} else {
					System.out.print("R");
				}
			}
			if(success == false) {
				throw new RuntimeException("Could not write block: " + url);
			}
			System.out.print("+");
			segmentCount++;
		}
		log.info("Writing last segment of file");
		String msg = reader.getSegment();
		String msgString = Base64.getEncoder().encodeToString(msg.getBytes());
		msgString = "{\"handle\":\"" + handle + "\",\"data\":\"" + msgString + "\"}";
		boolean success = false;
		for(int i=0;i<3;i++) {
			System.out.print("s");
			int status = sendBytes(url, token, handle, msgString);
			if(status == 200) {
				System.out.print("d");
				success = true;
				break;
			} else {
				System.out.print("R");
			}
		}
		if(success == false) {
			throw new RuntimeException("Could not write block: " + url);
		}
		log.info("Current Line: " + reader.getCurrentLine());
		log.info("Total Lines:  " + reader.getTotalLineCount());
		log.info("Segment:      " + segmentCount);
		log.info("Done.");
	}

	private static int sendBytes(String url, String token, String handle, String msg) {
		String addBlockUrl = url + "/dbfs/add-block";
		HttpRequestClient client = new HttpRequestClient(addBlockUrl);
		client.setOauthToken(token);
		client.doPost(msg);
		int status = client.getStatusCode();
		String responseMessage = client.getResponse().trim();
		if (status != 200) {
			log.info("Status: " + status);
			log.info("Error from server: \n" + responseMessage);
		}
		log.debug("Done sending message.");
		return status;
	}

	private static String closeConnection(String url, String token, String handleMsg) {
		// close connection
		log.info("--- CLOSE CONNECTION ---");
		log.info("Doing close: " + handleMsg.trim());
		String closeUrl = url + "/dbfs/close";
		HttpRequestClient closeClient = new HttpRequestClient(closeUrl);
		closeClient.setOauthToken(token);
		closeClient.doPost(handleMsg);
		String response = closeClient.getResponse().trim();
		log.info("STATUS: " + closeClient.getStatusCode());
		log.info("Response: " + response);
		return response;
	}

}