package com.nach.core.util.excel.example;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import com.monitorjbl.xlsx.StreamingReader;
import com.nach.core.util.file.FileUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class StreamingExample {

	private static final String DIR = "/com/nach/core/testfiles/excel";

	private static final String FILE_NAME = DIR + "/test-file.xlsx";

	public static void main(String[] args) {
		File file = FileUtil.getFile(FILE_NAME);
		StreamingReader reader = getReader(file, 0);
		for(Row row : reader) {
			String rowString = "";
			for(Cell cell : row) {
				if(rowString != "") {
					rowString += ",";
				}
				rowString += cell.getStringCellValue();
			}
			log.info(rowString);
		}
		log.info("Done.");
	}
	
	public static StreamingReader getReader(File file, int sheetIndex) {
		try {
			InputStream in = new FileInputStream(file);
			StreamingReader reader = StreamingReader.builder()
			        .rowCacheSize(100)      // number of rows to keep in memory (defaults to 10)
			        .bufferSize(4096)       // buffer size to use when reading InputStream to file (defaults to 1024)
			        .sheetIndex(sheetIndex) // index of sheet to use
			        .read(in);              // read the file
			return reader;
		} catch(Exception exp) {
			throw new RuntimeException(exp);
		}
	}
	
}
