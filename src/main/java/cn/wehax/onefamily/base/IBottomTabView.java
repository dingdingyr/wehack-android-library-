package cn.wehax.onefamily.base;

import android.support.v4.app.Fragment;
import android.view.View;

import cn.wehax.onefamily.base.BaseFragment;


/**
 * Created by Terry on 14/11/30.
 * mail: zhichangterry@gmail.com
 * QQ: 1090035354
 */
public interface IBottomTabView {


    public int getCurrentTabIndex();

    public int getTotalTabIndex();


    public void switchTab(int tabIndex);

    public void addCenterFragment(BaseFragment fragment);

    public void setupBottomLayout(int resLayoutId, final OnBottomInflateComplete callback);

    public BaseFragment getCenterFragmentAtIndex(int index);

    public interface OnBottomInflateComplete{
        public void setupClickListener(View inflatedView, View.OnClickListener listener);
    }
}
