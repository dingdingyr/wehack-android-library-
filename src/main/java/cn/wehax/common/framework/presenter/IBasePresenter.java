package cn.wehax.common.framework.presenter;

import cn.wehax.common.framework.view.IBaseView;

/**
 * Created by Terry on 14/12/17.
 * mail: zhichangterry@gmail.com
 * QQ: 1090035354
 */
public interface IBasePresenter<T extends IBaseView> {

    void setView(T view);

}
