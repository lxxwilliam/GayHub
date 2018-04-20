package com.calabar.commons.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

public class PinyinUtils {
	
	private static Logger logger = LoggerFactory.getLogger(PinyinUtils.class);

	/**
	 * 获得汉语拼音首字母
	 *
	 * @param chines
	 *            汉字
	 * @return
	 */
	public static String getAlpha(String chines) {
		String pinyinName = "";
		char[] nameChar = chines.toCharArray();
		HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
		defaultFormat.setCaseType(HanyuPinyinCaseType.UPPERCASE);
		defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		for (int i = 0; i < nameChar.length; i++) {
			if (nameChar[i] > 128) {
				try {
					String[] t = PinyinHelper.toHanyuPinyinStringArray(nameChar[i], defaultFormat);
					if(t!=null&&t.length>0){
						pinyinName += t[0].charAt(0);
					}else {
						pinyinName += nameChar[i];
					}
				} catch (BadHanyuPinyinOutputFormatCombination e) {
					logger.error(e.getMessage(),e);
				}
			} else {
				pinyinName += nameChar[i];
			}
		}
		return pinyinName;
	}

	/**
	 * 将字符串中的中文转化为拼音,英文字符不变
	 *
	 * @param inputString
	 *            汉字
	 * @param fistA
	 *            首字母是否大写
	 * @return
	 */
	public static String getPingYin(String inputString,boolean fistA) {
		HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
		format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
		format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		format.setVCharType(HanyuPinyinVCharType.WITH_V);
		String output = "";
		if (inputString != null && inputString.length() > 0 && !"null".equals(inputString)) {
			char[] input = inputString.trim().toCharArray();
			try {
				for (int i = 0; i < input.length; i++) {
					if (java.lang.Character.toString(input[i]).matches("[\\u4E00-\\u9FA5]+")) {
						String[] temp = PinyinHelper.toHanyuPinyinStringArray(input[i], format);
						String str = temp[0];
						if(fistA){
							String fist = String.valueOf(str.charAt(0));
							str = str.replaceFirst(fist, fist.toUpperCase());
						}
						output += str;
					} else
						output += java.lang.Character.toString(input[i]);
				}
			} catch (BadHanyuPinyinOutputFormatCombination e) {
				logger.error(e.getMessage(),e);
			}
		} else {
			return "*";
		}
		return output;
	}
	
	public static String getPingYin(String inputString) {
		return getPingYin(inputString,false);
	}

	public static void main(String[] args) {
		System.out.println(getAlpha("六环内（马驹桥镇）"));
	}
}
