package com.nach.core.util.time;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeUtil {

	public static int getCurrentYear() {
		return Calendar.getInstance().get(Calendar.YEAR);
	}
	
	public static String getCurrentYearAsString() {
		return Calendar.getInstance().get(Calendar.YEAR) + "";
	}
	
	public static Date getDateForYyyy_Mm_Dd(String str) {
		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-mm-dd");
			Date rtn = format.parse(str);
			return rtn;
		} catch(Exception exp) {
			throw new RuntimeException(exp);
		}
	}

	public static String getDateAsYyyyMmDd(Date date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyymmdd");
		String rtn = format.format(date);
		return rtn;
	}
	
	public static String getDateAsYyyyMmDdFromYyyy_Mm_Dd(String str) {
		Date date = getDateForYyyy_Mm_Dd(str);
		String rtn = getDateAsYyyyMmDd(date);
		return rtn;
	}
	
}
