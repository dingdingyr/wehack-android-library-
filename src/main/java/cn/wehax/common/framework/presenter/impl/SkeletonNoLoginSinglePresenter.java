package cn.wehax.common.framework.presenter.impl;

import cn.wehax.common.framework.data.DataStrategy;
import cn.wehax.common.framework.presenter.IContentPresenter;
import cn.wehax.common.framework.view.ISingleView;
import cn.wehax.common.framework.model.IBaseBean;

/**
 * Created by Terry on 14/12/19.
 * mail: zhichangterry@gmail.com
 * QQ: 1090035354
 */
public abstract class SkeletonNoLoginSinglePresenter<VI extends ISingleView, BB extends IBaseBean> extends SkeletonBasePresenter<VI> implements IContentPresenter<VI, BB> {

    protected BB mData;
    protected boolean isFirstDataLoad;

    protected abstract void bindView(BB data);

    @Override
    public final void startLoadPage(String... params) {

        onReceiveParams(params);
        beforeFirstLoadData();
        loadSingleData(getStartStrategy());
    }

    protected void beforeFirstLoadData() {
        mView.showLoadingView();
    }

    protected void afterFirstDataLoaded(BB data) {
        mView.showContentView();

    }

    protected void afterLaterDataLoaded(BB data) {

    }

    @Override
    public final void refreshPage() {
        loadSingleData(DataStrategy.CACHE_POLICY_NETWORK_ELSE_CACHE);
    }


    public abstract void loadSingleData(int strategy);

    protected abstract int getStartStrategy();

    protected abstract void onReceiveParams(String... params);

}
