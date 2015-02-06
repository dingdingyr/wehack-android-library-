package cn.wehax.common.framework.view.impl;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.wehax.common.R;
import cn.wehax.common.framework.view.IListView;
import roboguice.util.Ln;

/**
 * Created by Terry on 14/12/19.
 * mail: zhichangterry@gmail.com
 * QQ: 1090035354
 */
public abstract class SkeletonNoLoginListFragment extends SkeletonBaseFragment implements IListView {
    View loadingView;

    View reloadView;

    View contentView;


    protected abstract void onReloadData();
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        View view = getView();
        loadingView = view.findViewById(R.id.load_view_loading);
        reloadView = view.findViewById(R.id.load_view_reload);
        contentView = view.findViewById(R.id.load_view_content);
        if(reloadView != null){
            reloadView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onReloadData();
                }
            });
        }
    }

    @Override
    public void showLoadingView() {

        if(loadingView!= null){
            loadingView .setVisibility(View.VISIBLE);
        }
        if(reloadView != null){
            reloadView.setVisibility(View.GONE);
        }

        if(contentView != null){
            contentView.setVisibility(View.GONE);
        }
    }

    @Override
    public void showContentView() {
        if(loadingView!= null){
            loadingView .setVisibility(View.GONE);
        }
        if(reloadView != null){
            reloadView.setVisibility(View.GONE);
        }

        if(contentView != null){
            contentView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void showReloadView() {
        if(loadingView!= null){
            loadingView .setVisibility(View.GONE);
        }
        if(reloadView != null){
            reloadView.setVisibility(View.VISIBLE);
        }

        if(contentView != null){
            contentView.setVisibility(View.GONE);
        }
    }
}
