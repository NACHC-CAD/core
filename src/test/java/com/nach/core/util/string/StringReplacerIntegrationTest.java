package com.nach.core.util.string;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class StringReplacerIntegrationTest {

	@Test
	public void shouldReplaceString() {
		String str = "<STR>ing: <DELETE_ME><DELETE_ME><DELETE_ME>this is a <STR>.";
		StringReplacer rep = new StringReplacer();
		rep.add("<STR>",  "Test");
		rep.add("<DELETE_ME>", "");
		String newString = rep.replaceAll(str);
		log.info("Original: " + str);
		log.info("Replaced: " + newString);
		assertTrue("Testing: this is a Test.".contentEquals(newString));
		log.info("Done.");
	}
	
}
