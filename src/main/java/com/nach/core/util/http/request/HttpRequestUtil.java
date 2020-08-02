package com.nach.core.util.http.request;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;

public class HttpRequestUtil {

	public static String getBody(HttpServletRequest request) {
		try {
			if(request == null || request.getReader() == null) {
				return "";
			} else {
				return IOUtils.toString(request.getReader());
			}
		} catch(Exception exp) {
			throw new RuntimeException(exp);
		}
	}
	
}
