package cn.wehax.common.framework.presenter.impl;

import cn.wehax.common.framework.model.ErrorBean;
import cn.wehax.common.framework.model.IBaseBean;
import cn.wehax.common.framework.model.IDataCallback;

/**
 * Created by Terry on 14/12/24.
 * mail: zhichangterry@gmail.com
 * QQ: 1090035354
 */
public class DefaultDataCallback<BB extends IBaseBean> implements IDataCallback<BB> {

    private SkeletonNoLoginSinglePresenter<?, BB> mPresenter;

    public DefaultDataCallback(SkeletonNoLoginSinglePresenter<?, BB> presenter) {
        mPresenter = presenter;
    }


    @Override
    public void onDataReturn(BB data) {
        mPresenter.mData = data;
        if(mPresenter.isFirstDataLoad) {
            mPresenter.afterFirstDataLoaded(data);
            mPresenter.isFirstDataLoad = false;
        }else{
            mPresenter.afterLaterDataLoaded(data);
        }
        mPresenter.bindView(data);

    }

    @Override
    public void onError(ErrorBean error) {
        mPresenter.mView.showErrorMessage(error.getDescription());
    }
}