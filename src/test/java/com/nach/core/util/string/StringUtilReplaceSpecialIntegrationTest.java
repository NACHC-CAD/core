package com.nach.core.util.string;

import org.junit.Test;

import com.nach.core.util.file.FileUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class StringUtilReplaceSpecialIntegrationTest {

	@Test
	public void shouldReplaceSpecial() {
		log.info("Starting test...");
		String str = FileUtil.getAsString("/string/specialcharacters/special-characters.txt");
		log.info("Got String:  " + str);
		String rep = StringUtil.removeSpecial(str);
		log.info("Got Replace: [" + rep + "]");
		log.info("Done.");
	}
	
}
