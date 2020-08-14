package com.nach.core.util.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Properties;

public class FileUtil {

	/**
	 * Returns contents of a file as a string for a given filePath.
	 * 
	 * The fileName parameters is a file in the application path. For example
	 * "/com/mypackage/my-info.props".
	 */
	public static String getAsString(String filePath) {
		InputStream is = FileUtil.class.getResourceAsStream(filePath);
		return getAsString(is);
	}

	/**
	 * Returns contents of a file as a string for a given InputStream.
	 * 
	 * The fileName parameters is a file in the application path. For example
	 * "/com/mypackage/my-info.props".
	 */
	public static String getAsString(InputStream is) {
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(is));
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();
			while (line != null) {
				sb.append(line);
				sb.append(System.lineSeparator());
				line = br.readLine();
			}
			String rtn = sb.toString();
			return rtn;
		} catch (Exception exp) {
			throw new RuntimeException(exp);
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (Exception exp) {
					throw new RuntimeException(exp);
				}
			}
		}
	}

	/**
	 * Returns contents of a file as a string for a given File.
	 */
	public static String getAsString(File file) {
		InputStream is = null;
		try {
			String rtn = getAsString(is);
			return rtn;
		} catch (Exception exp) {
			throw new RuntimeException(exp);
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (Exception exp) {
					throw new RuntimeException(exp);
				}
			}
		}
	}

	/**
	 * Returns an input stream for the file.
	 * 
	 * The fileName parameters is a file in the application path. For example
	 * "/com/mypackage/my-info.props".
	 */
	public static InputStream getInputStream(String fileName) {
		try {
			Properties props = new Properties();
			InputStream in = FileUtil.class.getResourceAsStream(fileName);
			return in;
		} catch (Exception exp) {
			throw new RuntimeException(exp);
		}
	}
	
	public static File getProjectRoot() {
		String filePath = "/";
		String rootDirName = FileUtil.class.getResource(filePath).getPath();
		File rtn = new File(rootDirName, "../../");
		return rtn;
	}

	public static File getFromProjectRoot(String fileName) {
		File root = getProjectRoot();
		File file = new File(root, fileName);
		return file;
	}
	
	public static File getFile(String name) {
		String filePath = "/";
		String rootDirName = FileUtil.class.getResource(filePath).getPath();
		File rtn = new File(rootDirName, "../../");
		rtn = new File(rtn, name);
		return rtn;
	}

	public static List<File> list(File dir) {
		Comparator<File> comp = new Comparator<File>() {
			@Override
			public int compare(File file1, File file2) {
				return file1.getName().compareTo(file2.getName());
			}
		};
		File[] fileArray = dir.listFiles();
		List<File> files = Arrays.asList(fileArray);
		Collections.sort(files, comp);
		return files;
	}
	
}
