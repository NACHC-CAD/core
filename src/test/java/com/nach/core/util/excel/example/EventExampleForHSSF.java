package com.nach.core.util.excel.example;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.poi.hssf.eventusermodel.HSSFEventFactory;
import org.apache.poi.hssf.eventusermodel.HSSFListener;
import org.apache.poi.hssf.eventusermodel.HSSFRequest;
import org.apache.poi.hssf.record.BOFRecord;
import org.apache.poi.hssf.record.BoundSheetRecord;
import org.apache.poi.hssf.record.LabelSSTRecord;
import org.apache.poi.hssf.record.NumberRecord;
import org.apache.poi.hssf.record.Record;
import org.apache.poi.hssf.record.RowRecord;
import org.apache.poi.hssf.record.SSTRecord;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import com.nach.core.util.file.FileUtil;

/**
 * MIGHT NEED TO DO SOMETHING LIKE THIS IF NEED TO SUPPORT EARLIER VERSIONS OF EXCEL
 * This is from here:
 * http://poi.apache.org/components/spreadsheet/how-to.html#event_api 
 * 
 * This example shows how to use the event API for reading a file.
 */
public class EventExampleForHSSF implements HSSFListener {

	private static final String DIR = "/com/nach/core/testfiles/excel";

	private static final String SRC_FILE_NAME = DIR + "/large-file-for-excel-test.zip";

	private static final String UNZIPPED_DIR_NAME = DIR + "/unzipped";

	private static final String TARGET_CSV = DIR + "/unzipped/large-file-for-excel-test.csv";

	private static final String EXCEL_FILE_NAME = DIR + "/output/large-file-for-exel-text.xlsx";

	private SSTRecord sstrec;

	public static void main(String[] args) throws Exception {
		File file = FileUtil.getFile(EXCEL_FILE_NAME);
		parseFile(file);
	}

	private static void parseFile(File file) throws Exception {
		FileInputStream fin = new FileInputStream(file);
		POIFSFileSystem poifs = new POIFSFileSystem(fin);
		InputStream din = poifs.createDocumentInputStream("Workbook");
		HSSFRequest req = new HSSFRequest();
		req.addListenerForAllRecords(new EventExampleForHSSF());
		HSSFEventFactory factory = new HSSFEventFactory();
		factory.processEvents(req, din);
		fin.close();
		din.close();
		System.out.println("done.");
	}

	public void processRecord(Record record) {
		switch (record.getSid()) {
		// the BOFRecord can represent either the beginning of a sheet or the workbook
		case BOFRecord.sid:
			BOFRecord bof = (BOFRecord) record;
			if (bof.getType() == bof.TYPE_WORKBOOK) {
				System.out.println("Encountered workbook");
				// assigned to the class level member
			} else if (bof.getType() == bof.TYPE_WORKSHEET) {
				System.out.println("Encountered sheet reference");
			}
			break;
		case BoundSheetRecord.sid:
			BoundSheetRecord bsr = (BoundSheetRecord) record;
			System.out.println("New sheet named: " + bsr.getSheetname());
			break;
		case RowRecord.sid:
			RowRecord rowrec = (RowRecord) record;
			System.out.println("Row found, first column at "
					+ rowrec.getFirstCol() + " last column at " + rowrec.getLastCol());
			break;
		case NumberRecord.sid:
			NumberRecord numrec = (NumberRecord) record;
			System.out.println("Cell found with value " + numrec.getValue()
					+ " at row " + numrec.getRow() + " and column " + numrec.getColumn());
			break;
		// SSTRecords store a array of unique strings used in Excel.
		case SSTRecord.sid:
			sstrec = (SSTRecord) record;
			for (int k = 0; k < sstrec.getNumUniqueStrings(); k++) {
				System.out.println("String table value " + k + " = " + sstrec.getString(k));
			}
			break;
		case LabelSSTRecord.sid:
			LabelSSTRecord lrec = (LabelSSTRecord) record;
			System.out.println("String cell found with value "
					+ sstrec.getString(lrec.getSSTIndex()));
			break;
		}
	}

}