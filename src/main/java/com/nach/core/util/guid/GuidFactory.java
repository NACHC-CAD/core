package com.nach.core.util.guid;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GuidFactory {

	private static Logger logger = LoggerFactory.getLogger(GuidFactory.class);
	
	static {
		logger.debug("init uuid...");
		UUID uuid = UUID.randomUUID();
		logger.debug("Done with uuid init.");
	}
	
	public static String getGuid() {
		UUID uuid = UUID.randomUUID();
		String rtn = uuid.toString();
		return rtn;
	}
	
	public static void main(String[] args) {
		System.out.println(getGuid());
	}
	
}
