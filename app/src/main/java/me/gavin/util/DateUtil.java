package me.gavin.util;

import android.annotation.SuppressLint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * 时间工具类
 *
 * @author xiaoleilu
 */
@SuppressLint("SimpleDateFormat")
public class DateUtil {

    /**
     * 毫秒
     */
    public static final long MS = 1;
    /**
     * 每秒钟的毫秒数
     */
    public static final long SECOND_MS = MS * 1000;
    /**
     * 每分钟的毫秒数
     */
    public static final long MINUTE_MS = SECOND_MS * 60;
    /**
     * 每小时的毫秒数
     */
    public static final long HOUR_MS = MINUTE_MS * 60;
    /**
     * 每天的毫秒数
     */
    public static final long DAY_MS = HOUR_MS * 24;

    /**
     * 标准日期格式
     */
    public static final String NORM_DATE_PATTERN = "yyyy-MM-dd";
    /**
     * 标准时间格式
     */
    public static final String NORM_TIME_PATTERN = "HH:mm:ss";
    /**
     * 标准日期时间格式
     */
    public static final String NORM_DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    /**
     * HTTP头中日期时间格式
     */
    public static final String HTTP_DATETIME_PATTERN = "EEE, dd MMM yyyy HH:mm:ss z";

    /**
     * 标准日期（不含时间）格式化器
     */
    private static final SimpleDateFormat NORM_DATE_FORMAT = new SimpleDateFormat(NORM_DATE_PATTERN);
    /**
     * 标准时间格式化器
     */
    private static final SimpleDateFormat NORM_TIME_FORMAT = new SimpleDateFormat(NORM_TIME_PATTERN);
    /**
     * 标准日期时间格式化器
     */
    private static final SimpleDateFormat NORM_DATETIME_FORMAT = new SimpleDateFormat(NORM_DATETIME_PATTERN);
    /**
     * HTTP日期时间格式化器
     */
    private static final SimpleDateFormat HTTP_DATETIME_FORMAT = new SimpleDateFormat(HTTP_DATETIME_PATTERN, Locale.US);

    /**
     * 当前时间，格式 yyyy-MM-dd HH:mm:ss
     *
     * @return 当前时间的标准形式字符串
     */
    public static String now() {
        return formatDateTime(new Date());
    }

    /**
     * 当前日期，格式 yyyy-MM-dd
     *
     * @return 当前日期的标准形式字符串
     */
    public static String today() {
        return formatDate(new Date());
    }

    // ------------------------------------ Format start ----------------------------------------------

    /**
     * 根据特定格式格式化日期
     *
     * @param date   被格式化的日期
     * @param format 格式
     * @return 格式化后的字符串
     */
    public static String format(Date date, String format) {
        return new SimpleDateFormat(format).format(date);
    }

    /**
     * 格式 yyyy-MM-dd HH:mm:ss
     *
     * @param date 被格式化的日期
     * @return 格式化后的日期
     */
    public static String formatDateTime(Date date) {
//		return format(d, "yyyy-MM-dd HH:mm:ss");
        return NORM_DATETIME_FORMAT.format(date);
    }

    /**
     * 格式化为Http的标准日期格式
     *
     * @param date 被格式化的日期
     * @return HTTP标准形式日期字符串
     */
    public static String formatHttpDate(Date date) {
//		return new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US).format(date);
        return HTTP_DATETIME_FORMAT.format(date);
    }

    /**
     * 格式 yyyy-MM-dd
     *
     * @param date 被格式化的日期
     * @return 格式化后的字符串
     */
    public static String formatDate(Date date) {
//		return format(d, "yyyy-MM-dd");
        return NORM_DATE_FORMAT.format(date);
    }
    // ------------------------------------ Format end ----------------------------------------------

    // ------------------------------------ Parse start ----------------------------------------------

    /**
     * 将特定格式的日期转换为Date对象
     *
     * @param dateString 特定格式的日期
     * @param format     格式，例如yyyy-MM-dd
     * @return 日期对象
     */
    public static Date parse(String dateString, String format) {
        try {
            return (new SimpleDateFormat(format)).parse(dateString);
        } catch (ParseException e) {
            L.e("Parse " + dateString + " with format " + format + " error!", e);
        }
        return null;
    }

