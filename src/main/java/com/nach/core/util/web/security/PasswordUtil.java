package com.nach.core.util.web.security;

import com.nach.core.util.web.security.aes.AES;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PasswordUtil {

	private String salt;
	
	private String key;
	
	public PasswordUtil(String salt, String key) {
		this.salt = salt;
		this.key = key;
	}
	
	public boolean authenticate(String given, String expected) {
		String derived = create(given);
		log.info("Expected: " + expected);
		log.info("Derived:  " + derived);
		boolean rtn = expected.equals(derived);
		return rtn;
	}

	public String create(String given) {
		AES aes = new AES(key);
		String rtn = given + "|" + salt;
		rtn = aes.encrypt(rtn);
		return rtn;
	}

}
