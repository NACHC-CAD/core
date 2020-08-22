package com.nach.core.util.excel;

import java.io.InputStream;
import java.util.Date;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.nach.core.util.excel.enumeration.ExcelCellType;
import com.nach.core.util.time.TimeUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ExcelUtil {

	public static Workbook getWorkbook(InputStream in) {
		try {
			log.debug("Getting workbook (this might take a while)...");
			Workbook book = new XSSFWorkbook(in);
			log.debug("Got workbook");
			return book;
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
	
	public static Sheet getSheet(Workbook book, int index) {
		try {
			Sheet sheet = book.getSheetAt(index);
			return sheet;
		} catch(Exception exp) {
			throw new RuntimeException(exp);
		}
	}
	
	public static Iterator<Row> getRows(Sheet sheet) {
		return sheet.iterator();
	}
	
	public static Iterator<Cell> getCells(Row row) {
		return row.cellIterator();
	}
	
	public static String getStringValue(Cell cell) {
		if(cell == null) {
			return null;
		}
		int cellType = cell.getCellType();
		if(Cell.CELL_TYPE_NUMERIC == cellType && DateUtil.isCellDateFormatted(cell)) {
			Date date =  cell.getDateCellValue();
			String rtn = TimeUtil.getDateAsYyyyMmDd(date);
			return rtn;
		} else if(Cell.CELL_TYPE_NUMERIC == cellType) {
			return cell.getNumericCellValue() + "";
		} else {
			return cell.getStringCellValue();
		}
	}
	
	public static ExcelCellType getCellType(Cell cell) {
		int cellType = cell.getCellType();
		if(Cell.CELL_TYPE_NUMERIC == cellType && DateUtil.isCellDateFormatted(cell)) {
			return ExcelCellType.DATE_TIME;
		} else if(Cell.CELL_TYPE_NUMERIC == cellType) {
			return ExcelCellType.NUMBER;
		} else {
			return ExcelCellType.STRING;
		}
	}
	
}


