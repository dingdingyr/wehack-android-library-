package cn.wehax.common.framework.adapter.impl;

import cn.wehax.common.framework.adapter.IRenderer;
import cn.wehax.common.framework.presenter.IBasePresenter;
import cn.wehax.common.framework.presenter.impl.SkeletonBasePresenter;
import cn.wehax.common.framework.model.IBaseBean;

/**
 * Created by Terry on 14/12/14.
 * mail: zhichangterry@gmail.com
 * QQ: 1090035354
 */
public abstract class SkeletonRenderer<T extends IBaseBean> implements IRenderer<T> {

    protected SkeletonBasePresenter<?> mPresenter;

    @Override
    public int getItemViewType(T data, int position) {
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public void setPresenter(IBasePresenter<?> presenter) {
        this.mPresenter = (SkeletonBasePresenter<?>)presenter;
    }
}
