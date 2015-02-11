package cn.wehax.util.time;


public interface IDateFormatStrategy {
    /**
     * 格式化日期
     *
     * @param millisecond 日期对应
     * @return
     */
	String formatDate(long millisecond);
}
