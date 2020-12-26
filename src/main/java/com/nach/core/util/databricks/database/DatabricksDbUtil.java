package com.nach.core.util.databricks.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.yaorma.database.Data;
import org.yaorma.database.Database;
import org.yaorma.database.Row;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DatabricksDbUtil {

	//
	// connection
	//

	public static Connection getConnection(String url, String token) {
		try {
			if (url.endsWith("PWD=") == false) {
				url = url + "PWD=";
			}
			url = url + token;
			Connection conn = DriverManager.getConnection(url);
			return conn;
		} catch (Exception exp) {
			throw new RuntimeException(exp);
		}
	}

	public static Connection getConnection(String url) {
		try {
			Connection conn = DriverManager.getConnection(url);
			return conn;
		} catch (Exception exp) {
			throw new RuntimeException(exp);
		}
	}

	//
	// database methods
	//

	/**
	 * 
	 * Does a database exist.
	 * 
	 */
	public static boolean databaseExists(String schemaName, Connection conn) {
		Data data = showSchemas(conn);
		for (Map<String, String> row : data) {
			String namespace = row.get("namespace");
			if (schemaName.equalsIgnoreCase(namespace)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 
	 * Get a list of the existing databases.
	 * 
	 */
	public static Data showSchemas(Connection conn) {
		String sqlString = "show schemas";
		Data rtn = Database.query(sqlString, conn);
		return rtn;
	}

	/**
	 * 
	 * Drop a database.
	 * 
	 */
	public static void dropDatabase(String schemaName, Connection conn) {
		if (databaseExists(schemaName, conn) == true) {
			Data data = showTables(schemaName, conn);
			for (Map<String, String> row : data) {
				String tableName = row.get("tablename");
				dropTable(schemaName, tableName, conn);
			}
			String sqlString = "drop database if exists " + schemaName;
			Database.update(sqlString, conn);
		}
	}

	/**
	 * 
	 * Create a database.
	 * 
	 */
	public static void createDatabase(String databaseName, Connection conn) {
		String sqlString = "create database " + databaseName;
		Database.update(sqlString, conn);
	}

	//
	// table methods
	//

	/**
	 * 
	 * Get a list of tables for a given schema.
	 * 
	 */
	public static Data showTables(String schemaName, Connection conn) {
		String sqlString;
		sqlString = "show tables in " + schemaName;
		Data rtn = Database.query(sqlString, conn);
		return rtn;
	}

	/**
	 * 
	 * Drop a table.
	 * 
	 */
	public static void dropTable(String schemaName, String tableName, Connection conn) {
		dropTable(schemaName + "." + tableName, conn);
	}

	public static void dropTable(String fullyQualifiedName, Connection conn) {
		String sqlString = "drop table if exists " + fullyQualifiedName;
		Database.update(sqlString, conn);
	}

	public static void createCsvTableForDir(String databricksPath, String schemaName, String tableName, Connection conn) {
		createCsvTableForDir(databricksPath, schemaName, tableName, ",", conn);
	}

	public static void createCsvTableForDir(String databricksPath, String schemaName, String tableName, String delim, Connection conn) {
		log.info("Dropping if exists...");
		dropTable(schemaName, tableName, conn);
		String sqlString = "\n";
		sqlString += "create table " + schemaName + "." + tableName + " \n";
		sqlString += "using csv \n";
		sqlString += "options ( \n";
		sqlString += "  header = \"true\", \n";
		if (delim == null) {
			sqlString += "  inferDelimiter = \"true\", \n";
		} else {
			sqlString += "  delimiter = \"" + delim + "\", \n";
		}
		sqlString += "  inferSchema = \"false\", \n";
		sqlString += "  path = \"" + databricksPath + "\" \n";
		sqlString += ") \n";
		log.info("\n" + sqlString);
		Database.update(sqlString, conn);
		refreshTable(schemaName, tableName, conn);
	}

	public static void refreshTable(String schemaName, String tableName, Connection conn) {
		String sqlString = "refresh table " + schemaName + "." + tableName;
		log.info("Refreshing table: " + sqlString);
		Database.update(sqlString, conn);
	}

	public static void close(Connection conn) {
		Database.close(conn);
	}

	public static List<String> listRawSchema(Connection conn) {
		Data data = Database.query("show databases", conn);
		ArrayList<String> rtn = new ArrayList<String>();
		for (Row row : data) {
			String str = row.get("namespace");
			if (str != null && str.toLowerCase().equals("cosmos") == false && str.toLowerCase().equals("default") == false) {
				if(str.startsWith("this_is_") == false) {
					rtn.add(str);
				}
			}
		}
		return rtn;
	}

}
