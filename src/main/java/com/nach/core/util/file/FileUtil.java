package com.nach.core.util.file;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

import org.apache.commons.io.FileUtils;
import org.apache.tools.ant.DirectoryScanner;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FileUtil {

	// * * *
	//
	// FILE METHODS
	//
	// * * *

	//
	// get a file based on class path (e.g. /com/myorg/myproj/myfile.txt)
	//

	public static File getFile(String name) {
		return getFile(name, false);
	}

	public static File getFile(String name, boolean swapOutMvnTestClasses) {
		String filePath = "/";
		String rootDirName = FileUtil.class.getResource(filePath).getPath();
		if (swapOutMvnTestClasses == true) {
			rootDirName = rootDirName.replace("test-classes", "classes");
		}
		File rtn = new File(rootDirName, name);
		if (rtn.exists() == false) {
			rootDirName = rootDirName.replace("test-classes", "classes");
			File temp = new File(rootDirName, name);

			// Only move to classes if file exists there, otherwise, stay in test-classes
			if (temp.exists()) {
				rtn = temp;
			}
		}
		return rtn;
	}

	public static String getCanonicalPath(String filePath) {
		File file = getFile(filePath);
		try {
			return file.getCanonicalPath();
		} catch (Exception exp) {
			throw new RuntimeException(exp);
		}
	}

	//
	// get file from project root (based on mvn project)
	//

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

	//
	// get file contents as a string
	//

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
		return head(is, null);
	}

	/**
	 * Returns contents of a file as a string for a given File.
	 */
	public static String getAsString(File file) {
		InputStream is = null;
		try {
			is = new FileInputStream(file);
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

	//
	// get file as input stream
	//

	/**
	 * Returns an input stream for the file.
	 * 
	 * The fileName parameters is a file in the application path. For example
	 * "/com/mypackage/my-info.props".
	 */
	public static InputStream getInputStream(String fileName) {
		try {
			InputStream in = FileUtil.class.getResourceAsStream(fileName);
			return in;
		} catch (Exception exp) {
			throw new RuntimeException(exp);
		}
	}

	public static InputStream getInputStream(File file) {
		try {
			InputStream rtn = new FileInputStream(file);
			return rtn;
		} catch (Exception exp) {
			throw new RuntimeException(exp);
		}
	}

	public static InputStream getInputStreamFromProjectRoot(String fileName) {
		try {
			InputStream in = new FileInputStream(FileUtil.getFromProjectRoot(fileName));
			return in;
		} catch (Exception exp) {
			throw new RuntimeException(exp);
		}
	}

	/**
	 * 
	 * Method that returns the first n rows of a file as a string.
	 * 
	 */
	public static String head(File file, Integer n) {
		try {
			InputStream is = new FileInputStream(file);
			return head(is, n);
		} catch (Exception exp) {
			throw new RuntimeException(exp);
		}
	}

	public static String head(InputStream is, Integer n) {
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(is));
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();
			int cnt = 0;
			while (line != null) {
				cnt++;
				if (n != null && cnt > n) {
					break;
				}
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

	public static Long getLineCount(File file) {
		long lineCount;
		try (Stream<String> stream = Files.lines(file.toPath(), StandardCharsets.UTF_8)) {
			lineCount = stream.count();
		} catch (Exception exp) {
			throw new RuntimeException(exp);
		}
		return lineCount;
	}

	// * * *
	//
	// WRITE METHODS
	//
	// * * *

	public static void write(String string, File file) {
		try {
			InputStream initialStream = new ByteArrayInputStream(string.getBytes());
			FileUtils.copyInputStreamToFile(initialStream, file);
		} catch (Exception exp) {
			throw new RuntimeException(exp);
		}
	}

	public static void write(InputStream inputStream, File file) {
		try {
			FileUtils.copyInputStreamToFile(inputStream, file);
		} catch (Exception exp) {
			throw new RuntimeException(exp);
		}
	}

	// * * *
	//
	// FILE NAME METHODS
	//
	// * * *

	public static String getPrefix(String name) {
		int end = name.lastIndexOf(".");
		if (end < 0) {
			return name;
		} else {
			String rtn = name.substring(0, end);
			return rtn;
		}
	}

	public static String getPrefix(File file) {
		String name = file.getName();
		return getPrefix(name);
	}

	public static String getSuffix(File file) {
		String name = file.getName();
		return getSuffix(name);
	}

	public static String appendToFileName(File srcFile, String str) {
		String prefix = FileUtil.getPrefix(srcFile);
		String suffix = FileUtil.getSuffix(srcFile);
		String name = prefix + str + "." + suffix;
		return name;
	}

	public static File createFileWithAppendedName(File srcFile, String str) {
		File root = srcFile.getParentFile();
		String prefix = FileUtil.getPrefix(srcFile);
		String suffix = FileUtil.getSuffix(srcFile);
		String name = prefix + str + "." + suffix;
		File file = new File(root, name);
		return file;
	}

	public static String getSuffix(String name) {
		int start = name.lastIndexOf(".");
		if (start < 0) {
			return "";
		} else {
			String rtn = name.substring(start + 1, name.length());
			return rtn;
		}
	}

	// Doesn't Change the suffix. Just Returns a string with new file name.
	public static String changeSuffix(File file, String suffix) {
		String fileName = getPrefix(file);
		String rtn = fileName + "." + suffix;
		return rtn;
	}

	// * * *
	//
	// DIRECTORY METHODS
	//
	// * * *

	//
	// list files
	//

	public static List<File> list(File dir) {
		File[] fileArray = dir.listFiles();
		List<File> files = Arrays.asList(fileArray);
		ArrayList<File> rtn = new ArrayList<File>();
		files = sortByName(files);
		rtn.addAll(files);
		return rtn;
	}

	public static List<File> listDirs(File dir) {
		File[] fileArray = dir.listFiles();
		List<File> files = Arrays.asList(fileArray);
		ArrayList<File> rtn = new ArrayList<File>();
		files = sortByName(files);
		for (File file : files) {
			if (file.isDirectory()) {
				rtn.add(file);
			}
		}
		return rtn;
	}

	public static List<File> listFiles(String fileName, String pattern) {
		File file = new File(fileName);
		return listFiles(file, pattern);
	}

	public static List<File> listFiles(File file, String pattern) {
		List<File> rtn = new ArrayList<File>();
		DirectoryScanner scanner = new DirectoryScanner();
		scanner.setIncludes(new String[] { pattern });
		scanner.setBasedir(file);
		scanner.setCaseSensitive(false);
		scanner.scan();
		String[] files = scanner.getIncludedFiles();
		for (String str : files) {
			rtn.add(new File(file, str));
		}
		rtn = sortByName(rtn);
		return rtn;
	}

	public static List<File> listFiles(File file, String pattern, String excludesPattern) {
		List<File> rtn = new ArrayList<File>();
		DirectoryScanner scanner = new DirectoryScanner();
		scanner.setIncludes(new String[] { pattern });
		scanner.setExcludes(new String[] { excludesPattern });
		scanner.setBasedir(file);
		scanner.setCaseSensitive(false);
		scanner.scan();
		String[] files = scanner.getIncludedFiles();
		for (String str : files) {
			rtn.add(new File(file, str));
		}
		rtn = sortByName(rtn);
		return rtn;
	}

	public static List<File> listFiles(File file, String[] includes) {
		List<File> rtn = new ArrayList<File>();
		DirectoryScanner scanner = new DirectoryScanner();
		scanner.setIncludes(includes);
		scanner.setBasedir(file);
		scanner.setCaseSensitive(false);
		scanner.scan();
		String[] files = scanner.getIncludedFiles();
		for (String str : files) {
			rtn.add(new File(file, str));
		}
		rtn = sortByName(rtn);
		return rtn;
	}

	public static List<File> listFiles(File file) {
		File[] files = file.listFiles();
		List<File> rtn = Arrays.asList(files);
		rtn = sortByName(rtn);
		return rtn;
	}

	public static List<File> listFilesOnly(File file) {
		File[] files = file.listFiles();
		List<File> list = Arrays.asList(files);
		list = sortByName(list);
		ArrayList<File> rtn = new ArrayList<File>();
		for (File current : files) {
			if (file.isDirectory() == false) {
				rtn.add(current);
			}
		}
		return rtn;
	}

	public static List<File> removeStartsWith(List<File> files, String startsWith) {
		ArrayList<File> rtn = new ArrayList<File>();
		for (File file : files) {
			if (file.getName().startsWith(startsWith) == false) {
				rtn.add(file);
			}
		}
		return rtn;
	}

	//
	// sort a list of files by name
	//

	public static List<File> sortByName(List<File> files) {
		Comparator<File> comp = new Comparator<File>() {
			@Override
			public int compare(File file1, File file2) {
				return file1.getName().compareTo(file2.getName());
			}
		};
		Collections.sort(files, comp);
		return files;
	}

	//
	// mkdirs
	//

	public static File mkdirs(File dir, String name) {
		File file = new File(dir, name);
		file.mkdirs();
		return file;
	}

	public static File mkdirs(File dir) {
		dir.mkdirs();
		return dir;
	}

	//
	// delete dirs
	//

	public static void rmdir(File dir) {
		try {
			if (dir.exists()) {
				log.debug("Deleting dir: " + getCanonicalPath(dir));
				FileUtils.forceDelete(dir);
			}
		} catch (Exception exp) {
			throw new RuntimeException(exp);
		}
	}

	public static File clearContents(File dir, String name) {
		File file = new File(dir, name);
		clearContents(file);
		return file;
	}

	public static void clearContents(File dir) {
		log.debug("Clearing contents");
		if (dir.exists()) {
			rmdir(dir);
		}
		if (dir.exists() == true) {
			throw new RuntimeException("Dir not deleted: " + dir);
		}
		log.debug("Creating dir: " + getCanonicalPath(dir));
		dir.mkdir();
		if (dir.exists() == false) {
			throw new RuntimeException("Could not create directory: " + dir);
		}
	}

	//
	// get the size of a file (in BYTES)
	//

	public static long size(File file) {
		return file.length();
	}

	public static long getSize(File file) {
		return size(file);
	}

	//
	// get the path of a file (wrap checked exception)
	//

	public static String getCanonicalPath(File file) {
		try {
			if (file == null) {
				return null;
			} else {
				return file.getCanonicalPath();
			}
		} catch (Exception exp) {
			throw new RuntimeException(exp);
		}

	}

	//
	// create a file (wrap checked exception)
	//

	public static void createNewFile(File file) {
		try {
			file.createNewFile();
		} catch (Exception exp) {
			throw new RuntimeException(exp);
		}
	}

	//
	// close (wrap checked exception)
	//

	public static void close(OutputStream out) {
		try {
			out.close();
		} catch (Exception exp) {
			throw new RuntimeException(exp);
		}
	}

}
