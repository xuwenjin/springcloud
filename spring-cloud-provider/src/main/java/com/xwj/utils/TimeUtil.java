package com.sgp.travel.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;

import com.sgp.travel.consts.PatternConsts;
import com.sgp.travel.core.exception.InnerException;

/**
 * 日期时间的工具类
 */
public class TimeUtil {

	/**
	 * 判断日期字符串是否制定格式
	 * 
	 * @param dateStr       日期字符串
	 * @param parsePatterns 格式
	 */
	public static boolean isValidDate(String dateStr, String parsePatterns) {
		try {
			DateUtils.parseDate(dateStr, parsePatterns);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 日期字符串转日期，格式为yyyy-MM-dd
	 */
	public static Date stringToDate(String dateStr) {
		return stringToDate(dateStr, PatternConsts.DF_ZH_YMD);
	}

	/**
	 * 日期时间字符串转日期，格式为yyyy-MM-dd HH:mm:ss
	 */
	public static Date stringToDateTime(String dateTimeStr) {
		return stringToDate(dateTimeStr, PatternConsts.DF_ZH_YMDHMS);
	}

	/**
	 * 字符串转日期
	 * 
	 * @param dateStr       日期字符串
	 * @param parsePatterns 格式
	 */
	public static Date stringToDate(String dateStr, String parsePatterns) {
		try {
			return DateUtils.parseDate(dateStr, parsePatterns);
		} catch (IllegalArgumentException e) {
			String es = "解析日期时出错,请检查日期格式和日期字符串是否有效";
			throw new InnerException(es, e);
		} catch (ParseException e) {
			String es = "解析日期时出错,请检查代码";
			throw new InnerException(es, e);
		} catch (Exception e) {
			String es = "解析日期出错...";
			throw new InnerException(es, e);
		}
	}

	/**
	 * 将日期按指定格式转为字符串
	 * 
	 * @param dateFormat 日期格式
	 */
	public static String dateToString(Date date, String dateFormat) {
		if (date == null) {
			return null;
		}
		return DateFormatUtils.format(date, dateFormat);
	}

	/**
	 * 将日期按指定格式转为Long类型
	 * 
	 * @param date
	 * @param dateFormat 日期格式
	 * @return
	 */
	public static Long dateToLong(Date date, String dateFormat) {
		return Long.valueOf(DateFormatUtils.format(date, dateFormat));
	}

	/**
	 * 将日期按 yyyy-MM-dd 格式 解析成字符串返回.
	 * 
	 * @param date
	 * @return
	 */
	public static String dateToStringSimple(Date date) {
		return dateToString(date, PatternConsts.DF_ZH_YMD);
	}

	/**
	 * 将日期按 yyyy-MM-dd HH:mm:ss 格式 解析成字符串返回.
	 * 
	 * @param date
	 * @return
	 */
	public static String dateToString(Date date) {
		return dateToString(date, PatternConsts.DF_ZH_YMDHMS);
	}

	/**
	 * 将日期按 yyyy-MM-dd HH:mm:ss 格式 解析成Long类型返回.
	 * 
	 * @param date
	 * @return
	 */
	public static Long dateToLong(Date date) {
		return dateToLong(date, PatternConsts.DF_SIMPLE_YMDHMS);
	}

	/**
	 * 根据传入的日期格式(如{@link #DF_ZH_YMDHMS})返回今天日期的字符串 .
	 * 
	 * @author 丁云飞.
	 * @param format 日期格式 参见{@link SimpleDateFormat}
	 * @return
	 * @throws InnerException 调用者请根据业务要求手动处理或不处理解析异常后的情况.异常信息同
	 *                        {@link #dateToString(Date, String)}
	 * @see #dateToString(Date, String)
	 */
	public static String getTodayString(String format) {
		return dateToString(new Date(), format);
	}

	/**
	 * 以 {@link #DF_ZH_YMD}格式返回今天的日期字符串格式.
	 * 
	 * @author 丁云飞.
	 * @return
	 * @throws InnerException 调用者请根据业务要求手动处理或不处理解析异常后的情况. 异常信息同
	 *                        {@link #dateToString(Date, String)}
	 * @see #dateToString(Date, String)
	 */
	public static String getTodayString() {
		return getTodayString(PatternConsts.DF_ZH_YMD);
	}

	/**
	 * 以 {@link #DF_ZHCN_YMD}格式返回今天的日期字符串格式.
	 * 
	 * @author Hal.
	 * @return
	 * @throws InnerException 调用者请根据业务要求手动处理或不处理解析异常后的情况. 异常信息同
	 *                        {@link #dateToString(Date, String)}
	 * @see #dateToString(Date, String)
	 */
	public static String getTodayStringZhCN() {
		return getTodayString(PatternConsts.DF_ZHCN_YMD);
	}

	/**
	 * 以 {@link #DF_ZH_HMS}格式返回今天的日期字符串格式.
	 * 
	 * @author 丁云飞.
	 * @return
	 * @throws InnerException 调用者请根据业务要求手动处理或不处理解析异常后的情况. 异常信息同
	 *                        {@link #dateToString(Date, String)}
	 * @see #dateToString(Date, String)
	 */
	public static String getNowTime() {
		return getTodayString(PatternConsts.DF_ZH_HMS);
	}

	/**
	 * 以 {@link #DF_ZH_YMDHMS}格式返回今天的日期字符串格式.
	 * 
	 * @author 丁云飞.
	 * @return
	 * @throws InnerException 调用者请根据业务要求手动处理或不处理解析异常后的情况.异常信息同
	 *                        {@link #dateToString(Date, String)}
	 * @see #dateToString(Date, String)
	 */
	public static String getNowDateTime() {
		return getTodayString(PatternConsts.DF_ZH_YMDHMS);
	}

	/**
	 * 返回昨天的日期
	 */
	public static Date getYesterdayDate() {
		GregorianCalendar yesterday = new GregorianCalendar();
		yesterday.add(Calendar.DAY_OF_MONTH, -1);
		return yesterday.getTime();
	}

	/**
	 * 以{@link #DF_ZH_YMD}格式返回返回下月今天的日期字符串.
	 */
	public static String getNextMonthDateString() {
		return getNextMonthDateString(new Date());
	}

	/**
	 * 以{@link #DF_ZH_YMD}格式返回返回下月{@code date}的日期字符串.
	 * 
	 * @throws InnerException 调用者请根据业务要求手动处理或不处理解析异常后的情况. 异常信息同
	 *                        {@link #dateToString(Date, String)}
	 * @see #dateToString(Date, String)
	 */
	public static String getNextMonthDateString(Date date) {
		Calendar calendar = java.util.Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.MONDAY, calendar.get(Calendar.MONDAY) + 1);
		return dateToString(calendar.getTime(), PatternConsts.DF_ZH_YMD);
	}

	/**
	 * 指定时间 年 向前(过去,{@code movNum}为负) 或 向后(未来,{@code movNum}为正) 平移{@code movNum}
	 * ,,返回移动后的Date
	 * 
	 * @param date   指定的日期
	 * @param movNum 移动的长度
	 * @return
	 */
	public static Date setDateYear(Date date, int movNum) {
		GregorianCalendar now = new GregorianCalendar();
		now.setTime(date);
		now.add(GregorianCalendar.YEAR, movNum);
		return now.getTime();
	}

	/**
	 * 指定时间 月 向前(过去,{@code movNum}为负) 或 向后(未来,{@code movNum}为正) 平移{@code movNum}
	 * ,,返回移动后的Date
	 * 
	 * @param date   指定的日期
	 * @param movNum 移动的长度
	 * @return
	 */
	public static Date setDateMonth(Date date, int movNum) {
		GregorianCalendar now = new GregorianCalendar();
		now.setTime(date);
		now.add(GregorianCalendar.MONTH, movNum);
		return now.getTime();
	}

	/**
	 * 指定时间 天 向前(过去,{@code movNum}为负) 或 向后(未来,{@code movNum}为正) 平移{@code movNum}
	 * ,,返回移动后的Date
	 * 
	 * @param date   指定的日期
	 * @param movNum 移动的长度
	 * @return
	 */
	public static Date setDateDay(Date date, int movNum) {
		GregorianCalendar now = new GregorianCalendar();
		now.setTime(date);
		now.add(GregorianCalendar.DATE, movNum);
		return now.getTime();
	}

	/**
	 * 指定时间 小时 向前(过去,{@code movNum}为负) 或 向后(未来,{@code movNum}为正) 平移{@code movNum}
	 * ,,返回移动后的Date
	 * 
	 * @param date   指定的日期
	 * @param movNum 移动的长度
	 * @return
	 */
	public static Date setDateHour(Date date, int movNum) {
		GregorianCalendar now = new GregorianCalendar();
		now.setTime(date);
		now.add(GregorianCalendar.HOUR_OF_DAY, movNum);
		return now.getTime();
	}

	/**
	 * 指定时间 分钟 向前(过去,{@code movNum}为负) 或 向后(未来,{@code movNum}为正) 平移{@code movNum}
	 * ,返回移动后的Date
	 * 
	 * @param date   指定的日期
	 * @param movNum 移动的长度
	 * @return
	 */
	public static Date setDateMinute(Date date, int movNum) {
		GregorianCalendar now = new GregorianCalendar();
		now.setTime(date);
		now.add(GregorianCalendar.MINUTE, movNum);
		return now.getTime();
	}

	/**
	 * 指定时间 秒 向前(过去,{@code movNum}为负) 或 向后(未来,{@code movNum}为正) 平移{@code movNum}
	 * ,返回移动后的Date
	 * 
	 * @param date   指定的日期
	 * @param movNum 移动的长度
	 */
	public static Date setDateSecond(Date date, int movNum) {
		GregorianCalendar now = new GregorianCalendar();
		now.setTime(date);
		now.add(GregorianCalendar.SECOND, movNum);
		return now.getTime();
	}

	/**
	 * 得到两日期内的工作日的天数
	 * 
	 * @date 2011-11-14
	 * @param bofDate 开始日期
	 * @param eofDate 结束日期
	 */
	public static int getWorkDays(Date bofDate, Date eofDate) {
		int days = 0;
		if (bofDate != null && eofDate != null) {
			Calendar bofWork = Calendar.getInstance();// 工作开始时间
			Calendar eofWork = Calendar.getInstance();// 工作结束时间
			bofWork.setTime(bofDate);
			eofWork.setTime(eofDate);
			Calendar tmp = Calendar.getInstance();
			tmp.setTime(bofWork.getTime());
			while ((tmp.get(Calendar.YEAR) == eofWork.get(Calendar.YEAR))
					&& tmp.get(Calendar.DAY_OF_YEAR) <= eofWork.get(Calendar.DAY_OF_YEAR)) {
				if (tmp.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY
						&& tmp.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY) {
					days++;
				}
				tmp.add(Calendar.DAY_OF_YEAR, 1);
			}
		}
		return days;
	}

	/**
	 * 返回日期字符串{@link TimeUtil#DF_ZH_YMDHMS}:SSS.
	 * 
	 * @param calendar 日期
	 * @return
	 */
	public static String toDateString(Calendar calendar) {
		return calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-"
				+ calendar.get(Calendar.DAY_OF_MONTH) + " " + calendar.get(Calendar.HOUR_OF_DAY) + ":"
				+ calendar.get(Calendar.MINUTE) + ":" + calendar.get(Calendar.SECOND) + "."
				+ calendar.get(Calendar.MILLISECOND);
	}

	/**
	 * 计算二个日期的小时间隔 Calculate the hours between two dates
	 * 
	 * @param beginOperateTime
	 * @param endOperateBeginTime
	 * @return
	 * @throws InnerException 根据需要手动处理日期格式或者日期书写错误出现异常的处理情况.
	 */
	public static String calHours(Date beginOperateTime, Date endOperateBeginTime) {
		try {
			long mSeconds = endOperateBeginTime.getTime() - beginOperateTime.getTime();
			return String.valueOf(mSeconds / (1000 * 60 * 60));
		} catch (Exception e) {
			throw new InnerException("解析日期出错,请检查日期格式和日期字符串是否有误", e);
		}
	}

	/**
	 * 将Date转为java.sql.Timestamp Ken 建议可不用此方法. 将日期返回为sql的Timestamp
	 * 
	 * @param date 需要转换的日期.
	 * @return java.sql.Timestamp
	 */
	public static java.sql.Timestamp getTimestamp(Date date) {
		return new java.sql.Timestamp(date.getTime());
	}

	/**
	 * 获取{@code d}中的年
	 * 
	 * @param d 时间
	 * @return 返回 {@code d}中的年
	 */
	public static int getDateYear(Date d) {
		Calendar now = Calendar.getInstance(TimeZone.getDefault());
		now.setTime(d);
		return now.get(Calendar.YEAR);
	}

	/**
	 * 获取{@code d}中的月份
	 * 
	 * @param d 时间
	 * @return 返回 {@code d}中的月份
	 */
	public static int getDateMonth(Date d) {
		Calendar now = Calendar.getInstance(TimeZone.getDefault());
		now.setTime(d);
		return now.get(Calendar.MONTH) + 1;
	}

	/**
	 * 获取{@code d}中的小时
	 * 
	 * @param d 时间
	 * @return 返回 {@code d}中的小时
	 */
	public static int getDateHour(Date d) {
		Calendar now = Calendar.getInstance(TimeZone.getDefault());
		now.setTime(d);
		return now.get(Calendar.HOUR_OF_DAY);
	}

	/**
	 * 获取{@code d}中的分钟
	 * 
	 * @param d 时间
	 * @return 返回 {@code d}中的分钟
	 */
	public static int getDateMin(Date d) {
		Calendar now = Calendar.getInstance(TimeZone.getDefault());
		now.setTime(d);
		return now.get(Calendar.MINUTE);
	}

	/**
	 * 获取{@code d}中的秒
	 * 
	 * @param d 时间
	 * @return 返回 {@code d}中的秒
	 */
	public static int getDateSecond(Date d) {
		Calendar now = Calendar.getInstance(TimeZone.getDefault());
		now.setTime(d);
		return now.get(Calendar.SECOND);
	}

	/**
	 * 获得当前月份的天数
	 */
	public static int getCurentMonthDays() {
		return getMonthDays(new Date());
	}

	/**
	 * 获得{@code d}所在月份的天数
	 */
	public static int getMonthDays(Date d) {
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		return c.getActualMaximum(Calendar.DAY_OF_MONTH);
	}

	/**
	 * 过滤List里面的时间对象，并转换为指定格式的时间字符串
	 * 
	 * @param list       要处理的list对象
	 * @param dateFormat 需要转换的日期格式
	 */
	@SuppressWarnings("rawtypes")
	public static List convertListDateToString(List<Map<String, Object>> list, String dateFormat) {
		if (list == null) {
			return null;
		}
		if (StringUtils.isEmpty(dateFormat)) {
			dateFormat = "yyyy-MM-dd HH:mm:ss";
		}
		for (int i = 0; i < list.size(); i++) {
			Map<String, Object> row = list.get(i);
			for (Map.Entry<String, Object> col : row.entrySet()) {
				if (col.getValue() instanceof Date) {
					String dateStr = TimeUtil.dateToString((Date) col.getValue(), dateFormat);
					col.setValue(dateStr);
				}
			}
		}
		return list;
	}

	/**
	 * 将指定的字符串按指定格式转换成日期时间.
	 * 
	 * @param dateStr 时间字符串
	 * @param fmt     转换格式
	 * @return 日期时间
	 */
	public static Date getDate(String dateStr, String fmt) {
		try {
			return ((new java.text.SimpleDateFormat(fmt))).parse(dateStr);
		} catch (ParseException e) {
			return null;
		}
	}

	/**
	 * 获取当前年份
	 * 
	 * @return
	 */
	public static int getCurrYear() {
		Calendar now = Calendar.getInstance(TimeZone.getDefault());
		return now.get(Calendar.YEAR);
	}

	/**
	 * 获取该年份每天的日期
	 * 
	 * @param year 年份
	 * @return
	 */
	public static List<String> getDayListByYear(int year) {
		List<String> list = new ArrayList<>();
		Calendar cal = Calendar.getInstance(Locale.CHINA);
		cal.set(Calendar.YEAR, year);
		IntStream.range(0, 12).forEach(month -> {
			cal.set(Calendar.MONTH, month);
			list.addAll(getDayListByCal(cal));
		});
		return list;
	}

	private static List<String> getDayListByCal(Calendar cal) {
		List<String> list = new ArrayList<>();
		int year = cal.get(Calendar.YEAR);// 年份
		int month = cal.get(Calendar.MONTH) + 1;// 月份
		int maxDay = cal.getActualMaximum(Calendar.DATE); // 当月最大一天
		IntStream.range(1, maxDay + 1).forEach(day -> {
			String sMonth = "" + month;
			if (sMonth.length() == 1) {
				sMonth = "0" + sMonth;
			}
			String sDay = "" + day;
			if (sDay.length() == 1) {
				sDay = "0" + sDay;
			}
			String aDate = String.valueOf(year) + sMonth + sDay;
			System.out.println(aDate);
			list.add(aDate);
		});
		return list;
	}

	/**
	 * 根据传入的日期格式返回星期几 .
	 * 
	 * @author 沈天意.
	 */
	public static int dayForWeek(Date currentDate) {
		Calendar c = Calendar.getInstance();
		c.setTime(currentDate);
		int dayForWeek = 0;
		if (c.get(Calendar.DAY_OF_WEEK) == 1) {
			dayForWeek = 7;
		} else {
			dayForWeek = c.get(Calendar.DAY_OF_WEEK) - 1;
		}
		return dayForWeek;
	}

	/**
	 * 13位时间戳转date时间类型
	 */
	public static Date timeStamp2Date(String time) {
		Long timeLong = Long.parseLong(time);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 要转换的时间格式
		Date date;
		try {
			date = sdf.parse(sdf.format(timeLong));
			return date;
		} catch (ParseException e) {
			throw new InnerException("时间解析异常", e);
		}
	}

	public static String timeAdd(String timeStr1, String timeStr2) {
		String[] str1 = timeStr1.split(":");
		String[] str2 = timeStr2.split(":");
		String hourStr1 = str1[0];
		String minStr1 = str1[1];
		String hourStr2 = str2[0];
		String minStr2 = str2[1];
		Integer hour = Integer.valueOf(hourStr1) + Integer.valueOf(hourStr2);
		Integer min = Integer.valueOf(minStr1) + Integer.valueOf(minStr2);
		if ((Integer.valueOf(minStr1) + Integer.valueOf(minStr2)) > 60) {
			hour = hour + 1;
			min = min - 60;
		}
		if (min < 10) {
			return hour.toString() + ":0" + min.toString();
		}
		return hour.toString() + ":" + min.toString();
	}

	/**
	 * 获取精确到秒的时间戳(10位)
	 */
	public static long getSecondTimestamp(Date date) {
		if (null == date) {
			return 0;
		}
		String timestamp = String.valueOf(date.getTime() / 1000);
		return Long.valueOf(timestamp);
	}

	/**
	 * 获取精确到秒的时间戳(13位)
	 */
	public static long getMinSecondTimestamp(Date date) {
		if (null == date) {
			return 0;
		}
		String timestamp = String.valueOf(date.getTime());
		return Long.valueOf(timestamp);
	}

	/**
	 * 获取当前时间精确到秒的时间戳(10位)
	 */
	public static long getCurrSecondTimestamp() {
		return getSecondTimestamp(new Date());
	}

	/**
	 * 获取当前时间精确到毫秒的时间戳(13位)
	 */
	public static long getCurrMinSecondTimestamp() {
		return getMinSecondTimestamp(new Date());
	}

	/**
	 * 将精确到秒的时间戳转为日期
	 * 
	 * @param secondTimestamp 到秒的时间戳
	 */
	public static Date getDateBySecondTimestamp(long secondTimestamp) {
		return new Date(secondTimestamp * 1000);
	}

	/**
	 * LocalDateTime转换为Date
	 */
	public static Date localDateTime2Date(LocalDateTime localDateTime) {
		ZoneId zoneId = ZoneId.systemDefault();
		Instant instant = localDateTime.atZone(zoneId).toInstant();
		return Date.from(instant);
	}

	/**
	 * LocalDate转换为Date
	 */
	public static Date localDate2Date(LocalDate localDate) {
		ZoneId zoneId = ZoneId.systemDefault();
		Instant instant = localDate.atStartOfDay().atZone(zoneId).toInstant();
		return Date.from(instant);
	}

	/**
	 * Date转换为LocalDate
	 */
	public static LocalDate date2LocalDate(Date date) {
		return TimeUtil.date2LocalDateTime(date).toLocalDate();
	}

	/**
	 * Date转换为LocalDateTime
	 */
	public static LocalDateTime date2LocalDateTime(Date date) {
		Instant instant = date.toInstant();
		ZoneId zoneId = ZoneId.systemDefault();
		return instant.atZone(zoneId).toLocalDateTime();
	}

	/**
	 * 获取当前日期(线程安全)
	 */
	public static Date now() {
		LocalDateTime localDateTime = LocalDateTime.now();
		return localDateTime2Date(localDateTime);
	}

	/**
	 * 比较两个日期之间的时间间隔
	 * 
	 * @param unit 时间间隔类型
	 */
	public static long between(Date startTime, Date endTime, TimeUnit unit) {
		LocalDateTime startLocalDateTime = date2LocalDateTime(startTime);
		LocalDateTime endLocalDateTime = date2LocalDateTime(endTime);
		Duration duration = Duration.between(startLocalDateTime, endLocalDateTime);

		long betweenCount = 0;
		if (TimeUnit.DAYS.equals(unit)) { // 天数
			betweenCount = duration.toDays();
		}
		if (TimeUnit.HOURS.equals(unit)) { // 小时
			betweenCount = duration.toHours();
		}
		if (TimeUnit.MINUTES.equals(unit)) { // 分钟
			betweenCount = duration.toMinutes();
		}
		if (TimeUnit.MILLISECONDS.equals(unit)) { // 毫秒
			betweenCount = duration.toMillis();
		}
		return betweenCount;
	}

	/**
	 * 比较两个日期之间的分钟数
	 */
	public static long betweenToMinutes(Date startTime, Date endTime) {
		return between(startTime, endTime, TimeUnit.MINUTES);
	}

	/**
	 * 比较两个日期之间的天数
	 */
	public static long betweenToDays(Date startTime, Date endTime) {
		return between(startTime, endTime, TimeUnit.DAYS);
	}

	/**
	 * 将日期为空的，设置为当前日期
	 */
	public static Date setNullToNow(Date date) {
		if (date == null) {
			return now();
		}
		return date;
	}

}
