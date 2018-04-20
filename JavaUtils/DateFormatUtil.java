package com.calabar.commons.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.time.DateUtils;

public class DateFormatUtil {

	public static Date parse(String dateStr) {
		try {
			return DateUtils.parseDate(dateStr, "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd");
		} catch (ParseException e) {
			return null;
		}
	}

	public static Date parse(String dateStr, String formatStr) {
		try {
			return DateUtils.parseDate(dateStr, formatStr);
		} catch (ParseException e) {
			return null;
		}
	}

	public static String parse(long time) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String timeStr = sdf.format(new Date(time));
		return timeStr;
	}
	
	public static String parse(long time, String formatStr) {
        SimpleDateFormat sdf = new SimpleDateFormat(formatStr);
        String timeStr = sdf.format(new Date(time));
        return timeStr;
    }

}