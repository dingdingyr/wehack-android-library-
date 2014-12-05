package cn.wehax.library.base;

import android.view.View;
import android.widget.CompoundButton;


/**
 * Created by Terry on 14/11/30.
 * mail: zhichangterry@gmail.com
 * QQ: 1090035354
 * 可用与底部tab或顶部tab
 */
public interface ITabView {


    /**
     * 获取当前选择的tab索引
     */
    public int getCurrentTabIndex();

    /**
     * 获取所有tab数目
     */
    public int getTabCount();


    /**
     * 根据索引切换tab
     */
    public void switchTab(int tabIndex);

    /**
     * 添加Fragment
     */
    public void addCenterFragment(BaseFragment fragment);

    /**
     * 加载tab布局
     */
    public void setupTabLayout(int resLayoutId, final OnTabInflateComplete callback);

    /**
     * 根据索引获取Fragment
     */
    public BaseFragment getCenterFragmentAtIndex(int index);

    /**
     * tab布局加载完后回调
     */
    public interface OnTabInflateComplete {
        public void setupClickListener(View inflatedView, View.OnClickListener listener);
        public void setupCheckChangeListener(View inflatedView,CompoundButton.OnCheckedChangeListener listener);
    }
}
