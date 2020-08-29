package com.nach.core.util.string;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class StringUtilIntegrationTest {

	@Test
	public void shouldGetSuffix() {
		log.info("Starting test...");
		String str = "/FileStore/tables/test/integration-test/basic";
		String suffix = StringUtil.getSuffix(str, "/");
		log.info("String: " + str);
		log.info("Got suffix: " + suffix);
		assertTrue(suffix.equals("basic"));
		str += "/";
		suffix = StringUtil.getSuffix(str, "/");
		log.info("String: " + str);
		log.info("Got suffix: " + suffix);
		assertTrue(suffix.equals("basic"));
		str += "  ";
		suffix = StringUtil.getSuffix(str, "/");
		log.info("String: " + str);
		log.info("Got suffix: " + suffix);
		assertTrue(suffix.equals("basic"));
		log.info("Done.");
	}
	
}
