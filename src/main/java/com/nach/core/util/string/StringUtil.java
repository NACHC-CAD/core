package com.nach.core.util.string;

import org.apache.commons.lang3.StringUtils;

public class StringUtil {

	/**
	 * 
	 * Returns the bit of the string that follows that last instance of the suffix.  
	 * 
	 */	
	public static String getSuffix(String str, String delim) {
		if(StringUtils.isEmpty(str)) {
			return null;
		}
		String temp = str;
		temp = temp.trim();
		if(temp.endsWith(delim)) {
			temp = temp.substring(0, temp.length() - 1);
		}
		return temp.substring(temp.lastIndexOf(delim) + 1, temp.length());
	}
	
	public static String removeSpecial(String str) {
		//String  rtn = str.replaceAll("[^\\p{L}\\p{Z}]","");
		String  rtn = str.replaceAll("[^A-Za-z0-9_\\-]","");
		return rtn;
	}
	
}
