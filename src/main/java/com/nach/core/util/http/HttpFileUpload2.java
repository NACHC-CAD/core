package com.nach.core.util.http;

import java.io.File;
import java.io.FileInputStream;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import com.nach.core.util.file.FileUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HttpFileUpload2 {

	public static void main(String args[]) throws Exception {
		log.info("Starting...");
		String url = "https://nachc-nachc-databricks-dev.cloud.databricks.com/api/2.0/dbfs/put";
		String token = "dapied9d7b92a7cd009aad5568b21ebb23b1";
		String path = "/FileStore/tables/prod/million-hearts/delete_me/delete_me.txt";
		String srcFile = "C:\\_WORKSPACES\\nachc\\_PROJECT\\cosmos\\million-hearts\\me-med-mapping-2021-02-02\\flat\\he\\HealthEfficient Statin Therapy Medication Output from 1_31_2019 to 7_31_2020.csv";

		File file = new File(srcFile);
		HttpPost post = new HttpPost(url);
		//
		MultipartEntityBuilder builder = MultipartEntityBuilder.create();
		builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
		builder.addTextBody("path", path, ContentType.TEXT_PLAIN);
		log.info("Converting to byte[]...");
//		byte[] bytes = IOUtils.toByteArray(new FileInputStream(file));
		log.info("File exists: " + file.exists());
		String str = FileUtil.getAsString(file);
		log.info("Adding to builder");
//		builder.addBinaryBody("upload-file", bytes, ContentType.APPLICATION_OCTET_STREAM, file.getName());
//		builder.addBinaryBody("upload-file", new FileInputStream(file), ContentType.APPLICATION_OCTET_STREAM, file.getName());
		builder.addTextBody("file", str);
		
		HttpEntity entity = builder.build();
		//
		post.setEntity(entity);
		post.addHeader("Authorization", "Bearer " + token);
		CloseableHttpClient client = HttpClients.createDefault();
		log.info("Doing post...");
		HttpResponse httpResponse = client.execute(post);
		log.info("Getting response...");
		String response = HttpRequestClient.getResponse(httpResponse.getEntity().getContent());
		log.info("Got response: \n" + response);
		log.info("Done.");
	}

}
