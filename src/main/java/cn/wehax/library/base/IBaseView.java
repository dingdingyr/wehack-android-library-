package cn.wehax.library.base;

/**
 * Created by macuser on 14/12/5.
 */
public interface IBaseView {
    /**
     * 显示加载中界面
     */
    void showLoadingView();

    /**
     * 显示内容页
     */
    void showContentView();

    /**
     * 显示重新加载页
     */
    void showReloadView();
}
