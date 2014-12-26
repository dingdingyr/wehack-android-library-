package cn.wehax.common.framework.presenter;

import cn.wehax.common.framework.view.ISingleView;
import cn.wehax.common.framework.model.IBaseBean;

/**
 * Created by Terry on 14/12/24.
 * mail: zhichangterry@gmail.com
 * QQ: 1090035354
 */
public interface ISinglePresenter<V extends ISingleView, BB extends IBaseBean> extends IContentPresenter<V, BB> {
}
