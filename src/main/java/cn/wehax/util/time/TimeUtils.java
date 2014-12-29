package cn.wehax.util.time;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 时间相关辅助工具
 */
public class TimeUtils {
    public static final long MILLIS_IN_A_DAY = 24 * 60 * 60 * 1000; // 一天的毫秒数

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
        return sdf.format(new Date(milliseconds.longValue()));
    }

    /**
     * 获取格式化时间
     *
     * @param milliseconds
     * @param strategy 格式化策略
     * @return
     */
    public static String getFormatTime(long milliseconds, IDateFormatStrategy strategy) {
        if(strategy == null)
            return null;

        return strategy.formatTime(milliseconds);
    }

    /**
     * 计算两个日期之间相差多少天
     */
    public static int getDateDifference(long millis1, long millis2){
        long millisFormer;
        long millisLatter;

        // 保证millisFormer保存大时间，millisLatter保存小时间
        if(millis1 >= millis2){
            millisFormer = millis1;
            millisLatter = millis2;
        }else{
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
        return (int)((calFormer.getTimeInMillis() - calLatter.getTimeInMillis()) / TimeUtils.MILLIS_IN_A_DAY);
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