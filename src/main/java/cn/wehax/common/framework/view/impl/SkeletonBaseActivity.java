package cn.wehax.common.framework.view.impl;

import android.widget.Toast;

import cn.wehax.common.framework.view.IBaseView;
import roboguice.activity.RoboFragmentActivity;

/**
 * Created by Terry on 14/11/30.
 * mail: zhichangterry@gmail.com
 * QQ: 1090035354
 */
public abstract class SkeletonBaseActivity extends RoboFragmentActivity implements IBaseView {


    @Override
    final public void showErrorMessage(String err) {
        Toast.makeText(this, err, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showWaitingDialog(String msg) {
        //TODO:

    }

    @Override
    public void hideWaitingDialog() {
        //TODO:

    }


}
