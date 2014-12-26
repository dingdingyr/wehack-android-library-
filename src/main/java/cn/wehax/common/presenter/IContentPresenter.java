package cn.wehax.common.presenter;

import cn.wehax.common.view.IContentView;
import cn.wehax.common.model.IBaseBean;

/**
 * Created by Terry on 14/12/19.
 * mail: zhichangterry@gmail.com
 * QQ: 1090035354
 */
public interface IContentPresenter<T extends IContentView, BB extends IBaseBean> extends IBasePresenter<T> {

    void startLoadPage(String... params);

    void refreshPage();


}
