package com.nach.core.util.file;

import java.net.URL;
import java.security.CodeSource;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JarUtil {

	public static List<String> getFiles(String path, Class cls) {
		ArrayList<String> rtn = new ArrayList<String>();
		try {
			CodeSource src = cls.getProtectionDomain().getCodeSource();
			if (src != null) {
				URL jar = src.getLocation();
				ZipInputStream zip = new ZipInputStream(jar.openStream());
				while (true) {
					ZipEntry entry = zip.getNextEntry();
					if (entry == null) {
						break;
					}
					String name = "/" + entry.getName();
					log.info("NAME: " + name);
					if (name.startsWith(path) && name.equals(path) == false) {
						rtn.add(name);
					}
				}
			}
			return rtn;
		} catch (Exception exp) {
			throw new RuntimeException(exp);
		}
	}

}