    /**
     * 格式yyyy-MM-dd HH:mm:ss
     *
     * @param dateString 标准形式的时间字符串
     * @return 日期对象
     */
    public static Date parseDateTime(String dateString) {
//		return parse(s, "yyyy-MM-dd HH:mm:ss");
        try {
            return NORM_DATETIME_FORMAT.parse(dateString);
        } catch (ParseException e) {
            L.e("Parse " + dateString + " with format " + NORM_DATETIME_FORMAT.toPattern() + " error!", e);
        }
        return null;
    }

    /**
     * 格式yyyy-MM-dd
     *
     * @param dateString 标准形式的日期字符串
     * @return 日期对象
     */
    public static Date parseDate(String dateString) {
        try {
            return NORM_DATE_FORMAT.parse(dateString);
        } catch (ParseException e) {
            L.e("Parse " + dateString + " with format " + NORM_DATE_PATTERN + " error!", e);
        }
        return null;
    }

    /**
     * 格式HH:mm:ss
     *
     * @param timeString 标准形式的日期字符串
     * @return 日期对象
     */
    public static Date parseTime(String timeString) {
        try {
            return NORM_TIME_FORMAT.parse(timeString);
        } catch (ParseException e) {
            L.e("Parse " + timeString + " with format " + NORM_TIME_PATTERN + " error!", e);
        }
        return null;
    }

    /**
     * 格式：<br>
     * 1、yyyy-MM-dd HH:mm:ss<br>
     * 2、yyyy-MM-dd<br>
     * 3、HH:mm:ss>
     *
     * @param dateStr 日期字符串
     * @return 日期
     */
    public static Date parse(String dateStr) {
        int length = dateStr.length();
        try {
            if (length == DateUtil.NORM_DATETIME_PATTERN.length()) {
                return parseDateTime(dateStr);
            } else if (length == DateUtil.NORM_DATE_PATTERN.length()) {
                return parseDate(dateStr);
            } else if (length == DateUtil.NORM_TIME_PATTERN.length()) {
                return parseTime(dateStr);
            }
        } catch (Exception e) {
            L.e("Parse " + dateStr + " with format normal error!", e);
        }
        return null;
    }
    // ------------------------------------ Parse end ----------------------------------------------

    // ------------------------------------ Offset start ----------------------------------------------

    /**
     * 昨天
     *
     * @return 昨天
     */
    public static Date yesterday() {
        return offsetDate(new Date(), Calendar.DAY_OF_YEAR, -1);
    }

    /**
     * 上周
     *
     * @return 上周
     */
    public static Date lastWeek() {
        return offsetDate(new Date(), Calendar.WEEK_OF_YEAR, -1);
    }

    /**
     * 上个月
     *
     * @return 上个月
     */
    public static Date lastMouth() {
        return offsetDate(new Date(), Calendar.MONTH, -1);
    }

