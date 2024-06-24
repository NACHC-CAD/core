package com.nach.core.util.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ZipUtil {

	public static File unzip(File zipFile, File destDir) {
		try {
			FileInputStream fis = new FileInputStream(zipFile);
			return unzip(fis, destDir);
		} catch (Exception exp) {
			throw new RuntimeException(exp);
		}
	}

	public static File unzip(InputStream is, File destDir) {
		try {
			byte[] buffer = new byte[1024];
			ZipInputStream zis = new ZipInputStream(is);
			ZipEntry zipEntry = zis.getNextEntry();
			File rtn = null;
			int cnt = 0;
			while (zipEntry != null) {
				cnt++;
				File newFile = newFile(destDir, zipEntry);
				if (cnt == 1) {
					rtn = newFile;
				}
				if (zipEntry.isDirectory()) {
					if (!newFile.isDirectory() && !newFile.mkdirs()) {
						throw new IOException("Failed to create directory " + newFile);
					}
				} else {
					// fix for Windows-created archives
					File parent = newFile.getParentFile();
					if (!parent.isDirectory() && !parent.mkdirs()) {
						throw new IOException("Failed to create directory " + parent);
					}
					// write file content
					FileOutputStream fos = new FileOutputStream(newFile);
					int len;
					while ((len = zis.read(buffer)) > 0) {
						fos.write(buffer, 0, len);
					}
					fos.close();
				}
				zipEntry = zis.getNextEntry();
			}
			zis.closeEntry();
			zis.close();
			return rtn;
		} catch (Exception exp) {
			throw new RuntimeException(exp);
		}
	}

	public static File newFile(File destinationDir, ZipEntry zipEntry) throws IOException {
		File destFile = new File(destinationDir, zipEntry.getName());
		String destDirPath = destinationDir.getCanonicalPath();
		String destFilePath = destFile.getCanonicalPath();
		if (!destFilePath.startsWith(destDirPath + File.separator)) {
			throw new IOException("Entry is outside of the target dir: " + zipEntry.getName());
		}
		return destFile;
	}

	public static void createZip(File directoryToZip, File zipDir, String zipFileName) {
		File file = new File(zipDir, zipFileName);
		if(file.exists()) {
			boolean success = file.delete();
			if(success == false) {
				throw new RuntimeException("Could not deletfile: " + FileUtil.getCanonicalPath(file));
			}
		}
		try {
			ZipCreator.createZip(directoryToZip, file);
		} catch(Exception exp) {
			throw new RuntimeException(exp);
		}
	}
	
}