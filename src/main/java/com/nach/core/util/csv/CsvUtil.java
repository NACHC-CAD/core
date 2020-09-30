package com.nach.core.util.csv;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

public class CsvUtil {

	private static final char DEFAULT_SEPARATOR = ',';

	private static final char DEFAULT_QUOTE = '"';

	public static List<String> parseLine(String cvsLine) {
		return parseLine(cvsLine, DEFAULT_SEPARATOR, DEFAULT_QUOTE);
	}

	public static List<String> parseLine(String cvsLine, char separators) {
		return parseLine(cvsLine, separators, DEFAULT_QUOTE);
	}

	public static List<String> parseLine(String csvLine, char separators, char customQuote) {
		List<String> result = new ArrayList<>();
		if (StringUtils.isEmpty(csvLine)) {
			return result;
		}
		if (customQuote == ' ') {
			customQuote = DEFAULT_QUOTE;
		}
		if (separators == ' ') {
			separators = DEFAULT_SEPARATOR;
		}
		StringBuffer curVal = new StringBuffer();
		boolean inQuotes = false;
		boolean startCollectChar = false;
		boolean doubleQuotesInColumn = false;
		char[] chars = csvLine.toCharArray();
		for (char ch : chars) {
			if (inQuotes) {
				startCollectChar = true;
				if (ch == customQuote) {
					inQuotes = false;
					doubleQuotesInColumn = false;
				} else {
					if (ch == '\"') {
						if (!doubleQuotesInColumn) {
							curVal.append(ch);
							doubleQuotesInColumn = true;
						}
					} else {
						curVal.append(ch);
					}

				}
			} else {
				if (ch == customQuote) {
					inQuotes = true;
					if (chars[0] != '"' && customQuote == '\"') {
						curVal.append('"');
					}
					if (startCollectChar) {
						curVal.append('"');
					}

				} else if (ch == separators) {
					result.add(curVal.toString());
					curVal = new StringBuffer();
					startCollectChar = false;
				} else if (ch == '\r') {
					continue;
				} else if (ch == '\n') {
					break;
				} else {
					curVal.append(ch);
				}
			}

		}
		result.add(curVal.toString());
		return result;
	}

}