    /**
     * 获取指定日期偏移指定时间后的时间
     *
     * @param date          基准日期
     * @param calendarField 偏移的粒度大小（小时、天、月等）使用Calendar中的常数
     * @param offsite       偏移量，正数为向后偏移，负数为向前偏移
     * @return 偏移后的日期
     */
    public static Date offsetDate(Date date, int calendarField, int offsite) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(calendarField, offsite);
        return cal.getTime();
    }
    // ------------------------------------ Offset end ----------------------------------------------

    // ------------------------------------ Field start ----------------------------------------------

    public static int getYear(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.YEAR);
    }

    /**
     * 获取月份 从零开始
     */
    public static int getMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.MONTH);
    }

    public static int getDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    public static int getHour(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    public static int getMinute(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.MINUTE);
    }

    public static int getSecond(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.SECOND);
    }

    public static long getMillis(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.getTimeInMillis();
    }

    // ------------------------------------ Field end ----------------------------------------------

    /**
     * 判断两个日期相差的时长<br/>
     * 返回 minuend - subtrahend 的差
     *
     * @param subtrahend 减数日期
     * @param minuend    被减数日期
     * @param diffField  相差的选项：相差的天、小时
     * @return 日期差
     */
    public static long diff(Date subtrahend, Date minuend, long diffField) {
        long diff = minuend.getTime() - subtrahend.getTime();
        return diff / diffField;
    }

    /**
     * 计时，常用于记录某段代码的执行时间，单位：纳秒
     *
     * @param preTime 之前记录的时间
     * @return 时间差，纳秒
     */
    public static long spendNt(long preTime) {
        return System.nanoTime() - preTime;
    }

    /**
     * 计时，常用于记录某段代码的执行时间，单位：毫秒
     *
     * @param preTime 之前记录的时间
     * @return 时间差，毫秒
     */
    public static long spendMs(long preTime) {
        return System.currentTimeMillis() - preTime;
    }


    /**
     * 计算年龄 ( month 从 0 开始)
     */
    public static int getAge(int year, int month, int day) {
        Calendar birthday = new GregorianCalendar(year, month, day);
        Calendar now = Calendar.getInstance();
        int dd = now.get(Calendar.DAY_OF_MONTH) - birthday.get(Calendar.DAY_OF_MONTH);
        int dm = now.get(Calendar.MONTH) - birthday.get(Calendar.MONTH);
        int dy = now.get(Calendar.YEAR) - birthday.get(Calendar.YEAR);
        //按照减法原理，先day相减，不够向month借；然后month相减，不够向year借；最后year相减。
        if (dd < 0) {
            dm -= 1;
            now.add(Calendar.MONTH, -1);//得到上一个月，用来得到上个月的天数。
            dd = dd + now.getActualMaximum(Calendar.DAY_OF_MONTH);
        }
        if (dm < 0) {
            dm = (dm + 12) % 12;
            dy--;
        }
        L.v("年龄：" + dy + "年" + dm + "月" + dd + "天");
        return dy;
    }

    /**
     * 友好的时间显示
     *
     * @param dataTimeString @yyyy-MM-dd'T'HH:mm:ss.SSSZ 2017-04-28T09:20:05.077
     * @return
     */
    public static String getFriendlyTime(String dataTimeString) {
        try {
            Date date = parse(dataTimeString, "yyyy-MM-dd'T'HH:mm:ss.SSS");
            Calendar time = Calendar.getInstance();
            time.setTime(date);
            Calendar now = Calendar.getInstance();

            // 小于一分钟显示 刚刚
            if (now.getTimeInMillis() - time.getTimeInMillis() < MINUTE_MS)
                return "刚刚";

            // 小于一小时显示 x分钟前
            if (now.getTimeInMillis() - time.getTimeInMillis() < HOUR_MS)
                return (now.getTimeInMillis() - time.getTimeInMillis()) / MINUTE_MS + " 分钟前";

            // 小于一天显示 x小时前
            if (now.getTimeInMillis() - time.getTimeInMillis() < DAY_MS)
                return (now.getTimeInMillis() - time.getTimeInMillis()) / HOUR_MS + " 小时前";

            String dayStr = (now.getTimeInMillis() - time.getTimeInMillis()) / DAY_MS + " 天前";

            int dd = now.get(Calendar.DAY_OF_MONTH) - time.get(Calendar.DAY_OF_MONTH);
            int dm = now.get(Calendar.MONTH) - time.get(Calendar.MONTH);
            int dy = now.get(Calendar.YEAR) - time.get(Calendar.YEAR);
            //按照减法原理，先day相减，不够向month借；然后month相减，不够向year借；最后year相减。
            if (dd < 0) {
                dm -= 1;
                now.add(Calendar.MONTH, -1);//得到上一个月，用来得到上个月的天数。
            }
            if (dm < 0) {
                dm = (dm + 12) % 12;
                dy--;
            }

            // 大于十二个月显示 x年前
            if (dy > 0)
                return dy + " 年前";

            // 大于一个月显示 x个月前
            if (dm > 0) {
                return dm + " 个月前";
            }

            // 大于一天显示 x天前
            return dayStr;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "喵喵喵";
    }
}