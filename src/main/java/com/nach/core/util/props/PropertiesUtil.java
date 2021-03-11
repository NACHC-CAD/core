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
		return getAsProperties(FileUtil.getFile(fileName));
	}

	public static Properties getAsProperties(File file) {
		try {
			Properties props = new Properties();
			InputStream in = FileUtil.getInputStream(file);
			if (in == null) {
				throw new RuntimeException("Could not get InputStream for file: " + file.getCanonicalPath());
			}
			props.load(in);
			return props;
		} catch (Exception exp) {
			throw new RuntimeException(exp);
		}
	}

}
