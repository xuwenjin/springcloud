package com.xwj.billcode;

import java.security.SecureRandom;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

import com.xwj.billcode.base.IdWorker;
import com.xwj.util.PatternConsts;
import com.xwj.util.TimeUtil;

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
		// 14位时间戳
		String dateStr = TimeUtil.dateToString(TimeUtil.now(), PatternConsts.DF_SIMPLE_YMDHMS);
		try {
			// 18位雪花算法id
			long nextId = IdWorker.getFlowIdWorkerInstance().nextId();
			return dateStr + nextId;
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
		// 8位时间戳
		String dateStr = TimeUtil.dateToString(TimeUtil.now(), PatternConsts.DF_SIMPLE_YMD);
		try {
			long nextId = IdWorker.getFlowIdWorkerInstance().nextId();
			// 6位时间戳 + 10位雪花算法id
			return dateStr.substring(2, 8) + String.valueOf(nextId).substring(0, 10);
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
	 * @param digit
	 *            数字位数
	 */
	public static String getRandomNum(int digit) {
		if (digit < 1) {
			return "";
		}

		// ThreadLocalRandom解决了Random类在多线程下多个线程竞争内部唯一的原子性种子变量而导致大量线程自旋重试的不足
		ThreadLocalRandom localRandom = ThreadLocalRandom.current();
		StringBuilder numStr = new StringBuilder();
		IntStream.of(0, digit).forEach(index -> numStr.append(localRandom.nextInt(10)));
		return numStr.toString();
	}

}
