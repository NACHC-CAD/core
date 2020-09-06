package com.nach.core.util.excel.example;


import java.io.File;
import java.io.InputStream;
import java.util.Iterator;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.util.XMLHelper;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.model.SharedStringsTable;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import com.nach.core.util.file.FileUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ExampleEventUserModel {
	
	private static final String DIR = "/com/nach/core/testfiles/excel";

	private static final String SRC_FILE_NAME = DIR + "/large-file-for-excel-test.zip";

	private static final String UNZIPPED_DIR_NAME = DIR + "/unzipped";

	private static final String TARGET_CSV = DIR + "/unzipped/large-file-for-excel-test.csv";

	private static final String EXCEL_FILE_NAME = DIR + "/output/large-file-for-exel-text.xlsx";

    public static void main(String[] args) throws Exception {
        ExampleEventUserModel example = new ExampleEventUserModel();
        // example.processOneSheet(args[0]);
        File file = FileUtil.getFile(EXCEL_FILE_NAME);
        example.processAllSheets(file.getCanonicalPath());
    }

    public void processOneSheet(String fileName) throws Exception {
        OPCPackage pkg = OPCPackage.open(fileName);
        XSSFReader r = new XSSFReader( pkg );
        SharedStringsTable sst = r.getSharedStringsTable();
        XMLReader parser = fetchSheetParser(sst);
        // To look up the Sheet Name / Sheet Order / rID,
        //  you need to process the core Workbook stream.
        // Normally it's of the form rId# or rSheet#
        InputStream sheet2 = r.getSheet("rId2");
        InputSource sheetSource = new InputSource(sheet2);
        parser.parse(sheetSource);
        sheet2.close();
    }
    public void processAllSheets(String filename) throws Exception {
    	log.info("opening package");
        OPCPackage pkg = OPCPackage.open(filename);
        log.info("creating reader");
    	XSSFReader r = new XSSFReader( pkg );
        log.info("getting shared strings table");
    	SharedStringsTable sst = r.getSharedStringsTable();
        log.info("creating parser");
    	XMLReader parser = fetchSheetParser(sst);
        log.info("getting sheets");
    	Iterator<InputStream> sheets = r.getSheetsData();
        while(sheets.hasNext()) {
            log.info("Processing new sheet:\n");
            log.info("getting input stream for sheet");
            InputStream sheet = sheets.next();
            InputSource sheetSource = new InputSource(sheet);
            log.info("parsing sheet");
            parser.parse(sheetSource);
            log.info("closing sheet");
            sheet.close();
            log.info("");
        }
    }
    public XMLReader fetchSheetParser(SharedStringsTable sst) throws SAXException, ParserConfigurationException {
        XMLReader parser = XMLHelper.newXMLReader();
        ContentHandler handler = new SheetHandler(sst);
        parser.setContentHandler(handler);
        return parser;
    }
    /**
     * See org.xml.sax.helpers.DefaultHandler javadocs
     */
    private static class SheetHandler extends DefaultHandler {
        private SharedStringsTable sst;
        private String lastContents;
        private boolean nextIsString;
        private SheetHandler(SharedStringsTable sst) {
            this.sst = sst;
        }
        public void startElement(String uri, String localName, String name,
                                 Attributes attributes) throws SAXException {
            // c => cell
            if(name.equals("c")) {
                // Print the cell reference
                System.out.print(attributes.getValue("r") + " - ");
                // Figure out if the value is an index in the SST
                String cellType = attributes.getValue("t");
                if(cellType != null && cellType.equals("s")) {
                    nextIsString = true;
                } else {
                    nextIsString = false;
                }
            }
            // Clear contents cache
            lastContents = "";
        }
        public void endElement(String uri, String localName, String name)
                throws SAXException {
            // Process the last contents as required.
            // Do now, as characters() may be called more than once
            if(nextIsString) {
                int idx = Integer.parseInt(lastContents);
                lastContents = sst.getItemAt(idx).getString();
                nextIsString = false;
            }
            // v => contents of a cell
            // Output after we've seen the string contents
            if(name.equals("v")) {
                log.info(lastContents);
            }
        }
        public void characters(char[] ch, int start, int length) {
            lastContents += new String(ch, start, length);
        }
    }
}