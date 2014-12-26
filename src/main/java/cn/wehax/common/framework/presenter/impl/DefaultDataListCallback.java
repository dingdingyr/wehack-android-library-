package cn.wehax.common.framework.presenter.impl;

import java.util.List;

import cn.wehax.common.framework.model.ErrorBean;
import cn.wehax.common.framework.model.IBaseBean;
import cn.wehax.common.framework.model.IDataListCallback;

/**
 * Created by Terry on 14/12/24.
 * mail: zhichangterry@gmail.com
 * QQ: 1090035354
 */
public class DefaultDataListCallback<BB extends IBaseBean> implements IDataListCallback<BB> {

    private SkeletonNoLoginListPresenter<?, BB, ?> mPresenter;

    public DefaultDataListCallback(SkeletonNoLoginListPresenter<?, BB, ?> presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onDataListReturn(List<BB> dataList, int currentPage, int totalPage) {
        mPresenter.currentPage = currentPage;
        mPresenter.totalPage = totalPage;

        //如果请求是第一页清除列表
        if (currentPage == 1) {
            mPresenter.mData.clear();
        }

        mPresenter.mData.addAll(dataList);
        if(mPresenter.isFirstDataLoad) {
            mPresenter.afterFirstDataLoaded(dataList);
            mPresenter.isFirstDataLoad = false;
        }else{
            mPresenter.afterLaterDataLoaded(dataList);
        }
        mPresenter.refreshView();
    }

    @Override
    public void onError(ErrorBean error) {
        mPresenter.mView.showErrorMessage(error.getDescription());
    }
}
