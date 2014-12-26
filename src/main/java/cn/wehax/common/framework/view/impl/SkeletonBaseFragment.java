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
    public void showWaitingMessage(String msg) {
        //TODO:
    }

    @Override
    public void hideWaitingMessage() {
        //TODO:

    }

}
