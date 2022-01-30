package com.nach.core.util.oauth;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nach.core.util.http.HttpRequestClient;
import com.nach.core.util.json.JsonUtil;
import com.nach.core.util.params.ApplicationParams;
import com.nach.core.util.parser.oauth.OAuthTokenResponseParser;

public class OauthTokenFactory {

	private static final Logger logger = LoggerFactory.getLogger(OauthTokenFactory.class);

	public static String getToken(Properties props) {
		String url = props.getProperty("url");
		String uid = props.getProperty("uid");
		String pwd = props.getProperty("pwd");
		String rtn = OauthTokenFactory.getToken(url, uid, pwd);
		return rtn;
	}

	public static String getToken(String url, String uid, String secret, String[][] headers, String msg) {
		return getToken(url, uid, secret, headers, msg, false);
	}

	
	public static String getToken(String url, String uid, String secret, String[][] headers, String msg, boolean debug) {
		try {
			logger.debug("Getting token");
			logger.debug("Java version: " + System.getProperty("java.version"));
			if (debug == true) {
				System.setProperty("javax.net.debug", "ssl");
			}
			HttpRequestClient client = new HttpRequestClient(url);
			client.addBasicAuthentication(uid, secret);
			for(String[] header : headers) {
				client.addHeader(header[0], header[1]);
			}
			client.doPost(msg);
			logger.debug("Doing post");
			logger.info("Getting token from: " + client.getUrl());
			client.doPost(msg);
			String response = client.getResponse();
			response = JsonUtil.removeWhiteSpaces(response);
			logger.info("Got response: \n" + response);
			String rtn = new OAuthTokenResponseParser(response).getToken();
			logger.debug("Done.");
			return rtn;
		} catch (Exception exp) {
			throw (new RuntimeException(exp));
		} finally {
			logger.debug("Java version: " + System.getProperty("java.version"));
			logger.debug("Done.");
		}
	}

	public static String getToken(String url, String uid, String secret) {
		return getToken(url, uid, secret, false);
	}

	public static String getToken(String url, String uid, String secret, boolean debug) {
		try {
			logger.debug("Getting token");
			logger.debug("Java version: " + System.getProperty("java.version"));
			if (debug == true) {
				System.setProperty("javax.net.debug", "ssl");
			}
			HttpRequestClient client = new HttpRequestClient(url);
			client.addFormData("grant_type", "client_credentials");
			client.addFormData("client_id", uid);
			client.addFormData("client_secret", secret);
			logger.debug("Doing post");
			logger.info("Getting token from: " + client.getUrl());
			client.doPostForm();
			String response = client.getResponse();
			response = JsonUtil.removeWhiteSpaces(response);
			logger.info("Got response: \n" + response);
			String rtn = new OAuthTokenResponseParser(response).getToken();
			logger.debug("Done.");
			return rtn;
		} catch (Exception exp) {
			throw (new RuntimeException(exp));
		} finally {
			logger.debug("Java version: " + System.getProperty("java.version"));
			logger.debug("Done.");
		}
	}

}
