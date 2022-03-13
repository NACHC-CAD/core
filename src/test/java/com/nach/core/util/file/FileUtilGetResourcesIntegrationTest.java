package com.nach.core.util.file;

import java.util.List;

import org.junit.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FileUtilGetResourcesIntegrationTest {

	@Test
	public void shouldGetFileListing() {
		log.info("Starting test...");
		List<String> paths = FileUtil.listResources("/misc/file-util-test-files/two-text-files", null);
		log.info("Got " + paths.size() + " paths");
		for(String path : paths) {
			log.info("\t" + path);
		}
		log.info("Done.");
	}
	
}
