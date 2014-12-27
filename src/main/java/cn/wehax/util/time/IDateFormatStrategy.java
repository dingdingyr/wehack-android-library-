package cn.wehax.util.time;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public interface IDateFormatStrategy {
    /**
     * 格式化日期
     *
     * @param millisecond 日期
     * @return
     */
	String formatTime(long millisecond);
}
