package com.xwj.util;

import org.apache.commons.lang3.ObjectUtils;

/**
 * 时间格式常量类
 * 
 * @author xuwenjin 2018年7月17日
 */
public class PatternConsts {

	public static final String DF_SIMPLE_YM = ObjectUtils.CONST("yyyyMM");

	public static final String DF_SIMPLE_YMD = ObjectUtils.CONST("yyyyMMdd");

	public static final String DF_SIMPLE_YMDHMS = ObjectUtils.CONST("yyyyMMddHHmmss");

	public static final String DF_ZH_YM = ObjectUtils.CONST("yyyy-MM");

	public static final String DF_ZH_YMD = ObjectUtils.CONST("yyyy-MM-dd");

	public static final String DF_ZHCN_YMD = ObjectUtils.CONST("yyyy年MM月dd日");

	public static final String DF_ZH_HMS = ObjectUtils.CONST("HH:mm:ss");

	public static final String DF_ZH_YMDHMS = ObjectUtils.CONST("yyyy-MM-dd HH:mm:ss");

	public static final String DF_ZHCN_YMDHMS = ObjectUtils.CONST("yyyy年MM月dd日HH时mm分ss秒");


}
