package com.nach.core.util.csv;

import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

public class CsvToSimpleHtmlTable {

	public static String exec(InputStream csv) {
		try {
			String rtn = "";
			rtn += "<table>\n";
			CSVParser parser = CsvUtilApache.getParser(csv);
			List<CSVRecord> rows = parser.getRecords();
			for (CSVRecord row : rows) {
				rtn += "  <tr>\n";
				Iterator<String> colIter = row.iterator();
				while(colIter.hasNext()) {
					String val = colIter.next();
					rtn += "    <td>" + val + "</td>\n";
				}
				rtn += "  </tr>\n"; 
			}
			rtn += "</table>\n";
			return rtn;
		} catch (Exception exp) {
			throw new RuntimeException(exp);
		}
	}

}
