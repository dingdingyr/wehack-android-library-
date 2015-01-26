package cn.wehax.common.framework.presenter.impl;

import java.util.ArrayList;
import java.util.List;

import cn.wehax.common.framework.adapter.GenericAdapter;
import cn.wehax.common.framework.adapter.IRenderer;
import cn.wehax.common.framework.data.DataStrategy;
import cn.wehax.common.framework.presenter.IListPresenter;
import cn.wehax.common.framework.view.IListView;
import cn.wehax.common.framework.model.IBaseBean;

import static cn.wehax.common.exception.Assertion.assertThat;

/**
 * Created by Terry on 14/12/17.
 * mail: zhichangterry@gmail.com
 * QQ: 1090035354
 */
public abstract class SkeletonNoLoginListPresenter<VI extends IListView, BB extends IBaseBean, RD extends IRenderer<BB>> extends SkeletonBasePresenter<VI> implements IListPresenter<VI, BB> {

    protected GenericAdapter<BB> mAdapter;
    protected List<BB> mData;
    protected VI mView;
    protected Class<RD> renderClazz;

    protected RD render;

    protected int currentPage = 0;
    protected int totalPage = -1;
    protected boolean isFirstDataLoad;

    public SkeletonNoLoginListPresenter(Class<RD> clazz) {
        this.renderClazz = clazz;
        mData = new ArrayList<>();
        try {
            render = renderClazz.newInstance();
            render.setPresenter(this);
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        assertThat(render != null);
    }

    @Override
    public final void setView(VI view) {
        mView = view;
        mAdapter = new GenericAdapter<>(mData, render);
        view.setAdapter(mAdapter);
    }

    protected void beforeFirstLoadData() {
        mView.showLoadingView();

    }

    protected void afterFirstDataLoaded(List<BB> data) {
        mView.showContentView();

    }

    protected void afterLaterDataLoaded(List<BB> data) {

    }


    public final void startLoadPage(String... params) {
        onReceiveParams(params);
        beforeFirstLoadData();
        loadDataAtPage(getStartStrategy(), 1);
    }

    public final void refreshPage() {
        loadDataAtPage(DataStrategy.CACHE_POLICY_NETWORK_ONLY, 1);
    }

    protected final void refreshView() {
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 加载数据
     *
     * @param strategy  数据返回策略
     * @param pageIndex
     */
    public abstract void loadDataAtPage(int strategy, int pageIndex);

    /**
     * 返回数据读取策略
     *
     * @return
     */
    protected abstract int getStartStrategy();

    protected abstract void onReceiveParams(String... params);


}
