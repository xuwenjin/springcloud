package com.xwj.dbdef;

import org.apache.commons.lang.CharUtils;
import org.apache.commons.lang.StringUtils;

public class TableBeanUtil {

	/**
	 * 对象属性转换为字段 例如：userName to user_name
	 * 
	 * @param property
	 *            字段名
	 * @return
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
		for (int i = 0; i < chars.length; i++) {
			char c = chars[i];
			if (i == 0) {
				sb.append(StringUtils.lowerCase(CharUtils.toString(c)));
			} else {
				if (CharUtils.isAsciiAlphaUpper(c)) {
					sb.append("_" + StringUtils.lowerCase(CharUtils.toString(c)));
				} else {
					sb.append(c);
				}
			}
		}
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
	
	public static void main(String[] args) {
		System.out.println(propertyToField("UserName"));
		System.out.println(propertyToField("isUserName"));
		System.out.println(propertyToField("UUserName"));
		System.out.println(propertyToField("aUserName"));
	}

}
