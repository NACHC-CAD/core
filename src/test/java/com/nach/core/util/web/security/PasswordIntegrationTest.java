package com.nach.core.util.web.security;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PasswordIntegrationTest {

	@Test
	public void shouldValidatePassword() {
		String given = "correct";
		String salt = "salt";
		String key = "key";
		PasswordUtil util = new PasswordUtil(salt, key);
		String expected = util.create(given);
		log.info("Expected: " + expected);
		boolean auth;
		auth = util.authenticate(given, expected);
		assertTrue(auth == true);
		auth = util.authenticate("wrong", expected);
		assertTrue(auth == false);
		log.info("Done.");
	}
	
}
