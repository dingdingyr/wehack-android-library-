package cn.wehax.library.common.model;

/**
 * Created by mayuhan on 14/12/17.
 */
public interface IResultCallback {
    void onSuccess(ResultBean result);
    void onError(ErrorBean error);
}
