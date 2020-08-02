package com.nach.core.util.http;

import java.security.SecureRandom;

import javax.net.ssl.SSLContext;
import javax.net.ssl.X509TrustManager;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.impl.client.HttpClients;

public class HttpClientFactory {

	public HttpClient createClient() {
		try {
			SSLContext sslcontext = SSLContexts.custom().useSSL().build();
			sslcontext.init(null, getTrustManager(), new SecureRandom());
			SSLConnectionSocketFactory factory = new SSLConnectionSocketFactory(sslcontext, SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
			HttpClient client = HttpClients.custom().setSSLSocketFactory(factory).build();
			return client;
		} catch (Exception exp) {
			throw new RuntimeException(exp);
		}
	}

	private X509TrustManager[] getTrustManager() {
		return new X509TrustManager[] { new HttpsTrustManager() };
	}
	
}
