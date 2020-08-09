package com.nach.core.util.excel;

import java.io.InputStream;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelUtil {

	public static Workbook getWorkbook(InputStream in) {
		try {
			Workbook book = new XSSFWorkbook(in);
			return book;
		} catch(Exception exp) {
			throw new RuntimeException(exp);
		}
	}
	
	public static Sheet getSheet(InputStream in, String name) {
		try {
			Workbook book = new XSSFWorkbook(in);
			Sheet sheet = book.getSheet(name);
			return sheet;
		} catch(Exception exp) {
			throw new RuntimeException(exp);
		}
	}
	
	public static Sheet getSheet(Workbook book, String name) {
		try {
			Sheet sheet = book.getSheet(name);
			return sheet;
		} catch(Exception exp) {
			throw new RuntimeException(exp);
		}
	}
	
}


