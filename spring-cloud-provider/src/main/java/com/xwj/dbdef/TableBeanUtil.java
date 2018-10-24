package com.xwj.dbdef;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.CharUtils;
import org.apache.commons.lang.StringUtils;

public class TableBeanUtil {

	/**
	 * 对象属性转换为数据库字段。例如：userName => user_name
	 * 
	 * 连续大写字母不会以'_'分隔。例如settID => settid
	 * 
	 * @param property
	 *            对象属性
	 */
	public static String propertyToField(String property) {
		if (StringUtils.isEmpty(property)) {
			return "";
		}
		if (property.length() == 1) {
			return property.toLowerCase();
		}
		char[] chars = property.toCharArray();
		StringBuffer sb = new StringBuffer();

		char c = chars[0];
		boolean isSep = CharUtils.isAsciiAlphaUpper(c);
		sb.append(c);
		for (int i = 1; i < chars.length; i++) {
			char s = chars[i];
			boolean isCurrUp = CharUtils.isAsciiAlphaUpper(s);
			if (!isSep && isCurrUp) {
				sb.append("_" + s);
				isSep = true;
			} else {
				sb.append(s);
				isSep = false;
			}
		}
		return sb.toString().toLowerCase();
	}

	private static String lowercaseConStr(String property) {
		
//		property.replaceAll("", replacement)
		
		char[] chars = property.toCharArray();
		StringBuffer sb = new StringBuffer();
		char c = chars[0];
		int count = 1;
		StringBuffer upperChars = new StringBuffer();
		boolean isSep = CharUtils.isAsciiAlphaUpper(c);
		for (int i = 1; i < chars.length; i++) {
			char s = chars[i];
			boolean isCurrUp = CharUtils.isAsciiAlphaUpper(s);
			
			
			
			if (isCurrUp) {
				upperChars.delete(0, upperChars.length());
			} else {
				
			}

			if (isSep) {
				if (isCurrUp) {
					upperChars.append(s);
				} else {
					sb.append(s);
					upperChars.delete(0, upperChars.length());
				}
			} else {
				if (isCurrUp) {
					upperChars.append(s);
				} else {
					sb.append(s);
					upperChars.delete(0, upperChars.length());
				}
			}
			if (upperChars.length() > 2) {
				sb.append(upperChars.toString().toLowerCase());
			} else {
				sb.append(c);
			}
			c = s;
		}
		sb.append(c);
		return sb.toString();
	}

	/**
	 * 字段转换成对象属性 例如：user_name to userName
	 * 
	 * @param field
	 * @return
	 */
	public static String fieldToProperty(String field) {
		if (null == field) {
			return "";
		}
		char[] chars = field.toCharArray();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < chars.length; i++) {
			char c = chars[i];
			if (c == '_') {
				int j = i + 1;
				if (j < chars.length) {
					sb.append(StringUtils.upperCase(CharUtils.toString(chars[j])));
					i++;
				}
			} else {
				sb.append(c);
			}
		}
		return sb.toString();
	}

	private static void getNumFromString(String string) {
		StringBuilder sb = new StringBuilder();
		char c = string.charAt(0);
		int count = 1;
		for (int i = 1; i < string.length(); i++) {
			char s = string.charAt(i);
			if (s == c) {
				count++;
			} else {
				if (count > 1) {
					sb.append(count);
					sb.append(c);
					count = 1;
				} else {
					sb.append(c);
				}
			}
			c = s;
		}
		sb.append(c);
		System.out.println(sb.toString());

	}

	public static void main(String[] args) {
		// System.out.println(propertyToField("UserName"));
		// System.out.println(propertyToField("isUserName"));
		// System.out.println(propertyToField("UUserName"));
		// System.out.println(propertyToField("aUserName"));
		//
		// System.out.println(propertyToField("settID"));
		// System.out.println(propertyToField("settIDE"));

		System.out.println(lowercaseConStr("aBBcD"));
		System.out.println(lowercaseConStr("aBBC"));
		System.out.println(lowercaseConStr("AbcD"));
		System.out.println(lowercaseConStr("ABcD"));
	}

}
