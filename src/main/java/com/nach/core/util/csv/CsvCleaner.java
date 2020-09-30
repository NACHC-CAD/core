package com.nach.core.util.csv;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CsvCleaner {

	public static void clean(File in, File out, String delim, Character bogusDelim) {
		BufferedReader reader = null;
		BufferedWriter writer = null;
		CSVWriter csvWriter = null;
		CSVReader csvReader = null;
		try {
			reader = new BufferedReader(new FileReader(in));
			writer = new BufferedWriter(new FileWriter(out));
			csvWriter = new CSVWriter(writer);
			csvReader = new CSVReader(reader);
			String str;
			int colCnt = 0;
			int cnt = 0;
			int bogusCnt = 0;
			int lineCnt = 0;
			ArrayList<String> badLines = new ArrayList<String>();
			ArrayList<String> fixedLines = new ArrayList<String>();
			ArrayList<String> reallyBadLines = new ArrayList<String>();
			for (str = reader.readLine(); str != null; str = reader.readLine()) {
				// log.info(str);
				if (StringUtils.isBlank(str)) {
					continue;
				}
				cnt++;
				lineCnt++;
				String[] tokens = {};
				try {
					tokens = csvReader.getParser().parseLine(str);
				} catch (Exception exp) {
					tokens = fixlines(str, delim, bogusDelim);
					if (tokens.length != colCnt) {
						if(tokens.length == colCnt - 1) {
							tokens = getFixedTokens(tokens);
							fixedLines.add(str);
						} else {
							reallyBadLines.add(str);
							continue;
						}
					} else {
						badLines.add(str);
					}
				}
				if (cnt == 1) {
					colCnt = tokens.length;
				} else {
					if (tokens.length < colCnt && charCnt(str, bogusDelim) > tokens.length) {
						tokens = CsvUtil.parseLine(str, bogusDelim).toArray(new String[0]);
						bogusCnt++;
					}
				}
				csvWriter.writeNext(tokens);
				csvWriter.flush();
			}
			if (badLines.size() > 0) {
				String msg = "";
				for (String badLine : badLines) {
					msg += badLine + "\n";
				}
				log.info("\n\n* * * GOT SOME BOGUS LINES * * *\n" + msg);
				log.info("* * * END BAD LINES * * * \n\n");
			} else {
				log.info("NO BAD LINES !!!");
			}
			if (fixedLines.size() > 0) {
				String msg = "";
				for (String badLine : fixedLines) {
					msg += badLine + "\n";
				}
				log.info("\n\n* * * GOT SOME FIXED LINES * * *\n" + msg);
				log.info("* * * FIXED LINES * * * \n\n");
			} else {
				log.info("NO FIXED LINES !!!");
			}
			if (reallyBadLines.size() > 0) {
				String msg = "";
				for (String badLine : reallyBadLines) {
					msg += badLine + "\n";
				}
				log.info("\n\n* * * GOT SOME REALLY BAD LINES * * *\n" + msg);
				log.info("* * * END REALLY BAD LINES * * * \n\n");
			} else {
				log.info("NO REALLY BAD LINES !!!");
			}
			log.info("lines: " + lineCnt);
			log.info("bogus: " + bogusCnt);
			log.info("BAD LINES: " + badLines.size());
			log.info("FIXED LINES: " + fixedLines.size());
			log.info("REALLY BAD LINES: " + reallyBadLines.size());
		} catch (Exception exp) {
			throw new RuntimeException(exp);
		} finally {
			if (csvWriter != null) {
				try {
					reader.close();
					// writer.close();
					csvWriter.close();
					csvReader.close();
				} catch (IOException ioe) {
					throw new RuntimeException(ioe);
				}
			}
		}
	}

	private static String[] getFixedTokens(String[] tokens) {
		String[] fixedTokens = new String[tokens.length + 1];
		int cnt=0;
		for(String token : tokens) {
			fixedTokens[cnt] = token;
			cnt++;
		}
		fixedTokens[cnt] = "DUMMY";
		return fixedTokens;
	}

	private static String[] fixlines(String str, String delim, Character bogusDelim) {
		str = str.replace("\"", "");
		str = str.replace(delim, bogusDelim.toString());
		String[] rtn = CsvUtil.parseLine(str, bogusDelim).toArray(new String[0]);
		return rtn;
	}

	private static int charCnt(String str, Character ch) {
		return StringUtils.countMatches(str, ch);
	}

}
