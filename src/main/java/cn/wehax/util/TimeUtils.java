package cn.wehax.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 时间相关辅助工具
 */
public class TimeUtils {
    public static final String PATTERN_Y_SLASH_M_SLASH_D_H_COMMA_M = "yyyy/MM/dd HH:mm";
    public static final String PATTERN_Y_SLASH_M_SLASH_D = "yyyy/MM/dd";
    public static final String PATTERN_Y_DASH_M_DASH_D = "yyyy-MM-dd";
    public static final String PATTERN_D_SLASH_M_SLASH_Y = "dd/MM/yyyy";

    /**
     * 获取格式化时间
     */
    public static String getFormatTime(String milliseconds, String pattern) {
        return getFormatTime(Long.valueOf(milliseconds), pattern);
    }

    /**
     * 获取格式化时间
     */
    public static String getFormatTime(Long milliseconds, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(new Date(milliseconds));
    }

    /**
     * 判断指定日期是星期几
     *
     * @param timeMillis 设置的需要判断的时间（毫秒格式）
     *                   <p/>
     *                   示例：getWeek(System.currentTimeMillis())
     */
    public static String getWeek(long timeMillis) {

        String Week = "星期";

        Calendar c = Calendar.getInstance();
        c.setTime(new Date(timeMillis));

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
}
