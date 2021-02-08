package com.nach.core.util.http;

import java.io.File;
import java.io.FileInputStream;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HttpFileUpload {

	public static void main(String[] args) throws Exception {
		log.info("Starting...");

		String url = "https://nachc-nachc-databricks-dev.cloud.databricks.com/api/2.0/dbfs/put";
		String token = "dapied9d7b92a7cd009aad5568b21ebb23b1";
		String path = "/FileStore/tables/prod/million-hearts/delete_me/tail.txt";
		String srcFile = "C:\\_WORKSPACES\\nachc\\_PROJECT\\cosmos\\million-hearts\\me-med-mapping-2021-02-02\\flat\\he\\tail.txt";
		File file = new File(srcFile);

		MultipartEntityBuilder builder = MultipartEntityBuilder.create();
		builder.addTextBody("path", path, ContentType.TEXT_PLAIN);
		builder.addBinaryBody("file", file, ContentType.APPLICATION_OCTET_STREAM, file.getName());
		HttpEntity httpEntity = builder.build();

		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(url);
		httpPost.addHeader("Authorization", "Bearer " + token);
		httpPost.setEntity(httpEntity);

		log.info("Starting upload");
		CloseableHttpResponse response = httpClient.execute(httpPost);
		// we never get to this line
		log.info("Done with upload");
		HttpEntity responseEntity = response.getEntity();
		log.info("Got response");
		log.info("Done.");
	}

}
