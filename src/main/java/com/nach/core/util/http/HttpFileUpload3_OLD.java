package com.nach.core.util.http;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Base64;

import com.nach.core.util.databricks.database.DatabricksDbUtil;
import com.nach.core.util.databricks.file.DatabricksFileUtil;
import com.nach.core.util.file.FileUtil;
import com.nach.core.util.json.JsonUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HttpFileUpload3_OLD {

	public static void main(String[] args) throws Exception {
		try {
			log.info("Starting...");
			String url = "https://nachc-nachc-databricks-dev.cloud.databricks.com/api/2.0";
			String token = "dapied9d7b92a7cd009aad5568b21ebb23b1";
			String path = "/FileStore/tables/prod/million-hearts/delete_me/delete_me.txt";
			// String srcFileName = "C:\\_WORKSPACES\\nachc\\_PROJECT\\cosmos\\million-hearts\\me-med-mapping-2021-02-02\\flat\\he\\HealthEfficient Statin Therapy Medication Output from 1_31_2019 to 7_31_2020.csv";
			String srcFileName = "C:\\_WORKSPACES\\nachc\\_PROJECT\\cosmos\\million-hearts\\me-med-mapping-2021-02-02\\_ETC\\tail.txt";
			File file = new File(srcFileName);
			uploadFile(url, token, path, file);
		} finally {

		}
	}

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
		BufferedReader reader = null;
		try {
			log.info("--------------------");
			log.info("handleMsg: " + handleMsg);
			log.info("parsing json");
			String handle = JsonUtil.getAsString(handleMsg, "handle");
			log.info("json successfully parsed");
			log.info("HANDLE: " + handle);
			long lineCount = FileUtil.getLineCount(file);
			StringBuffer msg = new StringBuffer();
			InputStream in = new FileInputStream(file);
			reader = new BufferedReader(new InputStreamReader(in));
			String nextLine = reader.readLine();
			int lineNumber = 1;
			int seg = 0;
			int messageSize = 800000;
			log.info("Seg:   " + seg);
			log.info("File:  " + file.length());
			log.info("Total: " + lineCount);
			log.info("Line:  " + lineNumber);
			log.info("(message size is " + messageSize + ")");
			while (nextLine != null) {
				if ((msg + nextLine).length() > messageSize) {
					seg++;
					if(seg % 100 == 0 || seg == 1) {
						System.out.print("\n");
						log.info("Seg:   " + seg);
						log.info("File:  " + file.length());
						log.info("Total: " + lineCount);
						log.info("Line:  " + lineNumber);
						log.info("(message size is " + messageSize + ")");
					}
					System.out.print("e");
					String msgString = Base64.getEncoder().encodeToString(msg.toString().getBytes());
					System.out.print("+");
					msgString = "{\"handle\":\"" + handle + "\",\"data\":\"" + msgString + "\"}";
					int status = 0;
					boolean success = false;
					for(int i=0;i<3;i++) {
						System.out.print("s");
						status = sendBytes(url, token, handle, msgString);
						if(status == 200) {
							System.out.print("d");
							success = true;
							break;
						} 
					}
					if(success == false) {
						throw new RuntimeException("Could not write block: " + url);
					}
					// msg = nextLine + "\n";
					msg = new StringBuffer();
					msg.append(nextLine);
					msg.append("\n");
				} else {
					// msg += nextLine + "\n";
					msg.append(nextLine);
					msg.append("\n");
				}
				nextLine = reader.readLine();
				lineNumber++;
			}
			log.info("Writing last segment of file");
			log.info("File: " + file.length());
			log.info("Line: " + lineNumber);
			log.info("Seg:  " + seg);
			String msgString = Base64.getEncoder().encodeToString(msg.toString().getBytes());
			msgString = "{\"handle\":\"" + handle + "\",\"data\":\"" + msgString + "\"}";
			sendBytes(url, token, handle, msgString);
		} catch (Exception exp) {
			throw new RuntimeException(exp);
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (Exception exp) {
					throw new RuntimeException(exp);
				}
			}
		}
	}

	private static int sendBytes(String url, String token, String handle, String msg) {
		String addBlockUrl = url + "/dbfs/add-block";
		HttpRequestClient client = new HttpRequestClient(addBlockUrl);
		client.setOauthToken(token);
		client.doPost(msg);
		int status = client.getStatusCode();
		String responseMessage = client.getResponse().trim();
		if(status != 200) {
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