package com.xwj.dbdef;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.CharUtils;
import org.apache.commons.lang.StringUtils;

/**
 * 实体与数据库字段转换工具类
 * 
 * @author xwj
 *
 */
public class TableBeanUtil {

	/**
	 * 对象属性转换为数据库字段。例如：userName => user_name
	 * 
	 * 连续大写字母跳过，不会加"_"。例如settID => settid
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

		// 将property字符串中，连续两个及两个以上的字符替换成小写
		property = lowerConStr(property);

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
	 * 将property字符串中，连续两个及两个以上的字符替换成小写
	 */
	private static String lowerConStr(String property) {
		// 记录连续大写字母。元素组成：连续大写字母首个字母下标 + "_" + 连续大写字母个数
		List<String> uppList = new ArrayList<>();

		String info = "";
		char[] chars = property.toCharArray();
		char c = chars[0];
		int count = 1; // 记录连续大写字母个数
		for (int i = 1; i < chars.length; i++) {
			// 上个字符是否大写
			boolean isSep = CharUtils.isAsciiAlphaUpper(c);

			char s = chars[i];
			// 当前字符是否大写
			boolean isCurrUp = CharUtils.isAsciiAlphaUpper(s);

			// 连续大写，记录下来
			if (isSep && isCurrUp) {
				if (StringUtils.isEmpty(info)) {
					info = (i - 1) + "_";
				}
				count++;
				if (i == chars.length - 1) {
					String wordInfo = info + count;
					uppList.add(wordInfo);
				}
			} else {
				if (count > 1) {
					String wordInfo = info + count;
					uppList.add(wordInfo);
					count = 1;
					info = "";
				}
			}
			c = s;
		}
		String lower = lower(property, uppList);
		// System.out.println(property + " => " + lower);
		return lower;
	}

	/**
	 * 将property中连续大写字母，根据uppList记录，转为小写
	 */
	private static String lower(String property, List<String> uppList) {
		StringBuilder sb = new StringBuilder(property);
		for (String str : uppList) {
			String[] arr = str.split("_");
			int index = Integer.valueOf(arr[0]);
			int count = Integer.valueOf(arr[1]);

			String r = property.substring(index, index + count);
			sb.replace(index, index + count, r.toLowerCase());
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
		System.out.println(propertyToField("settID"));
		System.out.println(propertyToField("settIDE"));

		// lowerConStr("aBBcD");
		// lowerConStr("aBBC");
		// lowerConStr("AbcD");
		// lowerConStr("ABcD");
		// lowerConStr("ABCD");
		// lowerConStr("ABcDD");
		// lowerConStr("aaBBcDDFF");
	}

}
