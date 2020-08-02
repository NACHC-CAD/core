package com.nach.core.util.params;

import java.util.Properties;

import com.nach.core.util.props.PropertiesUtil;

public class ApplicationParams {

	private static final String PARAMS_FILE = "/app.properties";

	private static Properties props;

	static {
		try {
			props = PropertiesUtil.getAsProperties(PARAMS_FILE);
		} catch (Exception exp) {
			throw new RuntimeException(exp);
		}
	}

	public static String getProperty(String key) {
		String rtn = props.getProperty(key);
		if (rtn != null) {
			rtn = rtn.trim();
		}
		return rtn;
	}

}
