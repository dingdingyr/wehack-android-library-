package cn.wehax.common.framework.presenter;

import cn.wehax.common.framework.view.IContentView;
import cn.wehax.common.framework.model.IBaseBean;

/**
 * Created by Terry on 14/12/19.
 * mail: zhichangterry@gmail.com
 * QQ: 1090035354
 */
public interface IContentPresenter<T extends IContentView, BB extends IBaseBean> extends IBasePresenter<T> {

    void startLoadPage(String... params);

    void refreshData();


}
