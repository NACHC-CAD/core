package com.nach.core.util.excel;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.yaorma.util.time.TimeUtil;

import com.monitorjbl.xlsx.StreamingReader;
import com.nach.core.util.excel.enumeration.ExcelCellType;
import com.nach.core.util.file.FileUtil;
import com.nach.core.util.string.escape.Escape;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ExcelUtil {

	//
	// workbook methods
	//

	/**
	 * 
	 * Create a new workbook in memory (no file is created).
	 * 
	 */
	public static Workbook createNewWorkbook() {
		try {
			SXSSFWorkbook book = new SXSSFWorkbook();
			return book;
		} catch (Exception exp) {
			throw new RuntimeException(exp);
		}
	}

	/**
	 * 
	 * Create a workbook object in memeory for the given file.
	 * 
	 */
	public static Workbook getWorkbook(File file) {
		try {
			Workbook book = WorkbookFactory.create(file);
			return book;
			// FileInputStream in = new FileInputStream(file);
			// return getWorkbook(in);
		} catch (Exception exp) {
			throw new RuntimeException(exp);
		}
	}

	public static StreamingReader getReader(File file) {
		return getReader(file, 0);
	}

	public static StreamingReader getReader(File file, int sheetIndex) {
		try {
			InputStream in = new FileInputStream(file);
			StreamingReader reader = StreamingReader.builder()
					.rowCacheSize(100) // number of rows to keep in memory (defaults to 10)
					.bufferSize(4096) // buffer size to use when reading InputStream to file (defaults to 1024)
					.sheetIndex(sheetIndex) // index of sheet to use
					.read(in); // read the file
			return reader;
		} catch (Exception exp) {
			throw new RuntimeException(exp);
		}
	}

	/**
	 * 
	 * Create a workbook object in memeory for the given input stream.
	 * 
	 */
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

	public static Workbook getWorkbook(String string) {
		InputStream in = new ByteArrayInputStream(string.getBytes());
		return getWorkbook(in);
	}

	//
	// spreadsheet methods
	//

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

	public static List<Sheet> getSheets(Workbook book) {
		List<Sheet> rtn = new ArrayList<Sheet>();
		for (int i = 0; i < book.getNumberOfSheets(); i++) {
			Sheet sheet = book.getSheetAt(i);
			rtn.add(sheet);
		}
		return rtn;
	}

	//
	// row and col methods
	//

	public static Iterator<Row> getRows(Sheet sheet) {
		return sheet.iterator();
	}

	public static Iterator<Cell> getCells(Row row) {
		return row.cellIterator();
	}

	public static Cell getCell(Sheet sheet, int row, int col) {
		if(sheet.getRow(row) == null) {
			return null;
		}
		return sheet.getRow(row).getCell(col);
	}

	//
	// cell type method
	//

	/**
	 * 
	 * Method to get the type of a cell.
	 * 
	 */
	public static ExcelCellType getCellType(Cell cell) {
		CellType cellType = cell.getCellType();
		if (CellType.NUMERIC == cellType && DateUtil.isCellDateFormatted(cell)) {
			return ExcelCellType.DATE_TIME;
		} else if (CellType.NUMERIC == cellType) {
			return ExcelCellType.NUMBER;
		} else {
			return ExcelCellType.STRING;
		}
	}

	//
	// methods to get the value of a cell
	//

	public static Integer getIntValue(Sheet sheet, int row, int col) {
		Cell cell = getCell(sheet, row, col);
		if(cell == null) {
			return null;
		}
		return getIntValue(cell);
	}

	public static Double getDoubleValue(Sheet sheet, int row, int col) {
		Cell cell = getCell(sheet, row, col);
		if(cell == null) {
			return null;
		}
		return getDoubleValue(cell);
	}

	public static Double getDoubleValue(Cell cell) {
		try {
			String val = getStringValue(cell);
			if(StringUtils.isAllBlank(val)) {
				return null;
			}
			double dub = Double.parseDouble(val);
			return dub;
		} catch (Exception exp) {
			return null;
		}
	}

	public static Integer getIntValue(Cell cell) {
		try {
			String val = getStringValue(cell);
			if(StringUtils.isAllBlank(val)) {
				return null;
			}
			double dub = Double.parseDouble(val);
			int rtn = (int) dub;
			return rtn;
		} catch (Exception exp) {
			return null;
		}
	}

	public static String getStringValue(Cell cell) {
		return getStringValue(cell, null);
	}

	public static String getStringValue(Cell cell, Escape escape) {
		if (cell == null) {
			return null;
		}
		CellType cellType = cell.getCellType();
		if (CellType.NUMERIC == cellType && DateUtil.isCellDateFormatted(cell)) {
			Date date = cell.getDateCellValue();
			String rtn = TimeUtil.getDateAsYyyyMmDd(date);
			return rtn;
		} else if (CellType.NUMERIC == cellType) {
			return cell.getNumericCellValue() + "";
		} else if (CellType.FORMULA == cellType) {
			if (CellType.NUMERIC == cell.getCachedFormulaResultType()) {
				return cell.getNumericCellValue() + "";
			} else if (CellType.ERROR == cell.getCachedFormulaResultType()) {
				return null;
			} else {
				return cell.getStringCellValue();
			}
		} else if (CellType.ERROR == cellType) {
			return null;
		} else {
			String rtn = cell.getStringCellValue();
			if (escape != null) {
				rtn = escape.escape(rtn);
			}
			return rtn;
		}
	}

	/**
	 * 
	 * Get the value of a cell for the given sheet, row, and column.
	 * 
	 */
	public static String getStringValue(Sheet sheet, int r, int c) {
		Row row = sheet.getRow(r);
		if (row == null) {
			return null;
		}
		Cell cell = row.getCell(c);
		if (cell == null) {
			return null;
		}
		return getStringValue(cell);
	}

	/**
	 * 
	 * Get the value of a cell for the given row, and column.
	 * 
	 */
	public static String getStringValue(Row row, int c) {
		if (row == null) {
			return null;
		}
		Cell cell = row.getCell(c);
		if (cell == null) {
			return null;
		}
		return getStringValue(cell);
	}

	//
	// methods to set the value of a cell
	//

	/**
	 * 
	 * Set the value of a cell for the given sheet, row, and column.
	 * 
	 */
	public static void setStringValue(Sheet sheet, String val, int r, int c) {
		Row row = sheet.getRow(r);
		if (row == null) {
			row = sheet.createRow(r);
		}
		Cell cell = row.getCell(c);
		if (cell == null) {
			cell = row.createCell(c);
		}
		cell.setCellValue(val);
	}

	public static void addCol(Row row, String val) {
		int col = row.getLastCellNum();
		if (col < 0) {
			col = 0;
		}
		Cell cell = row.createCell(col);
		cell.setCellValue(val);
	}

	public static void addCol(Row row, Integer intVal) {
		int col = row.getLastCellNum();
		if (col < 0) {
			col = 0;
		}
		Cell cell = row.createCell(col);
		if(intVal == null) {
			cell.setCellValue("");
		} else {
			cell.setCellValue(intVal);
		}
	}

	public static void addCol(Row row, Double dubVal) {
		int col = row.getLastCellNum();
		if (col < 0) {
			col = 0;
		}
		Cell cell = row.createCell(col);
		if(dubVal == null) {
			cell.setCellValue("");
		} else {
			cell.setCellValue(dubVal);
		}
	}

	//
	// methods to add col/row
	//

	public static void addCol(Row row, String val, int cnt) {
		Cell cell = row.createCell(cnt);
		cell.setCellValue(val);
	}

	public static Row addRow(Sheet sheet) {
		return createNextRow(sheet);
	}

	public static Row createNextRow(Sheet sheet) {
		return sheet.createRow(sheet.getLastRowNum() + 1);
	}

	//
	// persistence methods
	//

	/**
	 * 
	 * Write a spreadsheet to a csv file. If the file exists it will be over
	 * written. If the dir does not exist it will be created.
	 * 
	 */
	public static void saveAsCsv(Sheet sheet, File target) {
		CSVPrinter csvPrinter = null;
		try {
			// create the csvPrinter
			if (target.exists() == true) {
				target.delete();
			}
			// create the dir if it doesn't exist
			if (target.getParentFile().exists() == false) {
				target.getParentFile().mkdirs();
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
			if (csvPrinter != null) {
				try {
					csvPrinter.close();
				} catch (Exception exp) {
					throw new RuntimeException(exp);
				}
			}
		}
	}

	public static void writeCsv(File file, Sheet sheet) {
		try {
			FileWriter writer = new FileWriter(file);
			writeCsv(writer, sheet);
		} catch (Exception exp) {
			throw new RuntimeException(exp);
		}
	}

	public static void writeCsv(Writer writer, Sheet sheet) {
		CSVPrinter csvPrinter = null;
		try {
			csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT);
			// iterate over each row
			Iterator<Row> rowIterator = sheet.rowIterator();
			while (rowIterator.hasNext()) {
				Row row = rowIterator.next();
				// iterate over each cell
				Iterator<Cell> cellIterator = row.cellIterator();
				int nextCell = 0;
				while (cellIterator.hasNext()) {
					Cell cell = cellIterator.next();
					int cellNum = cell.getColumnIndex();
					while (cellNum > nextCell) {
						csvPrinter.print("");
						nextCell++;
					}
					nextCell = cellNum + 1;
					String val = ExcelUtil.getStringValue(cell);
					csvPrinter.print(val);
				}
				csvPrinter.println();
			}
			csvPrinter.flush();
		} catch (Exception exp) {
			throw new RuntimeException(exp);
		} finally {
			if (csvPrinter != null) {
				try {
					csvPrinter.close();
				} catch (Exception exp) {
					throw new RuntimeException(exp);
				}
			}
		}
	}

	public static void save(Workbook book, File file) {
		OutputStream out = null;
		try {
			log.info(file.getCanonicalPath());
			log.info("Checking for existing file");
			if (file.exists() == true) {
				log.info("Deleting existing file: " + file.getCanonicalPath());
				file.delete();
			}
			log.info("Checking for parent dir");
			if (file.getParentFile().exists() == false) {
				log.info("Creating dir: " + file.getParentFile().getCanonicalPath());
				file.getParentFile().mkdir();
			}
			log.info("Getting output stream");
			out = new FileOutputStream(file);
			log.info("Writing file (this takes a little bit of time)...");
			book.write(out);
			log.info("Done with write");
			out.flush();
			log.info("Done with flush");
		} catch (Throwable exp) {
			exp.printStackTrace();
			throw new RuntimeException(exp);
		} finally {
			close(book);
			if (out != null) {
				log.info("Closing");
				FileUtil.close(out);
				log.info("Done with close");
			}
		}
	}

	public static void close(Workbook book) {
		try {
			log.info("Closing workbook.");
			book.close();
			if (book instanceof SXSSFWorkbook) {
				log.info("Doing dispose");
				((SXSSFWorkbook) book).dispose();
			}
			log.info("Done with close");
		} catch (Exception exp) {
			throw new RuntimeException(exp);
		}
	}

}
