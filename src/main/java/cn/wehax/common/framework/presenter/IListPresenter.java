package cn.wehax.common.framework.presenter;

import cn.wehax.common.framework.view.IListView;
import cn.wehax.common.framework.model.IBaseBean;

/**
 * Created by Terry on 14/12/17.
 * mail: zhichangterry@gmail.com
 * QQ: 1090035354
 */
public interface IListPresenter<T extends IListView, BB extends IBaseBean> extends IContentPresenter<T, BB> {

    void onItemClick(int i);

    void loadDataAtPage(int strategy, int pageIndex);


}
