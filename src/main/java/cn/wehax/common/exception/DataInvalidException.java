package cn.wehax.common.exception;

/**
 * 无效数据异常
 * Created by dss on 2014/12/5
 */
public class DataInvalidException extends Exception {

    public DataInvalidException(String detailMessage) {
        super(detailMessage);
    }

}
