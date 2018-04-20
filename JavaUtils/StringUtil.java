package com.calabar.commons.utils;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {

	public static String makeStr(Long value,int length){
		String str = String.format("%0" + length + "d", value);
		return str;
	}
	
	public static boolean validByRegex(String regex, String input) {
		Pattern p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
		Matcher regexMatcher = p.matcher(input);
		return regexMatcher.find();
	}
	
	/**
	 * 返回长度为【strLength】的随机数，在前面补0
	 */
	public static String getFixLenth(int strLength) {
		Random rm = new Random();
		// 获得随机数
		double pross = (1 + rm.nextDouble()) * Math.pow(10, strLength);
		// 将获得的获得随机数转化为字符串
		String fixLenthString = String.valueOf(pross);
		// 返回固定的长度的随机数
		return fixLenthString.substring(1, strLength + 1);
	}
	
	public static boolean isMobile(String s) {
		boolean rtn = validByRegex("^(1+\\d{10})$", s);
		return rtn;
	}
	
	public static void main(String[] args) {
		//String SMS_FT="【熊猫二手车】验证码：%d,十分钟内有效！熊猫二手车平台";
		//System.out.println("18048529817".substring(7));
	}
}
