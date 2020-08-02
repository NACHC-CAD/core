package com.nach.core.util.props;

import java.io.InputStream;
import java.util.Properties;

import com.nach.core.util.file.FileUtil;

public class PropertiesUtil {

	/**
	 * Returns Properties loaded from file.  
	 * 
	 * The fileName parameters is a file in the application path.  For example "/com/mypackage/my-info.props".  
	 */
	public static Properties getAsProperties(String fileName) {
		try {
			Properties props = new Properties();
			InputStream in = FileUtil.getInputStream(fileName);
			if(in == null) {
				throw new RuntimeException("Could not get InputStream for file: " + fileName);
			}
			props.load(in);
			return props;
		} catch (Exception exp) {
			throw new RuntimeException(exp);
		}
	}

}
