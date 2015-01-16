package cn.wehax.common.framework.view.impl;

import android.widget.Toast;

import cn.wehax.common.framework.view.IBaseView;
import roboguice.fragment.RoboFragment;

/**
 * Created by Terry on 14/11/30.
 * mail: zhichangterry@gmail.com
 * QQ: 1090035354
 */
public class SkeletonBaseFragment extends RoboFragment implements IBaseView {
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
    }

    @Override
    public void showWaitingDialog(int idRes) {

    }

    @Override
    public void hideWaitingDialog() {
        //TODO:

    }




}
