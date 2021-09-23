package com.nach.core.util.file;

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.junit.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FileUtilFileNameMethodsIntegrationTest {
	private static final String FILE_NAME = "/integration/files/simple/test-text-file.txt";
	private static final String NAME = "test-text-file.txt";
	private static final String NamePrefix = "test-text-file";
	private static final String OriginalSuffix = "txt";
	private static final String ChangedSuffix = "csv";
	private static final String NameWithChangedSuffix = "test-text-file.csv";

	@Test
	public void shouldGetPrefixFromString() {

		//Arrange & Act
		String s = FileUtil.getPrefix(NAME);

		//Assert
		assertEquals(NamePrefix, s);
	}

	@Test
	public void shouldGetPrefixFromFile() {

		//Arrange
		File f = FileUtil.getFile(FILE_NAME);

		//Act
		String s = FileUtil.getPrefix(f);

		//Assert
		assertEquals(NamePrefix, s);
	}

	@Test
	public void shouldGetSuffixFromString() {

		//Arrange & Act
		String s = FileUtil.getSuffix(NAME);

		//Assert
		assertEquals(OriginalSuffix, s);
	}

	@Test
	public void shouldGetSuffixFromFile() {

		//Arrange
		File f = FileUtil.getFile(FILE_NAME);

		//Act
		String s = FileUtil.getSuffix(f);

		//Assert
		assertEquals(OriginalSuffix, s);
	}


	//Doesn't Change the suffix. Just Returns a string with new file name.
	@Test
	public void shouldChangeSuffix() {

		//Arrange
		File f = FileUtil.getFile(FILE_NAME);

		//Act
		String s = FileUtil.changeSuffix(f, ChangedSuffix);

		//Assert
		assertEquals(NameWithChangedSuffix, s);
	}
}
