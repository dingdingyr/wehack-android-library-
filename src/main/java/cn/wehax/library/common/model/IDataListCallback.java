package cn.wehax.library.common.model;

import java.util.List;

/**
 * Created by Terry on 14/12/17.
 * mail: zhichangterry@gmail.com
 * QQ: 1090035354
 */
public interface IDataListCallback<T extends IBaseBean> {
    void onDataListReturn(List<T> dataList, int currentPage, int totalPage);
    void onError(ErrorBean error);
}
