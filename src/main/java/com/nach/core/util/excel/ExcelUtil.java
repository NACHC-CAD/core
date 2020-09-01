package com.nach.core.util.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.Writer;
import java.nio.file.Files;
import java.util.Date;
import java.util.Iterator;

import javax.swing.text.html.HTML;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.tools.ant.taskdefs.Javadoc.Html;

import com.nach.core.util.excel.enumeration.ExcelCellType;
import com.nach.core.util.time.TimeUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ExcelUtil {

	public static Workbook createNewWorkbook() {
		XSSFWorkbook book = new XSSFWorkbook();
		return book;
	}

	public static void saveAsCsv(Sheet sheet, File target) {
		CSVPrinter csvPrinter = null;
		try {
			// create the csvPrinter
			if(target.exists() == true) {
				target.delete();
			}
			log.debug("Creating file at: " + target.getCanonicalPath());
			target.getParentFile().mkdirs();
			target.createNewFile();
			Writer writer = Files.newBufferedWriter(target.toPath());
			csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT);
			// iterate over each row
			Iterator<Row> rowIterator = sheet.rowIterator();
			while (rowIterator.hasNext()) {
				Row row = rowIterator.next();
				// iterate over each cell
				Iterator<Cell> cellIterator = row.cellIterator();
				while (cellIterator.hasNext()) {
					Cell cell = cellIterator.next();
					csvPrinter.print(cell.getStringCellValue());
				}
				csvPrinter.println();
			}
			csvPrinter.flush();
		} catch (Exception exp) {
			throw new RuntimeException(exp);
		} finally {
			if(csvPrinter != null) {
				try {
					csvPrinter.close();
				} catch(Exception exp) {
					throw new RuntimeException (exp);
				}
			}
		}
	}

	public static Workbook getWorkbook(File file) {
		try {
			FileInputStream in = new FileInputStream(file);
			return getWorkbook(in);
		} catch (Exception exp) {
			throw new RuntimeException(exp);
		}
	}

	public static Workbook getWorkbook(InputStream in) {
		try {
			log.debug("Getting workbook (this might take a while)...");
			Workbook book = new XSSFWorkbook(in);
			log.debug("Got workbook");
			return book;
		} catch (Exception exp) {
			throw new RuntimeException(exp);
		}
	}

	public static Sheet getSheet(Workbook book, String name) {
		try {
			Sheet sheet = book.getSheet(name);
			return sheet;
		} catch (Exception exp) {
			throw new RuntimeException(exp);
		}
	}

	public static Sheet getSheet(Workbook book, int index) {
		try {
			Sheet sheet = book.getSheetAt(index);
			return sheet;
		} catch (Exception exp) {
			throw new RuntimeException(exp);
		}
	}

	public static Iterator<Row> getRows(Sheet sheet) {
		return sheet.iterator();
	}

	public static Iterator<Cell> getCells(Row row) {
		return row.cellIterator();
	}

	public static String getStringValue(Sheet sheet, int r, int c) {
		Row row = sheet.getRow(r);
		if(row == null) {
			return null;
		}
		Cell cell = row.getCell(c);
		if(cell == null) {
			return null;
		}
		return getStringValue(cell);
	}
	
	public static String getStringValue(Cell cell) {
		if (cell == null) {
			return null;
		}
		int cellType = cell.getCellType();
		if (Cell.CELL_TYPE_NUMERIC == cellType && DateUtil.isCellDateFormatted(cell)) {
			Date date = cell.getDateCellValue();
			String rtn = TimeUtil.getDateAsYyyyMmDd(date);
			return rtn;
		} else if (Cell.CELL_TYPE_NUMERIC == cellType) {
			return cell.getNumericCellValue() + "";
		} else {
			String rtn = cell.getStringCellValue();
			rtn = StringEscapeUtils.escapeHtml4(rtn);
			rtn = rtn.replace("\n", "&NewLine;");
			return rtn;
		}
	}

	public static void setStringValue(Sheet sheet, String val, int r, int c) {
		Row row = sheet.getRow(r);
		Cell cell = row.getCell(c);
		cell.setCellValue(val);
	}
	
	public static ExcelCellType getCellType(Cell cell) {
		int cellType = cell.getCellType();
		if (Cell.CELL_TYPE_NUMERIC == cellType && DateUtil.isCellDateFormatted(cell)) {
			return ExcelCellType.DATE_TIME;
		} else if (Cell.CELL_TYPE_NUMERIC == cellType) {
			return ExcelCellType.NUMBER;
		} else {
			return ExcelCellType.STRING;
		}
	}

	public static void addCol(Row row, String val, int cnt) {
		Cell cell = row.createCell(cnt);
		cell.setCellValue(val);
	}

	public static Row createNextRow(Sheet sheet) {
		return sheet.createRow(sheet.getLastRowNum() + 1);
	}
	
}
