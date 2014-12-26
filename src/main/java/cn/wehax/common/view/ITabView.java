package cn.wehax.common.view;

import android.support.v4.app.Fragment;



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
     * @param fragment
     */
    public void addCenterFragment(Fragment fragment);


    /**
     * 根据索引获取Fragment
     */
    public Fragment getCenterFragmentAtIndex(int index);

}
