package com.nach.core.util.oauth.impl;

import java.util.Properties;

import com.nach.core.util.oauth.OauthTokenFactory;
import com.nach.core.util.props.PropertiesUtil;

public class OauthTokenProviderExample {

	private static Properties props = PropertiesUtil.getAsProperties("/path/to/my-file.properties");

	public static String getToken() {
		return OauthTokenFactory.getToken(props);
	}

}
