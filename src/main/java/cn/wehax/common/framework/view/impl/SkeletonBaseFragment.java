package cn.wehax.common.framework.view.impl;

import android.app.Activity;
import android.app.ProgressDialog;
import android.widget.Toast;

import cn.wehax.common.framework.view.IBaseView;
import roboguice.fragment.RoboFragment;

/**
 * Created by Terry on 14/11/30.
 * mail: zhichangterry@gmail.com
 * QQ: 1090035354
 */
public class SkeletonBaseFragment extends RoboFragment implements IBaseView {

    private ProgressDialog mDialog;

    @Override
    final public void showErrorMessage(String err) {
        if (getActivity() != null && !getActivity().isFinishing())
            Toast.makeText(getActivity(), err, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showErrorMessage(int idRes) {
        if (getActivity() != null && !getActivity().isFinishing())
            Toast.makeText(getActivity(), idRes, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showWaitingDialog(String msg) {
        //TODO:
        if (mDialog == null) {
            mDialog = ProgressDialog.show(getActivity(), null, msg);
            mDialog.setCanceledOnTouchOutside(false);
            mDialog.setCancelable(false);
        } else {
            mDialog.setMessage(msg);
            mDialog.show();
        }
    }

    @Override
    public void showWaitingDialog(int idRes) {
        if(isActivityAlive())
            showWaitingDialog(getActivity().getString(idRes));
    }

    @Override
    public void hideWaitingDialog() {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }

    }

    @Override
    public Activity getActivityContext() {
        return getActivity();
    }

    @Override
    public boolean isActivityAlive() {
        if(getActivity() == null || getActivity().isFinishing()) {
            return false;
        }
        return true;
    }


}
