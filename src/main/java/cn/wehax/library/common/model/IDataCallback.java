package cn.wehax.library.common.model;

/**
 * Created by Terry on 14/12/17.
 * mail: zhichangterry@gmail.com
 * QQ: 1090035354
 */
public interface IDataCallback<T extends IBaseBean> {
        void onDataReturn(T data);
        void onError(ErrorBean error);
}
