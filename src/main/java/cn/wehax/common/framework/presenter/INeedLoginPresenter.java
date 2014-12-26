package cn.wehax.common.framework.presenter;

import cn.wehax.common.framework.view.IBaseView;

/**
 * Created by Terry on 14/12/24.
 * mail: zhichangterry@gmail.com
 * QQ: 1090035354
 */
public interface INeedLoginPresenter<V extends IBaseView> extends IBasePresenter<V> {
    void onClickLogin(int requestCode);

    void startWithCheckLogin(String... params);
}
