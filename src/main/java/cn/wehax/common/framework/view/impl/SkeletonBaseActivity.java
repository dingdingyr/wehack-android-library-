package cn.wehax.common.framework.view.impl;

import android.app.Activity;
import android.app.ProgressDialog;
import android.view.MotionEvent;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.umeng.analytics.MobclickAgent;

import cn.wehax.common.framework.view.IBaseView;
import roboguice.activity.RoboFragmentActivity;

/**
 * Created by Terry on 14/11/30.
 * mail: zhichangterry@gmail.com
 * QQ: 1090035354
 */
public abstract class SkeletonBaseActivity extends RoboFragmentActivity implements IBaseView {


    private ProgressDialog mDialog;

    @Override
    final public void showErrorMessage(String err) {
        Toast.makeText(this, err, Toast.LENGTH_SHORT).show();
    }

    @Override
    final public void showErrorMessage(int idRes) {
        Toast.makeText(this, idRes, Toast.LENGTH_SHORT).show();
    }

    @Override
    final public void showWaitingDialog(int idRes) {
        showWaitingDialog(getResources().getString(idRes));
    }

    @Override
    public void showWaitingDialog(String msg) {
        //TODO:
        if (mDialog == null) {
            mDialog = ProgressDialog.show(this, null, msg);
            mDialog.setCanceledOnTouchOutside(false);
            mDialog.setCancelable(false);
        } else {
            mDialog.setMessage(msg);
            mDialog.show();
        }

    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        try {
            InputMethodManager im = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(getCurrentFocus().getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return super.onTouchEvent(event);
        }
    }

    @Override
    public void hideWaitingDialog() {
        //TODO:
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    public Activity getActivityContext() {
        return this;
    }

    @Override
    public boolean isActivityAlive() {
        if(this.isFinishing()) {
            return false;
        }
        return true;
    }
}
