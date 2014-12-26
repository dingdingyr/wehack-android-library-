package cn.wehax.common.presenter;

import cn.wehax.common.view.ISingleView;
import cn.wehax.common.model.IBaseBean;

/**
 * Created by Terry on 14/12/24.
 * mail: zhichangterry@gmail.com
 * QQ: 1090035354
 */
public interface ISinglePresenter<V extends ISingleView, BB extends IBaseBean> extends IContentPresenter<V, BB> {
}
