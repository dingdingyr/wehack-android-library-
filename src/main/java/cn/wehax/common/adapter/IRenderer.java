package cn.wehax.common.adapter;

import android.view.View;
import android.view.ViewGroup;

import cn.wehax.common.presenter.IBasePresenter;
import cn.wehax.common.model.IBaseBean;

/**
 * Created by Terry on 14/12/17.
 * mail: zhichangterry@gmail.com
 * QQ: 1090035354
 */
public interface IRenderer<T extends IBaseBean> {
    /**
     * 将列表指定项视图与数据进行绑定
     *
     * @param i
     * @param data
     * @param view
     * @param viewGroup
     * @return
     */
    View bind(int i, T data, View view, ViewGroup viewGroup);

    /**
     * delegate对应Adapter的getItemViewType方法，提供对应位置的视图。
     * @param data
     * @param position
     * @return
     */
    int getItemViewType(T data, int position);

    /**
     * delegate对应Adapter的getItemViewType方法，提供总视图的种类。
     */

    int getViewTypeCount();

    void setPresenter(IBasePresenter<?> presenter);
}
