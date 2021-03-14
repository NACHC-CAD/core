package com.nach.core.util.props;

import java.io.File;
import java.io.InputStream;
import java.util.Properties;

import com.nach.core.util.file.FileUtil;

public class PropertiesUtil {

	/**
	 * Returns Properties loaded from file.
	 * 
	 * The fileName parameters is a file in the application path. For example
	 * "/com/mypackage/my-info.props".
	 */
	public static Properties getAsProperties(String fileName) {
		InputStream is =  new PropertiesUtil().getClass().getClassLoader().getResourceAsStream(fileName);
		return getAsProperties(is, fileName);
	}

	public static Properties getAsProperties(File file) {
		InputStream in = FileUtil.getInputStream(file);
		return getAsProperties(in, FileUtil.getCanonicalPath(file));
	}

	public static Properties getAsProperties(InputStream in, String fileName) {
		try {
			Properties props = new Properties();
			if (in == null) {
				throw new RuntimeException("Could not get InputStream for file: " + fileName);
			}
			props.load(in);
			return props;
		} catch (Exception exp) {
			throw new RuntimeException(exp);
		}
	}

}
