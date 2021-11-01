package com.nach.core.util.csv;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.io.input.BOMInputStream;

public class CsvUtilApache {

	// ---
	//
	// csv inputs
	//
	// ---
	
	public static CSVParser getParser(File file) {
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(new BOMInputStream(new FileInputStream(file)),"UTF-8"));
			CSVParser parser = new CSVParser(reader, CSVFormat.EXCEL);
			return parser;
		} catch(Exception exp) {
			throw new RuntimeException(exp);
		}
	}

	public static CSVParser getParser(InputStream is) {
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(new BOMInputStream(is),"UTF-8"));
			CSVParser parser = new CSVParser(reader, CSVFormat.EXCEL);
			return parser;
		} catch(Exception exp) {
			throw new RuntimeException(exp);
		}
	}

	// ---
	//
	// csv outputs
	//
	// ---
	
	public static CSVPrinter getWriter(File file) {
		try {
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
			CSVPrinter printer = new CSVPrinter(writer, CSVFormat.EXCEL);
			return printer;
		} catch(Exception exp) {
			throw new RuntimeException(exp);
		}
	}
	
	public static CSVPrinter getWriter(OutputStream os) {
		try {
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os));
			CSVPrinter printer = new CSVPrinter(writer, CSVFormat.EXCEL);
			return printer;
		} catch(Exception exp) {
			throw new RuntimeException(exp);
		}
	}
	
}
