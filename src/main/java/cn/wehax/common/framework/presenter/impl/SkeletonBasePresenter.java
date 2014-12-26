package cn.wehax.common.framework.presenter.impl;

import com.google.inject.Inject;

import cn.wehax.common.framework.presenter.IBasePresenter;
import cn.wehax.common.framework.view.IBaseView;
import cn.wehax.common.image.IImageManager;
import cn.wehax.common.image.ImageManager;

/**
 * Created by Terry on 14/12/17.
 * mail: zhichangterry@gmail.com
 * QQ: 1090035354
 */
public abstract class SkeletonBasePresenter<T extends IBaseView> implements IBasePresenter<T> {

    protected T mView;

    @Inject
    ImageManager imageManager;

    @Override
    public void setView(T view) {
        this.mView = view;
    }

    public IImageManager getImageManager(){
        return imageManager;
    }

}