package com.nach.core.util.file;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class NotJarUtil {

	public static List<String> getResources(String path) {
		try {
			return new NotJarUtil().getResourceFiles(path);
		} catch(Exception exp) {
			throw new RuntimeException(exp);
		}
	}
	
	private List<String> getResourceFiles(String path) throws IOException {
		List<String> fileNames = new ArrayList<>();
		File dir = new File(path);
		if(dir == null || dir.exists() == false) {
			dir = FileUtil.getFile(path);
		}
		File[] files = dir.listFiles();
		for(File file : files) {
			String fileName = FileUtil.getCanonicalPath(file);
			fileNames.add(fileName);
		}
		return fileNames;
	}

	private InputStream getResourceAsStream(String resource) {
		final InputStream in = getContextClassLoader().getResourceAsStream(resource);

		return in == null ? getClass().getResourceAsStream(resource) : in;
	}

	private ClassLoader getContextClassLoader() {
		return Thread.currentThread().getContextClassLoader();
	}
	
}
