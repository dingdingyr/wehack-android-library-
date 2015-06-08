package cn.wehax.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 时间相关辅助工具
 */
public class TimeUtils {
    /**
     * 一天的毫秒数
     */
    public static final long MILLIS_IN_A_DAY = 24 * 60 * 60 * 1000;
    /**
     * 1h=3600s
     */
    private final static int SECOND_IN_HOUR = 3600;
    /**
     * 1m=60s
     */
    private final static int SECOND_IN_MINUTE = 60;
    /**
     * 1h=60m
     */
    private final static int MINUTE_IN_HOUR = 60;

    // 若干日期格式化样式
    public static final String PATTERN_Y_SLASH_M_SLASH_D_H_COMMA_M = "yyyy/MM/dd HH:mm";
    public static final String PATTERN_Y_SLASH_M_SLASH_D = "yyyy/MM/dd";
    public static final String PATTERN_Y_DASH_M_DASH_D = "yyyy-MM-dd";
    public static final String PATTERN_D_SLASH_M_SLASH_Y = "dd/MM/yyyy";

    /**
     * 获取格式化日期
     */
    public static String getFormatDate(Long milliseconds, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);

        return sdf.format(new Date(milliseconds));
    }

    public static String getFormatDate(String milliseconds, String pattern) {
        return getFormatDate(Long.valueOf(milliseconds), pattern);
    }

    /**
     * 获取格式化时间
     *
     * @param milliseconds 日期
     * @param strategy     格式化策略
     * @return
     */
    public static String getFormatDate(long milliseconds, IDateFormatStrategy strategy) {
        if (strategy == null)
            return null;

        return strategy.formatDate(milliseconds);
    }

    /**
     * 格式化视频录制时间（时间格式：hh:mm:ss'）
     *
     * @param seconds 视频录制时间（单位秒）
     * @return
     */
    public static String getFormatRecordTime(long seconds) {
        long hh = seconds / SECOND_IN_HOUR;
        long mm = seconds % SECOND_IN_HOUR;
        long ss = mm % SECOND_IN_MINUTE;
        mm = mm / SECOND_IN_MINUTE;

        return (mm == 0 ? "" : (mm < 10 ? "0" + mm : mm) + ":")
                + (ss == 0 ? "" : (ss < 10 ? "0" + ss : ss) + "'");
    }

    /**
     * 格式化倒计时时间（时间格式：hh:mm:ss'）
     *
     * @param milliseconds 倒计时时间（单位毫秒）
     * @return
     */
    public static String getFormatCountDownTime(long milliseconds) {
        if (milliseconds < 0) {
            milliseconds = 0;
        }

        long seconds = milliseconds / 1000;
        long hh = seconds / SECOND_IN_HOUR;
        long mm = seconds % SECOND_IN_HOUR;
        long ss = mm % SECOND_IN_MINUTE;
        mm = mm / SECOND_IN_MINUTE;

        String time;
        if (hh > 0) {
            time = String.format("%02d:%02d:%02d", hh, mm, ss);
        } else if (mm > 0) {
            time = String.format("%02d:%02d", mm, ss);
        } else {
            time = String.format("%02d", ss);
        }

        return time;
    }

    /**
     * 计算两个日期之间相差天数
     *
     * @param millis1 日期1
     * @param millis2 日期2
     * @return
     */
    public static int getDateDifference(long millis1, long millis2) {
        long millisFormer;
        long millisLatter;

        // 保证millisFormer保存大时间，millisLatter保存小时间
        if (millis1 >= millis2) {
            millisFormer = millis1;
            millisLatter = millis2;
        } else {
            millisFormer = millis2;
            millisLatter = millis1;
        }

    	/*
         *  因为昨天与今天两个日期之间可能相差几秒，可能相差几十小时
    	 *  比如：2014/12/25 23:59:50 和  2014/12/26 00:00:01
    	 *  比如：2014/12/25 00:00:01 和  2014/12/26 23:59:50
    	 *  因此，由日期生成的Calendar对象必须将时、分、秒参数清0，否则将影响最终计算结果
    	 */
        Calendar calFormer = Calendar.getInstance();
        calFormer.setTime(new Date(millisFormer));
        calFormer.set(Calendar.HOUR_OF_DAY, 0);
        calFormer.set(Calendar.MINUTE, 0);
        calFormer.set(Calendar.SECOND, 0);

        Calendar calLatter = Calendar.getInstance();
        calLatter.setTime(new Date(millisLatter));
        calLatter.set(Calendar.HOUR_OF_DAY, 0);
        calLatter.set(Calendar.MINUTE, 0);
        calLatter.set(Calendar.SECOND, 0);

        // 计算两个日期相差天数
        return (int) ((calFormer.getTimeInMillis() - calLatter.getTimeInMillis()) / TimeUtils.MILLIS_IN_A_DAY);
    }

    /**
     * 计算指定日期是星期几
     *
     * @param milliseconds 日期
     */
    public static String getWeek(long milliseconds) {
        String Week = "星期";

        Calendar c = Calendar.getInstance();
        c.setTime(new Date(milliseconds));

        if (c.get(Calendar.DAY_OF_WEEK) == 1) {
            Week += "天";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 2) {
            Week += "一";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 3) {
            Week += "二";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 4) {
            Week += "三";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 5) {
            Week += "四";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 6) {
            Week += "五";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 7) {
            Week += "六";
        }

        return Week;
    }

    public interface IDateFormatStrategy {
        /**
         * 格式化日期
         *
         * @param millisecond 日期对应
         * @return
         */
        String formatDate(long millisecond);
    }

}
