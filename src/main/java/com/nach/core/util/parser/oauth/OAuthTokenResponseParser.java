package com.nach.core.util.parser.oauth;

import org.json.JSONObject;

public class OAuthTokenResponseParser {

	private String response;
	
	public OAuthTokenResponseParser(String response) {
		this.response = response;
	}
	
	public String getToken() {
		try {
			JSONObject responseObj = new JSONObject(response);
			String rtn = responseObj.getString("access_token");
			return rtn;
		} catch(Exception exp) {
			throw new RuntimeException(exp);
		}
	}

}
