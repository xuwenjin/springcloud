package com.xwj.billcode;

import java.security.SecureRandom;
import java.util.Date;
import java.util.Random;

import org.apache.commons.lang3.time.DateFormatUtils;

import com.xwj.billcode.base.IdWorker;
import com.xwj.util.PatternConsts;

import lombok.SneakyThrows;

/**
 * 不重复订单号生成类
 *
 */
public class SequenceGenerator {

	/**
	 * 生成32位随机数（推荐）
	 */
	@SneakyThrows
	public static String uuid32() {
		Date date = new Date(System.currentTimeMillis());
		String dateStr = DateFormatUtils.format(date, PatternConsts.DF_SIMPLE_YMDHMS);
		try {
			return dateStr + IdWorker.getFlowIdWorkerInstance().nextId();
		} catch (Exception e) {
			SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
			StringBuffer tmp = new StringBuffer("");
			for (int i = 0; i < 18; i++) {
				tmp.append(random.nextInt(10));
			}
			return dateStr + tmp.toString();
		}
	}

	/**
	 * 生成16位随机数
	 */
	@SneakyThrows
	public static String uuid16() {
		Date date = new Date(System.currentTimeMillis());
		String dateStr = DateFormatUtils.format(date, PatternConsts.DF_SIMPLE_YMD);
		try {
			return dateStr.substring(2, 8)
					+ String.valueOf(IdWorker.getFlowIdWorkerInstance().nextId()).substring(0, 10);
		} catch (Exception e) {
			SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
			StringBuffer tmp = new StringBuffer("");
			for (int i = 0; i < 16; i++) {
				tmp.append(random.nextInt(10));
			}
			return dateStr.substring(2, 8) + tmp.substring(0, 10);
		}
	}

	/**
	 * 生成指定位数随机数字
	 * 
	 * @param digit 数字位数
	 */
	public static String getRandomNum(int digit) {
		if (digit < 1) {
			return "";
		}

		Random rd = new Random();
		StringBuilder numStr = new StringBuilder();
		for (int i = 0; i < digit; i++) {
			numStr.append(rd.nextInt(10));
		}
		return numStr.toString();
	}
	
	
	public static void main(String[] args) {
		System.out.println(uuid32());
		System.out.println(uuid16());
	}

}
