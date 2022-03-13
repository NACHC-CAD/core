package com.nach.core.util.file;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
		List<String> filenames = new ArrayList<>();

		try (
				InputStream in = getResourceAsStream(path);
				BufferedReader br = new BufferedReader(new InputStreamReader(in))) {
			String resource;

			while ((resource = br.readLine()) != null) {
				filenames.add(resource);
			}
		}
		filenames.remove(path);
		return filenames;
	}

	private InputStream getResourceAsStream(String resource) {
		final InputStream in = getContextClassLoader().getResourceAsStream(resource);

		return in == null ? getClass().getResourceAsStream(resource) : in;
	}

	private ClassLoader getContextClassLoader() {
		return Thread.currentThread().getContextClassLoader();
	}
	
}
